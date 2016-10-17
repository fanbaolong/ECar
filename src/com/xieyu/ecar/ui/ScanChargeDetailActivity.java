package com.xieyu.ecar.ui;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.Piles;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.util.DialogUtil;
import com.xieyu.ecar.util.PreferenceUtil;

import de.greenrobot.event.EventBus;

/**
 * @author fanbaolong
 *
 *         充电信息
 */
public class ScanChargeDetailActivity extends BackableTitleBarActivity
{
	@V
	private TextView tv_scan_poi_name, tv_scan_poi_address, tv_scan_piles_num;
	@V
	private TextView charge_pile_number, charge_pile_status;
	@V
	private RelativeLayout tv_scan_charge_time_relat, tv_scan_charge_money_relat, tv_scan_charge_degree_relat;
	@V
	private TextView tv_scan_charge_time, tv_scan_charge_money, tv_scan_charge_degree;
	@V
	private Button btn_scan_charge;
	@V
	private TextView tv_scan_free_start_time, tv_scan_free_end_time;

	private Piles mPil = new Piles();

	private int minTime = 0;
	private int degree = 0;
	private int money = 0;
	private String mStartTime = "";
	private String mEndTime = "";
	private String mOrderId = "";
	private float power = 0;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_scan_charge_detail);
		getTitleBar().setTitle(R.string.charge_detail);

		mPil = ScanChargeActivity.mPile;
		power = Float.parseFloat(mPil.getPilesCategory().getElectric() + "") / 60;

		mOrderId = getIntent().getStringExtra("OrderId");
		mStartTime = getIntent().getStringExtra("startTime");
		mEndTime = getIntent().getStringExtra("endTime");

		setView();

	}

	private void setView()
	{
		Injector.getInstance().inject(this);

		tv_scan_charge_time_relat.setOnClickListener(this);
		tv_scan_charge_money_relat.setOnClickListener(this);
		tv_scan_charge_degree_relat.setOnClickListener(this);
		btn_scan_charge.setOnClickListener(this);

		tv_scan_poi_name.setText(mPil.getSite().getName());
		tv_scan_poi_address.setText(mPil.getSite().getPositionName());
		tv_scan_piles_num.setText(mPil.getSite().getPilesSum() + "");

		charge_pile_number.setText(mPil.getSn());
		charge_pile_status.setText("可充电");

		if (mStartTime != null && !"".equals(mStartTime))
		{
			tv_scan_free_start_time.setText(splitTimeText(mStartTime));
		}

		if (mEndTime != null && !"".equals(mEndTime))
		{
			tv_scan_free_end_time.setText(splitTimeText(mEndTime));
		}

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{

		case R.id.btn_scan_charge:
			createChargeOrder();
			break;

		case R.id.tv_scan_charge_time_relat:
			showInputDialog(1);
			break;

		case R.id.tv_scan_charge_degree_relat:
			showInputDialog(2);
			break;

		default:
			break;
		}

	}

	/**
	 * @param type
	 *            1,2 1为弹时间，2位弹度数
	 * @param titleMsg
	 */
	private void showInputDialog(final int type)
	{

		String titleMsg = "";
		if (type == 1)
		{
			titleMsg = "请选择时间";
		} else
		{
			titleMsg = "请选择度数";
		}

		final Dialog dialog = DialogUtil.showCustomInputDialog(this, titleMsg);
		Button btnBottomOk = (Button) dialog.findViewById(R.id.btn_common_bottom_ok);
		Button btnBottomCancel = (Button) dialog.findViewById(R.id.btn_common_bottom_cancel);

		final EditText editText1 = (EditText) dialog.findViewById(R.id.et_input_hours);
		final EditText editText2 = (EditText) dialog.findViewById(R.id.et_input_minute);
		final EditText editText3 = (EditText) dialog.findViewById(R.id.et_input_degree);

		final RelativeLayout relativeLayout1 = (RelativeLayout) dialog.findViewById(R.id.rl_input);
		final RelativeLayout relativeLayout2 = (RelativeLayout) dialog.findViewById(R.id.rl_input_degree);

		if (type == 1)
		{
			relativeLayout1.setVisibility(View.VISIBLE);
			relativeLayout2.setVisibility(View.GONE);
		} else
		{
			relativeLayout1.setVisibility(View.GONE);
			relativeLayout2.setVisibility(View.VISIBLE);
		}

		btnBottomOk.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if (type == 1)
				{
					int hourse = 0;
					int min = 0;

					if (getText(editText1) == null || "".equals(getText(editText1)))
					{
						hourse = 0;
					} else
					{
						hourse = Integer.parseInt(getText(editText1));
					}

					if (getText(editText2) == null || "".equals(getText(editText2)))
					{
						min = 0;
					} else if (Integer.parseInt(getText(editText2)) > 59)
					{
						App.showShortToast("分钟不能大于或等于60");
						return;
					} else
					{
						min = Integer.parseInt(getText(editText2));
					}

					minTime = hourse * 60 + min;
					double d1 = Math.ceil(minTime * power);
					degree = (int) d1;
					tv_scan_charge_time.setText(hourse + "小时" + min + "分");
					tv_scan_charge_degree.setText(degree + "度");
					tv_scan_charge_money.setText(degree * 1.5 + "元");

				} else
				{

					String degre = getText(editText3);
					degree = Integer.parseInt(degre);
					double d2 = Math.ceil(Float.parseFloat(degre) / power);
					minTime = (int) d2;
					tv_scan_charge_time.setText((minTime / 60) + "小时" + (minTime % 60) + "分");
					tv_scan_charge_money.setText(degree * 1.5 + "元");
					tv_scan_charge_degree.setText(degree + "度");

				}

				cancleInputManager(arg0);
				dialog.cancel();
			}
		});

		btnBottomCancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				cancleInputManager(arg0);
				dialog.cancel();
			}
		});
	}

	private void cancleInputManager(View arg0)
	{
		// View view =
		// ScanChargeDetailActivity.this.getWindow().peekDecorView();
		if (arg0 != null)
		{
			// 隐藏虚拟键盘
			InputMethodManager inputmanger = (InputMethodManager) ScanChargeDetailActivity.this.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
		}
	}

	/**
	 * 分割时间string
	 * 
	 * @param time
	 * @return
	 */
	private String splitTimeText(String time)
	{
		String[] aa = time.split("\\s+");
		String s = aa[1] + "\n" + aa[0];
		return s;

	}

	// *String sessionId 登陆时候获取到的SeesionId
	// * String type 类型 Car/Piles
	// * Long siteId 网点Id
	// * Long id 扫描对象的id
	// * String sn 扫描对象的唯一编号
	// * String minuteCount 充电时间 多少分钟
	// * String degrees 充电度数
	// * Long orderId 订单编号 如果已经有预约订单,回传订单编号，如果是新订单 就null
	/**
	 * 扫码后的下单
	 * 
	 */
	private void createChargeOrder()
	{
		if (invalidateText(tv_scan_charge_time, R.string.please_input_time) || invalidateText(tv_scan_charge_degree, R.string.please_input_dgree))
		{
			return;
		}

		RequestParams params = new RequestParams(BaseConstants.scanCodeOrder);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(this, BaseConstants.prefre.SessionId));
		params.addBodyParameter("type", "Piles");
		params.addBodyParameter("siteId", mPil.getSite().getId() + "");
		params.addBodyParameter("id", mPil.getId() + "");
		params.addBodyParameter("sn", mPil.getSn());
		params.addBodyParameter("minuteCount", minTime + "");
		params.addBodyParameter("degrees", degree + "");
		params.addBodyParameter("orderId", mOrderId + "");

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
						App.showShortToast("充电开始");
						EventBus.getDefault().post(EventMessage.refreshOrder);
						// 打开自定义的Activity
						Intent i = new Intent(context, MainActivity.class);
						App.ispager = true;
						App.select = 1;

						// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(i);

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
