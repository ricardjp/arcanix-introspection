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

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class Property {
	
	public static final String NESTED = ".";
	public static final String NESTED_REGEX = "\\.";
	
	public static final String INDEXED_START = "[";
	public static final String INDEXED_END = "]";
	
	public static final String MAPPED_START = "(";
	public static final String MAPPED_END = ")";
	
	private final String value;
	private final String name;
	private final Integer index;
	private final String key;
	private final Property nextProperty;
	
	private Property(final PropertyBuilder propertyBuilder) {		
		this.value = propertyBuilder.getValue();
		this.name = propertyBuilder.getName();
		this.index = propertyBuilder.getIndex();
		this.key = propertyBuilder.getKey();		
		this.nextProperty = propertyBuilder.getNextProperty();
	}

	public String getValue() {
		return this.value;
	}
	
	public String getName() {
		return this.name;
	}

	public Integer getIndex() {
		return this.index;
	}

	public String getKey() {
		return this.key;
	}

	public boolean isMapped() {
		return this.key != null;
	}

	public boolean isIndexed() {
		return this.index != null;
	}
	
	public Property getNextProperty() {
		return this.nextProperty;
	}
	
	public String toExpression() {
		Property nextProperty = this;
		StringBuilder stringBuilder = new StringBuilder();
		
		boolean skip = false;
		do {
			if (!skip) {
				stringBuilder.append(nextProperty.toString());
				if (nextProperty.isMapped() || nextProperty.isIndexed()) {
					skip = true;
				}
				if (!skip && nextProperty.getNextProperty() != null) {
					stringBuilder.append(Property.NESTED);
				}
			} else {
				skip = false;
			}
			nextProperty = nextProperty.getNextProperty();
		}
		while (nextProperty != null);
		
		return stringBuilder.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		if (this.name != null) {
			stringBuilder.append(this.name);
		}
		if (isIndexed()) {
			stringBuilder.append(INDEXED_START).append(this.index).append(INDEXED_END);
		}
		if (isMapped()) {
			stringBuilder.append(MAPPED_START).append(this.key).append(MAPPED_END);
		}
		return stringBuilder.toString();
	}
	
	public static final class PropertyBuilder {
		
		private String value;
		private String name;
		private String key;
		private Integer index;
		private Property nextProperty;
		
		public String getValue() {
			return this.value;
		}
		
		public PropertyBuilder setValue(String value) {
			this.value = value;
			return this;
		}
		
		public String getName() {
			return this.name;
		}
		
		public PropertyBuilder setName(String name) {
			this.name = name;
			return this;
		}
		
		public String getKey() {
			return this.key;
		}
		
		public PropertyBuilder setKey(String key) {
			this.key = key;
			return this;
		}
		
		public Integer getIndex() {
			return this.index;
		}
		
		public PropertyBuilder setIndex(Integer index) {
			this.index = index;
			return this;
		}
		
		public Property getNextProperty() {
			return this.nextProperty;
		}
		
		public PropertyBuilder setNextProperty(Property nextProperty) {
			this.nextProperty = nextProperty;
			return this;
		}
		
		public Property build() {
			return new Property(this);
		}
		
	}
	
}
