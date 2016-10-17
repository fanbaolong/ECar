package com.xieyu.ecar.adapter;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.News;
import com.xieyu.ecar.util.AlertDialog;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.StringUtil;
import com.xieyu.ecar.util.AlertDialog.OnClickOKListener;

import de.greenrobot.event.EventBus;

/**
 * @author fbl
 * 
 *         消息列表适配器
 *
 */
public class NewsAdapter extends AbsViewHolderAdapter<News>
{
	private Activity context;

	public NewsAdapter(Activity context, int layoutRes)
	{
		super(context, layoutRes);
		this.context = context;
	}

	@Override
	protected void bindData(int pos, final News itemData)
	{

		TextView tv_news_time = getViewFromHolder(R.id.tv_news_time);
		TextView tv_news_title = getViewFromHolder(R.id.tv_news_title);
		TextView tv_news_content = getViewFromHolder(R.id.tv_news_content);
		TextView tv_news_status = getViewFromHolder(R.id.tv_news_status);
		ImageView tv_news_delete = getViewFromHolder(R.id.tv_news_delete);

		tv_news_time.setText(StringUtil.isNull(itemData.getUpdateTime()));
		tv_news_title.setText(StringUtil.isNull(itemData.getTitle()));
		tv_news_content.setText(StringUtil.isNull(itemData.getContent()));
		tv_news_status.setText(StringUtil.isNull(itemData.getMessageState()));

		if (itemData.getMessageState().equals("已读"))
		{
			tv_news_status.setTextColor(mContext.getResources().getColor(R.color.black));
		} else if (itemData.getMessageState().equals("未读"))
		{
			tv_news_status.setTextColor(mContext.getResources().getColor(R.color.holo_title));
		}

		tv_news_delete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				AlertDialog dialog = new AlertDialog(context, R.style.add_dialog, "您确定要删除吗？");
				dialog.show();
				dialog.setOnClickOKListener(new OnClickOKListener()
				{
					@Override
					public void getOK()
					{
						deleteNew(itemData.getId() + "");
					}
				});
			}
		});

	}

	/**
	 * 删除消息
	 * 
	 * @param string
	 */
	private void deleteNew(String string)
	{

		RequestParams params = new RequestParams(BaseConstants.deleteMes);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(mContext, BaseConstants.prefre.SessionId));
		params.addBodyParameter("id", string + "");

		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if ("OK".equals(jsonObject.getString("resultType")))
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

}
