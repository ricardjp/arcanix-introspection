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
import java.util.ArrayList;
import java.util.List;

import com.arcanix.convert.ConversionException;
import com.arcanix.convert.Converters;
import com.arcanix.introspection.Property;
import com.arcanix.introspection.util.ReflectionUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public class ListWrapper extends AbstractWrapper {

	private final List<Object> list;
	
	private final Type type;
	private final Class<?> elementType;
	
	@SuppressWarnings("unchecked")
	public ListWrapper(final Object initialValue, final Type type, final Converters converters) {
		super(converters);
		
		if (initialValue != null) {
			this.list = (List<Object>) initialValue;
		} else {
			this.list = new ArrayList<>();
		}
		
		if (!(type instanceof ParameterizedType)) {
			throw new IllegalArgumentException("List must be parameterized");
		}
		
		final ParameterizedType parameterizedType = (ParameterizedType) type;
		this.type = parameterizedType.getActualTypeArguments()[0];
		this.elementType = ReflectionUtils.getClass(parameterizedType.getActualTypeArguments()[0]);
	}
	
	@Override
	public Class<?> getTargetClass() {
		return List.class;
	}
	
	@Override
	public Type getPropertyType(final Property property) {
		return this.type;
	}
	
	@Override
	public Object getResult() {
		return this.list;
	}
	
	public void setLocalProperty(final Property property) throws ConversionException {
		if (this.elementType.getClass() == Class.class) {
			this.list.add(getConverters().convert((Class<?>) this.elementType, property.getValue()));
		} else {
			throw new IllegalStateException("Cannot set local property of this container");
		}
	}
	
	@Override
	public void setLocalProperty(final Property property, final PropertyWrapper propertyWrapper) {
		if (property.getIndex() < 0 || property.getIndex() >= this.list.size()) {
			this.list.add(property.getIndex(), propertyWrapper.getResult());
		} else {
			this.list.set(property.getIndex(), propertyWrapper.getResult());
		}
	}

	@Override
	public Object getValue(final Property property) {
		if (!property.isIndexed()) {
			throw new IllegalArgumentException("Property must be indexed");
		}
		if (property.getIndex() < 0 || property.getIndex() >= this.list.size()) {
			return null;
		}
		return this.list.get(property.getIndex());
	}
	
}
