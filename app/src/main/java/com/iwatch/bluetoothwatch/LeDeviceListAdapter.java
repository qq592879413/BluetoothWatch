package com.iwatch.bluetoothwatch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;

/**
 * Created by DAIKO T540P on 2018/6/30.
 */

public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<SearchResult> mLeDevices;
    private LayoutInflater mInflator;
    private Context mContext;

    public LeDeviceListAdapter(Context context) {
        mContext = context;
        mLeDevices = new ArrayList<SearchResult>();
        mInflator = LayoutInflater.from(mContext);
    }

    public void addDevice(SearchResult device) {
        //mLeDevices.clear();
        if(!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    public SearchResult getDevice(int position) {
        return mLeDevices.get(position);
    }
    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress =  view.findViewById(R.id.device_address);
            viewHolder.deviceName =  view.findViewById(R.id.device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        SearchResult device = mLeDevices.get(i);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);
        viewHolder.deviceAddress.setText(device.getAddress());

        return view;
    }
    public static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}

