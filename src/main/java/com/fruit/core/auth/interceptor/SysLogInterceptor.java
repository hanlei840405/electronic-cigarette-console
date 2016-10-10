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
package com.fruit.core.auth.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.fruit.core.util.CommonUtils;
import com.fruit.core.util.IWebUtils;
import com.fruit.model.SysLog;
import com.fruit.model.SysUser;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 日志拦截器
 * @author eason
 */
public class SysLogInterceptor implements Interceptor {
	
	public void intercept(Invocation inv) {
		String from=inv.getController().getRequest().getHeader("Referer");
		String ip=CommonUtils.getIP(inv.getController().getRequest());
		String className=inv.getController().getClass().getName();
		String methodName=inv.getMethodName();
		Integer uid=null;
		SysUser sysUser=IWebUtils.getCurrentSysUser(inv.getController().getRequest());
		if(sysUser!=null){uid=sysUser.getInt("id");}
		long startTime=System.currentTimeMillis();
		try{
			inv.invoke();
			SysLog.dao.saveSysLog(uid, ip, from, inv.getActionKey(), className, methodName, startTime, 0,"");
		}catch(RuntimeException e){
			SysLog.dao.saveSysLog(uid, ip, from, inv.getActionKey(), className, methodName, startTime, -1,getExceptionAllinformation(e));
		}
	}
	 /**
	  * 获取异常详细信息
	  */
	  private static String getExceptionAllinformation(Throwable e){   
          StringWriter sw = new StringWriter();   
          PrintWriter pw = new PrintWriter(sw, true);   
          e.printStackTrace(pw);   
          pw.flush();   
          sw.flush();   
          return sw.toString();   
	  } 
}
