package com.iwatch.bluetoothwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by DAIKO T540P on 2018/7/3.
 */

public class DetailDatasActivity extends AppCompatActivity implements View.OnClickListener {

    private String notifyService = "0000fee0-0000-1000-8000-00805f9b34fb";
    private String notifyUUid = "0000fee1-0000-1000-8000-00805f9b34fb";
    private String writeUUid = "0000fee2-0000-1000-8000-00805f9b34fb";
    //private String order_open = "SC163*D*066*SET*ID*UUID*OUTV=OPEN*END\r\n";
    //private String order_close = "SC163*D*066*SET*ID*UUID*OUTV=CLOSE*END\r\n";;
    private String bleName,bleAddress;
    private BluetoothClient mClient;
    private RelativeLayout rlReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.battery_msg);
        mClient = new BluetoothClient(DetailDatasActivity.this);

        rlReturn = findViewById(R.id.rl_return);
        rlReturn.setOnClickListener(this);

        Intent intent = getIntent();
        bleName = intent.getStringExtra("bleName");
        bleAddress = intent.getStringExtra("bleAddress");
        if (bleName != null) {

        } else {

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_open:

                break;
            case R.id.btn_close:

                break;
            case R.id.rl_return:
                mClient.disconnect(bleAddress);
                Intent intent=new Intent();
                intent.setClass(DetailDatasActivity.this, MainActivity.class);
                startActivity(intent);
                DetailDatasActivity.this.finish();
                break;
            case R.id.tv_closeBle:
                mClient.disconnect(bleAddress);

                break;
        }
    }
    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == Constants.REQUEST_SUCCESS) {
                Log.d("write", "----success ---->");
            } else {
                Log.d("write", "----failed ---->");
            }
        }
    };
    public void notifyDatas(){
        mClient.notify(bleAddress, UUID.fromString(notifyService), UUID.fromString(notifyUUid), mNotifyRsp);
    }
    ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
    StringBuffer sb = new StringBuffer();
    byte[] bytes ;
    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(UUID.fromString(notifyService)) && character.equals(UUID.fromString(notifyUUid))) {
                String ss = hexStr2Str(ByteUtils.byteToString(value));

                sb.append(ss);
                if (sb.toString().startsWith("SC") && sb.toString().endsWith("\r\n")){
                    if (sb.toString().contains("COUNT=")&& sb.toString().contains("*END")) {
                        String nxt = sb.toString().substring(40, 46);

                        Log.e("nxt", "---- ---->" + sb.toString());
                        sb.delete(0, sb.length());
                    }
                }
            }
        }

        @Override
        public void onResponse(int code) {
            if (code == Constants.REQUEST_SUCCESS) {

            } else {

            }
        }
    };
    public void writeToBleCon(final String str) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("----11---11---", "" + str);
                if (bleAddress != null) {
                    writeOne(str);
                }
            }
        }).start();
    }
    public boolean writeOne(String string) {
        byte[] bytes = string.getBytes();
        int len = bytes.length;
        byte[] byteTmp;
        if (len <= 20) {
            mClient.write(bleAddress, UUID.fromString(notifyService), UUID.fromString(writeUUid),
                    bytes, mWriteRsp);
        } else {
            int sLen = len / 20;
            int aLen = len % 20;
            for (int i = 0; i < (sLen + 1); i++) {
                if (i != sLen) {
                    byteTmp = new byte[20];
                    System.arraycopy(bytes, i * 20, byteTmp, 0, 20);
                    mClient.write(bleAddress, UUID.fromString(notifyService), UUID.fromString(writeUUid),
                            byteTmp, mWriteRsp);
                } else {
                    int l = len - sLen * 20;
                    byteTmp = new byte[l];
                    System.arraycopy(bytes, sLen * 20, byteTmp, 0, l);
                    mClient.write(bleAddress, UUID.fromString(notifyService), UUID.fromString(writeUUid),
                            byteTmp, mWriteRsp);
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return true;
    }
    public static List<String> getSubUtil(String soap, String rgex){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            list.add(m.group(0));
        }
        Log.e("........",list.toString());
        return list;
    }
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (bleAddress!=null){
            notifyDatas();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mClient.unnotify(bleAddress, UUID.fromString(notifyService),
                UUID.fromString(writeUUid), new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }
}
