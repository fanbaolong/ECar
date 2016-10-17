package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * 汽车订单
 * @author wangfeng
 *
 */
public class OrderCar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/**网点名称*/
	private String net_name;
	/**网点地址*/
	private String net_address;
	/**开始时间*/
	private String start_time;
	/**结束时间*/
	private String end_time;
	/**预约时间*/
	private String booking_time;
	/**类型（充电桩类型，汽车类型）*/
	private String car_type;
	/**是否预约*/
	private boolean isBooking;
	/**数量*/
	private String num;
	/**已预约数量*/
	private String booked;
	/**满电续航(km)*/
	private String drive_distance;
	/**最高时速（km/h）*/
	private String max_speed;
	/**功率（kw）*/
	private String power;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNet_name() {
		return net_name;
	}
	public void setNet_name(String net_name) {
		this.net_name = net_name;
	}
	public String getNet_address() {
		return net_address;
	}
	public void setNet_address(String net_address) {
		this.net_address = net_address;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getBooking_time() {
		return booking_time;
	}
	public void setBooking_time(String booking_time) {
		this.booking_time = booking_time;
	}
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	public boolean isBooking() {
		return isBooking;
	}
	public void setBooking(boolean isBooking) {
		this.isBooking = isBooking;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getBooked() {
		return booked;
	}
	public void setBooked(String booked) {
		this.booked = booked;
	}
	public String getDrive_distance() {
		return drive_distance;
	}
	public void setDrive_distance(String drive_distance) {
		this.drive_distance = drive_distance;
	}
	public String getMax_speed() {
		return max_speed;
	}
	public void setMax_speed(String max_speed) {
		this.max_speed = max_speed;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	
	
	
}
