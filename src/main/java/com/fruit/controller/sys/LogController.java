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
package com.fruit.controller.sys;

import java.util.HashSet;
import java.util.Set;

import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.auth.interceptor.SysLogInterceptor;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.CommonUtils;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.model.SysLog;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
/**
 * 日志管理.
 *
 * @author eason
 */
@Clear(SysLogInterceptor.class)
public class LogController extends BaseController {

	@RequiresPermissions
	public void index() {
		render("log_index.jsp");
	}
	
	public void getListData() {
		String title=this.getPara("title");
		Set<Condition> conditions=new HashSet<Condition>();
		if(CommonUtils.isNotEmpty(title)){
			conditions.add(new Condition("class_name",Operators.LIKE,title));
		}
		Page<SysLog> pageInfo=SysLog.dao.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
		this.renderJson(JqGridModelUtils.toJqGridView(pageInfo)); 
	}
}





