/**
 * 
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
package com.fruit.core.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
/**
 * 加密工具
 */
public class MyDigestUtils {
	
	public static void main(String[] args) {
		System.out.println(shaDigestForPasswrod("123456"));
	}
	  public static String encode64(String str)
	  {
	    try
	    {
	      return Base64.encodeBase64URLSafeString(str.getBytes("utf-8")); } catch (UnsupportedEncodingException e) {
	    }
	    return Base64.encodeBase64URLSafeString(str.getBytes());
	  }

	  public static String decode64(String str)
	  {
	    try
	    {
	      return new String(Base64.decodeBase64(str), "utf-8"); } catch (UnsupportedEncodingException e) {
	    }
	    return new String(Base64.decodeBase64(str));
	  }
	 /**
	  * 加密密码
	  * @author eason	
	  * @param str
	  * @param key
	  * @return
	  */
	  public static String shaDigestForPasswrod(String str,String key)
	  {
	    return DigestUtils.shaHex(str + key);
	  }
	  
	  public static String shaDigestForPasswrod(String str)
	  {
	    return DigestUtils.shaHex(str+ "eason");
	  }
}
