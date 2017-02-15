/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fruit.model.mall;

import com.fruit.core.model.Condition;
import com.fruit.core.model.Operators;
import com.fruit.core.view.ZtreeView;
import com.fruit.model.base.BaseMallCategory;

import java.util.*;

/**
 * @author eason
 *         商品类别
 */
public class Category extends BaseMallCategory<Category> {
    /**
     *
     */
    private static final long serialVersionUID = -1982696969221258167L;
    public static Category me = new Category();


    /**
     * 获取商品分类
     *
     * @return
     * @author eason
     */
    public List<Category> getRootCategoryList() {

        return this.paginate(1, 10000, "select *", "from mall_category where parentCode is null").getList();
    }

    /**
     * 获取类目树结构，暂定两级结构
     *
     * @return
     */
    public Map<String, Object> getTreeGridView() {
        Map<String, Object> rows = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
        orderby.put("cateCode", "asc");
        Set<Condition> conditions = new HashSet<Condition>();
        conditions.add(new Condition("parentCode", Operators.EQ, null));
        List<Category> categories = this.getList(1, 10000, conditions, orderby);
        for (Category item : categories) {
            Map<String, Object> mapItem = new HashMap<String, Object>();
            mapItem.put("id", item.getLong("id"));
            mapItem.put("cateCode", item.getStr("cateCode"));
            mapItem.put("cateName", item.getStr("cateName"));
            mapItem.put("parentCode", item.getStr("parentCode"));
            mapItem.put("sequence", item.getInt("sequence"));
            mapItem.put("remare", item.getStr("remare"));
            mapItem.put("loaded", true);
            mapItem.put("expanded", true);
            mapItem.put("level", 0);
            conditions = new HashSet<Condition>();
            conditions.add(new Condition("parentCode", Operators.EQ, item.getStr("cateCode")));
            List<Category> subCategories = this.getList(1, 10000, conditions, orderby);
            if (subCategories.size() == 0) {
                mapItem.put("isLeaf", true);
            } else {
                mapItem.put("isLeaf", false);
            }
            list.add(mapItem);
            for (Category subItem : subCategories) {
                mapItem = new HashMap<String, Object>();
                mapItem.put("id", subItem.getLong("id"));
                mapItem.put("cateCode", subItem.getStr("cateCode"));
                mapItem.put("cateName", subItem.getStr("cateName"));
                mapItem.put("parentCode", subItem.getStr("parentCode"));
                mapItem.put("sequence", subItem.getInt("sequence"));
                mapItem.put("remare", subItem.getStr("remare"));
                mapItem.put("loaded", true);
                mapItem.put("expanded", true);
                mapItem.put("level", 1);
                mapItem.put("isLeaf", true);
                list.add(mapItem);
            }
        }
        rows.put("rows", list);
        return rows;
    }

    /**
     * 获取上级资源
     *
     * @return
     */
    public List<ZtreeView> getZtreeViewList() {
        List<ZtreeView> ztreeViews = new ArrayList<ZtreeView>();
        List<Category> categories = this.find("select * from mall_category");
        if (categories.size() > 0) {
            int i = 1;
            for (Category category : categories) {
                Category parent = Category.me.findFirst("select * from mall_category where cateCode=?", category.getParentCode());
                Map<String, Object> attribute = new HashMap<String, Object>();
                attribute.put("code", category.getCateCode());
                if (parent != null) {
                    ztreeViews.add(new ZtreeView(category.getLong("id").intValue(), parent.getLong("id").intValue(), category.getStr("cateName"), i == 1 ? true : false, attribute));
                } else {
                    ztreeViews.add(new ZtreeView(category.getLong("id").intValue(), null, category.getStr("cateName"), i == 1 ? true : false, attribute));
                }
            }
            i++;
        }
        return ztreeViews;
    }
}
