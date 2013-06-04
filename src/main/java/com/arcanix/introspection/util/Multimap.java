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
package com.arcanix.introspection.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class Multimap {

	private Map<String, List<String>> delegate = new LinkedHashMap<>();
	
	public boolean containsKey(final Object key) {
		return this.delegate.containsKey(key);
	}
	
	public Collection<Map.Entry<String, String>> entries() {
		List<Map.Entry<String, String>> entries = new ArrayList<>();
		for (Map.Entry<String, List<String>> delegateEntry : this.delegate.entrySet()) {
			for (String value : delegateEntry.getValue()) {
				entries.add(new MultimapEntry(delegateEntry.getKey(), value));
			}
		}
		return entries;
	}
	
	public void put(final String key, final String value) {
		List<String> values = this.delegate.get(key);
		if (values == null) {
			values = new ArrayList<>();
			this.delegate.put(key, values);
		}
		values.add(value);
	}
	
	private static final class MultimapEntry implements Map.Entry<String, String> {

		private String key;
		private String value;
		
		public MultimapEntry(final String key, final String value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public String getKey() {
			return this.key;
		}

		@Override
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String setValue(String value) {
			throw new RuntimeException("Unsupported operation");
		}

		@Override
		public String toString() {
			return "{" + this.key + ": " + this.value + "}";
		}
	}
	
	
	
	
	
}
