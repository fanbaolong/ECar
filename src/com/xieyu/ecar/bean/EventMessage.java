package com.xieyu.ecar.bean;

/**
 * 用作eventbus 传递消息的key值
 * 
 * @author fbl
 *
 */
public enum EventMessage
{
	/** 刷新订单 */
	refreshOrder,
	/** 刷新消息 */
	refreshNews,
	/** 清空消息 */
	cleanNews,
	/** 底部小图标 */
	badgeAdd,
	/** 登出 */
	loginOut,
	/** 有更新 */
	hasUpdate,
	/** 更新map */
	updateMap,
	/** 更新订单中开关UI */
	switchUpdate
}
