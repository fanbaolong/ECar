package com.xieyu.ecar.ui;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.User;
import com.xieyu.ecar.util.PreferenceUtil;

/**
 * @author fanbaolong
 * 
 *         我的钱包
 */
@ContentView(R.layout.fragment_mywallet)
public class MyWalletActivity extends BaseActivity {

	@ViewInject(R.id.title_left)
	private ImageButton title_left;
	
	@ViewInject(R.id.tv_recharge)
	private TextView tv_recharge;

	@ViewInject(R.id.tv_recharge_detail)
	private TextView tv_recharge_detail;

	@ViewInject(R.id.my_balance)
	private TextView my_balance;

	@ViewInject(R.id.freeze_balance)
	private TextView freeze_balance;

	private User mUser;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		tv_recharge.setOnClickListener(this);
		tv_recharge_detail.setOnClickListener(this);
		title_left.setOnClickListener(this);

		getData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.tv_recharge:
			startActivity(new Intent(MyWalletActivity.this, RechargeActivity.class), true);
			break;
		case R.id.tv_recharge_detail:
			startActivity(new Intent(MyWalletActivity.this, MyBillListActivity.class), true);
			break;

		default:
			break;
		}
	}

	private void getData() {

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

	private void updateView() {
		my_balance.setText(""+ (mUser.getBalance() - mUser.getFreezeBalance()));
		freeze_balance.setText(""+mUser.getFreezeBalance());
	}
}
