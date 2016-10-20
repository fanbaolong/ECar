package com.xieyu.ecar.util;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xieyu.ecar.R;
import com.xieyu.ecar.adapter.AbsViewHolderAdapter;

public class DialogList extends Dialog implements OnClickListener{

	private Activity activity;

	private TextView tv_title;
	private ListView lv_list;
	private ImageView img_close;

	private String title;
	private List<Pay> pays = new ArrayList<DialogList.Pay>();
	private int selectId = 1;

	// 设置窗口显示
	private Window window = null;
	private OnClickCloseListener closeListener;
	public DialogList(Context context) {
		super(context);
	}

	public DialogList(Activity activity, String title, int selectId){
		super(activity, R.style.Dialog);
		this.activity = activity;
		this.selectId = selectId;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_list);
		tv_title = (TextView) findViewById(R.id.tv_title);
		img_close = (ImageView) findViewById(R.id.img_close);
		lv_list = (ListView) findViewById(R.id.lv_list);
		img_close.setOnClickListener(this);
		showView();
		setWidth();
		setCancelable(true);
	}

	private void setWidth(){
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //设置宽度
		lp.height = (int)(display.getHeight()*1/2);
		getWindow().setAttributes(lp);
	}

	private void showView(){
		pays = getPays();
		tv_title.setText(title);
		ListAdapter adapter = new ListAdapter(activity, pays, R.layout.adapter_dialog_list);
		lv_list.setAdapter(adapter);
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				closeListener.clickCancel(pays.get(arg2));
				dismiss();
			}
		});
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
		wl.gravity = Gravity.BOTTOM; // 设置重力
		window.setAttributes(wl);
	}

	@Override
	public void onClick(View arg0) {
		if (img_close == arg0) {
			dismiss();
		}
	}

	public void setCloseListener(OnClickCloseListener listener) {
		this.closeListener = listener;
	}

	public interface OnClickCloseListener {
		void clickCancel(Pay pay);
	}
	
	public class Pay{
		public int id;
		public String img_logo;
		public String name;
		public String money;
		public boolean select;
	}
	
	private List<Pay> getPays(){
		List<Pay> pays = new ArrayList<DialogList.Pay>();
		Pay pay;
		pay = new Pay();
		pay.id = 1;
		pay.img_logo = "1";
		pay.money = "可用额度2,567.22元";
		pay.name = "绿能公司";
		pay.select = false;
		pays.add(pay);
		pay = new Pay();
		pay.id = 2;
		pay.img_logo = "2";
		pay.money = "可用额度577.55元";
		pay.name = "我是个人测试员";
		pay.select = false;
		pays.add(pay);
		pays.get(selectId - 1).select = true;
		return pays;
	}
	
	private class ListAdapter extends AbsViewHolderAdapter<Pay>{

		public ListAdapter(Context context, List<Pay> data, int layoutRes) {
			super(context, data, layoutRes);
		}

		@Override
		protected void bindData(int pos, Pay itemData) {
			TextView name = getViewFromHolder(R.id.tv_name);
			TextView content = getViewFromHolder(R.id.tv_content);
			ImageView img_select = getViewFromHolder(R.id.img_select);
			ImageView img_logo = getViewFromHolder(R.id.img_logo);
			name.setText(itemData.name);
			content.setText(itemData.money);
			if (itemData.select) {
				img_select.setImageResource(R.drawable.select_yes);
			}else {
				img_select.setImageResource(R.drawable.select_no);
			}
			if (itemData.id == 1) {
				img_logo.setImageResource(R.drawable.order_company);
			}else {
				img_logo.setImageResource(R.drawable.order_person);
			}
		}
		
	}
}
