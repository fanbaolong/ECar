package com.xieyu.ecar.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gghl.view.wheelview.JudgeDate;
import com.gghl.view.wheelview.ScreenInfo;
import com.gghl.view.wheelview.WheelMain;
import com.xieyu.ecar.R;

public class DateTime extends Dialog implements OnClickListener{

	private Activity context;
	private Calendar time = Calendar.getInstance(Locale.CHINA);
	private String timestr = "";

	private WheelMain wheelMain;
	private TextView date_yes, date_no;
	@SuppressLint("SimpleDateFormat") private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private OnClickTimeListener onClickTimeListener;

	public DateTime(Context context) {
		super(context);
	}
	
	public DateTime(Activity context, int theme, String time){
		super(context, theme);
		this.context = context;
		this.timestr = time;
	}

	public interface OnClickTimeListener{
		public void getTime(String time);
	}

	public void setOnClickTimeListener(OnClickTimeListener oTimeListener){
		this.onClickTimeListener = oTimeListener;
	}

	@SuppressLint("InflateParams") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(context);
		final View timepickerview = inflater.inflate(R.layout.timepicker,
				null);
		setContentView(timepickerview);
		setCanceledOnTouchOutside(false);
		ScreenInfo screenInfo = new ScreenInfo(context);
		wheelMain = new WheelMain(timepickerview, true, context);
		wheelMain.screenheight = screenInfo.getHeight();
		if (JudgeDate.isDate(timestr, "yyyy-MM-dd HH:mm")) {
			try {
				time.setTime(dateFormat.parse(timestr));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = time.get(Calendar.YEAR);
		int month = time.get(Calendar.MONTH);
		int day = time.get(Calendar.DAY_OF_MONTH);
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int minute = time.get(Calendar.MINUTE);
		wheelMain.initDateTimePicker(year, month, day, hour, minute);

		date_yes = (TextView) findViewById(R.id.date_yes);
		date_no = (TextView) findViewById(R.id.date_no);

		date_yes.setOnClickListener(this);
		date_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_yes:
			onClickTimeListener.getTime(wheelMain.getTime());
			dismiss();
			break;
		case R.id.date_no:
			dismiss();
			break;

		default:
			break;
		}
	}

}
