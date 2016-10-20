package com.xieyu.ecar.ui;

import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.http.HttpCallBack;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.DialogList;
import com.xieyu.ecar.util.DialogList.OnClickCloseListener;
import com.xieyu.ecar.util.DialogList.Pay;
/**
 * 预约车辆页面
 * @author think
 *
 */
public class BookCarInfoActivity extends BaseActivity{
	
	@V
	private LinearLayout ll_payment_account;
	@V
	private TextView tv_payment_account;
	
	private Pay mPay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookcarinfo);
		Injector.getInstance().inject(this);
		setColor();
		ll_payment_account.setOnClickListener(this);
		init();
	}
	
	private void init() {
		RequestParams params = new RequestParams(BaseConstants.getSites);
		requestPost(false, "", BaseConstants.getSites, params);
		getLogin();
	}

	private void getLogin() {
		RequestParams params = new RequestParams(BaseConstants.Login);
		params.addBodyParameter("userName", "18013508619");
		params.addBodyParameter("passWord", "123456");
		requestPost(true, "登陆中...", BaseConstants.Login, params);
	}
	
	@Override
	public void responseSuccess(String result, String msg, String tag) {
		super.responseSuccess(result, msg, tag);
		if (tag.equals(BaseConstants.getSites)) {
			Log.i(BaseConstants.getSites, result);
		}else if (tag.equals(BaseConstants.Login)) {
			Log.i(BaseConstants.Login, result);
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

		default:
			break;
		}
	}
}
