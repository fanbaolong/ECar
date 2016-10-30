package com.xieyu.ecar.ui.fragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xieyu.ecar.R;
import com.xieyu.ecar.ui.CarRentalActivity;
import com.xieyu.ecar.ui.MyActivity;
import com.xieyu.ecar.ui.MyDetailActivity;
import com.xieyu.ecar.ui.MyWalletActivity;

/**
 * @author fanbaolong
 * @data 2016年10月29日  下午7:17:54
 * @描述  我的界面
 */
@ContentView(R.layout.fragment_mine)
public class MainMineFragemnt extends BaseFragment{
	
	@ViewInject(R.id.mine_top_relat)
	private RelativeLayout mine_top_relat;
	
	@ViewInject(R.id.title_right)
	private ImageView title_right;
	
	@ViewInject(R.id.user_detail_relat)
	private RelativeLayout user_detail_relat;
	
	@ViewInject(R.id.userName)
	private TextView userName;
	
	@ViewInject(R.id.userTel)
	private TextView userTel;
	
	@ViewInject(R.id.tv_rental_num)
	private TextView tv_rental_num;
	
	@ViewInject(R.id.tv_driven_distance)
	private TextView tv_driven_distance;
	
	@ViewInject(R.id.tv_saving)
	private TextView tv_saving;
	
	@ViewInject(R.id.tv_rental_time)
	private TextView tv_rental_time;
	
	@ViewInject(R.id.tv_my_walet)
	private TextView tv_my_walet;
	
	@ViewInject(R.id.tv_my_discount)
	private TextView tv_my_discount;
	
	@ViewInject(R.id.tv_my_piao)
	private TextView tv_my_piao;
	
	@ViewInject(R.id.tv_flow)
	private TextView tv_flow;
	
	@ViewInject(R.id.tv_notice)
	private TextView tv_notice;
	
	@ViewInject(R.id.tv_suggest)
	private TextView tv_suggest;
	
	@ViewInject(R.id.tv_about)
	private TextView tv_about;

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setView();
	}

	private void setView() {
		user_detail_relat.setOnClickListener(this);
		title_right.setOnClickListener(this);
		tv_my_walet.setOnClickListener(this);
		tv_my_discount.setOnClickListener(this);
		tv_my_piao.setOnClickListener(this);
		tv_flow.setOnClickListener(this);
		tv_notice.setOnClickListener(this);
		tv_suggest.setOnClickListener(this);
		tv_about.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.user_detail_relat:
			startActivity(new Intent(getActivity(),MyDetailActivity.class));
			break;
		case R.id.title_right:
			break;
		case R.id.tv_my_walet:
			startActivity(new Intent(getActivity(),MyWalletActivity.class));
			break;
		case R.id.tv_my_discount:
			break;
		case R.id.tv_my_piao:
			break;
		case R.id.tv_flow:
			Intent it = new Intent();
			it.setClass(getActivity(), CarRentalActivity.class);
			it.putExtra("type", "2");
			getActivity().startActivity(it);
			break;
		case R.id.tv_notice:
			Intent it1 = new Intent();
			it1.setClass(getActivity(), CarRentalActivity.class);
			it1.putExtra("type", "1");
			getActivity().startActivity(it1);
			break;
		case R.id.tv_suggest:
			break;
		case R.id.tv_about:
			Intent it2 = new Intent();
			it2.setClass(getActivity(), CarRentalActivity.class);
			it2.putExtra("type", "3");
			getActivity().startActivity(it2);
			break;

		default:
			break;
		}
	}



}
