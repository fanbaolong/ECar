package com.xieyu.ecar.ui;

import android.os.Bundle;
import android.view.InflateException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.view.SimpleTitleBar;

/**
 * 标题栏 Activity
 * 
 * @author fbl
 */
public class SimpleTitleBarActivity extends BaseActivity implements OnClickListener
{

	private SimpleTitleBar mTitleBar;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

	}

	@Override
	public void setContentView(int layoutResID)
	{
		this.setContentView(getLayoutInflater().inflate(layoutResID, null));
	}

	@Override
	public void setContentView(View view)
	{
		this.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void setContentView(View view, LayoutParams params)
	{
		if (view instanceof LinearLayout)
		{
			mTitleBar = new SimpleTitleBar(this);
			LinearLayout rootView = (LinearLayout) view;
			rootView.addView(mTitleBar, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.titlebar_default_height)));
			onTitleBarCreated(mTitleBar);
		} else
		{
			new InflateException("父布局应该为 LinearLayout");
		}
		super.setContentView(view, params);
	}

	/**
	 * 标题栏创建好后的回调
	 */
	protected void onTitleBarCreated(SimpleTitleBar titleBar)
	{

	}

	public SimpleTitleBar getTitleBar()
	{
		return mTitleBar;
	}

	@Override
	public void onClick(View v)
	{

	}
}
