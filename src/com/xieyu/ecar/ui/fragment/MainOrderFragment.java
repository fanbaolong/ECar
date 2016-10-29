package com.xieyu.ecar.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.adapter.NOrderPileAdapter;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.OrderPile;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.NOrderCarDetailActivity;
import com.xieyu.ecar.ui.view.TabHeadView;
import com.xieyu.ecar.util.PreferenceUtil;

import de.greenrobot.event.EventBus;

/**
 * 首页订单
 * 
 * @author wangfeng
 *
 */
@SuppressWarnings("rawtypes")
public class MainOrderFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2 {
	@V
	private PullToRefreshListView lv_ordercharge;
	@V
	private RelativeLayout empty_relat;
	@V
	private TabHeadView top_title;

	private List<OrderPile> mOrderPiles = new ArrayList<OrderPile>();
	private NOrderPileAdapter orderPileAdapter;
	private int pageNum = 1;

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mainorder, container, false);
		Injector.getInstance().inject(getActivity(), this, view);
		EventBus.getDefault().register(this);// 注册订阅者 MyFragment
		initTitle();
		setView();
		return view;

	}

	private void initTitle() {
		top_title.getTitle().setText("订单");
		top_title.getLeftButton().setVisibility(View.GONE);
	}

	private void setView() {
		lv_ordercharge.setMode(PullToRefreshBase.Mode.BOTH);
		lv_ordercharge.setOnRefreshListener(this);
		lv_ordercharge.setShowIndicator(false);

		orderPileAdapter = new NOrderPileAdapter (getActivity(), R.layout.adapter_order_n);
		lv_ordercharge.setEmptyView(empty_relat);
		lv_ordercharge.setAdapter(orderPileAdapter);

		getOrderList(1, true);

		lv_ordercharge.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3)
			{
				Intent in = new Intent(getActivity(), NOrderCarDetailActivity.class);
				in.putExtra("orderCharge", orderPileAdapter.getData().get(pos - 1));
				mActivity.startActivity(in, true);
			}
		});

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		pageNum = 1;
		getOrderList(pageNum, true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		pageNum++;
		getOrderList(pageNum, false);
	}
	/**
	 * @param page
	 * @param isRefresh
	 * 
	 *            获取我的订单列表
	 */
	private void getOrderList(int page, final boolean isRefresh) {

		RequestParams params = new RequestParams(BaseConstants.orderList);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(getActivity(), BaseConstants.prefre.SessionId));
		params.addBodyParameter("pageNumber", page + "");
		params.addBodyParameter("orderType", "CarType");// PilesType CarType

		x.http().post(params, new CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				App.showLog("result==" + result);

				try {
					JSONObject jsonObject = new JSONObject(result);
					if ("OK".equals(jsonObject.getString("resultType")))
					{
						Gson gson = new Gson();
						mOrderPiles = gson.fromJson(jsonObject.getJSONArray("objectResult").toString(), new TypeToken<List<OrderPile>>()
						{
						}.getType());

						if (isRefresh)
						{
							orderPileAdapter.update(mOrderPiles);
						} else
						{
							if (mOrderPiles.size() <= 0)
							{
								App.showShortToast("没有数据了");
							} else
							{
								orderPileAdapter.append(mOrderPiles);
							}
						}

					} else if ("SESSIONOUT".equals(jsonObject.getString("resultType")))
					{
						App.showShortToast("登录过期，请重新登录");
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
				lv_ordercharge.onRefreshComplete();
			}
		});

	}

	public void onEvent(EventMessage message) {
		switch (message) {
		case refreshOrder:
			getOrderList(1, true);
			break;

		default:
			break;
		}
	}

}
