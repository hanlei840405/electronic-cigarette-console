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
package com.fruit.core.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.fruit.core.util.IWebUtils;
import com.fruit.model.SysUser;
import com.jfinal.kit.StrKit;
/**
 * 功能按钮
 * @author eason
 *
 */
public class ButtonTag extends TagSupport{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String className;
	/**
	 * 需要的权限
	 */
	private String permission;
	
	private String style;
	
	private String name;
	
	private String textName;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			boolean hasPermission=true;
			StringBuilder button=new StringBuilder("<button");
			if(StrKit.notBlank(permission)){//判断有没有权限
				 HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
				SysUser sysUser=IWebUtils.getCurrentSysUser(request);
				if(sysUser==null||!sysUser.getPermissionSets().contains(permission)){
					hasPermission=false;
				}
			}
			if(hasPermission){ 
				if(StrKit.notBlank(className)){
					button.append(" class=\""+className+"\"");
				}
				if(StrKit.notBlank(id)){
					button.append(" id=\""+id+"\"");
				}
				if(StrKit.notBlank(name)){
					button.append(" name=\""+name+"\"");
				}
				if(StrKit.notBlank(style)){
					button.append(" style=\""+style+"\"");
				}
				button.append(">");
				if(StrKit.notBlank(textName)){
					button.append(textName);
				}
				button.append("</button>");
				pageContext.getOut().print(button.toString());
			}else{
				pageContext.getOut().print("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTextName() {
		return textName;
	}

	public void setTextName(String textName) {
		this.textName = textName;
	}
	
}
