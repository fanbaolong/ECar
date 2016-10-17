package com.xieyu.ecar.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.xieyu.ecar.R;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;

/**
 * @author fanbaolong
 *
 *         租车协议
 */
public class RegisterRuleActivity extends BackableTitleBarActivity
{
	@V
	private WebView register_rule;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_register_rule);
		Injector.getInstance().inject(this);

		getTitleBar().setTitle(R.string.register_text2);

		register_rule.setOverScrollMode(View.OVER_SCROLL_NEVER);
		register_rule.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		register_rule.getSettings().setSupportZoom(true);
		register_rule.getSettings().setBuiltInZoomControls(true);
		register_rule.getSettings().setJavaScriptEnabled(true);
		register_rule.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		register_rule.getSettings().setAllowFileAccess(true);
		register_rule.getSettings().setAppCacheEnabled(true);
		register_rule.getSettings().setDomStorageEnabled(true);

		register_rule.loadUrl("file:///android_asset/agreement.html");
	}
}
