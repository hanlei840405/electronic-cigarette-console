package com.fruit.transaction;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.mall.AsAftersaleod;
import com.fruit.model.mall.AsAftersaleodDe;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by hanlei6 on 2016/11/2.
 */
public class AfterSaleService {

    @Before(Tx.class)
    public void send(String asodID, String bkcourierNum, List<AsAftersaleodDe> asAftersaleodDes) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("asodID", Operators.EQ, asodID));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", 2);
        if (!StringUtils.isEmpty(bkcourierNum)) {
            params.put("bkcourierNum", bkcourierNum);
        }
        SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
        BigDecimal amount = new BigDecimal(0);
        for (AsAftersaleodDe asAftersaleodDe : asAftersaleodDes) {
            int quantity = asAftersaleodDe.getNewqty();
            String sku = asAftersaleodDe.getSku();
            amount.add(skuInboundService.subtractLeftQty(sku, quantity));
        }
        params.put("amount", amount);
        AsAftersaleod.dao.update(conditions, params);
        Db.batchUpdate(asAftersaleodDes, 20);
    }
}
