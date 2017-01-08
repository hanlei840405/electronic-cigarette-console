package com.fruit.controller.mall;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.mall.AsAftersaleod;
import com.fruit.model.mall.AsAftersaleodDe;
import com.fruit.transaction.AfterSaleService;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Page;

import java.util.*;

/**
 * Created by hanlei6 on 2016/11/1.
 */
public class AfterSaleController extends BaseController {

    @RequiresPermissions(value = {"/mall/aftersale"})
    public void index() {
        render("aftersale_index.jsp");
    }

    @RequiresPermissions(value = {"/mall/aftersale"})
    public void getListData() {
        String search = this.getPara("search");
        String select = "select t1.*,t2.cusName";
        StringBuilder from = new StringBuilder("from as_aftersaleod t1 INNER JOIN mall_customer t2 on t1.customer = t2.cusCode where 1=1");
        List<String> params = new ArrayList<String>();
        if (!StringUtils.isEmpty(search)) {
            from.append(" and (t1.asodID = ? OR t2.cusCode = ?)");
            params.add(search);
            params.add(search);
        }
        from.append(" order by t1.asodID desc");
        Page<AsAftersaleod> pageInfo = AsAftersaleod.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/aftersale"})
    public void detail() {
        String asodID = this.getPara("asodID");
        String select = "select t1.*,t2.skuName,t2.specName";
        StringBuilder from = new StringBuilder("from as_aftersaleod_de t1 INNER JOIN mall_sku t2 on t1.sku = t2.sku where 1=1");
        List<String> params = new ArrayList<String>();
        from.append(" ").append("and t1.asodID = ?");
        params.add(asodID);
        Page<AsAftersaleodDe> pageInfo = AsAftersaleodDe.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }

    @RequiresPermissions(value = {"/mall/aftersale"})
    public void saveReceive() {
        String asodID = this.getPara("asodID");
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("asodID", Operators.EQ, asodID));
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("status", 1);
        AsAftersaleod.dao.clear().update(conditions, values);
        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/aftersale"})
    public void saveSend() {
        String asodID = this.getPara("asodID");
        String skus = this.getPara("skus");
        List<Map<String, String>> list = (List<Map<String, String>>) JSONUtils.parse(skus);
        List<AsAftersaleodDe> asAftersaleodDes = new ArrayList<AsAftersaleodDe>();
        for (Map<String, String> map : list) {
            Long id = Long.valueOf(map.get("id"));
            Integer newQty = Integer.valueOf(map.get("newQty"));
            AsAftersaleodDe asAftersaleodDe = AsAftersaleodDe.dao.findById(id);
            if (newQty < 0) {
                this.renderJson(InvokeResult.failure("商品[" + asAftersaleodDe.getSku() + "]换新数量需大于等于0"));
                return;
            }
            if (newQty > asAftersaleodDe.getQuantity()) {
                this.renderJson(InvokeResult.failure("商品[" + asAftersaleodDe.getSku() + "]换新数量超过退回数量"));
                return;
            }
            asAftersaleodDe.setNewqty(newQty);
            asAftersaleodDes.add(asAftersaleodDe);
        }
        String bkcourierNum = this.getPara("bkcourierNum");
        AfterSaleService afterSaleService = Duang.duang(AfterSaleService.class);
        try {
            afterSaleService.send(asodID, bkcourierNum, asAftersaleodDes);
        } catch (Exception e) {
            this.renderJson(InvokeResult.failure(e.getMessage()));
            return;
        }
        this.renderJson(InvokeResult.success());
    }

    @RequiresPermissions(value = {"/mall/aftersale"})
    public void receive() {
        String asodID = this.getPara("asodID");
        AsAftersaleod asAftersaleod = AsAftersaleod.dao.findFirst("SELECT t1.*,t2.cusName FROM as_aftersaleod t1 INNER JOIN mall_customer t2 ON t1.customer = t2.cusCode WHERE t1.asodID=?", asodID);
        setAttr("asAftersaleod", asAftersaleod);
        render("aftersale_receive.jsp");
    }

    @RequiresPermissions(value = {"/mall/aftersale"})
    public void send() {
        String asodID = this.getPara("asodID");
        AsAftersaleod asAftersaleod = AsAftersaleod.dao.findFirst("SELECT t1.*,t2.cusName FROM as_aftersaleod t1 INNER JOIN mall_customer t2 ON t1.customer = t2.cusCode WHERE t1.asodID=?", asodID);
        setAttr("asAftersaleod", asAftersaleod);
        render("aftersale_send.jsp");
    }

    @RequiresPermissions(value = {"/mall/aftersale"})
    public void view() {
        String asodID = this.getPara("asodID");
        AsAftersaleod asAftersaleod = AsAftersaleod.dao.findFirst("SELECT t1.*,t2.cusName FROM as_aftersaleod t1 INNER JOIN mall_customer t2 ON t1.customer = t2.cusCode WHERE t1.asodID=?", asodID);
        setAttr("asAftersaleod", asAftersaleod);
        render("aftersale_view.jsp");
    }
}
