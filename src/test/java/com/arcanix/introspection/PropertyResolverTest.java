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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 *
 */
public class PropertyResolverTest {

	private PropertyResolver resolver;
	
	@Before
	public void setup() {
		this.resolver = new PropertyResolver();
	}
	
	@Test
	public void testSimpleProperty() {
		Property property = this.resolver.resolve("message", "Hello World!");
		
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals(-1, property.getIndex());
		assertNull(property.getKey());
		assertEquals("message", property.getName());
		assertEquals("Hello World!", property.getValue());
		assertNull(property.getPreviousProperty());
		assertNull(property.getNextProperty());
	}
	
	@Test
	public void testMappedProperty() {
		Property property = this.resolver.resolve("translations(fr)", "propriété");
		
		// reference to map
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals(-1, property.getIndex());
		assertNull(property.getKey());
		assertEquals("translations", property.getName());
		assertEquals("propriété", property.getValue());
		assertNull(property.getPreviousProperty());
		
		// simple property to set in map
		Property nextProperty = property.getNextProperty();
		
		assertNotNull(nextProperty);
		assertFalse(nextProperty.isIndexed());
		assertTrue(nextProperty.isMapped());
		assertEquals(-1, nextProperty.getIndex());
		assertEquals("fr", nextProperty.getKey());
		assertEquals("translations", nextProperty.getName());
		assertEquals("propriété", nextProperty.getValue());
	}
	
	@Test
	public void testIndexedProperty() {
		Property property = this.resolver.resolve("components[0]", "component #1");
		
		// reference to list
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals(-1, property.getIndex());
		assertNull(property.getKey());
		assertEquals("components", property.getName());
		assertEquals("component #1", property.getValue());
		assertNull(property.getPreviousProperty());
		
		// simple property to set in list
		Property nextProperty = property.getNextProperty();
		assertNotNull(nextProperty);
		assertTrue(nextProperty.isIndexed());
		assertFalse(nextProperty.isMapped());
		assertEquals(0, nextProperty.getIndex());
		assertNull(nextProperty.getKey());
		assertEquals("components", nextProperty.getName());
		assertEquals("component #1", nextProperty.getValue());
	}
	
	@Test
	public void testListOfMapProperty() {
		PropertyResolver propertyResolver = new PropertyResolver();
		Property property = propertyResolver.resolve("components[1](id)", "test");
		
		// list property
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals("components", property.getName());
		
		// map property inserted at list index=1
		assertNotNull(property.getNextProperty());
		Property mapProperty = property.getNextProperty();
		assertTrue(mapProperty.isIndexed());
		assertEquals(1, mapProperty.getIndex());
		
		// into map property
		assertNotNull(property.getNextProperty().getNextProperty());
		Property intoMapProperty = mapProperty.getNextProperty();
		assertTrue(intoMapProperty.isMapped());
		assertEquals("id", intoMapProperty.getKey());
		
		// ensure no more property
		assertNull(property.getNextProperty().getNextProperty().getNextProperty());
	}
	
}
