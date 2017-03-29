package com.fruit.transaction;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.NumberUtils;
import com.fruit.model.ConsoleSequence;
import com.fruit.model.mall.Customer;
import com.fruit.model.mall.Order;
import com.fruit.model.mall.OrderDe;
import com.fruit.model.mall.UserRated;
import com.fruit.model.stock.SkuInboundDe;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by hanlei6 on 2016/11/2.
 */
public class OrderService {

    @Before(Tx.class)
    public void auditOrder(String orderID, int status, String express, String courierNum, String reviewer) {
        if (status == 2) {
            Order order = Order.dao.findById(orderID);
            CustomerRatedService customerRatedService = Duang.duang(CustomerRatedService.class);
            customerRatedService.create(orderID, order.getCustomer());
        }
        BigDecimal cost = new BigDecimal(0);
        if (status == 3) {
            Order order = Order.dao.findById(orderID);
            List<OrderDe> orderDes = OrderDe.dao.find("SELECT * FROM od_order_de WHERE orderID=?", orderID);
            SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
            for (OrderDe orderDe : orderDes) {
                int quantity = orderDe.getQuantity();
                String sku = orderDe.getSku();
                orderDe.setAllcost(skuInboundService.subtractLeftQty(sku, quantity));
                cost = cost.add(orderDe.getAllcost());
            }
            Db.update("update mall_customer set amount = amount + ? where cusCode=?", order.getAmount(), order.getCustomer());
            Db.batchUpdate(orderDes, 20);
        }
        if (status == 4) { // 回退库存
            // FIXME: 2017/2/6 自动生成入库单
            SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
            String inboundID = ConsoleSequence.dao.generateSequence("入库");
            skuInboundService.saveInbound(inboundID,reviewer);
            List<OrderDe> orderDes = OrderDe.dao.find("SELECT * FROM od_order_de WHERE orderID=?", orderID);
            for (OrderDe orderDe : orderDes) {
                SkuInboundDe skuInboundDe = SkuInboundDe.dao.clear().set("inboundID", inboundID).set("sku", orderDe.getSku()).set("status", 1).set("quantity", orderDe.getQuantity()).set("cost", orderDe.getAllcost().divide(new BigDecimal(orderDe.getQuantity()))).set("leftQty", orderDe.getQuantity());
                skuInboundService.saveInboundDetail(skuInboundDe);
//                Db.update("UPDATE sk_stock SET quantity=quantity + ? WHERE sku=?", orderDe.getQuantity(), orderDe.getSku());
            }

        }

        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("orderID", Operators.EQ, orderID));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        if (!StringUtils.isEmpty(express)) {
            params.put("express", express);
        }
        if (!StringUtils.isEmpty(courierNum)) {
            params.put("courierNum", courierNum);
        }
        params.put("reviewer", reviewer);
        if (cost.doubleValue() > 0.00d) { // 更新订单总成本
            params.put("cost", cost);
        }
        Order.dao.clear().update(conditions, params);
    }

    @Before(Tx.class)
    public void rated(Long searchUser, String searchYear, String searchMonth) {
        List<Order> orders = Order.dao.find("select t1.*,t2.rate from od_order t1 INNER JOIN mall_customer t2 on t1.customer = t2.cusCode where t1.status in (1,2,3) and t1.rated is null and t2.saler=? and t1.odtime like ?", searchUser, searchYear + '-' + searchMonth + "%");
        BigDecimal amount = new BigDecimal(0);
        for (Order order : orders) {
            Customer customer = Customer.dao.findFirst("select * from mall_customer where cusCode=?", order.getCustomer());
            amount = NumberUtils.round(amount.add(order.getAmount().multiply(customer.getRate().divide(new BigDecimal(100)))), 2);
        }
        Db.update("update od_order t1 inner join mall_customer t2 on t1.customer = t2.cusCode set t1.rated= now() where t1.status in (1,2,3) and t1.rated is null and t2.saler= ? and date_format(t1.odtime,'%Y-%m')=?", searchUser, searchYear + '-' + searchMonth);

        UserRated.dao.clear().set("saler", searchUser).set("rated", searchYear + '-' + searchMonth).set("amount", amount).save();
    }
}
