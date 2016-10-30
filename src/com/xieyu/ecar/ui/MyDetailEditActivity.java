package com.xieyu.ecar.ui;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.view.SimpleTitleBar;

/**
 * @author fanbaolong
 * @data 2016年10月30日  下午9:42:57
 * @描述 我的详情修改界面
 */
@ContentView(R.layout.activity_my_detail)
public class MyDetailEditActivity extends BackableTitleBarActivity{
	
	@ViewInject(R.id.my_edit)
	private EditText my_edit;
	
	@ViewInject(R.id.my_delete)
	private ImageView my_delete;
	
	private int type;
	private String str = "";
	
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setView();
	}

	private void setView() {
		type = getIntent().getIntExtra("type", 0);
		str = getIntent().getStringExtra("str");
		if(null != str && !"".equals(str) ){
			my_edit.setText(str);
		}
		
		String title = getStrByType(type);
		getTitleBar().setTitle(title);
		my_delete.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.my_delete:
			my_edit.setText("");
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * 根据传过来的type来显示title
	 * @param type
	 * @return
	 */
	private String getStrByType(int type){
		String str = "";
		if(type == 0){
			str = "我的用户名";
		}else if(type == 1){
			str = "我的手机号";
		}else if(type == 2){
			str = "我的手机号";
		}else if(type == 3){
			str = "我的地址";
		}
		
		return str;
	}
	
	@Override
	protected void onTitleBarCreated(SimpleTitleBar titleBar) {
		super.onTitleBarCreated(titleBar);
		titleBar.setRightButtonText("保存");
		titleBar.setOnRightButtonClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String mStr = getText(my_edit);
				if(TextUtils.isEmpty(mStr)){
					App.showShortToast("请输入有效内容后再保存");
					return;
				}
				if(!"".equals(str) && mStr.equals(str)){
					App.showShortToast("没有改动，不需要保存");
					return;
				}
				hideInputManager(MyDetailEditActivity.this);
				Intent intent = new Intent();
				intent.putExtra("str", mStr);
				intent.putExtra("type", type);
				setResult(123, intent);
				finish();
			}
		});
	}

}
