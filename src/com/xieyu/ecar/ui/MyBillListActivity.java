package com.xieyu.ecar.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.xieyu.ecar.adapter.BillAdapter;
import com.xieyu.ecar.bean.RechareOrder;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.PreferenceUtil;

/**
 * @author fanbaolong
 *
 *         我的账单列表
 */
@SuppressWarnings("rawtypes")
public class MyBillListActivity extends BackableTitleBarActivity implements PullToRefreshBase.OnRefreshListener2
{
	@V
	private RelativeLayout empty_relat;
	@V
	private PullToRefreshListView lv_bill;

	private int pageNum = 1;
	private BillAdapter mAdapter;
	private List<RechareOrder> mOrderList = new ArrayList<RechareOrder>();
	private List<RechareOrder> orders = new ArrayList<RechareOrder>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_bill_list);
		getTitleBar().setTitle(R.string.mingxi);

		Injector.getInstance().inject(this);

		lv_bill.setMode(PullToRefreshBase.Mode.BOTH);
		lv_bill.setOnRefreshListener(this);
		lv_bill.setShowIndicator(false);
		mAdapter = new BillAdapter(this, R.layout.item_bill);
		lv_bill.setEmptyView(empty_relat);
		lv_bill.setAdapter(mAdapter);
		getDataList(1, true);
		lv_bill.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				RechareOrder rechareOrder = orders.get(arg2 - 1);
				Intent intent = new Intent(MyBillListActivity.this, MyBillDetailActivity.class);
				intent.putExtra("createtime", rechareOrder.getCreateTime());
				intent.putExtra("detailstate", rechareOrder.getDetailState());
				intent.putExtra("money", rechareOrder.getMoney());
				intent.putExtra("moneytype", rechareOrder.getMoneyType());
				intent.putExtra("orderssn", rechareOrder.getOrdersSn());
				intent.putExtra("paymentmethod", RechareOrder.PaymentMethod(rechareOrder.getPaymentMethod()));

				startActivity(intent, true);
			}
		});

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView)
	{
		pageNum = 1;
		getDataList(pageNum, true);

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView)
	{
		++pageNum;
		getDataList(pageNum, false);

	}

	private void getDataList(int page, final boolean isRefresh)
	{
		RequestParams params = new RequestParams(BaseConstants.getAccountDetails);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("pageNumber", page + "");
		params.addBodyParameter("moneyType", "Recharge");// PilesType

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
						Gson gson = new Gson();

						mOrderList = gson.fromJson(jsonObject.getJSONArray("objectResult").toString(), new TypeToken<List<RechareOrder>>()
						{
						}.getType());
						if (isRefresh)
						{
							orders.clear();
							mAdapter.update(mOrderList);
						} else
						{
							if (mOrderList.size() <= 0)
							{
								App.showShortToast("没有数据了");
							} else
							{
								mAdapter.append(mOrderList);
							}

						}
						orders.addAll(mOrderList);
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
				lv_bill.onRefreshComplete();
			}
		});

	}

}
