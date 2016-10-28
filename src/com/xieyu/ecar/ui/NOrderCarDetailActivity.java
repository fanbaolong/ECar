package com.xieyu.ecar.ui;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.bean.OrdersStateType;
import com.xieyu.ecar.http.HttpOrderUtil;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.view.CircularImageView;
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
public class NOrderCarDetailActivity extends BackableTitleBarActivity
{
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
	 * 下单成功
	*/
	@V
	private RelativeLayout info_switch;
	@V
	private ImageView info_switch_arrow;
	@V
	private TextView get_car_addr,tv_car_full_charge,tv_car_max_speed,tv_car_state;
	@V
	private TextView order_number,order_time,tv_order_cancel;
	@V
	private LinearLayout order_get_car,car_config_info;
	/*
	 * 使用中
	*/
	
	
	/*
	 * 行程结束
	*/
	
	
	private OrderPile orderCar = new OrderPile();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_ordercardetail_n);
		getTitleBar().setTitle(R.string.order_car_detail);
		Injector.getInstance().inject(this);
		orderCar = (OrderPile) getIntent().getSerializableExtra("orderCharge");
		setView();

	}

	private void setView()
	{
		tv_order_cancel.setOnClickListener(this);
		
		order_get_car.setOnClickListener(this);
		
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
			}else if (orderCar.getOrdersStateType().equals(OrdersStateType.Waste)) {
				order_statu.setText("您的订单已过期作废！");
			}else {
				order_statu.setText("您已下单成功！");
			}
			dataOrderSuccess();
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
		}
	}

	private void dataOrderSuccess() {
		get_car_addr.setText(StringUtil.isNull(orderCar.getReSitePositionName()));
		tv_car_full_charge.setText(StringUtil.isNull(orderCar.getCarCategory().getFullElectricalEndurance()) + "km");
		tv_car_max_speed.setText(StringUtil.isNull(orderCar.getCarCategory().getTopSpeed()) + "km/h");
		tv_car_state.setText(StringUtil.isNull(orderCar.getCarCategory().getTopSpeed()) + "kw");
		
		order_number.setText(StringUtil.isNull(orderCar.getSn()));
		order_time.setText(StringUtil.isNull(orderCar.getReServeTime()));
		
		info_switch.setOnClickListener(this);
		info_switch_arrow.setSelected(true);
		car_config_info.setVisibility(View.VISIBLE);
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
		case R.id.info_switch:
			if (info_switch_arrow.isSelected()) {
				info_switch_arrow.setSelected(false);
				car_config_info.setVisibility(View.GONE);
			}else {
				info_switch_arrow.setSelected(true);
				car_config_info.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
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
