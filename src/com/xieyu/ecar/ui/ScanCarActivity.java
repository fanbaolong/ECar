package com.xieyu.ecar.ui;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.CodeCarDetail;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.StringUtil;

/**
 * @author fbl
 * 
 *         扫码用车
 *
 */
public class ScanCarActivity extends BackableTitleBarActivity
{

	private String id = "";
	private String siteId = "";
	private String sn = "";
	private CodeCarDetail codeCarDetail = new CodeCarDetail();

	@V
	private LinearLayout ll_not_order, ll_have_order;
	@V
	private TextView tv_car_type, tv_car_booknum, tv_car_starttime, tv_car_endtime;
	@V
	private TextView car_id_text, car_type_text, car_status_text, car_xuhang_text, car_speed_text;
	@V
	private Button btn_use_car;

	public static ScanCarActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_scan_car);
		getTitleBar().setTitle(R.string.scan_use_car);
		Injector.getInstance().inject(this);

		instance = this;

		id = getIntent().getStringExtra("id");

		btn_use_car.setOnClickListener(this);

		getDataCarDetail();

	}

	private void getDataCarDetail()
	{

		RequestParams params = new RequestParams(BaseConstants.getCarById);
		params.addBodyParameter("id", id);
		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					String resultType = jsonObject.getString("resultType");
					if ("OK".equals(resultType))
					{
						if (jsonObject.isNull("objectResult"))
						{
							updateView(null);
						} else
						{

							Gson gson = new Gson();
							codeCarDetail = gson.fromJson(jsonObject.getJSONObject("objectResult").toString(), CodeCarDetail.class);

							siteId = codeCarDetail.getSite().getId() + "";
							sn = codeCarDetail.getSn();

							updateView(codeCarDetail);
						}

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

	private void updateView(CodeCarDetail mDetail)
	{

		if (mDetail == null)
		{
			car_id_text.setText("");
			car_type_text.setText("");
			car_status_text.setText("");
			car_xuhang_text.setText("");
			car_speed_text.setText("");

			btn_use_car.setBackgroundResource(R.drawable.btn_of_gray);
			btn_use_car.setClickable(false);
		} else
		{
			car_id_text.setText(StringUtil.isNull(mDetail.getLicense()));
			car_type_text.setText(StringUtil.isNull(mDetail.getCarCategory().getName()));
			car_status_text.setText(StringUtil.isNull(mDetail.getCarStatus()));
			car_xuhang_text.setText(StringUtil.isNull(mDetail.getCarCategory().getFullElectricalEndurance()) + "km");
			car_speed_text.setText(StringUtil.isNull(mDetail.getCarCategory().getTopSpeed()) + "km/h");

			if (mDetail.getCarStatus().equals("可用"))
			{
				btn_use_car.setBackgroundResource(R.drawable.btn_yellow_select);
				btn_use_car.setClickable(true);
			} else
			{
				btn_use_car.setBackgroundResource(R.drawable.btn_of_gray);
				btn_use_car.setClickable(false);
			}
		}

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.btn_use_car:

			toMachOrder();
			break;

		default:
			break;
		}
	}

	private void toMachOrder()
	{
		RequestParams params = new RequestParams(BaseConstants.scanCode);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("type", "Car");
		params.addBodyParameter("siteId", codeCarDetail.getSite().getId() + "");
		params.addBodyParameter("id", id);
		params.addBodyParameter("sn", sn);

		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				
				OrderPile orderPile = null;
				try {
					JSONObject jsonObject = new JSONObject(result);
					if(jsonObject.has("objectResult")){
						Gson gson  = new Gson();
						orderPile = gson.fromJson(jsonObject.getJSONObject("objectResult").toString(),  OrderPile.class);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				Intent intent = new Intent(ScanCarActivity.this, BookCarDetailActivity.class);
				intent.putExtra("mCarDetail", codeCarDetail);
				intent.putExtra("type", "carDetail");
				intent.putExtra("orderPile", orderPile);
				startActivity(intent);
				finish();
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

}
