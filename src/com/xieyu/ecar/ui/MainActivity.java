package com.xieyu.ecar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.http.HttpGetNewVersion;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.fragment.MainFragment;
import com.xieyu.ecar.ui.view.TabHeadView;
import com.xieyu.ecar.util.PreferenceUtil;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity
{

	@V
	private TabHeadView headView_main;
	@V
	public DrawerLayout drawer_layout;
	@V
	public LinearLayout mMenu_layout;
	@V
	private LinearLayout ll_my_head, ll_login, ll_my_wallet, ll_car_process, ll_car_rental, ll_about, ll_edit_pwd, ll_login_out, ll_main;
	@V
	private Button btn_menu_login;
	@V
	private TextView tv_username;

	private MainFragment mainFragment;

	private static long exitTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Injector.getInstance().inject(this);
		EventBus.getDefault().register(this);
		setView();
		if (App.isFirst)
		{
			HttpGetNewVersion.getNewVersion(this);
			App.isFirst = false;
		}
	}

	private void initView()
	{
		String ssesionId = PreferenceUtil.getString(this, BaseConstants.prefre.SessionId);
		if (ssesionId == null || "".equals(ssesionId))
		{
			// 没有登录，隐藏部分选项

			ll_my_head.setVisibility(View.GONE);
			ll_login.setVisibility(View.VISIBLE);
			ll_my_wallet.setVisibility(View.GONE);
			ll_edit_pwd.setVisibility(View.GONE);
			ll_login_out.setVisibility(View.GONE);

		} else
		{
			ll_my_head.setVisibility(View.VISIBLE);
			ll_login.setVisibility(View.GONE);
			ll_my_wallet.setVisibility(View.VISIBLE);
			ll_edit_pwd.setVisibility(View.VISIBLE);
			ll_login_out.setVisibility(View.VISIBLE);
			tv_username.setText(PreferenceUtil.getString(this, BaseConstants.prefre.mUserName));
		}

	}

	public DrawerLayout getDrawer()
	{
		return drawer_layout;
	}

	private void setView()
	{
		// final LinearLayout.LayoutParams params = new
		// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// params.setMargins(0, 0, App.getInstance().getScreenWidth()/3, 0);
		// mMenu_layout.setLayoutParams(params);
		ll_my_head.setOnClickListener(this);
		ll_my_wallet.setOnClickListener(this);
		ll_car_rental.setOnClickListener(this);
		ll_car_process.setOnClickListener(this);
		ll_about.setOnClickListener(this);
		ll_edit_pwd.setOnClickListener(this);
		ll_login_out.setOnClickListener(this);
		ll_main.setOnClickListener(this);
		btn_menu_login.setOnClickListener(this);

		if (mainFragment == null)
		{
			mainFragment = new MainFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.basecontent, mainFragment).commit(); // 将左菜单默认VIEW替换为左菜单Fragment
	}

	public void showContent()
	{
		if (menu.isMenuShowing())
		{
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					menu.toggle(false);
				}
			}, 50);
		}
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.ll_my_head:
			enter(MyActivity.class);
			break;
		case R.id.ll_my_wallet:
			enter(MyWalletActivity.class);
			break;
		case R.id.ll_car_rental:
			Intent it = new Intent();
			it.setClass(MainActivity.this, CarRentalActivity.class);
			it.putExtra("type", "1");
			startActivity(it, true);
			break;
		case R.id.ll_car_process:
			Intent it1 = new Intent();
			it1.setClass(MainActivity.this, CarRentalActivity.class);
			it1.putExtra("type", "2");
			startActivity(it1, true);
			break;
		case R.id.ll_about:
			enter(AboutActivity.class);
			break;
		case R.id.ll_edit_pwd:
			enter(EditPwdActivity.class);
			break;
		case R.id.ll_login_out:
			enter(LoginActivity.class);
			PreferenceUtil.putString(this, BaseConstants.prefre.SessionId, "");
			back();
			break;
		case R.id.btn_menu_login:
			enter(LoginActivity.class);
			break;
		default:
			break;
		}
	}

	private void enter(Class<? extends Activity> clz)
	{
		Intent it = new Intent();
		it.setClass(MainActivity.this, clz);
		startActivity(it, true);
		// drawer_layout.closeDrawer(mMenu_layout);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		initView();

	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		int keyCode = event.getKeyCode();
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			exitApp();
			return true; // 为了让代码不执行下去，如果不加的话会被执行2次
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 退出程序
	 */
	public void exitApp()
	{
		// 2次点击事件时间2秒以内
		if ((System.currentTimeMillis() - exitTime) > 2000)
		{
			App.showShortToast(R.string.click_exit);
			exitTime = System.currentTimeMillis();
		} else
		{
			finish();
			App.isFirst = true;
		}
	}

	public void onEvent(EventMessage message)
	{
		switch (message)
		{
		case loginOut:
			initView();
			break;

		default:
			break;
		}

	}

}
