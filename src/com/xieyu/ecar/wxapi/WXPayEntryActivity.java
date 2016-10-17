package com.xieyu.ecar.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xieyu.ecar.App;

import de.greenrobot.event.EventBus;

/**
 * @author fanbaolong
 * 
 *         微信支付结果返回
 *
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req)
	{
	}

	@Override
	public void onResp(BaseResp resp)
	{
		App.showLog("onPayFinish,errCode = " + resp.errCode);

		switch (resp.errCode)
		{
		case 0:
			setPayResult(Constants.PayResult.success);
			break;
		case -1:
			setPayResult(Constants.PayResult.fail);
			break;
		case -2:
			setPayResult(Constants.PayResult.cancle);
			break;
		default:

			break;
		}

	}

	private void setPayResult(Constants.PayResult payResultResult)
	{
		EventBus.getDefault().post(payResultResult);
		// 结束掉中间那个Activity
		// ActivityCollector.removeLastActivity();
		// 结束掉自己
		finish();
	}

}