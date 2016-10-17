package com.xieyu.ecar.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.adapter.NewsAdapter;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.News;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.BaseActivity;
import com.xieyu.ecar.ui.NewsDetailActivity;
import com.xieyu.ecar.util.PreferenceUtil;

import de.greenrobot.event.EventBus;

/**
 * 首页消息
 * 
 * @author fbl
 *
 */
@SuppressWarnings("rawtypes")
public class MainNewsFragment extends SuperFragment implements PullToRefreshBase.OnRefreshListener2
{
	@V
	private TextView tv_warning, tv_service;
	@V
	private PullToRefreshListView lv_warning, lv_service;
	@V
	private View warn_empty, service_empty;

	public int select = 1;// 1:警告消息；2：服务消息
	private NewsAdapter newsServiceAdapter; // 用作服务小时适配
	private NewsAdapter newsWarnAdapter;// 用作错误消息适配
	private List<News> mNewLists = new ArrayList<News>();

	// private String mMsgType = "Alarm"; // Alarm警告，Info消息

	private int warnPageNum = 1;
	private int serverPageNum = 1;
	private boolean isFirst = true;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_mainnews, container, false);
		Injector.getInstance().inject(getActivity(), this, view);
		EventBus.getDefault().register(this);
		setView();
		return view;
	}

	@SuppressWarnings("unchecked")
	private void setView()
	{
		tv_warning.setOnClickListener(this);
		tv_service.setOnClickListener(this);

		if (App.isPush)
		{
			select = App.newSelect;
			App.isPush = false;
		}

		setTextSelect(select);
		getNewsList(1, true);

		lv_warning.setMode(PullToRefreshBase.Mode.BOTH);
		lv_warning.setOnRefreshListener(this);
		lv_warning.setShowIndicator(false);
		newsWarnAdapter = new NewsAdapter(getActivity(), R.layout.adapter_news);
		lv_warning.setEmptyView(warn_empty);
		lv_warning.setAdapter(newsWarnAdapter);

		lv_service.setMode(PullToRefreshBase.Mode.BOTH);
		lv_service.setOnRefreshListener(this);
		lv_service.setShowIndicator(false);
		newsServiceAdapter = new NewsAdapter(getActivity(), R.layout.adapter_news);
		lv_service.setEmptyView(service_empty);
		lv_service.setAdapter(newsServiceAdapter);

		lv_warning.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
			{
				App.showLog("newsWarnAdapter==" + (pos - 1));
				int newId = newsWarnAdapter.getData().get(pos - 1).getId();
				if ("未读".equals(newsWarnAdapter.getData().get(pos - 1).getMessageState()))
				{
					setMesRead(newId);
				}

				startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("news", newsWarnAdapter.getData().get(pos - 1)));

			}
		});

		lv_service.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
			{
				App.showLog("newsServiceAdapter==" + (pos - 1));
				int newId = newsServiceAdapter.getData().get(pos - 1).getId();
				if ("未读".equals(newsServiceAdapter.getData().get(pos - 1).getMessageState()))
				{
					setMesRead(newId);
				}

				startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("news", newsServiceAdapter.getData().get(pos - 1)));
			}

		});
	}

	@Override
	public void onStart()
	{
		super.onStart();

		// if (App.isPush)
		// {
		// select = App.newSelect;
		// App.isPush = false;
		// }
		//
		// setTextSelect(select);
		// getNewsList(1, true);

	}

	/**
	 * 给两个textView设置背景
	 * 
	 * @param i
	 */
	private void setTextSelect(int i)
	{
		if (i == 1)
		{
			tv_warning.setTextColor(getResources().getColor(R.color.white));
			tv_warning.setBackgroundResource(R.drawable.order_left_top_pressed);
			tv_service.setTextColor(getResources().getColor(R.color.holo_title));
			tv_service.setBackgroundResource(R.drawable.order_right_top_normal);
			lv_service.setVisibility(View.GONE);
			lv_warning.setVisibility(View.VISIBLE);
		} else
		{
			tv_warning.setTextColor(getResources().getColor(R.color.holo_title));
			tv_warning.setBackgroundResource(R.drawable.order_left_top_normal);
			tv_service.setTextColor(getResources().getColor(R.color.white));
			tv_service.setBackgroundResource(R.drawable.order_right_top_pressed);
			lv_service.setVisibility(View.VISIBLE);
			lv_warning.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.tv_warning:
			select = 1;
			setTextSelect(select);
			if (isFirst)
			{ // 只在第一次进入如果是select=2的时候点击第1个按钮让他刷新
				isFirst = false;
				getNewsList(1, true);
			}

			break;
		case R.id.tv_service:
			select = 2;
			setTextSelect(select);
			if (isFirst)
			{ // 只在第一次进入的时候select=1的时候点击第2个按钮让他刷新
				isFirst = false;
				getNewsList(1, true);
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView)
	{
		if (select == 1)
		{
			warnPageNum = 1;
		} else
		{
			serverPageNum = 1;
		}

		getNewsList(1, true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView)
	{
		if (select == 1)
		{
			++warnPageNum;
			getNewsList(warnPageNum, false);
		} else
		{
			++serverPageNum;
			getNewsList(serverPageNum, false);
		}

	}

	/**
	 * @param page
	 * @param isRefresh
	 * 
	 *            获取我的消息列表
	 */
	private void getNewsList(int page, final boolean isRefresh)
	{
		((BaseActivity) getActivity()).showLoadingDialog("");

		RequestParams params = new RequestParams(BaseConstants.getMessages);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(getActivity(), BaseConstants.prefre.SessionId));
		params.addBodyParameter("pageNumber", page + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("messageType", select == 1 ? "Alarm" : "Info");//

		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				App.showLog("result==" + result);

				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if ("OK".equals(jsonObject.getString("resultType")))
					{
						Gson gson = new Gson();
						mNewLists = gson.fromJson(jsonObject.getJSONArray("objectResult").toString(), new TypeToken<List<News>>()
						{
						}.getType());

						if (select == 1)
						{
							if (isRefresh)
							{
								newsWarnAdapter.update(mNewLists);
							} else
							{
								if (mNewLists.size() <= 0)
								{
									App.showShortToast("没有数据了");
								} else
								{
									newsWarnAdapter.append(mNewLists);
								}
							}

						} else
						{

							if (isRefresh)
							{
								newsServiceAdapter.update(mNewLists);
							} else
							{
								if (mNewLists.size() <= 0)
								{
									App.showShortToast("没有数据了");
								} else
								{
									newsServiceAdapter.append(mNewLists);
								}
							}

						}

					}else if("SESSIONOUT".equals(jsonObject.getString("resultType"))){
						
						PreferenceUtil.putString(getActivity(), BaseConstants.prefre.SessionId, "");
						EventBus.getDefault().post(EventMessage.loginOut);
						
					} else
					{
						App.showShortToast(jsonObject.getString("resultMes"));
					}

				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{
			}

			@Override
			public void onCancelled(CancelledException cex)
			{
			}

			@Override
			public void onFinished()
			{
				((BaseActivity) getActivity()).dismissLoadingDialog();
				lv_service.onRefreshComplete();
				lv_warning.onRefreshComplete();
			}
		});

	}

	/**
	 * 请求接口，告诉服务器你这条信息读了
	 * 
	 * @param id
	 */
	private void setMesRead(int id)
	{
		RequestParams params = new RequestParams(BaseConstants.mesRead);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(getActivity(), BaseConstants.prefre.SessionId));
		params.addBodyParameter("id", id + "");

		x.http().post(params, new CommonCallback<String>()
		{
			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						EventBus.getDefault().post(EventMessage.refreshNews);
					}

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{
			}

			@Override
			public void onCancelled(CancelledException cex)
			{
			}

			@Override
			public void onFinished()
			{
			}
		});

	}

	/**
	 * 删除全部消息
	 */
	private void cleanNews()
	{
		RequestParams params = new RequestParams(BaseConstants.deleteMesAll);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(getActivity(), BaseConstants.prefre.SessionId));
		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						EventBus.getDefault().post(EventMessage.refreshNews);
					} else
					{
						App.showShortToast(jsonObject.getString("resultMes"));
					}

				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{
			}

			@Override
			public void onCancelled(CancelledException cex)
			{
			}

			@Override
			public void onFinished()
			{
			}
		});

	}

	public void onEvent(EventMessage message)
	{

		switch (message)
		{

		case refreshNews:
			getNewsList(1, true);
			break;

		case cleanNews:
			cleanNews();
			break;

		default:
			break;
		}

	}

}
