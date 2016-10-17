package com.xieyu.ecar.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.xieyu.ecar.R;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;

/**
 * 租车须知
 * 
 * @author wangfeng
 *
 */
public class CarRentalActivity extends BackableTitleBarActivity
{
	@V
	private WebView car_rental;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_car_rental);
		String s = getIntent().getStringExtra("type");

		Injector.getInstance().inject(this);

		car_rental.setOverScrollMode(View.OVER_SCROLL_NEVER);
		car_rental.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		car_rental.getSettings().setSupportZoom(true);
		car_rental.getSettings().setBuiltInZoomControls(true);
		car_rental.getSettings().setJavaScriptEnabled(true);
		car_rental.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		car_rental.getSettings().setAllowFileAccess(true);
		car_rental.getSettings().setAppCacheEnabled(true);
		car_rental.getSettings().setDomStorageEnabled(true);

		if ("1".equals(s))
		{
			getTitleBar().setTitle(R.string.car_rental);
			car_rental.loadUrl("file:///android_asset/zuchexuzhi.html");
		} else if ("2".equals(s))
		{
			car_rental.loadUrl("file:///android_asset/zucheliucheng.html");
			getTitleBar().setTitle(R.string.car_progress);
		}

	}
}
