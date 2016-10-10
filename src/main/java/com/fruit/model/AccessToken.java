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
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.fruit.core.model.BaseModel;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;


@SuppressWarnings("serial")
public class AccessToken extends BaseModel<AccessToken>{

	public static AccessToken dao = new AccessToken();

	public void updateImToken(String token, String imToken) {
		// TODO Auto-generated method stub 
		Set<Condition> conditions=Sets.newHashSet();
		conditions.add(new Condition("token",Operators.EQ,token));
		Map<String,Object> newValues=Maps.newHashMap();
		newValues.put("im_token", imToken);
		this.update(conditions, newValues);
	} 

	public AccessToken getAccessToken(String token) {
		Set<Condition> conditions=Sets.newHashSet();
		conditions.add(new Condition("token",Operators.EQ,token));
		 return this.get(conditions);
	}
	public AccessToken createOrUpdateAccessToken(Integer uid) {
		Set<Condition> conditions=Sets.newHashSet();
		conditions.add(new Condition("uid",Operators.EQ,uid));
		AccessToken accessToken=this.get(conditions);
		if(accessToken!=null){
			AccessToken.dao.delete(conditions);
		}
		accessToken=new AccessToken();
		accessToken.set("uid", uid);
		//accessToken.set("token",UUIDUtils.getUUIDKey());
		accessToken.set("modified_time", new Date());
		accessToken.set("create_time", new Date());
		accessToken.save();
		return accessToken;
	}

}