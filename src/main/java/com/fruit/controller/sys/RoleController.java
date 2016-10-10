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
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.CommonUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.SysRole;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

public class RoleController extends BaseController {
	

	@RequiresPermissions(value={"/sys/role"})
	public void index() {
		render("role_index.jsp");
	}
	
	@RequiresPermissions(value={"/sys/role"})
	public void add() {
		Integer id=this.getParaToInt("id");
		if(id!=null){
			this.setAttr("item", SysRole.me.findById(id));
		}
		this.setAttr("id", id);
		render("role_add.jsp");
	}
	
	
	@RequiresPermissions(value={"/sys/role"})
	public void getListData() {
		String name=this.getPara("name");
		Set<Condition> conditions=new HashSet<Condition>();
		if(CommonUtils.isNotEmpty(name)){
			conditions.add(new Condition("name",Operators.LIKE,name));
		}
		Page<SysRole> pageInfo=SysRole.me.getPage(this.getPage(), this.getRows(),conditions,this.getOrderby());
		this.renderJson(SysRole.me.toJqGridView(pageInfo)); 
	}

	@RequiresPermissions(value={"/sys/role"})
	public void setVisible(){
		Integer visible=this.getParaToInt("visible");
		String bids=this.getPara("ids");
		InvokeResult result=SysRole.me.setVisible(bids, visible);
		this.renderJson(result);
	}

	@RequiresPermissions(value={"/sys/role"})
	public void getZtree(){
		Integer type=this.getParaToInt("type");
		Integer roleId=this.getParaToInt("roleId");
		InvokeResult result=SysRole.me.getZtreeViewList(type, roleId);
		this.setAttr("jsonTree", result);
		this.setAttr("roleId", roleId);
		render("menu_assign.jsp");
	}
	
	/**
	 * 角色绑定资源.
	 */
	@RequiresPermissions(value={"/sys/role"})
	@Before(Tx.class)
	public void saveMenuAssign(){
		Integer roleId=this.getParaToInt("roleId");
		String menuIds=this.getPara("menuIds");
		InvokeResult result=SysRole.me.saveMenuAssign(menuIds, roleId);
		this.renderJson(result);
	}
	
	@RequiresPermissions(value={"/sys/role"})
	public void save(){
		InvokeResult result=SysRole.me.save(this.getParaToInt("id"),this.getPara("name"),this.getPara("des"));
		this.renderJson(result);
	}
	
}





