package com.fruit.model.base;

import com.fruit.core.model.BaseModel;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCustomerRatedDe<M extends BaseCustomerRatedDe<M>> extends BaseModel<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setRatedID(Integer ratedID) {
		set("ratedID", ratedID);
	}

	public Integer getRatedID() {
		return get("ratedID");
	}

	public void setOrderDeID(Integer orderDeID) {
		set("orderDeID", orderDeID);
	}

	public Integer getOrderDeID() {
		return get("orderDeID");
	}

	public void setSku(String sku) {
		set("sku", sku);
	}

	public String getSku() {
		return get("sku");
	}

	public void setAmount(java.math.BigDecimal amount) {
		set("amount", amount);
	}

	public java.math.BigDecimal getAmount() {
		return get("amount");
	}

}
