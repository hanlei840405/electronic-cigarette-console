package com.fruit.controller.stock;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.model.stock.SkStock;
import com.jfinal.plugin.activerecord.Page;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hanlei6 on 2016/10/29.
 */
public class StockController extends BaseController {

    @RequiresPermissions(value = {"/stock/stock"})
    public void index() {
        render("stock_index.jsp");
    }

    @RequiresPermissions(value = {"/stock/stock"})
    public void getListData() {
        String searchSku = this.getPara("searchSku");
        Set<Condition> conditions = new HashSet<Condition>();
        if (!StringUtils.isEmpty(searchSku)) {
            conditions.add(new Condition("sku", Operators.EQ, searchSku));
        }
        Page<SkStock> pageInfo = SkStock.dao.getPage(getPage(), this.getRows(), conditions);
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }
}
