package com.xieyu.ecar.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.widget.Toast;

import com.xieyu.ecar.R;

public class FileManager
{
	// app文件存储根目录
	public static final String SDPATH = Environment.getExternalStorageDirectory() + "/LvNeng/";

	/**
	 * 获取文件大小
	 *
	 * @param filePath
	 *            :文件的路径
	 * @return
	 */
	public static String getFileSize(String filePath)
	{
		String fileSize = "";
		File targetFile = new File(filePath);
		if (targetFile.exists() && targetFile.isFile())
		{
			long size = targetFile.length();
			fileSize = formetFileSize(size);
		}
		return fileSize;
	}

	/**
	 * 格式化文件大小 * @param fileS，文件的路径
	 *
	 * @return 文件大小的字符串
	 */
	public static String formetFileSize(long fileSize)
	{
		String result = "";
		if ((fileSize / 1024) >= 1)
		{
			fileSize /= 1024;
			if (fileSize / 1024 >= 1)
			{
				result = new BigDecimal(fileSize * 1.0 / 1024).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + " MB";
			} else
			{
				result = fileSize + " kb";
			}
		} else
		{
			result = fileSize + " B";
		}
		return result;
	}


	/**
	 * 删除指定路径的文件
	 */
	public static void deleteGeneratedFile(String path)
	{
		if (path == null || path.equals(""))
		{
			return;
		}
		File videoFile = new File(path);
		if (videoFile.exists() && videoFile.isFile())
		{
			videoFile.delete();
		}
	}
	/**
	 * 判断指定目录下文件是否存在
	 *
	 * @param fileName
	 * @return boolean
	 */
	public static boolean isFileExist(String path, String fileName)
	{
		File file = new File(path + fileName);
		return file.exists();
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath)
	{
		File file = new File(filePath);
		return file.exists();
	}

	public static void deleteAllFile(String path)
	{
		File file = new File(path);
		if (file != null && file.exists())
		{
			for (File temp : file.listFiles())
			{
				temp.delete();
			}
		}
	}

	public static long getAvailableStore(String path)
	{
		// 取得sdcard文件路径
		StatFs statFs = new StatFs(path);
		// 获取block的SIZE
		long blocSize = statFs.getBlockSize();
		// 可使用的Block的数目
		long availaBlock = statFs.getAvailableBlocks();
		long availableSpare = availaBlock * blocSize;

		return availableSpare;
	}

	/**
	 * 流复制
	 *
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copyStream(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[4096];
		int n;
		while ((n = in.read(buffer)) > 0)
		{
			out.write(buffer, 0, n);
		}
		out.close();
	}

	/**
	 * 生成需要的文件夹
	 */
	public static void createFolder(Context context)
	{
		File file = null;
		if (!checkSDCardExist())
			Toast.makeText(context, context.getString(R.string.check_sdcard_fail), Toast.LENGTH_SHORT).show();
		else
		{
			try
			{
				file = new File(FileManager.SDPATH);
				if (!file.exists())
				{
					file.mkdirs();
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/** 检查sd卡 */
	public static boolean checkSDCardExist()
	{
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
}
