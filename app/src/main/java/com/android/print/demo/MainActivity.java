package com.android.print.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.print.demo.bluetooth.BluetoothDeviceList;
import com.android.print.demo.bluetooth.BluetoothOperation;
import com.android.print.demo.databinding.ActivityMainBinding;
import com.android.print.demo.permission.EasyPermission;
import com.android.print.demo.usb.UsbOperation;
import com.android.print.demo.util.PrintUtils;
import com.android.print.demo.util.UriGetPath;
import com.android.print.demo.wifi.WifiOperation;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.bluetooth.BluetoothPort;
import com.android.print.sdk.wifi.WifiAdmin;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements EasyPermission.PermissionCallback {
    public static final int CONNECT_DEVICE = 1;
    public static final int ENABLE_BT = 2;
    public static final int REQUEST_PERMISSION = 4;
    public static final int REQUEST_SELECT_FILE = 3;
    /* access modifiers changed from: private */
    public static boolean isConnected;
    protected static IPrinterOpertion myOpertion;
    /* access modifiers changed from: private */
    public ActivityMainBinding binding;
    private String bt_mac;
    private String bt_name;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public ProgressDialog dialog;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    boolean unused = MainActivity.isConnected = true;
                    PrinterInstance unused2 = MainActivity.this.mPrinter = MainActivity.myOpertion.getPrinter();
                    Timer timer = new Timer();
                    MyTask unused3 = MainActivity.this.myTask = new MyTask();
                    timer.schedule(MainActivity.this.myTask, 0, 2000);
                    Toast.makeText(MainActivity.this.context, R.string.yesconn, 0).show();
                    break;
                case 102:
                    if (MainActivity.this.myTask != null) {
                        MainActivity.this.myTask.cancel();
                    }
                    boolean unused4 = MainActivity.isConnected = false;
                    Toast.makeText(MainActivity.this.context, R.string.conn_failed, 0).show();
                    break;
                case 103:
                    if (MainActivity.this.myTask != null) {
                        MainActivity.this.myTask.cancel();
                    }
                    boolean unused5 = MainActivity.isConnected = false;
                    Toast.makeText(MainActivity.this.context, R.string.conn_closed, 0).show();
                    break;
                case 104:
                    boolean unused6 = MainActivity.isConnected = false;
                    Toast.makeText(MainActivity.this.context, R.string.conn_no, 0).show();
                    break;
            }
            MainActivity.this.updateButtonState();
            if (MainActivity.this.dialog != null && MainActivity.this.dialog.isShowing()) {
                MainActivity.this.dialog.dismiss();
            }
        }
    };
    /* access modifiers changed from: private */
    public PrinterInstance mPrinter;
    /* access modifiers changed from: private */
    public MyTask myTask;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v == MainActivity.this.binding.connectLayout) {
                MainActivity.this.connClick();
            } else if (v == MainActivity.this.binding.printText) {
                MainActivity.this.printText();
            } else if (v == MainActivity.this.binding.printImage) {
                MainActivity.this.printImage();
            } else if (v == MainActivity.this.binding.printQr) {
                MainActivity.this.printBarcode();
            } else if (v == MainActivity.this.binding.bigData) {
                MainActivity.this.printBigData();
            } else if (v == MainActivity.this.binding.update) {
                MainActivity.this.startSelectFile();
            }
        }
    };
    String[] permisions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    private String wifi_mac;
    private String wifi_name;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.context = this;
        initView();
    }

    private void initView() {
        this.binding.connectLayout.setOnClickListener(this.onClickListener);
        this.binding.printText.setOnClickListener(this.onClickListener);
        this.binding.printImage.setOnClickListener(this.onClickListener);
        this.binding.update.setOnClickListener(this.onClickListener);
        this.binding.printQr.setOnClickListener(this.onClickListener);
        this.binding.bigData.setOnClickListener(this.onClickListener);
        initDialog();
    }

    /* access modifiers changed from: private */
    public void initDialog() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setProgressStyle(0);
        this.dialog.setTitle("Connecting");
        this.dialog.setMessage("Please Wait...");
        this.dialog.setIndeterminate(true);
        this.dialog.setCancelable(false);
        this.dialog.setCanceledOnTouchOutside(false);
    }

    private boolean hasSDcardPermissions() {
        if (EasyPermission.hasPermissions(this.context, this.permisions)) {
            return true;
        }
        EasyPermission.with((Activity) this).rationale("选择文件需要SDCard读写权限").addRequestCode(4).permissions(this.permisions).request();
        return false;
    }

    /* access modifiers changed from: private */
    public void startSelectFile() {
        if ((isConnected || this.mPrinter != null) && hasSDcardPermissions()) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("*/*");
            intent.addCategory("android.intent.category.OPENABLE");
            startActivityForResult(intent, 3);
        }
    }

    private void tipUpdate(final String filePath) {
        AlertDialog dialog2 = new AlertDialog.Builder(this.context).setTitle("提示").setMessage(filePath + "\n请确认打印机版本是否支持").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.printUpdate(filePath);
            }
        }).setNegativeButton("取消", (DialogInterface.OnClickListener) null).setCancelable(false).create();
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.show();
    }

    /* access modifiers changed from: private */
    public void printText() {
        if (isConnected || this.mPrinter != null) {
            new Thread() {
                public void run() {
                    PrintUtils.printText(MainActivity.this.context.getResources(), MainActivity.this.mPrinter);
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public void printImage() {
        if (isConnected || this.mPrinter != null) {
            new Thread() {
                public void run() {
                    PrintUtils.printImage(MainActivity.this.context.getResources(), MainActivity.this.mPrinter);
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public void printBarcode() {
        if (isConnected || this.mPrinter != null) {
            new Thread() {
                public void run() {
                    PrintUtils.printBarcode(MainActivity.this.context.getResources(), MainActivity.this.mPrinter);
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public void printBigData() {
        if (isConnected || this.mPrinter != null) {
            this.dialog.setTitle((CharSequence) null);
            this.dialog.setMessage("正在打印, 请稍后...");
            this.dialog.show();
            new Thread() {
                public void run() {
                    PrintUtils.printBigData(MainActivity.this.context.getResources(), MainActivity.this.mPrinter);
                    MainActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            MainActivity.this.initDialog();
                        }
                    });
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public void printUpdate(final String filePath) {
        if (isConnected || this.mPrinter != null) {
            this.dialog.setTitle((CharSequence) null);
            this.dialog.setMessage("正在升级, 请稍后...");
            this.dialog.show();
            new Thread() {
                public void run() {
                    PrintUtils.printUpdate(MainActivity.this.context.getResources(), MainActivity.this.mPrinter, filePath);
                    MainActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this.context, "数据已发送, 请等待打开机升级完成", 1).show();
                            MainActivity.this.initDialog();
                        }
                    });
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public void updateButtonState() {
        if (!isConnected) {
            this.binding.connectAddress.setText(R.string.no_conn_address);
            this.binding.connectState.setText(R.string.connect);
            this.binding.connectName.setText(R.string.no_conn_name);
            return;
        }
        switch (this.binding.tabLayout.getSelectedTabPosition()) {
            case 0:
                if (this.bt_mac != null && !this.bt_mac.equals("")) {
                    this.binding.connectAddress.setText(getString(R.string.str_address) + this.bt_mac);
                    this.binding.connectState.setText(R.string.disconnect);
                    this.binding.connectName.setText(getString(R.string.str_name) + this.bt_name);
                    return;
                } else if (this.bt_mac == null) {
                    this.bt_mac = BluetoothPort.getmDeviceAddress();
                    this.bt_name = BluetoothPort.getmDeviceName();
                    this.binding.connectAddress.setText(getString(R.string.str_address) + this.bt_mac);
                    this.binding.connectState.setText(R.string.disconnect);
                    this.binding.connectName.setText(getString(R.string.str_name) + this.bt_name);
                    return;
                } else {
                    return;
                }
            case 1:
                this.binding.connectAddress.setText(getString(R.string.str_address) + this.wifi_mac);
                this.binding.connectState.setText(R.string.disconnect);
                this.binding.connectName.setText(getString(R.string.str_name) + this.wifi_name);
                return;
            case 2:
                this.binding.connectAddress.setText(getString(R.string.disconnect));
                this.binding.connectState.setText(R.string.disconnect);
                this.binding.connectName.setText(getString(R.string.disconnect));
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void connClick() {
        if (isConnected) {
            myOpertion.close();
            myOpertion = null;
            this.mPrinter = null;
            return;
        }
        new AlertDialog.Builder(this.context).setTitle(R.string.str_message).setMessage(R.string.str_connlast).setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                MainActivity.this.openConn();
            }
        }).setNegativeButton(R.string.str_resel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.reselConn();
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void openConn() {
        switch (this.binding.tabLayout.getSelectedTabPosition()) {
            case 0:
                myOpertion = new BluetoothOperation(this.context, this.mHandler);
                myOpertion.btAutoConn(this.context, this.mHandler);
                return;
            case 1:
                WifiManager mWifi = (WifiManager) getApplicationContext().getSystemService("wifi");
                if (!mWifi.isWifiEnabled()) {
                    mWifi.setWifiEnabled(true);
                    return;
                }
                this.wifi_name = mWifi.getConnectionInfo().getSSID();
                if (this.wifi_name != null && !this.wifi_name.equals("")) {
                    this.wifi_mac = new WifiAdmin(this).intToIp(mWifi.getDhcpInfo().serverAddress);
                    myOpertion = new WifiOperation(this, this.mHandler);
                    Intent intent = new Intent();
                    intent.putExtra("ip_address", this.wifi_mac);
                    myOpertion.open(intent);
                    return;
                }
                return;
            case 2:
                myOpertion = new UsbOperation(this, this.mHandler);
                myOpertion.usbAutoConn((UsbManager) getSystemService("usb"));
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void reselConn() {
        switch (this.binding.tabLayout.getSelectedTabPosition()) {
            case 0:
                myOpertion = new BluetoothOperation(this.context, this.mHandler);
                myOpertion.chooseDevice();
                return;
            case 1:
                myOpertion = new WifiOperation(this.context, this.mHandler);
                myOpertion.chooseDevice();
                return;
            case 2:
                myOpertion = new UsbOperation(this.context, this.mHandler);
                myOpertion.chooseDevice();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            switch (this.binding.tabLayout.getSelectedTabPosition()) {
                case 0:
                    this.bt_mac = data.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
                    this.bt_name = data.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_NAME);
                    this.dialog.show();
                    new Thread(new Runnable() {
                        public void run() {
                            MainActivity.myOpertion.open(data);
                        }
                    }).start();
                    return;
                case 1:
                    this.wifi_mac = data.getStringExtra("ip_address");
                    this.wifi_name = data.getExtras().getString("device_name");
                    if (this.wifi_mac.equals("") || this.wifi_mac == null) {
                        this.mHandler.obtainMessage(102).sendToTarget();
                        return;
                    }
                    myOpertion.open(data);
                    this.dialog.show();
                    return;
                case 2:
                    myOpertion.open(data);
                    return;
                default:
                    return;
            }
        } else if (requestCode == 2) {
            if (resultCode == -1) {
                myOpertion.chooseDevice();
            } else {
                Toast.makeText(this, R.string.bt_not_enabled, 0).show();
            }
        } else if (requestCode == 3 && resultCode == -1) {
            String filePath = new UriGetPath().getUriToPath(this.context, data.getData());
            if (filePath == null) {
                Toast.makeText(this, "获取升级文件失败", 0).show();
                return;
            }
            if (!filePath.endsWith("bin")) {
                filePath = null;
                Toast.makeText(this, "升级文件错误", 0).show();
            }
            tipUpdate(filePath);
        }
    }

    private class MyTask extends TimerTask {
        private MyTask() {
        }

        public void run() {
            byte[] by;
            if (MainActivity.isConnected && MainActivity.this.mPrinter != null && (by = MainActivity.this.mPrinter.read()) != null) {
                System.out.println(MainActivity.this.mPrinter.isConnected() + " read byte " + Arrays.toString(by));
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void onPermissionGranted(int requestCode, List<String> list) {
        startSelectFile();
    }

    public void onPermissionDenied(int requestCode, List<String> perms) {
        boolean checkDeniedPermissionsNeverAskAgain = EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "选择文件需要开启权限，请在应用设置开启权限。", R.string.gotoSettings, R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        }, perms);
    }
}
