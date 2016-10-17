package com.xieyu.ecar.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.xieyu.ecar.R;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.StringUtil;

public class MyBillDetailActivity extends BackableTitleBarActivity
{

	@V
	private TextView tv_detailstate, tv_money, tv_orderssn, tv_money_pay, tv_pay_time, tv_paymentmethod;

	private String detailstate, createtime, money, moneytype, orderssn, paymentmethod;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_bill_list_detail);
		getTitleBar().setTitle(R.string.mingxidetail);
		showView();
	}

	private void showView()
	{
		Injector.getInstance().inject(this);
		detailstate = getIntent().getStringExtra("detailstate");
		createtime = getIntent().getStringExtra("createtime");
		money = getIntent().getStringExtra("money");
		orderssn = getIntent().getStringExtra("orderssn");
		paymentmethod = getIntent().getStringExtra("paymentmethod");
		tv_detailstate.setText(StringUtil.isNull(detailstate));
		tv_money.setText(StringUtil.isNull(money));
		tv_orderssn.setText("流水号：" + StringUtil.isNull(orderssn));
		tv_money_pay.setText("支付金额：" + StringUtil.isNull(money));
		tv_pay_time.setText("支付时间：" + StringUtil.isNull(createtime));
		tv_paymentmethod.setText("支付方式：" + StringUtil.isNull(paymentmethod));
	}

}
