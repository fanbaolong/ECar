package com.xieyu.ecar.ui.fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.ui.BaseActivity;
import com.xieyu.ecar.util.StringUtil;

/**
 * 父类fragment
 * 
 * @author wangfeng
 *
 */
public class BaseFragment extends Fragment implements OnClickListener {

	public BaseActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mActivity = (BaseActivity) getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}

	@Override
	public void onClick(View v) {
	}
	
	public void requestPost(final String tag, RequestParams params){
		requestPost(false, "", tag, params);
	}
	
	public void requestPost(final boolean isDialog, final String tag, RequestParams params){
		requestPost(isDialog, "", tag, params);
	}
	
	public void requestPost(final boolean isDialog, String dialogContent, final String tag, RequestParams params){
		if (isDialog) {
			mActivity.showLoadingDialog(StringUtil.isNull(dialogContent));
		}
		x.http().post(params, new CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					String resultType = jsonObject.getString("resultType");
					String resultMes = jsonObject.getString("resultMes");
					String objectResult = jsonObject.getString("objectResult");
					if (resultType.equals("OK")) {
						responseSuccess(objectResult, resultMes, tag);
					}else {
						responseFail(resultMes, tag);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
			}

			@Override
			public void onCancelled(CancelledException cex) {
			}

			@Override
			public void onFinished() {
				if (isDialog) {
					mActivity.dismissLoadingDialog();
				}
			}
		});
	}

	public void responseSuccess(String result, String msg, String tag){
		
	}
	
	public void responseFail(String msg, String tag){
		if (!tag.equals(BaseConstants.getCurrentCar)) {
			App.showShortToast(msg);
		}
	}

}
