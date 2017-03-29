package com.fruit.controller.mall;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.util.security.MessageDigestPasswordEncoder;
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
    public void getCustomerUpRatedListData() {
        String customer = this.getPara("customer");
        String select = "SELECT t1.*,t2.skuName";
        String from = "from customer_up_rated t1 inner join mall_sku t2 on t1.sku = t2.sku where t1.customer=?";
        List<String> params = new ArrayList<String>();
        params.add(customer);
        Page<CustomerUpRated> pageInfo = CustomerUpRated.dao.getPage(getPage(), this.getRows(), select, from, null, params.toArray());
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
    public void upRated() {
        Long id = this.getParaToLong("id");
        Customer customer = Customer.dao.findById(id);
        Customer up = Customer.dao.findFirst("select * from mall_customer where cusCode=?", customer.getUpCode());
        setAttr("customer", customer.getCusCode());
        setAttr("upName", up.getCusName());
        List<Category> categories = Category.me.find("select * from mall_category where parentCode is not null");
        for (Category category : categories) {
            category.setSkus(Sku.dao.find("select * from mall_sku where category=?", category.getCateCode()));
        }
        setAttr("categories", categories);
        render("customer_up_rated.jsp");
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void saveAudit() {
        Long id = this.getParaToLong("id");
        String cusCode = this.getPara("cusCode");
        String upCode = this.getPara("upCode");
        String saler = this.getPara("saler");
        String strRate = this.getPara("rate");
        Float rate = Float.parseFloat(strRate);
        String priceType = this.getPara("priceType");
        String agency = this.getPara("agency");
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
        params.put("agency", agency);
        Customer.dao.clear().update(conditions, params);

        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void saveUpRated() {
        String customer = this.getPara("customer");
        String sku = this.getPara("sku");
        Customer up = Customer.dao.findFirst("select t1.* from mall_customer t1 inner join mall_customer t2 on t1.cusCode = t2.upCode where t2.cusCode=?", customer);
        String strRated = this.getPara("rated");
        BigDecimal rated = new BigDecimal(Double.parseDouble(strRated));
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("customer", Operators.EQ, customer));
        conditions.add(new Condition("up", Operators.EQ, up.getCusCode()));
        conditions.add(new Condition("sku", Operators.EQ, sku));
        CustomerUpRated customerUpRated = CustomerUpRated.dao.get(conditions);
        try {
            if (customerUpRated != null) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("rated", rated);
                CustomerUpRated.dao.clear().update(conditions, params);
            } else {
                CustomerUpRated.dao.clear().set("customer", customer).set("up", up.getCusCode()).set("sku", sku).set("rated", rated).save();
            }
            this.renderJson(InvokeResult.success());
        }catch (Exception e) {
            this.renderJson(InvokeResult.failure("保存失败，请检查上级商家、商品是否存在或者金额是否正确"));
        }
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
            SkuSprice.dao.clear().update(conditions, params);
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

    @RequiresPermissions(value = {"/mall/customer"})
    public void reset() {
        Long id = this.getParaToLong("id");
        Customer customer = Customer.dao.findById(id);

        String password = "123456";
        MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("MD5");
        password = encoder.encodePassword(password, customer.getCusCode());
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("id", Operators.EQ, id));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("passwd", password);
        Customer.dao.clear().update(conditions, params);

        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void disable() {
        Long id = this.getParaToLong("id");
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("id", Operators.EQ, id));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", 2);
        Customer.dao.clear().update(conditions, params);

        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/customer"})
    public void deleteCustomerUpRated() {
        String[] ids = this.getPara("ids").split(",");
        Set<Condition> conditions = new HashSet<Condition>();
        List<Long> array = new ArrayList<Long>();
        for (String id : ids) {
            array.add(Long.parseLong(id));
        }
        conditions.add(new Condition("id", Operators.IN, array));
        CustomerUpRated.dao.delete(conditions);

        this.renderJson(InvokeResult.success());
    }
}
