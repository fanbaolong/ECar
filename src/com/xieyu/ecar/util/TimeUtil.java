package com.xieyu.ecar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;

/**
 * 时间公共方法类
 * 
 * @author wangfeng
 *
 */
public class TimeUtil
{

	/**
	 * 时间戳转化成string/date
	 * 
	 * @param a
	 * @return
	 * @throws ParseException
	 */
	public static String getStrTime0(long a)
	{

		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Long time = new Long(a);

			String d = format.format(time);
			Date date = format.parse(d);
			return d;
		} catch (ParseException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 时间戳转化成string/date
	 * 
	 * @param a
	 * @return
	 * @throws ParseException
	 */
	public static String getStrTime(long a)
	{

		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			Long time = new Long(a);

			String d = format.format(time);
			Date date = format.parse(d);
			return d;
		} catch (ParseException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 时间戳转化成string/date
	 * 
	 * @param a
	 * @return
	 * @throws ParseException
	 */
	public static String getStrTime1(long a)
	{

		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Long time = new Long(a);

			String d = format.format(time);
			Date date = format.parse(d);
			return d;
		} catch (ParseException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * date/string转化成时间戳(String time="1970-01-06 11:45:55";)
	 * 
	 * @return
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getLongTime(String time)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try
		{
			date = format.parse(time);
			return date.getTime();
		} catch (ParseException e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 应收金额
	 * 
	 * @param startTime
	 *            ：开始时间（yyyy-MM-dd HH:mm）
	 * @param endTime
	 *            ：结束时间（yyyy-MM-dd HH:mm）
	 * @param meneyStr
	 *            ：每小时多少钱
	 * @return
	 */
	public static String getMoney(String startTime, String endTime, double moneyStr)
	{
		String endMoney = "00.00";
		try
		{
			long start = getLongTime(startTime);
			long end = getLongTime(endTime);
			double time = (end - start) / 3600000;
			double money = (long) (moneyStr * time);
			endMoney = String.format("%.2f", money);
		} catch (Exception e)
		{
		}
		return endMoney;
	}

	public static long getMinute(String startTime, String endTime)
	{
		long endMoney = 0;
		try
		{
			long start = getLongTime(startTime);
			long end = getLongTime(endTime);
			endMoney = (end - start) / 60000;
		} catch (Exception e)
		{
		}
		return endMoney;
	}

	public static List<Date> getTimeSegment()
	{

		Calendar cal = Calendar.getInstance();

		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		cal.set(Calendar.MILLISECOND, 0);

		long startTime = cal.getTimeInMillis();

		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

		long endTime = cal.getTimeInMillis();

		final int seg = 5 * 60 * 1000;// 五分钟

		ArrayList<Date> result = new ArrayList<Date>((int) ((endTime - startTime) / seg + 1));

		for (long time = startTime; time <= endTime; time += seg)
		{

			result.add(new Date(time));

		}

		return result;

	}

	public static List<Date> test(Date date)
	{

		Date start = dayStartDate(date);// 转换为天的起始date

		Date nextDayDate = nextDay(start);// 下一天的date

		List<Date> result = new ArrayList<Date>();

		while (start.compareTo(nextDayDate) < 0)
		{

			result.add(start);

			// 日期加5分钟

			start = addFiveMin(start, 5);

		}

		return result;

	}

	private static Date addFiveMin(Date start, int offset)
	{

		Calendar c = Calendar.getInstance();

		c.setTime(start);

		c.add(Calendar.MINUTE, offset);

		return c.getTime();

	}

	private static Date nextDay(Date start)
	{

		Calendar c = Calendar.getInstance();

		c.setTime(start);

		c.add(Calendar.DATE, 1);

		return c.getTime();

	}

	private static Date dayStartDate(Date date)
	{

		Calendar c = Calendar.getInstance();

		c.setTime(date);

		c.set(Calendar.HOUR_OF_DAY, 0);

		c.set(Calendar.MINUTE, 0);

		c.set(Calendar.SECOND, 0);

		c.set(Calendar.MILLISECOND, 0);

		return c.getTime();

	}
}
