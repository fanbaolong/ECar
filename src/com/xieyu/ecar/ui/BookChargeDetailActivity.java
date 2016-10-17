package com.xieyu.ecar.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import charting.animation.Easing;
import charting.charts.LineChart;
import charting.components.Legend;
import charting.components.Legend.LegendForm;
import charting.components.LimitLine;
import charting.components.LimitLine.LimitLabelPosition;
import charting.components.XAxis;
import charting.components.XAxis.XAxisPosition;
import charting.components.YAxis;
import charting.data.Entry;
import charting.data.LineData;
import charting.data.LineDataSet;
import charting.highlight.Highlight;
import charting.listener.ChartTouchListener;
import charting.listener.ChartTouchListener.ChartGesture;
import charting.listener.OnChartGestureListener;
import charting.listener.OnChartValueSelectedListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.PilesCategory;
import com.xieyu.ecar.bean.Sites;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.view.MyMarkerView;
import com.xieyu.ecar.util.DateTime;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.TimeUtil;
import com.xieyu.ecar.util.TypePopupwindow;
import com.xieyu.ecar.util.DateTime.OnClickTimeListener;
import com.xieyu.ecar.util.TypePopupwindow.IDropdownItemClickListener;

import de.greenrobot.event.EventBus;

/**
 * 预约充电桩详情
 * 
 * @author wangfeng
 *
 */
public class BookChargeDetailActivity extends BackableTitleBarActivity implements OnSeekBarChangeListener, OnChartGestureListener, OnChartValueSelectedListener
{

	@V
	private TextView tv_charge_name, tv_charge_address, tv_charge_num, tv_charge_badnum, tv_charge_type, tv_charge_starttime, tv_charge_endtime, tv_booked;

	@V
	private LineChart lc_charge_tu;

	private Sites mpo = new Sites();
	private List<PilesCategory> chargeTypes = new ArrayList<PilesCategory>();
	private int select = 0;
	private List<String> strings = new ArrayList<String>();
	private List<Integer> nums = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_bookchargedetail);
		getTitleBar().setTitle("充电预约");
		setView();
	}

	private void setView()
	{
		Injector.getInstance().inject(this);

		mpo = (Sites) getIntent().getSerializableExtra("mpo");

		tv_charge_type.setOnClickListener(this);
		tv_charge_starttime.setOnClickListener(this);
		tv_charge_endtime.setOnClickListener(this);
		tv_booked.setOnClickListener(this);
		tv_charge_starttime.setText(TimeUtil.getStrTime(System.currentTimeMillis()));
		tv_charge_endtime.setText(TimeUtil.getStrTime(System.currentTimeMillis()));
		tv_charge_name.setText("网点：" + mpo.getName());
		tv_charge_address.setText("地址：" + mpo.getPositionName());
		tv_charge_num.setText("充电桩数：" + mpo.getPilesSum() + "");

		lc_charge_tu.setOnChartGestureListener(this);
		lc_charge_tu.setOnChartValueSelectedListener(this);
		lc_charge_tu.setDrawGridBackground(false);
		lc_charge_tu.setDescription("");
		lc_charge_tu.setNoDataTextDescription("You need to provide data for the chart.");
		// enable touch gestures
		lc_charge_tu.setTouchEnabled(true);

		// enable scaling and dragging
		lc_charge_tu.setDragEnabled(true);
		lc_charge_tu.setScaleEnabled(true);
		// if disabled, scaling can be done on x- and y-axis separately
		lc_charge_tu.setPinchZoom(true);
		lc_charge_tu.zoom(10f, 10f, 10f, 10f);
		// to use for it
		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

		// set the marker to the chart
		lc_charge_tu.setMarkerView(mv);
		// x-axis limit line 加条竖线
		LimitLine llXAxis = new LimitLine(10f, "Index 10");
		llXAxis.setLineWidth(4f);
		llXAxis.enableDashedLine(10f, 0f, 0f);
		llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
		llXAxis.setTextSize(10f);

		// X轴设置
		XAxis xAxis = lc_charge_tu.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);

		YAxis leftAxis = lc_charge_tu.getAxisLeft();
		leftAxis.removeAllLimitLines();

		// leftAxis.addLimitLine(ll2);
		leftAxis.setAxisMaxValue(80f);
		leftAxis.setAxisMinValue(0f);
		leftAxis.setStartAtZero(false);
		// leftAxis.setYOffset(20f);
		leftAxis.enableGridDashedLine(10f, 0f, 0f);

		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(true);

		lc_charge_tu.getAxisRight().setEnabled(false);

		// lc_charge_tu.animateX(2500, Easing.EasingOption.EaseInOutQuart);
		Legend l = lc_charge_tu.getLegend();
		l.setForm(LegendForm.CIRCLE);
		getChargeType();
	}

	private void getChartData(int select)
	{
		RequestParams params = new RequestParams(BaseConstants.getChart);
		params.addBodyParameter("siteId", mpo.getId() + "");
		params.addBodyParameter("pileType", chargeTypes.get(select).getId() + "");
		params.addBodyParameter("chartTime", TimeUtil.getStrTime1(System.currentTimeMillis()));
		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject object = new JSONObject(result);
					String resultType = object.getString("resultType");
					if (resultType.equals("OK"))
					{
						Gson gson = new Gson();
						nums = gson.fromJson(object.getJSONArray("objectResult").toString(), new TypeToken<List<Integer>>()
						{
						}.getType());
						setData(nums);
					}
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{

			}

			@Override
			public void onCancelled(CancelledException cex)
			{

			}

			@Override
			public void onFinished()
			{

			}
		});
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.tv_charge_type:
			if (strings.size() == 0)
			{
				getChargeType();
			} else
			{
				showPopup(strings, tv_charge_type);
			}
			break;
		case R.id.tv_charge_starttime:
			dateTimePickerDialog(tv_charge_starttime);
			break;
		case R.id.tv_charge_endtime:
			dateTimePickerDialog(tv_charge_endtime);
			break;
		case R.id.tv_booked:
			submitBooked();
			break;
		default:
			break;
		}
	}

	/** 提交充电桩预约接口 */
	private void submitBooked()
	{
		if (chargeTypes.size() == 0)
		{
			App.showShortToast("请先选择充电桩类型");
			return;
		}
		if (TimeUtil.getLongTime(getText(tv_charge_starttime)) == TimeUtil.getLongTime(getText(tv_charge_endtime)))
		{
			App.showShortToast("开始时间不能等于结束时间");
			return;
		}
		if (TimeUtil.getLongTime(getText(tv_charge_starttime)) > TimeUtil.getLongTime(getText(tv_charge_endtime)))
		{
			App.showShortToast("开始时间不能大于结束时间");
			return;
		}
		RequestParams params = new RequestParams(BaseConstants.reServePiles);
		params.addBodyParameter("siteId", mpo.getId() + "");
		params.addBodyParameter("sessionId", PreferenceUtil.getString(BookChargeDetailActivity.this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("pilesCategoryId", chargeTypes.get(select).getId() + "");
		params.addBodyParameter("reServeStartTime", getText(tv_charge_starttime));
		params.addBodyParameter("reServeEndTime", getText(tv_charge_endtime));
		org.xutils.x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					String resultType = jsonObject.getString("resultType");
					if (resultType.equals("OK"))
					{
						App.showShortToast(jsonObject.getString("objectResult"));
						EventBus.getDefault().post(EventMessage.refreshOrder);
						App.select = 1;
						App.ispager = true;
						back();
					} else
					{
						App.showShortToast(jsonObject.getString("objectResult"));
					}
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{

			}

			@Override
			public void onCancelled(CancelledException cex)
			{

			}

			@Override
			public void onFinished()
			{

			}
		});

	}

	/** 获取充电桩类型接口 */
	private void getChargeType()
	{
		RequestParams params = new RequestParams(BaseConstants.getPilesCategoryBySite);
		params.addBodyParameter("siteId", mpo.getId() + "");
		org.xutils.x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					String resultType = jsonObject.getString("resultType");
					if (resultType.equals("OK"))
					{
						Gson gson = new Gson();
						chargeTypes.clear();
						strings.clear();
						chargeTypes = gson.fromJson(jsonObject.getJSONArray("objectResult").toString(), new TypeToken<List<PilesCategory>>()
						{
						}.getType());
						if (chargeTypes.size() == 0)
						{
							App.showShortToast("暂无充电桩类型");
							return;
						}
						for (int i = 0; i < chargeTypes.size(); i++)
						{
							// ChargeType chargeType = new ChargeType();
							// chargeType = chargeTypes.get(i);
							strings.add(chargeTypes.get(i).getName());
						}
						tv_charge_type.setText(chargeTypes.get(0).getName());
						getChartData(0);
					}
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{

			}

			@Override
			public void onCancelled(CancelledException cex)
			{

			}

			@Override
			public void onFinished()
			{

			}
		});
	}

	private TypePopupwindow popup;

	private void showPopup(List<String> sList, final TextView tv)
	{

		int[] location = new int[2];
		tv.getLocationInWindow(location);
		location[1] += tv.getHeight();
		popup = new TypePopupwindow(BookChargeDetailActivity.this, sList);
		popup.showAt(location, tv.getWidth(), tv.getHeight(), false);
		popup.setItemClickListener(new IDropdownItemClickListener()
		{

			@Override
			public void onItemClick(String text, int position)
			{
				tv.setText(text);
				select = position;
				getChartData(position);
				lc_charge_tu.invalidate();
			}
		});
	}

	// 选择时间
	public void dateTimePickerDialog(final TextView tv)
	{
		DateTime dialog = new DateTime(BookChargeDetailActivity.this, R.style.add_dialog, tv.getText().toString());
		dialog.show();
		dialog.setOnClickTimeListener(new OnClickTimeListener()
		{

			@Override
			public void getTime(String time)
			{
				tv.setText(time);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.line, menu);
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	private void setData(List<Integer> mLists)
	{

		ArrayList<String> xVals = new ArrayList<String>();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		List<Date> ds = TimeUtil.test(date);
		for (Date d : ds)
		{

			xVals.add(fmt.format(d));

		}
		xVals.add("24:00");

		ArrayList<Entry> yVals = new ArrayList<Entry>();

		for (int i = 0; i < mLists.size(); i++)
		{

			int y = mLists.get(i);
			float mult = y;
			yVals.add(new Entry(mult, i));
		}

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals, "可用充电桩");
		// set1.setFillAlpha(110);
		// set1.setFillColor(Color.RED);

		// set the line to be drawn like this "- - - - - -"
		set1.enableDashedLine(10f, 0f, 0f);
		set1.enableDashedHighlightLine(10f, 0f, 0f);
		set1.setColor(getResources().getColor(R.color.holo_title));
		set1.setCircleColor(Color.BLACK);
		set1.setLineWidth(1f);
		set1.setCircleSize(1.5f);
		set1.setDrawCircleHole(false);
		set1.setHighlightEnabled(false);
		set1.setValueTextSize(0f);
		// set1.setFillAlpha(65);
		set1.setFillColor(Color.BLACK);
		set1.setDrawFilled(true);
		// set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
		// Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set1); // add the datasets

		// create a data object with the datasets
		LineData data = new LineData(xVals, dataSets);

		// set data
		lc_charge_tu.setData(data);

		lc_charge_tu.animateX(3000, Easing.EasingOption.EaseInOutQuart);

	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h)
	{

	}

	@Override
	public void onNothingSelected()
	{

	}

	@Override
	public void onChartGestureStart(MotionEvent me, ChartGesture lastPerformedGesture)
	{

	}

	@Override
	public void onChartGestureEnd(MotionEvent me, ChartGesture lastPerformedGesture)
	{
		if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
			lc_charge_tu.highlightValues(null);
	}

	@Override
	public void onChartLongPressed(MotionEvent me)
	{

	}

	@Override
	public void onChartDoubleTapped(MotionEvent me)
	{

	}

	@Override
	public void onChartSingleTapped(MotionEvent me)
	{

	}

	@Override
	public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY)
	{

	}

	@Override
	public void onChartScale(MotionEvent me, float scaleX, float scaleY)
	{

	}

	@Override
	public void onChartTranslate(MotionEvent me, float dX, float dY)
	{

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{

	}

}
