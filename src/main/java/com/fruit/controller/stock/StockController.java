package com.fruit.controller.stock;

import com.alibaba.druid.util.StringUtils;
import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.util.JqGridModelUtils;
import com.fruit.model.stock.SkStock;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

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
        String select = "select t1.*,t2.skuName,t2.specName";
        StringBuilder from = new StringBuilder("from sk_stock t1 INNER JOIN mall_sku t2 on t1.sku = t2.sku where 1=1");
        List<String> params = new ArrayList<String>();
        if (!StringUtils.isEmpty(searchSku)) {
            from.append(" and (t1.sku = ? OR t2.skuName = ?)");
            params.add(searchSku);
            params.add(searchSku);
        }
        Page<SkStock> pageInfo = SkStock.dao.getPage(getPage(), this.getRows(), select, from.toString(), null, params.toArray());
        this.renderJson(JqGridModelUtils.toJqGridView(pageInfo));
    }
}
