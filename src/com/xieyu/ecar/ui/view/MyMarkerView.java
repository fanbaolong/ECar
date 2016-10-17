package com.xieyu.ecar.ui.view;

import java.util.Calendar;

import android.content.Context;
import android.widget.TextView;
import charting.components.MarkerView;
import charting.data.CandleEntry;
import charting.data.Entry;
import charting.highlight.Highlight;
import charting.utils.Utils;

import com.xieyu.ecar.R;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView
{

	private TextView tv_numy, tv_date, tv_timex;

	public MyMarkerView(Context context, int layoutResource)
	{
		super(context, layoutResource);

		tv_numy = (TextView) findViewById(R.id.tv_numy);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_timex = (TextView) findViewById(R.id.tv_timex);
	}

	// callbacks everytime the MarkerView is redrawn, can be used to update the
	// content (user-interface)
	@Override
	public void refreshContent(Entry e, Highlight highlight)
	{

		Calendar calendar = Calendar.getInstance();
		tv_date.setText(calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH));
		tv_timex.setText(Utils.formatNumber(e.getXIndex() - 1, 0, true) + ":00");
		if (e instanceof CandleEntry)
		{
			CandleEntry ce = (CandleEntry) e;
			tv_numy.setText("可用充电桩" + Utils.formatNumber(ce.getHigh(), 0, true) + "个");
		} else
		{
			tv_numy.setText("可用充电桩" + Utils.formatNumber(e.getVal(), 0, true) + "个");
		}
	}

	@Override
	public int getXOffset(float xpos)
	{
		// this will center the marker-view horizontally
		return -(getWidth() / 2);
	}

	@Override
	public int getYOffset(float ypos)
	{
		// this will cause the marker-view to be above the selected value
		return -getHeight();
	}
}
