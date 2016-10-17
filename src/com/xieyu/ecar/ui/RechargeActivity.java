package com.xieyu.ecar.ui;

import java.util.UUID;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xieyu.aliplay.AliPayReqTools;
import com.xieyu.aliplay.PayResult;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.PaymentMethod;
import com.xieyu.ecar.bean.RechareOrder;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.wxapi.Constants;
import com.xieyu.ecar.wxapi.MD5;

import de.greenrobot.event.EventBus;

/**
 * @author fbl
 * 
 *         充值界面
 */
public class RechargeActivity extends BackableTitleBarActivity
{
	@V
	private RadioButton choice_recharge_radio0, choice_recharge_radio1;
	@V
	private EditText money_text;
	@V
	private Button btn_recharge;

	private IWXAPI api;

	private String appId;
	// 商家想财付通申请的商家 id
	private String partnerId;
	// 预支付订单
	private String prepayId;
	// 随机串，防重发
	private String nonceStr;
	// 时间戳，防重发
	private String timeStamp;
	// 商家根据财付通文档填写的数据和签名
	private String packages;
	// 商家根据微信开放平台文档对数据做的签名
	private String sign;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_recharge);
		getTitleBar().setTitle(R.string.recharge);
		EventBus.getDefault().register(this);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

		setView();
	}

	private void setView()
	{
		Injector.getInstance().inject(this);
		btn_recharge.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.btn_recharge:

			topay();

			break;
		default:
			break;
		}

	}

	/**
	 * 判断充值方式
	 */
	private void topay()
	{

		if (invalidateText(money_text, R.string.input_charge_money))
		{
			return;
		}

		if (choice_recharge_radio0.isChecked())
		{

			if (isCanPayForChart())
			{
				getOrder(1, getText(money_text));
				// payForChart("");
			} else
			{
				App.showShortToast("您微信版本过低或者没安装微信");
			}

		} else if (choice_recharge_radio1.isChecked())
		{
			getOrder(2, getText(money_text));
			// payForAliPay();
		}

	}

	private boolean isCanPayForChart()
	{
		return api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
	}

	/**
	 * 微信支付
	 * 
	 * @param order
	 */
	private void payForChart(RechareOrder order)
	{
		appId = Constants.APP_ID;
		partnerId = BaseConstants.partnerId;
		String noncStr = UUID.randomUUID().toString().replace("-", "");
		packages = "Sign=WXPay";
		timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

		String A = "appid=" + appId + "&noncestr=" + noncStr + "&package=" + packages + "&partnerid=" + partnerId + "&prepayid=" + prepayId + "&timestamp=" + timeStamp;
		String SignTemp = A + "&key=lvnengvencentmoutlookcom12345678";

		App.showLog("SignTemp==" + SignTemp);

		byte[] sendBytes = null;
		try
		{
			sendBytes = SignTemp.getBytes("UTF8");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		sign = MD5.getMessageDigest(sendBytes).toUpperCase();
		
		App.showLog("sign==" + sign);

		PayReq request = new PayReq();
		request.appId = appId;
		request.partnerId = partnerId;
		request.prepayId = prepayId;
		request.packageValue = packages;
		request.nonceStr = noncStr;
		request.timeStamp = timeStamp;
		request.sign = sign;
		
		App.showShortToast("正常调起支付");
		api.registerApp(Constants.APP_ID);
		api.sendReq(request);

	}

	/**
	 * 支付宝支付
	 */
	private void payForAliPay(RechareOrder order)
	{

		String orderNo = order.getOrdersSn();
		String orderName = "余额充值";
		String price = order.getMoney() + "";

		AliPayReqTools aliPayReqTools = new AliPayReqTools(this, handler);
		aliPayReqTools.pay(orderNo, orderName, price);

	}

	/**
	 * 作为支付宝支付的回调
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case 1:
				// aliPayResultMsg(result);

				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000"))
				{
					App.showShortToast("支付成功");
				} else
				{
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000"))
					{
						App.showShortToast("支付结果确认中");

					} else
					{
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
					}
				}

				break;
			case 2:
				Toast.makeText(RechargeActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 获取订单
	 * 
	 * @param payMethodStr
	 * @param money
	 */
	private void getOrder(final int type, String money)
	{
		showLoadingDialog("");
		RequestParams params = new RequestParams(BaseConstants.createAccountDetail);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("paymentMethod", type == 1 ? PaymentMethod.微信.toString() : PaymentMethod.支付宝.toString());
		params.addBodyParameter("money", money);

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
						RechareOrder rechaerOrder = gson.fromJson(jsonObject.getJSONObject("objectResult").getJSONObject("accountDetail").toString(), RechareOrder.class);

						if (type == 1)
						{
							prepayId = jsonObject.getJSONObject("objectResult").getString("prepayId");
							payForChart(rechaerOrder);

						} else
						{
							payForAliPay(rechaerOrder);
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
				dismissLoadingDialog();
			}
		});

	}

	public void onEvent(Constants.PayResult payResultResult)
	{
		switch (payResultResult)
		{
		case success:
			App.showShortToast("支付成功");
			break;
		case cancle:
			// setPayResultBack("用户取消");
			App.showShortToast("用户取消");
			break;
		case fail:
			// setPayResultBack("支付失败");
			App.showShortToast("支付失败");
			break;
		case error:
			// setPayResultBack("未知错误");
			App.showShortToast("未知错误");
			break;

		default:
			break;
		}

	}
}
