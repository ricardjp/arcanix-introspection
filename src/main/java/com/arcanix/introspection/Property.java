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

import com.arcanix.introspection.PropertyResolver.PropertyBuilder;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class Property {
	
	private final String value;
	private final String name;
	private final int index;
	private final String key;
	private final boolean mapped;
	private final boolean indexed;
	private final Property nextProperty;
	
	Property(final PropertyBuilder propertyBuilder) {
		this.value = propertyBuilder.getValue();
		this.name = propertyBuilder.getName();
		this.index = propertyBuilder.getIndex();
		this.key = propertyBuilder.getKey();
		this.indexed = propertyBuilder.isIndexed();
		this.mapped = propertyBuilder.isMapped();
		this.nextProperty = propertyBuilder.getNextProperty();
	}

	public String getValue() {
		return this.value;
	}
	
	public String getName() {
		return this.name;
	}

	public int getIndex() {
		return this.index;
	}

	public String getKey() {
		return this.key;
	}

	public boolean isMapped() {
		return this.mapped;
	}

	public boolean isIndexed() {
		return this.indexed;
	}
	
	public Property getNextProperty() {
		return this.nextProperty;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("name=").append(this.name).append(", ")
			.append("index=").append(this.index).append(", ")
			.append("key=").append(this.key).append(", ")
			.append("value=").append(this.value)
			.toString();
	}
	
}
