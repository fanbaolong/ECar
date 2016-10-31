package com.xieyu.ecar.ui;

import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.http.HttpOrderUtil;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.view.CircularImageView;
import com.xieyu.ecar.util.AlertDialog;
import com.xieyu.ecar.util.AlertDialog.OnClickOKListener;
import com.xieyu.ecar.util.StringUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 还车详情
 * 
 * @author niemin
 *
 */
public class OrderCarReturnActivity extends BackableTitleBarActivity
{
	/*
	 * title
	 */
	@V
	private CircularImageView img_url;
	@V
	private TextView tv_car_id, tv_car_oil, tv_car_type,tv_repay_car_time;

	@V
	private RelativeLayout coupon_layout;
	@V
	private TextView get_car_addr, return_car_addr, get_car_time,return_car_time,repay_car_duration,
	yugu_cost,coupon,jifen_max_amount,jifen_amount,yugu_pending,tv_order_return_car;

	private OrderPile orderCar = new OrderPile();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_ordercarreturn);
		getTitleBar().setTitle(R.string.order_car_detail);
		Injector.getInstance().inject(this);
		orderCar = (OrderPile) getIntent().getSerializableExtra("orderDetail");
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

		coupon_layout.setOnClickListener(this);
		tv_order_return_car.setOnClickListener(this);
		
		get_car_addr.setText(StringUtil.isNull(orderCar.getReSitePositionName()));
		return_car_addr.setText(StringUtil.isNull(orderCar.getReSitePositionName()));
		get_car_time.setText(StringUtil.isNull(StringUtil.getDateTime(orderCar.getBeginTime())));
		return_car_time.setText(StringUtil.isNull(StringUtil.getDateTime(orderCar.getEndTime())));
		
		repay_car_duration.setText(StringUtil.isNull(orderCar.getReSitePositionName()));
		yugu_cost.setText(StringUtil.isNull(orderCar.getReSitePositionName()));
		coupon.setText(StringUtil.isNull(StringUtil.getDateTime(orderCar.getBeginTime())));
		jifen_max_amount.setText(StringUtil.isNull(StringUtil.getDateTime(orderCar.getEndTime())));
		yugu_pending.setText(StringUtil.isNull(orderCar.getReSitePositionName()));

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.coupon_layout:
//			selectCouponPopupWindow();
			break;
		case R.id.tv_order_return_car:
			
			break;
		default:
			break;
		}
	}

//	private void selectCouponPopupWindow() {
//		PopupWindow popu;
//		datalist.clear();
//		for (int i = 0; i < 10; i++) {
//			datalist.add("测试"+i);
//		}
//
//		View selectView = inflater.inflate(R.layout.pop_selector, null);
//		ListView listView = (ListView) selectView.findViewById(R.id.list_one);
//		int height = datalist.size();
//		if (datalist.size() > 5) {
//			height = 5;
//			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) listView.getLayoutParams();
//			params.height = popupwindow.getHeight()*height;
//			listView.setLayoutParams(params);
//		}
//		adapter = new SelectAdapter<String>(this, datalist);
//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				popupwindow.setText(datalist.get(position));
//				adapter.notifyDataSetChanged();
//				popu.dismiss();
//			}
//		});
//
//		popu = new PopupWindow(selectView);
//		popu.setWidth(popupwindow.getWidth());
//		popu.setHeight(LayoutParams.WRAP_CONTENT);
//		popu.setOutsideTouchable(true);
//		popu.setFocusable(true);
//		lp.alpha = 0.4f;
//		getWindow().setAttributes(lp);
//		popu.setOnDismissListener(new OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				lp.alpha = 1.0f;
//				getWindow().setAttributes(lp);
//			}
//		});
//		popu.setBackgroundDrawable(new ColorDrawable());
//		int[] location = new int[2];  
//		popupwindow.getLocationOnScreen(location);
//
//		//在控件下方，popup 从上向下显示
//		popu.setAnimationStyle(R.style.TypeSelAnimationFade);
//		popu.showAsDropDown(popupwindow);
//
//		//在空间上方，popup 从下向上显示
//		//		popu.setAnimationStyle(R.style.TypeSelAnimationFadeBottom);
//		//		popu.showAtLocation(popupwindow, Gravity.NO_GRAVITY, 
//		//				location[0], location[1]-popupwindow.getHeight()*height);
//
//	
//	}

	private void toOperateOrder()
	{
		switch (orderCar.getOrdersStateType())
		{
		case Reserve:
			// 预约中
			HttpOrderUtil.cancleOrder(OrderCarReturnActivity.this, orderCar.getId(), true);
			break;
		case InUse:
			// / 使用中
			AlertDialog dialog = new AlertDialog(OrderCarReturnActivity.this, R.style.add_dialog, "");
			dialog.setContent("该操作将触发还车申请，不可撤销，是否确认？");
			dialog.setOnClickOKListener(new OnClickOKListener()
			{
				@Override
				public void getOK()
				{
					HttpOrderUtil.returnCar(OrderCarReturnActivity.this, orderCar.getId(), orderCar.getCar().getSite().getId(), true);
				}
			});
			dialog.show();

			break;
		case Complete:
			// / 完成
			HttpOrderUtil.deleteOrder(OrderCarReturnActivity.this, orderCar.getId(), true);
			break;
		case Cancel:
			// / 取消
			HttpOrderUtil.deleteOrder(OrderCarReturnActivity.this, orderCar.getId(), true);
			break;
		case Waste:
			// / 作废
			HttpOrderUtil.deleteOrder(OrderCarReturnActivity.this, orderCar.getId(), true);
			break;
		default:
			break;
		}
	}

}
