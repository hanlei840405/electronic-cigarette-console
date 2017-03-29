package com.fruit.controller.mall;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.util.IWebUtils;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.SysUser;
import com.fruit.model.mall.Order;
import com.fruit.model.mall.OrderDe;
import com.fruit.transaction.OrderService;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanlei6 on 2016/11/1.
 */
public class OrderController extends BaseController {

    @RequiresPermissions(value = {"/mall/order"})
    public void index() {
        render("order_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void auditIndex() {
        render("order_audit_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void sendIndex() {
        render("order_send_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void getListData() {
        String status = this.getPara("status");
        String search = this.getPara("search");
        String select = "select t1.*,t2.cusName,t2.phone,t2.wechat,t2.upCode,t2.saler,t3.addr";
        StringBuilder from = new StringBuilder("from od_order t1 INNER JOIN mall_customer t2 on t1.customer = t2.cusCode INNER JOIN od_order_addr t3 on t1.orderID = t3.orderID where 1=1");
        List<String> params = new ArrayList<String>();
        if (!StringUtils.isEmpty(status)) {
            from.append(" and t1.status=?");
            params.add(status);
        }
        if (!StringUtils.isEmpty(search)) {
            from.append(" and (t1.orderID = ? OR t2.cusCode = ?)");
            params.add(search);
            params.add(search);
        }
        from.append(" order by t1.odtime desc");
        Page<Order> pageInfo = Order.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void orderDetail() {
        String orderID = this.getPara("oid");
        String select = "select t1.*,t2.skuName,t2.specName";
        StringBuilder from = new StringBuilder("from od_order_de t1 INNER JOIN mall_sku t2 on t1.sku = t2.sku where 1=1");
        List<String> params = new ArrayList<String>();
        from.append(" ").append("and t1.orderID = ?");
        params.add(orderID);
        Page<OrderDe> pageInfo = OrderDe.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void audit() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        String orderID = this.getPara("orderID");
        Integer status = this.getParaToInt("status");
        OrderService orderService = Duang.duang(OrderService.class);
        orderService.auditOrder(orderID, status, null, null, sysUser.getName());
        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void send() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        String orderID = this.getPara("orderID");
        String express = this.getPara("express");
        String courierNum = this.getPara("courierNum");
        OrderService orderService = Duang.duang(OrderService.class);
        orderService.auditOrder(orderID, 3, express, courierNum, sysUser.getName());
        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void view() {
        String orderID = this.getPara("orderID");
        Order order = Order.dao.findFirst("SELECT t1.*,t2.cusName,t3.addr FROM od_order t1 INNER JOIN mall_customer t2 ON t1.customer = t2.cusCode INNER JOIN od_order_addr t3 on t1.orderID = t3.orderID WHERE t1.orderID=?", orderID);
        setAttr("order", order);
        setAttr("category", "view");
        render("order_view.jsp");
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void auditView() {
        String orderID = this.getPara("orderID");
        Order order = Order.dao.findFirst("SELECT t1.*,t2.cusName,t3.addr FROM od_order t1 INNER JOIN mall_customer t2 ON t1.customer = t2.cusCode INNER JOIN od_order_addr t3 on t1.orderID = t3.orderID WHERE t1.orderID=?", orderID);
        setAttr("order", order);
        setAttr("category", "view");
        render("order_audit_view.jsp");
    }

    @RequiresPermissions(value = {"/mall/order"})
    public void sendView() {
        String orderID = this.getPara("orderID");
        Order order = Order.dao.findFirst("SELECT t1.*,t2.cusName,t3.addr FROM od_order t1 INNER JOIN mall_customer t2 ON t1.customer = t2.cusCode INNER JOIN od_order_addr t3 on t1.orderID = t3.orderID WHERE t1.orderID=?", orderID);
        setAttr("order", order);
        setAttr("category", "view");
        render("order_send_view.jsp");
    }
}
