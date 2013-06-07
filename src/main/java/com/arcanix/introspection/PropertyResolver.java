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

import java.util.LinkedList;
import java.util.StringTokenizer;

import com.arcanix.introspection.Property.PropertyBuilder;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PropertyResolver {
	
	private static final String INDEXED_PROPERTY_PATTERN =
			"\\" + Property.INDEXED_START + "\\d+" + "\\" + Property.INDEXED_END;
	
	private static final String MAPPED_PROPERTY_PATTERN =
			"\\" + Property.MAPPED_START + "(.*)?" + "\\" + Property.MAPPED_END;
	
	public boolean isIndexed(final String property) {
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
					property.indexOf(Property.INDEXED_START) + 1, property.indexOf(Property.INDEXED_END)));
		}
		return -1;		
	}
	
	public String getKey(final String property) {
		if (isMapped(property)) {
			return property.substring(property.indexOf(Property.MAPPED_START) + 1, property.indexOf(Property.MAPPED_END));
		}
		return null;
	}
	
	public String getProperty(final String property) {
		String propertyName = property;
		if (isIndexed(property)) {
			propertyName = propertyName.replaceAll(INDEXED_PROPERTY_PATTERN, "");
		}
		if (isMapped(property)) {
			propertyName = propertyName.replaceAll(MAPPED_PROPERTY_PATTERN, "");
		}
		return propertyName;
	}
	
	public Property resolve(final String property) {
		PropertyBuilder propertyBuilder = new PropertyBuilder();
		propertyBuilder.setName(getProperty(property));
		if (isIndexed(property)) {
			propertyBuilder.setIndex(getIndex(property));
			propertyBuilder.setIndexed(true);
		}
		if (isMapped(property)) {
			propertyBuilder.setKey(getKey(property));
			propertyBuilder.setMapped(true);
		}
		return propertyBuilder.build();
	}
	
	public Property resolveNestedProperty(final String nestedProperty, final String value) {
		
		LinkedList<PropertyBuilder> properties = new LinkedList<>();

		StringTokenizer tokenizer = new StringTokenizer(nestedProperty, Property.NESTED);
		
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			PropertyBuilder propertyBuilder = new PropertyBuilder();
			if (!properties.isEmpty()) {
				propertyBuilder.setPreviousProperty(properties.getLast());
			}
			propertyBuilder.setValue(value);
			propertyBuilder.setName(getProperty(token));
			properties.add(propertyBuilder);
			
			if (isIndexed(token)) {

				propertyBuilder.setIndex(getIndex(token));
				
				PropertyBuilder nextPropertyBuilder = new PropertyBuilder();
				nextPropertyBuilder.setPreviousProperty(propertyBuilder);
				nextPropertyBuilder.setValue(value);
				nextPropertyBuilder.setName(getProperty(token));
				nextPropertyBuilder.setIndex(getIndex(token));
				
				properties.add(nextPropertyBuilder);
			}
			if (isMapped(token)) {
				
				propertyBuilder.setKey(getKey(token));
				
				PropertyBuilder nextPropertyBuilder = new PropertyBuilder();
				nextPropertyBuilder.setPreviousProperty(propertyBuilder);
				nextPropertyBuilder.setValue(value);
				nextPropertyBuilder.setName(getProperty(token));
				nextPropertyBuilder.setKey(getKey(token));
				
				properties.add(nextPropertyBuilder);
			}
			
		}
		
		if (!properties.isEmpty()) {
			return properties.getFirst().build();	
		}
		return null;
	}
	
}
