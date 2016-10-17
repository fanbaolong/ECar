package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * 消息类
 * 
 * @author wangfeng
 *
 */
public class News implements Serializable
{

	// id : 40
	// createTime : 2016-01-12 07:38:00
	// updateTime : 2016-01-12 07:38:00
	// onDeleted : false
	// orderNo : null
	// remarks : null
	// messageType : Info
	// content : 您的订单(编号：201601122579),距离预约取车的时间已经超过十分钟，订单已被作废！
	// messageState : 未读
	// title : 预约提醒

	private static final long serialVersionUID = 1L;

	private int id;

	private String createTime;

	private String updateTime;

	private boolean onDeleted;

	private String orderNo;

	private String remarks;

	private String messageType;

	private String content;

	private String messageState;

	private String title;

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

	public String getMessageType()
	{
		return messageType;
	}

	public void setMessageType(String messageType)
	{
		this.messageType = messageType;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getMessageState()
	{
		return messageState;
	}

	public void setMessageState(String messageState)
	{
		this.messageState = messageState;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public enum MessageType
	{
		// 告警
		Alarm,

		// 消息
		Info,
	}

}
