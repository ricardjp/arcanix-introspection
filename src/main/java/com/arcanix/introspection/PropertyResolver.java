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
import java.util.StringTokenizer;
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
	
	public int getIndex(final String property) {
		if (isIndexed(property)) {
			return Integer.parseInt(property.substring(
					property.indexOf(Property.INDEXED_START) + 1,
					property.indexOf(Property.INDEXED_END)));
		}
		return -1;		
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
	
	private List<String> getCollectionTokens(String token) {
		Pattern collectionPattern = Pattern.compile(COLLECTION_PROPERTY_PATTERN);
		Matcher matcher = collectionPattern.matcher(token);
		
		List<String> collectionTokens = new ArrayList<>(); 
		
		while (matcher.find()) {
			collectionTokens.add(matcher.group(0));
		}
		return collectionTokens;
	}
	
	public Property resolve(final String nestedProperty, final String value) {
		
		PropertyList properties = new PropertyList();

		StringTokenizer tokenizer = new StringTokenizer(nestedProperty, Character.toString(Property.NESTED));
		
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			PropertyBuilder propertyBuilder = new PropertyBuilder();
			propertyBuilder.setValue(value);
			propertyBuilder.setName(getProperty(token));
			properties.add(propertyBuilder);
			
			for (String collectionToken : getCollectionTokens(token)) {
				PropertyBuilder nextPropertyBuilder = new PropertyBuilder();
				nextPropertyBuilder.setValue(value);
				nextPropertyBuilder.setName(getProperty(token));
				
				if (isIndexed(collectionToken)) {
					nextPropertyBuilder.setIndex(getIndex(collectionToken));
				} if (isMapped(collectionToken)) {
					nextPropertyBuilder.setKey(getKey(collectionToken));
				}	
				
				properties.add(nextPropertyBuilder);
				propertyBuilder = nextPropertyBuilder;
			}	
		}
		
		return properties.build();
	}
	
}
