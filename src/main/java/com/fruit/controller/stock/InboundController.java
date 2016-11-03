package com.fruit.controller.stock;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.IWebUtils;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.ConsoleSequence;
import com.fruit.model.SysUser;
import com.fruit.model.stock.SkuInbound;
import com.fruit.model.stock.SkuInboundDe;
import com.fruit.transaction.SkuInboundService;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by hanlei6 on 2016/10/29.
 */
public class InboundController extends BaseController {

    @RequiresPermissions(value = {"/stock/inbound"})
    public void index() {
        render("inbound_index.jsp");
    }

    @RequiresPermissions(value = {"/stock/inbound"})
    public void add() {
        String inboundID = this.getPara("inboundID");
        setAttr("inboundID", inboundID);
        render("inbound_add.jsp");
    }

    @RequiresPermissions(value = {"/stock/inbound"})
    public void view() {
        String inboundID = this.getPara("inboundID");
        setAttr("inboundID", inboundID);
        render("inbound_view.jsp");
    }

    @RequiresPermissions(value = {"/stock/inbound"})
    public void getListData() {
        String searchExecutor = this.getPara("searchExecutor");
        Page<SkuInbound> pageInfo = SkuInbound.dao.getPage(getPage(), this.getRows(), searchExecutor);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/stock/inbound"})
    public void getDetailData() {
        String inboundID = this.getPara("inboundID");
        String select = "select t1.*,t2.skuName,t2.specName";
        StringBuilder from = new StringBuilder("from sku_inbound_de t1 LEFT JOIN mall_sku t2 on t1.sku = t2.sku where 1=1");
        List<String> params = new ArrayList<String>();
        from.append(" ").append("and t1.inboundID = ?");
        params.add(inboundID);
        Page<SkuInboundDe> pageInfo = SkuInboundDe.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/stock/inbound"})
    public void save() {
        Map<String, Object> result = new HashMap<String, Object>();
        SysUser sysUser = IWebUtils.getCurrentSysUser(getRequest());
        String inboundID = this.getPara("inboundID");
        String sku = this.getPara("sku");
        Integer quantity = this.getParaToInt("quantity");
        Double historicalCost = Double.parseDouble(this.getPara("historicalCost"));
        if (historicalCost * quantity < 0) {
            result.put("code", "500");
            result.put("msg", "入库数量与成本不可一正一负");
            this.renderJson(result);
            return;
        }
        SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
        SkuInboundDe skuInboundDe;


        // 检查表中是否有记录，如果有，合并数据，如果没有，新增数据
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("inboundID", Operators.EQ, inboundID));
        conditions.add(new Condition("sku", Operators.EQ, sku));


        try {
            skuInboundDe = SkuInboundDe.dao.get(conditions);
            if (skuInboundDe != null) {
                if (skuInboundDe.getHistoricalCost().subtract(skuInboundDe.getCost()).intValue() <= 0) {
                    skuInboundDe.setQuantity(skuInboundDe.getQuantity() + quantity);
                    skuInboundDe.setHistoricalCost(skuInboundDe.getHistoricalCost().add(BigDecimal.valueOf(historicalCost)));
                    if (skuInboundDe.getQuantity() < 0 || skuInboundDe.getCost().doubleValue() < 0 || skuInboundDe.getHistoricalCost().doubleValue() < 0) {
                        result.put("code", "500");
                        result.put("msg", "入库单总数量、剩余总成本、出事总成本不能为负");
                        this.renderJson(result);
                        return;
                    }
                }
                skuInboundDe.setCost(skuInboundDe.getCost().add(BigDecimal.valueOf(historicalCost)));
                skuInboundDe.setLeftQty(skuInboundDe.getLeftQty() + quantity);
                skuInboundService.updateInboundDetail(skuInboundDe, quantity);
            } else {
                skuInboundDe = SkuInboundDe.dao.clear().set("inboundID", inboundID).set("sku", sku).set("status", 1).set("quantity", quantity).set("historicalCost", historicalCost).set("cost", historicalCost).set("leftQty", quantity);
                if (quantity < 0 || historicalCost.doubleValue() < 0) {
                    result.put("code", "500");
                    result.put("msg", "入库单总数量、剩余总成本、出事总成本不能为负");
                    this.renderJson(result);
                    return;
                }
                if (StringUtils.isEmpty(inboundID)) { // 需要创建入库单主表信息
                    inboundID = ConsoleSequence.dao.generateSequence("入库");
                    skuInboundDe.setInboundID(inboundID);
                    skuInboundService.saveInbound(inboundID, skuInboundDe, sysUser);
                } else {
                    skuInboundService.saveInboundDetail(skuInboundDe, quantity);
                }
            }
            result.put("code", "200");
            result.put("inboundID", inboundID);
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "保存失败");
        }
        this.renderJson(result);
    }

    @RequiresPermissions(value = {"/mall/inbound"})
    public void delete() {
        String[] array = this.getPara("ids").split(",");
        List<String> inbounds = new ArrayList<String>();
        for (String id : array) {
            inbounds.add(SkuInbound.dao.findById(Long.parseLong(id)).getInboundID());
        }

        SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
        try {
            skuInboundService.deleteInbound(inbounds);
            this.renderJson(InvokeResult.success());
        } catch (Exception e) {
            this.renderJson(InvokeResult.failure("删除失败"));
        }
    }

    @RequiresPermissions(value = {"/mall/inbound"})
    public void deleteDetail() {
        String[] array = this.getPara("ids").split(",");
        List<Long> ids = new ArrayList<Long>();
        for (String id : array) {
            ids.add(Long.parseLong(id));
        }

        SkuInboundService skuInboundService = Duang.duang(SkuInboundService.class);
        try {
            skuInboundService.deleteInboundDetail(ids);
            this.renderJson(InvokeResult.success());
        } catch (Exception e) {
            this.renderJson(InvokeResult.failure("删除失败"));
        }
    }
}
