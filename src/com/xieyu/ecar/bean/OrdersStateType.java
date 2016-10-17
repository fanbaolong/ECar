package com.xieyu.ecar.bean;

/**
 * @author fbl
 * 
 *         订单状态类型
 *
 */
public enum OrdersStateType
{

	// / 预约中
	Reserve,

	// / 使用中
	InUse,

	// / 完成
	Complete,

	// / 取消
	Cancel,
	
	// 过期作废
	Waste,
	
	//用车等待确认
	WaitUse,
	
	//还车等待确认
	WaitBack

}
