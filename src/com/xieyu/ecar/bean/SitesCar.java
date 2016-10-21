package com.xieyu.ecar.bean;

import java.io.Serializable;

public class SitesCar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5445961334757063967L;
	private String id;// 15,
    private String createTime;// private String 2016-05-30 20:03:16 ,
    private String updateTime;// private String 2016-10-18 11:30:54 ,
    private String onDeleted;// false,
    private String orderNo;// null,
    private String remarks;// ,
    private String sn;//  106,
    private String license;//  苏E.8W09Y,
    private String deviceNumber;//  116232100003020 ,
    private String serviceTime;//  2016-03-23 ,
    private String carStatus;// 可用 ,
    private String carCategoryPath;// null,
    private String carFrameNumber;// "LJ8E3A1M8GE001301",
    private String carEngineNumber;// "DWS135B1315120389"
    private CarCategory carCategory = new CarCategory();
    private Sites site = new Sites();
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getOnDeleted() {
		return onDeleted;
	}
	public void setOnDeleted(String onDeleted) {
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
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	public String getCarStatus() {
		return carStatus;
	}
	public void setCarStatus(String carStatus) {
		this.carStatus = carStatus;
	}
	public String getCarCategoryPath() {
		return carCategoryPath;
	}
	public void setCarCategoryPath(String carCategoryPath) {
		this.carCategoryPath = carCategoryPath;
	}
	public String getCarFrameNumber() {
		return carFrameNumber;
	}
	public void setCarFrameNumber(String carFrameNumber) {
		this.carFrameNumber = carFrameNumber;
	}
	public String getCarEngineNumber() {
		return carEngineNumber;
	}
	public void setCarEngineNumber(String carEngineNumber) {
		this.carEngineNumber = carEngineNumber;
	}
	public CarCategory getCarCategory() {
		return carCategory;
	}
	public void setCarCategory(CarCategory carCategory) {
		this.carCategory = carCategory;
	}
	public Sites getSite() {
		return site;
	}
	public void setSite(Sites site) {
		this.site = site;
	}
    
}
