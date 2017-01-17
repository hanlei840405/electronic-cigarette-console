package com.fruit.model.base;

import com.fruit.core.model.BaseModel;
import com.jfinal.plugin.activerecord.IBean;

import java.math.BigDecimal;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseMallCustomer<M extends BaseMallCustomer<M>> extends BaseModel<M> implements IBean {

    public void setId(Long id) {
        set("id", id);
    }

    public Long getId() {
        return get("id");
    }

    public void setCusCode(String cusCode) {
        set("cusCode", cusCode);
    }

    public String getCusCode() {
        return get("cusCode");
    }

    public void setCusName(String cusName) {
        set("cusName", cusName);
    }

    public String getCusName() {
        return get("cusName");
    }

    public void setPasswd(String passwd) {
        set("passwd", passwd);
    }

    public String getPasswd() {
        return get("passwd");
    }

    public void setStatus(Integer status) {
        set("status", status);
    }

    public Integer getStatus() {
        return get("status");
    }

    public void setSex(String sex) {
        set("sex", sex);
    }

    public String getSex() {
        return get("sex");
    }

    public void setBirthday(java.util.Date birthday) {
        set("birthday", birthday);
    }

    public java.util.Date getBirthday() {
        return get("birthday");
    }

    public void setEmail(String email) {
        set("email", email);
    }

    public String getEmail() {
        return get("email");
    }

    public void setPhone(String phone) {
        set("phone", phone);
    }

    public String getPhone() {
        return get("phone");
    }

    public void setWechat(String wechat) {
        set("wechat", wechat);
    }

    public String getWechat() {
        return get("wechat");
    }

    public void setUpCode(String upCode) {
        set("upCode", upCode);
    }

    public String getUpCode() {
        return get("upCode");
    }

    public void setSaler(String saler) {
        set("saler", saler);
    }

    public String getSaler() {
        return get("saler");
    }

    public void setRate(BigDecimal rate) {
        set("rate", rate);
    }

    public BigDecimal getRate() {
        return get("rate");

    }

    public void setAmount(java.math.BigDecimal amount) {
        set("amount", amount);
    }

    public java.math.BigDecimal getAmount() {
        return get("amount");
    }

    public void setPriceType(java.lang.String priceType) {
        set("priceType", priceType);
    }

    public java.lang.String getPriceType() {
        return get("priceType");
    }

    public void setCusType(java.lang.String cusType) {
        set("cusType", cusType);
    }

    public java.lang.String getCusType() {
        return get("cusType");
    }

    public void setAgency(java.lang.String agency) {
        set("agency", agency);
    }

    public java.lang.String getAgency() {
        return get("agency");
    }

}
