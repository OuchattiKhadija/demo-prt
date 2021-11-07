package com.android.print.demo.usb;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.android.print.demov3.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@TargetApi(12)
public class UsbDeviceList<V> extends Activity {
    private Button backButton;
    private ArrayAdapter<String> deviceArrayAdapter;
    /* access modifiers changed from: private */
    public List<UsbDevice> deviceList;
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
            UsbDeviceList.this.returnToPreviousActivity((UsbDevice) UsbDeviceList.this.deviceList.get(position));
        }
    };
    private ListView mFoundDevicesListView;
    private Button scanButton;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setTitle(R.string.select_device);
        setResult(0);
        this.scanButton = (Button) findViewById(R.id.button_scan);
        this.scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UsbDeviceList.this.doDiscovery();
            }
        });
        this.backButton = (Button) findViewById(R.id.button_bace);
        this.backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UsbDeviceList.this.finish();
            }
        });
        this.deviceArrayAdapter = new ArrayAdapter<>(this, R.layout.device_item);
        this.mFoundDevicesListView = (ListView) findViewById(R.id.paired_devices);
        this.mFoundDevicesListView.setAdapter(this.deviceArrayAdapter);
        this.mFoundDevicesListView.setOnItemClickListener(this.mDeviceClickListener);
        doDiscovery();
    }

    /* access modifiers changed from: private */
    public void doDiscovery() {
        this.deviceArrayAdapter.clear();
        HashMap<String, UsbDevice> devices = ((UsbManager) getSystemService("usb")).getDeviceList();
        this.deviceList = new ArrayList();
        for (UsbDevice device : devices.values()) {
            this.deviceArrayAdapter.add(device.getDeviceName() + "\nvid: " + device.getVendorId() + " pid: " + device.getProductId());
            this.deviceList.add(device);
        }
    }

    /* access modifiers changed from: private */
    public void returnToPreviousActivity(UsbDevice device) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("device", device);
        intent.putExtras(bundle);
        setResult(-1, intent);
        finish();
    }
}
