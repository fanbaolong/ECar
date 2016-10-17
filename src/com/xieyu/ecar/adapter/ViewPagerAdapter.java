package com.xieyu.ecar.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.LoginActivity;
import com.xieyu.ecar.util.PreferenceUtil;

/**
 *
 *
 */
public class ViewPagerAdapter extends PagerAdapter
{

	// 界面列表
	private List<View> views;
	private Activity activity;

	public ViewPagerAdapter(List<View> views, Activity activity)
	{
		this.views = views;
		this.activity = activity;
	}

	// 销毁arg1位置的界面
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2)
	{
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0)
	{
	}

	// 获得当前界面数
	@Override
	public int getCount()
	{
		if (views != null)
		{
			return views.size();
		}
		return 0;
	}

	// 初始化arg1位置的界面
	@Override
	public Object instantiateItem(View arg0, int arg1)
	{
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1)
		{
			ImageView mImage = (ImageView) arg0.findViewById(R.id.imgthree);
			Button mBtn = (Button) arg0.findViewById(R.id.enter_btn);

			mImage.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					PreferenceUtil.putBoolean(activity, BaseConstants.prefre.PREF_FRIST_STRING, true);
					goHome();
				}
			});

			mBtn.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					PreferenceUtil.putBoolean(activity, BaseConstants.prefre.PREF_FRIST_STRING, true);
					goHome();
				}
			});
		}
		return views.get(arg1);
	}

	private void goHome()
	{
		// 跳转
		Intent intent = new Intent(activity, LoginActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{
	}

	@Override
	public Parcelable saveState()
	{
		return null;
	}

	@Override
	public void startUpdate(View arg0)
	{
	}

}
