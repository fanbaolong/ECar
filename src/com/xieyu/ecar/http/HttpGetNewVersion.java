package com.xieyu.ecar.http;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.Gson;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.NewVersion;
import com.xieyu.ecar.util.AlertDialog;
import com.xieyu.ecar.util.PackageInfoUtil;
import com.xieyu.ecar.util.AlertDialog.OnClickOKListener;

public class HttpGetNewVersion
{

	/**
	 * 获取fir上最新版本
	 * 
	 * @param context
	 */
	public static void getNewVersion(final Activity context)
	{

		RequestParams params = new RequestParams(BaseConstants.UPDATE_URL_ON_FIR);

		x.http().get(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					Gson gson = new Gson();
					NewVersion theVersion = gson.fromJson(jsonObject.toString(), NewVersion.class);

					int code = PackageInfoUtil.getVersionCode(context);
					if (code < theVersion.getVersion())
					{
						App.hasUpdate = true;

						AlertDialog dialog = new AlertDialog(context, R.style.add_dialog, "发现新的版本，是否更新？\n" + theVersion.getChangelog());
						dialog.show();
						dialog.setOnClickOKListener(new OnClickOKListener()
						{
							@Override
							public void getOK()
							{
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								Uri content_url = Uri.parse(BaseConstants.DOWNLOAD_URL_ON_FIR);
								intent.setData(content_url);
								context.startActivity(intent);
							}
						});

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
}
