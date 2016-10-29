package com.fruit.transaction;

import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.SysUser;
import com.fruit.model.stock.SkStock;
import com.fruit.model.stock.SkuInbound;
import com.fruit.model.stock.SkuInboundDe;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hanlei6 on 2016/10/29.
 */
public class SkuInboundService {

    @Before(Tx.class)
    public void saveInbound(String inboundID, SkuInboundDe skuInboundDe, SysUser sysUser) {
        SkuInbound.dao.clear().set("inboundID", inboundID).set("executor", sysUser.get("name")).set("extime", new Date()).save();
        skuInboundDe.save();
        SkStock.dao.add(skuInboundDe.getSku(), skuInboundDe.getQuantity());
    }

    @Before(Tx.class)
    public void updateInboundDetail(SkuInboundDe skuInboundDe, long quantity) {
        skuInboundDe.update();
        SkStock.dao.add(skuInboundDe.getSku(), quantity);
    }

    @Before(Tx.class)
    public void saveInboundDetail(SkuInboundDe skuInboundDe, long quantity) {
        skuInboundDe.save();
        SkStock.dao.add(skuInboundDe.getSku(), quantity);
    }

    @Before(Tx.class)
    public void deleteInbound(List<String> inboundIDs) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("inboundID", Operators.IN, inboundIDs));
        List<SkuInboundDe> skuInboundDes = SkuInboundDe.dao.getList(1, inboundIDs.size(), conditions);
        for (SkuInboundDe skuInboundDe : skuInboundDes) {
            SkStock.dao.subtract(skuInboundDe.getSku(), skuInboundDe.getQuantity());
        }

        SkuInbound.dao.delete(conditions);
        SkuInboundDe.dao.delete(conditions);
    }

    @Before(Tx.class)
    public void deleteInboundDetail(List<Long> ids) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("id", Operators.IN, ids));
        List<SkuInboundDe> skuInboundDes = SkuInboundDe.dao.getList(1, ids.size(), conditions);
        for (SkuInboundDe skuInboundDe : skuInboundDes) {
            SkStock.dao.subtract(skuInboundDe.getSku(), skuInboundDe.getQuantity());
        }
        SkuInboundDe.dao.delete(conditions);
    }
}
