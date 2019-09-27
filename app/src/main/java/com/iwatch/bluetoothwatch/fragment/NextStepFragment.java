package com.iwatch.bluetoothwatch.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iwatch.bluetoothwatch.R;
import com.iwatch.bluetoothwatch.util.ClientManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAIKO T540P on 2019/9/25.
 */

public class NextStepFragment extends Fragment{

    private Button btnNextStep;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nextstep,container,false);

        btnNextStep = view.findViewById(R.id.btn_nextstep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ClientManager.getClient().isBluetoothOpened()){
                    EventBus.getDefault().post("bleSearch");
                    Log.e("-----------","bleSearch"+ClientManager.getClient().isBluetoothOpened());
                }else{
                    EventBus.getDefault().post("bleOpen");
                    Log.e("-----------","bleOpen");
                }

            }
        });

        return view;
    }



}
