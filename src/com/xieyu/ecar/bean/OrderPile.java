package com.xieyu.ecar.bean;

import java.io.Serializable;

import com.xieyu.ecar.R;

/**
 * @author fbl
 * 
 *         电桩实体类
 *
 */
public class OrderPile implements Serializable
{

	private static final long serialVersionUID = 9020513361681378108L;
	private int id;
	/***/
	private String createTime;
	/***/
	private String updateTime;
	/***/
	private boolean onDeleted;
	/** 订单编号 */
	private String orderNo;
	/***/
	private String remarks;
	/** 订单编号 */
	private String sn;
	/***/
	private String mobile;
	/***/
	private String reServeTime;
	/***/
	private String reServerBeginTime;
	/***/
	private String reServerEndTime;
	/** 车 */
	private CarCategory carCategory;

	/***/
	private Piles piles;
	/***/
	private String paymentMethod;
	/***/
	private boolean reserve;
	/***/
	private String cost;
	/***/
	private String freezingAmount;
	/** 正常结束:NormalEnd,异常结束:ExceptionEnd */
	private String endType;
	/***/
	private OrdersStateType ordersStateType;
	/** 车 */
	private CodeCarDetail car;
	/** 车辆订单:CarType,充电桩订单:PilesType */
	private String orderType;
	/***/
	private int minuteCount;
	/** 充电度数 */
	private int degrees;
	/***/
	private Long endTime;
	/***/
	private Long beginTime;
	/** 地址 */
	private String reSitePositionName;
	/***/
	private String carCategoryName;
	/***/
	private String pilesCategoryName;
	/***/
	private String reSiteName;
	/***/
	private String pilesSn;
	/** 扣费原因 */
	private String detainreason;
	/** 违章扣费金额 */
	private double detainMoney;

	
	public String getDetainreason() {
		return detainreason;
	}

	public void setDetainreason(String detainreason) {
		this.detainreason = detainreason;
	}

	public double getDetainMoney() {
		return detainMoney;
	}

	public void setDetainMoney(double detainMoney) {
		this.detainMoney = detainMoney;
	}

	public String getFreezingAmount()
	{
		return freezingAmount;
	}

	public void setFreezingAmount(String freezingAmount)
	{
		this.freezingAmount = freezingAmount;
	}

	public CarCategory getCarCategory()
	{
		return carCategory;
	}

	public void setCarCategory(CarCategory carCategory)
	{
		this.carCategory = carCategory;
	}

	public CodeCarDetail getCar()
	{
		return car;
	}

	public void setCar(CodeCarDetail car)
	{
		this.car = car;
	}

	public int getMinuteCount()
	{
		return minuteCount;
	}

	public void setMinuteCount(int minuteCount)
	{
		this.minuteCount = minuteCount;
	}

	public int getDegrees()
	{
		return degrees;
	}

	public void setDegrees(int degrees)
	{
		this.degrees = degrees;
	}

	public String getPilesSn()
	{
		return pilesSn;
	}

	public void setPilesSn(String pilesSn)
	{
		this.pilesSn = pilesSn;
	}

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

	public String getSn()
	{
		return sn;
	}

	public void setSn(String sn)
	{
		this.sn = sn;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getReServeTime()
	{
		return reServeTime;
	}

	public void setReServeTime(String reServeTime)
	{
		this.reServeTime = reServeTime;
	}

	public String getReServerBeginTime()
	{
		return reServerBeginTime;
	}

	public void setReServerBeginTime(String reServerBeginTime)
	{
		this.reServerBeginTime = reServerBeginTime;
	}

	public String getReServerEndTime()
	{
		return reServerEndTime;
	}

	public void setReServerEndTime(String reServerEndTime)
	{
		this.reServerEndTime = reServerEndTime;
	}

	public Piles getPiles()
	{
		return piles;
	}

	public void setPiles(Piles piles)
	{
		this.piles = piles;
	}

	public String getPaymentMethod()
	{
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}

	public boolean isReserve()
	{
		return reserve;
	}

	public void setReserve(boolean reserve)
	{
		this.reserve = reserve;
	}

	public String getCost()
	{
		return cost;
	}

	public void setCost(String cost)
	{
		this.cost = cost;
	}

	public String getEndType()
	{
		return endType;
	}

	public void setEndType(String endType)
	{
		this.endType = endType;
	}

	public OrdersStateType getOrdersStateType()
	{
		return ordersStateType;
	}

	public void setOrdersStateType(OrdersStateType ordersStateType)
	{
		this.ordersStateType = ordersStateType;
	}

	public String getOrderType()
	{
		return orderType;
	}

	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}

	public Long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Long endTime)
	{
		this.endTime = endTime;
	}

	public Long getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(Long beginTime)
	{
		this.beginTime = beginTime;
	}

	public String getReSitePositionName()
	{
		return reSitePositionName;
	}

	public void setReSitePositionName(String reSitePositionName)
	{
		this.reSitePositionName = reSitePositionName;
	}

	public String getCarCategoryName()
	{
		return carCategoryName;
	}

	public void setCarCategoryName(String carCategoryName)
	{
		this.carCategoryName = carCategoryName;
	}

	public String getPilesCategoryName()
	{
		return pilesCategoryName;
	}

	public void setPilesCategoryName(String pilesCategoryName)
	{
		this.pilesCategoryName = pilesCategoryName;
	}

	public String getReSiteName()
	{
		return reSiteName;
	}

	public void setReSiteName(String reSiteName)
	{
		this.reSiteName = reSiteName;
	}

	/**
	 * 将状态转化成文字显示
	 * 
	 * @param otype
	 * @return
	 */
	public static String getOrderStatesStr(OrdersStateType otype)
	{
		String ss = "";
		switch (otype)
		{
		case Reserve:
			ss = "已预约";
			break;
		case InUse:
			ss = "用车中";
			break;
		case Complete:
			ss = "已还车";
			break;
		case Cancel:
			ss = "已取消";
			break;
		case Waste:
			ss = "已作废";
			break;
		case WaitBack:
			ss = "还车待确认";
			break;
		case WaitUse:
			ss = "租车待确认";
			break;

		default:
			break;
		}
		return ss;

	}

	/**
	 * 判断该状态下该去做什么
	 * 
	 * @param orstate
	 * @return
	 */
	public static String showOrderStatus(OrdersStateType orstate)
	{
		String result = "";

		switch (orstate)
		{
		case Reserve:
			result = "取消预约";
			break;
		case InUse:
			result = "立即还车";
			break;
		case Complete:
			result = "删除订单";
			break;
		case Cancel:
			result = "删除订单";
			break;
		case Waste:
			result = "删除订单";
			break;
		case WaitUse:
			result = "";
			break;
		case WaitBack:
			result = "";
			break;

		default:
			break;
		}

		return result;
	}

	/**
	 * 判断该状态下字体的颜色
	 * 
	 * @param orserState
	 * @return
	 */
	public static int getColorId(OrdersStateType orserState)
	{
		int colorId = 0;
		switch (orserState)
		{
		case Reserve:
			colorId = R.color.holo_title;
			break;
		case InUse:
			colorId = R.color.txt_yrllow;
			break;
		case Complete:
			colorId = R.color.black;
			break;
		case Cancel:
			colorId = R.color.black;
			break;
		case Waste:
			colorId = R.color.black;
			break;

		default:
			break;
		}
		return colorId;
	}

	/**
	 * 判断该状态下该显示的图片背景
	 * 
	 * @param orserState
	 * @return
	 */
	public static int getDrawableType(OrdersStateType orserState)
	{
		int drawableId = 0;
		switch (orserState)
		{
		case Reserve:
			drawableId = R.drawable.bg_booked;
			break;
		case InUse:
			drawableId = R.drawable.bg_booked1;
			break;
		case Complete:
			drawableId = R.drawable.bg_booked2;
			break;
		case Cancel:
			drawableId = R.drawable.bg_booked2;
			break;
		case Waste:
			drawableId = R.drawable.bg_booked2;
			break;

		default:
			break;
		}
		return drawableId;
	}

}
