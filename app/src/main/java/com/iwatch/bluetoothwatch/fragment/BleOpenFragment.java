package com.iwatch.bluetoothwatch.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.iwatch.bluetoothwatch.R;
import com.iwatch.bluetoothwatch.util.ClientManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAIKO T540P on 2019/9/25.
 */

public class BleOpenFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_openble,container,false);


        ClientManager.getClient().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                if (openOrClosed){
                    EventBus.getDefault().post("bleSearch");
                }
            }
        });
        return view;
    }
}
