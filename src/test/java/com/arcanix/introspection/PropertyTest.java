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

import org.junit.Test;

import com.arcanix.introspection.Property.PropertyBuilder;

import static org.junit.Assert.*;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public class PropertyTest {

	@Test
	public void testCreationWithName() {
		Property property = new PropertyBuilder().setName("name").build();
		assertEquals("name", property.getName());
	}
	
	@Test
	public void testCreationWithValue() {
		Property property = new PropertyBuilder().setValue("value").build();
		assertEquals("value", property.getValue());
	}
	
	@Test
	public void testCreationWithKey() {
		Property property = new PropertyBuilder().setKey("key").build();
		assertEquals("key", property.getKey());		
	}
	
	@Test
	public void testCreationWithIndex() {
		Property property = new PropertyBuilder().setIndex(Integer.valueOf(1)).build();
		assertEquals(Integer.valueOf(1), property.getIndex());		
	}
	
	@Test
	public void testIsIndexedWithNoIndex() {
		Property property = new PropertyBuilder().build();
		assertFalse(property.isIndexed());
	}
	
	@Test
	public void testIsMappedWithNoKey() {
		Property property = new PropertyBuilder().build();
		assertFalse(property.isMapped());
	}
	
	@Test
	public void testIsIndexedWithIndex() {
		Property property = new PropertyBuilder().setIndex(Integer.valueOf(1)).build();
		assertTrue(property.isIndexed());
	}
	
	@Test
	public void testIsMappedWithKey() {
		Property property = new PropertyBuilder().setKey("key").build();
		assertTrue(property.isMapped());
	}
	
}
