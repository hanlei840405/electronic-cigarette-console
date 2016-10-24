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
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.core.view.ZtreeView;
import com.fruit.model.mall.Category;
import com.fruit.model.mall.Sku;
import com.fruit.model.mall.SkuNprice;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigDecimal;
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

    @RequiresPermissions(value = {"/mall/sku"})
    public void getListDataExt() {
        String category = this.getPara("category");
        String sku = this.getPara("sku");
        String select = "select t1.*,IFNULL(t2.price,'') as price";
        StringBuilder from = new StringBuilder("from mall_sku t1 LEFT JOIN mall_sku_sprice t2 on t1.sku = t2.sku where 1=1");
        List<String> params = new ArrayList<String>();
        if (!StringUtils.isEmpty(category)) {
            from.append(" ").append("and t1.category = ?");
            params.add(category);
        }
        if (!StringUtils.isEmpty(sku)) {
            from.append(" ").append("and t1.sku = ?");
            params.add(sku);
        }
        Page<Sku> pageInfo = Sku.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
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
        String attribute = this.getPara("attribute");
        if (id == null) {
            Sku.dao.clear().set("skuName", skuName).set("sku", sku).set("category", category).set("specName", specName).set("attribute", attribute).save();
            if (!"1".equals(attribute)) {
                SkuNprice.dao.clear().set("sku", sku).set("priceType", "A").save();
                SkuNprice.dao.clear().set("sku", sku).set("priceType", "B").save();
                SkuNprice.dao.clear().set("sku", sku).set("priceType", "C").save();
                SkuNprice.dao.clear().set("sku", sku).set("priceType", "D").save();
            }
        } else {
            Set<Condition> conditions = new HashSet<Condition>();
            conditions.add(new Condition("id", Operators.EQ, id));
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("skuName", skuName);
            values.put("sku", sku);
            values.put("category", category);
            values.put("specName", specName);
            values.put("attribute", attribute);
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
        List<String> skus = new ArrayList<String>();
        for (String id : array) {
            ids.add(Long.parseLong(id));
            skus.add(Sku.dao.findById(Long.parseLong(id)).getSku());
        }
        conditions.add(new Condition("id", Operators.IN, ids));
        Sku.dao.delete(conditions);
        conditions.clear();
        conditions.add(new Condition("sku", Operators.IN, skus));
        SkuNprice.dao.delete(conditions);
        this.renderJson(InvokeResult.success());
    }

    /**
     * 设置sku4类价格
     */
    @RequiresPermissions(value = {"/mall/sku"})
    public void setting() {
        Sku sku = Sku.dao.findById(this.getParaToLong("id"));
        SkuNprice skuNpriceA = SkuNprice.dao.findFirst("select * from mall_sku_nprice where sku = ? and priceType='A'", sku.getSku());
        SkuNprice skuNpriceB = SkuNprice.dao.findFirst("select * from mall_sku_nprice where sku = ? and priceType='A'", sku.getSku());
        SkuNprice skuNpriceC = SkuNprice.dao.findFirst("select * from mall_sku_nprice where sku = ? and priceType='A'", sku.getSku());
        SkuNprice skuNpriceD = SkuNprice.dao.findFirst("select * from mall_sku_nprice where sku = ? and priceType='A'", sku.getSku());

        setAttr("typeA", "A");
        setAttr("numA1", skuNpriceA.getNum1());
        setAttr("priceA1", skuNpriceA.getPrice1());
        setAttr("numA2", skuNpriceA.getNum2());
        setAttr("priceA2", skuNpriceA.getPrice2());
        setAttr("numA3", skuNpriceA.getNum3());
        setAttr("priceA3", skuNpriceA.getPrice3());
        setAttr("typeB", "B");
        setAttr("numB1", skuNpriceB.getNum1());
        setAttr("priceB1", skuNpriceB.getPrice1());
        setAttr("numB2", skuNpriceB.getNum2());
        setAttr("priceB2", skuNpriceB.getPrice2());
        setAttr("numB3", skuNpriceB.getNum3());
        setAttr("priceB3", skuNpriceB.getPrice3());
        setAttr("typeC", "C");
        setAttr("numC1", skuNpriceC.getNum1());
        setAttr("priceC1", skuNpriceC.getPrice1());
        setAttr("numC2", skuNpriceC.getNum2());
        setAttr("priceC2", skuNpriceC.getPrice2());
        setAttr("numC3", skuNpriceC.getNum3());
        setAttr("priceC3", skuNpriceC.getPrice3());
        setAttr("typeD", "D");
        setAttr("numD1", skuNpriceD.getNum1());
        setAttr("priceD1", skuNpriceD.getPrice1());
        setAttr("numD2", skuNpriceD.getNum2());
        setAttr("priceD2", skuNpriceD.getPrice2());
        setAttr("numD3", skuNpriceD.getNum3());
        setAttr("priceD3", skuNpriceD.getPrice3());
        setAttr("sku", sku.getSku());
        render("sku_setting.jsp");
    }

    @RequiresPermissions(value = {"/mall/sku"})
    public void saveSkuPrice() {
        String sku = this.getPara("sku");
        Integer numA1 = this.getParaToInt("numA1");
        Integer numA2 = this.getParaToInt("numA2");
        Integer numA3 = this.getParaToInt("numA3");
        Integer numB1 = this.getParaToInt("numB1");
        Integer numB2 = this.getParaToInt("numB2");
        Integer numB3 = this.getParaToInt("numB3");
        Integer numC1 = this.getParaToInt("numC1");
        Integer numC2 = this.getParaToInt("numC2");
        Integer numC3 = this.getParaToInt("numC3");
        Integer numD1 = this.getParaToInt("numD1");
        Integer numD2 = this.getParaToInt("numD2");
        Integer numD3 = this.getParaToInt("numD3");

        BigDecimal priceA1 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceA1")));
        BigDecimal priceA2 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceA2")));
        BigDecimal priceA3 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceA3")));
        BigDecimal priceB1 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceB1")));
        BigDecimal priceB2 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceB2")));
        BigDecimal priceB3 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceB3")));
        BigDecimal priceC1 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceC1")));
        BigDecimal priceC2 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceC2")));
        BigDecimal priceC3 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceC3")));
        BigDecimal priceD1 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceD1")));
        BigDecimal priceD2 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceD2")));
        BigDecimal priceD3 = BigDecimal.valueOf(Double.parseDouble(this.getPara("priceD3")));
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("sku", Operators.EQ, sku));
        conditions.add(new Condition("priceType", Operators.EQ, "A"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("num1", numA1);
        params.put("num2", numA2);
        params.put("num3", numA3);
        params.put("price1", priceA1);
        params.put("price2", priceA2);
        params.put("price3", priceA3);
        SkuNprice.dao.update(conditions, params);
        conditions.clear();
        params.clear();
        conditions.add(new Condition("sku", Operators.EQ, sku));
        conditions.add(new Condition("priceType", Operators.EQ, "B"));
        params.put("num1", numB1);
        params.put("num2", numB2);
        params.put("num3", numB3);
        params.put("price1", priceB1);
        params.put("price2", priceB2);
        params.put("price3", priceB3);
        SkuNprice.dao.update(conditions, params);
        conditions.clear();
        params.clear();
        conditions.add(new Condition("sku", Operators.EQ, sku));
        conditions.add(new Condition("priceType", Operators.EQ, "C"));
        params.put("num1", numC1);
        params.put("num2", numC2);
        params.put("num3", numC3);
        params.put("price1", priceC1);
        params.put("price2", priceC2);
        params.put("price3", priceC3);
        SkuNprice.dao.update(conditions, params);
        conditions.clear();
        params.clear();
        conditions.add(new Condition("sku", Operators.EQ, sku));
        conditions.add(new Condition("priceType", Operators.EQ, "D"));
        params.put("num1", numD1);
        params.put("num2", numD2);
        params.put("num3", numD3);
        params.put("price1", priceD1);
        params.put("price2", priceD2);
        params.put("price3", priceD3);
        SkuNprice.dao.update(conditions, params);

        this.renderJson(InvokeResult.success());
    }
}





