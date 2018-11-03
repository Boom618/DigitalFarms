/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.baidu.mapapi.clusterutil.clustering;


import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.model.LatLng;
import com.ty.digitalfarms.bean.DeviceInfo;

/**
 * ClusterItem represents a marker on the map.
 */
public interface ClusterItem {

    /**
     * The position of this marker. This must always return the same value.
     */
    LatLng getPosition();

    BitmapDescriptor getBitmapDescriptor();

   DeviceInfo.ResultBean getData();
}