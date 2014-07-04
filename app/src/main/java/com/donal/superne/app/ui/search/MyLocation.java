package com.donal.superne.app.ui.search;

import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.AppManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyLocation extends BaseActivity implements View.OnClickListener, LocationSource, AMapLocationListener{

    @ViewInject(R.id.map)
    private MapView map;

    private AMap aMap;
    private OnLocationChangedListener locationChangedListener;
    private LocationManagerProxy locationManagerProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        initNavgation();
        ViewUtils.inject(this);
        map.onCreate(savedInstanceState);
        initMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (locationChangedListener != null && aMapLocation != null) {
            locationChangedListener.onLocationChanged(aMapLocation);
            float bearing = aMap.getCameraPosition().bearing;
            aMap.setMyLocationRotateAngle(bearing);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;
        if (locationManagerProxy == null) {
            locationManagerProxy = LocationManagerProxy.getInstance(this);
            locationManagerProxy.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        locationChangedListener = null;
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
        }
        locationManagerProxy = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setBtnLeft(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLeft:
                AppManager.getAppManager().finishActivity(this);
                break;
        }
    }

    private void initMap() {
        if (aMap == null) {
            aMap = map.getMap();
            setupMap();
        }
    }

    private void setupMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        myLocationStyle.strokeColor(Color.BLACK);
        myLocationStyle.strokeWidth(5);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(180);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        LatLng marker = new LatLng(23.40 ,113.22);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,11));
    }
}
