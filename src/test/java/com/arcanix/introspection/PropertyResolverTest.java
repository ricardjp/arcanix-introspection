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
		assertNull(property.getNextProperty());
	}
	
	@Test
	public void testMultipleSimpleProperties() {
		Property property = this.resolver.resolve("employee.message", "Hello World!");
		
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals(-1, property.getIndex());
		assertNull(property.getKey());
		assertEquals("employee", property.getName());
		assertEquals("Hello World!", property.getValue());
		
		Property nextProperty = property.getNextProperty();
		assertFalse(nextProperty.isIndexed());
		assertFalse(nextProperty.isMapped());
		assertEquals(-1, nextProperty.getIndex());
		assertNull(nextProperty.getKey());
		assertEquals("message", nextProperty.getName());
		assertEquals("Hello World!", nextProperty.getValue());
		assertNull(nextProperty.getNextProperty());
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
	public void testMappedPropertyOfBean() {
		Property property = this.resolver.resolve("translations(fr).name", "propriété");
		
		// reference to map
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals(-1, property.getIndex());
		assertNull(property.getKey());
		assertEquals("translations", property.getName());
		assertEquals("propriété", property.getValue());
		
		// bean in map
		Property nextProperty = property.getNextProperty();
		
		assertNotNull(nextProperty);
		assertFalse(nextProperty.isIndexed());
		assertTrue(nextProperty.isMapped());
		assertEquals(-1, nextProperty.getIndex());
		assertEquals("fr", nextProperty.getKey());
		assertEquals("translations", nextProperty.getName());
		assertEquals("propriété", nextProperty.getValue());
		
		// bean property
		Property beanProperty = nextProperty.getNextProperty();
		
		assertNotNull(beanProperty);
		assertFalse(beanProperty.isIndexed());
		assertFalse(beanProperty.isMapped());
		assertEquals(-1, beanProperty.getIndex());
		assertNull(beanProperty.getKey());
		assertEquals("name", beanProperty.getName());
		assertEquals("propriété", beanProperty.getValue());
	}
	
	@Test
	public void testBeanMappedProperty() {
		Property property = this.resolver.resolve("dictionary.translations(fr)", "propriété");
		
		// reference to bean
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals(-1, property.getIndex());
		assertNull(property.getKey());
		assertEquals("dictionary", property.getName());
		assertEquals("propriété", property.getValue());
		
		// reference to map
		Property nextProperty = property.getNextProperty();
		
		assertNotNull(nextProperty);
		assertFalse(nextProperty.isIndexed());
		assertFalse(nextProperty.isMapped());
		assertEquals(-1, nextProperty.getIndex());
		assertNull(nextProperty.getKey());
		assertEquals("translations", nextProperty.getName());
		assertEquals("propriété", nextProperty.getValue());
		
		// simple property to set in map
		Property beanProperty = nextProperty.getNextProperty();
		
		assertNotNull(beanProperty);
		assertFalse(beanProperty.isIndexed());
		assertTrue(beanProperty.isMapped());
		assertEquals(-1, beanProperty.getIndex());
		assertEquals("fr", beanProperty.getKey());
		assertEquals("translations", beanProperty.getName());
		assertEquals("propriété", beanProperty.getValue());
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
	public void testIndexedPropertyOfBean() {
		Property property = this.resolver.resolve("components[0].name", "component #1");
		
		// reference to list
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals(-1, property.getIndex());
		assertNull(property.getKey());
		assertEquals("components", property.getName());
		assertEquals("component #1", property.getValue());
		
		// simple property to set in list
		Property nextProperty = property.getNextProperty();
		assertNotNull(nextProperty);
		assertTrue(nextProperty.isIndexed());
		assertFalse(nextProperty.isMapped());
		assertEquals(0, nextProperty.getIndex());
		assertNull(nextProperty.getKey());
		assertEquals("components", nextProperty.getName());
		assertEquals("component #1", nextProperty.getValue());
		
		Property beanProperty = nextProperty.getNextProperty();
		assertNotNull(nextProperty);
		assertFalse(beanProperty.isIndexed());
		assertFalse(beanProperty.isMapped());
		assertEquals(-1, beanProperty.getIndex());
		assertNull(beanProperty.getKey());
		assertEquals("name", beanProperty.getName());
		assertEquals("component #1", beanProperty.getValue());
	}
	
	@Test
	public void testBeanIndexedProperty() {
		Property property = this.resolver.resolve("name.components[0]", "component #1");
		
		// reference to bean
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals(-1, property.getIndex());
		assertNull(property.getKey());
		assertEquals("name", property.getName());
		assertEquals("component #1", property.getValue());
		
		// reference to list
		Property nextProperty = property.getNextProperty();
		assertNotNull(nextProperty);
		assertFalse(nextProperty.isIndexed());
		assertFalse(nextProperty.isMapped());
		assertEquals(-1, nextProperty.getIndex());
		assertNull(nextProperty.getKey());
		assertEquals("components", nextProperty.getName());
		assertEquals("component #1", nextProperty.getValue());
		
		// simple property to set in list
		Property beanProperty = nextProperty.getNextProperty();
		assertNotNull(beanProperty);
		assertTrue(beanProperty.isIndexed());
		assertFalse(beanProperty.isMapped());
		assertEquals(0, beanProperty.getIndex());
		assertNull(beanProperty.getKey());
		assertEquals("components", beanProperty.getName());
		assertEquals("component #1", beanProperty.getValue());
	}
	
	@Test
	public void testListOfMapProperty() {
		Property property = this.resolver.resolve("components[1](id)", "test");
		
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
	
	@Test
	public void testMapOfListProperty() {
		Property property = this.resolver.resolve("components(id)[1]", "test");
		
		// list property
		assertFalse(property.isIndexed());
		assertFalse(property.isMapped());
		assertEquals("components", property.getName());
		
		// list property mapped with key "id"
		assertNotNull(property.getNextProperty());
		Property listProperty = property.getNextProperty();
		assertTrue(listProperty.isMapped());
		assertEquals("id", listProperty.getKey());
		
		// into list property
		assertNotNull(property.getNextProperty().getNextProperty());
		Property intoListProperty = listProperty.getNextProperty();
		assertTrue(intoListProperty.isIndexed());
		assertEquals(1, intoListProperty.getIndex());
		
		// ensure no more property
		assertNull(property.getNextProperty().getNextProperty().getNextProperty());
	}
	
}
