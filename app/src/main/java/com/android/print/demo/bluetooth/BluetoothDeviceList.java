package com.android.print.demo.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.print.demov3.R;
import java.util.Set;

public class BluetoothDeviceList extends Activity {
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";
    public static String EXTRA_RE_PAIR = "re_pair";
    private static final String TAG = "DeviceListActivity";
    private Button backButton;
    private BluetoothAdapter mBtAdapter;
    private View.OnCreateContextMenuListener mCreateContextMenuListener = new View.OnCreateContextMenuListener() {
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo arg2) {
            menu.setHeaderTitle(R.string.select_options);
            String info = ((TextView) ((AdapterView.AdapterContextMenuInfo) arg2).targetView).getText().toString();
            System.out.println("message1:" + info);
            if (info.contains(" ( " + BluetoothDeviceList.this.getResources().getText(R.string.has_paired) + " )")) {
                menu.add(0, 0, 0, R.string.rePaire_connect).setOnMenuItemClickListener(BluetoothDeviceList.this.mOnMenuItemClickListener);
                menu.add(0, 1, 1, R.string.connect_paired).setOnMenuItemClickListener(BluetoothDeviceList.this.mOnMenuItemClickListener);
                return;
            }
            menu.add(0, 2, 2, R.string.paire_connect).setOnMenuItemClickListener(BluetoothDeviceList.this.mOnMenuItemClickListener);
        }
    };
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            System.out.println("message:" + info);
            String address = info.substring(info.length() - 17);
            String name = info.substring(0, info.length() - 17);
            System.out.println("name:" + name);
            BluetoothDeviceList.this.returnToPreviousActivity(address, false, name);
        }
    };
    private AdapterView.OnItemLongClickListener mDeviceLongClickListener = new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            return false;
        }
    };
    /* access modifiers changed from: private */
    public final MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            String info = ((TextView) ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).targetView).getText().toString();
            String address = info.substring(info.length() - 17);
            String name = info.substring(0, 5);
            switch (item.getItemId()) {
                case 0:
                    BluetoothDeviceList.this.returnToPreviousActivity(address, true, name);
                    break;
                case 1:
                case 2:
                    BluetoothDeviceList.this.returnToPreviousActivity(address, false, name);
                    break;
            }
            return false;
        }
    };
    /* access modifiers changed from: private */
    public ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                String itemName = device.getName() + " ( " + BluetoothDeviceList.this.getResources().getText(device.getBondState() == 12 ? R.string.has_paired : R.string.not_paired) + " )\n" + device.getAddress();
                BluetoothDeviceList.this.mPairedDevicesArrayAdapter.remove(itemName);
                BluetoothDeviceList.this.mPairedDevicesArrayAdapter.add(itemName);
                BluetoothDeviceList.this.pairedListView.setEnabled(true);
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                BluetoothDeviceList.this.setProgressBarIndeterminateVisibility(false);
                BluetoothDeviceList.this.setTitle(R.string.select_device);
                if (BluetoothDeviceList.this.mPairedDevicesArrayAdapter.getCount() == 0) {
                    BluetoothDeviceList.this.mPairedDevicesArrayAdapter.add(BluetoothDeviceList.this.getResources().getText(R.string.none_found).toString());
                    BluetoothDeviceList.this.pairedListView.setEnabled(false);
                }
                BluetoothDeviceList.this.scanButton.setEnabled(true);
            }
        }
    };
    /* access modifiers changed from: private */
    public ListView pairedListView;
    /* access modifiers changed from: private */
    public Button scanButton;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setTitle(R.string.select_device);
        setResult(0);
        initView();
    }

    private void initView() {
        this.scanButton = (Button) findViewById(R.id.button_scan);
        this.scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BluetoothDeviceList.this.doDiscovery();
                v.setEnabled(false);
            }
        });
        this.backButton = (Button) findViewById(R.id.button_bace);
        this.backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BluetoothDeviceList.this.finish();
            }
        });
        this.mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_item);
        this.pairedListView = (ListView) findViewById(R.id.paired_devices);
        this.pairedListView.setAdapter(this.mPairedDevicesArrayAdapter);
        this.pairedListView.setOnItemClickListener(this.mDeviceClickListener);
        this.pairedListView.setOnItemLongClickListener(this.mDeviceLongClickListener);
        this.pairedListView.setOnCreateContextMenuListener(this.mCreateContextMenuListener);
        this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = this.mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                this.mPairedDevicesArrayAdapter.add(device.getName() + " ( " + getResources().getText(R.string.has_paired) + " )\n" + device.getAddress());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        if (this.mBtAdapter != null && this.mBtAdapter.isDiscovering()) {
            this.mBtAdapter.cancelDiscovery();
        }
        unregisterReceiver(this.mReceiver);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.device.action.FOUND");
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        registerReceiver(this.mReceiver, filter);
        super.onResume();
    }

    /* access modifiers changed from: private */
    public void doDiscovery() {
        Log.d(TAG, "doDiscovery()");
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        if (this.mBtAdapter.isDiscovering()) {
            this.mBtAdapter.cancelDiscovery();
        }
        this.mPairedDevicesArrayAdapter.clear();
        this.mBtAdapter.startDiscovery();
    }

    /* access modifiers changed from: private */
    public void returnToPreviousActivity(String address, boolean re_pair, String name) {
        if (this.mBtAdapter.isDiscovering()) {
            this.mBtAdapter.cancelDiscovery();
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
        intent.putExtra(EXTRA_RE_PAIR, re_pair);
        intent.putExtra(EXTRA_DEVICE_NAME, name);
        setResult(-1, intent);
        finish();
    }
}
