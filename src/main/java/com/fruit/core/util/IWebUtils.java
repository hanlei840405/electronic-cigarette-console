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
package com.fruit.core.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.SysUser;
import com.jfinal.kit.PropKit;
/**
 * 
 * @author eason
 */
public class IWebUtils {
	
	/**
	 * 获取系统用户
	 * @author eason
	 * @param HttpSession
	 * @return
	 */
	public static SysUser getCurrentSysUser(HttpSession  httpSession ) {
		SysUser sysUser=(SysUser)httpSession.getAttribute("sysUser");
		return sysUser;
	}
	/**
	 * 保存登陆用户
	 * @author eason
	 * @param HttpSession
	 * @param user
	 */
	public static void setCurrentLoginSysUserToSession(HttpSession httpSession ,SysUser sysUser){
		httpSession.setAttribute("sysUser",sysUser);
	}
//	public static void setCurrentLoginSysUser(HttpServletResponse response ,HttpSession httpSession,SysUser sysUser){
//		String token=RandomUtils.getRandomString(64);
//		sysUser.setToken(token);
//		sysUser.update();
//		CookieUtils.addCookie(response, "token", token, 60*60*24);
//		setCurrentLoginSysUserToSession(httpSession, sysUser);
//	}
	public static void setCurrentLoginSysUser(HttpServletResponse response ,HttpSession httpSession,SysUser sysUser,Integer autoLogin){
		setCurrentLoginSysUserToSession(httpSession, sysUser);
		if(autoLogin.equals(1)){
			//保存7天
			long expireTime=DateUtils.addDay(new Date(),7).getTime();
			//得到用户名、经MD5加密后的用户密码、cookie有效时间
			String userInfo=MD5Utils.GetMD5Code(sysUser.getName()+sysUser.getPwd()+expireTime+PropKit.get("app_key"));
			String userBase64=Base64.encodeBase64String(new String(sysUser.getName()+":"+expireTime+":"+userInfo).getBytes());
			CookieUtils.addCookie(response, "token", userBase64, 60*60*24*7);
		}
	}
	
	/**
	 * 获取当前登陆的用户
	 * @author eason
	 * @param request
	 * @return
	 */
	public static SysUser getCurrentSysUser(HttpServletRequest request){
		SysUser sysUser=getCurrentSysUser(request.getSession());
		if(sysUser==null){
			String token=CookieUtils.getCookieByName(request, "token");
			if(CommonUtils.isNotEmpty(token)){
				String[] tokenArray=new String(Base64.decodeBase64(token)).split(":");
				if(tokenArray.length==3){
					String expireTimeStr= tokenArray[1];
					if(new Date(Long.valueOf(expireTimeStr)).after(new Date())){
						String name=tokenArray[0];
						String userInfo=tokenArray[2];
						SysUser user=SysUser.me.get(CommonUtils.getConditions(new Condition("name",Operators.EQ,name)));
						if(user!=null&&user.getStatus().equals(1)){
							String userInfo2=MD5Utils.GetMD5Code(user.getName()+user.getPwd()+expireTimeStr+PropKit.get("app_key"));
							if(userInfo2.equals(userInfo)){
								sysUser=user;
								setCurrentLoginSysUserToSession(request.getSession(), sysUser);
							}
						}
					}
				}
				return sysUser;
			}
		}
		return sysUser; 
	}
	/**
	 * 清除session中的系统用户
	 * @param HttpSession
	 */
	public static void removeCurrentSysUserFromSession(HttpSession httpSession){
		httpSession.removeAttribute("sysUser");
	}
	
	public static void removeCurrentSysUser(HttpServletRequest request,HttpServletResponse response){
		CookieUtils.delCookie("", request, response);
		removeCurrentSysUserFromSession(request.getSession());
	}
	
}
