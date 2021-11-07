package com.android.print.demo.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.android.print.demo.permission.EasyPermission;
import com.android.print.demov3.R;
import com.android.print.sdk.wifi.WifiAdmin;
import java.util.List;

public class WifiDeviceList extends Activity implements EasyPermission.PermissionCallback {
    private static final String TAG = "DeviceListActivity";
    /* access modifiers changed from: private */
    public String address;
    private Button backButton;
    private Context context;
    private ArrayAdapter<String> deviceArrayAdapter;
    /* access modifiers changed from: private */
    public List<ScanResult> list;
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View v, int position, long arg3) {
            ScanResult mScanResult1 = (ScanResult) WifiDeviceList.this.list.get(position);
            String unused = WifiDeviceList.this.ssid = mScanResult1.SSID;
            String mkey1 = mScanResult1.capabilities;
            if (mkey1.indexOf("NOPASS") > 0) {
                int unused2 = WifiDeviceList.this.mkey = 1;
            } else if (mkey1.indexOf("WEP") > 0) {
                int unused3 = WifiDeviceList.this.mkey = 2;
            } else if (mkey1.indexOf("WPA") > 0) {
                int unused4 = WifiDeviceList.this.mkey = 3;
            }
            WifiDeviceList.this.showDialog_Layout(WifiDeviceList.this);
        }
    };
    private ListView mFoundDevicesListView;
    private ScanResult mScanResult;
    /* access modifiers changed from: private */
    public WifiAdmin mWifiAdmin;
    /* access modifiers changed from: private */
    public BroadcastReceiver mWifiConnectReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(WifiDeviceList.TAG, "Wifi onReceive action = " + intent.getAction());
            if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                int message = intent.getIntExtra("wifi_state", -1);
                Log.d(WifiDeviceList.TAG, "liusl wifi onReceive msg=" + message);
                switch (message) {
                    case 0:
                        Log.d(WifiDeviceList.TAG, "WIFI_STATE_DISABLING");
                        return;
                    case 1:
                        Log.d(WifiDeviceList.TAG, "WIFI_STATE_DISABLED");
                        return;
                    case 2:
                        Log.d(WifiDeviceList.TAG, "WIFI_STATE_ENABLING");
                        return;
                    case 3:
                        Log.d(WifiDeviceList.TAG, "WIFI_STATE_ENABLED");
                        return;
                    case 4:
                        Log.d(WifiDeviceList.TAG, "WIFI_STATE_UNKNOWN");
                        return;
                    default:
                        return;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public WifiManager mWifiManager;
    /* access modifiers changed from: private */
    public int mkey;
    String[] permisions = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_WIFI_STATE"};
    /* access modifiers changed from: private */
    public String pswd;
    private Button scanButton;
    /* access modifiers changed from: private */
    public String ssid;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        requestWindowFeature(5);
        setContentView(R.layout.device_list);
        setTitle(R.string.select_device);
        setResult(0);
        initView();
        hasScanPermissions();
    }

    private void hasScanPermissions() {
        if (!EasyPermission.hasPermissions(this.context, this.permisions)) {
            EasyPermission.with((Activity) this).rationale("程序扫描WIFI需要权限").addRequestCode(1).permissions("android.permission.ACCESS_COARSE_LOCATION").request();
        }
    }

    private void initView() {
        this.scanButton = (Button) findViewById(R.id.button_scan);
        this.scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WifiDeviceList.this.getAllNetWorkList();
            }
        });
        this.backButton = (Button) findViewById(R.id.button_bace);
        this.backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WifiDeviceList.this.finish();
            }
        });
        this.deviceArrayAdapter = new ArrayAdapter<>(this, R.layout.device_item);
        this.mFoundDevicesListView = (ListView) findViewById(R.id.paired_devices);
        this.mFoundDevicesListView.setAdapter(this.deviceArrayAdapter);
        this.mFoundDevicesListView.setOnItemClickListener(this.mDeviceClickListener);
        this.mWifiAdmin = new WifiAdmin(this);
    }

    /* access modifiers changed from: private */
    public void registerWIFI() {
        IntentFilter mWifiFilter = new IntentFilter();
        mWifiFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(this.mWifiConnectReceiver, mWifiFilter);
    }

    /* access modifiers changed from: private */
    public void returnToPreviousActivity(String address2) {
        Intent intent = new Intent();
        intent.putExtra("ip_address", address2);
        Log.v("ipaddress", address2);
        intent.putExtra("device_name", this.ssid);
        setResult(-1, intent);
        finish();
    }

    /* access modifiers changed from: private */
    public void showDialog_Layout(Context context2) {
        View textEntryView = LayoutInflater.from(this).inflate(R.layout.ip_address_edit, (ViewGroup) null);
        final EditText edtInput = (EditText) textEntryView.findViewById(R.id.edtInput);
        AlertDialog.Builder builder = new AlertDialog.Builder(context2);
        builder.setCancelable(false);
        builder.setTitle(R.string.password);
        builder.setView(textEntryView);
        builder.setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String unused = WifiDeviceList.this.pswd = edtInput.getText().toString();
                if (WifiDeviceList.this.pswd != null && !WifiDeviceList.this.pswd.equals("")) {
                    WifiDeviceList.this.registerWIFI();
                    WifiAdmin wifiAdmin = new WifiAdmin(WifiDeviceList.this);
                    if (wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(WifiDeviceList.this.ssid, WifiDeviceList.this.pswd, WifiDeviceList.this.mkey))) {
                        WifiManager unused2 = WifiDeviceList.this.mWifiManager = (WifiManager) WifiDeviceList.this.getApplicationContext().getSystemService("wifi");
                        String unused3 = WifiDeviceList.this.address = WifiDeviceList.this.mWifiAdmin.intToIp(WifiDeviceList.this.mWifiManager.getDhcpInfo().serverAddress);
                        WifiDeviceList.this.returnToPreviousActivity(WifiDeviceList.this.address);
                        WifiDeviceList.this.unregisterReceiver(WifiDeviceList.this.mWifiConnectReceiver);
                        return;
                    }
                    WifiDeviceList.this.returnToPreviousActivity("");
                    WifiDeviceList.this.unregisterReceiver(WifiDeviceList.this.mWifiConnectReceiver);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        AlertDialog show = builder.show();
    }

    public void getAllNetWorkList() {
        this.deviceArrayAdapter.clear();
        this.mWifiAdmin.startScan();
        this.list = this.mWifiAdmin.getWifiList();
        if (this.list != null) {
            for (int i = 0; i < this.list.size(); i++) {
                this.mScanResult = this.list.get(i);
                this.deviceArrayAdapter.add(this.mScanResult.SSID + "\n " + this.mScanResult.capabilities + "\n");
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void onPermissionGranted(int requestCode, List<String> list2) {
    }

    public void onPermissionDenied(int requestCode, List<String> perms) {
        Toast.makeText(this.context, "程序未授权,扫描失败", 0).show();
        EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "扫描设备需要开启权限，请在应用设置开启权限。", R.string.gotoSettings, R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                WifiDeviceList.this.finish();
            }
        }, perms);
    }
}
