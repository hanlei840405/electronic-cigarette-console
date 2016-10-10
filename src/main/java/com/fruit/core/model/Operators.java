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


public enum Operators {
	/**
	 * 等于
	 */
	EQ,
	/**
	 * 大于
	 */
	GT,
	/**
	 *小于
	 */
	LT,
	/**
	 * 大于等于
	 */
	GE,
	/**
	 * 小于等于
	 */
	LE,
	/**
	 * 包含
	 */
	IN,
	/**
	 * 不包含
	 */
	NOT_IN,
	/**
	 * 模糊
	 */
	LIKE
	/**
	 * 不等于
	 */
	, NOT_EQ
	/**
	 * 范围查询
	 */
	, BETWEEN
}
