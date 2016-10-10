/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fruit.core.model;

import java.io.Serializable;


public class Condition implements Serializable {

	private static final long serialVersionUID = -3022761015114864793L;
	// /属性名称查询条件名称
	private String property;
	// 条件规则
	private Operators oper;
	// 条件设置的值
	private Object value;
	// betwwen查询条件的第一个参数
	private Object firstValue;

	/***
	 * between条件的时候使用
	 * 
	 * @param property
	 * @param oper
	 * @param value
	 * @param v2
	 */
	public Condition(String property, Operators oper, Object value, Object v2) {
		this.property = property;
		this.oper = oper;
		this.value = v2;
		this.firstValue = value;
	}

	/**
	 * 除between之外
	 * 
	 * @param property
	 * @param oper
	 * @param value
	 */
	public Condition(String property, Operators oper, Object value) {
		this.property = property;
		this.oper = oper;
		this.value = value;
	}

	public Condition(String property, Operators oper, Object... values) {
		this.property = property;
		this.oper = oper;
		if (oper.equals(Operators.BETWEEN)) {
			this.firstValue = values[0];
			this.value = values[1];
		} else {
			if (values == null) {
				this.value = null;
			} else {
				this.value = values[0];
			}
		}
	}

	public Condition() {
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Operators getOper() {
		return oper;
	}

	public void setOper(Operators oper) {
		this.oper = oper;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(Object firstValue) {
		this.firstValue = firstValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oper == null) ? 0 : oper.hashCode());
		result = prime * result
				+ ((property == null) ? 0 : property.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Condition other = (Condition) obj;
		if (oper == null) {
			if (other.oper != null)
				return false;
		} else if (!oper.equals(other.oper))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}

}
