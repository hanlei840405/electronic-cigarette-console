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
package com.fruit.core.view;


/**
 * 门面调用封装
 * @author eason
 *
 */
public class InvokeResult {

	private Object data;

	private String msg="";

	private int code;
	
	public static InvokeResult success(Object data) {
		InvokeResult result = new InvokeResult();
		result.data = data;
		result.code=0;
		return result;
	}

	public static InvokeResult success() {
		InvokeResult result = new InvokeResult();
		result.code=0;
		return result;
	}

	public static InvokeResult failure(String msg) {
		return failure(-1,msg);
	}
	public static InvokeResult failure(int code,String msg) {
		InvokeResult result = new InvokeResult();
		result.msg = msg;
		result.code=code; 
		return result;
	}
	public Object getData() {
		return data;
	}

	public String getMsg() {
		return msg;
	}
	public int getCode() {
		return code;
	}
}

