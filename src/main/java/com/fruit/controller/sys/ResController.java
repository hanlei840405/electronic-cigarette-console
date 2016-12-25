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
package com.fruit.controller.sys;


import com.fruit.core.auth.anno.RequiresPermissions;
import com.fruit.core.controller.BaseController;
import com.fruit.core.view.InvokeResult;
import com.fruit.model.SysRes;
import com.jfinal.kit.JsonKit;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ResController extends BaseController {

    @RequiresPermissions(value = {"/sys/res"})
    public void index() {
        this.renderJsp("res_index.jsp");

    }

    @RequiresPermissions(value = {"/sys/res"})
    public void getTreeGridView() {
        this.renderJson(SysRes.me.getTreeGridView());
    }

    @RequiresPermissions(value = {"/sys/res"})
    public void add() {
        Integer id = getParaToInt("id");
        if (id != null) {
            SysRes sysRes = SysRes.me.getById(id);
            if (sysRes != null) {
                Integer pid = (Integer) sysRes.getInt("pid");
                if (pid != null) {//获取父资源
                    SysRes pRes = SysRes.me.getById(pid);
                    setAttr("pRes", pRes);
                }
            }
            setAttr("sysRes", sysRes);
        }
        setAttr("jsonTree", JsonKit.toJson(SysRes.me.getZtreeViewList()));
        this.renderJsp("res_add.jsp");
    }


    @RequiresPermissions(value = {"/sys/res"})
    public void saveRes() throws UnsupportedEncodingException {
        Integer id = getParaToInt("id");
        String name = getPara("name");
        String seq = getPara("seq");
        String url = getPara("url");
        String iconCls = getPara("iconCls");
        Integer type = getParaToInt("type");
        Integer pId = getParaToInt("pid");
        int result = SysRes.me.saveRes(id, name, seq, url, iconCls, type, pId);
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("code", "success");
        if (result == 1) {
            mapData.put("result", "添加成功！！");
        } else if (result == 2) {
            mapData.put("result", "编辑成功！！");
        }
        this.renderJson(mapData);
    }

    @RequiresPermissions(value = {"/sys/res"})
    public void setEnabled() {
        Integer resId = getParaToInt("resId");
        Integer status = getParaToInt("status");
        SysRes.me.setEnabled(resId, status);
        this.renderJson(InvokeResult.success());
    }
}





