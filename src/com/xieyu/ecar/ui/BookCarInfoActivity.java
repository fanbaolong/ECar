package com.xieyu.ecar.ui;

import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.SitesCar;
import com.xieyu.ecar.http.HttpCallBack;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.DialogList;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.DialogList.OnClickCloseListener;
import com.xieyu.ecar.util.DialogList.Pay;
import com.xieyu.ecar.util.StringUtil;
/**
 * 预约车辆页面
 * @author think
 *
 */
public class BookCarInfoActivity extends BaseActivity{
	
	@V
	private LinearLayout ll_payment_account;
	@V
	private TextView tv_payment_account, tv_car_title, tv_car_num, tv_car_electricity
	, tv_car_name, tv_car_deposit, tv_car_hour_money, tv_car_day_money, tv_car_week_money
	, tv_car_address, tv_car_state, tv_car_speed, tv_car_full, tv_order;
	@V
	private ImageView img_back;
	
	private Pay mPay;
	private SitesCar sitesCar = new SitesCar();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookcarinfo);
		Injector.getInstance().inject(this);
//		setColor();
		init();
	}
	
	private void init() {
		sitesCar = (SitesCar) getIntent().getSerializableExtra("mpo");
		ll_payment_account.setOnClickListener(this);
		tv_order.setOnClickListener(this);
		img_back.setOnClickListener(this);
		
		tv_car_title.setText(StringUtil.isNull(sitesCar.getCarCategory().getName()));
		tv_car_name.setText(StringUtil.isNull(sitesCar.getCarCategory().getName()));
		tv_car_num.setText(StringUtil.isNull(sitesCar.getLicense()));
		tv_car_hour_money.setText("￥"+StringUtil.isNull(sitesCar.getCarCategory().getMoneyTime()) + "/时");
		tv_car_day_money.setText(StringUtil.isNull(sitesCar.getCarCategory().getMoneyDay()) + "元/天");
		tv_car_week_money.setText(StringUtil.isNull(sitesCar.getCarCategory().getMoneyWeek()) + "元/周");
		tv_car_address.setText(StringUtil.isNull(sitesCar.getSite().getPositionName()));
		tv_car_state.setText(StringUtil.isNull(sitesCar.getCarStatus()));
		tv_car_speed.setText(StringUtil.isNull(sitesCar.getCarCategory().getTopSpeed()) + "km/h");
		tv_car_full.setText(StringUtil.isNull(sitesCar.getCarCategory().getFullElectricalEndurance()) + "km");
	}

	@Override
	public void responseSuccess(String result, String msg, String tag) {
		super.responseSuccess(result, msg, tag);
		if (tag.equals(BaseConstants.sendOrder)) {
			App.showShortToast("预约成功");
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_payment_account:
			int selectId = 1;
			if (mPay != null) {
				selectId = mPay.id;
			}
			DialogList dialogList = new DialogList(context, "选择付款账户", selectId);
			dialogList.showDialog();
			dialogList.setCloseListener(new OnClickCloseListener() {
				
				@Override
				public void clickCancel(Pay pay) {
					mPay = pay;
					tv_payment_account.setText(pay.name);
				}
			});
			break;
		case R.id.tv_order:
			RequestParams params = new RequestParams(BaseConstants.sendOrder);
			boolean personal = false;
			if (mPay != null && mPay.id == 2) {
				personal = true;
			}
			params.addBodyParameter("personal", personal+"");
			params.addBodyParameter("carId", sitesCar.getId());
			params.addBodyParameter("sessionId", PreferenceUtil.getString(
					this, BaseConstants.prefre.SessionId));
			requestPost(true, "正在下单...", BaseConstants.sendOrder, params);
			break;
		case R.id.img_back:
			back();
			break;
		default:
			break;
		}
	}
}
