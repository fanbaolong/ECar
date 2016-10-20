package com.xieyu.ecar.http;

public interface HttpCallBack<ResultType> {
	
	void onSuccess(ResultType result, String tag);
	void onfail(ResultType result);

}
