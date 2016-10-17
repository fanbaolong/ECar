package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * @author fanbaolong
 *
 *         车辆类别
 */
public class CarType implements Serializable
{
	private static final long serialVersionUID = 1705369303256237781L;

	private int count;

	private CarCategory carCategory;

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
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
