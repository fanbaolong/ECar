package com.xieyu.ecar.ui;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.http.HttpOrderUtil;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.view.TabHeadView;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.StringUtil;

/**
 * 充电桩订单详情
 * 
 * @author wangfeng
 *
 */
public class OrderChargeDetailActivity extends BaseActivity
{

	@V
	private TextView tv_net_name, tv_net_address, tv_charge_num, tv_charge_badnum, tv_charge_type, tv_charge_degree, tv_charge_starttime, tv_charge_endtime, tv_charge_ordercode, tv_charge_booktime, tv_order_cancel, tv_charge_start, tv_isbook, tv_charge_money;
	@V
	private TabHeadView headview_order_charge;
	@V
	private LinearLayout ll_charge_cancel;
	@V
	private TextView tv_charge_real_begintime, tv_charge_real_endtime;

	private OrderPile orderCharge = new OrderPile();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_orderchargedetail);
		Injector.getInstance().inject(this);
		setView();
	}

	private void setView()
	{
		tv_order_cancel.setOnClickListener(this);
		tv_charge_start.setOnClickListener(this);
		headview_order_charge.getTitle().setText(getResources().getString(R.string.order_charge_detail));
		headview_order_charge.getLeftButton().setImageResource(R.drawable.ic_titlebar_back);
		headview_order_charge.getLeftButton().setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				back();
			}
		});
		orderCharge = (OrderPile) getIntent().getSerializableExtra("orderCharge");
		// getData();
		showView();

	}

	private void showView()
	{
		tv_net_name.setText("网点：" + StringUtil.isNull(orderCharge.getReSiteName()));
		tv_net_address.setText("地址：" + StringUtil.isNull(orderCharge.getReSitePositionName()));
		tv_isbook.setText(OrderPile.getOrderStatesStr(orderCharge.getOrdersStateType()));
		// tv_charge_num.setText("充电桩数：" +
		// StringUtil.isNull(orderCharge.getPiles().get));
		// tv_charge_badnum.setText("故障电桩数：" +
		// StringUtil.isNull(orderCharge.charge_badnum));

		tv_charge_ordercode.setText("订单编号：" + StringUtil.isNull(orderCharge.getSn()));
		tv_charge_booktime.setText("下单时间：" + StringUtil.isNull(orderCharge.getCreateTime()));
		tv_charge_type.setText("充电类型：" + StringUtil.isNull(orderCharge.getPilesCategoryName()));
		tv_charge_degree.setText("充电度数：" + StringUtil.isNull(orderCharge.getDegrees() + "度"));
		tv_charge_starttime.setText("开始时间：" + StringUtil.isNull(orderCharge.getReServerBeginTime()));
		tv_charge_endtime.setText("结束时间：" + StringUtil.isNull(orderCharge.getReServerEndTime()));

		tv_charge_money.setText(StringUtil.isNull(orderCharge.getCost() + ""));
		tv_order_cancel.setText(OrderPile.showOrderStatus(orderCharge.getOrdersStateType()));

		if (orderCharge.getBeginTime() != null)
		{
			tv_charge_real_begintime.setText("实际开始时间：" + StringUtil.isNull(StringUtil.getDateTime(orderCharge.getBeginTime())));
		} else
		{
			tv_charge_real_begintime.setVisibility(View.GONE);
		}

		if (orderCharge.getEndTime() != null)
		{
			tv_charge_real_endtime.setText("实际结束时间：" + StringUtil.isNull(StringUtil.getDateTime(orderCharge.getEndTime())));
		} else
		{
			tv_charge_real_endtime.setVisibility(View.GONE);
		}

	}

	/** 详情接口 */
	private void getData()
	{
		RequestParams params = new RequestParams(BaseConstants.getOrderById);
		params.addBodyParameter("id", orderCharge.getId() + "");
		params.addBodyParameter("sessionId", PreferenceUtil.getString(OrderChargeDetailActivity.this, BaseConstants.prefre.SessionId));
		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				JSONObject jsonObject;
				try
				{
					jsonObject = new JSONObject(result);
					if ("OK".equals(jsonObject.getString("resultType")))
					{
						Gson gson = new Gson();
						orderCharge = gson.fromJson(jsonObject.getJSONObject("objectResult").toString(), OrderPile.class);
						showView();
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
		case R.id.tv_order_cancel:
			getOrder();
			break;
		default:
			break;
		}
	}

	private void getOrder()
	{
		switch (orderCharge.getOrdersStateType())
		{
		case Reserve:
			HttpOrderUtil.cancleOrder(OrderChargeDetailActivity.this, orderCharge.getId(), true);
			break;
		case InUse:
			HttpOrderUtil.stopOrder(OrderChargeDetailActivity.this, orderCharge.getId(), true);
			break;
		case Complete:
			HttpOrderUtil.deleteOrder(OrderChargeDetailActivity.this, orderCharge.getId(), true);
			break;
		case Cancel:
			HttpOrderUtil.deleteOrder(OrderChargeDetailActivity.this, orderCharge.getId(), true);
			break;
		case Waste:
			HttpOrderUtil.deleteOrder(OrderChargeDetailActivity.this, orderCharge.getId(), true);
			break;
		default:
			break;
		}
	}

}
