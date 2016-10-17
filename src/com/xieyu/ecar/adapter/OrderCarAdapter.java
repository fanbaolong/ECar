package com.xieyu.ecar.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.OrderCar;
import com.xieyu.ecar.util.StringUtil;

public class OrderCarAdapter extends AbsViewHolderAdapter<OrderCar>
{

	public OrderCarAdapter(Context context, int layoutRes)
	{
		super(context, layoutRes);
	}

	@Override
	protected void bindData(int pos, OrderCar itemData)
	{
		TextView tv_name = getViewFromHolder(R.id.tv_name);
		TextView tv_isbook = getViewFromHolder(R.id.tv_isbook);
		TextView tv_address = getViewFromHolder(R.id.tv_address);
		TextView tv_starttime = getViewFromHolder(R.id.tv_starttime);
		TextView tv_endtime = getViewFromHolder(R.id.tv_endtime);
		TextView tv_type = getViewFromHolder(R.id.tv_type);
		TextView tv_booktime = getViewFromHolder(R.id.tv_booktime);
		TextView tv_booked = getViewFromHolder(R.id.tv_booked);
		ImageView img_url = getViewFromHolder(R.id.img_url);

		tv_name.setText(StringUtil.isNull(itemData.getNet_name()));
		if (itemData.isBooking())
		{
			tv_isbook.setText("已预约");
		} else
		{
			tv_isbook.setText("未预约");
		}
//		tv_address.setText("地址：" + StringUtil.isNull(itemData.net_address));
//		tv_starttime.setText("取车时间：" + StringUtil.isNull(itemData.start_time));
//		tv_endtime.setText("还车时间：" + StringUtil.isNull(itemData.end_time));
//		tv_type.setText("车子类型：" + StringUtil.isNull(itemData.car_type));
//		tv_booktime.setText(StringUtil.isNull(itemData.booking_time));
		tv_booked.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				App.showShortToast("已取消");
			}
		});
	}

}
