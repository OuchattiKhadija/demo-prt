package com.android.print.sdk.wifi;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.util.List;

public class WifiAdmin {
    private static final String TAG = "[WifiAdmin]";
    private DhcpInfo dhcpInfo;
    private List<WifiConfiguration> mWifiConfiguration;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList = null;
    private WifiManager.WifiLock mWifiLock;
    private WifiManager mWifiManager;

    public WifiAdmin(Context context) {
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    public boolean openWifi() {
        if (!this.mWifiManager.isWifiEnabled()) {
            Log.i(TAG, "setWifiEnabled.....");
            this.mWifiManager.setWifiEnabled(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "setWifiEnabled.....end");
        }
        return this.mWifiManager.isWifiEnabled();
    }

    public void closeWifi() {
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }
    }

    public int checkState() {
        return this.mWifiManager.getWifiState();
    }

    public void acquireWifiLock() {
        this.mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (this.mWifiLock.isHeld()) {
            this.mWifiLock.acquire();
        }
    }

    public void creatWifiLock() {
        this.mWifiLock = this.mWifiManager.createWifiLock("Test");
    }

    public List<WifiConfiguration> getConfiguration() {
        return this.mWifiConfiguration;
    }

    public void connectConfiguration(int index) {
        if (index <= this.mWifiConfiguration.size()) {
            this.mWifiManager.enableNetwork(this.mWifiConfiguration.get(index).networkId, true);
        }
    }

    public void startScan() {
        Log.i(TAG, "startScan result:" + this.mWifiManager.startScan());
        this.mWifiList = this.mWifiManager.getScanResults();
        this.mWifiConfiguration = this.mWifiManager.getConfiguredNetworks();
        if (this.mWifiList != null) {
            Log.i(TAG, "startScan result:" + this.mWifiList.size());
            for (int i = 0; i < this.mWifiList.size(); i++) {
                ScanResult result = this.mWifiList.get(i);
                Log.i(TAG, "startScan result[" + i + "]" + result.SSID + "," + result.BSSID);
            }
            Log.i(TAG, "startScan result end.");
            return;
        }
        Log.i(TAG, "startScan result is null.");
    }

    public List<ScanResult> getWifiList() {
        return this.mWifiList;
    }

    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.mWifiList.size(); i++) {
            stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
            stringBuilder.append(this.mWifiList.get(i).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    public String getMacAddress() {
        return this.mWifiInfo == null ? "NULL" : this.mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return this.mWifiInfo == null ? "NULL" : this.mWifiInfo.getBSSID();
    }

    public DhcpInfo getDhcpInfo() {
        DhcpInfo dhcpInfo2 = this.mWifiManager.getDhcpInfo();
        this.dhcpInfo = dhcpInfo2;
        return dhcpInfo2;
    }

    public int getIPAddress() {
        if (this.mWifiInfo == null) {
            return 0;
        }
        return this.mWifiInfo.getIpAddress();
    }

    public int getNetworkId() {
        if (this.mWifiInfo == null) {
            return 0;
        }
        return this.mWifiInfo.getNetworkId();
    }

    public WifiInfo getWifiInfo() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        return this.mWifiInfo;
    }

    public boolean addNetwork(WifiConfiguration wcg) {
        boolean enableNetwork = this.mWifiManager.enableNetwork(this.mWifiManager.addNetwork(wcg), true);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void disconnectWifi(int netId) {
        this.mWifiManager.disableNetwork(netId);
        this.mWifiManager.disconnect();
    }

    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
        Log.i(TAG, "SSID:" + SSID + ",password:" + Password);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        WifiConfiguration tempConfig = IsExsits(SSID);
        if (tempConfig != null) {
            this.mWifiManager.removeNetwork(tempConfig.networkId);
        } else {
            Log.i(TAG, "IsExsits is null.");
        }
        if (Type == 1) {
            Log.i(TAG, "Type =1.");
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 2) {
            Log.i(TAG, "Type =2.");
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(0);
            config.allowedGroupCiphers.set(1);
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) {
            Log.i(TAG, "Type =3.");
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(0);
            config.allowedGroupCiphers.set(2);
            config.allowedKeyManagement.set(1);
            config.allowedPairwiseCiphers.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedPairwiseCiphers.set(2);
            config.status = 2;
        }
        return config;
    }

    private WifiConfiguration IsExsits(String SSID) {
        for (WifiConfiguration existingConfig : this.mWifiManager.getConfiguredNetworks()) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    public String intToIp(int i) {
        return String.valueOf(i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }
}
