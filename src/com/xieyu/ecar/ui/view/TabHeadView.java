package com.xieyu.ecar.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xieyu.ecar.R;

/**
 * Created by lingcheng on 2014/7/23.
 */
public class TabHeadView extends RelativeLayout
{
	
	private Context context;

	public RelativeLayout getTitlebg()
	{
		return titlebg;
	}

	public void setTitlebg(RelativeLayout titlebg)
	{
		this.titlebg = titlebg;
	}

	private RelativeLayout titlebg;

	public TabHeadView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}

	public TabHeadView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}

	public void setRightInViliable()
	{
		mRightButton.setVisibility(View.INVISIBLE);
	}

	private ViewGroup mViewContainer;
	private ImageButton mLeftButton;
	private TextView titlebar_left_title;
	private TextView mRightButton;

	public TextView getTitle()
	{
		mTitle.setVisibility(View.VISIBLE);
		return mTitle;
	}

	private TextView mTitle;

	public void init()
	{
		mViewContainer = (ViewGroup) View.inflate(getContext(), R.layout.table_head_titlebar, this);
		mLeftButton = (ImageButton) mViewContainer.findViewById(R.id.titlebar_left_btn);
		titlebar_left_title = (TextView) mViewContainer.findViewById(R.id.titlebar_left_title);
		mRightButton = (TextView) mViewContainer.findViewById(R.id.titlebar_right_btn);
		mTitle = (TextView) mViewContainer.findViewById(R.id.titlebar_title);
		titlebg = (RelativeLayout) mViewContainer.findViewById(R.id.titlebar_content);
	}

	public void setLeftOnClick(OnClickListener click)
	{
		mLeftButton.setOnClickListener(click);
	}

	public void setTitle(String title)
	{
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(title);
	}

	public void setRightOnClick(OnClickListener click)
	{
		mRightButton.setOnClickListener(click);
	}

	public ImageButton getLeftButton()
	{
		mLeftButton.setVisibility(View.VISIBLE);
		return mLeftButton;
	}

	public TextView getRightButton()
	{
		mRightButton.setVisibility(View.VISIBLE);
		return mRightButton;
	}
	
	public void setLeftTitle(String leftTitle){
		titlebar_left_title.setText(leftTitle);
	}
}
