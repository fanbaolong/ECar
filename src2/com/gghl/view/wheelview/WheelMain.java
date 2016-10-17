package com.gghl.view.wheelview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xieyu.ecar.R;

public class WheelMain {

	private View view;
	private Context mContext;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	private WheelView wv_ss;
	private TextView select_time;
	public int screenheight;
	private boolean hasSelectTime;
	private static int START_YEAR = 1990, END_YEAR = 2100;
	private static int START_MONTH = 1, START_DAY = 1;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public static void setSTART_MONTH(int start_month){
		START_MONTH = start_month; 
	}

	public static void setSTART_DAY(int start_day){
		START_DAY = start_day;
	}

	public WheelMain(View view, Context context) {
		super();
		this.view = view;
		this.mContext = context;
		hasSelectTime = false;
		setView(view);
	}

	public WheelMain(View view, boolean hasSelectTime, Context context) {
		super();
		this.view = view;
		this.mContext = context;
		this.hasSelectTime = hasSelectTime;
		setView(view);
	}

	public void initDateTimePicker(int year, int month, int day) {
		this.initDateTimePicker(year, month, day, 0, 0);
	}
	
	public void initDateTimePicker(int year, int month) {
		this.initDateTimePicker(year, month, 0, 0, 0);
	}
	
	public void initDateTimePicker(int year, int month, int day, int h, int m){
		initDateTimePicker(year, month, day, h, m, 0);
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(int year, int month, int day, int h, int m, int s) {
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		select_time = (TextView) view.findViewById(R.id.select_time);
		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(false);// 可循环滚动
		wv_year.setLabel("");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
		if (START_YEAR == (wv_year.getCurrentItem() + START_YEAR)) {
			wv_month.setAdapter(new NumericWheelAdapter(START_MONTH, 12));
			wv_month.setCyclic(false);
			wv_month.setCurrentItem(month + 1 - START_MONTH);
		}else {
			wv_month.setAdapter(new NumericWheelAdapter(1, 12));
			wv_month.setCyclic(true);
			wv_month.setCurrentItem(month);
		}
		wv_month.setLabel("");


		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);

		int select_day = 1;
		if (START_MONTH == (wv_month.getCurrentItem() + START_MONTH)) {
			select_day = START_DAY;
			wv_day.setCyclic(false);
		}else {
			select_day = 1;
			wv_day.setCyclic(true);
		}
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(select_day, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(select_day, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(select_day, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(select_day, 28));
		}
		wv_day.setLabel("");
		if (START_MONTH == (wv_month.getCurrentItem() + START_MONTH)) {
			wv_day.setCurrentItem(day-START_DAY);
		}else {
			wv_day.setCurrentItem(day-1);
		}

		wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_mins = (WheelView) view.findViewById(R.id.min);
		wv_ss = (WheelView) view.findViewById(R.id.ss);
		if (hasSelectTime) {
			wv_hours.setVisibility(View.VISIBLE);
			wv_mins.setVisibility(View.VISIBLE);

			wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
			wv_hours.setCyclic(true);// 可循环滚动
			wv_hours.setLabel("");// 添加文字
			wv_hours.setCurrentItem(h);

			wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
			wv_mins.setCyclic(true);// 可循环滚动
			wv_mins.setLabel("");// 添加文字
			wv_mins.setCurrentItem(m);
			
			wv_ss.setAdapter(new NumericWheelAdapter(0, 59));
			wv_ss.setCyclic(true);// 可循环滚动
			wv_ss.setLabel("");// 添加文字
			wv_ss.setCurrentItem(s);
		} else {
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
			wv_ss.setVisibility(View.GONE);
		}

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				if (START_YEAR == (wv_year.getCurrentItem() + START_YEAR)) {
					wv_month.setAdapter(new NumericWheelAdapter(START_MONTH, 12));
					wv_month.setCurrentItem(0);
					wv_month.setCyclic(false);
				}else {
					wv_month.setAdapter(new NumericWheelAdapter(1, 12));
					wv_month.setCyclic(true);
				}
				int select_day = 1;
				if (START_MONTH == (wv_month.getCurrentItem() + START_MONTH) && START_YEAR == (wv_year.getCurrentItem() + START_YEAR)) {
					select_day = START_DAY;
					wv_day.setCyclic(false);
				}else {
					select_day = 1;
					wv_day.setCyclic(true);
				}
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(select_day, 31));

				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(select_day, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(select_day, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(select_day, 28));
				}
				showTime(getTime());
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				int select_day = 1;
				if (START_MONTH == (wv_month.getCurrentItem() + START_MONTH) && START_YEAR == (wv_year.getCurrentItem() + START_YEAR)) {
					select_day = START_DAY;
					wv_day.setCurrentItem(0);
					wv_day.setCyclic(false);
				}else {
					select_day = 1;
					wv_day.setCyclic(true);
				}
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(select_day, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(select_day, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(select_day, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(select_day, 28));
				}
				showTime(getTime());
			}
		};

		// 添加"日，时，分"监听
		OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showTime(getTime());
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		wv_day.addChangingListener(wheelListener_day);
		wv_hours.addChangingListener(wheelListener_day);
		wv_mins.addChangingListener(wheelListener_day);
		wv_ss.addChangingListener(wheelListener_day);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (screenheight / 100) * 3;
		else
			textSize = (screenheight / 100) * 4;
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		wv_ss.TEXT_SIZE = textSize;
		showTime(getTime());
	}

	public String showTime(String time){
		try {
			SimpleDateFormat format;
			if (time.length() < 12) {
				format = new SimpleDateFormat( "yyyy-MM-dd" );
			}else if (time.length() > 17) {
				format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			}else {
				format = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
			}
			String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
			Date date=format.parse(time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0)
				w = 0;
			if (w == 0 || w == 6) {
				select_time.setTextColor(mContext.getResources().getColor(R.color.red));
			}else {
				select_time.setTextColor(mContext.getResources().getColor(R.color.holo_title));
			}
			select_time.setText(time + "  " + weekDays[w]);
			return time + "  " + weekDays[w];
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String getMonth_Day(int time){
		if (time < 10) {
			return "0"+time;
		}else {
			return ""+time;
		}
	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		int add_month = 1,add_day = 1;
		if (START_YEAR == (wv_year.getCurrentItem() + START_YEAR)) {
			add_month = START_MONTH;
		}
		if (START_MONTH == (wv_month.getCurrentItem() + START_MONTH) && START_YEAR == (wv_year.getCurrentItem() + START_YEAR)) {
			add_day = START_DAY;
		}
		if (!hasSelectTime)
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
			.append(getMonth_Day(wv_month.getCurrentItem() + add_month)).append("-")
			.append(getMonth_Day(wv_day.getCurrentItem() + add_day));
		else
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
			.append(getMonth_Day(wv_month.getCurrentItem() + add_month)).append("-")
			.append(getMonth_Day(wv_day.getCurrentItem() + add_day)).append(" ")
			.append(getMonth_Day(wv_hours.getCurrentItem())).append(":")
			.append(getMonth_Day(wv_mins.getCurrentItem()));
		if (wv_ss.isShown()) {
			sb.append(":")
			.append(getMonth_Day(wv_ss.getCurrentItem()));
		}
		return sb.toString();
	}
}
