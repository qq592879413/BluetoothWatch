package com.iwatch.bluetoothwatch.util;

import android.util.Log;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

/**
 * Created by DAIKO T540P on 2018/12/7.
 */

public class BlueUtils {
    private String notifyService = "0000fee0-0000-1000-8000-00805f9b34fb";
    private String writeUUid = "0000fee2-0000-1000-8000-00805f9b34fb";
    private BluetoothClient mClient = ClientManager.getClient();
    public final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == Constants.REQUEST_SUCCESS) {
                Log.d("write", "----success ---->");
            } else {
                Log.d("write", "----failed ---->");
            }
        }
    };
    public void writeToBleCon(final String str,final String bleAddress){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("----11---11---",""+str);
                writeOne(str,bleAddress);
            }
        }).start();
    }
    public boolean writeOne(String string,String bleAddress) {
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
                            byteTmp,  mWriteRsp);
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
    public final BleUnnotifyResponse mUnnotifyRsps = new BleUnnotifyResponse() {
        @Override
        public void onResponse(int code) {
            if (code == Constants.REQUEST_SUCCESS) {

            }
        }
    };

}
