package com.fruit.transaction;

import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.mall.CustomerGift;
import com.fruit.model.stock.SkStock;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by JesseHan on 2017/1/17.
 */
public class CustomerGiftService {

    @Before(Tx.class)
    public void send(Long id, String gift, Integer quantity) throws Exception {
        SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
        BigDecimal temp = skuInboundService.subtractLeftQty(gift, quantity);
        if (temp.intValue() == 0) { // 入库单剩余数量不足
            throw new Exception("入库单剩余数量不足,无法赠送");
        }
        SkStock.dao.subtract(gift, quantity);

        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("id", Operators.EQ, id));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gift", gift);
        params.put("quantity", quantity);
        params.put("status", 1);
        CustomerGift.dao.clear().update(conditions, params);
    }
}
