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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import com.arcanix.convert.ConversionException;
import com.arcanix.convert.Converters;
import com.arcanix.introspection.BeanInfoWrapper;
import com.arcanix.introspection.Property;
import com.arcanix.introspection.util.ReflectionUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class BeanWrapper extends AbstractWrapper implements PropertyWrapper {

	private final BeanInfoWrapper beanInfoWrapper;
	private final Object bean;
	private final Class<?> targetClass;
	
	public BeanWrapper(final Object initialValue, final Type type, final Converters converters) throws IntrospectionException, IllegalAccessException, InstantiationException {
		super(converters);
		
		this.targetClass = ReflectionUtils.getClass(type);
		if (initialValue != null) {
			this.bean = initialValue;
		} else {
			this.bean = this.targetClass.newInstance();
		}
		this.beanInfoWrapper = new BeanInfoWrapper(Introspector.getBeanInfo(ReflectionUtils.getClass(type)));
	}
	
	@Override
	public Class<?> getTargetClass() {
		return this.targetClass;
	}
	
	public Type getPropertyType(final Property property) throws NoSuchMethodException {
		return this.beanInfoWrapper.getPropertyDescriptor(property.getName()).getReadMethod().getGenericReturnType();
	}
	
	public void setLocalProperty(final Property property) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ConversionException {
		PropertyDescriptor propertyDescriptor = this.beanInfoWrapper.getPropertyDescriptor(property.getName());
		Class<?> expectedPropertyType = ReflectionUtils.getClass(getPropertyType(property));
		propertyDescriptor.getWriteMethod().invoke(this.bean, getConverters().convert(expectedPropertyType, property.getValue()));
	}
	
	public void setLocalProperty(final Property property, final PropertyWrapper propertyWrapper) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		PropertyDescriptor propertyDescriptor = this.beanInfoWrapper.getPropertyDescriptor(property.getName());
		propertyDescriptor.getWriteMethod().invoke(this.bean, propertyWrapper.getResult());		
	}
	
	public Object getValue(final Property property) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		PropertyDescriptor propertyDescriptor = this.beanInfoWrapper.getPropertyDescriptor(property.getName());
		return propertyDescriptor.getReadMethod().invoke(this.bean);
	}
	
	@Override
	public Object getResult() {
		return this.bean;
	}
	
}
