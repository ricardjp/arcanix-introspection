/**
 * Copyright (C) 2013 Jean-Philippe Ricard.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arcanix.introspection;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.arcanix.introspection.Property.PropertyBuilder;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PropertyResolver {
	
	private static final String INDEXED_PROPERTY_PATTERN =
			"\\" + Property.INDEXED_START + "\\d+" + "\\" + Property.INDEXED_END;
	
	private static final String MAPPED_PROPERTY_PATTERN =
			"\\" + Property.MAPPED_START + "(.*)?" + "\\" + Property.MAPPED_END;
	
	private static final String COLLECTION_PROPERTY_PATTERN =
			INDEXED_PROPERTY_PATTERN + "|" + MAPPED_PROPERTY_PATTERN;
	
	private boolean isIndexed(final String property) {
		if (property == null || property.length() == 0) {
			return false;
		}
		return (property.matches("^(.*)?" + INDEXED_PROPERTY_PATTERN + "$"));
	}
	
	public boolean isMapped(final String property) {
		if (property == null || property.length() == 0) {
			return false;
		}
		return (property.matches("^(.*)" + MAPPED_PROPERTY_PATTERN + "$"));		
	}
	
	public Integer getIndex(final String property) {
		if (isIndexed(property)) {
			return Integer.valueOf(property.substring(
					property.indexOf(Property.INDEXED_START) + 1,
					property.indexOf(Property.INDEXED_END)));
		}
		return null;		
	}
	
	public String getKey(final String property) {
		if (isMapped(property)) {
			return property.substring(
					property.indexOf(Property.MAPPED_START) + 1,
					property.indexOf(Property.MAPPED_END));
		}
		return null;
	}
	
	public String getProperty(final String property) {
		if (property == null) {
			return null;
		}
		
		String propertyName = property;
		propertyName = propertyName.replaceAll(INDEXED_PROPERTY_PATTERN, "");
		propertyName = propertyName.replaceAll(MAPPED_PROPERTY_PATTERN, "");
		return propertyName;
	}
	
	public Property resolve(final String nestedProperty, final String value) {
		PropertyList properties = new PropertyList();
		
		for (String token : nestedProperty.split(Property.NESTED_REGEX)) {
			String name = getProperty(token);
			properties.add(new PropertyBuilder().setValue(value).setName(name));
			buildCollectionProperties(properties, token, name, value);
		}
		
		return properties.build();
	}
	
	private void buildCollectionProperties(PropertyList properties, String token, String name, String value) {
		for (String collectionToken : getCollectionTokens(token)) {				
			properties.add(buildPropertyFromCollectionToken(collectionToken, name, value));
		}
	}
	
	private List<String> getCollectionTokens(String token) {
		Pattern collectionPattern = Pattern.compile(COLLECTION_PROPERTY_PATTERN);
		Matcher matcher = collectionPattern.matcher(token);
		
		List<String> collectionTokens = new ArrayList<>(); 
		
		while (matcher.find()) {
			collectionTokens.add(matcher.group(0));
		}
		return collectionTokens;
	}
	
	private PropertyBuilder buildPropertyFromCollectionToken(String collectionToken, String name, String value) {
		PropertyBuilder propertyBuilder = new PropertyBuilder().setValue(value).setName(name);
		if (isIndexed(collectionToken)) {
			propertyBuilder.setIndex(getIndex(collectionToken));
		} else if (isMapped(collectionToken)) {
			propertyBuilder.setKey(getKey(collectionToken));
		}
		return propertyBuilder;
	}
	
}
