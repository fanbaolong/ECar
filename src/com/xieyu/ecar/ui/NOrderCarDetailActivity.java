package com.xieyu.ecar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.R.layout;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.bean.OrdersStateType;
import com.xieyu.ecar.http.HttpOrderUtil;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.view.CircularImageView;
import com.xieyu.ecar.util.AlertDialog;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.AlertDialog.OnClickOKListener;
import com.xieyu.ecar.util.StringUtil;

/**
 * 租车订单详情
 * 
 * @author niemin
 *
 */
public class NOrderCarDetailActivity extends BackableTitleBarActivity
{
	@V
	private View order_checkout_success,order_using,order_stroke_ends,order_checkout_cancel;
	@V
	private View order_checkout_success_bt,order_using_bt;
	@V
	private RelativeLayout bottom_layout;
	/*
	 * title
	 */
	@V
	private CircularImageView img_url;
	@V
	private TextView tv_car_id, tv_car_oil, tv_car_type,tv_repay_car_time;
	/*
	 * order statu
	 */
	@V
	private ImageView order_success_left,order_success,using_line_left,using,using_line_right,stroke_ends,stroke_ends_reight;
	@V
	private TextView tv_order_success, tv_using, tv_stroke_ends;
	@V
	private TextView order_statu;
	/*
	 * order car config info  车辆配置信息
	 */
	@V
	private RelativeLayout info_switch;
	@V
	private ImageView info_switch_arrow;
	@V
	private LinearLayout car_config_info;
	@V
	private TextView tv_car_full_charge,tv_car_max_speed,tv_car_state;
	@V
	private TextView order_number,order_time;
	/*
	 * 下单成功
	 */
	@V
	private TextView get_car_addr,tv_order_cancel;
	@V
	private LinearLayout order_get_car;
	//订单取消
	@V
	private TextView order_cancel_time,order_cost;
	
	/*
	 * 使用中
	 */
	@V
	private TextView order_used_time,order_current_cost,order_current_charge;
	@V
	private ImageView order_open_door,order_close_door;
	@V
	private TextView tv_order_return_car;
	/*
	 * 行程结束
	 */
	@V
	private TextView get_car_addr_end,return_car_addr,get_car_time,return_car_time,repay_car_duration,
	car_rental_costs,coupon,amount_credit,real_payment,payment_account;

	private OrderPile orderCar = new OrderPile();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_ordercardetail_n);
		getTitleBar().setTitle(R.string.order_car_detail);
		Injector.getInstance().inject(this);
		EventBus.getDefault().register(this);
		orderCar = (OrderPile) getIntent().getSerializableExtra("orderCharge");
		setView();

	}

	private void setView()
	{
		tv_car_type.setText(StringUtil.isNull(orderCar.getCarCategoryName()));
		if (null != orderCar.getCar()){
			tv_car_id.setText(StringUtil.isNull(orderCar.getCar().getLicense()));
		} else {
			tv_car_id.setText("");
		}
		tv_car_oil.setText("100%");
		orderStatu();
	}

	private void orderStatu() {

		dataCarConfigInfo();
		if (orderCar.getOrdersStateType().equals(OrdersStateType.Reserve)
				|| orderCar.getOrdersStateType().equals(OrdersStateType.Cancel)
				|| orderCar.getOrdersStateType().equals(OrdersStateType.WaitUse)
				|| orderCar.getOrdersStateType().equals(OrdersStateType.Waste)) {

			order_success_left.setSelected(true);
			order_success.setSelected(true);
			using_line_left.setSelected(false);
			using.setSelected(false);
			using_line_right.setSelected(false);
			stroke_ends.setSelected(false);
			stroke_ends_reight.setSelected(false);

			tv_order_success.setTextColor(getResources().getColor(R.color.text_6));
			tv_using.setTextColor(getResources().getColor(R.color.text_9));
			tv_stroke_ends.setTextColor(getResources().getColor(R.color.text_9));

			if (orderCar.getOrdersStateType().equals(OrdersStateType.Cancel)) {
				order_statu.setText("您的订单已取消！");
				dataOrderCancel();
			}else if (orderCar.getOrdersStateType().equals(OrdersStateType.Waste)) {
				order_statu.setText("您的订单已过期作废！");
				dataOrderCancel();
			}else {
				order_statu.setText("您已下单成功！");
				dataOrderSuccess();

			}
		}else if (orderCar.getOrdersStateType().equals(OrdersStateType.WaitBack)
				|| orderCar.getOrdersStateType().equals(OrdersStateType.InUse)) {

			order_success_left.setSelected(true);
			order_success.setSelected(true);
			using_line_left.setSelected(true);
			using.setSelected(true);
			using_line_right.setSelected(false);
			stroke_ends.setSelected(false);
			stroke_ends_reight.setSelected(false);

			tv_order_success.setTextColor(getResources().getColor(R.color.text_6));
			tv_using.setTextColor(getResources().getColor(R.color.text_6));
			tv_stroke_ends.setTextColor(getResources().getColor(R.color.text_9));

			order_statu.setText("车辆使用中");

			dataUsing();
		}else if (orderCar.getOrdersStateType().equals(OrdersStateType.Complete)) {

			order_success_left.setSelected(true);
			order_success.setSelected(true);
			using_line_left.setSelected(true);
			using.setSelected(true);
			using_line_right.setSelected(true);
			stroke_ends.setSelected(true);
			stroke_ends_reight.setSelected(true);

			tv_order_success.setTextColor(getResources().getColor(R.color.text_6));
			tv_using.setTextColor(getResources().getColor(R.color.text_6));
			tv_stroke_ends.setTextColor(getResources().getColor(R.color.text_6));

			order_statu.setText("您的行程已结束，谢谢使用！");
			dataStrokeEnds();

		}
	}
	/*
	 * 车辆配置信息
	 */
	private void dataCarConfigInfo() {
		tv_car_full_charge.setText(StringUtil.isNull(orderCar.getCarCategory().getFullElectricalEndurance()) + "km");
		tv_car_max_speed.setText(StringUtil.isNull(orderCar.getCarCategory().getTopSpeed()) + "km/h");
		tv_car_state.setText(StringUtil.isNull(orderCar.getCarCategory().getTopSpeed()) + "kw");
		order_number.setText("订单编号："+StringUtil.isNull(orderCar.getSn()));
		order_time.setText("下单时间："+StringUtil.isNull(orderCar.getReServeTime()));

		info_switch.setOnClickListener(this);
		info_switch_arrow.setSelected(true);
		car_config_info.setVisibility(View.VISIBLE);
	}
	/*
	 * 下单成功
	 */
	private void dataOrderSuccess() {
		order_checkout_success.setVisibility(View.VISIBLE);
		order_checkout_success_bt.setVisibility(View.VISIBLE);
		order_using.setVisibility(View.GONE);
		order_using_bt.setVisibility(View.GONE);
		order_stroke_ends.setVisibility(View.GONE);
		order_checkout_cancel.setVisibility(View.GONE);
		bottom_layout.setVisibility(View.VISIBLE);
		
		get_car_addr.setText(StringUtil.isNull(orderCar.getReSitePositionName()));

		tv_order_cancel.setOnClickListener(this);
		order_get_car.setOnClickListener(this);
	}
	/*
	 * 下单成功：订单取消
	 */
	private void dataOrderCancel() {
		order_checkout_success.setVisibility(View.GONE);
		order_checkout_success_bt.setVisibility(View.GONE);
		order_using.setVisibility(View.GONE);
		order_using_bt.setVisibility(View.GONE);
		order_stroke_ends.setVisibility(View.GONE);
		order_checkout_cancel.setVisibility(View.VISIBLE);
		bottom_layout.setVisibility(View.GONE);
		
		info_switch.setVisibility(View.GONE);
		car_config_info.setVisibility(View.GONE);
	}
	/*
	 * 使用中
	 */
	private void dataUsing() {
		order_checkout_success.setVisibility(View.GONE);
		order_checkout_success_bt.setVisibility(View.GONE);
		order_using.setVisibility(View.VISIBLE);
		order_using_bt.setVisibility(View.VISIBLE);
		order_stroke_ends.setVisibility(View.GONE);
		order_checkout_cancel.setVisibility(View.GONE);
		bottom_layout.setVisibility(View.VISIBLE);
		
		order_used_time.setText("01天13时20分15秒");
		order_current_cost.setText("3000.00");
		order_current_charge.setText("50%");

		tv_order_return_car.setOnClickListener(this);
		order_open_door.setOnClickListener(this);
		order_close_door.setOnClickListener(this);
		order_open_door.setSelected(true);
		order_close_door.setSelected(false);
	}
	/*
	 * 行程结束
	 */
	private void dataStrokeEnds() {
		order_checkout_success.setVisibility(View.GONE);
		order_checkout_success_bt.setVisibility(View.GONE);
		order_using.setVisibility(View.GONE);
		order_using_bt.setVisibility(View.GONE);
		order_stroke_ends.setVisibility(View.VISIBLE);
		order_checkout_cancel.setVisibility(View.GONE);
		bottom_layout.setVisibility(View.GONE);
		
		get_car_addr_end.setText(StringUtil.isNull(orderCar.getReSitePositionName()));
		return_car_addr.setText(StringUtil.isNull(orderCar.getReSitePositionName()));
		get_car_time.setText(StringUtil.isNull(StringUtil.getDateTime(orderCar.getBeginTime())));
		return_car_time.setText(StringUtil.isNull(StringUtil.getDateTime(orderCar.getEndTime())));
		
//		repay_car_duration.setText(StringUtil.getDateTime(orderCar.getBeginTime()));
//		car_rental_costs.setText(StringUtil.getDateTime(orderCar.getBeginTime()));
//		coupon.setText(StringUtil.getDateTime(orderCar.getBeginTime()));
//		amount_credit.setText(StringUtil.getDateTime(orderCar.getBeginTime()));
//		real_payment.setText(StringUtil.getDateTime(orderCar.getBeginTime()));
//		payment_account.setText(StringUtil.getDateTime(orderCar.getBeginTime()));
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
		case R.id.order_get_car:
			getCar();
			break;
		case R.id.tv_order_return_car:
			Intent intent = new Intent(NOrderCarDetailActivity.this,OrderCarReturnActivity.class);
			intent.putExtra("orderDetail", orderCar);
			startActivity(intent);
			break;
		case R.id.info_switch:
			if (info_switch_arrow.isSelected()) {
				info_switch_arrow.setSelected(false);
				car_config_info.setVisibility(View.GONE);
			}else {
				info_switch_arrow.setSelected(true);
				car_config_info.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.order_open_door:
			HttpOrderUtil.openDoor(this, orderCar.getCar().getId());
			break;
		case R.id.order_close_door:
			HttpOrderUtil.closeDoor(this, orderCar.getCar().getId());
			break;
		default:
			break;
		}
	}
	public void onEvent(EventMessage message)
	{
		switch (message){
		case switchUpdate:
			if (order_open_door.isSelected()) {
				order_open_door.setSelected(false);
				order_close_door.setSelected(true);
			}else {
				order_open_door.setSelected(true);
				order_close_door.setSelected(false);
			}
			break;

		default:
			break;
		}
	}
	
	private void getCar()
	{
		RequestParams params = new RequestParams(BaseConstants.takeOrder);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(NOrderCarDetailActivity.this, BaseConstants.prefre.SessionId));
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
			HttpOrderUtil.cancleOrder(NOrderCarDetailActivity.this, orderCar.getId(), true);
			break;
		case InUse:
			// / 使用中
			AlertDialog dialog = new AlertDialog(NOrderCarDetailActivity.this, R.style.add_dialog, "");
			dialog.setContent("该操作将触发还车申请，不可撤销，是否确认？");
			dialog.setOnClickOKListener(new OnClickOKListener()
			{
				@Override
				public void getOK()
				{
					HttpOrderUtil.returnCar(NOrderCarDetailActivity.this, orderCar.getId(), orderCar.getCar().getSite().getId(), true);
				}
			});
			dialog.show();

			break;
		case Complete:
			// / 完成
			HttpOrderUtil.deleteOrder(NOrderCarDetailActivity.this, orderCar.getId(), true);
			break;
		case Cancel:
			// / 取消
			HttpOrderUtil.deleteOrder(NOrderCarDetailActivity.this, orderCar.getId(), true);
			break;
		case Waste:
			// / 作废
			HttpOrderUtil.deleteOrder(NOrderCarDetailActivity.this, orderCar.getId(), true);
			break;
		default:
			break;
		}
	}

}
