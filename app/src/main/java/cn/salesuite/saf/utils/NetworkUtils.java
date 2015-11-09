/**
 * 
 */
package cn.salesuite.saf.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author Tony Shen
 * 
 */
public class NetworkUtils {

	/**
	 * 获得本机ip，没有联网 return null
	 * 
	 * @return
	 */
	public static String getIpAddress() {
		try {
			String ipv4;
			List<NetworkInterface> nilist = Collections.list(NetworkInterface
					.getNetworkInterfaces());
			for (NetworkInterface ni : nilist) {
				List<InetAddress> ialist = Collections.list(ni
						.getInetAddresses());
				for (InetAddress address : ialist) {
					if (!address.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ipv4 = address
									.getHostAddress())) {
						return ipv4;
					}
				}

			}

		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取wifi ip地址
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getWifiIpAddress(Context mContext) {
		WifiInfo localWifiInfo = null;
		if (mContext != null) {
			localWifiInfo = ((WifiManager) mContext.getSystemService("wifi"))
					.getConnectionInfo();
			if (localWifiInfo != null) {
				String str = convertIntToIp(localWifiInfo.getIpAddress());
				return str;
			}
		}
		return "";
	}
	
	private static String convertIntToIp(int paramInt) {
		return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "."
				+ (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
	}
}
