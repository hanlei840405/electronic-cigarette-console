package com.fruit.controller.mall;

import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.IWebUtils;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.SysUser;
import com.fruit.model.mall.Order;
import com.fruit.model.mall.UserRated;
import com.fruit.transaction.OrderService;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hanlei6 on 2016/10/22.
 */
public class RateController extends BaseController {

    @RequiresPermissions(value = {"/mall/rate"})
    public void index() {
        List<SysUser> sysUsers = SysUser.me.find("select * from sys_user where status<>2");
        setAttr("sysUsers", sysUsers);
        render("rate_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/rate"})
    public void view() {
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        setAttr("userId", sysUser.getId());
        render("rate_view.jsp");
    }

    @RequiresPermissions(value = {"/mall/rate"})
    public void viewRoot() {
        List<SysUser> sysUsers = SysUser.me.find("select * from sys_user where status<>2");
        setAttr("sysUsers", sysUsers);
        render("rate_root_view.jsp");
    }

    /**
     * 查询等待计提的订单
     */
    @RequiresPermissions(value = {"/mall/rate"})
    public void getListData() {
        Long searchUser = this.getParaToLong("search_user");
        String searchYear = this.getPara("search_year");
        String searchMonth = this.getPara("search_month");
        String select = "select t1.*,t2.cusName,t2.phone,t2.wechat,t3.addr";
        StringBuilder from = new StringBuilder("from od_order t1 INNER JOIN mall_customer t2 on t1.customer = t2.cusCode INNER JOIN od_order_addr t3 on t1.orderID = t3.orderID where t1.status in (1,2,3) and t1.rated is null and t2.saler=? and t1.odtime like ? order by t1.odtime desc");
        List<Object> params = new ArrayList<Object>();
        params.add(searchUser);
        params.add(searchYear + "-" + searchMonth + "%");
        Page<Order> pageInfo = Order.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    /**
     * 查询已经计提的数据
     */
    @RequiresPermissions(value = {"/mall/rate"})
    public void getRatedData() {
        Long searchUser = this.getParaToLong("search_user");
        String searchYear = this.getPara("search_year");
        String searchMonth = this.getPara("search_month");
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("saler", Operators.EQ, searchUser));
        conditions.add(new Condition("rated", Operators.EQ, searchYear + "-" + searchMonth));
        Page<UserRated> pageInfo = UserRated.dao.getPage(getPage(), this.getRows(), conditions);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/rate"})
    public void rated() {
        Long searchUser = this.getParaToLong("search_user");
        String searchYear = this.getPara("search_year");
        String searchMonth = this.getPara("search_month");
        OrderService orderService = Duang.duang(OrderService.class);
        try {
            orderService.rated(searchUser, searchYear, searchMonth);
        } catch (Exception e) {
            this.renderJson(InvokeResult.failure("操作失败，联系运维"));
        }
        this.renderJson(InvokeResult.success());
    }
}
