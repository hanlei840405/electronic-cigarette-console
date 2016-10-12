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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fruit.core.cache.CacheClearUtils;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.CommonUtils;
import com.fruit.core.util.MyDigestUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.base.BaseMallCategory;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author eason
 * 商品类别
 */
public class MallCategory extends BaseMallCategory<MallCategory>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1982696969221258167L;
	public static MallCategory me = new MallCategory();
	

	/**
	 * 获取商品分类
	 * @author eason	
	 * @param uid
	 * @return
	 */
	public List<MallCategory> getMallCategoryList(){
		
		return this.paginate(1, 20, "select *", "from mall_category ").getList();
	}
	
	/**
	 * 修改用户角色
	 * @param uid
	 * @param roleIds
	 * @return
	 */
	public InvokeResult changeUserRoles(Integer uid,String roleIds){
		Db.update("delete from sys_user_role where user_id = ?", uid);
		List<String> sqlList=Lists.newArrayList();
		for(String roleId : roleIds.split(",")){
			if(CommonUtils.isNotEmpty(roleId)){
				sqlList.add("insert into sys_user_role (user_id,role_id) values ("+uid+","+Integer.valueOf(roleId)+")");
			}
		}
		Db.batch(sqlList, 5);
		CacheClearUtils.clearUserMenuCache();
		return InvokeResult.success();
	};
	/**
	 * 密码修改
	 * @param uid
	 * @param newPwd
	 * @return
	 */
	public InvokeResult savePwdUpdate(Integer uid, String newPwd) {
		// TODO Auto-generated method stub
		MallCategory sysUser=MallCategory.me.findById(uid);
		if(sysUser!=null){
			sysUser.set("pwd", newPwd).update();
			return InvokeResult.success();
		}else{
			return InvokeResult.failure(-2, "修改失败");
		}
		
	}
	public Page<MallCategory> getSysUserPage(int page, int rows, String keyword,
			String orderbyStr) {
		String select="select su.*, (select group_concat(name) as roleNames from sys_role where id in(select role_id from sys_user_role where user_id=su.id)) as roleNames";
		StringBuffer sqlExceptSelect=new StringBuffer("from sys_user su");
		return this.paginate(page, rows, select, sqlExceptSelect.toString());
	}
}
