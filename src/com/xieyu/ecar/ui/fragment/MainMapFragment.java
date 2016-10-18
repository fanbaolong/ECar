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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.baidu.mapapi.map.InfoWindow;
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
import com.xieyu.ecar.App;
import com.xieyu.ecar.BaseConstants;
import com.xieyu.ecar.R;
import com.xieyu.ecar.bean.EventMessage;
import com.xieyu.ecar.bean.Sites;
import com.xieyu.ecar.injector.Injector;
import com.xieyu.ecar.injector.V;
import com.xieyu.ecar.ui.BaseActivity;
import com.xieyu.ecar.ui.BookCarDetailActivity;
import com.xieyu.ecar.ui.BookChargeDetailActivity;
import com.xieyu.ecar.ui.MainActivity;
import com.xieyu.ecar.ui.ZBarScanActivity;
import com.xieyu.ecar.util.StringUtil;

import de.greenrobot.event.EventBus;

/**
 * 首页地图
 * 
 * @author wangfeng
 *
 */
public class MainMapFragment extends SuperFragment
{

	@V
	private MapView main_map;
	// @V
	// private TabHeadView headView_main;
	@V
	private ImageView map_top_right_btn;
	@V
	private ImageButton map_top_left_btn;
	@V
	private ImageView dingwei_image;

	private BaiduMap baiduMap;
	private BitmapDescriptor bitmap;
	private Marker marker;
	private List<Sites> mSites = new ArrayList<Sites>();

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	boolean isFirstLoc = true;// 是否首次定位
	boolean isLocal = false;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_mainmap, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		Injector.getInstance().inject(getActivity(), this, view);
		EventBus.getDefault().register(this);
		setView();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener
	{

		@Override
		public void onReceiveLocation(BDLocation location)
		{
			// map view 销毁后不在处理新接收的位置
			if (location == null || main_map == null)
			{
				return;
			}
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			baiduMap.setMyLocationData(locData);
			if (isFirstLoc || isLocal)
			{
				isLocal = false;
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				baiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation)
		{
		}
	}

	private void setView()
	{
		map_top_left_btn.setOnClickListener(this);
		map_top_right_btn.setOnClickListener(this);
		dingwei_image.setOnClickListener(this);

		isFirstLoc = true;
		mActivity.setGesture(false);
		baiduMap = main_map.getMap();
		baiduMap.setTrafficEnabled(true);// 开启交通图
		MapStatusUpdate sUpdate = MapStatusUpdateFactory.zoomTo(14.0f);
		baiduMap.setMapStatus(sUpdate);

		baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.NORMAL, true, mCurrentMarker));
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
//		getSites();
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener()
		{

			@Override
			public boolean onMarkerClick(Marker marker)
			{

				final int position = marker.getZIndex();
//				 final Sites mpo; //= mSites.get(position);
//				 final String type ;//= mpo.getSiteType();
				//这里存在一个安全隐患，虽然说了marker一般不会变，这里更新数据，但是不排除在你进去的时候marker会增加一个
				//但这是需求
				((BaseActivity) getActivity()).showLoadingDialog("");
				RequestParams params = new RequestParams(BaseConstants.getSites);
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

								Gson gson = new Gson();
								mSites = gson.fromJson(jsonObject.getJSONArray("objectResult").toString(), new TypeToken<List<Sites>>()
								{
								}.getType());

								 final Sites mpo = mSites.get(position);
								 final String type = mpo.getSiteType();
								
								// 创建InfoWindow展示的view
								View view = LayoutInflater.from(getActivity()).inflate(R.layout.map_pop, null);
								TextView mPoiName = (TextView) view.findViewById(R.id.poi_name);
								TextView mPoiAddress = (TextView) view.findViewById(R.id.poi_pop_address);
								TextView mPoipileNum = (TextView) view.findViewById(R.id.poi_pop_pilenum);
								TextView mPoiTrickNum = (TextView) view.findViewById(R.id.poi_pop_tricklenum);
								Button mPoipileBtn = (Button) view.findViewById(R.id.btn_yuyue);
								LinearLayout mPoiPileLayout = (LinearLayout) view.findViewById(R.id.pile_layout);
								LinearLayout mPoiCarLayout = (LinearLayout) view.findViewById(R.id.car_layout);
								TextView mPoiCarNum = (TextView) view.findViewById(R.id.poi_pop_carNum);
								TextView mPoiCarSaleTime = (TextView) view.findViewById(R.id.poi_pop_saletime);
								TextView mPoiCarState = (TextView) view.findViewById(R.id.poi_pop_state);
								ImageView mPoiImage = (ImageView) view.findViewById(R.id.poi_pop_image);

								mPoiName.setText(mpo.getName());
								mPoiAddress.setText(mpo.getPositionName());

								if ("Piles".equals(type))
								{// 充电
									mPoiImage.setImageResource(R.drawable.charge);
									mPoiPileLayout.setVisibility(View.VISIBLE);
									mPoiCarLayout.setVisibility(View.GONE);
									mPoipileNum.setText(mpo.getPilesSum() + "");
									mPoiTrickNum.setText(StringUtil.getPilesCategrey(mpo.getPilesCategoryForShow()));

								} else if ("Car".equals(type))
								{// 租车
									mPoiImage.setImageResource(R.drawable.car);
									mPoiPileLayout.setVisibility(View.GONE);
									mPoiCarLayout.setVisibility(View.VISIBLE);
									mPoiCarNum.setText(mpo.getCarSum() + "");
									mPoiCarSaleTime.setText(mpo.getWorkBeginTime() + "-" + mpo.getWordEndTime());
								}

								mPoipileBtn.setOnClickListener(new OnClickListener()
								{
									@Override
									public void onClick(View arg0)
									{

										Intent intent;
										if ("Piles".equals(type))
										{
											intent = new Intent(getActivity(), BookChargeDetailActivity.class);
											intent.putExtra("mpo", mpo);
											mActivity.startActivity(intent, true);
										} else
										{
											intent = new Intent(getActivity(), BookCarDetailActivity.class);
											intent.putExtra("type", "map");
											intent.putExtra("mpo", mpo);
											mActivity.startActivity(intent, true);
										}

									}
								});

								view.setOnClickListener(new OnClickListener()
								{

									@Override
									public void onClick(View arg0)
									{
										baiduMap.hideInfoWindow();
									}
								});

								// 定义用于显示该InfoWindow的坐标点
								LatLng pt = new LatLng(Double.parseDouble(mpo.getPositionY()), Double.parseDouble(mpo.getPositionX()));
								// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
								InfoWindow mInfoWindow = new InfoWindow(view, pt, -60);
								// 显示InfoWindow
								baiduMap.showInfoWindow(mInfoWindow);
								
								
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
					{ }

					@Override
					public void onCancelled(CancelledException cex)
					{
					}
					@Override
					public void onFinished()
					{
						((BaseActivity) getActivity()).dismissLoadingDialog();
					}
				});
				
				

				// // 将地图移到点附近
				// LatLng ll = new LatLng(Double.parseDouble(mpo.getPositionY())
				// + 0.03, Double.parseDouble(mpo.getPositionX()));
				// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				// baiduMap.animateMapStatus(u);

//				// 创建InfoWindow展示的view
//				View view = LayoutInflater.from(getActivity()).inflate(R.layout.map_pop, null);
//				TextView mPoiName = (TextView) view.findViewById(R.id.poi_name);
//				TextView mPoiAddress = (TextView) view.findViewById(R.id.poi_pop_address);
//				TextView mPoipileNum = (TextView) view.findViewById(R.id.poi_pop_pilenum);
//				TextView mPoiTrickNum = (TextView) view.findViewById(R.id.poi_pop_tricklenum);
//				Button mPoipileBtn = (Button) view.findViewById(R.id.btn_yuyue);
//				LinearLayout mPoiPileLayout = (LinearLayout) view.findViewById(R.id.pile_layout);
//				LinearLayout mPoiCarLayout = (LinearLayout) view.findViewById(R.id.car_layout);
//				TextView mPoiCarNum = (TextView) view.findViewById(R.id.poi_pop_carNum);
//				TextView mPoiCarSaleTime = (TextView) view.findViewById(R.id.poi_pop_saletime);
//				TextView mPoiCarState = (TextView) view.findViewById(R.id.poi_pop_state);
//				ImageView mPoiImage = (ImageView) view.findViewById(R.id.poi_pop_image);
//
//				mPoiName.setText(mpo.getName());
//				mPoiAddress.setText(mpo.getPositionName());
//
//				if ("Piles".equals(type))
//				{// 充电
//					mPoiImage.setImageResource(R.drawable.charge);
//					mPoiPileLayout.setVisibility(View.VISIBLE);
//					mPoiCarLayout.setVisibility(View.GONE);
//					mPoipileNum.setText(mpo.getPilesSum() + "");
//					mPoiTrickNum.setText(StringUtil.getPilesCategrey(mpo.getPilesCategoryForShow()));
//
//				} else if ("Car".equals(type))
//				{// 租车
//					mPoiImage.setImageResource(R.drawable.car);
//					mPoiPileLayout.setVisibility(View.GONE);
//					mPoiCarLayout.setVisibility(View.VISIBLE);
//					mPoiCarNum.setText(mpo.getCarSum() + "");
//					mPoiCarSaleTime.setText(mpo.getWorkBeginTime() + "-" + mpo.getWordEndTime());
//				}
//
//				mPoipileBtn.setOnClickListener(new OnClickListener()
//				{
//					@Override
//					public void onClick(View arg0)
//					{
//
//						Intent intent;
//						if ("Piles".equals(type))
//						{
//							intent = new Intent(getActivity(), BookChargeDetailActivity.class);
//							intent.putExtra("mpo", mpo);
//							mActivity.startActivity(intent, true);
//						} else
//						{
//							intent = new Intent(getActivity(), BookCarDetailActivity.class);
//							intent.putExtra("type", "map");
//							intent.putExtra("mpo", mpo);
//							mActivity.startActivity(intent, true);
//						}
//
//					}
//				});
//
//				view.setOnClickListener(new OnClickListener()
//				{
//
//					@Override
//					public void onClick(View arg0)
//					{
//						baiduMap.hideInfoWindow();
//					}
//				});
//
//				// 定义用于显示该InfoWindow的坐标点
//				LatLng pt = new LatLng(Double.parseDouble(mpo.getPositionY()), Double.parseDouble(mpo.getPositionX()));
//				// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//				InfoWindow mInfoWindow = new InfoWindow(view, pt, -60);
//				// 显示InfoWindow
//				baiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
		baiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener()
		{

			@Override
			public void onMapStatusChangeStart(MapStatus arg0)
			{
				baiduMap.hideInfoWindow();
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus arg0)
			{
			}

			@Override
			public void onMapStatusChange(MapStatus arg0)
			{
			}
		});
	}

	private void getSites()
	{
		RequestParams params = new RequestParams(BaseConstants.getSites);
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

						Gson gson = new Gson();
						mSites = gson.fromJson(jsonObject.getJSONArray("objectResult").toString(), new TypeToken<List<Sites>>()
						{
						}.getType());

							addPoint(mSites);	
						
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

			}

		});

	}

	/**
	 * 在地图上添加点
	 */
	private void addPoint(List<Sites> mpois)
	{
		baiduMap.clear();
		int size = mpois.size();
		for (int i = 0; i < size; i++)
		{
			Sites p = mpois.get(i);
			if (p.getSiteType().equals("Piles"))
			{
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.dianzhuang);
			} else if (p.getSiteType().equals("Car"))
			{
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.wangdain);
			}

			if ("".equals(p.getPositionY()) || null == p.getPositionY())
			{
				continue;
			}

			LatLng llA = new LatLng(Double.parseDouble(p.getPositionY()), Double.parseDouble(p.getPositionX()));
			p.setLatLng(String.valueOf(llA));

			OverlayOptions oo = new MarkerOptions().icon(bitmap).zIndex(i).position(mSites.get(i).getLatLng()).draggable(true);
			marker = (Marker) baiduMap.addOverlay(oo);

		}

	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSites();
	}

	@Override
	public void onDestroy()
	{
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		baiduMap.setMyLocationEnabled(false);
		main_map.onDestroy();
		main_map = null;
		super.onDestroy();
	}
	
	public void onEvent(EventMessage message){
		switch (message) {
		case updateMap:
			getSites();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.map_top_left_btn:

			MainActivity main = (MainActivity) getActivity();
			if (main.drawer_layout.isDrawerOpen(main.mMenu_layout))
			{
				main.drawer_layout.closeDrawer(main.mMenu_layout);
			} else
			{
				main.drawer_layout.openDrawer(main.mMenu_layout);
			}

			break;
		case R.id.map_top_right_btn:
			startActivity(new Intent(getActivity(), ZBarScanActivity.class));
			break;

		case R.id.dingwei_image:
			isLocal = true;
			break;

		default:
			break;
		}

	}
}