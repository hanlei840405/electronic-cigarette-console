package com.fruit.transaction;

import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.SysUser;
import com.fruit.model.stock.SkStock;
import com.fruit.model.stock.SkuOutbound;
import com.fruit.model.stock.SkuOutboundDe;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hanlei6 on 2016/10/29.
 */
public class SkuOutboundService {

    @Before(Tx.class)
    public void saveOutbound(String outboundID, SkuOutboundDe skuOutboundDe, SysUser sysUser) {
        SkuOutbound.dao.clear().set("outboundID", outboundID).set("executor", sysUser.get("name")).set("extime", new Date()).save();
        skuOutboundDe.save();
        SkStock.dao.subtract(skuOutboundDe.getSku(), skuOutboundDe.getQuantity());
    }

    @Before(Tx.class)
    public void updateOutboundDetail(SkuOutboundDe SkuOutboundDe, long quantity) {
        SkuOutboundDe.update();
        SkStock.dao.subtract(SkuOutboundDe.getSku(), quantity);
    }

    @Before(Tx.class)
    public void saveOutboundDetail(SkuOutboundDe SkuOutboundDe, long quantity) {
        SkuOutboundDe.save();
        SkStock.dao.subtract(SkuOutboundDe.getSku(), quantity);
    }

    @Before(Tx.class)
    public void deleteOutbound(List<String> outboundIDs) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("outboundID", Operators.IN, outboundIDs));
        List<SkuOutboundDe> SkuOutboundDes = SkuOutboundDe.dao.getList(1, outboundIDs.size(), conditions);
        for (SkuOutboundDe SkuOutboundDe : SkuOutboundDes) {
            SkStock.dao.add(SkuOutboundDe.getSku(), SkuOutboundDe.getQuantity());
        }

        SkuOutbound.dao.delete(conditions);
        SkuOutboundDe.dao.delete(conditions);
    }

    @Before(Tx.class)
    public void deleteOutboundDetail(List<Long> ids) {
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("id", Operators.IN, ids));
        List<SkuOutboundDe> SkuOutboundDes = SkuOutboundDe.dao.getList(1, ids.size(), conditions);
        for (SkuOutboundDe SkuOutboundDe : SkuOutboundDes) {
            SkStock.dao.add(SkuOutboundDe.getSku(), SkuOutboundDe.getQuantity());
        }
        SkuOutboundDe.dao.delete(conditions);
    }
}
