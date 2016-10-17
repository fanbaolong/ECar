package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * 扫码后查询的车详情
 * 
 * @author wangfeng
 *
 */
public class CodeCarDetail implements Serializable
{

	private static final long serialVersionUID = -6165482953497962632L;
	/** */
	private String id;
	/** */
	private String createTime;
	/** */
	private String updateTime;
	/** */
	private boolean onDeleted;
	/** */
	private String orderNo;
	/** */
	private String remarks;
	/** */
	private String sn;
	/** */
	private String license;
	/** */
	private String serviceTime;
	/** */
	private String carStatus;
	/** */
	private Sites site = new Sites();
	/** */
	private CarCategory carCategory = new CarCategory();

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}

	public boolean isOnDeleted()
	{
		return onDeleted;
	}

	public void setOnDeleted(boolean onDeleted)
	{
		this.onDeleted = onDeleted;
	}

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public String getSn()
	{
		return sn;
	}

	public void setSn(String sn)
	{
		this.sn = sn;
	}

	public String getLicense()
	{
		return license;
	}

	public void setLicense(String license)
	{
		this.license = license;
	}

	public String getServiceTime()
	{
		return serviceTime;
	}

	public void setServiceTime(String serviceTime)
	{
		this.serviceTime = serviceTime;
	}

	public String getCarStatus()
	{
		return carStatus;
	}

	public void setCarStatus(String carStatus)
	{
		this.carStatus = carStatus;
	}

	public Sites getSite()
	{
		return site;
	}

	public void setSite(Sites site)
	{
		this.site = site;
	}

	public CarCategory getCarCategory()
	{
		return carCategory;
	}

	public void setCarCategory(CarCategory carCategory)
	{
		this.carCategory = carCategory;
	}

}
