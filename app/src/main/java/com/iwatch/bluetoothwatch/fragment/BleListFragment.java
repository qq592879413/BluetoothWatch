package com.iwatch.bluetoothwatch.fragment;

import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.iwatch.bluetoothwatch.LeDeviceListAdapter;
import com.iwatch.bluetoothwatch.MainActivity;
import com.iwatch.bluetoothwatch.R;
import com.iwatch.bluetoothwatch.util.ClientManager;
import com.iwatch.bluetoothwatch.util.DeviceMsgToSend;
import com.iwatch.bluetoothwatch.util.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by DAIKO T540P on 2019/9/25.
 */

public class BleListFragment extends Fragment {
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private ListView mLvDevice;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blelist,container,false);
        mLvDevice = view.findViewById(R.id.lv_device);
        mLeDeviceListAdapter = new LeDeviceListAdapter(MyApplication.getInstance());
        mLvDevice.setAdapter(mLeDeviceListAdapter);

        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bleSearchToBleList(DeviceMsgToSend deviceMsg){
        Log.e("----------",deviceMsg+"");
        mLeDeviceListAdapter.addDevice(deviceMsg.getMessage());
        mLeDeviceListAdapter.notifyDataSetChanged();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
            Log.e("-----EventBus启动-----","");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}

