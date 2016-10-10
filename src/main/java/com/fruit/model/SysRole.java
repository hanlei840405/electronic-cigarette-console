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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.fruit.core.cache.CacheClearUtils;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.CommonUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.core.view.ZtreeView;
import com.fruit.model.base.BaseSysRole;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * @author eason
 * 系统角色
 */
public class SysRole extends BaseSysRole<SysRole>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2374713709626532804L;
	public static final SysRole me = new SysRole(); 
	/**
	 * @author eason	
	 * @param uid
	 * @return
	 */
	public List<SysRole> getSysRoleList(int uid){
		
		return this.find("select * from sys_role where id in(select role_id from sys_user_role where user_id =?)",uid);
	}
	
	public List<SysRole> getSysRoleIdList(int uid){
		return this.find("select id from sys_role where id in(select role_id from sys_user_role where user_id =?)",uid);
	}
	
	public InvokeResult setVisible(String idStrs, Integer visible) {
		List<Integer> ids=CommonUtils.getIntegerListByStrs(idStrs);
		if(ids.contains(1)){
			return InvokeResult.failure("超级管理员不能被修改");
		}
		Set<Condition> conditions=new HashSet<Condition>();
		conditions.add(new Condition("id",Operators.IN,ids));
		Map<String,Object> newValues=new HashMap<String,Object>();
		newValues.put("status", visible);
		this.update(conditions, newValues);
		return InvokeResult.success();
	} 

	public InvokeResult getZtreeViewList(Integer type, Integer roleId) {
		List<SysRes> curMenulist=this.getSysMenus(roleId);
		List<ZtreeView> ztreeViews=new ArrayList<ZtreeView>();
		ztreeViews.add(new ZtreeView(10000,null,"资源列表",true));
		for(SysRes SysRes : curMenulist){
			ZtreeView ztreeView=new ZtreeView();
			ztreeView.setId(SysRes.getInt("id"));
			ztreeView.setName(SysRes.getStr("name"));
			ztreeView.setOpen(true);
			if(SysRes.getInt("pid")==null){
				ztreeView.setpId(10000);
			}else{
				ztreeView.setpId(SysRes.getInt("pid"));
			}
			ztreeView.setChecked(SysRes.getLong("selected")==1?true:false);
			ztreeViews.add(ztreeView);
		}
		return InvokeResult.success(JsonKit.toJson(ztreeViews));
	}
	
	public InvokeResult getRoleZtreeViewList(Integer uid) {
		List<SysRole> list=this.getSysRoles(uid);
		List<ZtreeView> ztreeViews=new ArrayList<ZtreeView>();
		ztreeViews.add(new ZtreeView(10000,null,"角色列表",true));
		for(SysRole role : list){
			ZtreeView ztreeView=new ZtreeView();
			ztreeView.setId(role.getId());
			ztreeView.setName(role.getName());
			ztreeView.setOpen(true);
			ztreeView.setpId(10000);
			ztreeView.setChecked(role.getLong("selected")==1?true:false);
			ztreeViews.add(ztreeView);
		}
		return InvokeResult.success(JsonKit.toJson(ztreeViews));
	}
	
	public List<SysRes> getSysMenus(Integer roleId) {
		SysRole role=this.findById(roleId); 
		if(role!=null){
			List<SysRes> list=SysRes.me.find("SELECT *,(CASE WHEN re.id IN (SELECT rr.res_id from sys_role_res rr WHERE rr.role_id="+roleId+" ) THEN 1 ELSE 0 END) as selected FROM sys_res re where re.enabled=1");
			return list;
		}
		return Lists.newArrayList();
	}
	public List<SysRole> getSysRoles(Integer uid) {
		List<SysRole> list=this.find("SELECT *,(CASE WHEN re.id IN (SELECT rr.role_id from sys_user_role rr WHERE rr.user_id="+uid+" ) THEN 1 ELSE 0 END) as selected FROM sys_role re where re.status=1");
		return list;
	}
	
	public InvokeResult saveMenuAssign(String menuIds, Integer roleId) {
		if(roleId.equals(1)){
			return InvokeResult.failure("超级管理员不能被修改");
		}
		Db.update("delete from sys_role_res where role_id = ?", roleId);
		if(CommonUtils.isNotEmpty(menuIds)){//改成批量插入
			List<String> sqlList=Lists.newArrayList();
			for(String id : menuIds.split(",")){
				if(CommonUtils.isNotEmpty(id)){
					if(!Integer.valueOf(id).equals(10000))
						sqlList.add("insert into sys_role_res (role_id,res_id) values ("+roleId+","+Integer.valueOf(id)+")");
				}
			}
			Db.batch(sqlList, 50);
			CacheClearUtils.clearUserMenuCache();
		}
		return InvokeResult.success();
	}
	
	public InvokeResult save(Integer id, String name, String des) {
		// TODO Auto-generated method stub
		if(id!=null){
			SysRole role=this.findById(id);
			role.set("name", name).set("des",des).update();
		}else{
			new SysRole().set("name", name).set("des",des).set("createdate", new Date()).save();
		}
		return InvokeResult.success();
	} 
	
	public List<SysRole> getSysRoleNamelist(){
		return this.find("select id,name from sys_role where status = 1");
	}
	
	public String getRoleNames(String roleIds){
		String sql="select group_concat(name) as roleNames from sys_role where id in("+roleIds+")";
		SysRole sysRole=this.findFirst(sql);
		if(sysRole!=null){
			return sysRole.getStr("roleNames");
		}
		return "";
	}
}
