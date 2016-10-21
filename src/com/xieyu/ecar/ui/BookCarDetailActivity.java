package com.xieyu.ecar.ui;

import java.util.ArrayList;
import java.util.List;

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
import com.google.gson.reflect.TypeToken;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.CarCategory;
import com.xieyu.ecar.bean.CarType;
import com.xieyu.ecar.bean.CodeCarDetail;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.bean.Sites;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.DateTime;
import com.xieyu.ecar.util.DateTime.OnClickTimeListener;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.StringUtil;
import com.xieyu.ecar.util.TimeUtil;
import com.xieyu.ecar.util.TypePopupwindow;
import com.xieyu.ecar.util.TypePopupwindow.IDropdownItemClickListener;

import de.greenrobot.event.EventBus;

/**
 * 预约租车详情
 * 
 * @author wangfeng
 *
 */
public class BookCarDetailActivity extends BackableTitleBarActivity
{

	@V
	private TextView tv_net_name, tv_net_address, tv_charge_num, tv_charge_badnum;
	@V
	private TextView tv_car_starttime, tv_car_type, tv_car_endtime;
	@V
	private TextView tv_is_order, tv_yuyue_quche_time,tv_yuyue_huanche_time,tv_order_sn,tv_car_drivedistance, tv_car_maxspeed, tv_car_power;
	@V
	private Button btn_booked;
	@V
	private LinearLayout lin_yuyue;

	private Sites mSites = new Sites();

	private String type = "";

	private int select = 0;
	private List<CarType> mCarType = new ArrayList<CarType>();
	private List<String> mCarTypeList = new ArrayList<String>();

	private CodeCarDetail mCodeCarDetail = new CodeCarDetail();
	private OrderPile mOrderPile = new OrderPile();

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_bookcardetail);
		Injector.getInstance().inject(this);
		getTitleBar().setTitle("车辆预约");

		// 判断是从哪里进来的
		type = getIntent().getStringExtra("type");
		if ("carDetail".equals(type))
		{
			lin_yuyue.setVisibility(View.VISIBLE);
			mCodeCarDetail = (CodeCarDetail) getIntent().getSerializableExtra("mCarDetail");
			mOrderPile = (OrderPile) getIntent().getSerializableExtra("orderPile");
			mSites = mCodeCarDetail.getSite();
			
			updateReserveView();

		} else if ("map".equals(type))
		{
			mSites = (Sites) getIntent().getSerializableExtra("mpo");
			tv_is_order.setVisibility(View.GONE);
		}

		setView();

	}
	private void updateReserveView(){
		
		if(null == mOrderPile){
			lin_yuyue.setVisibility(View.GONE);
			tv_is_order.setText("是否是预约订单:否");
			
		}else {
			tv_is_order.setVisibility(View.VISIBLE);
			tv_is_order.setText("是否是预约订单:是");
			tv_yuyue_quche_time.setText("预约取车时间:"+mOrderPile.getReServerBeginTime());
			tv_yuyue_huanche_time.setText("预约还车时间:"+mOrderPile.getReServerEndTime());
			tv_order_sn.setText("订单编号:"+mOrderPile.getSn());
		}
	}

	private void setView()
	{

		tv_car_starttime.setOnClickListener(this);
		tv_car_endtime.setOnClickListener(this);
		tv_car_type.setOnClickListener(this);
		btn_booked.setOnClickListener(this);

		tv_net_name.setText("取车网点: " + mSites.getName());
		tv_net_address.setText("取车地址: " + mSites.getPositionName());
		// tv_charge_num.setText("剩余车辆: " + mSites.getCarSum() + " 辆");
		// tv_charge_badnum.setText("营业时间: " + mSites.getWorkBeginTime() + "-" +
		// mSites.getWordEndTime());

		tv_car_starttime.setText(TimeUtil.getStrTime(System.currentTimeMillis()));
		tv_car_endtime.setText(TimeUtil.getStrTime(System.currentTimeMillis()));

		if ("map".equals(type))
		{
			getCarType();
		} else
		{
			updateView1(mCodeCarDetail.getCarCategory());
		}

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.tv_car_starttime:
			dateTimePickerDialog(tv_car_starttime);
			break;
		case R.id.tv_car_endtime:
			dateTimePickerDialog(tv_car_endtime);
			break;
		case R.id.tv_car_type:
			if (mCarTypeList.size() == 0)
			{
				getCarType();
			} else
			{
				showPopup(mCarTypeList, tv_car_type);
			}
			break;

		case R.id.btn_booked:

			if ("carDetail".equals(type))
			{
				zuche();
			} else if ("map".equals(type))
			{
				ReServeCar();
			}

			break;

		default:
			break;
		}
	}

	private TypePopupwindow popup;

	private void showPopup(List<String> sList, final TextView tv)
	{

		int[] location = new int[2];
		tv.getLocationInWindow(location);
		location[1] += tv.getHeight();
		popup = new TypePopupwindow(BookCarDetailActivity.this, sList);
		popup.showAt(location, tv.getWidth(), tv.getHeight(), false);
		popup.setItemClickListener(new IDropdownItemClickListener()
		{

			@Override
			public void onItemClick(String text, int position)
			{
				select = position;
				tv.setText(text);
				updateView(mCarType.get(position));
			}
		});
	}

	// 选择时间
	public void dateTimePickerDialog(final TextView tv)
	{
		DateTime dialog = new DateTime(BookCarDetailActivity.this, R.style.add_dialog, tv.getText().toString());
		dialog.show();
		dialog.setOnClickTimeListener(new OnClickTimeListener()
		{

			@Override
			public void getTime(String time)
			{
				tv.setText(time);
			}
		});
	}

	/**
	 * 获取车辆类别
	 */
	private void getCarType()
	{
		RequestParams params = new RequestParams(BaseConstants.getCarCategoryBySite);
		params.addBodyParameter("siteId", mSites.getId() + "");
		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					String resultType = jsonObject.getString("resultType");
					if (resultType.equals("OK"))
					{
						Gson gson = new Gson();
						mCarType.clear();
						mCarTypeList.clear();
						mCarType = gson.fromJson(jsonObject.getJSONArray("objectResult").toString(), new TypeToken<List<CarType>>()
						{
						}.getType());
						if (mCarType.size() == 0)
						{
							App.showShortToast("暂无车辆类型");
							return;
						}
						for (int i = 0; i < mCarType.size(); i++)
						{
							mCarTypeList.add(mCarType.get(i).getCarCategory().getName());
						}
						updateView(mCarType.get(0));

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

	protected void updateView(CarType mCarType)
	{
		tv_car_type.setText(mCarType.getCarCategory().getName());
		tv_car_drivedistance.setText(StringUtil.isNull(mCarType.getCarCategory().getFullElectricalEndurance()) + "km");
		tv_car_maxspeed.setText(StringUtil.isNull(mCarType.getCarCategory().getTopSpeed()) + "km/h");
		tv_car_power.setText(mCarType.getCarCategory().getElectric() + "kw");
	}

	protected void updateView1(CarCategory mCarCategory)
	{
		tv_car_type.setText(mCarCategory.getName());
		tv_car_type.setCompoundDrawables(null, null, null, null);
		tv_car_type.setClickable(false);

		tv_car_starttime.setCompoundDrawables(null, null, null, null);
		tv_car_starttime.setClickable(false);

		tv_car_drivedistance.setText(StringUtil.isNull(mCarCategory.getFullElectricalEndurance()) + "km");
		tv_car_maxspeed.setText(StringUtil.isNull(mCarCategory.getTopSpeed()) + "km/h");
		tv_car_power.setText(mCarCategory.getElectric() + "kw");

	}

	/**
	 * 租车
	 */
	private void zuche()
	{
		showLoadingDialog("");

		if (TimeUtil.getLongTime(getText(tv_car_starttime)) == TimeUtil.getLongTime(getText(tv_car_endtime)))
		{
			App.showShortToast("开始时间不能等于结束时间");
			return;
		}
		if (TimeUtil.getLongTime(getText(tv_car_starttime)) > TimeUtil.getLongTime(getText(tv_car_endtime)))
		{
			App.showShortToast("开始时间不能大于结束时间");
			return;
		}

		RequestParams params = new RequestParams(BaseConstants.scanCodeOrder);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("type", "Car");
		params.addBodyParameter("siteId", mSites.getId() + "");
		params.addBodyParameter("id", mCodeCarDetail.getId());
		params.addBodyParameter("sn", mCodeCarDetail.getSn());
		if(mOrderPile == null ){
			params.addBodyParameter("orderId", "");
		}else {
			params.addBodyParameter("orderId", mOrderPile.getId()+"");
		}
		params.addBodyParameter("beginTime", getText(tv_car_starttime));
		params.addBodyParameter("endTime", getText(tv_car_endtime));

		x.http().post(params, new CommonCallback<String>()
		{
			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if ("OK".equals(jsonObject.getString("resultType")))
					{
						Gson gson = new Gson();
						OrderPile mOrderPile = gson.fromJson(jsonObject.getJSONObject("objectResult").toString(), OrderPile.class);

						Intent intent = new Intent(BookCarDetailActivity.this, ScanCarCompleteActivity.class);
						intent.putExtra("mOrderPile", mOrderPile);
						startActivity(intent);
						finish();

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
			}

			@Override
			public void onCancelled(CancelledException cex)
			{
			}

			@Override
			public void onFinished()
			{
				dismissLoadingDialog();
			}
		});

	}

	/**
	 * 预约车辆
	 */
	private void ReServeCar()
	{
		if (mCarType.size() == 0)
		{
			App.showShortToast("请先选择车辆类型");
			return;
		}

		if (TimeUtil.getLongTime(getText(tv_car_starttime)) == TimeUtil.getLongTime(getText(tv_car_endtime)))
		{
			App.showShortToast("开始时间不能等于结束时间");
			return;
		}
		if (TimeUtil.getLongTime(getText(tv_car_starttime)) > TimeUtil.getLongTime(getText(tv_car_endtime)))
		{
			App.showShortToast("开始时间不能大于结束时间");
			return;
		}

		RequestParams params = new RequestParams(BaseConstants.reServeCar);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("siteId", mSites.getId() + "");
		params.addBodyParameter("carCategoryId", mCarType.get(select).getCarCategory().getId() + "");
		params.addBodyParameter("reServeStartTime", getText(tv_car_starttime));
		params.addBodyParameter("reServeEndTime", getText(tv_car_endtime));

		x.http().post(params, new CommonCallback<String>()
		{
			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					String resultType = jsonObject.getString("resultType");
					if (resultType.equals("OK"))
					{
						App.showShortToast(jsonObject.getString("resultMes"));
						EventBus.getDefault().post(EventMessage.refreshOrder);
						App.select = 1;
						App.ispager = true;
						back();
					} else
					{
						App.showShortToast(jsonObject.getString("resultMes"));
					}
				} catch (JSONException e)
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

}
