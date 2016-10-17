package com.xieyu.ecar.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.RechareOrder;

/**
 * 充值详情
 * 
 * @author fanbaolong
 *
 */
public class BillAdapter extends AbsViewHolderAdapter<RechareOrder>
{

	public BillAdapter(Context context, int layoutRes)
	{
		super(context, layoutRes);
	}

	@Override
	protected void bindData(int pos, RechareOrder itemData)
	{
		ImageView imageView = getViewFromHolder(R.id.item_bill_image);
		TextView mMoneyText = getViewFromHolder(R.id.item_bill_money);
		TextView mTimeText = getViewFromHolder(R.id.item_bill_time);
		TextView mStatusText = getViewFromHolder(R.id.item_bill_status);

		mMoneyText.setText(itemData.getMoney() + "元");
		mTimeText.setText(itemData.getCreateTime());
		mStatusText.setText(itemData.getDetailState());

	}

}
