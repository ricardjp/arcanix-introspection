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

import java.util.Iterator;
import java.util.LinkedList;

import com.arcanix.introspection.Property.PropertyBuilder;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public class PropertyList {

	private final LinkedList<PropertyBuilder> properties = new LinkedList<>();
	
	public void add(PropertyBuilder propertyBuilder) {
		this.properties.add(propertyBuilder);
	}
	
	public Property build() {
		PropertyBuilder next = null;
		Property first = null;
		
		Iterator<PropertyBuilder> reverseIterator = this.properties.descendingIterator();
		while (reverseIterator.hasNext()) {
			PropertyBuilder property = reverseIterator.next();
			if (next != null) {
				property.setNextProperty(next.build());
			}
			
			next = property;
			first = property.build();
		}

		return first;
	}
	
}
