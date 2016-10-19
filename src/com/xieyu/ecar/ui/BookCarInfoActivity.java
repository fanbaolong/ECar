package com.xieyu.ecar.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.xieyu.ecar.R;
/**
 * 预约车辆页面
 * @author think
 *
 */
public class BookCarInfoActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookcarinfo);
		setColor();
	}
}
