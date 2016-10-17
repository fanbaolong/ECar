package com.xieyu.ecar.ui;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import android.content.Intent;
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
import com.xieyu.ecar.util.StringUtil;

/**
 * @author fanbaolong
 *
 *         注册
 */
public class RegisterActivity extends BackableTitleBarActivity
{

	@V
	private EditText register_name_edit, register_user_name_edit, register_pass_edit, register_code_edit;
	@V
	private TextView get_register_code_text, text2;
	@V
	private Button btn_register;

	private String mSmsCode = "";

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_register);
		getTitleBar().setTitle(R.string.register);
		Injector.getInstance().inject(this);

		init();
		initView();

	}

	private void init()
	{
		btn_register.setOnClickListener(this);
		get_register_code_text.setOnClickListener(this);
		text2.setOnClickListener(this);

	}

	private void initView()
	{
		get_register_code_text.setClickable(false);
		register_name_edit.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (!hasFocus)
				{
				} else
				{
					if (invalidateText(register_name_edit))
					{
						get_register_code_text.setTextColor(getResources().getColor(R.color.gray));
						get_register_code_text.setClickable(false);
					} else
					{
						get_register_code_text.setTextColor(getResources().getColor(R.color.holo_title));
						get_register_code_text.setClickable(true);
					}
				}
			}
		});

		register_name_edit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{
				if (!invalidateText(register_name_edit))
				{
					get_register_code_text.setTextColor(getResources().getColor(R.color.holo_title));
					get_register_code_text.setClickable(true);
				} else
				{
					get_register_code_text.setTextColor(getResources().getColor(R.color.gray));
					get_register_code_text.setClickable(false);
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
		case R.id.btn_register:
			judgeRegister();
			break;

		case R.id.get_register_code_text:
			getCodeSms();
			break;

		case R.id.text2:
			startActivity(new Intent(this, RegisterRuleActivity.class));
			break;

		default:
			break;
		}
	}

	private void getCodeSms()
	{
		if (invalidateText(register_name_edit, R.string.username_hint))
		{
			return;
		}
		if (!StringUtil.isPhoneNo(getText(register_name_edit)))
		{
			App.showShortToast(R.string.please_input_correct_phone);
			return;
		}

		MyCountTimer timeCount = new MyCountTimer(get_register_code_text, getResources().getColor(R.color.holo_title), getResources().getColor(R.color.gray));// 传入了文字颜色值
		timeCount.start();

		RequestParams params = new RequestParams(BaseConstants.sendVerification);
		params.addBodyParameter("mobile", getText(register_name_edit));
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
						mSmsCode = jsonObject.getString("objectResult");
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

	/**
	 * 判断注册条件
	 */
	private void judgeRegister()
	{

		if (invalidateText(register_name_edit, R.string.username_hint) || invalidateText(register_user_name_edit, R.string.please_input_username) || invalidateText(register_pass_edit, R.string.please_input_userpass) || invalidateText(register_code_edit, R.string.please_input_ver_code))
		{
			return;
		}
		if (getText(register_pass_edit).length() < 6 || getText(register_pass_edit).length() > 20)
		{
			App.showShortToast(R.string.pwd_is_no);
			return;
		}

		if (!mSmsCode.equals(getText(register_code_edit)))
		{
			App.showShortToast(R.string.err_ver_code);
			return;
		}

		RequestParams params = new RequestParams(BaseConstants.Register);
		params.addBodyParameter("userName", getText(register_user_name_edit));
		params.addBodyParameter("passWord", getText(register_pass_edit));
		params.addBodyParameter("mobile", getText(register_name_edit));

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
						App.showShortToast("注册成功，请登录");
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
}
