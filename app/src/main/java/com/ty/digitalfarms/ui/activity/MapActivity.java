package com.ty.digitalfarms.ui.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.jaeger.library.StatusBarUtil;
import com.ty.digitalfarms.R;
import com.ty.digitalfarms.bean.DeviceInfo;
import com.ty.digitalfarms.bean.MapPointInfo;
import com.ty.digitalfarms.ui.FarmsApp;
import com.ty.digitalfarms.ui.adapter.MapPointAdapter;
import com.ty.digitalfarms.ui.view.LineItemDecoration;
import com.ty.digitalfarms.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MapActivity extends BaseActivity implements BaiduMap.OnMapLoadedCallback {

    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rv_map)
    RecyclerView rvSearch;


    private BaiduMap bdMap;

    private List<MapPointInfo> mapPointInfoList = new ArrayList<>();
    private MapPointAdapter adapter;
    private SearchView searchView;
    private List<DeviceInfo.ResultBean> weatherStationList;
    private List<DeviceInfo.ResultBean> allDevicesList = new ArrayList<>();
    //中心点位置
    private double currLat;
    private double currLng;

    @Override
    protected void onBaseCreate() {
        setContentView(R.layout.activity_map);
        StatusBarUtil.setColor(MapActivity.this, getResources().getColor(R.color.toolbar_color));
        ButterKnife.bind(this);
        initData();
        initMapView();
    }

    private void initData() {
        Intent intent = getIntent();
        weatherStationList = intent.getParcelableArrayListExtra("weatherStationList");
        //石家庄--114.505305,38.055049
        //山西屯留--113.0227781，36.3522553
        //广西--108.364152,22.828367
        if (weatherStationList == null||weatherStationList.size() == 0) {
            UIUtils.showToast("没有设备信息！");
            //山西--屯留
            currLat = 36.3522553;
            currLng = 113.0227781;
            return;
        }

        for (int i = 0; i < weatherStationList.size(); i++) {
            DeviceInfo.ResultBean resultBean = weatherStationList.get(i);
            mapPointInfoList.add(new MapPointInfo(resultBean.getTag(),
                    resultBean.getLatitude(), resultBean.getLongtitude()));
            allDevicesList.add(resultBean);
        }

        //设置中心点位置
        if (allDevicesList.size() > 0) {
            currLat = Double.parseDouble(allDevicesList.get(0).getLatitude());
            currLng = Double.parseDouble(allDevicesList.get(0).getLongtitude());
        }

    }

    private void initMapView() {

        toolBar.setTitle("地图模式");
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolBar.setOnCreateContextMenuListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        rvSearch.addItemDecoration(new LineItemDecoration(UIUtils.getContext(),
                LineItemDecoration.VERTICAL_LIST, 2, R.color.line_color));
        rvSearch.setLayoutManager(mLayoutManager);
        bdMap = mapView.getMap();
        bdMap.setOnMapLoadedCallback(this);
        mapView.removeViewAt(1);//去除百度log
        //  bdMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //bdMap.setMaxAndMinZoomLevel(21, 10);//设置地图的最大/最小级别
        //定位到设备位置(山西屯留基地)

        // LatLng latLng = new LatLng(36.3522553, 113.0227781);// lat,lng
        LatLng latLng = new LatLng(currLat, currLng);
        //设置地图显示级别
        MapStatus mapStatus = new MapStatus.Builder().target(latLng).build();
        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        bdMap.setMapStatus(statusUpdate);

        bdMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                bdMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        addMarker(allDevicesList);
        adapter = new MapPointAdapter(mapPointInfoList);
        adapter.setItemClickListener(new MapPointAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(String lat, String lon) {
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                //设置地图显示级别
                MapStatus mapStatus = new MapStatus.Builder().zoom(12).target(latLng).build();
                MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                bdMap.setMapStatus(statusUpdate);
                rvSearch.setVisibility(View.GONE);
                searchView.clearFocus();
            }
        });
        rvSearch.setAdapter(adapter);
    }

    private void addMarker(List<DeviceInfo.ResultBean> data) {
        ClusterManager<MyItem> mClusterManager = new ClusterManager<MyItem>(FarmsApp.getContext(), bdMap);
        //向地图添加Marker点
        List<MyItem> items = new ArrayList<>();
        for (DeviceInfo.ResultBean info : data) {
            items.add(new MyItem(info));
        }
        mClusterManager.addItems(items);
        //设置地图监听，当地图状态发生改变时，进行点聚合运算
        bdMap.setOnMapStatusChangeListener(mClusterManager);

        //设置marker点击时的响应
        bdMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                // 将地图移到到当前点击经纬度位置 并增加缩放等级
                bdMap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(new MapStatus.Builder()
                                .zoom(bdMap.getMapStatus().zoom + 5)
                                .target(cluster.getPosition())
                                .build()));
                return true;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                final DeviceInfo.ResultBean deviceInfo = item.getData();
                LatLng position = item.getPosition();
               if (deviceInfo.getTypeCategory() == 2) {
                    Intent intent = new Intent(MapActivity.this, CurrentDataActivity.class);
                    intent.putExtra("deviceInfo", deviceInfo);
                    intent.putExtra("deviceTag", deviceInfo.getTag());
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    public void onMapLoaded() {
        MapStatus ms = new MapStatus.Builder().zoom(9).build();
        bdMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private final DeviceInfo.ResultBean mData;

        public MyItem(DeviceInfo.ResultBean data) {
            this.mData = data;
            Double lat = Double.parseDouble(data.getLatitude());
            Double lng = Double.parseDouble(data.getLongtitude());
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return UIUtils.getMarker(mData.getTypeCategory());
        }

        @Override
        public DeviceInfo.ResultBean getData() {
            return mData;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    UIUtils.showToast(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText)) {
                        rvSearch.setVisibility(View.GONE);
                    } else {
                        rvSearch.setVisibility(View.VISIBLE);
                        if (adapter != null) {
                            adapter.getFilter().filter(newText);
                        }
                    }
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
