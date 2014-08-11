package cn.salesuite.saf.net;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class CellIDInfoManager {
	private TelephonyManager manager;
	//private MyPhoneStateListener listener;
	private GsmCellLocation gsm;
	private CdmaCellLocation cdma;
	int lac;
	private int networkType;
	String current_ci,mcc, mnc;
	private ArrayList<CellIDInfo> CellID;
	
	public CellIDInfoManager(){}
	
	public ArrayList<CellIDInfo> getCellIDInfo(Context context)throws Exception{
		manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		/*listener = new MyPhoneStateListener();
		manager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);*/
		CellID = new ArrayList<CellIDInfo>();
		CellIDInfo currentCell = new CellIDInfo();
		int type = manager.getPhoneType();
		networkType = manager.getNetworkType();
		
		if (type == TelephonyManager.PHONE_TYPE_GSM) {
			gsm = ((GsmCellLocation) manager.getCellLocation());
		    if (gsm == null) return null;
		    try{
			    lac  = gsm.getLac();
				mcc = manager.getNetworkOperator().substring(0, 3);
				mnc = manager.getNetworkOperator().substring(3, 5);
		    }catch(Exception e){
		    	Log.i("manager.getNetworkOperator()",manager.getNetworkOperator());
		    	return null;
		    }
			currentCell.cellId = gsm.getCid();
			currentCell.mobileCountryCode = mcc;
			currentCell.mobileNetworkCode = mnc;
			currentCell.locationAreaCode = lac;
			currentCell.radioType = "gsm";
			currentCell.signal_strength = -80;
			CellID.add(currentCell);
			
			List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				if(list.get(i).getCid()>0){
					CellIDInfo info = new CellIDInfo();
					info.cellId = list.get(i).getCid();
					info.mobileCountryCode = mcc;
					info.mobileNetworkCode = mnc;
					info.locationAreaCode = list.get(i).getLac();
					info.signal_strength = calcDMB(list.get(i).getRssi());
					CellID.add(info);
				}
			}
			return CellID;
			
		} else if (type == TelephonyManager.PHONE_TYPE_CDMA) {
			cdma = ((CdmaCellLocation) manager.getCellLocation());
			if (cdma == null) return null;
	    	
	    	//if ("460".equals(manager.getSimOperator().substring(0, 3))) 
	    	//	return null;
	    	
	    	int sid = cdma.getSystemId();
            int bid = cdma.getBaseStationId();
            int nid = cdma.getNetworkId();
            
            currentCell.cellId = bid;
            currentCell.locationAreaCode = nid;
            currentCell.mobileNetworkCode = String.valueOf(sid);
            currentCell.mobileCountryCode = manager.getSimOperator().substring(0, 3);
            currentCell.radioType = "cdma";
            currentCell.signal_strength = -80;
            CellID.add(currentCell);
            
            return CellID;
		}
		return null;
	}
	
	/*private class MyPhoneStateListener extends PhoneStateListener{
		int dBm = 0;
		boolean flag = true;

		public MyPhoneStateListener() {
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			if (signalStrength.isGsm()) {
				int gsmSignalStrength = signalStrength.getGsmSignalStrength();
				int asu = (gsmSignalStrength == 99 ? -1 : gsmSignalStrength);
				if (asu != -1 && flag) {
					dBm = -113 + 2 * asu;
					flag = false;
				}
			} else {
				if (flag) {
					dBm = signalStrength.getCdmaDbm();
					flag = false;
				}
			}
		}

		public int getdBm() {
			return dBm;
		}
	};*/
	
	private int calcDMB(int signalStrength) {
		int dBm = -1;
		int asu = (signalStrength == 99 ? -1 : signalStrength);
		if (asu != -1) {
			dBm = -113 + 2 * asu;
		}
		return dBm;
	}
	
	public int getNetworkType() {
		return networkType;
	}
	
	public String getMnc() {
		return mnc;
	}
}
