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
import com.fruit.model.mall.Order;
import com.fruit.model.mall.Sku;
import com.fruit.model.mall.SkuNprice;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

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
        String select = "select t1.*,t2.cateName";
        StringBuilder from = new StringBuilder("from mall_sku t1 INNER JOIN mall_category t2 on t1.category = t2.cateCode where 1=1");
        List<String> params = new ArrayList<String>();
        if (!StringUtils.isEmpty(sku)) {
            from.append(" and (t1.sku = ? OR t2.skuName = ?)");
            params.add(sku);
            params.add(sku);
        }
        Page<Sku> pageInfo = Sku.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/sku"})
    public void get() {
        String sku = this.getPara("sku");
        Sku entity = Sku.dao.findFirst("SELECT * FROM mall_sku WHERE sku=?", sku);
        Map<String, Object> result = new HashMap<String, Object>();
        if (entity != null) {
            result.put("code", "200");
            result.put("sku", entity);
        } else {
            result.put("code", "500");
            result.put("msg", "商品不存在");
        }
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/mall/sku"})
    public void getListDataForSp() {
        String category = this.getPara("category");
        String customer = this.getPara("customer");
        String sku = this.getPara("sku");
        String select = "SELECT t1.*,IFNULL(t2.price,'') AS price, t3.cateName";
        StringBuilder from = new StringBuilder("FROM mall_sku t1");
        from.append(" LEFT JOIN mall_sku_sprice t2 ON t1.sku = t2.sku AND t2.customer = ?");
        from.append(" LEFT JOIN mall_category t3 ON t1.category = t3.cateCode");
        from.append(" WHERE 1=1");
        List<String> params = new ArrayList<String>();
        params.add(customer);
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

    @RequiresPermissions(value = {"/mall/sku"})
    public void getListDataForEx() {
        String category = this.getPara("category");
        String customer = this.getPara("customer");
        String sku = this.getPara("sku");
        String select = "SELECT t1.*,t2.customer,t3.cateName";
        StringBuilder from = new StringBuilder("FROM mall_sku t1");
        from.append(" LEFT JOIN mall_customer_sku t2 ON t1.sku = t2.sku AND t2.customer = ?");
        from.append(" LEFT JOIN mall_category t3 ON t1.category = t3.cateCode");
        from.append(" WHERE 1=1");
        List<String> params = new ArrayList<String>();
        params.add(customer);
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
        String exclusive = this.getPara("exclusive");
        String image = this.getPara("image");
        if (id == null) {
            Set<Condition> conditions = new HashSet<Condition>();
            conditions.add(new Condition("sku", Operators.EQ, sku));
            Sku exist = Sku.dao.get(conditions);
            if (exist != null) {
                this.renderJson(InvokeResult.failure("商品编号已存在！"));
                return;
            }
            Sku.dao.clear().set("skuName", skuName).set("sku", sku).set("category", category).set("specName", specName).set("attribute", attribute).set("image", image).set("exclusive", exclusive).save();
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
            values.put("image", image);
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
        SkuNprice skuNpriceB = SkuNprice.dao.findFirst("select * from mall_sku_nprice where sku = ? and priceType='B'", sku.getSku());
        SkuNprice skuNpriceC = SkuNprice.dao.findFirst("select * from mall_sku_nprice where sku = ? and priceType='C'", sku.getSku());
        SkuNprice skuNpriceD = SkuNprice.dao.findFirst("select * from mall_sku_nprice where sku = ? and priceType='D'", sku.getSku());
        if (skuNpriceA != null) {
            setAttr("typeA", "A");
            setAttr("numA1", skuNpriceA.getNum1());
            setAttr("priceA1", skuNpriceA.getPrice1());
            setAttr("numA2", skuNpriceA.getNum2());
            setAttr("priceA2", skuNpriceA.getPrice2());
            setAttr("numA3", skuNpriceA.getNum3());
            setAttr("priceA3", skuNpriceA.getPrice3());
        }
        if (skuNpriceB != null) {
            setAttr("typeB", "B");
            setAttr("numB1", skuNpriceB.getNum1());
            setAttr("priceB1", skuNpriceB.getPrice1());
            setAttr("numB2", skuNpriceB.getNum2());
            setAttr("priceB2", skuNpriceB.getPrice2());
            setAttr("numB3", skuNpriceB.getNum3());
            setAttr("priceB3", skuNpriceB.getPrice3());
        }
        if (skuNpriceC != null) {
            setAttr("typeC", "C");
            setAttr("numC1", skuNpriceC.getNum1());
            setAttr("priceC1", skuNpriceC.getPrice1());
            setAttr("numC2", skuNpriceC.getNum2());
            setAttr("priceC2", skuNpriceC.getPrice2());
            setAttr("numC3", skuNpriceC.getNum3());
            setAttr("priceC3", skuNpriceC.getPrice3());
        }
        if (skuNpriceD != null) {
            setAttr("typeD", "D");
            setAttr("numD1", skuNpriceD.getNum1());
            setAttr("priceD1", skuNpriceD.getPrice1());
            setAttr("numD2", skuNpriceD.getNum2());
            setAttr("priceD2", skuNpriceD.getPrice2());
            setAttr("numD3", skuNpriceD.getNum3());
            setAttr("priceD3", skuNpriceD.getPrice3());
        }
        setAttr("sku", sku.getSku());
        render("sku_setting.jsp");
    }

    @RequiresPermissions(value = {"/mall/sku"})
    public void saveSkuPrice() {
        String sku = this.getPara("sku");
        Integer numA1 = this.getParaToInt("numA1");
        Integer numA2 = this.getParaToInt("numA2");
        numA2 = numA2 == null ? numA1 : numA2;
        Integer numA3 = this.getParaToInt("numA3");
        numA3 = numA3 == null ? numA1 : numA3;
        Integer numB1 = this.getParaToInt("numB1");
        Integer numB2 = this.getParaToInt("numB2");
        numB2 = numB2 == null ? numB1 : numB2;
        Integer numB3 = this.getParaToInt("numB3");
        numB3 = numB3 == null ? numB1 : numB3;
        Integer numC1 = this.getParaToInt("numC1");
        Integer numC2 = this.getParaToInt("numC2");
        numC2 = numC2 == null ? numC1 : numC2;
        Integer numC3 = this.getParaToInt("numC3");
        numC3 = numC3 == null ? numC1 : numC3;
        Integer numD1 = this.getParaToInt("numD1");
        Integer numD2 = this.getParaToInt("numD2");
        numD2 = numD2 == null ? numC1 : numD2;
        Integer numD3 = this.getParaToInt("numD3");
        numD3 = numD3 == null ? numC1 : numD3;
        double tempPriceA1 = Double.parseDouble(this.getPara("priceA1"));
        double tempPriceB1 = Double.parseDouble(this.getPara("priceB1"));
        double tempPriceC1 = Double.parseDouble(this.getPara("priceC1"));
        double tempPriceD1 = Double.parseDouble(this.getPara("priceD1"));
        double tempPriceA2;
        double tempPriceB2;
        double tempPriceC2;
        double tempPriceD2;
        double tempPriceA3;
        double tempPriceB3;
        double tempPriceC3;
        double tempPriceD3;
        String valueA2 = this.getPara("priceA2");
        String valueA3 = this.getPara("priceA3");
        String valueB2 = this.getPara("priceB2");
        String valueB3 = this.getPara("priceB3");
        String valueC2 = this.getPara("priceC2");
        String valueC3 = this.getPara("priceC3");
        String valueD2 = this.getPara("priceD2");
        String valueD3 = this.getPara("priceD3");

        if (StringUtils.isEmpty(valueA2)) {
            tempPriceA2 = tempPriceA1;
        } else {
            tempPriceA2 = Double.parseDouble(this.getPara("priceA2"));
        }

        if (StringUtils.isEmpty(valueA3)) {
            tempPriceA3 = tempPriceA1;
        } else {
            tempPriceA3 = Double.parseDouble(this.getPara("priceA3"));
        }

        if (StringUtils.isEmpty(valueB2)) {
            tempPriceB2 = tempPriceB1;
        } else {
            tempPriceB2 = Double.parseDouble(this.getPara("priceB2"));
        }

        if (StringUtils.isEmpty(valueB3)) {
            tempPriceB3 = tempPriceB1;
        } else {
            tempPriceB3 = Double.parseDouble(this.getPara("priceB3"));
        }

        if (StringUtils.isEmpty(valueC2)) {
            tempPriceC2 = tempPriceC1;
        } else {
            tempPriceC2 = Double.parseDouble(this.getPara("priceC2"));
        }

        if (StringUtils.isEmpty(valueC3)) {
            tempPriceC3 = tempPriceC1;
        } else {
            tempPriceC3 = Double.parseDouble(this.getPara("priceC3"));
        }

        if (StringUtils.isEmpty(valueD2)) {
            tempPriceD2 = tempPriceD1;
        } else {
            tempPriceD2 = Double.parseDouble(this.getPara("priceD2"));
        }

        if (StringUtils.isEmpty(valueD3)) {
            tempPriceD3 = tempPriceA1;
        } else {
            tempPriceD3 = Double.parseDouble(this.getPara("priceD3"));
        }

        BigDecimal priceA1 = BigDecimal.valueOf(tempPriceA1);
        BigDecimal priceA2 = BigDecimal.valueOf(tempPriceA2);
        BigDecimal priceA3 = BigDecimal.valueOf(tempPriceA3);
        BigDecimal priceB1 = BigDecimal.valueOf(tempPriceB1);
        BigDecimal priceB2 = BigDecimal.valueOf(tempPriceB2);
        BigDecimal priceB3 = BigDecimal.valueOf(tempPriceB3);
        BigDecimal priceC1 = BigDecimal.valueOf(tempPriceC1);
        BigDecimal priceC2 = BigDecimal.valueOf(tempPriceC2);
        BigDecimal priceC3 = BigDecimal.valueOf(tempPriceC3);
        BigDecimal priceD1 = BigDecimal.valueOf(tempPriceD1);
        BigDecimal priceD2 = BigDecimal.valueOf(tempPriceD2);
        BigDecimal priceD3 = BigDecimal.valueOf(tempPriceD3);

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

    @RequiresPermissions(value = {"/mall/sku"})
    public void uploadImage() throws IOException {
        UploadFile uploadFile = this.getFile();
        File file = uploadFile.getFile();

        BufferedImage prevImage = ImageIO.read(file);
        int newWidth = 268;
        int newHeight = 249;
        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);

        Path rootLocation = Paths.get(System.getProperties().getProperty("user.home"), "/upload/public/");


        File folder = new File(rootLocation.toUri());
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdir();
        }
        if (Files.exists(rootLocation.resolve(uploadFile.getOriginalFileName()))) {
            Files.delete(rootLocation.resolve(uploadFile.getOriginalFileName()));
        }
        File dist = new File(rootLocation.resolve(uploadFile.getOriginalFileName()).toString());
        ImageIO.write(image, "jpg", dist);
        file.delete();

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("name", uploadFile.getOriginalFileName());
        renderJson(result);
    }
}





