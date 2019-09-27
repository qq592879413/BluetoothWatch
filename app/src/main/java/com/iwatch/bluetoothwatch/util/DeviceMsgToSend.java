package com.iwatch.bluetoothwatch.util;

import android.util.Log;

import com.inuker.bluetooth.library.search.SearchResult;

/**
 * Created by DAIKO T540P on 2019/9/26.
 */

public class DeviceMsgToSend {
    private SearchResult device;
    public DeviceMsgToSend(SearchResult device){
        this.device = device;
    }
    public SearchResult getMessage(){
        Log.e("----device------",device+"");
        return device;
    }
    public void setMessage(SearchResult device){
        this.device = device;
    }
}
