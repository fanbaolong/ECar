package com.xieyu.ecar.ui;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.http.HttpOrderUtil;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.AlertDialog;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.StringUtil;
import com.xieyu.ecar.util.AlertDialog.OnClickOKListener;

/**
 * 租车订单详情
 * 
 * @author wangfeng
 *
 */
public class OrderCarDetailActivity extends BackableTitleBarActivity
{

	@V
	private TextView tv_isbook, tv_site_name, tv_address, tv_type;
	@V
	private TextView tv_car_id, tv_car_mun, tv_car_booked, tv_car_drive_distance, tv_car_max_speed, tv_car_power, tv_car_type;
	@V
	private TextView tv_create_time, tv_take_car_time, tv_repay_car_time, tv_take_car_time_reality, tv_repay_car_time_reality;
	@V
	private TextView reson_weizhang,money_weizhang, money_jiesuan, tv_is_appoint;
	@V
	private TextView tv_order_cancel, tv_order_suoche, tv_order_jiesuo;

	@V
	private WebView image_web;
	@V
	private LinearLayout buttom_lin;

	private OrderPile orderCar = new OrderPile();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_ordercardetail);
		getTitleBar().setTitle(R.string.order_car_detail);
		Injector.getInstance().inject(this);
		orderCar = (OrderPile) getIntent().getSerializableExtra("orderCharge");
		setView();

	}

	private void setView()
	{
		tv_order_cancel.setOnClickListener(this);
		tv_order_suoche.setOnClickListener(this);
		tv_order_jiesuo.setOnClickListener(this);

		
		tv_isbook.setText(OrderPile.getOrderStatesStr(orderCar.getOrdersStateType()));
		// 订单
		tv_site_name.setText("取车网点：" + StringUtil.isNull(orderCar.getReSiteName()));
		tv_address.setText("取车地址：" + StringUtil.isNull(orderCar.getReSitePositionName()));

		// 车辆信息
		if (orderCar.getCar() != null)
		{
			tv_car_id.setText(StringUtil.isNull(orderCar.getCar().getLicense()));
		} else
		{
			tv_car_id.setText("");
		}

		tv_car_type.setText(StringUtil.isNull(orderCar.getCarCategoryName()));
		tv_car_drive_distance.setText(StringUtil.isNull(orderCar.getCarCategory().getFullElectricalEndurance()) + "km");
		tv_car_max_speed.setText(StringUtil.isNull(orderCar.getCarCategory().getTopSpeed()) + "km/h");

		// 是否是预约单
		tv_is_appoint.setText("是否是预约订单：" + (orderCar.isReserve() == true ? "是" : "否"));

		// 订单信息
		tv_create_time.setText("下单时间：" + StringUtil.isNull(orderCar.getReServeTime()));
		//不是预约单的时候不显示
		if(orderCar.isReserve()){
			tv_take_car_time.setVisibility(View.VISIBLE);
			tv_repay_car_time.setVisibility(View.VISIBLE);
			tv_take_car_time.setText("预约取车时间：" + StringUtil.isNull(orderCar.getReServerBeginTime()));
			tv_repay_car_time.setText("预约还车时间：" + StringUtil.isNull(orderCar.getReServerEndTime()));
		}else {
			tv_take_car_time.setVisibility(View.GONE);
			tv_repay_car_time.setVisibility(View.GONE);
		}
		
		

		if (orderCar.getBeginTime() != null)
		{
			tv_take_car_time_reality.setText("取车时间：" + StringUtil.isNull(StringUtil.getDateTime(orderCar.getBeginTime())));
		} else
		{
			tv_take_car_time_reality.setVisibility(View.GONE);
		}

		if (orderCar.getEndTime() != null)
		{
			tv_repay_car_time_reality.setText("还车时间：" + StringUtil.isNull(StringUtil.getDateTime(orderCar.getEndTime())));
		} else
		{
			tv_repay_car_time_reality.setVisibility(View.GONE);
		}

		// 金额
		if(orderCar.getDetainreason() == null || "".equals(orderCar.getDetainreason()) ){
			reson_weizhang.setVisibility(View.GONE);
			money_weizhang.setVisibility(View.GONE);
		}else {
			reson_weizhang.setText("扣费原因：" + StringUtil.isNull(orderCar.getDetainreason()));
			money_weizhang.setText("违章扣费：" + orderCar.getDetainMoney());
		}

		money_jiesuan.setText("结算金额：" + StringUtil.isNull(orderCar.getCost()));

		showWebView();

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.tv_order_cancel:
			toOperateOrder();
			break;
		case R.id.tv_order_jiesuo:
			HttpOrderUtil.startCar(this, orderCar.getCar().getId(), true);
			break;
		case R.id.tv_order_suoche:
			HttpOrderUtil.closeCar(this, orderCar.getCar().getId(), true);
			break;

		default:
			break;
		}
	}

	private void showWebView()
	{
		switch (orderCar.getOrdersStateType())
		{
		case Reserve:
			// 已预约
			image_web.setVisibility(View.GONE);
			tv_order_cancel.setBackgroundColor(getResources().getColor(R.color.holo_title));
			tv_order_cancel.setText("取消预约");
			break;
		case InUse:
			// 使用中
			image_web.setVisibility(View.VISIBLE);
			tv_order_cancel.setBackgroundColor(getResources().getColor(R.color.orange));
			tv_order_cancel.setText("立即还车");
			tv_order_suoche.setVisibility(View.VISIBLE);
			tv_order_jiesuo.setVisibility(View.VISIBLE);

			image_web.loadUrl("file:///android_asset/image.html");
			break;
		case Complete:
			// 已还车
			tv_order_cancel.setBackgroundColor(getResources().getColor(R.color.holo_title));
			tv_order_cancel.setText("删除订单");
			image_web.setVisibility(View.VISIBLE);
			image_web.loadUrl("file:///android_asset/image1.html");
			break;
		case Cancel:
			// 取消
			tv_order_cancel.setBackgroundColor(getResources().getColor(R.color.holo_title));
			tv_order_cancel.setText("删除订单");
			image_web.setVisibility(View.GONE);
			break;
		case Waste:
			// 已作废
			tv_order_cancel.setBackgroundColor(getResources().getColor(R.color.holo_title));
			tv_order_cancel.setText("删除订单");
			image_web.setVisibility(View.GONE);
			break;
		case WaitUse:
			// 等待使用
			buttom_lin.setVisibility(View.GONE);
			image_web.setVisibility(View.GONE);
			break;
		case WaitBack:
			// 等待还车
			buttom_lin.setVisibility(View.GONE);
			image_web.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/** 详情接口 */
	private void getData()
	{
		RequestParams params = new RequestParams(BaseConstants.getOrderById);
		params.addBodyParameter("id", orderCar.getId() + "");
		params.addBodyParameter("sessionId", PreferenceUtil.getString(OrderCarDetailActivity.this, BaseConstants.prefre.SessionId));
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

	private void toOperateOrder()
	{
		switch (orderCar.getOrdersStateType())
		{
		case Reserve:
			// 预约中
			HttpOrderUtil.cancleOrder(OrderCarDetailActivity.this, orderCar.getId(), true);
			break;
		case InUse:
			// / 使用中
			AlertDialog dialog = new AlertDialog(OrderCarDetailActivity.this, R.style.add_dialog, "");
				dialog.setContent("该操作将触发还车申请，不可撤销，是否确认？");
				dialog.setOnClickOKListener(new OnClickOKListener()
				{
					@Override
					public void getOK()
					{
						HttpOrderUtil.returnCar(OrderCarDetailActivity.this, orderCar.getId(), orderCar.getCar().getSite().getId(), true);
					}
				});
				dialog.show();
			
			break;
		case Complete:
			// / 完成
			HttpOrderUtil.deleteOrder(OrderCarDetailActivity.this, orderCar.getId(), true);
			break;
		case Cancel:
			// / 取消
			HttpOrderUtil.deleteOrder(OrderCarDetailActivity.this, orderCar.getId(), true);
			break;
		case Waste:
			// / 作废
			HttpOrderUtil.deleteOrder(OrderCarDetailActivity.this, orderCar.getId(), true);
			break;
		default:
			break;
		}
	}

}
