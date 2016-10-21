package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * @author fbl
 * 
 */
public class CarCategory implements Serializable
{

	private static final long serialVersionUID = -6974051212960556812L;
	/** */
	private int id;
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
	private String name;
	/** 每小时租金 */
	private String moneyTime;
	/** 每天租金 */
	private String moneyDay;
	/** 每周租金*/
	private String moneyWeek;
	/** 每月租金*/
	private String moneyMonth;
	/** */
	private int seats;
	/** */
	private String gear;
	/** */
	private String introduction;
	/** */
	private String electricType;
	/** 最高时速*/
	private String topSpeed;
	/** 满电续航*/
	private String fullElectricalEndurance;
	/** */
	private int electric;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMoneyTime() {
		return moneyTime;
	}

	public void setMoneyTime(String moneyTime) {
		this.moneyTime = moneyTime;
	}

	public String getMoneyDay()
	{
		return moneyDay;
	}

	public void setMoneyDay(String moneyDay)
	{
		this.moneyDay = moneyDay;
	}

	public String getMoneyWeek()
	{
		return moneyWeek;
	}

	public void setMoneyWeek(String moneyWeek)
	{
		this.moneyWeek = moneyWeek;
	}

	public String getMoneyMonth()
	{
		return moneyMonth;
	}

	public void setMoneyMonth(String moneyMonth)
	{
		this.moneyMonth = moneyMonth;
	}

	public int getSeats()
	{
		return seats;
	}

	public void setSeats(int seats)
	{
		this.seats = seats;
	}

	public String getGear()
	{
		return gear;
	}

	public void setGear(String gear)
	{
		this.gear = gear;
	}

	public String getIntroduction()
	{
		return introduction;
	}

	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}

	public String getElectricType()
	{
		return electricType;
	}

	public void setElectricType(String electricType)
	{
		this.electricType = electricType;
	}

	public String getTopSpeed()
	{
		return topSpeed;
	}

	public void setTopSpeed(String topSpeed)
	{
		this.topSpeed = topSpeed;
	}

	public String getFullElectricalEndurance()
	{
		return fullElectricalEndurance;
	}

	public void setFullElectricalEndurance(String fullElectricalEndurance)
	{
		this.fullElectricalEndurance = fullElectricalEndurance;
	}

	public int getElectric()
	{
		return electric;
	}

	public void setElectric(int electric)
	{
		this.electric = electric;
	}

}
