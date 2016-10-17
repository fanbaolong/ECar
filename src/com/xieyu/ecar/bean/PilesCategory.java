package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * @author fbl
 * 
 * 充电桩类型分类
 */
public class PilesCategory implements Serializable{
	
	private static final long serialVersionUID = 6348334474316817145L;
	
	private int id;
	
	private String createTime;
	
	private String updateTime;
	
	private boolean onDeleted;
	
	private String orderNo;
	
	private String remarks;
	
	private String name;
	
	private String speedType;
	
	private String electricType;
	
	private int electric;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isOnDeleted() {
		return onDeleted;
	}

	public void setOnDeleted(boolean onDeleted) {
		this.onDeleted = onDeleted;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpeedType() {
		return speedType;
	}

	public void setSpeedType(String speedType) {
		this.speedType = speedType;
	}

	public String getElectricType() {
		return electricType;
	}

	public void setElectricType(String electricType) {
		this.electricType = electricType;
	}

	public int getElectric() {
		return electric;
	}

	public void setElectric(int electric) {
		this.electric = electric;
	}
	
}
