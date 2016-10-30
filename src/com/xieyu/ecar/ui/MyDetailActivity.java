package com.xieyu.ecar.ui;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.util.PreferenceUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author fanbaolong
 * @data 2016年10月30日  下午10:18:31
 * @描述 个人中心页面
 */
@ContentView(R.layout.activty_my_detail)
public class MyDetailActivity extends BackableTitleBarActivity{

	@ViewInject(R.id.my_userno_relat)
	private RelativeLayout my_userno_relat;

	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	
	@ViewInject(R.id.my_usetel_relat)
	private RelativeLayout my_usetel_relat;
	
	@ViewInject(R.id.tv_tel)
	private TextView tv_tel;
	
	@ViewInject(R.id.my_email_relat)
	private RelativeLayout my_email_relat;
	
	@ViewInject(R.id.tv_email)
	private TextView tv_email;
	
	@ViewInject(R.id.my_address_relat)
	private RelativeLayout my_address_relat;
	
	@ViewInject(R.id.tv_address)
	private TextView tv_address;
	
	@ViewInject(R.id.my_shiming_relat)
	private RelativeLayout my_shiming_relat;
	
	@ViewInject(R.id.tv_shiming)
	private TextView tv_shiming;
	
	@ViewInject(R.id.my_drive_card_relat)
	private RelativeLayout my_drive_card_relat;
	
	@ViewInject(R.id.tv_drive_card)
	private TextView tv_drive_card;
	
	@ViewInject(R.id.btn_logout)
	private Button btn_logout;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		getTitleBar().setTitle("我的资料");
		setView();
	}
	
	private void setView() {
		my_userno_relat.setOnClickListener(this);
		my_usetel_relat.setOnClickListener(this);
		my_email_relat.setOnClickListener(this);
		my_address_relat.setOnClickListener(this);
		my_shiming_relat.setOnClickListener(this);
		my_drive_card_relat.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.my_userno_relat:
			startActivityForResult(0, getText(tv_name));
			break;
			
		case R.id.my_usetel_relat:
			startActivityForResult(1, getText(tv_tel));
			break;
			
		case R.id.my_email_relat:
			startActivityForResult(2, getText(tv_email));
			break;
			
		case R.id.my_address_relat:
			startActivityForResult(3, getText(tv_address));
			break;
			
		case R.id.my_shiming_relat:
			break;
			
		case R.id.my_drive_card_relat:
			break;
			
		case R.id.btn_logout:
			loginout();
			break;

		default:
			break;
		}
	}
	
	private void startActivityForResult(int type,String str){
		Intent intent = new Intent(this,MyDetailEditActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("str", str);
		startActivityForResult(intent, 123);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			if(requestCode == 123 && resultCode == 123){
				int type = data.getExtras().getInt("type");
				String str = data.getExtras().getString("str");
				updateDate(type,str);
			}
		}
		
		
	}

	private void updateDate(int type, String str) {
		if(type == 0){
			tv_name.setText(str);
		}else if(type == 1){
			tv_tel.setText(str);
		}else if(type == 2){
			tv_email.setText(str);
		}else if(type == 3){
			tv_address.setText(str);
		}
	}

	/** 退出登录 */
	private void loginout() {
		
		PreferenceUtil.remove(this, BaseConstants.prefre.SessionId);
		Intent intent = new Intent(this,LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

}
