package com.xieyu.ecar.ui.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.MainActivity;
import com.xieyu.ecar.ui.view.MyViewPager;
import com.xieyu.ecar.ui.view.PagerSlidingTabStrip;
import com.xieyu.ecar.ui.view.TabHeadView;
import com.xieyu.ecar.util.AlertDialog;
import com.xieyu.ecar.util.AlertDialog.OnClickOKListener;
import com.xieyu.ecar.util.PreferenceUtil;

import de.greenrobot.event.EventBus;

public class MainFragment extends SuperFragment
{

	@V
	private PagerSlidingTabStrip tabs;
	@V
	private MyViewPager pager;
	@V
	private TabHeadView headView_main;
	private TextView tv_newsnum;

	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private MyPagerAdapter adapter;
	private int fragment = 0;
	private MainMapFragment mainmap;
	private MainOrderFragment mainorder;
	private MainNewsFragment mainnews;
	private int j = 0;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		Injector.getInstance().inject(mActivity, this, view);
		EventBus.getDefault().register(this);

		setView();
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (App.ispager)
		{
			pager.setCurrentItem(App.select);
			App.ispager = false;
		}
	}

	private void setView()
	{
		headView_main.getTitle().setText(getResources().getText(R.string.map));// 这里还是要设下，否则初次进入的话title不会显示地图
		headView_main.getLeftButton().setImageResource(R.drawable.icon_menu);
		headView_main.getLeftButton().setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				MainActivity main = (MainActivity) getActivity();
				if (main.drawer_layout.isDrawerOpen(main.mMenu_layout))
				{
					main.drawer_layout.closeDrawer(main.mMenu_layout);
				} else
				{
					main.drawer_layout.openDrawer(main.mMenu_layout);
				}
			}
		});
		if (mainmap == null)
		{
			mainmap = new MainMapFragment();
			fragments.add(mainmap);
		}
		if (mainorder == null)
		{
			mainorder = new MainOrderFragment();
			fragments.add(mainorder);
		}
		if (mainnews == null)
		{
			mainnews = new MainNewsFragment();
			fragments.add(mainnews);
		}
		fragment = 0;
		tabs.setAllCaps(true);
		tabs.setTabWidth(App.getInstance().getScreenWidth() / 3);
		tabs.setDividerColorResource(android.R.color.transparent);
		tabs.setIndicatorColor(Color.TRANSPARENT);
		tabs.setTabPaddingLeftRight(0);
		tabs.setDividerPadding(0);
		pager.setOffscreenPageLimit(3);// 预加载fragment数量
		pager.setScanScroll(false);
		adapter = new MyPagerAdapter(getFragmentManager(), tabs);
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);

	}

	public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider
	{

		private final String[] TITLES;
		private final TabView[] VIEWS;
		private final int[] imgIds = new int[]
		{ R.drawable.main_map_normal, R.drawable.main_order_normal, R.drawable.main_news_normal };
		private final int[] imgPressedIds = new int[]
		{ R.drawable.main_map_pressed, R.drawable.main_order_pressed, R.drawable.main_news_pressed };
		private final PagerSlidingTabStrip tab;

		public MyPagerAdapter(FragmentManager fm, PagerSlidingTabStrip tab)
		{
			super(fm);
			this.tab = tab;
			TITLES = getResources().getStringArray(R.array.bottom_navigation);
			VIEWS = new TabView[TITLES.length];
			tab.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
			{
				@Override
				public void onPageScrolled(int i, float v, int i2)
				{
				}

				@Override
				public void onPageSelected(int i)
				{
					if (i != j && j == 2)
					{
						PreferenceUtil.putInt(getActivity(), BaseConstants.prefre.mBadge, 0);
						EventBus.getDefault().post(EventMessage.badgeAdd);
					} 
					if (i == 0 && j != 0 ) {
						EventBus.getDefault().post(EventMessage.updateMap);
					}

					if (i == 2)
					{
						j = 2;
						headView_main.getRightButton().setText("清空");
						headView_main.getRightButton().setOnClickListener(new OnClickListener()
						{

							@Override
							public void onClick(View arg0)
							{
								AlertDialog dialog = new AlertDialog(getActivity(), R.style.add_dialog, "");

								dialog.setContent("您确定要删除所有消息吗？");
								dialog.setOnClickOKListener(new OnClickOKListener()
								{
									@Override
									public void getOK()
									{
										EventBus.getDefault().post(EventMessage.cleanNews);
									}
								});

								dialog.show();
							}
						});

						PreferenceUtil.putInt(getActivity(), BaseConstants.prefre.mBadge, 0);
						EventBus.getDefault().post(EventMessage.badgeAdd);
					} else
					{
						j = i;
						
						headView_main.getRightButton().setText(null);
						headView_main.getRightButton().setOnClickListener(null);
					}
					setSelectView(i);
					fragment = i;
					headView_main.getTitle().setText(TITLES[i]);
				}

				@Override
				public void onPageScrollStateChanged(int i)
				{
				}
			});
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return TITLES[position];
		}

		@Override
		public int getCount()
		{
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position)
		{
			return fragments.get(position);
		}

		@Override
		public View getPageView(int i)
		{
			if (VIEWS[i] == null)
				VIEWS[i] = getTabItemView(i);
			return VIEWS[i];
		}

		private void setSelectView(int index)
		{
			for (int i = 0; i < VIEWS.length; i++)
			{
				if (i == index)
					VIEWS[i].setSelect(imgPressedIds[i]);
				else
					VIEWS[i].setUnSelect(imgIds[i]);
			}
		}

		/**
		 * 给Tab按钮设置图标和文字
		 */
		private TabView getTabItemView(int index)
		{
			return new TabView(mActivity, index);
		}

		private class TabView extends LinearLayout
		{

			private final ImageView imageView;
			private final TextView textView, tv_news;
			private final View layout;

			public TabView(Context context, int index)
			{
				super(context);
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				layout = layoutInflater.inflate(R.layout.layout_sliding_tab_item, null);
				imageView = (ImageView) layout.findViewById(R.id.imageView);
				textView = (TextView) layout.findViewById(R.id.txt_cn);
				tv_news = (TextView) layout.findViewById(R.id.tv_news);
				if (index == 0)
				{
					setSelect(imgPressedIds[index]);
				} else
				{
					setUnSelect(imgIds[index]);
				}
				if (index == 2)
				{
					int badge = PreferenceUtil.getInt(getActivity(), BaseConstants.prefre.mBadge);
					if (badge == 0)
					{
						tv_news.setVisibility(View.GONE);
					} else
					{
						tv_news.setVisibility(View.VISIBLE);
						tv_news.setText(badge + "");
					}
					tv_newsnum = tv_news;
				} else
				{
					tv_news.setVisibility(View.GONE);
				}
				TextView textView = (TextView) layout.findViewById(R.id.txt_cn);
				textView.setText(TITLES[index]);
				textView.setTextSize(getResources().getDimension(R.dimen.dimen_15dp) / getResources().getDisplayMetrics().density);
				addView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			}

			private void setSelect(int id)
			{
				imageView.setImageResource(id);
				textView.setTextColor(getResources().getColor(R.color.bottom_text));
				// layout.setBackgroundColor(getResources().getColor(R.color.base_title_alpth));
			}

			private void setUnSelect(int imgId)
			{
				imageView.setImageResource(imgId);
				textView.setTextColor(getResources().getColor(R.color.white));
				// layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}

		}
	}

	/**
	 * 改变消息底部的小数点数字的 在代码后直接用eventbus 发 “badge”消息即可
	 * 
	 * @param str
	 */
	public void onEvent(EventMessage message)
	{
		switch (message)
		{
		case badgeAdd:

			int i = PreferenceUtil.getInt(getActivity(), BaseConstants.prefre.mBadge);
			if (i <= 0)
			{
				tv_newsnum.setVisibility(View.GONE);
			} else
			{
				tv_newsnum.setVisibility(View.VISIBLE);
				tv_newsnum.setText(i + "");
			}

			break;

		default:
			break;
		}

	}

}
