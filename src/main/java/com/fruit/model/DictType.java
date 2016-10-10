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

import com.fruit.core.util.DateUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.base.BaseDictType;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class DictType extends BaseDictType<DictType> {
	public static final DictType dao = new DictType();

	public InvokeResult saveDictType(Integer id, String name, String remark) {
		DictType dictType=new DictType();
		dictType.setId(id);
		dictType.setName(name);
		dictType.setRemark(remark);
		dictType.setUpdateTime(DateUtils.formatDateToUnixTimestamp(new Date()));
		if(id!=null){
			dictType.update();
		}else{
			dictType.save();
		}
		return InvokeResult.success();
	}
}
