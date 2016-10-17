package com.xieyu.ecar.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.PackageInfoUtil;

/**
 * 关于我们
 * 
 * @author fbl
 *
 */
public class AboutActivity extends BackableTitleBarActivity
{
	@V
	private TextView about_version;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_about);
		getTitleBar().setTitle(R.string.about);

		Injector.getInstance().inject(this);
		about_version.setOnClickListener(this);

		String versionName = PackageInfoUtil.getVersionName(this);
		setTextStr(versionName, App.hasUpdate);

	}

	/**
	 * 给textView设置字体
	 * 
	 * @param version
	 * @param update
	 */
	private void setTextStr(String version, boolean update)
	{
		String str1 = "当前版本 V";
		String str2 = "(已是最新版本)";
		String str3 = "(有新版本,点击更新↑)";

		if (update)
		{
			about_version.setText(str1 + version + str3);
			about_version.setClickable(true);
		} else
		{
			about_version.setText(str1 + version + str2);
			about_version.setClickable(false);
		}

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.about_version:

			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(BaseConstants.DOWNLOAD_URL_ON_FIR);
			intent.setData(content_url);
			startActivity(intent);

			break;
		default:
			break;
		}

	}

}
