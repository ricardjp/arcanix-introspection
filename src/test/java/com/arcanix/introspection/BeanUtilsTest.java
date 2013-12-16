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

import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

import com.arcanix.convert.ConversionException;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public class BeanUtilsTest {

	@Test
	public void testSimpleProperty() throws ConversionException {
		MockBean mock = new MockBean();
		
		BeanUtils beanUtils = new BeanUtils();
		beanUtils.setNestedProperty(mock, "message", "hello world");
		
		assertEquals("hello world", mock.getMessage());
	}
	
	@Test
	public void testSimpleConvertedProperty() throws ConversionException {
		MockBean mock = new MockBean();
		
		BeanUtils beanUtils = new BeanUtils();
		beanUtils.setNestedProperty(mock, "count", "3");
	}
	
	@Test
	public void testSimpleIndexedProperty() throws ConversionException {
		MockBean mock = new MockBean();
		
		BeanUtils beanUtils = new BeanUtils();
		beanUtils.setNestedProperty(mock, "names[0]", "John Smith");
		beanUtils.setNestedProperty(mock, "names[1]", "John Doe");
		
		assertEquals(2, mock.getNames().size());
		assertEquals("John Smith", mock.getNames().get(0));
		assertEquals("John Doe", mock.getNames().get(1));
	}
	
	@Test
	public void testSimpleMappedProperty() throws ConversionException {
		MockBean mock = new MockBean();
		
		BeanUtils beanUtils = new BeanUtils();
		beanUtils.setNestedProperty(mock, "addresses(residence)", "123 Main Street");
		beanUtils.setNestedProperty(mock, "addresses(work)", "456 King Street");
		
		assertEquals(2, mock.getAddresses().size());
		assertEquals("123 Main Street", mock.getAddresses().get("residence"));
		assertEquals("456 King Street", mock.getAddresses().get("work"));
	}
	
	@Test
	public void testListOfMapProperty() throws ConversionException {
		MockBean mock = new MockBean();
		
		BeanUtils beanUtils = new BeanUtils();
		beanUtils.setNestedProperty(mock, "phones[0](area)", "111");
		beanUtils.setNestedProperty(mock, "phones[0](number)", "123-4567");
		beanUtils.setNestedProperty(mock, "phones[1](area)", "222");
		beanUtils.setNestedProperty(mock, "phones[1](number)", "987-6543");
		
		assertEquals(2, mock.getPhones().size());
		assertEquals("111", mock.getPhones().get(0).get("area"));
		assertEquals("123-4567", mock.getPhones().get(0).get("number"));
		assertEquals("222", mock.getPhones().get(1).get("area"));
		assertEquals("987-6543", mock.getPhones().get(1).get("number"));
	}
	
	public static class MockBean {
		
		private String message;
		private Integer count;
		private List<String> names;
		private Map<String, String> addresses;
		private List<Map<String, String>> phones;
		
		public void setMessage(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return this.message;
		}
		
		public void setCount(Integer count) {
			this.count = count;
		}
		
		public Integer getCount() {
			return this.count;
		}
		
		public List<String> getNames() {
			return this.names;
		}
		
		public void setNames(List<String> names) {
			this.names = names;
		}
		
		public Map<String, String> getAddresses() {
			return this.addresses;
		}
		
		public void setAddresses(Map<String, String> addresses) {
			this.addresses = addresses;
		}
		
		public List<Map<String, String>> getPhones() {
			return this.phones;
		}
		
		public void setPhones(List<Map<String, String>> phones) {
			this.phones = phones;
		}
		
	}
	
}
