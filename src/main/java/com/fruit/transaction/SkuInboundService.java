package com.fruit.transaction;

import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.model.SysUser;
import com.fruit.model.stock.SkStock;
import com.fruit.model.stock.SkuInbound;
import com.fruit.model.stock.SkuInboundDe;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;
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
        SkStock.dao.reset(skuInboundDe.getSku(), quantity);
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

    /**
     * 获取quantity数量的最新总成本，并将quantity占用的入库数量做减法，根据FIFO原则，逐条抵扣数量，直至抵扣为0为止
     *
     * @param sku
     * @param quantity
     * @return
     */
    @Before(Tx.class)
    public BigDecimal subtractLeftQty(String sku, int quantity) {
        BigDecimal allcost = new BigDecimal(0);
        while (quantity > 0) {
            SkuInboundDe skuInboundDe = SkuInboundDe.dao.findFirst("SELECT t1.* FROM sku_inbound_de t1 INNER JOIN sku_inbound t2 ON t1.inboundID = t2.inboundID WHERE sku=? AND status = 1 ORDER BY extime", sku);
            if (skuInboundDe != null) {
                Integer leftQty = skuInboundDe.getLeftQty();
                Long id = skuInboundDe.getId();
                // 计算总成本
                if (leftQty - quantity > 0) {
                    // 入库明细单库存数量大于订单数量
                    allcost = allcost.add(skuInboundDe.getCost().multiply(BigDecimal.valueOf(quantity)));
                    // 设置入库单剩余数量
                    Db.update("UPDATE sku_inbound_de SET leftQty=leftQty - ? WHERE id=?", quantity, id);
                } else {
                    // 如果入库数量小于或等于订单数量，将入库明细单状态设置为0,剩余数量设置为0
                    Db.update("UPDATE sku_inbound_de SET status=0, leftQty=0 WHERE id=?", id);
                    allcost = allcost.add(skuInboundDe.getCost().multiply(BigDecimal.valueOf(leftQty)));
                }
                // 订单中待分配的sku数量
                quantity -= leftQty;
            } else { // 入库单剩余数量不足
                quantity = 0;
                allcost = new BigDecimal(0);
            }
        }
        return allcost;
    }
}
