package com.xieyu.ecar.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.adapter.ViewPagerAdapter;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.PreferenceUtil;

/**
 * @author fanbaolong
 * 
 *         启动页
 */
public class StartActivity extends Activity implements Runnable, OnPageChangeListener
{

	@V
	private ViewPager viewpager;
	@V
	private ImageView img;

	private ArrayList<View> views;
	private int position = 0;
	private ViewPagerAdapter vpAdapter;

	@SuppressLint("InflateParams") @SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_start);
		Injector.getInstance().inject(StartActivity.this);
		boolean isfrist = PreferenceUtil.getBoolean(StartActivity.this, BaseConstants.prefre.PREF_FRIST_STRING);
		if (isfrist)
		{
			viewpager.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
			img.setImageResource(R.drawable.start);
			new Handler().postDelayed(this, 2 * 1000);
		} else
		{
			viewpager.setVisibility(View.VISIBLE);
			img.setVisibility(View.GONE);
			LayoutInflater inflater = LayoutInflater.from(this);
			views = new ArrayList<View>();
			// 初始化引导图片列表
			views.add(inflater.inflate(R.layout.start_one, null));
			views.add(inflater.inflate(R.layout.start_two, null));
			views.add(inflater.inflate(R.layout.start_three, null));
			// 初始化Adapter
			vpAdapter = new ViewPagerAdapter(views, this);
			viewpager.setAdapter(vpAdapter);
			// 绑定回调
			viewpager.setOnPageChangeListener(this);
		}

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		JPushInterface.onPause(this);
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter
	{
		ArrayList<Fragment> fraArrayList;

		public MyFragmentPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments)
		{
			super(fm);
			this.fraArrayList = fragments;
		}

		@Override
		public Fragment getItem(int arg0)
		{
			return fraArrayList.get(arg0);
		}

		@Override
		public int getCount()
		{
			return fraArrayList.size();
		}

	}

	public class MyPageChangerlistener implements ViewPager.OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int arg0)
		{

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		@Override
		public void onPageSelected(int arg0)
		{
			position = arg0;

		}

	}

	@Override
	public void run()
	{
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

	public void onPageScrollStateChanged(int arg0)
	{

	}

	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

	public void onPageSelected(int arg0)
	{

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return false;
	}

}
