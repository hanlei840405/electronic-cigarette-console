/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fruit.controller.mall;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.ConsoleSequence;
import com.fruit.model.SysUser;
import com.fruit.model.mall.Category;

import java.util.*;

/**
 * 类别管理.
 *
 * @author eason
 */
public class CategoryController extends BaseController {

    @RequiresPermissions(value = {"/mall/category"})
    public void index() {
        this.renderJsp("category_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/category"})
    public void getTreeGridView() {
        this.renderJson(Category.me.getTreeGridView());
    }

    @RequiresPermissions(value = {"/mall/category"})
    public void delete() {
        Long id = this.getParaToLong("id");
        if(id != null) {
            Category.me.deleteById(id);
            this.renderJson(InvokeResult.success());

        }else {
            this.renderJson(InvokeResult.failure(500, "该数据不存在"));
        }
    }

    @RequiresPermissions(value = {"/mall/category"})
    public void add() {
        List<Category> categories = Category.me.getRootCategoryList();
        setAttr("categories", categories);
        Long id = this.getParaToLong("id");
        if (id != null) {
            Category category = Category.me.findById(id);
            setAttr("category", category);
        }
        this.renderJsp("category_add.jsp");
    }

    @RequiresPermissions(value = {"/mall/category"})
    public void save() {
        Long id = this.getParaToLong("id");
        String parentCode = this.getPara("parentCode");
        String cateName = this.getPara("cateName");
        if (id == null) {
            String cateCode = ConsoleSequence.dao.generateSequence("类目");
            Category.me.clear().set("cateCode", cateCode).set("cateName", cateName).set("parentCode", StringUtils.isEmpty(parentCode) ? null : parentCode).save();
        } else {
            Set<Condition> conditions = new HashSet<Condition>();
            conditions.add(new Condition("id", Operators.EQ, id));
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("cateName", cateName);
            values.put("parentCode", parentCode);
            Category.me.update(conditions, values);
        }
        this.renderJson(InvokeResult.success());
    }
}





