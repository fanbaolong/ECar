package com.xieyu.ecar.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xieyu.ecar.R;

public class DialogUtil
{

	/**
	 * 自定义输入框Dailog
	 * 
	 * @param context
	 * @param tipMsg
	 */

	@SuppressWarnings("deprecation")
	public static Dialog showCustomInputDialog(Activity context, final String tipMsg)
	{
		final Dialog dialog = new Dialog(context);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) context.getWindowManager().getDefaultDisplay().getWidth();// 设置宽度
		dialog.getWindow().setAttributes(lp);

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog.getWindow().setContentView(R.layout.common_input_dialog);
		dialog.setCanceledOnTouchOutside(false);

		((TextView) dialog.getWindow().findViewById(R.id.tv_common_title)).setText(tipMsg);

		return dialog;
	}
}
