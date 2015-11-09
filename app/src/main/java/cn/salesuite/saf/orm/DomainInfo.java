/**
 * 
 */
package cn.salesuite.saf.orm;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import cn.salesuite.saf.utils.ReflectionUtils;
import cn.salesuite.saf.utils.SAFUtils;

/**
 * @author Tony Shen
 *
 */
public final class DomainInfo {

	public static final String TAG = "DomainInfo";
	private final static String DOMAIN_PACKAGE = "DOMAIN_PACKAGE";
	
	private Map<Class<? extends DBDomain>, TableInfo> mTableInfos = new HashMap<Class<? extends DBDomain>, TableInfo>();
	
	public DomainInfo(Application app) {
		if (loadDomainFromMetaData(app)) {
			Log.i(TAG,"DomainInfo loaded successfully.");
		} else {
			Log.i(TAG,"DomainInfo loaded failure.");
		}
	}
	
	private boolean loadDomainFromMetaData(Application app) {
		String domainPackage = SAFUtils.getMetaData(app, DOMAIN_PACKAGE);
		
		if (!TextUtils.isEmpty(domainPackage)) {
			loadDomainList(app, domainPackage);
		}

		return mTableInfos.size() > 0;
	}

	private void loadDomainList(Application app, String domainPackage) {
		final ClassLoader classLoader = app.getClass().getClassLoader();
		try {
			List<String>  domains = ReflectionUtils.getPackageAllClassName(app, domainPackage);
			if (domains!=null && domains.size()>0) {
				for(String domain:domains) {
					Class domainClass = Class.forName(domain, false, classLoader);
					mTableInfos.put(domainClass, new TableInfo(domainClass));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Collection<TableInfo> getTableInfos() {
		return mTableInfos.values();
	}

	public TableInfo getTableInfo(Class<? extends DBDomain> type) {
		return mTableInfos.get(type);
	}

	@SuppressWarnings("unchecked")
	public List<Class<? extends DBDomain>> getModelClasses() {
		return (List<Class<? extends DBDomain>>) mTableInfos.keySet();
	}

}
