package com.xieyu.ecar.ui;

import android.content.Intent;
import android.os.Bundle;

import com.xieyu.ecar.App;
import com.xieyu.ecar.util.URLAnalysisUtil;

/**
 * @author fanbaolong
 * 
 *         处理ZBar扫描的结果
 *
 */
public class ZBarScanActivity extends AbsScanBarCodeActivity
{

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		getTitleBar().setTitle("扫码");
	}

	@Override
	int[] getSymbols()
	{
		return null;
	}

	@Override
	void onScanSuccess(String result)
	{
		App.showLog("result=="+result);
		try
		{

			URLAnalysisUtil urlAnalysis = new URLAnalysisUtil();
			urlAnalysis.analysis(result);
			String type = urlAnalysis.getParam("type");
			String id = urlAnalysis.getParam("id");
			String siteId = urlAnalysis.getParam("siteId");
			String sn = urlAnalysis.getParam("sn");

			if ("ECar".equals(type))
			{
				Intent intent = new Intent(ZBarScanActivity.this, ScanCarActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("sn", sn);
				intent.putExtra("siteId", siteId);
				startActivity(intent, true);
			} else if("Piles".equals(type))
			{
				Intent intent = new Intent(ZBarScanActivity.this, ScanChargeActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}else {
				App.showShortToast("错误二维码");
			}
			finish();

		} catch (Exception e)
		{
			App.showShortToast("错误二维码");
			rescan();
		}

	}

}
