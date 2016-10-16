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

import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.core.view.ZtreeView;
import com.fruit.model.mall.Category;
import com.fruit.model.mall.Sku;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

import java.util.*;

/**
 * sku管理.
 *
 * @author eason
 */
public class SkuController extends BaseController {

    @RequiresPermissions(value = {"/mall/sku"})
    public void index() {
        render("sku_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/sku"})
    public void getListData() {
        String sku = this.getPara("sku");
        Page<Sku> pageInfo = Sku.dao.getPage(getPage(), this.getRows(), sku);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    /**
     * 上下架
     */
    @RequiresPermissions(value = {"/mall/sku"})
    public void upOrDown() {
        String[] array = this.getPara("ids").split(",");
        String status = this.getPara("status");
        Set<Condition> conditions = new HashSet<Condition>();
        List<Long> ids = new ArrayList<Long>();
        for (String id : array) {
            ids.add(Long.parseLong(id));
        }
        conditions.add(new Condition("id", Operators.IN, ids));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        Sku.dao.update(conditions, params);
        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/sku"})
    public void add() {
        Long id = this.getParaToLong("id");
        if (id != null) {
            Sku sku = Sku.dao.findFirst("select sku.*,cate.cateName from mall_sku sku inner join mall_category cate on sku.category = cate.cateCode where sku.id = ?", id);
            setAttr("sku", sku);
        }
        List<ZtreeView> result = Category.me.getZtreeViewList();
        setAttr("jsonTree", JsonKit.toJson(result));
        render("sku_add.jsp");
    }

    @RequiresPermissions(value = {"/mall/sku"})
    public void save() {
        Long id = this.getParaToLong("id");
        String skuName = this.getPara("skuName");
        String sku = this.getPara("sku");
        String category = this.getPara("category");
        String specName = this.getPara("specName");
        if (id == null) {
            Sku.dao.clear().set("skuName", skuName).set("sku", sku).set("category", category).set("specName", specName).save();
        } else {
            Set<Condition> conditions = new HashSet<Condition>();
            conditions.add(new Condition("id", Operators.EQ, id));
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("skuName", skuName);
            values.put("sku", sku);
            values.put("category", category);
            values.put("specName", specName);
            Sku.dao.update(conditions, values);
        }
        this.renderJson(InvokeResult.success());
    }

    /**
     * 删除
     */
    @RequiresPermissions(value = {"/mall/sku"})
    public void delete() {
        String[] array = this.getPara("ids").split(",");
        Set<Condition> conditions = new HashSet<Condition>();
        List<Long> ids = new ArrayList<Long>();
        for (String id : array) {
            ids.add(Long.parseLong(id));
        }
        conditions.add(new Condition("id", Operators.IN, ids));
        Sku.dao.delete(conditions);
        this.renderJson(InvokeResult.success());
    }
}





