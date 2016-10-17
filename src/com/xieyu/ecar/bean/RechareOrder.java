package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * @author fanbaolong
 *
 *         充值订单
 */
public class RechareOrder implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// id : 21
	// createTime : 2016-01-14 06:13:19
	// updateTime : 2016-01-14 06:13:19
	// onDeleted : false
	// orderNo : null
	// remarks : null
	// money : 1
	// moneyType : Recharge
	// ordersSn : 20160114611
	// paymentMethod : 支付宝
	// detailState : 待充值

	private int id;

	private String createTime;

	private String updateTime;

	private boolean onDeleted;

	private String orderNo;

	private String remarks;

	private String money;
	// /充值 Recharge, 花费 Cost,退款 Refund,
	private String moneyType;
	// 订单编号
	private String ordersSn;

	private PaymentMethod paymentMethod;
	// 订单状态
	private String detailState;

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

	public String getMoney()
	{
		return money;
	}

	public void setMoney(String money)
	{
		this.money = money;
	}

	public String getMoneyType()
	{
		return moneyType;
	}

	public void setMoneyType(String moneyType)
	{
		this.moneyType = moneyType;
	}

	public String getOrdersSn()
	{
		return ordersSn;
	}

	public void setOrdersSn(String ordersSn)
	{
		this.ordersSn = ordersSn;
	}

	public PaymentMethod getPaymentMethod()
	{
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}

	public String getDetailState()
	{
		return detailState;
	}

	public void setDetailState(String detailState)
	{
		this.detailState = detailState;
	}

	public static String PaymentMethod(PaymentMethod paymentMethod){
		String method = "";
		if (paymentMethod == null) {
			return method;
		}
		
		switch (paymentMethod) {
		case 微信:
			method = "微信";
			break;
		case 支付宝:
			method = "支付宝";
			break;
		case 银联:
			method = "银联";
			break;

		default:
			break;
		}
		return method;
	}
}
