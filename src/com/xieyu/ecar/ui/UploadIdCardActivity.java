package com.xieyu.ecar.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.User;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.view.ActionSheetDialog;
import com.xieyu.ecar.ui.view.ActionSheetDialog.OnSheetItemClickListener;
import com.xieyu.ecar.ui.view.ActionSheetDialog.SheetItemColor;
import com.xieyu.ecar.util.FileManager;
import com.xieyu.ecar.util.PreferenceUtil;
import com.xieyu.ecar.util.Properties;
import com.xieyu.ecar.util.StringUtil;
import com.xieyu.ecar.util.UpLoadImageUtil;
import com.xieyu.ecar.util.UpLoadImageUtil.upLoadStatesListener;
import com.xieyu.ecar.util.Utils;

/**
 * @author fanbaolong
 *
 *         上传证件号
 */
public class UploadIdCardActivity extends BackableTitleBarActivity
{
	@V
	private EditText my_name_edit, my_card_edit;

	@V
	private ImageView card_img1, card_img2, shili1, shili2;

	@V
	private TextView my_name_left_text, my_card_left_text, tv_image_describe, tv_upload_status;

	@V
	private RelativeLayout relat_upload_status;

	@V
	private Button btn_upload;

	private String type;

	private int imgType = 0;
	private String imageStr1 = "";
	private String imageStr2 = "";

	private String imagePath1 = "";
	private String imagePath2 = "";

	private static final int IMAGE_REQUEST_CODE = 0; // 相册
	private static final int CAMERA_REQUEST_CODE = 1; // 拍照
	private static final int RESULT_REQUEST_CODE = 2; //

	private User mUser = new User();
	
	private File mImageOutPutFile;
	private String mImagePath;
	private Uri mImageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_upload_idcard);
		type = getIntent().getStringExtra("type");
		Injector.getInstance().inject(this);
		
		
		// 为了解决sd卡物理删除后出现的bug
		File file = new File(FileManager.SDPATH);
		if (!file.exists())
		{
			file.mkdirs();
		}

		mUser = (User) getIntent().getSerializableExtra("user");

		if ("1".equals(type))
		{
			getTitleBar().setTitle("上传身份证");
		} else if ("2".equals(type))
		{
			getTitleBar().setTitle("上传驾驶证");
		}

		initView();
	}

	private void initView()
	{
		btn_upload.setOnClickListener(this);
		card_img1.setOnClickListener(this);
		card_img2.setOnClickListener(this);

		updateUI();

	}

	private void updateUI()
	{
		if ("1".equals(type))
		{
			// 身份证提交页面

			my_card_left_text.setText(R.string.id_number);
			my_card_edit.setHint(R.string.id_number_hint);

			String mstates = mUser.getLicenseExamineState();
			if ("待上传".equals(mstates))
			{
				tv_upload_status.setText("待上传");
				relat_upload_status.setVisibility(View.GONE);

				card_img1.setImageResource(R.drawable.add_card1);
				card_img2.setImageResource(R.drawable.add_card2);
				shili1.setImageResource(R.drawable.id_card1);
				shili2.setImageResource(R.drawable.id_card2);

			} else
			{
				tv_upload_status.setText(mstates);
				relat_upload_status.setVisibility(View.VISIBLE);

				my_name_edit.setText(mUser.getName());
				my_card_edit.setText(mUser.getLicense());
				my_name_edit.setEnabled(false);
				my_card_edit.setEnabled(false);
				App.mImageLoader.displayImage(BaseConstants.API + mUser.getLicensePath1(), card_img1);
				App.mImageLoader.displayImage(BaseConstants.API + mUser.getLicensePath2(), card_img2);
				shili1.setVisibility(View.GONE);
				shili2.setVisibility(View.GONE);
				tv_image_describe.setVisibility(View.GONE);
				btn_upload.setVisibility(View.GONE);

				// 审核未通过时再次提交
				if ("审核不通过".equals(mstates))
				{
					my_name_edit.setEnabled(true);
					my_card_edit.setEnabled(true);
					btn_upload.setVisibility(View.VISIBLE);
					btn_upload.setText("重新上传");
					tv_image_describe.setVisibility(View.VISIBLE);
					tv_image_describe.setText(mUser.getFailureCauseForlicense());
				}

			}

		} else if ("2".equals(type))
		{
			// 驾驶证提交页面

			my_card_left_text.setText(R.string.driving_number);
			my_card_edit.setHint(R.string.driving_number_hint);

			String mstates = mUser.getDrivingLicenseExamineState();

			if ("待上传".equals(mstates))
			{
				tv_upload_status.setText("待上传");
				relat_upload_status.setVisibility(View.GONE);

				card_img1.setImageResource(R.drawable.add_drive1);
				card_img2.setImageResource(R.drawable.add_drive2);
				shili1.setImageResource(R.drawable.drive_card1);
				shili2.setImageResource(R.drawable.drive_card2);

			} else
			{
				tv_upload_status.setText(mstates);
				relat_upload_status.setVisibility(View.VISIBLE);

				my_name_edit.setText(mUser.getName());
				my_card_edit.setText(mUser.getDrivingLicense());
				my_name_edit.setEnabled(false);
				my_card_edit.setEnabled(false);
				App.mImageLoader.displayImage(BaseConstants.API + mUser.getDriveImgPath1(), card_img1);
				App.mImageLoader.displayImage(BaseConstants.API + mUser.getDriveImgPath2(), card_img2);
				shili1.setVisibility(View.GONE);
				shili2.setVisibility(View.GONE);
				tv_image_describe.setVisibility(View.GONE);
				btn_upload.setVisibility(View.GONE);

				// 审核未通过时需要再次提交
				if ("审核不通过".equals(mstates))
				{
					my_name_edit.setEnabled(true);
					my_card_edit.setEnabled(true);
					btn_upload.setVisibility(View.VISIBLE);
					btn_upload.setText("重新上传");
					tv_image_describe.setVisibility(View.VISIBLE);
					tv_image_describe.setText(mUser.getFailureCauseFordrivingLicense());
				}
			}

		}

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.btn_upload:
			toCheck();
			break;
		case R.id.card_img1:
			imgType = 1;
			showSheetDialog();
			break;
		case R.id.card_img2:
			imgType = 2;
			showSheetDialog();
			break;

		default:
			break;
		}
	}

	private void toCheck()
	{
		if ("1".equals(type))
		{
			//这里做身份证等信息的提交
			if (invalidateText(my_name_edit, R.string.my_name_hint) || invalidateText(my_card_edit, R.string.id_number_hint))
			{
				return;
			}
			if(!StringUtil.isIDCard(getText(my_card_edit))){
				App.showShortToast("请输入正确的身份证号");
				return;
			}
			
			if ("".equals(imageStr1) || "".equals(imageStr1))
			{
				App.showShortToast("请选择图片");
				return;
			}

			uploadImage(1, imageStr1, imageStr2);

		} else if ("2".equals(type))
		{
			//这里做驾驶证等信息的提交
			if (invalidateText(my_name_edit, R.string.my_name_hint) || invalidateText(my_card_edit, R.string.driving_number_hint))
			{
				return;
			}

			if ("".equals(imageStr1) || "".equals(imageStr1))
			{
				App.showShortToast("请选择图片");
				return;
			}

			uploadImage(2, imageStr1, imageStr2);
		}

	}

	private void uploadImage(final int type, final String mImage1, final String mImage2)
	{

		UpLoadImageUtil mUtil = new UpLoadImageUtil();
		mUtil.uploadImage(PreferenceUtil.getString(UploadIdCardActivity.this, BaseConstants.prefre.SessionId), mImage1);

		mUtil.setUploadState(new upLoadStatesListener()
		{
			@Override
			public void isFailure()
			{
				App.showLog("isFailure");
			}

			@Override
			public void isSuccess(String ss)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(ss);
					if ("OK".equals(jsonObject.get("resultType")))
					{
						imagePath1 = jsonObject.getString("objectResult");
						uploadImage2(type, mImage2);
					} else
					{
						App.showShortToast(jsonObject.getString("resultMes"));
					}

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	private void uploadImage2(final int cardType, String mImage2)
	{

		UpLoadImageUtil mUtil = new UpLoadImageUtil();
		mUtil.uploadImage(PreferenceUtil.getString(UploadIdCardActivity.this, BaseConstants.prefre.SessionId), mImage2);

		mUtil.setUploadState(new upLoadStatesListener()
		{
			@Override
			public void isFailure()
			{
				App.showLog("isFailure");
			}

			@Override
			public void isSuccess(String ss)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(ss);
					if ("OK".equals(jsonObject.get("resultType")))
					{
						imagePath2 = jsonObject.getString("objectResult");
						bindCard(cardType, imagePath1, imagePath2);
					} else
					{
						App.showShortToast(jsonObject.getString("resultMes"));
					}

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 绑定信息
	 * 
	 * @param type
	 * @param image1
	 * @param image2
	 */
	private void bindCard(int type, String image1, String image2)
	{
		RequestParams params = null;
		if (1 == type)
		{
			params = new RequestParams(BaseConstants.saveLicense);
			params.addBodyParameter("licenseNumber", getText(my_card_edit));
			params.addBodyParameter("licensePic1", image1);
			params.addBodyParameter("licensePic2", image2);

		} else if (2 == type)
		{
			params = new RequestParams(BaseConstants.saveDriverLicense);
			params.addBodyParameter("carLicenseNumber", getText(my_card_edit));
			params.addBodyParameter("carLicensePic1", image1);
			params.addBodyParameter("carLicensePic2", image2);
		}

		params.addBodyParameter("sessionId", PreferenceUtil.getString(UploadIdCardActivity.this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("name", getText(my_name_edit));

		x.http().post(params, new CommonCallback<String>()
		{

			@Override
			public void onSuccess(String result)
			{
				App.showShortToast("信息已提交审核");
				finish();
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

	private void showSheetDialog()
	{
		// 设置头像
		new ActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem("拍照", SheetItemColor.Blue, new OnSheetItemClickListener()
		{
			@Override
			public void onClick(int which)
			{
//				photo();
				getPicFromCapture(Properties.requestcode_takePhoto);
			}
		}).addSheetItem("相册", SheetItemColor.Blue, new OnSheetItemClickListener()
		{
			@Override
			public void onClick(int which)
			{
//				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//				// i.setPackage("xxxx");// xxxx是具体报名，系统默认的一般是com.android.xx
//				startActivityForResult(i, IMAGE_REQUEST_CODE);
				
				choosePhoto(Properties.requestcode_choose_photo);
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

		File file = new File( FileManager.SDPATH, String.valueOf(System.currentTimeMillis()) + ".jpg");
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
			// 输出图片的sd卡路径
			String pathStr = FileManager.SDPATH + String.valueOf(System.currentTimeMillis()) + ".jpg";

			FileOutputStream out = new FileOutputStream(pathStr);
			if (imgType == 1)
			{
				imageStr1 = pathStr;
			} else if (imgType == 2)
			{
				imageStr2 = pathStr;
			}

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
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 2);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 510);
		intent.putExtra("outputY", 324);
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
			if (imgType == 1)
			{
				card_img1.setImageDrawable(drawable);
			} else if (imgType == 2)
			{
				card_img2.setImageDrawable(drawable);
			}

			try
			{
				String imagePath = "";
				if (imgType == 1)
				{
					imagePath = imageStr1;
				} else if (imgType == 2)
				{
					imagePath = imageStr2;
				}

				FileOutputStream out = new FileOutputStream(imagePath);
				photo.compress(Bitmap.CompressFormat.PNG, 90, out);

			} catch (Exception e)
			{

			}

		}
	}
//
//	/**
//	 * 取得图片回传的数据
//	 */
//	public void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		
//		App.showLog("onActivityResult");
//
//		if (resultCode != RESULT_CANCELED)
//		{
//			ContentResolver resolver = getContentResolver();
//			Bitmap bm = null;
//			switch (requestCode)
//			{
//
//			// 本地图库
//			case IMAGE_REQUEST_CODE:
//
//				startPhotoZoom(data.getData());
//
//				try
//				{
//					Uri originalUri = data.getData();
//					bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
//					String[] proj =
//					{ MediaStore.Images.Media.DATA };
//					Cursor cursor = managedQuery(originalUri, proj, null, null, null);
//					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//					cursor.moveToFirst();
//					String path = cursor.getString(column_index);
//					// 压缩大小
//					getimage(path);
//
//				} catch (Exception e)
//				{
//					Log.w(TAG, e.toString());
//				}
//				break;
//			case CAMERA_REQUEST_CODE:
//
//				if (FileManager.checkSDCardExist())
//				{
//					startPhotoZoom(imageUri);
//					// 压缩大小
//					getimage(path);
//				} else
//				{
//					App.showShortToast("没找到存储卡");
//				}
//				break;
//			case RESULT_REQUEST_CODE:
//				if (data != null)
//				{
//					getImageToView(data);
//				}
//				break;
//			}
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		try
		{
			if (resultCode == RESULT_OK)
			{

				if (requestCode == Properties.requestcode_takePhoto)
				{
					// 拍照 
					takePhotoCrop(Properties.requestcode_picture_crop);

				} else if (requestCode == Properties.requestcode_picture_crop)
				{
					// 完成后 裁剪图片
					if (mImageOutPutFile == null)
					{
						mImageOutPutFile = new File(mImagePath);
					}
					mImagePath = mImageOutPutFile.getPath();
					
						if (imgType == 1)
						{
							App.mImageLoader.displayImage("file:///"+mImagePath,card_img1);
							imageStr1 = mImagePath;
						} else if (imgType == 2)
						{
							App.mImageLoader.displayImage("file:///"+mImagePath,card_img2);
							imageStr2 = mImagePath;
						}
					

				} else if (requestCode == Properties.requestcode_choose_photo)
				{
					Intent intent = Utils.getPictureIntent(data.getData(), mImageUri, 510, 324);
					startActivityForResult(intent, Properties.requestcode_picture_crop);
				}
			} else
			{
			}
		} catch (Exception e)
		{
			App.showShortToast("操作出现异常");
		}
	}
	
	
	
	/**
	 * 拍照
	 *
	 * @param requestCode
	 */
	private void getPicFromCapture(int requestCode)
	{
		// 先检查是否有sd卡
		if (!FileManager.checkSDCardExist())
		{
			Toast.makeText(this,"没有发现sd卡", Toast.LENGTH_LONG).show();
			return;
		}
		mImageOutPutFile = new File(FileManager.SDPATH, Utils.getPhotoFileName());
		mImagePath = mImageOutPutFile.getAbsolutePath();
		Intent intent = Utils.getPicFromCapture(this, mImageOutPutFile);
		startActivityForResult(intent, requestCode);
	}
	
	

	/**
	 * 拍照自动裁剪
	 *
	 * @param requestCode
	 */
	private void takePhotoCrop(int requestCode)
	{
		if (mImageOutPutFile == null) {
			mImageOutPutFile = new File(mImagePath);
		}
		mImagePath = mImageOutPutFile.getPath();
		mImageUri = Uri.fromFile(mImageOutPutFile);

		Intent intent = Utils.pictureCrop(mImageUri, 510, 324, true);
		
		
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 选择系统相册
	 * 
	 * @param requestCode
	 */
	private void choosePhoto(int requestCode)
	{
		mImageOutPutFile = new File(FileManager.SDPATH, Utils.getPhotoFileName());
		mImagePath = mImageOutPutFile.getAbsolutePath();
		mImageUri = Uri.fromFile(mImageOutPutFile);

		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, requestCode);
	}

	private Bitmap decodeUriAsBitmap(Uri uri)
	{
		Bitmap bitmap = null;
		try
		{
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e)
		{
			e.printStackTrace();
			return null;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putString("mImagePath", mImagePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		mImagePath = savedInstanceState.getString("mImagePath");
	}
	

}
