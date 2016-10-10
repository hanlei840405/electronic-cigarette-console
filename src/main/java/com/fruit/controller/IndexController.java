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
package com.fruit.controller;

import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.auth.interceptor.AuthorityInterceptor;
import com.fruit.core.auth.interceptor.SysLogInterceptor;
import com.fruit.core.util.IWebUtils;
import com.fruit.core.util.MyDigestUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.SysLoginRecord;
import com.fruit.model.SysUser;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

/**
 * 首页、登陆、登出
 * @author eason
 */
@Clear(SysLogInterceptor.class)
@RequiresPermissions(value={"/"})
public class IndexController extends Controller {
	
	public void index() {
		render("index.jsp");
	}
	
	public void home() {
		render("home.jsp");
	}
	
	@Clear(AuthorityInterceptor.class)
	public void login() {
		this.setAttr("url", this.getPara("url"));
		render("login.jsp");
	}
	@Clear(AuthorityInterceptor.class)
	public void dologin() {
		String imageCode=this.getPara("imageCode");
		if(StrKit.notBlank(imageCode)){
			String imageCodeSession=(String)this.getSessionAttr("imageCode");
			if(!imageCodeSession.toLowerCase().equals(imageCode.trim().toLowerCase())){
				this.renderJson(InvokeResult.failure("验证码输入有误"));
				return;
			}
		}else{
			this.renderJson(InvokeResult.failure("请输入验证码"));
			return;
		}
		SysUser sysUser=SysUser.me.getByName(this.getPara("username"));
		if(sysUser==null){
			this.renderJson(InvokeResult.failure("用户不存在"));
			return;
		}
		if(SysLoginRecord.dao.hasOverLoginErrTimes(sysUser.getId())){
			this.renderJson(InvokeResult.failure("今天连续输入密码错误次数超过5次"));
			return;
		}
		if(!sysUser.getPwd().equals(MyDigestUtils.shaDigestForPasswrod(this.getPara("password")))){
			SysLoginRecord.dao.saveSysLoginRecord(sysUser.getId(),0);
			this.renderJson(InvokeResult.failure("用户密码输入有误"));
			return;
		}
		if(sysUser.getInt("status")==2){
			this.renderJson(InvokeResult.failure("用户被冻结，请联系管理员"));
			return;
		}
		Integer autoLogin=this.getParaToInt("autoLogin",0);
		IWebUtils.setCurrentLoginSysUser(this.getResponse(),this.getSession(),sysUser,autoLogin);
		SysLoginRecord.dao.saveSysLoginRecord(sysUser.getId(),1);
		this.renderJson(InvokeResult.success());
	}
	
	public void loginOut() {
		IWebUtils.removeCurrentSysUser(getRequest(), getResponse());
		this.redirect("/login");
	}
	
	public void pwdSetting(){
		try {
			SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
			this.setAttr("sysUser", sysUser);
			render("sys/pwd_setting.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void savePwdUpdate(){
		String oldPwd=MyDigestUtils.shaDigestForPasswrod(getPara("oldPwd"));
		String newPwd=MyDigestUtils.shaDigestForPasswrod(getPara("newPwd"));
		String pwdRepeat=MyDigestUtils.shaDigestForPasswrod(getPara("pwdRepeat"));
		try {
			SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
			if(!sysUser.get("pwd").equals(oldPwd)){
				this.renderJson(InvokeResult.failure(-3, "旧密码不正确"));
			}else{
				if(newPwd.equals(pwdRepeat)){
					InvokeResult result=SysUser.me.savePwdUpdate(sysUser.getInt("id"), newPwd);
					this.renderJson(result);
				}else{ 
					this.renderJson(InvokeResult.failure(-1, "两次密码不一致"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}





