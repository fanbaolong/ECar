package com.xieyu.ecar.ui;

import android.view.View;

import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.view.SimpleTitleBar;

/**
 * 
 * 带有返回键的Avtivity
 * 
 * @author fbl
 */
public class BackableTitleBarActivity extends SimpleTitleBarActivity
{

	@Override
	protected void onTitleBarCreated(SimpleTitleBar titleBar)
	{
		titleBar.setLeftButton(R.drawable.ic_titlebar_back) // back_ico_one
				.setOnLeftButtonClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						back();
					}
				});
	}

}
