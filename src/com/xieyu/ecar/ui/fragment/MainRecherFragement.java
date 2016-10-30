package com.xieyu.ecar.ui.fragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.view.TabHeadView;

@ContentView(R.layout.fragement_recher)
public class MainRecherFragement extends BaseFragment{
	
	@ViewInject(R.id.top_title)
	private TabHeadView top_title;
	
	@ViewInject(R.id.empty_text)
	private TextView empty_view;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setView();
	}
	private void setView(){
		
		initTitle();
	}
	private void initTitle() {
		top_title.setTitle("充电");
		top_title.getLeftButton().setVisibility(View.GONE);
		empty_view.setText("暂未开通，敬请期待！");
	}

}
