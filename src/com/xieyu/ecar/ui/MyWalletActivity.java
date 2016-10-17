package com.xieyu.ecar.ui;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.User;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.PreferenceUtil;

/**
 * @author fanbaolong
 * 
 *         我的钱包
 */
public class MyWalletActivity extends BackableTitleBarActivity
{

	@V
	private RelativeLayout recharge_relat, recharge_detail_relat;
	@V
	private TextView my_balance, freeze_balance, my_integral, my_coupons;

	private User mUser;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.fragment_mywallet);
		getTitleBar().setTitle(R.string.my_wallet);
		Injector.getInstance().inject(this);
		recharge_relat.setOnClickListener(this);
		recharge_detail_relat.setOnClickListener(this);

		getData();
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);

		switch (v.getId())
		{
		case R.id.recharge_relat:
			startActivity(new Intent(MyWalletActivity.this, RechargeActivity.class), true);
			break;
		case R.id.recharge_detail_relat:
			startActivity(new Intent(MyWalletActivity.this, MyBillListActivity.class), true);
			break;

		default:
			break;
		}
	}

	private void getData()
	{

		RequestParams params = new RequestParams(BaseConstants.getMember);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(MyWalletActivity.this, BaseConstants.prefre.SessionId));
		x.http().post(params, new Callback.CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				App.showLog(result);
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						Gson json = new Gson();
						mUser = json.fromJson(jsonObject.getJSONObject("objectResult").toString(), User.class);

						updateView();

					} else
					{
						App.showShortToast(jsonObject.getString("resultMes"));
					}

				} catch (Exception e)
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

	private void updateView()
	{
		my_balance.setText("可用余额：" + (mUser.getBalance() - mUser.getFreezeBalance()) + "元");
		freeze_balance.setText("冻结押金：" + mUser.getFreezeBalance() + "元");
	}
}
