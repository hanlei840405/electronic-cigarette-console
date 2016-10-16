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
package com.fruit.core.view;

import java.util.HashMap;
import java.util.Map;

/**
 * ztree树
 */
public class ZtreeView {

    private Integer id;

    private Integer pId;

    private String name;

    private boolean open;

    private boolean checked = false;

    private Map<String, Object> attribute = new HashMap<String, Object>();

    public ZtreeView() {
    }

    public ZtreeView(Integer id, Integer pId, String name, boolean open) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
    }

    public ZtreeView(Integer id, Integer pId, String name, boolean open, Map<String, Object> attribute) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
        this.attribute = attribute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Map<String, Object> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, Object> attribute) {
        this.attribute = attribute;
    }
}
