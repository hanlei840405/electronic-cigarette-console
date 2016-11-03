package com.fruit.transaction;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.mall.Order;
import com.fruit.model.mall.OrderDe;
import com.fruit.model.stock.SkuInboundDe;
import com.jfinal.aop.Before;
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
        Order.dao.update(conditions, params);

        if (status == 3) {
            List<OrderDe> orderDes = OrderDe.dao.find("SELECT * FROM od_order_de WHERE orderID=?", orderID);
            for (OrderDe orderDe : orderDes) {
                int quantity = orderDe.getQuantity();
                String sku = orderDe.getSku();
                BigDecimal allcost = new BigDecimal(0);
                while (quantity > 0) {
                    SkuInboundDe skuInboundDe = SkuInboundDe.dao.findFirst("SELECT t1.* FROM sku_inbound_de t1 INNER JOIN sku_inbound t2 ON t1.inboundID = t2.inboundID WHERE sku=? AND status = 1 ORDER BY extime LIMIT 1", sku);
                    Integer leftQty = skuInboundDe.getLeftQty();
                    Long id = skuInboundDe.getId();
                    // 计算总成本
                    if (leftQty - quantity > 0) {
                        // 入库明细单库存数量大于订单数量
                        BigDecimal cost = skuInboundDe.getCost().divide(BigDecimal.valueOf(leftQty), 2, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(quantity));
                        allcost = allcost.add(cost);
                        // 设置入库单剩余数量
                        Db.update("UPDATE sku_inbound_de SET leftQty=leftQty - ?, cost = cost - ? WHERE id=?", quantity, cost, id);
                    } else {
                        // 如果入库数量小于或等于订单数量，将入库明细单状态设置为0,剩余数量设置为0
                        Db.update("UPDATE sku_inbound_de SET status=0, leftQty=0, cost=0 WHERE id=?", id);
                        allcost = allcost.add(skuInboundDe.getCost());
                    }
                    // 订单中待分配的sku数量
                    quantity -= leftQty;
                }
                // 减库存
                orderDe.setAllcost(allcost);
            }
            Db.batchUpdate(orderDes, 20);
        }
        if (status == 4) {
            List<OrderDe> orderDes = OrderDe.dao.find("SELECT * FROM od_order_de WHERE orderID=?", orderID);
            for (OrderDe orderDe : orderDes) {
                Db.update("UPDATE sk_stock SET quantity=quantity + ? WHERE sku=?", orderDe.getQuantity(), orderDe.getSku());
            }
        }
    }
}
