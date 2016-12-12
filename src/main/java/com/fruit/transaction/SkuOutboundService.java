package com.fruit.transaction;

import com.fruit.model.SysUser;
import com.fruit.model.stock.SkStock;
import com.fruit.model.stock.SkuOutbound;
import com.fruit.model.stock.SkuOutboundDe;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hanlei6 on 2016/10/29.
 */
public class SkuOutboundService {

    @Before(Tx.class)
    public void saveOutbound(String outboundID, SkuOutboundDe skuOutboundDe, SysUser sysUser) {
        SkuOutbound.dao.clear().set("outboundID", outboundID).set("executor", sysUser.get("name")).set("extime", new Date()).save();
        SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
        BigDecimal allcost = skuInboundService.subtractLeftQty(skuOutboundDe.getSku(), skuOutboundDe.getQuantity());
        skuOutboundDe.set("allcost", allcost).save();
        SkStock.dao.subtract(skuOutboundDe.getSku(), skuOutboundDe.getQuantity());
    }

    @Before(Tx.class)
    public void saveOutboundDetail(SkuOutboundDe skuOutboundDe, long quantity) {
        SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
        BigDecimal allcost = skuInboundService.subtractLeftQty(skuOutboundDe.getSku(), skuOutboundDe.getQuantity());
        skuOutboundDe.set("allcost", allcost).save();
        SkStock.dao.subtract(skuOutboundDe.getSku(), quantity);
    }
}
