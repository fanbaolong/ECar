package com.xieyu.ecar.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;
import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.LoginActivity;

public class DialogPrompt extends Dialog implements OnClickListener{

	private Activity activity;

	private TextView tv_content, tv_ok, tv_login, tv_cancle;
	
	private String content, leftBtn, centerBtn, rightBtn;

    // 设置窗口显示
    private Window window = null;
    private OnClickCloseListener closeListener;
    public DialogPrompt(Context context) {
        super(context);
    }

    public DialogPrompt(Activity activity, String content, String btn, int type){
        super(activity, R.style.Dialog);
        this.activity = activity;
        this.content = content;
        if (type == 1) {
        	this.rightBtn = btn;
		}else {
			this.centerBtn = btn;
		}
        
    }
    
	public DialogPrompt(Activity activity, String content, String leftBtn, String rightBtn){
        super(activity, R.style.Dialog);
        this.activity = activity;
        this.content = content;
        this.leftBtn = leftBtn;
        this.rightBtn = rightBtn;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prompt);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_ok.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        showView();
        setWidth();
		setCancelable(false);
    }

    private void setWidth(){
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        lp.height = (int)(display.getHeight()*1/5);
        getWindow().setAttributes(lp);
    }

    private void showView(){
    	setShowBtn(tv_content, content);
    	setShowBtn(tv_cancle, leftBtn);
    	setShowBtn(tv_login, centerBtn);
    	setShowBtn(tv_ok, rightBtn);
    }
    
    /**
     * 控制文字是否显示
     * @param tv
     * @param str
     */
    private void setShowBtn(TextView tv, String str){
    	if (StringUtils.isEmpty(str)) {
			tv.setVisibility(View.INVISIBLE);
		}else {
			tv.setVisibility(View.VISIBLE);
			tv.setText(str);
		}
    }
    
    public void showDialog() {
        windowDeploy();
        // 设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(true);
        show();
    }

    public void windowDeploy() {
        window = getWindow(); // 得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); // 设置窗口弹出动画
        // 设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        // 根据x，y坐标设置窗口需要显示的位置
        wl.gravity = Gravity.CENTER; // 设置重力
        window.setAttributes(wl);
    }
    
	@Override
	public void onClick(View arg0) {
		if (tv_ok == arg0) {
			if (closeListener != null) {
				closeListener.clickCancel();
			} else {
				dismiss();
			}
		}else if (tv_login == arg0) {
			Intent in = new Intent(activity, LoginActivity.class);
			activity.startActivity(in);
			dismiss();
		}else if (tv_cancle == arg0) {
			dismiss();
		}
	}
	
	public void setCloseListener(OnClickCloseListener listener) {
		this.closeListener = listener;
	}
	
	public interface OnClickCloseListener {
		void clickCancel();
	}
}
