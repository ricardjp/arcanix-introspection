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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.arcanix.convert.ConversionException;
import com.arcanix.convert.Converters;
import com.arcanix.introspection.Property;
import com.arcanix.introspection.util.ReflectionUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 *
 */
public class MapWrapper extends AbstractWrapper implements PropertyWrapper {

	private final Map<Object, Object> map;
	
	private final Class<?> keyType;
	private final Type valueType;
	private final Class<?> valueElementType;
	
	@SuppressWarnings("unchecked")
	public MapWrapper(final Object initialValue, final Type type, final Converters converters) {
		super(converters);
		
		if (!(type instanceof ParameterizedType)) {
			throw new IllegalArgumentException("Map must be parameterized");
		}
		
		if (initialValue == null) {
			this.map = new HashMap<>();
		} else {
			this.map = (Map<Object, Object>) initialValue;
		}
		
		ParameterizedType parameterizedType = (ParameterizedType) type;
		this.keyType = ReflectionUtils.getClass(parameterizedType.getActualTypeArguments()[0]);
		this.valueType = parameterizedType.getActualTypeArguments()[1];
		this.valueElementType = ReflectionUtils.getClass(parameterizedType.getActualTypeArguments()[1]);
	}
	
	@Override
	public Object getResult() {
		return this.map;
	}
	
	@Override
	public Class<?> getTargetClass() {
		return Map.class;
	}
	
	@Override
	public Type getPropertyType(final Property property) {
		return this.valueType;
	}
	
	public void setLocalProperty(final Property property) throws ConversionException {
		String key = null;
		if (property.isMapped()) {
			key = property.getKey(); 
		} else {
			key = property.getName();
		}
		this.map.put(
			getConverters().convert(this.keyType, key),
			getConverters().convert(this.valueElementType, property.getValue()));
	}
	
	@Override
	public void setLocalProperty(
			final Property property,
			final PropertyWrapper propertyWrapper) throws ConversionException {
		
		if (!property.isMapped()) {
			throw new IllegalArgumentException("Property must be mapped");
		}
		this.map.put(getConverters().convert(this.keyType, property.getKey()), propertyWrapper.getResult());
	}
	
	@Override
	public Object getValue(final Property property) throws ConversionException {
		if (!property.isMapped()) {
			throw new IllegalArgumentException("Property must be mapped");
		}
		return this.map.get(getConverters().convert(this.keyType, property.getKey()));
	}
	
}
