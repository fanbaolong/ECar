package com.xieyu.ecar.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieyu.ecar.R;

/**
 * Loading旋转加载框
 * 
 * @author fbl
 */
public class LoadingDialog extends AbsCustomAlertDialog
{
	private boolean isCancelable = true;

	public LoadingDialog(Context context)
	{
		super(context);
	}

	public LoadingDialog(Context context, boolean isCancelable)
	{
		super(context);
		this.isCancelable = isCancelable;
	}

	@Override
	protected View getCustomContentView(CharSequence msg)
	{
		View view = View.inflate(getContext(), R.layout.widget_dialog_loading, null);
		TextView msgText = (TextView) view.findViewById(R.id.message);
		msgText.setText(msg);
		LinearLayout mProgressLin = (LinearLayout) view.findViewById(R.id.progressLin);
		// 当msg为空的时候只出现prodressbar，当有内容的时候显示出背景
		if (!"".equals(msg) && msg != null)
		{
			mProgressLin.setBackgroundResource(R.drawable.loadingback);
		}

		return view;
	}

	@Override
	public void showDialog(CharSequence msg)
	{
		super.showDialog(msg);
		mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		WindowManager.LayoutParams lp = mAlertDialog.getWindow().getAttributes();
		lp.dimAmount = 0.3f;
		mAlertDialog.setCanceledOnTouchOutside(isCancelable); // 没有屏蔽返回键
		// mAlertDialog.setCancelable(isCancelable); //连返回键都屏蔽了
		mAlertDialog.getWindow().setAttributes(lp);
		mAlertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}

}
