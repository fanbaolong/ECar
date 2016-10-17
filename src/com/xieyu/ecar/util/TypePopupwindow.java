package com.xieyu.ecar.util;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.xieyu.ecar.R;
import com.xieyu.ecar.adapter.TypeAdapter;


public class TypePopupwindow {
	
	public PopupWindow mPopupWindow_Area;
	private Activity mActivity;
	private List<String> mList;
	private IDropdownItemClickListener mItemClickListener;
	public TypePopupwindow(Activity activity,List<String> list){
		this.mActivity = activity;
		this.mList = list;
	}
	
	public interface IDropdownItemClickListener {
		public void onItemClick(String text,int position);
	}
	
	public void showAt(int[] location, int width,int height,boolean isfill){
		if(mPopupWindow_Area == null){
			LayoutInflater inflater = mActivity.getLayoutInflater();
			View contentView = inflater.inflate(R.layout.popupwindow_type, null);
			ListView listView = (ListView) contentView.findViewById(R.id.lv_type);
			TypeAdapter adapter = new TypeAdapter(mActivity, mList,R.layout.adapter_type);
			listView.setAdapter(adapter);
			if (isfill) {
				int lv_height = 3;
				if (mList.size() > 3) {
					lv_height = 3;
					LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) listView.getLayoutParams();
					params.height = lv_height*height;
					listView.setLayoutParams(params);
				}
			}
			mPopupWindow_Area = new PopupWindow(contentView, width,LayoutParams.WRAP_CONTENT);
			mPopupWindow_Area.setOutsideTouchable(true);
			mPopupWindow_Area.setFocusable(true);
			mPopupWindow_Area.setBackgroundDrawable(new ColorDrawable());
			mPopupWindow_Area.update();
			listView.setOnItemClickListener(mAreaDropItemListener);
			mPopupWindow_Area.setAnimationStyle(R.style.TypeSelAnimationFade);
			mPopupWindow_Area.showAtLocation(mActivity.getWindow()
					.getDecorView(), Gravity.NO_GRAVITY, location[0], location[1]);
		}
	}
	
	public void setItemClickListener(IDropdownItemClickListener listener){
		mItemClickListener = listener;
	}
	
	private OnItemClickListener mAreaDropItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(mPopupWindow_Area != null && mPopupWindow_Area.isShowing()){
				mPopupWindow_Area.dismiss();
			}
			if(mItemClickListener != null){
				mItemClickListener.onItemClick(""+mList.get(arg2),arg2);
			}
		}
	};

}
