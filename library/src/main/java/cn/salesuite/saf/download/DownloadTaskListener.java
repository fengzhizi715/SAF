package cn.salesuite.saf.download;


public abstract class DownloadTaskListener {

	public DownloadManager.Status lastDownloadStatus = null;
	public int lastProgress = -1;
	
	public abstract void pauseProcess(DownloadTask mgr);		    //下载进度暂停
    public abstract void updateProcess(DownloadTask mgr);			// 更新进度
    public abstract void finishDownload(DownloadTask mgr);			// 完成下载
    public abstract void preDownload(DownloadTask mgr);			    // 准备下载
    public abstract void errorDownload(DownloadTask mgr, int error);// 下载错误
}
