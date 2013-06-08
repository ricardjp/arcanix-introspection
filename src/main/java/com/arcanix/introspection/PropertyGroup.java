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

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public class PropertyGroup implements Iterable<Property> {

	private final LinkedList<Property> properties = new LinkedList<>();
	
	@Override
	public Iterator<Property> iterator() {
		return this.properties.iterator();
	}
	
	public void addProperty(Property property) {
		this.properties.add(property);
	}
	
	public Property getPrevious(Property property) {
		int index = this.properties.indexOf(property);
		if (index > 0) {
			return this.properties.get(index - 1);
		}
		return null;
	}
	
	public Property getNext(Property property) {
		int index = this.properties.indexOf(property);
		if (index < this.properties.size() - 1) {
			return this.properties.get(index + 1);
		}
		return null;
	}
}
