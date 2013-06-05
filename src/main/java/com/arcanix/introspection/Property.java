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
	
	public static final char INDEXED_START = '[';
	public static final char INDEXED_END = ']';
	
	public static final char MAPPED_START = '(';
	public static final char MAPPED_END = ')';
	
	private final String value;
	private final String name;
	private final int index;
	private final String key;
	private final boolean mapped;
	private final boolean indexed;
	private final Property nextProperty;
	private final Property previousProperty;
	
	private Property(final PropertyBuilder propertyBuilder) {
		this.value = propertyBuilder.getValue();
		this.name = propertyBuilder.getName();
		this.index = propertyBuilder.getIndex();
		this.key = propertyBuilder.getKey();
		this.indexed = propertyBuilder.isIndexed();
		this.mapped = propertyBuilder.isMapped();
		if (propertyBuilder.getNextProperty() != null) {
			this.nextProperty = propertyBuilder.getNextProperty().build();
		} else {
			this.nextProperty = null;
		}
		if (propertyBuilder.getPreviousProperty() != null) {
			this.previousProperty = propertyBuilder.getPreviousProperty().build();
		} else {
			this.previousProperty = null;
		}
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
	
	public Property getPreviousProperty() {
		return this.previousProperty;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		if (this.name != null) {
			stringBuilder.append(this.name);
		}
		if (this.indexed) {
			stringBuilder.append(INDEXED_START).append(this.index).append(INDEXED_END);
		}
		if (this.mapped) {
			stringBuilder.append(MAPPED_START).append(this.key).append(MAPPED_END);
		}
		return stringBuilder.toString();
	}
	
	public static final class PropertyBuilder {
		
		private String value;
		private String name;
		private String key;
		private int index = -1;
		private boolean mapped;
		private boolean indexed;
		private PropertyBuilder nextProperty;
		private PropertyBuilder previousProperty;
		
		public String getValue() {
			return this.value;
		}
		
		public PropertyBuilder setValue(final String value) {
			this.value = value;
			return this;
		}
		
		public String getName() {
			return this.name;
		}
		
		public PropertyBuilder setName(final String name) {
			this.name = name;
			return this;
		}
		
		public String getKey() {
			return this.key;
		}
		
		public PropertyBuilder setKey(final String key) {
			this.key = key;
			if (key != null) {
				setMapped(true);
			} else {
				setMapped(false);
			}
			return this;
		}
		
		public int getIndex() {
			return this.index;
		}
		
		public PropertyBuilder setIndex(final int index) {
			this.index = index;
			if (index != -1) {
				setIndexed(true);
			} else {
				setIndexed(false);
			}
			return this;
		}
		
		public boolean isMapped() {
			return this.mapped;
		}
		
		public PropertyBuilder setMapped(final boolean mapped) {
			this.mapped = mapped;
			return this;
		}
		
		public boolean isIndexed() {
			return this.indexed;
		}
		
		public PropertyBuilder setIndexed(final boolean indexed) {
			this.indexed = indexed;
			return this;
		}
		
		public PropertyBuilder getNextProperty() {
			return this.nextProperty;
		}
		
		public PropertyBuilder setNextProperty(final PropertyBuilder nextProperty) {
			this.nextProperty = nextProperty;
			return this;
		}
		
		public PropertyBuilder getPreviousProperty() {
			return this.previousProperty;
		}
		
		public PropertyBuilder setPreviousProperty(final PropertyBuilder previousProperty) {
			this.previousProperty = previousProperty;
			return this;
		}
		
		public Property build() {
			return new Property(this);
		}
		
		@Override
		public String toString() {
			return build().toString();
		}
		
	}
	
}
