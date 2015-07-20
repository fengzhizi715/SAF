package cn.salesuite.saf.download;

import java.io.File;
import java.net.MalformedURLException;

import cn.salesuite.saf.utils.SAFUtils;
import cn.salesuite.saf.utils.ToastUtils;
import android.content.Context;

/**
 * 单downloadtask的manager
 * 
 *
 */
public class DownloadManager {
	
	public enum Status {
		COMPLETE,FAILED,PROGRESS,PAUSE,WAIT;
    }

	private static DownloadManager manager;
	private Context mContext;
	private DownloadTask task;

	private DownloadManager(Context context) {
		this.mContext = context;
	}
	
	public static DownloadManager getInstance(Context context) {
		if (manager == null) {
			manager = new DownloadManager(context);
		}
		return manager;
	}
	
	public synchronized void startDownload(String url, String path, String fileName, DownloadTaskListener listener) {
		if (!SAFUtils.hasSdcard()) {
			ToastUtils.showShort(mContext, "未发现SD卡");
			return;
		}
		
		File file = new File(path + fileName);
		if (file.exists())
			file.delete();
		try {
			task = new DownloadTask(mContext, url,	path, fileName, listener);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		task.execute();
	}

	public synchronized void pauseDownload() {
		if (task != null) {
			task.onCancelled();
		}
	}

	public synchronized void continueDownload(String url, String path, String fileName, DownloadTaskListener listener) {
		task = null;
		try {
			task = new DownloadTask(mContext, url,	path, listener);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		}
		task.execute();
	}
}
