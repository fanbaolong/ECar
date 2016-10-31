package com.xieyu.ecar.http;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.app.Activity;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.ui.BaseActivity;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.TimeUtil;

import de.greenrobot.event.EventBus;

public class HttpOrderUtil
{
	/**
	 * 删除订单
	 * 
	 * @param id
	 */
	public static void deleteOrder(final Activity cActivity, int id, final boolean detail)
	{
		((BaseActivity) cActivity).showLoadingDialog("删除中。。");
		RequestParams params = new RequestParams(BaseConstants.deleteOrder);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(cActivity, BaseConstants.prefre.SessionId));
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
						App.showShortToast("已删除");
						if (detail)
						{
							cActivity.finish();
						}

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
				((BaseActivity) cActivity).dismissLoadingDialog();
			}

		});

	}

	/**
	 * 停止充电
	 * 
	 * @param id
	 */
	public static void stopOrder(final Activity cActivity, int id, final boolean detail)
	{
		((BaseActivity) cActivity).showLoadingDialog("停止充电。。");
		RequestParams params = new RequestParams(BaseConstants.closePiles);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(cActivity, BaseConstants.prefre.SessionId));
		params.addBodyParameter("mobile", PreferenceUtil.getString(cActivity, BaseConstants.prefre.mTelphone));
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
						if (detail)
						{
							cActivity.finish();
						}
						App.showShortToast("已停止");

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
				((BaseActivity) cActivity).dismissLoadingDialog();
			}

		});

	}

	/**
	 * 取消订单
	 * 
	 * @param id
	 */
	public static void cancleOrder(final Activity cActivity, int id, final boolean detail)
	{
		((BaseActivity) cActivity).showLoadingDialog("取消订单中。。");
		RequestParams params = new RequestParams(BaseConstants.cancelOrder);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(cActivity, BaseConstants.prefre.SessionId));
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
						App.showShortToast("已取消");
						if (detail)
						{
							cActivity.finish();
						}
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
				((BaseActivity) cActivity).dismissLoadingDialog();
			}

		});
	}

	/**
	 * 开启车辆
	 * 
	 * @param id
	 */
	public static void startCar(final Activity cActivity, String id, final boolean detail)
	{
		((BaseActivity) cActivity).showLoadingDialog("解锁中。。");
		RequestParams params = new RequestParams(BaseConstants.startCar);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(cActivity, BaseConstants.prefre.SessionId));
		params.addBodyParameter("carId", id);

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
						App.showShortToast("已解锁");
						if (detail)
						{
							cActivity.finish();
						}
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
				((BaseActivity) cActivity).dismissLoadingDialog();
			}
		});
	}

	/**
	 * 关闭车辆
	 * 
	 * @param id
	 */
	public static void closeCar(final Activity cActivity, String id, final boolean detail)
	{
		((BaseActivity) cActivity).showLoadingDialog("锁车中。。");
		RequestParams params = new RequestParams(BaseConstants.closeCar);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(cActivity, BaseConstants.prefre.SessionId));
		params.addBodyParameter("carId", id + "");

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
						App.showShortToast("已锁车");
						if (detail)
						{
							cActivity.finish();
						}
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
				((BaseActivity) cActivity).dismissLoadingDialog();
			}
		});
	}

	/**
	 * 归还车辆
	 * 
	 * @param orderId
	 */
	public static void returnCar(final Activity cActivity, int orderId, int siteId, final boolean detail)
	{
		((BaseActivity) cActivity).showLoadingDialog("归还车辆。。");
		RequestParams params = new RequestParams(BaseConstants.returnCar);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(cActivity, BaseConstants.prefre.SessionId));
		params.addBodyParameter("orderId", orderId + "");
		params.addBodyParameter("siteId", siteId + "");
		params.addBodyParameter("endType", "NormalEnd");
		params.addBodyParameter("remarks", "");
		params.addBodyParameter("endTime", TimeUtil.getStrTime0(System.currentTimeMillis()));

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
						App.showShortToast("还车待确认");
						if (detail)
						{
							cActivity.finish();
						}
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
				((BaseActivity) cActivity).dismissLoadingDialog();
			}
		});
	}
	/**
	 * 开车门
	 * 
	 * @param id
	 */
	public static void openDoor(final Activity cActivity, String id)
	{
		((BaseActivity) cActivity).showLoadingDialog("开车门中。。");
		RequestParams params = new RequestParams(BaseConstants.openDoor);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(cActivity, BaseConstants.prefre.SessionId));

		x.http().post(params, new CommonCallback<String>()
		{
			@Override
			public void onSuccess(String result)
			{
				App.showLog("result==" + result);
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						EventBus.getDefault().post(EventMessage.switchUpdate);
					} 
					App.showShortToast(jsonObject.getString("resultMes"));

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
				((BaseActivity) cActivity).dismissLoadingDialog();
			}
		});
	}

	/**
	 * 关车门
	 * 
	 * @param id
	 */
	public static void closeDoor(final Activity cActivity, String id)
	{
		((BaseActivity) cActivity).showLoadingDialog("关车门中。。");
		RequestParams params = new RequestParams(BaseConstants.closeDoor);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(cActivity, BaseConstants.prefre.SessionId));

		x.http().post(params, new CommonCallback<String>()
		{
			@Override
			public void onSuccess(String result)
			{
				App.showLog("result==" + result);
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						EventBus.getDefault().post(EventMessage.switchUpdate);
					} 
					App.showShortToast(jsonObject.getString("resultMes"));

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
				((BaseActivity) cActivity).dismissLoadingDialog();
			}
		});
	}

}
