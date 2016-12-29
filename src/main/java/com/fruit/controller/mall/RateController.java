package com.fruit.controller.mall;

import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.model.SysUser;
import com.fruit.model.mall.Order;
import com.fruit.transaction.OrderService;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void getListData() {
        String searchUser = this.getPara("search_user");
        String searchYear = this.getPara("search_year");
        String searchMonth = this.getPara("search_month");
        String select = "select t1.*,t2.cusName,t2.phone,t2.wechat,t3.addr";
        StringBuilder from = new StringBuilder("from od_order t1 INNER JOIN mall_customer t2 on t1.customer = t2.cusCode INNER JOIN od_order_addr t3 on t1.orderID = t3.orderID where t1.status in (1,2,3) and t1.rated is null and t2.saler=? and t1.odtime like ?");
        List<String> params = new ArrayList<String>();
        params.add(searchUser);
        params.add(searchYear + "-" + searchMonth + "%");
        Page<Order> pageInfo = Order.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/rate"})
    public void rated() {
        String searchUser = this.getPara("search_user");
        String searchYear = this.getPara("search_year");
        String searchMonth = this.getPara("search_month");
        OrderService orderService = Duang.duang(OrderService.class);
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            orderService.rated(searchUser, searchYear + '-' + searchMonth);
            result.put("code", "200");
            result.put("msg", "操作成功");
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "操作失败，联系运维");
        }
        this.renderJson(result);
    }
}
