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
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.DictData;
import com.fruit.model.DictType;
import com.jfinal.plugin.activerecord.Page;

public class DictController extends BaseController {
	

	@RequiresPermissions(value={"/dict"})
	public void index() {
		render("type_index.jsp");
	}
	
	@RequiresPermissions(value={"/dict"})
	public void add_type() {
		Integer id=this.getParaToInt("id");
		if(id!=null){
			this.setAttr("item", DictType.dao.findById(id));
		}
		render("type_add.jsp");
	}
	@RequiresPermissions(value={"/dict"})
	public void add_data() {
		Integer id=this.getParaToInt("id");
		if(id!=null){
			this.setAttr("item", DictData.dao.findById(id));
		}
		this.setAttr("typeId", this.getParaToInt("typeId"));
		render("data_add.jsp");
	}
	@RequiresPermissions(value={"/dict"})
	public void data_index() {
		Integer typeId=this.getParaToInt("typeId");
		this.setAttr("typeId", typeId);
		render("data_index.jsp");
	}
	
	@RequiresPermissions(value={"/dict"})
	public void saveType(){
		Integer id=this.getParaToInt("id");
		String name=this.getPara("name");
		String remark=this.getPara("remark"); 
		InvokeResult invokeResult=DictType.dao.saveDictType(id,name,remark);
		this.renderJson(invokeResult);
	}
	
	@RequiresPermissions(value={"/dict"})
	public void saveData(){
		Integer id=this.getParaToInt("id");
		Integer seq=this.getParaToInt("seq");
		String value=this.getPara("value");
		Integer typeId=this.getParaToInt("typeId");
		String name=this.getPara("name");
		String remark=this.getPara("remark"); 
		InvokeResult invokeResult=DictData.dao.saveDictData(id,name,remark,seq,value,typeId);
		this.renderJson(invokeResult);
	}
	
	@RequiresPermissions(value={"/dict"})
	public void deleteData(){
		Integer id=this.getParaToInt("id");
		InvokeResult invokeResult=DictData.dao.deleteData(id);
		this.renderJson(invokeResult);
	}
	
	@RequiresPermissions(value={"/dict"})
	public void getTypeListData() {
		String keyword=this.getPara("keyword");
		Set<Condition> conditions=new HashSet<Condition>();
		if(CommonUtils.isNotEmpty(keyword)){
			conditions.add(new Condition("name",Operators.LIKE,keyword));
		}
		Page<DictType> pageInfo=DictType.dao.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
		this.renderJson(JqGridModelUtils.toJqGridView(pageInfo)); 
	}
	
	@RequiresPermissions(value={"/dict"})
	public void getListData() {
		Integer typeId=this.getParaToInt("typeId");
		String keyword=this.getPara("keyword");
		Set<Condition> conditions=new HashSet<Condition>();
		if(CommonUtils.isNotEmpty(keyword)){
			conditions.add(new Condition("name",Operators.LIKE,keyword));
		}
		conditions.add(new Condition("dict_type_id",Operators.EQ,typeId));
		Page<DictData> pageInfo=DictData.dao.getPage(getPage(), this.getRows(),conditions,this.getOrderby());
		this.renderJson(JqGridModelUtils.toJqGridView(pageInfo)); 
	}
}





