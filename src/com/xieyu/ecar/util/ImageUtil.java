package com.xieyu.ecar.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author fbl
 * 
 * 封装了图片的方法
 *
 */
public class ImageUtil {
	
	/**
	 * @Title: getPictureIntent
	 * @Description: 获取系统图库里图片
	 * @param imageUri
	 * @param outputX
	 * @param outputY
	 * @return Intent 返回类型
	 */
	public static Intent getPictureIntent(Uri uri, Uri imageUri, int outputX, int outputY)
	{

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		// crop true是设置在开启的Intent中设置显示的View可裁剪
		intent.putExtra("crop", "true");
		// aspectX X方向上的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
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
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static Intent startPhotoZoom(Uri uri)
	{

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", false);

		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false); // no face detection

		return intent;
	}

	/**
	 * 图片压缩方法实现
	 * 
	 * @param srcPath
	 * @returnc
	 */
	public static Bitmap getimage(String srcPath, String imagePath)
	{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int hh = 800;// 这里设置高度为800f
		int ww = 480;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww)
		{// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh)
		{// 如果高度高的话根据高度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap, imagePath);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 质量压缩
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, String imagePath)
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100)
		{ // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset(); // 重置baos即清空baos
			options -= 10; // 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		try
		{
			FileOutputStream out = new FileOutputStream(imagePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (Exception e)
		{

		}
		return bitmap;
	}

}
