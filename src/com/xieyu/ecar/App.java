package com.xieyu.ecar;

import org.xutils.DbManager;
import org.xutils.x;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;
import com.xieyu.ecar.util.FileManager;
import com.xieyu.ecar.util.ScreenSizeUtil;

/**
 * @author fbl
 */
public class App extends Application
{

	private static final String TAG = "com.xieyu.ecar";

	private static final boolean DBG = true;
	
	public static boolean hasUpdate = false; // 判断是否有更新

	public static int select = 0; // 0:地图；1：订单；2：消息
	public static boolean ispager = false; // 判断是否需要选择界面

	public static boolean isPush = false; // 是否是从极光推送回来
	public static int newSelect = 1;// 控制进入消息时选择的界面

	public static boolean isFirst = true;

	private static App mInstance = null;

	public static DbManager db;

	public static ImageLoader mImageLoader = ImageLoader.getInstance();

	public static App getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new App();
		}

		return mInstance;

	}

	// ---------------------------------------------

	@Override
	public void onCreate()
	{
		super.onCreate();

		mInstance = this;

		FileManager.createFolder(this); // Folder

		initImageLoader();
		initJPush();

		x.Ext.init(this);
		x.Ext.setDebug(BuildConfig.DEBUG);
		initDb();

		sizeUtil = new ScreenSizeUtil(this);

		// 百度地图sdk初始化
		SDKInitializer.initialize(getApplicationContext());

		// 添加bugly异常crash
		CrashReport.initCrashReport(this, "900017565", false);

	}

	private void initDb()
	{
		// .setDbDir(new File("/sdcard"))
		DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("LvNengDb").setDbVersion(1).setDbUpgradeListener(new DbManager.DbUpgradeListener()
		{
			@Override
			public void onUpgrade(DbManager db, int oldVersion, int newVersion)
			{

			}
		});

		db = x.getDb(daoConfig);

	}

	private ScreenSizeUtil sizeUtil;

	public ScreenSizeUtil getSizeUtil()
	{
		return sizeUtil;
	}

	public void setSizeUtil(ScreenSizeUtil sizeUtil)
	{
		this.sizeUtil = sizeUtil;
	}

	public int getScreenWidth()
	{
		return sizeUtil.getScreenWidth();
	}

	public int getScreenHeight()
	{
		return sizeUtil.getScreenHeight();
	}

	private void initJPush()
	{
		// 极光推送
		JPushInterface.setDebugMode(BuildConfig.DEBUG); // 设置开启日志，发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
	}

	/* 在这里实例化图片缓存的ImageLoader的功能 */
	private void initImageLoader()
	{

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_img_0).showImageOnLoading(R.drawable.default_img_0).showImageOnFail(R.drawable.default_img_0).cacheInMemory(true).cacheOnDisc(true).build();
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).discCacheSize(100 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		mImageLoader.init(config);

	}

	private static Handler mHandler = new Handler();
	private static Toast toast;

	public static void showShortToast(final CharSequence msg)
	{
		mHandler.post(new Runnable()
		{
			public void run()
			{
				if (toast == null)
					toast = Toast.makeText(mInstance, msg, Toast.LENGTH_SHORT);
				else
					toast.setText(msg);

				toast.show();
			}
		});
	}

	public static void showShortToast(int resId)
	{
		showShortToast(mInstance.getString(resId));
	}

	/**
	 * 打印Logs
	 * 
	 * @param msg
	 */
	public static void showLog(Object msg)
	{
		if (DBG)
			Log.i("ecar", msg + "");
	}

	/**
	 * @param tag
	 * @param msg
	 */
	public static void debug(String tag, Object msg)
	{
		if (DBG)
			Log.d(TextUtils.isEmpty(tag) ? TAG : tag, String.valueOf(msg));
	}

}
