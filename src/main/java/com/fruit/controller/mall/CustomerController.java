package com.fruit.controller.mall;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.model.mall.Customer;
import com.jfinal.plugin.activerecord.Page;

import java.util.HashSet;
import java.util.Set;

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
    public void setting() {
        Long id = this.getParaToLong("id");
        setAttr("id", id);
        render("customer_setting.jsp");
    }
}
