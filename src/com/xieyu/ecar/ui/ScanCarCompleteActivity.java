package com.xieyu.ecar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.StringUtil;

import de.greenrobot.event.EventBus;

/**
 * 充电成功
 * 
 * @author fanbaolong
 *
 */
public class ScanCarCompleteActivity extends SimpleTitleBarActivity
{
	@V
	private TextView charge_site_name, charge_site_address, charge_site_time, charge_site_order_sn;
	@V
	private WebView charging_web;
	@V
	private Button btn_to_order_list;

	private OrderPile mOrder = new OrderPile();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_scan_car_complete);

		mOrder = (OrderPile) getIntent().getSerializableExtra("mOrderPile");
		getTitleBar().setTitle("正在用车");
		initView();
	}

	private void initView()
	{
		Injector.getInstance().inject(this);
		btn_to_order_list.setOnClickListener(this);

		charge_site_name.setText(mOrder.getReSiteName());
		charge_site_address.setText(mOrder.getReSitePositionName());
		charge_site_time.setText(StringUtil.getDateTime(mOrder.getBeginTime()));
		charge_site_order_sn.setText(mOrder.getSn());
		charging_web.loadUrl("file:///android_asset/image.html");
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.btn_to_order_list:
			toOrderChargeDetailActivity();
			break;

		default:
			break;
		}

		super.onClick(v);

	}

	/**
	 * 去订单详情
	 */
	private void toOrderChargeDetailActivity()
	{

		EventBus.getDefault().post(EventMessage.refreshOrder);
		// 打开自定义的Activity
		Intent i = new Intent(this, MainActivity.class);
		App.ispager = true;
		App.select = 1;

		i = new Intent(this, OrderCarDetailActivity.class);// 充电订单详情
		i.putExtra("orderCharge", mOrder);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);

		finish();

	}

}
