package com.fruit.transaction;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.mall.Order;
import com.fruit.model.mall.OrderDe;
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
    public void auditOrder(String orderID, int status, String express, String courierNum) {

        BigDecimal cost = new BigDecimal(0);
        if (status == 3) {
            List<OrderDe> orderDes = OrderDe.dao.find("SELECT * FROM od_order_de WHERE orderID=?", orderID);
            SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
            for (OrderDe orderDe : orderDes) {
                int quantity = orderDe.getQuantity();
                String sku = orderDe.getSku();
                orderDe.setAllcost(skuInboundService.subtractLeftQty(sku, quantity));
                cost.add(orderDe.getAllcost());
            }

            Db.batchUpdate(orderDes, 20);
        }
        if (status == 4) { // 回退库存
            List<OrderDe> orderDes = OrderDe.dao.find("SELECT * FROM od_order_de WHERE orderID=?", orderID);
            for (OrderDe orderDe : orderDes) {
                Db.update("UPDATE sk_stock SET quantity=quantity + ? WHERE sku=?", orderDe.getQuantity(), orderDe.getSku());
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
        if (cost.doubleValue() > 0.00d) { // 更新订单总成本
            params.put("cost", cost);
        }
        Order.dao.update(conditions, params);
    }
}
