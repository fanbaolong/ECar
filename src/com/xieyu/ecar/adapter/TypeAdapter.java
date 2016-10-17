package com.xieyu.ecar.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.xieyu.ecar.R;

public class TypeAdapter extends AbsViewHolderAdapter<String>
{

	public TypeAdapter(Context context, List<String> data, int layoutRes)
	{
		super(context, data, layoutRes);
	}

	@Override
	protected void bindData(int pos, String itemData)
	{
		TextView tv = getViewFromHolder(R.id.tv_type);
		tv.setText(itemData);
	}

}
