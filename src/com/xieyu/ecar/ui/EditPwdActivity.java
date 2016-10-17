package com.xieyu.ecar.ui;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.PreferenceUtil;

/**
 * 修改密码
 * 
 * @author fbl
 *
 */
public class EditPwdActivity extends BackableTitleBarActivity
{
	@V
	private EditText old_pwd_edit, new_pwd_edit, new_pwd_again_edit;
	@V
	private Button btn_change_pwd;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.fragment_editpwd);
		getTitleBar().setTitle(R.string.change_pwd);
		setView();
	}

	private void setView()
	{
		Injector.getInstance().inject(this);
		btn_change_pwd.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.btn_change_pwd:
			toChangePwd();
			break;

		default:
			break;
		}
	}

	private void toChangePwd()
	{
		if (invalidateText(old_pwd_edit, R.string.input_old_pwd) || invalidateText(new_pwd_edit, R.string.input_new_pwd) || invalidateText(new_pwd_again_edit, R.string.input_new_pwd_again))
		{
			return;
		}
		if (!getText(new_pwd_edit).equals(getText(new_pwd_again_edit)))
		{
			App.showShortToast("两次密码输入不一样");
			return;
		}

		showLoadingDialog("");
		RequestParams params = new RequestParams(BaseConstants.changePsd);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("oldPsd", getText(old_pwd_edit));
		params.addBodyParameter("newPsd", getText(new_pwd_edit));

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
						App.showShortToast("修改成功");
						finish();
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
				dismissLoadingDialog();
			}
		});

	}
}
