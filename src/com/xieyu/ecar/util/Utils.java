package com.xieyu.ecar.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xieyu.ecar.App;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class Utils {
	
	
	// 拍照
		public static Intent getPicFromCapture(Context ct, File out)
		{
			if (FileManager.checkSDCardExist())
			{
				// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //
				// "android.media.action.IMAGE_CAPTURE";
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // "android.media.action.IMAGE_CAPTURE";
				Uri uri = Uri.fromFile(out);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				return intent;
			} else
			{
				App.showShortToast("没有找到sd卡");
			}
			return null;
		}
	
	
	/**
	 * @Title: getPictureIntent
	 * @Description: 获取系统图库里图片
	 * @param imageUri
	 * @param outputX
	 * @param outputY
	 * @return Intent 返回类型
	 */
	public static Intent getPictureIntent(Uri uri, Uri imageUri, int outputX, int outputY) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		// crop true是设置在开启的Intent中设置显示的View可裁剪
		intent.putExtra("crop", "true");
		// aspectX X方向上的比例
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 2);
		// outputX 裁剪区的宽
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		// 是否保留比例
		intent.putExtra("scale", true);
		// 是否将数据保留在Bitmap中返回
		intent.putExtra("return-data", false);
		// 将URI指向相应的file:///
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false); // no face detection

		return intent;
	}

	
	/**
	 * 图片裁剪
	 * 
	 * @param imageUri
	 * @param outputX
	 * @param outputY
	 * @param isTakePhoto
	 *            是否是拍照
	 * @return
	 */
	public static Intent pictureCrop(Uri imageUri, int outputX, int outputY, boolean isTakePhoto) {
		Intent intent = null;
		if (isTakePhoto)
		{
			intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(imageUri, "image/*");
		} else
		{
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
		}

		// crop true是设置在开启的Intent中设置显示的View可裁剪
		intent.putExtra("crop", "true");
		// aspectX X方向上的比例
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 2);
		// outputX 裁剪区的宽
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		// 是否保留比例
		intent.putExtra("scale", true);
		// 是否将数据保留在Bitmap中返回
		intent.putExtra("return-data", false);
		// 将URI指向相应的file:///
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false); // no face detection

		return intent;
	}

	// 用当前时间给取得的图片命名
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
}
