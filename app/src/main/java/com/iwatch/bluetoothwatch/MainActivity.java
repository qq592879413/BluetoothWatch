package com.iwatch.bluetoothwatch;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothDevice mDevice;
    private ListView mLvDevice;
    private BluetoothClient mClient;
    private ZLoadingDialog dialog2;
    private String addressName,deviceName;
    private Button mBtnScan;
    private TextView tvTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mayRequestLocation();

        initView();
    }

    private void initView() {
        tvTitleName = findViewById(R.id.tv_titleName);
        mClient = new BluetoothClient(MainActivity.this);
        mLvDevice = (ListView) findViewById(R.id.lv_device);
        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mBtnScan.setOnClickListener(this);
        mDevice = BluetoothUtils.getRemoteDevice(addressName);
        mLvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final SearchResult device = mLeDeviceListAdapter.getDevice(position);
                addressName = device.getAddress();
                deviceName = device.getName();
                dialog2 = new ZLoadingDialog(MainActivity.this);
                dialog2.setLoadingBuilder(Z_TYPE.STAR_LOADING)//设置类型
                        .setLoadingColor(Color.parseColor("#3ab6e0"))//颜色
                        .setHintText("Connecting...")
                        .show();
                BleConnectOptions options = new BleConnectOptions.Builder()
                        .setConnectRetry(3)
                        .setConnectTimeout(20000)
                        .setServiceDiscoverRetry(3)
                        .setServiceDiscoverTimeout(10000)
                        .build();

                mClient.connect(addressName, options, new BleConnectResponse() {
                    @Override
                    public void onResponse(int code, BleGattProfile profile) {
                        if (code == REQUEST_SUCCESS) {
                            dialog2.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("bleName",deviceName);
                            intent.putExtra("bleAddress",addressName);
                            Log.e("page1-----------",""+addressName+";"+deviceName);
                            intent.setClass(MainActivity.this,DetailDatasActivity.class);
                            startActivity(intent);

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_scan:
                if (!mClient.isBluetoothOpened()){
                    mClient.openBluetooth();
                }else{
                    mLeDeviceListAdapter = new LeDeviceListAdapter(MainActivity.this);
                    mLvDevice.setAdapter(mLeDeviceListAdapter);
                    searchDevice();
                }
                break;
        }
    }
    private void searchDevice() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();
        mClient.search(request,mSearchResponse);
    }
    private final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            tvTitleName.setText("扫描蓝牙中...");
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            mLeDeviceListAdapter.addDevice(device);
            mLeDeviceListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSearchStopped() {
            tvTitleName.setText("设备列表");
        }

        @Override
        public void onSearchCanceled() {

        }
    };
    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //判断是否需要 向用户解释，为什么要申请该权限
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this,R.string.ble_need_location, Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
                return;
            }else{

            }
        } else {

        }
    }

}
