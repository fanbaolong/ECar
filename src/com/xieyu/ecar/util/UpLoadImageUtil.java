package com.xieyu.ecar.util;

import java.io.File;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import com.xieyu.ecar.BaseConstants;

public class UpLoadImageUtil
{

	public upLoadStatesListener isUpload;

	public interface upLoadStatesListener
	{
		public void isSuccess(String ss);

		public void isFailure();
	}

	public void setUploadState(upLoadStatesListener isUpload)
	{
		this.isUpload = isUpload;
	}

	public void uploadImage(String sessionid, String imageUrl)
	{
		RequestParams params = new RequestParams(BaseConstants.upLoadFile);
		params.addBodyParameter("sessionId", sessionid);
		params.addBodyParameter("Filedata ", new File(imageUrl));

		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				isUpload.isSuccess(result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{
				isUpload.isFailure();
			}

			@Override
			public void onCancelled(CancelledException cex)
			{
				isUpload.isFailure();
			}

			@Override
			public void onFinished()
			{

			}
		});

	}
}
