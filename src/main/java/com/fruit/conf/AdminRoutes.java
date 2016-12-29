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
package com.fruit.conf;

import com.fruit.controller.ImageController;
import com.fruit.controller.IndexController;
import com.fruit.controller.app.AppVersionController;
import com.fruit.controller.mall.*;
import com.fruit.controller.stock.InboundController;
import com.fruit.controller.stock.OutboundController;
import com.fruit.controller.stock.StockController;
import com.fruit.controller.sys.*;
import com.jfinal.config.Routes;

/**
 * 后台管理Routes配置
 *
 * @author eason
 */
public class AdminRoutes extends Routes {

    @Override
    public void config() {
        add("/", IndexController.class, "/WEB-INF/view");
        add("/sys/log", LogController.class, "/WEB-INF/view/sys");
        add("/sys/res", ResController.class, "/WEB-INF/view/sys");
        add("/sys/user", UserController.class, "/WEB-INF/view/sys");
        add("/sys/role", RoleController.class, "/WEB-INF/view/sys");
        add("/dict", DictController.class, "/WEB-INF/view/sys/dict");
        add("/app", AppVersionController.class, "/WEB-INF/view/app");
        add("/image", ImageController.class, "/WEB-INF/view/image");
        add("/mall/category", CategoryController.class, "/WEB-INF/view/mall");
        add("/mall/sku", SkuController.class, "/WEB-INF/view/mall");
        add("/mall/customer", CustomerController.class, "/WEB-INF/view/mall");
        add("/stock/inbound", InboundController.class, "/WEB-INF/view/stock");
        add("/stock/outbound", OutboundController.class, "/WEB-INF/view/stock");
        add("/stock/stock", StockController.class, "/WEB-INF/view/stock");
        add("/mall/order", OrderController.class, "/WEB-INF/view/mall");
        add("/mall/aftersale", AfterSaleController.class, "/WEB-INF/view/mall");
        add("/mall/rate", AfterSaleController.class, "/WEB-INF/view/mall");
    }

}
