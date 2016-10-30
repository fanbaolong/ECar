package com.xieyu.ecar.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.User;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.view.ActionSheetDialog;
import com.xieyu.ecar.ui.view.ActionSheetDialog.OnSheetItemClickListener;
import com.xieyu.ecar.ui.view.ActionSheetDialog.SheetItemColor;
import com.xieyu.ecar.ui.view.CircularImageView;
import com.xieyu.ecar.ui.view.SimpleTitleBar;
import com.xieyu.ecar.util.FileManager;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.StringUtil;

/**
 * @author fbl
 *
 *         个人中心
 */
public class MyActivity extends BackableTitleBarActivity {
	@V
	private CircularImageView my_user_head;
	@V
	private TextView my_user_name;
	@V
	private EditText my_userName_edit, my_telephone_edit, my_email_edit, my_address_edit;
	@V
	private TextView my_driver_text, my_driver_update_text, my_card_text, my_card_update_text;

	private String mName = "", mTel = "", mEmail = "", mAddress = "";

	private User mUser;

	private static final int IMAGE_REQUEST_CODE = 0; // 相册
	private static final int CAMERA_REQUEST_CODE = 1; // 拍照
	private static final int RESULT_REQUEST_CODE = 2; //

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_my);
		getTitleBar().setTitle(R.string.my_information);
		setView();
	}

	private void setView()
	{
		Injector.getInstance().inject(this);

		my_user_head.setOnClickListener(this);
		my_driver_update_text.setOnClickListener(this);
		my_card_update_text.setOnClickListener(this);

		File file = new File(FileManager.SDPATH);
		if (!file.exists())
		{
			file.mkdirs();
		}

		// getMyDetail();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		getMyDetail();
	}

	private void upDateView(User user)
	{
		my_user_name.setText(user.getUserName());
		my_userName_edit.setText(user.getUserName());
		my_telephone_edit.setText(user.getTelPhone());
		my_email_edit.setText(user.getEmail());
		my_address_edit.setText(user.getAddress());

//		if (null == user.getLicenseExamineState())
//		{
//			my_driver_update_text.setText(R.string.noupload);
//		} else
//		{
//			my_driver_update_text.setText(user.getLicenseExamineState());
//		}

		setImageStatus(my_driver_update_text, user.getDrivingLicenseExamineState());
		setImageStatus(my_card_update_text, user.getLicenseExamineState());
		setTextStatus(1, my_driver_text, user.getDrivingLicenseExamineState());
		setTextStatus(2, my_card_text, user.getLicenseExamineState());

	}
	
	
	private void setTextStatus(int type ,TextView mText, String mstates)
	{
		if ("待上传".equals(mstates))
		{
			if(type == 1){
				mText.setText("上传驾驶证图片");	
			}else {
				mText.setText("上传身份证图片");
			}
			

		} else if ("待审核".equals(mstates))
		{
			mText.setText("审核中");

		} else if ("审核通过".equals(mstates))
		{
			mText.setText("审核已通过");

		} else if ("审核不通过".equals(mstates))
		{
			mText.setText("审核未通过");
		}

	}

	private void setImageStatus(TextView mText, String mstates)
	{
		if ("待上传".equals(mstates))
		{
			mText.setBackgroundResource(R.drawable.btn_login_select);
			mText.setText("待上传");
			mText.setClickable(true);

		} else if ("待审核".equals(mstates))
		{
			mText.setBackgroundResource(R.drawable.btn_of_gray);
			mText.setText("待审核");
			// mText.setClickable(false);

		} else if ("审核通过".equals(mstates))
		{
			mText.setBackgroundResource(R.drawable.btn_login_select);
			mText.setText("已通过");
			mText.setClickable(true);

		} else if ("审核不通过".equals(mstates))
		{
			mText.setBackgroundResource(R.drawable.btn_login_select);
			mText.setText("未通过");
			mText.setClickable(true);
		}

	}

	@Override
	protected void onTitleBarCreated(SimpleTitleBar titleBar)
	{
		super.onTitleBarCreated(titleBar);
		titleBar.setRightButtonText(R.string.save);

		titleBar.setOnRightButtonClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				updateInfo();
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.my_user_head:
			// showSheetDialog();
			break;

		case R.id.my_driver_update_text:
			// showSheetDialog();
			toUploadActivity(2, mUser.getDrivingLicenseExamineState());

			// Intent intent = new Intent(this, UploadIdCardActivity.class);
			// intent.putExtra("type", "2");
			// startActivity(intent);

			break;

		case R.id.my_card_update_text:

			toUploadActivity(1, mUser.getLicenseExamineState());

			// Intent intent1 = new Intent(this, UploadIdCardActivity.class);
			// intent1.putExtra("type", "1");
			// startActivity(intent1);
			break;

		default:
			break;
		}
	}

	private void toUploadActivity(int type, final String status)
	{
		Intent intent = new Intent(this, UploadIdCardActivity.class);
		intent.putExtra("type", type + "");
		intent.putExtra("user", mUser);
		startActivity(intent);

	}

	/**
	 * 获取个人详情
	 */
	private void getMyDetail()
	{
		showLoadingDialog("");
		RequestParams params = new RequestParams(BaseConstants.getMember);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(MyActivity.this, BaseConstants.prefre.SessionId));
		x.http().post(params, new Callback.CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				App.showLog(result);
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						Gson json = new Gson();
						mUser = json.fromJson(jsonObject.getJSONObject("objectResult").toString(), User.class);
						mName = mUser.getUserName();
						mTel = mUser.getTelPhone();
						mEmail = mUser.getEmail();
						mAddress = mUser.getAddress();

						upDateView(mUser);
					} else {
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

	/**
	 * 更新信息
	 */
	private void updateInfo()
	{
		if (mName.equals(getText(my_userName_edit)) && mEmail.equals(getText(my_email_edit)) && mAddress.equals(getText(my_address_edit)))
		{
			App.showShortToast("信息没有改动，不需要保存");
			return;
		}
		if (!StringUtil.isEmail(getText(my_email_edit))) {
			App.showShortToast("请输入正确的邮箱");
			return;
		}

		showLoadingDialog("");
		RequestParams params = new RequestParams(BaseConstants.saveInfo);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(MyActivity.this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("userName", getText(my_userName_edit));
		params.addBodyParameter("email", getText(my_email_edit));
		params.addBodyParameter("address", getText(my_address_edit));

		x.http().post(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String result)
			{
				App.showLog(result);
				try
				{
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK"))
					{
						App.showShortToast("信息保存成功");
						PreferenceUtil.putString(MyActivity.this, BaseConstants.prefre.mUserName, getText(my_userName_edit));
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

	private void showSheetDialog()
	{
		// 设置头像
		new ActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem("拍照", SheetItemColor.Blue, new OnSheetItemClickListener()
		{
			@Override
			public void onClick(int which)
			{
				photo();
			}
		}).addSheetItem("相册", SheetItemColor.Blue, new OnSheetItemClickListener()
		{
			@Override
			public void onClick(int which)
			{
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// i.setPackage("xxxx");// xxxx是具体报名，系统默认的一般是com.android.xx
				startActivityForResult(i, IMAGE_REQUEST_CODE);
			}
		}).show();
	}

	private String path = "";
	private Uri imageUri;

	public void photo()
	{

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String state = Environment.getExternalStorageState();
		if (!state.equals(Environment.MEDIA_MOUNTED))
		{
			App.showShortToast("没找到存储卡");
			return;
		}

		File file = new File(Environment.getExternalStorageDirectory() + "/ECar/", String.valueOf(System.currentTimeMillis()) + ".jpg");
		path = file.getPath();
		App.showLog("path===" + path);
		imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, CAMERA_REQUEST_CODE);
	}

	/**
	 * 图片压缩方法实现
	 * 
	 * @param srcPath
	 * @returnc
	 */
	private Bitmap getimage(String srcPath)
	{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int hh = 800;// 这里设置高度为800f
		int ww = 480;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww)
		{// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh)
		{// 如果高度高的话根据高度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 质量压缩
	 * 
	 * @param image
	 * @return
	 */
	private Bitmap compressImage(Bitmap image)
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100)
		{ // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset(); // 重置baos即清空baos
			options -= 10; // 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		try
		{
			FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Workshop/faceimage.jpg");
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (Exception e)
		{

		}
		return bitmap;
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri)
	{

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	@SuppressWarnings("deprecation")
	private void getImageToView(Intent data)
	{
		Bundle extras = data.getExtras();
		if (extras != null)
		{
			Bitmap photo = extras.getParcelable("data");
			final Drawable drawable = new BitmapDrawable(photo);
			// 显示图片
			my_user_head.setImageDrawable(drawable);

			// 此处为上传代码------------------------
			// 为了测试将代码放于此处，到时应该和数据上传发在一起

			try
			{
				FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Workshop/faceimage.jpg");
				photo.compress(Bitmap.CompressFormat.PNG, 90, out); //

			} catch (Exception e)
			{

			}

		}
	}

	/**
	 * 取得图片回传的数据
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (resultCode != RESULT_CANCELED)
		{
			ContentResolver resolver = getContentResolver();
			Bitmap bm = null;
			switch (requestCode)
			{

			// 本地图库
			case IMAGE_REQUEST_CODE:

				startPhotoZoom(data.getData());

				try
				{
					Uri originalUri = data.getData();
					bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					String[] proj =
					{ MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(originalUri, proj, null, null, null);
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(column_index);
					// 压缩大小
					getimage(path);

				} catch (Exception e)
				{
					Log.w(TAG, e.toString());
				}
				break;
			case CAMERA_REQUEST_CODE:

				if (FileManager.checkSDCardExist())
				{
					startPhotoZoom(imageUri);
					// 压缩大小
					getimage(path);
				} else
				{
					App.showShortToast("没找到存储卡");
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null)
				{
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
