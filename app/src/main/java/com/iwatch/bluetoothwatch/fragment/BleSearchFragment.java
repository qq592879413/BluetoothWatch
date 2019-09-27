package com.iwatch.bluetoothwatch.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.iwatch.bluetoothwatch.R;
import com.iwatch.bluetoothwatch.util.ClientManager;
import com.iwatch.bluetoothwatch.util.DeviceMsgToSend;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAIKO T540P on 2019/9/25.
 */

public class BleSearchFragment extends Fragment {
    private TextView tvBleSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blesearch,container,false);
        mayRequestLocation();
        tvBleSearch = view.findViewById(R.id.tv_bleSearch);
        searchDevice();
        return view;
    }
    private void searchDevice() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        ClientManager.getClient().search(request,mSearchResponse);
    }
    private final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            tvBleSearch.setText("充电座搜索中...");
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            EventBus.getDefault().post(new DeviceMsgToSend(device));

        }

        @Override
        public void onSearchStopped() {
            //tvBleSearch.setText("设备列表");
            EventBus.getDefault().post("bleList");
        }

        @Override
        public void onSearchCanceled() {

        }
    };
    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);

        } else {

        }
    }
}
