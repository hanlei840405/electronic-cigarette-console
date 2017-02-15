package com.fruit.model.base;

import com.fruit.core.model.BaseModel;
import com.fruit.model.mall.Sku;
import com.jfinal.plugin.activerecord.IBean;

import java.util.List;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseMallCategory<M extends BaseMallCategory<M>> extends BaseModel<M> implements IBean {

    private List<Sku> skus;

    public void setId(Long id) {
        set("id", id);
    }

    public Long getId() {
        return get("id");
    }

    public void setCateName(String cateName) {
        set("cateName", cateName);
    }

    public String getCateName() {
        return get("cateName");
    }

    public void setCateCode(String cateCode) {
        set("cateCode", cateCode);
    }

    public String getCateCode() {
        return get("cateCode");
    }

    public void setParentCode(String parentCode) {
        set("parentCode", parentCode);
    }

    public String getParentCode() {
        return get("parentCode");
    }

    public void setRemare(String remare) {
        set("remare", remare);
    }

    public String getRemare() {
        return get("remare");
    }

    public void setSequence(Integer sequence) {
        set("sequence", sequence);
    }

    public String getSequence() {
        return get("sequence");
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public List<Sku> getSkus() {
        return this.skus;
    }

}
