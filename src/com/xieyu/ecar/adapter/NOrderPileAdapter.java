package com.xieyu.ecar.adapter;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.bean.OrdersStateType;
import com.xieyu.ecar.http.HttpOrderUtil;
import com.xieyu.ecar.util.AlertDialog;
import com.xieyu.ecar.util.AlertDialog.OnClickOKListener;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.StringUtil;

import de.greenrobot.event.EventBus;

/**
 * @author fbl
 * 
 *         电桩适配器
 *
 */
public class NOrderPileAdapter extends AbsViewHolderAdapter<OrderPile>
{
	private Activity cActivity;

	public NOrderPileAdapter(Activity context, int layoutRes)
	{
		super(context, layoutRes);
		this.cActivity = context;
	}

	@SuppressLint("NewApi")
	@Override
	protected void bindData(int pos, final OrderPile itemData)
	{

		TextView tv_name = getViewFromHolder(R.id.tv_name);
		TextView tv_isbook = getViewFromHolder(R.id.tv_isbook);
		TextView tv_address = getViewFromHolder(R.id.tv_address);
		TextView tv_starttime = getViewFromHolder(R.id.tv_starttime);
		TextView tv_endtime = getViewFromHolder(R.id.tv_endtime);
		TextView tv_type = getViewFromHolder(R.id.tv_type);
		TextView tv_booktime = getViewFromHolder(R.id.tv_booktime);
		TextView tv_booked = getViewFromHolder(R.id.tv_booked);
		ImageView img_url = getViewFromHolder(R.id.img_url);

		// tv_isbook.setTextColor(mContext.getResources().getColor(OrderPile.getColorId(itemData.getOrdersStateType())));
		// tv_booked.setTextColor(mContext.getResources().getColor(OrderPile.getColorId(itemData.getOrdersStateType())));
		// tv_booked.setBackground(mContext.getResources().getDrawable(OrderPile.getDrawableType(itemData.getOrdersStateType())));

		tv_name.setText("订单号：" + StringUtil.isNull(itemData.getOrderNo()));
		
		if (itemData.getOrdersStateType() != null)
		{
			tv_isbook.setText(OrderPile.getOrderStatesStr(itemData.getOrdersStateType()));
		} else
		{
			tv_isbook.setText("");
		}
		tv_address.setText("取车地址：" + StringUtil.isNull(itemData.getReSitePositionName()));
		tv_starttime.setText("下单时间：" + StringUtil.isNull(itemData.getReServeTime()));
		if (TextUtils.isEmpty(itemData.getReServerEndTime())) {
			tv_endtime.setText("还车时间：" + StringUtil.defaultTime);
		}else {
			tv_endtime.setText("还车时间：" + StringUtil.isNull(itemData.getReServerEndTime()));
		}
//		if (itemData.isReserve())
//		{
//			tv_starttime.setText("预约取车时间：" + StringUtil.isNull(itemData.getReServerBeginTime()));
//			tv_endtime.setText("预约还车时间：" + StringUtil.isNull(itemData.getReServerEndTime()));
//		} else
//		{
//			tv_starttime.setText("取车时间：" + StringUtil.isNull(itemData.getReServerBeginTime()));
//			tv_endtime.setText("还车时间：" + StringUtil.isNull(itemData.getReServerEndTime()));
//		}

//		tv_type.setText("车子类型：" + StringUtil.isNull(itemData.getCarCategoryName()));
//		//根据状态来判断按钮是否显示
//		if(itemData.getOrdersStateType().equals(OrdersStateType.WaitUse) || itemData.getOrdersStateType().equals(OrdersStateType.WaitBack)){
//			tv_booked.setVisibility(View.GONE);
//		}else {
//			tv_booked.setVisibility(View.VISIBLE);
//			tv_booked.setText(OrderPile.showOrderStatus(itemData.getOrdersStateType()));
//		}
		tv_type.setVisibility(View.GONE);
		tv_booktime.setText(StringUtil.isNull(itemData.getCarCategoryName()));
		tv_booked.setVisibility(View.GONE);
		tv_booked.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// doSomeByType(itemData.getOrdersStateType(), itemData.getId(),
				// itemData.getCar().getSite().getId());
				doSomeByType(itemData);
			}
		});
	}

	/**
	 * 判断即将要去做什么 取消订单 ，停止充电，删除订单等操作
	 * 
	 * @param ordersStateType
	 * @param id
	 */
	protected void doSomeByType(final OrderPile mOrderPile)
	{
		OrdersStateType ordersStateType = mOrderPile.getOrdersStateType();
		final int id = mOrderPile.getId();

		// OrdersStateType ordersStateType, final int id, final int siteId
		AlertDialog dialog = new AlertDialog(cActivity, R.style.add_dialog, "");
		switch (ordersStateType)
		{
		case Reserve:
			dialog.setContent("您确定要取消订单吗？");
			dialog.setOnClickOKListener(new OnClickOKListener()
			{
				@Override
				public void getOK()
				{
					HttpOrderUtil.cancleOrder(cActivity, id, false);
				}
			});
			break;
		case InUse:
			dialog.setContent("该操作将触发还车申请，不可撤销，是否确认？");
			dialog.setOnClickOKListener(new OnClickOKListener()
			{
				@Override
				public void getOK()
				{
					HttpOrderUtil.returnCar(cActivity, id, mOrderPile.getCar().getSite().getId(), false);
				}
			});
			break;
		case Complete:
			dialog.setContent("您确定要删除订单吗？");
			dialog.setOnClickOKListener(new OnClickOKListener()
			{
				@Override
				public void getOK()
				{
					HttpOrderUtil.deleteOrder(cActivity, id, false);
				}
			});
			break;
		case Cancel:
			dialog.setContent("您确定要删除订单吗？");
			dialog.setOnClickOKListener(new OnClickOKListener()
			{
				@Override
				public void getOK()
				{
					HttpOrderUtil.deleteOrder(cActivity, id, false);
				}
			});
			break;
		case Waste:
			dialog.setContent("您确定要删除订单吗？");
			dialog.setOnClickOKListener(new OnClickOKListener()
			{
				@Override
				public void getOK()
				{
					HttpOrderUtil.deleteOrder(cActivity, id, false);
				}
			});
			break;
		default:
			break;
		}
		dialog.show();

	}

	/**
	 * 删除订单
	 * 
	 * @param id
	 */
	private void deleteOrder(int id)
	{

		RequestParams params = new RequestParams(BaseConstants.deleteOrder);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(mContext, BaseConstants.prefre.SessionId));
		params.addBodyParameter("ids", id + "");

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
						EventBus.getDefault().post(EventMessage.refreshOrder);
						App.showShortToast(jsonObject.getString("resultMes"));

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
			}

		});

	}

	/**
	 * 停止充电
	 * 
	 * @param id
	 */
	private void stopOrder(int id)
	{
		RequestParams params = new RequestParams(BaseConstants.closePiles);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(mContext, BaseConstants.prefre.SessionId));
		params.addBodyParameter("mobile", PreferenceUtil.getString(mContext, BaseConstants.prefre.mTelphone));
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
						EventBus.getDefault().post(EventMessage.refreshOrder);
						App.showShortToast(jsonObject.getString("resultMes"));

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

	/**
	 * 取消订单
	 * 
	 * @param id
	 */
	private void cancleOrder(int id)
	{

		RequestParams params = new RequestParams(BaseConstants.cancelOrder);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(mContext, BaseConstants.prefre.SessionId));
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
						EventBus.getDefault().post(EventMessage.refreshOrder);
						App.showShortToast(jsonObject.getString("resultMes"));

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

}
