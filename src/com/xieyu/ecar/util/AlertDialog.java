package com.xieyu.ecar.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.R;

public class AlertDialog extends Dialog implements OnClickListener{

	private Activity context;
	private String contentStr = "";

	private TextView tv_yes, tv_no, tv_content;

	private OnClickOKListener onClickOKListener;

	public AlertDialog(Activity context) {
		super(context);
	}

	public AlertDialog(Activity context, int theme, String content){
		super(context, theme);
		this.context = context;
		this.contentStr = content;
	}

	public void setContent(String conString){
		this.contentStr = conString;
	}

	public interface OnClickOKListener{
		public void getOK();
	}

	public void setOnClickOKListener(OnClickOKListener oClickOKListener){
		this.onClickOKListener = oClickOKListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(context);
		final View timepickerview = inflater.inflate(R.layout.dialog,
				null);
		setContentView(timepickerview);
		
		   WindowManager wm = context.getWindowManager();
           Display display = wm.getDefaultDisplay();
           android.view.WindowManager.LayoutParams lp = getWindow().getAttributes();   
          lp.width = App.getInstance().getScreenWidth()*2/3;
          lp.height =LayoutParams.WRAP_CONTENT;
          getWindow().setAttributes(lp);
		
		setCanceledOnTouchOutside(true);

		tv_yes = (TextView) findViewById(R.id.tv_yes);
		tv_no = (TextView) findViewById(R.id.tv_no);
		tv_content = (TextView) findViewById(R.id.tv_content);

		tv_content.setText(StringUtil.isNull(contentStr));
		tv_yes.setOnClickListener(this);
		tv_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_yes:
			onClickOKListener.getOK();
			dismiss();
			break;
		case R.id.tv_no:
			dismiss();
			break;

		default:
			break;
		}
	}

}
