package com.xieyu.ecar.ui;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.view.TabHeadView;
import com.xieyu.ecar.util.PreferenceUtil;
/**
 * 远程控制
 * @author think
 *
 */
public class ControlActivity extends BaseActivity{
	
	@V
	private TabHeadView headView_control;
	@V
	private TextView tv_car_name, tv_car_electricity, tv_car_num;
	@V
	private ImageView img_car, img_car_open, img_car_close, img_car_blow, 
	img_car_back;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		Injector.getInstance().inject(this);
		init();
	}

	private void init() {
		headView_control.setLeftTitle("远程控制");
		headView_control.getLeftButton().setImageResource(R.drawable.car_left);
		headView_control.setLeftOnClick(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				back();
			}
		});
		img_car_open.setOnClickListener(this);
		img_car_close.setOnClickListener(this);
		img_car_blow.setOnClickListener(this);
		img_car_back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_car_open://开门
			setHttp(BaseConstants.openDoor);
			break;
		case R.id.img_car_close://关门
			setHttp(BaseConstants.clsoeDoor);
			break;
		case R.id.img_car_blow://鸣笛
			setHttp(BaseConstants.lookCar);
			break;
		case R.id.img_car_back://还车
			
			break;

		default:
			break;
		}
	}
	
	private void setHttp(String url){
		RequestParams params = new RequestParams(url);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		requestPost(true, "", url, params);
	}
	
	@Override
	public void responseSuccess(String result, String msg, String tag) {
		super.responseSuccess(result, msg, tag);
		if (tag.equals(BaseConstants.openDoor)) {
			App.showShortToast("开门成功");
		}else if (tag.equals(BaseConstants.clsoeDoor)) {
			App.showShortToast("关门成功");
		}else if (tag.equals(BaseConstants.lookCar)) {
			App.showShortToast("鸣笛成功");
		}
	}
	
}
