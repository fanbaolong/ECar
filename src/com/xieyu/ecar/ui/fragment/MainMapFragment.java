package com.xieyu.ecar.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ta.utdid2.android.utils.StringUtils;
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.Sites;
import com.xieyu.ecar.bean.SitesCar;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.BaseActivity;
import com.xieyu.ecar.ui.BookCarInfoActivity;
import com.xieyu.ecar.ui.BookChargeDetailActivity;
import com.xieyu.ecar.ui.ControlActivity;
import com.xieyu.ecar.ui.ZBarScanActivity;
import com.xieyu.ecar.ui.view.TabHeadView;
import com.xieyu.ecar.util.DialogPrompt;
import com.xieyu.ecar.util.PreferenceUtil;

import de.greenrobot.event.EventBus;

/**
 * 首页地图
 * 
 * @author wangfeng
 * 
 */
public class MainMapFragment extends BaseFragment implements OnClickListener {

	@V
	private MapView main_map;
	@V
	private TabHeadView titlebar_content;
	@V
	private ImageView dingwei_image, img_key;

	// 下单弹框
	@V
	private View ll_dialog, v_dialog, include_dialog;
	@V
	private TextView tv_rental_address, tv_rental_parking, tv_car_name,
	tv_car_plate, tv_car_deposit, tv_car_daily;
	@V
	private ImageView img_plcae_order, img_rental_left, img_rental_car,
	img_rental_right;

	private BaiduMap baiduMap;
	private BitmapDescriptor bitmap;
	private Marker marker;
	private List<Sites> mSites = new ArrayList<Sites>();
	private List<SitesCar> sitesCars = new ArrayList<SitesCar>();
	private int carSelect = 0;
	private String keyString;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	boolean isFirstLoc = true;// 是否首次定位
	boolean isLocal = false;
	int position;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mainmap, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Injector.getInstance().inject(getActivity(), this, view);
		EventBus.getDefault().register(this);
		initTitle();
		setView();
		getCurrentCar();
	}

	private void initTitle() {
		titlebar_content.getTitle().setText(getResources().getString(R.string.map));
		titlebar_content.getLeftButton().setVisibility(View.GONE);
		titlebar_content.getRightButton().setImageResource(R.drawable.ic_scan);
		titlebar_content.getRightButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ZBarScanActivity.class));	
			}
		});
	}

	/** 获取是否存在可控车辆 */
	private void getCurrentCar() {
		RequestParams params = new RequestParams(BaseConstants.getCurrentCar);
		params.addBodyParameter("sessionId", PreferenceUtil.getString(
				getActivity(), BaseConstants.prefre.SessionId));
		requestPost(false, "", BaseConstants.getCurrentCar, params);
	}

	@Override
	public void responseSuccess(String result, String msg, String tag) {
		super.responseSuccess(result, msg, tag);
		if (tag.equals(BaseConstants.getCurrentCar)) {
			img_key.setImageResource(R.drawable.key_press);
		}else if (tag.equals(BaseConstants.getFreeCarBySite)) {
			Gson gson = new Gson();
			sitesCars = gson.fromJson(result, new TypeToken<List<SitesCar>>() {
			}.getType());
			if(sitesCars.size() == 0){
				App.showShortToast("暂无车辆");
				return;
			}
			showSitesCar();
		}
	}

	@Override
	public void responseFail(String msg, String tag) {
		super.responseFail(msg, tag);
		if (tag.equals(BaseConstants.getCurrentCar)) {
			img_key.setImageResource(R.drawable.key);
			keyString = msg;
		}
	}

	/**
	 * 显示预约弹框
	 * @param result
	 */
	private void showSitesCar(){
		ll_dialog.setVisibility(View.VISIBLE);
		tv_rental_address.setText(sitesCars.get(carSelect).getSite().getPositionName());
		tv_rental_parking.setText(Html.fromHtml("剩余车辆：<font color=\"#009E3C\">"+
				sitesCars.get(carSelect).getSite().getCarPortNum() +"</font>  专用停车位：<font color=\"#009E3C\">"+
				sitesCars.get(carSelect).getSite().getCarSum() +"</font>"));
		tv_car_plate.setText(sitesCars.get(carSelect).getLicense());
		tv_car_name.setText(sitesCars.get(carSelect).getCarCategory().getName());
		tv_car_daily.setText("日租：￥" + sitesCars.get(carSelect).getCarCategory().getMoneyDay() + "/天");
		img_rental_left.setVisibility(View.VISIBLE);
		img_rental_right.setVisibility(View.VISIBLE);

		if (carSelect == 0) {
			img_rental_left.setVisibility(View.INVISIBLE);
		}
		if (carSelect == sitesCars.size()-1) {
			img_rental_right.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || main_map == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
			.accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
			.direction(100).latitude(location.getLatitude())
			.longitude(location.getLongitude()).build();
			baiduMap.setMyLocationData(locData);
			if (isFirstLoc || isLocal) {
				isLocal = false;
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				baiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void setView() {
		dingwei_image.setOnClickListener(this);
		img_key.setOnClickListener(this);

		img_rental_left.setOnClickListener(this);
		img_rental_right.setOnClickListener(this);
		img_plcae_order.setOnClickListener(this);
		v_dialog.setOnClickListener(this);
		include_dialog.setOnClickListener(this);

		isFirstLoc = true;
		mActivity.setGesture(false);
		baiduMap = main_map.getMap();
		baiduMap.setTrafficEnabled(true);// 开启交通图
		MapStatusUpdate sUpdate = MapStatusUpdateFactory.zoomTo(14.0f);
		baiduMap.setMapStatus(sUpdate);

		baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, mCurrentMarker));
		// 开启定位图层
		baiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// getData();
		// getSites();
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {

				position = marker.getZIndex();
				// final Sites mpo; //= mSites.get(position);
				// final String type ;//= mpo.getSiteType();
				// 这里存在一个安全隐患，虽然说了marker一般不会变，这里更新数据，但是不排除在你进去的时候marker会增加一个
				// 但这是需求
				((BaseActivity) getActivity()).showLoadingDialog("");
				RequestParams params = new RequestParams(BaseConstants.getSites);
				x.http().post(params, new CommonCallback<String>() {
					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getString("resultType").equals("OK")) {

								Gson gson = new Gson();
								mSites = gson.fromJson(
										jsonObject.getJSONArray("objectResult")
										.toString(),
										new TypeToken<List<Sites>>() {
										}.getType());
								getFreeCarBySite();
							} else {
								App.showShortToast(jsonObject
										.getString("resultMes"));
							}

						} catch (Exception e) {
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
						((BaseActivity) getActivity()).dismissLoadingDialog();
					}
				});
				return true;
			}
		});
		baiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				baiduMap.hideInfoWindow();
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
			}

			@Override
			public void onMapStatusChange(MapStatus arg0) {
			}
		});
	}

	private void getFreeCarBySite() {
		Sites mpo = mSites.get(position);
		RequestParams params = new RequestParams(BaseConstants.getFreeCarBySite);
		params.addBodyParameter("siteId", mpo.getId() + "");
		requestPost(true, "", BaseConstants.getFreeCarBySite, params);
	}

	private void getSites() {
		RequestParams params = new RequestParams(BaseConstants.getSites);
		x.http().post(params, new CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString("resultType").equals("OK")) {

						Gson gson = new Gson();
						mSites = gson.fromJson(
								jsonObject.getJSONArray("objectResult")
								.toString(),
								new TypeToken<List<Sites>>() {
								}.getType());

						addPoint(mSites);

					} else {
						App.showShortToast(jsonObject.getString("resultMes"));
					}

				} catch (Exception e) {
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

			}

		});

	}

	/**
	 * 在地图上添加点
	 */
	private void addPoint(List<Sites> mpois) {
		baiduMap.clear();
		int size = mpois.size();
		for (int i = 0; i < size; i++) {
			Sites p = mpois.get(i);
			if (p.getSiteType().equals("Piles")) {
				bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.dianzhuang);
			} else if (p.getSiteType().equals("Car")) {
				bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.wangdain);
			}

			if ("".equals(p.getPositionY()) || null == p.getPositionY()) {
				continue;
			}

			LatLng llA = new LatLng(Double.parseDouble(p.getPositionY()),
					Double.parseDouble(p.getPositionX()));
			p.setLatLng(String.valueOf(llA));

			OverlayOptions oo = new MarkerOptions().icon(bitmap).zIndex(i)
					.position(mSites.get(i).getLatLng()).draggable(true);
			marker = (Marker) baiduMap.addOverlay(oo);

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		getSites();
	}

	@Override
	public void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		baiduMap.setMyLocationEnabled(false);
		main_map.onDestroy();
		main_map = null;
		super.onDestroy();
	}

	public void onEvent(EventMessage message) {
		switch (message) {
		case updateMap:
			getSites();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.dingwei_image:
			isLocal = true;
			break;
		case R.id.img_key:// 远程控制
			if (StringUtils.isEmpty(keyString)) {
				startActivity(new Intent(getActivity(), ControlActivity.class));
			}else {
				DialogPrompt dialogPrompt = new DialogPrompt(mActivity, keyString, "知道了", 1);
				dialogPrompt.showDialog();

			}

			break;
		case R.id.img_rental_left:
			if (carSelect > 0) {
				carSelect--;
				showSitesCar();
			}
			break;
		case R.id.img_rental_right:
			if (carSelect < sitesCars.size()-1) {
				carSelect++;
				showSitesCar();
			}
			break;
		case R.id.v_dialog:
			ll_dialog.setVisibility(View.GONE);
			break;
		case R.id.include_dialog:
			ll_dialog.setVisibility(View.VISIBLE);
			break;
		case R.id.img_plcae_order:
			ll_dialog.setVisibility(View.GONE);
			if(sitesCars.size() == 0){
				App.showShortToast("暂无车辆");
				return;
			}
			SitesCar sitesCar = sitesCars.get(carSelect);
			String type = sitesCar.getSite().getSiteType();
			Intent intent;
			if ("Piles".equals(type)) {
				intent = new Intent(getActivity(),
						BookChargeDetailActivity.class);
				intent.putExtra("mpo", sitesCar);
				mActivity.startActivity(intent, true);
			} else {
				intent = new Intent(getActivity(), BookCarInfoActivity.class);
				intent.putExtra("type", "map");
				intent.putExtra("mpo", sitesCar);
				mActivity.startActivity(intent, true);
			}
			break;

		default:
			break;
		}

	}
}
