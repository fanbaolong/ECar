package com.xieyu.ecar.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 安装包版本工具类
 * @author fbl
 * 
 */
public class PackageInfoUtil {

	/**
	 * 获得本地安装包的信息
	 * 
	 * @param context 上下文对象
	 * @throws NameNotFoundException 
	 */
	public static PackageInfo getLocalPackageInfo(Context context) throws NameNotFoundException{
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		return packageManager.getPackageInfo( context.getPackageName(),0);
	}

	public static String getVersionName(Context context) {
		try {
			return getLocalPackageInfo(context).versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	public static int getVersionCode(Context context) {
		try {
			return getLocalPackageInfo(context).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}

	}


}
