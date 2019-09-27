package com.iwatch.bluetoothwatch;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.iwatch.bluetoothwatch.fragment.BindDeviceFragment;
import com.iwatch.bluetoothwatch.fragment.BindFailFragment;
import com.iwatch.bluetoothwatch.fragment.BindOkFragment;
import com.iwatch.bluetoothwatch.fragment.BindSearchAgainFragment;
import com.iwatch.bluetoothwatch.fragment.BleListFragment;
import com.iwatch.bluetoothwatch.fragment.BleOpenFragment;
import com.iwatch.bluetoothwatch.fragment.BleSearchFragment;
import com.iwatch.bluetoothwatch.fragment.NextStepFragment;
import com.iwatch.bluetoothwatch.fragment.UnBindDeviceFragment;
import com.iwatch.bluetoothwatch.fragment.UnBindOkFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by DAIKO T540P on 2019/9/25.
 */

public class HomeActivity extends AppCompatActivity {

    public BindDeviceFragment bindDeviceFragment;
    public BindFailFragment bindFailFragment;
    public BindOkFragment bindOkFragment;
    public BindSearchAgainFragment bindSearchAgainFragment;
    public BleListFragment bleListFragment;
    public BleOpenFragment bleOpenFragment;
    public BleSearchFragment bleSearchFragment;
    public NextStepFragment nextStepFragment;
    public UnBindDeviceFragment unBindDeviceFragment;
    public UnBindOkFragment unBindOkFragment;

    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        bindDeviceFragment = new BindDeviceFragment();
        bindFailFragment = new BindFailFragment();
        bindOkFragment= new BindOkFragment();
        bindSearchAgainFragment = new BindSearchAgainFragment();
        bleListFragment = new BleListFragment();
        bleOpenFragment = new BleOpenFragment();
        bleSearchFragment = new BleSearchFragment();
        nextStepFragment = new NextStepFragment();
        unBindDeviceFragment = new UnBindDeviceFragment();
        unBindOkFragment = new UnBindOkFragment();

        fm = getFragmentManager();
        initFragmentSelected("nextStep");
        /*ft.add(R.id.framelayout,bindDeviceFragment,"bindDevice");
        ft.add(R.id.framelayout,bindFailFragment,"bindFail");
        ft.add(R.id.framelayout,bindOkFragment,"bindOk");
        ft.add(R.id.framelayout,bindSearchAgainFragment,"bindSearchAgain");
        ft.add(R.id.framelayout,bleListFragment,"bleList");
        //ft.add(R.id.framelayout,bleOpenFragment,"bleOpen");
        ft.add(R.id.framelayout,bleSearchFragment,"bleSearch");
        ft.add(R.id.framelayout,nextStepFragment,"nextStep");
        ft.add(R.id.framelayout,unBindDeviceFragment,"unBindDevice");
        ft.add(R.id.framelayout,unBindOkFragment,"unBindOk");
        ft.show(nextStepFragment);
        ft.commit();*/

        initViews();
    }
    public void initViews() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fragmentButtonClicked(String str){
        switch (str){
            //case "nextStep":
               // initFragmentSelected("bleOpen");
               // break;
            case "bleOpen":
                initFragmentSelected("bleOpen");
                break;
            case "bleSearch":
                initFragmentSelected("bleSearch");
                break;
            case "bleList":
                initFragmentSelected("bleList");
                break;
            default:
                break;
        }
    }
    private void initFragmentSelected(String i) {
        fm.executePendingTransactions();
        ft = fm.beginTransaction();
        hideFragments(ft);
        switch (i){
            case "nextStep":
                if (!nextStepFragment.isAdded()){
                    ft.add(R.id.framelayout,nextStepFragment,"nextStep");
                }
                ft.show(nextStepFragment);
                break;
            case "bleOpen":
                if (!bleOpenFragment.isAdded()){
                    ft.add(R.id.framelayout,bleOpenFragment,"bleOpen");
                }
                ft.show(bleOpenFragment);
                break;
            case "bleSearch":
                if (!bleSearchFragment.isAdded()){
                    ft.add(R.id.framelayout,bleSearchFragment,"bleSearch");
                }
                ft.show(bleSearchFragment);
                break;
            case "bleList":
                if (!bleListFragment.isAdded() && null == fm.findFragmentByTag("bleList")){

                    ft.add(R.id.framelayout,bleListFragment,"bleList");
                }
                ft.show(bleListFragment);
                break;
        }
        ft.commit();
    }
    private void hideFragments(FragmentTransaction ft) {
        if (nextStepFragment!=null){
            ft.hide(nextStepFragment);
        }
        if (bleOpenFragment!=null){
            ft.hide(bleOpenFragment);
        }
        if (bleSearchFragment!=null){
            ft.hide(bleSearchFragment);
        }
        if (bleListFragment!=null){
            ft.hide(bleListFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("----onResume---","");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("----onStart---","");
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
            Log.e("----avtivity Event启动---","");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}



