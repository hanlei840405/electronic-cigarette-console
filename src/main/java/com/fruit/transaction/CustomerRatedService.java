package com.fruit.transaction;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.mall.*;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by JesseHan on 2017/1/21.
 */
public class CustomerRatedService {

    /**
     * 计算供应商提成或上家礼品
     * @param orderId
     * @param customer
     */
    @Before(Tx.class)
    public void create(String orderId, String customer) {
        Customer cus = Customer.dao.findFirst("SELECT * FROM mall_customer WHERE cusCode=?", customer);
        if (StringUtils.isEmpty(cus.getUpCode())) {
            return;
        } else if ("0".equals(cus.getAgency())) { // 非经销商
            Long count = Db.queryLong("SELECT COUNT(*) AS cnt FROM od_order WHERE customer=? AND status IN ('2','3')", customer);
            if (count == 0) { // 只在下家首次购买时发放礼物
                CustomerGift.dao.clear().set("customer", cus.getUpCode()).set("relationship", customer).set("orderID", orderId).set("status", "0").save();
            }
            return;
        }
        Record customerRated = new Record().set("customer", cus.getUpCode()).set("orderID", orderId).set("status", "0");
        Db.save("customerRated", customerRated);
        List<OrderDe> orderDetails = OrderDe.dao.find("SELECT * FROM od_order_de WHERE orderID=?", orderId);

        BigDecimal totalAmount = new BigDecimal(0);
        List<CustomerRatedDe> customerRatedDes = new ArrayList<CustomerRatedDe>();
        for (OrderDe orderDe : orderDetails) {
            BigDecimal amount;
            CustomerRatedDe customerRatedDe = new CustomerRatedDe();
            // 查询该商户的商品有没有配置与上级商家的固定提成
            CustomerUpRated customerUpRated = CustomerUpRated.dao.clear().findFirst("SELECT * FROM customer_up_rated WHERE customer=? AND sku=? AND up=?", customer, orderDe.getSku(), cus.getUpCode());
            if (customerUpRated != null) { // 从配置中获取固定提成
                amount = customerUpRated.getRated();
            } else { // 下线的最低价格减去上线的最高价格
                BigDecimal price = orderDe.getPrice();
                SkuSprice skuSprice = SkuSprice.dao.clear().findFirst("SELECT * FROM mall_sku_sprice WHERE sku=? AND customer=?", orderDe.getSku(), customer);
                if (skuSprice.getPrice() != null) {
                    amount = price.subtract(skuSprice.getPrice());
                } else {
                    SkuNprice skuNprice = SkuNprice.dao.clear().findFirst("SELECT * FROM mall_sku_nprice WHERE sku=? AND priceType=?", orderDe.getSku(), cus.getPriceType());
                    amount = price.subtract(skuNprice.getPrice1());
                }
            }
            totalAmount = totalAmount.add(amount.doubleValue() > 0d ? amount.multiply(new BigDecimal(orderDe.getQuantity())) : new BigDecimal(0));
            customerRatedDe.setOrderDeID(orderDe.getId());
            customerRatedDe.setRatedID(customerRated.getLong("id"));
            customerRatedDe.setSku(orderDe.getSku());
            customerRatedDe.setAmount(amount.multiply(new BigDecimal(orderDe.getQuantity())));
            customerRatedDes.add(customerRatedDe);
        }

        Db.batchSave(customerRatedDes, customerRatedDes.size());
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("customer", Operators.EQ, cus.getUpCode()));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", totalAmount);
        CustomerRated.dao.clear().update(conditions, params);
    }
}
