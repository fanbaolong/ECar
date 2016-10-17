package com.xieyu.ecar.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.adapter.AbsViewHolderAdapter;
import com.xieyu.ecar.bean.MoreUser;
import com.xieyu.ecar.ui.view.SimpleTitleBar;
import com.xieyu.ecar.util.NetworkUtil;
import com.xieyu.ecar.util.PreferenceUtil;

/**
 * @author fanbaolong
 *
 *         登录
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends SimpleTitleBarActivity
{

	@ViewInject(R.id.register_name_edit)
	private EditText mUserNameEdit;

	@ViewInject(R.id.register_name_del)
	private ImageView mNameDel;

	@ViewInject(R.id.txtPassword)
	private EditText mUserPassEdit;

	@ViewInject(R.id.imgFooter_del)
	private ImageView mPassDel;

	@ViewInject(value = R.id.btn_login)
	private Button mLoginBtn;

	@ViewInject(value = R.id.icon_moreuser)
	private ImageView moreUserImage;

	@ViewInject(value = R.id.line1)
	private View line1;

	private List<MoreUser> mUserList = new ArrayList<MoreUser>();

	private MoreUserAdapter mAdapter;
	// 弹出多个帐号选择
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		getTitleBar().setTitle(R.string.title_login);

		String seesionId = PreferenceUtil.getString(this, BaseConstants.prefre.SessionId);
		if (null != seesionId && !"".equals(seesionId))
		{
			toMainActivity();
		}

		showDeleteImage();

		getMoreUser();

	}

	private void showDeleteImage()
	{

		mUserNameEdit.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				mNameDel.setVisibility((hasFocus && mUserNameEdit.getText().length() > 0) ? View.VISIBLE : View.GONE);
				mPassDel.setVisibility(View.GONE);

				if (!hasFocus)
				{
					mNameDel.setVisibility(View.GONE);
				} else
				{
					if (invalidateText(mUserNameEdit))
						mNameDel.setVisibility(View.GONE);
					else
						mNameDel.setVisibility(View.VISIBLE);
				}
			}
		});

		mUserNameEdit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{

				if (!invalidateText(mUserNameEdit))
				{
					mNameDel.setVisibility(View.VISIBLE);
				} else
				{
					mNameDel.setVisibility(View.GONE);
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

		mNameDel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				mUserNameEdit.setText("");
			}
		});

		mUserPassEdit.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				mNameDel.setVisibility(View.GONE);
				mPassDel.setVisibility((hasFocus && mUserPassEdit.getText().length() > 0) ? View.VISIBLE : View.GONE);

				if (!hasFocus)
				{
					mPassDel.setVisibility(View.GONE);
				} else
				{
					if (invalidateText(mUserPassEdit))
						mPassDel.setVisibility(View.GONE);
					else
						mPassDel.setVisibility(View.VISIBLE);
				}
			}
		});

		mUserPassEdit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
			{
				if (!invalidateText(mUserNameEdit))
				{
					mPassDel.setVisibility(View.VISIBLE);
				} else
				{
					mPassDel.setVisibility(View.GONE);
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

		mPassDel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				mUserPassEdit.setText("");
			}
		});
	}

	@Event(value =
	{ R.id.btn_login, R.id.regiest_text, R.id.forgetpass_text, R.id.icon_moreuser })
	private void loginClickEvent(View view)
	{
		switch (view.getId())
		{
		case R.id.btn_login:
			judgeLogin();
			break;
		case R.id.regiest_text:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.forgetpass_text:
			startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
			break;
		case R.id.icon_moreuser:
			showMoreUserPop();
			break;

		default:
			break;
		}
	}

	/**
	 * pop弹出判断
	 */
	private void showMoreUserPop()
	{
		if (popupWindow != null && popupWindow.isShowing())
		{
			popupWindow.dismiss();
		} else
		{
			mUserList = getUserList();
			if (mUserList != null && mUserList.size() > 0)
			{
				showWindow(line1);
			}
		}

	}

	private void judgeLogin()
	{
		if (invalidateText(mUserNameEdit, R.string.please_input_username) || invalidateText(mUserPassEdit, R.string.please_input_userpass))
		{
			return;
		}
		if (!NetworkUtil.isNetworkConnected(this))
		{
			App.showShortToast(R.string.network_is_not_connected);
			return;
		}

		doLogin(getText(mUserNameEdit), getText(mUserPassEdit));
	}

	/**
	 * 登录请求
	 */
	private void doLogin(String userName, final String userPass)
	{
		showLoadingDialog("");

		RequestParams params = new RequestParams(BaseConstants.Login);
		params.addBodyParameter("userName", userName);
		params.addBodyParameter("passWord", userPass);

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
						// 保存的sessionId 以后的每次请求都要加上
						PreferenceUtil.putString(LoginActivity.this, BaseConstants.prefre.SessionId, jsonObject.getJSONObject("objectResult").getString("sessionId"));
						PreferenceUtil.putString(LoginActivity.this, BaseConstants.prefre.mUserName, jsonObject.getJSONObject("objectResult").getJSONObject("member").getString("userName"));
						PreferenceUtil.putString(LoginActivity.this, BaseConstants.prefre.mTelphone, jsonObject.getJSONObject("objectResult").getJSONObject("member").getString("telPhone"));
						PreferenceUtil.putString(LoginActivity.this, BaseConstants.prefre.mUserId, jsonObject.getJSONObject("objectResult").getJSONObject("member").getString("id"));
						PreferenceUtil.putString(LoginActivity.this, BaseConstants.prefre.mUserpass, userPass);

						MoreUser mUser = new MoreUser();
						mUser.setUserName(getText(mUserNameEdit));
						mUser.setPassWord(getText(mUserPassEdit));

						MoreUser user = App.db.selector(MoreUser.class).where("UserName", "=", getText(mUserNameEdit)).findFirst();
						if (user == null)
						{
							App.db.save(mUser);
						} else
						{
							App.db.delete(user);
							App.db.save(mUser);
						}
						App.showShortToast("登录成功");
						JPushInterface.setAliasAndTags(getApplicationContext(), PreferenceUtil.getString(LoginActivity.this, BaseConstants.prefre.mUserId), null, tagAliasCallback);

						toMainActivity();

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
				dismissLoadingDialog();
			}

		});

	}

	@Override
	protected void onTitleBarCreated(SimpleTitleBar titleBar)
	{
		super.onTitleBarCreated(titleBar);
		// titleBar.setRightButtonText("跳过");
		//
		// titleBar.setOnRightButtonClickListener(new OnClickListener()
		// {
		// public void onClick(View arg0)
		// {
		// toMainActivity();
		// }
		// });
	}

	/**
	 * 查询保存的用户名密码
	 * 
	 * @return
	 */
	private List<MoreUser> getUserList()
	{
		List<MoreUser> mlist = new ArrayList<MoreUser>();

		try
		{
			mlist = App.db.selector(MoreUser.class).orderBy("id", true).findAll();// 逆向排序
		} catch (DbException e)
		{
			e.printStackTrace();
		}

		return mlist;

	}

	private void getMoreUser()
	{
		mUserList = getUserList();

		if (mUserList != null && mUserList.size() > 0)
		{
			mUserNameEdit.setText(mUserList.get(0).getUserName());
			mUserPassEdit.setText(mUserList.get(0).getPassWord());
		}
	}

	/**
	 * 弹出帐号选择框
	 * 
	 * @param parent
	 */

	@SuppressWarnings("deprecation")
	private void showWindow(View parent)
	{
		if (popupWindow == null)
		{
			ListView lv_user = new ListView(this);
			lv_user.setDivider(new ColorDrawable(getResources().getColor(R.color.light_gray_transparent)));
			lv_user.setDividerHeight(2);
			mAdapter = new MoreUserAdapter(LoginActivity.this, R.layout.item_more_user);
			lv_user.setAdapter(mAdapter);
			mAdapter.update(mUserList);
			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(lv_user, parent.getWidth() - 200, ViewGroup.LayoutParams.WRAP_CONTENT);
			// 使其聚集
			popupWindow.setFocusable(true);
			// 设置允许在外点击消失
			popupWindow.setOutsideTouchable(true);
			// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.showAsDropDown(parent, 100, 2);
			lv_user.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
				{
					popupWindow.dismiss();
					mUserNameEdit.setText(mUserList.get(position).getUserName());
					mUserPassEdit.setText(mUserList.get(position).getPassWord());
				}
			});
		} else
			popupWindow.showAsDropDown(parent, 100, 2);
	}

	private void toMainActivity()
	{
		Intent i = new Intent(LoginActivity.this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		LoginActivity.this.finish();
	}

	// 极光推送注册回调
	TagAliasCallback tagAliasCallback = new TagAliasCallback()
	{
		public boolean connectAgain = true; // 极光注册失败后，进行一次重新注册（只一次）后会置为false；

		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2)
		{
			App.showLog("JPush==tag and alias = " + arg0);
			if (arg0 != 0)
			{
				if (connectAgain)
				{ // 进行一次重新注册
					connectAgain = false;
					JPushInterface.setAliasAndTags(getApplicationContext(), PreferenceUtil.getString(LoginActivity.this, BaseConstants.prefre.mUserId), null, this);
				} else
					Toast.makeText(LoginActivity.this, R.string.jpush_regist_failed, Toast.LENGTH_SHORT).show();
			}
		}
	};

	class MoreUserAdapter extends AbsViewHolderAdapter<MoreUser>
	{

		public MoreUserAdapter(Context context, int layoutRes)
		{
			super(context, layoutRes);
		}

		@Override
		protected void bindData(final int pos, final MoreUser itemData)
		{
			TextView mobie_username = getViewFromHolder(R.id.mobie_username);
			ImageView mobie_del = getViewFromHolder(R.id.mobie_del);

			mobie_username.setText(itemData.getUserName());

			mobie_del.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					try
					{
						// 此处不知道为何，剩余最后一个的时候删掉了布局不会刷新，这里直接隐藏pop，达到相同效果
						if (getUserList().size() == 1)
						{
							popupWindow.dismiss();
						}

						App.db.delete(itemData);
						mAdapter.update(getUserList());

					} catch (DbException e)
					{
						e.printStackTrace();
					}

				}
			});
		}
	}

}
