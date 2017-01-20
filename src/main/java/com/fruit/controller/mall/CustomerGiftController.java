package com.fruit.controller.mall;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.mall.*;
import com.fruit.transaction.CustomerGiftService;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanlei6 on 2016/10/22.
 */
public class CustomerGiftController extends BaseController {

    @RequiresPermissions(value = {"/mall/customerGift"})
    public void index() {
        render("customer_gift_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/customerGift"})
    public void view() {
        Long id = getParaToLong("id");
        CustomerGift customerGift = CustomerGift.dao.findById(id);
        Sku gift = Sku.dao.findFirst("select * from mall_sku where sku=?", customerGift.getGift());
        Order order = Order.dao.findById(customerGift.getOrderID());
        Customer customer = Customer.dao.findFirst("select * from mall_customer where cusCode=?", order.getCustomer());
        setAttr("id", id);
        setAttr("status", customerGift.getStatus());
        setAttr("orderID", customerGift.getOrderID());
        setAttr("cusName", customer.getCusName());
        setAttr("amount", order.getAmount());
        setAttr("gift", gift == null ? null : gift.getSkuName());
        setAttr("quantity", customerGift.getQuantity());
        List<Category> categories = Category.me.find("select * from mall_category where parentCode is not null");
        for (Category category : categories) {
            category.setSkus(Sku.dao.find("select * from mall_sku where category=? and attribute='1'", category.getCateCode()));
        }
        setAttr("categories", categories);


        render("customer_gift_view.jsp");
    }

    @RequiresPermissions(value = {"/mall/customerGift"})
    public void getListData() {
        String searchCustomer = this.getPara("search_customer");
        String searchStatus = this.getPara("search_status");
        String select = "select t1.*,t2.cusName as customerName,t3.cusName as relationshipName,t4.odtime";
        StringBuilder from = new StringBuilder("from customer_gift t1 INNER JOIN mall_customer t2 on t1.customer = t2.cusCode INNER JOIN mall_customer t3 on t1.relationship = t3.cusCode INNER JOIN od_order t4 on t1.orderID = t4.orderID where 1=1");
        List<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(searchCustomer)) {
            from.append(" and t1.customer = ?");
            params.add(searchCustomer);
        }
        if (!StringUtils.isEmpty(searchStatus)) {
            from.append(" and t1.status = ?");
            params.add(searchStatus);
        }
        from.append(" order by t1.id desc");
        Page<CustomerGift> pageInfo = CustomerGift.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/customerGift"})
    public void send() {
        Long id = getParaToLong("id");
        String gift = getPara("gift");
        Integer quantity = getParaToInt("quantity");
        try {
            CustomerGiftService customerGiftService = Duang.duang(CustomerGiftService.class);
            customerGiftService.send(id, gift, quantity);
            this.renderJson(InvokeResult.success());
        } catch (Exception e) {
            this.renderJson(InvokeResult.failure("操作失败，联系运维"));
        }
    }
}
