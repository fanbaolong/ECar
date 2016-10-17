package com.xieyu.ecar.ui;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import charting.animation.Easing;
import charting.charts.LineChart;
import charting.components.Legend;
import charting.components.Legend.LegendForm;
import charting.components.LimitLine;
import charting.components.LimitLine.LimitLabelPosition;
import charting.components.XAxis;
import charting.components.XAxis.XAxisPosition;
import charting.components.YAxis;
import charting.data.DataSet;
import charting.data.Entry;
import charting.data.LineData;
import charting.data.LineDataSet;
import charting.data.filter.Approximator;
import charting.data.filter.Approximator.ApproximatorType;
import charting.highlight.Highlight;
import charting.listener.ChartTouchListener;
import charting.listener.OnChartGestureListener;
import charting.listener.OnChartValueSelectedListener;
import charting.listener.ChartTouchListener.ChartGesture;

import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.view.MyMarkerView;

public class LineChartActivity1 extends BackableTitleBarActivity implements OnSeekBarChangeListener, OnChartGestureListener, OnChartValueSelectedListener
{

	private LineChart mChart;
	private SeekBar mSeekBarX, mSeekBarY;
	private TextView tvX, tvY;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linechart);
		getTitleBar().setTitle("折线示例");

		tvX = (TextView) findViewById(R.id.tvXMax);
		tvY = (TextView) findViewById(R.id.tvYMax);

		mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
		mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

		mSeekBarX.setProgress(45);
		mSeekBarY.setProgress(100);

		mSeekBarY.setOnSeekBarChangeListener(this);
		mSeekBarX.setOnSeekBarChangeListener(this);

		mChart = (LineChart) findViewById(R.id.chart1);
		mChart.setOnChartGestureListener(this);
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDrawGridBackground(false);

		mChart.setDrawBorders(true); // 是否在折线图上添加边框

		// no description text
		mChart.setDescription("");
		mChart.setNoDataTextDescription("You need to provide data for the chart.");

		// enable touch gestures
		mChart.setTouchEnabled(true);

		// enable scaling and dragging
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		// mChart.setScaleXEnabled(true);
		// mChart.setScaleYEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		mChart.setPinchZoom(true);

		// set an alternative background color
		// mChart.setBackgroundColor(Color.GRAY);

		// create a custom MarkerView (extend MarkerView) and specify the layout
		// to use for it
		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

		// set the marker to the chart
		mChart.setMarkerView(mv);

		// x-axis limit line 加条竖线
		LimitLine llXAxis = new LimitLine(10f, "Index 10");
		llXAxis.setLineWidth(4f);
		llXAxis.enableDashedLine(10f, 0f, 0f);
		llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
		llXAxis.setTextSize(10f);

		// X轴设置
		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.mAxisLabelModulus = 3;
		// xAxis.addLimitLine(llXAxis); // add x-axis limit line

		// Typeface tf = Typeface.createFromAsset(getAssets(),
		// "OpenSans-Regular.ttf");

		// LimitLine ll1 = new LimitLine(130f, "Upper Limit");
		// ll1.setLineWidth(4f);
		// ll1.enableDashedLine(10f, 10f, 0f);
		// ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
		// ll1.setTextSize(10f);
		// ll1.setTypeface(tf);

		// LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
		// ll2.setLineWidth(4f);
		// ll2.enableDashedLine(10f, 10f, 0f);
		// ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
		// ll2.setTextSize(10f);
		// ll2.setTypeface(tf);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
										// overlapping lines
										// leftAxis.addLimitLine(ll1);
		// leftAxis.addLimitLine(ll2);
		leftAxis.setAxisMaxValue(100f);
		leftAxis.setAxisMinValue(0f);
		leftAxis.setStartAtZero(false);
		// leftAxis.setYOffset(20f);
		leftAxis.enableGridDashedLine(10f, 0f, 0f);

		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(true);

		mChart.getAxisRight().setEnabled(false);
		
		
	

		// mChart.getViewPortHandler().setMaximumScaleY(2f);
		// mChart.getViewPortHandler().setMaximumScaleX(2f);

		// add data
		setData(288, 24);

		// mChart.setVisibleXRange(20);
		// mChart.setVisibleYRange(20f, AxisDependency.LEFT);
		// mChart.centerViewTo(20, 50, AxisDependency.LEFT);

		mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
		// mChart.invalidate();

		// get the legend (only possible after setting data)
		Legend l = mChart.getLegend();

		// modify the legend ...
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.CIRCLE);

		// // dont forget to refresh the drawing
		// mChart.invalidate();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.line, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
		case R.id.actionToggleValues:
		{
			for (DataSet<?> set : mChart.getData().getDataSets())
				set.setDrawValues(!set.isDrawValuesEnabled());

			mChart.invalidate();
			break;
		}
		case R.id.actionToggleHighlight:
		{
			if (mChart.getData() != null)
			{
				mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
				mChart.invalidate();
			}
			break;
		}
		case R.id.actionToggleFilled:
		{

			ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData().getDataSets();

			for (LineDataSet set : sets)
			{
				if (set.isDrawFilledEnabled())
					set.setDrawFilled(false);
				else
					set.setDrawFilled(true);
			}
			mChart.invalidate();
			break;
		}
		case R.id.actionToggleCircles:
		{
			ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData().getDataSets();

			for (LineDataSet set : sets)
			{
				if (set.isDrawCirclesEnabled())
					set.setDrawCircles(false);
				else
					set.setDrawCircles(true);
			}
			mChart.invalidate();
			break;
		}
		case R.id.actionToggleCubic:
		{
			ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData().getDataSets();

			for (LineDataSet set : sets)
			{
				if (set.isDrawCubicEnabled())
					set.setDrawCubic(false);
				else
					set.setDrawCubic(true);
			}
			mChart.invalidate();
			break;
		}
		case R.id.actionToggleStartzero:
		{
			mChart.getAxisLeft().setStartAtZero(!mChart.getAxisLeft().isStartAtZeroEnabled());
			mChart.getAxisRight().setStartAtZero(!mChart.getAxisRight().isStartAtZeroEnabled());
			mChart.invalidate();
			break;
		}
		case R.id.actionTogglePinch:
		{
			if (mChart.isPinchZoomEnabled())
				mChart.setPinchZoom(false);
			else
				mChart.setPinchZoom(true);

			mChart.invalidate();
			break;
		}
		case R.id.actionToggleAutoScaleMinMax:
		{
			mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
			mChart.notifyDataSetChanged();
			break;
		}
		case R.id.animateX:
		{
			mChart.animateX(3000);
			break;
		}
		case R.id.animateY:
		{
			mChart.animateY(3000, Easing.EasingOption.EaseInCubic);
			break;
		}
		case R.id.animateXY:
		{
			mChart.animateXY(3000, 3000);
			break;
		}
		case R.id.actionToggleFilter:
		{

			// the angle of filtering is 35°
			Approximator a = new Approximator(ApproximatorType.DOUGLAS_PEUCKER, 35);

			if (!mChart.isFilteringEnabled())
			{
				mChart.enableFiltering(a);
			} else
			{
				mChart.disableFiltering();
			}
			mChart.invalidate();
			break;
		}
		case R.id.actionSave:
		{
			if (mChart.saveToPath("title" + System.currentTimeMillis(), ""))
			{
				Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!", Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT).show();

			// mChart.saveToGallery("title"+System.currentTimeMillis())
			break;
		}
		}
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{

		tvX.setText("" + (mSeekBarX.getProgress() + 1));
		tvY.setText("" + (mSeekBarY.getProgress()));

		setData(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress());

		// redraw
		mChart.invalidate();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{

	}

	private void setData(int count, float range)
	{

		ArrayList<String> xVals = new ArrayList<String>();
		xVals.add("0:00");
		for (int i = 0; i < count; i++)
		{
			// xVals.add((i) + ":00");
			xVals.add(i % 12 + "");
		}

		ArrayList<Entry> yVals = new ArrayList<Entry>();

		for (int i = 0; i < count; i++)
		{

			float mult = (range + 1);
			float val = (float) (Math.random() * mult) + 3;// + (float)
			// ((mult *
			// 0.1) / 10);
			yVals.add(new Entry(val, i));
		}

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
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
		set1.setValueTextSize(0f);
		// set1.setFillAlpha(65);
		set1.setFillColor(Color.BLACK);
		set1.setDrawFilled(true);// 设置是否填充
		// set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
		// Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set1); // add the datasets

		// create a data object with the datasets
		LineData data = new LineData(xVals, dataSets);

		// set data
		mChart.setData(data);
	}

	@Override
	public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
	{
		Log.i("Gesture", "START");
	}

	@Override
	public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
	{
		Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

		// un-highlight values after the gesture is finished and no single-tap
		if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
			mChart.highlightValues(null); // or highlightTouch(null) for
											// callback to
											// onNothingSelected(...)
	}

	@Override
	public void onChartLongPressed(MotionEvent me)
	{
		Log.i("LongPress", "Chart longpressed.");
	}

	@Override
	public void onChartDoubleTapped(MotionEvent me)
	{
		Log.i("DoubleTap", "Chart double-tapped.");
	}

	@Override
	public void onChartSingleTapped(MotionEvent me)
	{
		Log.i("SingleTap", "Chart single-tapped.");
	}

	@Override
	public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY)
	{
		Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
	}

	@Override
	public void onChartScale(MotionEvent me, float scaleX, float scaleY)
	{
		Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
	}

	@Override
	public void onChartTranslate(MotionEvent me, float dX, float dY)
	{
		Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h)
	{
		Log.i("Entry selected", e.toString());
		Log.i("", "low: " + mChart.getLowestVisibleXIndex() + ", high: " + mChart.getHighestVisibleXIndex());
	}

	@Override
	public void onNothingSelected()
	{
		Log.i("Nothing selected", "Nothing selected.");
	}
}
