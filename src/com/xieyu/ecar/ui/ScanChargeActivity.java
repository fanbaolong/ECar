package com.xieyu.ecar.ui;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.Piles;
import com.xieyu.ecar.bean.PilesStatus;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.PreferenceUtil;

/**
 * @author fbl
 * 
 *         扫码预约充电
 *
 */
public class ScanChargeActivity extends BackableTitleBarActivity
{
	@V
	private TextView charge_type_text, car_status_text, charge_num_text;
	@V
	private Button btn_to_charge;

	private String id = "";
	private String siteId = "";
	private String sn = "";

	public static Piles mPile = new Piles();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_scan_charge);
		getTitleBar().setTitle(R.string.scan_charge);

		id = getIntent().getStringExtra("id");

		setView();

	}

	private void setView()
	{
		Injector.getInstance().inject(this);

		getPilesById(id);

		btn_to_charge.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.btn_to_charge:
			toMatchOrder();

			break;

		default:
			break;
		}
	}

	/**
	 * 获取充电桩详情
	 * 
	 * @param id
	 */
	private void getPilesById(String id)
	{
		RequestParams params = new RequestParams(BaseConstants.getPilesById);
		params.addBodyParameter("id", id + "");
		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						Gson gson = new Gson();
						if (jsonObject.isNull("objectResult"))
						{
							mPile = null;
						} else
						{
							mPile = gson.fromJson(jsonObject.getJSONObject("objectResult").toString(), Piles.class);
							siteId = mPile.getSite().getId() + "";
							sn = mPile.getSn();
						}

						updateView(mPile);

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
				App.showShortToast(ex.getMessage());
				finish();
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

	protected void updateView(Piles piles)
	{
		if (piles == null)
		{
			car_status_text.setText("不可用");
			btn_to_charge.setBackgroundResource(R.drawable.btn_of_gray);
			btn_to_charge.setClickable(false);
		} else
		{
			charge_type_text.setText(mPile.getPilesCategory().getName());
			charge_num_text.setText(mPile.getSn());

			if (mPile.getPilesStatus() == null)
			{
				car_status_text.setText("不可用");
				setButtonTextColor(PilesStatus.Using);
			} else
			{
				car_status_text.setText(mPile.getPilesStatusStr(mPile.getPilesStatus()));
				setButtonTextColor(mPile.getPilesStatus());
			}

		}

	}

	private void setButtonTextColor(PilesStatus pilesStatus)
	{

		switch (pilesStatus)
		{

		case Using:
			btn_to_charge.setBackgroundResource(R.drawable.btn_of_gray);
			btn_to_charge.setClickable(false);
			break;
		case Usable:
			btn_to_charge.setBackgroundResource(R.drawable.btn_yellow_select);
			btn_to_charge.setClickable(true);
			break;
		case Exception:
			btn_to_charge.setBackgroundResource(R.drawable.btn_of_gray);
			btn_to_charge.setClickable(false);
			break;

		default:
			break;
		}
	}

	/**
	 * 去匹配订单
	 */
	private void toMatchOrder()
	{
		RequestParams params = new RequestParams(BaseConstants.scanCode);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("type", "Piles");
		params.addBodyParameter("siteId", siteId);
		params.addBodyParameter("id", id);
		params.addBodyParameter("sn", sn);

		x.http().post(params, new CommonCallback<String>()
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
						String id = "";
						// 为null表示没有订单，需要去重新建立,不为null把id传过去
						if (jsonObject.getJSONObject("objectResult").isNull("order"))
						{
							id = "";
						} else
						{
							id = jsonObject.getJSONObject("objectResult").getJSONObject("order").getString("id");
						}
						String startTime = jsonObject.getJSONObject("objectResult").getString("startTime");
						String endTime = jsonObject.getJSONObject("objectResult").getString("endTime");

						Intent intent = new Intent(ScanChargeActivity.this, ScanChargeDetailActivity.class);
						intent.putExtra("OrderId", id);
						intent.putExtra("startTime", startTime);
						intent.putExtra("endTime", endTime);
						startActivity(intent);
					} else
					{
						App.showShortToast(jsonObject.getString("resultMes"));
					}

				} catch (Exception e)
				{
				}

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{
				App.showShortToast(ex.getMessage());
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

}
