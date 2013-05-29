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
import java.util.HashSet;
import java.util.Set;

import com.arcanix.convert.ConversionException;
import com.arcanix.convert.Converters;
import com.arcanix.introspection.Property;
import com.arcanix.introspection.util.ReflectionUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 *
 */
public class SetWrapper extends AbstractWrapper {

	private final Set<Object> set;
	
	private final Type type;
	private final Class<?> elementType;
	
	@SuppressWarnings("unchecked")
	public SetWrapper(final Object initialValue, final Type type, final Converters converters) {
		super(converters);
		
		if (initialValue != null) {
			this.set = (Set<Object>) initialValue;
		} else {
			this.set = new HashSet<>();
		}
		
		if (!(type instanceof ParameterizedType)) {
			throw new IllegalArgumentException("Set must be parameterized");
		}
		
		final ParameterizedType parameterizedType = (ParameterizedType) type;
		this.type = parameterizedType.getActualTypeArguments()[0];
		this.elementType = ReflectionUtils.getClass(parameterizedType.getActualTypeArguments()[0]);
	}
	
	@Override
	public Object getResult() {
		return this.set;
	}
	
	@Override
	public Class<?> getTargetClass() {
		return this.elementType;
	}
	
	@Override
	public Type getPropertyType(final Property property) throws NoSuchMethodException {
		return this.type;
	}
	
	public void setLocalProperty(final Property property) throws ConversionException {
		if (this.elementType.getClass() == Class.class) {
			this.set.add(getConverters().convert(this.elementType, property.getValue()));
		} else {
			throw new IllegalStateException("Cannot set local property of this container");
		}
	}
	
	@Override
	public void setLocalProperty(final Property property, final PropertyWrapper propertyWrapper) {
		this.set.add(propertyWrapper.getResult());
	}
	
	@Override
	public Object getValue(final Property property) throws ConversionException {
		Object converted = getConverters().convert(this.elementType, property.getValue());
		
		// only to get a specific instance from a set is to loop through elements
		// TODO define own Set implementation ?
		for (Object element : this.set) {
			if (element.equals(converted)) {
				return element;
			}
		}
		return null;
	}
	
}
