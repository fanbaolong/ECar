package com.xieyu.ecar.ui;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.MyCountTimer;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.StringUtil;

/**
 * @author fanbaolong
 *
 *         找回密码
 */
public class ForgetPwdActivity extends BackableTitleBarActivity
{
	@V
	private TextView get_code_text;
	@V
	private EditText forget_name_edit, forget_pwd_edit, forget_pass_edit;
	@V
	private Button btn_forget_pwd;

	private String smsCode = "";

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_forget_pwd);
		getTitleBar().setTitle(R.string.title_get_pwd);

		setView();

	}

	private void setView()
	{
		Injector.getInstance().inject(this);
		get_code_text.setOnClickListener(this);
		btn_forget_pwd.setOnClickListener(this);

		get_code_text.setClickable(false);
		forget_name_edit.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (!hasFocus)
				{
				} else
				{
					if (invalidateText(forget_name_edit))
					{
						get_code_text.setTextColor(getResources().getColor(R.color.gray));
						get_code_text.setClickable(false);
					} else
					{
						get_code_text.setTextColor(getResources().getColor(R.color.holo_title));
						get_code_text.setClickable(true);
					}
				}
			}
		});

		forget_name_edit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{
				if (!invalidateText(forget_name_edit))
				{
					get_code_text.setTextColor(getResources().getColor(R.color.holo_title));
					get_code_text.setClickable(true);
				} else
				{
					get_code_text.setTextColor(getResources().getColor(R.color.gray));
					get_code_text.setClickable(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{
			}

			@Override
			public void afterTextChanged(Editable arg0)
			{
			}
		});

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.get_code_text:
			getSmsCode();
			break;

		case R.id.btn_forget_pwd:
			changePwd();
			break;
		default:
			break;
		}

	}

	private void getSmsCode()
	{
		if (invalidateText(forget_name_edit, R.string.username_hint))
		{
			return;
		}
		if (!StringUtil.isPhoneNo(getText(forget_name_edit)))
		{
			App.showShortToast(R.string.please_input_correct_phone);
			return;
		}

		MyCountTimer timeCount = new MyCountTimer(get_code_text, getResources().getColor(R.color.holo_title), getResources().getColor(R.color.gray));// 传入了文字颜色值
		timeCount.start();

		RequestParams params = new RequestParams(BaseConstants.sendVerification);
		params.addBodyParameter("mobile", getText(forget_name_edit));
		params.addBodyParameter("type", "1");

		x.http().post(params, new Callback.CommonCallback<String>()
		{
			@Override
			public void onSuccess(String result)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						smsCode = jsonObject.getString("objectResult");
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
				App.showShortToast(ex.getMessage());
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

	private void changePwd()
	{

		if (invalidateText(forget_name_edit, R.string.username_hint) || invalidateText(forget_pass_edit, R.string.input_new_pwd) || invalidateText(forget_pwd_edit, R.string.please_input_ver_code))
		{
			return;
		}
		if (!smsCode.equals(getText(forget_pwd_edit)))
		{
			App.showShortToast(R.string.err_ver_code);
			return;
		}
		showLoadingDialog("");
		RequestParams params = new RequestParams(BaseConstants.findPsd);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("mobile", getText(forget_name_edit));
		params.addBodyParameter("newPsd", getText(forget_pass_edit));

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
						App.showShortToast("密码重置成功,请重新登录");
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
