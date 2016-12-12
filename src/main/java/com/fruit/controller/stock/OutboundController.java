package com.fruit.controller.stock;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.IWebUtils;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.model.ConsoleSequence;
import com.fruit.model.SysUser;
import com.fruit.model.stock.SkStock;
import com.fruit.model.stock.SkuOutbound;
import com.fruit.model.stock.SkuOutboundDe;
import com.fruit.transaction.SkuOutboundService;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;

import java.util.*;

/**
 * Created by hanlei6 on 2016/10/29.
 */
public class OutboundController extends BaseController {

    @RequiresPermissions(value = {"/stock/outbound"})
    public void index() {
        render("outbound_index.jsp");
    }

    @RequiresPermissions(value = {"/stock/outbound"})
    public void add() {
        String outboundID = this.getPara("outboundID");
        setAttr("outboundID", outboundID);
        render("outbound_add.jsp");
    }

    @RequiresPermissions(value = {"/stock/outbound"})
    public void view() {
        String outboundID = this.getPara("outboundID");
        setAttr("outboundID", outboundID);
        render("outbound_view.jsp");
    }

    @RequiresPermissions(value = {"/stock/outbound"})
    public void getListData() {
        String searchExecutor = this.getPara("searchExecutor");

        Set<Condition> conditions = new HashSet<Condition>();
        if (!StringUtils.isEmpty(searchExecutor)) {
            conditions.add(new Condition("executor", Operators.EQ, searchExecutor));
        }
        Page<SkuOutbound> pageInfo = SkuOutbound.dao.getPage(getPage(), this.getRows(), conditions);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/stock/outbound"})
    public void getDetailData() {
        String outboundID = this.getPara("outboundID");
        String select = "select t1.*,t2.skuName,t2.specName";
        StringBuilder from = new StringBuilder("from sku_outbound_de t1 LEFT JOIN mall_sku t2 on t1.sku = t2.sku where 1=1");
        List<String> params = new ArrayList<String>();
        from.append(" ").append("and t1.outboundID = ?");
        params.add(outboundID);
        Page<SkuOutboundDe> pageInfo = SkuOutboundDe.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/stock/outbound"})
    public void save() {
        Map<String, Object> result = new HashMap<String, Object>();
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        String outboundID = this.getPara("outboundID");
        String sku = this.getPara("sku");
        Integer quantity = this.getParaToInt("quantity");

        SkStock skStock = SkStock.dao.findBySku(sku);
        if (skStock.getQuantity() < quantity) {
            result.put("code", "500");
            result.put("msg", "库存不足");
            this.renderJson(result);
            return;
        }

        SkuOutboundService skuOutboundService = Duang.duang(SkuOutboundService.class);

        // 检查表中是否有记录，如果有，合并数据，如果没有，新增数据
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("outboundID", Operators.EQ, outboundID));
        conditions.add(new Condition("sku", Operators.EQ, sku));


        try {
            SkuOutboundDe skuOutboundDe = SkuOutboundDe.dao.get(conditions);
            if (skuOutboundDe != null) {
                result.put("code", "500");
                result.put("msg", "存在重复的商品出库");
                this.renderJson(result);
                return;
            } else {
                skuOutboundDe = SkuOutboundDe.dao.clear().set("outboundID", outboundID).set("sku", sku).set("quantity", quantity);
                if (StringUtils.isEmpty(outboundID)) { // 需要创建入库单主表信息
                    outboundID = ConsoleSequence.dao.generateSequence("入库");
                    skuOutboundDe.setOutboundID(outboundID);
                    skuOutboundService.saveOutbound(outboundID, skuOutboundDe, sysUser);
                } else {
                    skuOutboundService.saveOutboundDetail(skuOutboundDe, quantity);
                }
            }
            result.put("code", "200");
            result.put("outboundID", outboundID);
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "保存失败");
        }
        this.renderJson(result);
    }
}
