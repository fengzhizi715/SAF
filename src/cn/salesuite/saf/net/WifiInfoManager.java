package cn.salesuite.saf.net;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiInfoManager {
	
	private WifiManager wm;
	private List<ScanResult> mWifiList;
	
	public WifiInfoManager(){}
	
	public ArrayList<WifiInfo> getWifiInfo(Context context) throws Exception{
		try{
			wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(wm==null || wm.isWifiEnabled()==false) return null;
		
		//if(!AppUtil.isWiFiActive(context)) return null;
		
		startScanWifi();		
		/*
		if (mWifiList==null || mWifiList.size()==0) return null;
		*/
		return getWifiData();
	}
	
	private void startScanWifi() {
		wm.startScan();
		mWifiList = wm.getScanResults();
	}
	
	private ArrayList<WifiInfo> getWifiData() {
		ArrayList<WifiInfo> wifi = new ArrayList<WifiInfo>();

		WifiInfo info;
		if(mWifiList != null){
			for (int i = 0; i < mWifiList.size(); i++) {
				info = new WifiInfo();
				info.mac = (mWifiList.get(i)).BSSID;
				info.signal_strength = (mWifiList.get(i)).level;
				wifi.add(info);
			}
		}
		if(wifi.size()==0 && wm.getConnectionInfo()!=null){
			info = new WifiInfo();
			info.mac = wm.getConnectionInfo().getBSSID();
			info.signal_strength = wm.getConnectionInfo().getRssi();
			wifi.add(info);
		}

		return wifi;
	}
}
