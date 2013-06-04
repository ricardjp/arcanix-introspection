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
package com.arcanix.introspection.wrapper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arcanix.convert.Converters;
import com.arcanix.introspection.util.ReflectionUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PropertyWrapperFactory {

	public static PropertyWrapper getPropertyWrapper(
			final Object initialValue,
			final Type type,
			final Converters converters) {
		
		Class<?> clazz = ReflectionUtils.getClass(type);
		if (clazz.isAssignableFrom(List.class)) {
			return new ListWrapper(initialValue, type, converters);
		} else if (clazz.isAssignableFrom(Set.class)) {
			return new SetWrapper(initialValue, type, converters);
		} else if (clazz.isAssignableFrom(Map.class)) {
			return new MapWrapper(initialValue, type, converters);
		} else {
			return new BeanWrapper(initialValue, type, converters);
		}
	}
	
	public static boolean isWrapperType(final Type type) {
		Class<?> clazz = ReflectionUtils.getClass(type);
		if (List.class.isAssignableFrom(clazz)
				|| Set.class.isAssignableFrom(clazz)
				|| Map.class.isAssignableFrom(clazz)) {
		
			return true;
		}
		return false;
	}
	
}
