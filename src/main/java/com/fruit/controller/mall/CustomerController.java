package com.fruit.controller.mall;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.mall.*;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by hanlei6 on 2016/10/22.
 */
public class CustomerController extends BaseController {

    @RequiresPermissions(value = {"/mall/customer"})
    public void index() {
        render("customer_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void getListData() {
        String customer = this.getPara("customer");
        Set<Condition> conditions = new HashSet<Condition>();
        if (!StringUtils.isEmpty(customer)) {
            conditions.add(new Condition("cusCode", Operators.EQ, customer));
        }
        Page<Customer> pageInfo = Customer.dao.getPage(getPage(), this.getRows(), conditions);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void audit() {
        Long id = this.getParaToLong("id");
        Customer customer = Customer.dao.findById(id);
        setAttr("customer", customer);
        List<CustomerShop> customerShops = CustomerShop.dao.find("select * from mall_customer_shop where customer = ?", customer.getCusCode());
        setAttr("customerShops", customerShops);
        render("customer_audit.jsp");
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void setting() {
        Long id = this.getParaToLong("id");
        Customer customer = Customer.dao.findById(id);
        setAttr("customer", customer.getCusCode());
        List<Category> categories = Category.me.find("select * from mall_category where parentCode is not null");
        setAttr("categories", categories);
        render("customer_setting.jsp");
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void exclusive() {
        Long id = this.getParaToLong("id");
        Customer customer = Customer.dao.findById(id);
        setAttr("customer", customer.getCusCode());
        List<Category> categories = Category.me.find("select * from mall_category where parentCode is not null");
        setAttr("categories", categories);
        render("customer_exclusive.jsp");
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void saveAudit() {
        Long id = this.getParaToLong("id");
        String cusCode = this.getPara("cusCode");
        String upCode = this.getPara("upCode");
        String saler = this.getPara("saler");
        Integer rate = this.getParaToInt("rate");
        String priceType = this.getPara("priceType");
        Set<Condition> conditions = new HashSet<Condition>();
        Condition condition = new Condition("id", Operators.EQ, id);
        conditions.add(condition);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cusCode", cusCode);
        params.put("upCode", upCode);
        params.put("saler", saler);
        params.put("rate", rate);
        params.put("priceType", priceType);
        params.put("status", 1);
        Customer.dao.update(conditions, params);

        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void saveCustomerPrice() {
        String customer = this.getPara("customer");
        String sku = this.getPara("sku");
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(this.getPara("price")));
        Set<Condition> conditions = new HashSet<Condition>();
        Condition condition1 = new Condition("customer", Operators.EQ, customer);
        Condition condition2 = new Condition("sku", Operators.EQ, sku);
        conditions.add(condition1);
        conditions.add(condition2);
        boolean isExist = SkuSprice.dao.isExit(conditions);
        if (isExist) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("price", price);
            SkuSprice.dao.update(conditions, params);
        } else {
            SkuSprice.dao.clear().set("customer", customer).set("sku", sku).set("price", price).save();
        }

        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void saveCustomerExclusive() {
        String customer = this.getPara("customer");
        String sku = this.getPara("sku");
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("customer", Operators.EQ, customer));
        conditions.add(new Condition("sku", Operators.EQ, sku));
        CustomerSku customerSku = CustomerSku.dao.get(conditions);
        if (customerSku != null) {
            CustomerSku.dao.deleteById(customerSku.getId());
        } else {
            CustomerSku.dao.clear().set("customer", customer).set("sku", sku).save();
        }
        this.renderJson(InvokeResult.success());
    }
}
