package com.xieyu.ecar.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.xieyu.ecar.ui.BaseActivity;

/**
 * 父类fragment
 * 
 * @author wangfeng
 *
 */
public class SuperFragment extends Fragment implements OnClickListener {

	public BaseActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mActivity = (BaseActivity) getActivity();
	}

	@Override
	public void onClick(View v) {
	}

}
