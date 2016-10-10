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
package com.fruit.model;

import java.util.Date;

import com.fruit.model.base.BaseSysLog;

/**
 * @author eason
 * 系统日志
 */
@SuppressWarnings("serial")
public class SysLog extends BaseSysLog<SysLog>
{
	public static SysLog dao = new SysLog();
	
	public void saveSysLog(Integer uid,String ip,String from,String url,String className,String methodName,long startTime,int errCode,String errMsg){
		SysLog sysLog=new SysLog();
		sysLog.setUid(uid);
		sysLog.setIp(ip);
		sysLog.setFrom(from);
		sysLog.setUrl(url);
		sysLog.setClassName(className);
		sysLog.setMethodName(methodName);
		sysLog.setErrCode(errCode);
		sysLog.setErrMsg(errMsg);
		sysLog.setStartTime(new Date(startTime));
		sysLog.setSpendTime(System.currentTimeMillis()-startTime);
		sysLog.save();
	}
}
