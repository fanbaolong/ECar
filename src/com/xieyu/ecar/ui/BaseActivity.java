package com.xieyu.ecar.ui;

import org.xutils.x;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xieyu.ecar.ExitManager;
import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.view.AbsCustomAlertDialog;
import com.xieyu.ecar.ui.view.LoadingDialog;
import com.xieyu.ecar.util.StringUtil;
import com.xieyu.ecar.util.SystemBarTintManager;

/**
 * @author fanbaolong
 *
 *         基础activity 所有的activity都要继承这个
 */
public class BaseActivity extends FragmentActivity implements GestureDetector.OnGestureListener, OnClickListener
{

	public  final String TAG = getClass().getSimpleName();
	public SlidingMenu menu;
	private LoadingDialog mLoadingDialog;
	public FragmentActivity context;
	private float startX = 0;
	private float endX = 0;
	private int flingLength, flingHeight;
	private GestureDetector detector;
	private boolean gesture = true;
	private boolean startAnim = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		detector = new GestureDetector(context, this);
		ExitManager.getInstance().addActivity(this); // 将activity注册到activity容器
		x.view().inject(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			setTranslucentStatus(context, true);
		}
		// 创建状态栏的管理实例
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 激活导航栏设置
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.holo_title);

		JPushInterface.init(this);
	}

	/**
	 * 状态栏高度算法
	 * 
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity)
	{
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight)
		{
			Class<?> localClass;
			try
			{
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
				statusHeight = activity.getResources().getDimensionPixelSize(i5);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	private static void setTranslucentStatus(Activity activity, boolean on)
	{

		Window win = activity.getWindow();

		WindowManager.LayoutParams winParams = win.getAttributes();

		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

		if (on)
		{

			winParams.flags |= bits;

		} else
		{

			winParams.flags &= ~bits;

		}

		win.setAttributes(winParams);

	}

	public boolean isGesture()
	{
		return gesture;
	}

	public void setGesture(boolean gesture)
	{
		this.gesture = gesture;
	}

	/**
	 * 显示正在加载的进度框
	 */
	public void showLoadingDialog(String msg)
	{
		dismissLoadingDialog();
		mLoadingDialog = new LoadingDialog(this);
		mLoadingDialog.showDialog(msg);
	}

	/**
	 * 显示正在加载的进度框
	 */
	public void showLoadingDialog(String msg, boolean isCancelable)
	{
		dismissLoadingDialog();
		mLoadingDialog = new LoadingDialog(this, isCancelable);
		mLoadingDialog.showDialog(msg);
	}

	/**
	 * 卸载加载进度框
	 */
	public void dismissLoadingDialog()
	{
		AbsCustomAlertDialog.dismissDialog(mLoadingDialog);
	}

	/**
	 * 验证 TextView 中的内容是否为空，若为空就弹出 Toast，内容为 errorStringRes
	 * 
	 * @param textView
	 * @param errorStringRes
	 * @return true 验证未通过，false 验证通过
	 */
	protected boolean invalidateText(TextView textView, int errorStringRes)
	{
		return StringUtil.invalidateContent(getText(textView), errorStringRes);
	}

	/**
	 * 验证 TextView 中的内容是否为空
	 * 
	 * @param textView
	 * @param errorStringRes
	 * @return true 验证未通过，false 验证通过
	 */
	protected boolean invalidateText(TextView textView)
	{
		return TextUtils.isEmpty(getText(textView));
	}

	/**
	 * get the text info...
	 * 
	 * @param textView
	 * @return
	 */
	protected String getText(TextView textView)
	{
		return textView.getText().toString();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:// 起始位置
			// if (null != menu) {
			// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// }
			startX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:// 拖动中位置
			break;
		case MotionEvent.ACTION_UP:// 结束时位置
			if (!gesture)
			{
				break;
			}
			endX = event.getX();
			System.out.println("startX:" + startX + ";endX:" + endX);
			if (endX - startX > 100.0)
			{
				if (null != menu)
				{
					menu.showMenu();
				}
			}
			break;
		default:
			break;
		}
		if (gesture)
			return this.detector.onTouchEvent(event);
		else
			return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0)
	{
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		try
		{
			if (e1.getX() - e2.getX() > flingLength)
			{
				// Intent intent = new Intent();
				// intent.setClass(MainActivity.this, MainActivity.class);
				// startActivity(intent);
				// overridePendingTransition(R.anim.push_left_out,
				// R.anim.push_left_in);
				return true;
			} else if (e2.getX() - e1.getX() > flingLength && Math.abs(e1.getY() - e2.getY()) < flingHeight)
			{
				back(true);
				// Intent intent = new Intent();
				// intent.setClass(MainActivity.this, MainActivity.class);
				// startActivity(intent);
				// overridePendingTransition(R.anim.push_right_out,
				// R.anim.push_right_in);
				return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0)
	{

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0)
	{

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0)
	{
		return false;
	}

	protected void back()
	{
		back(true);
	}

	protected void back(boolean anim)
	{
		finish();
		if (android.os.Build.VERSION.SDK_INT > 10)
		{
			if (anim)
				overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
			else
			{

			}
		}
	}

	public boolean isStartAnim()
	{
		return startAnim;
	}

	public void setStartAnim(boolean startAnim)
	{
		this.startAnim = startAnim;
	}

	public void startActivity(Intent intent, boolean anim)
	{
		startAnim = anim;
		startActivity(intent);
		if (android.os.Build.VERSION.SDK_INT > 10)
		{
			if (anim)
			{
				overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			} else
			{
				overridePendingTransition(0, 0);
			}
		}
	}

	public void popBackAllStack()
	{
		getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	@Override
	public void onClick(View v)
	{

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		try
		{
			JPushInterface.onResume(this); // 统计用户打开次数
		} catch (Exception e)
		{
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		JPushInterface.onPause(this);
	}

	// 隐藏软键盘
	public void hideInputManager(Context ct)
	{
		try
		{
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (!imm.isActive())
			{
				return;
			}
			if (((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)) != null && (((Activity) ct).getCurrentFocus() != null))
			{
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) ct).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e)
		{
			Log.e("KDActivity", "hideInputManager Catch error,skip it!", e);
		}
	}

	public void showInputManager(View v)
	{
		try
		{
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(v, 0);
		} catch (Exception e)
		{
			Log.e("KDActivity", "showInputManager Catch error,skip it!", e);
		}
	}

}
