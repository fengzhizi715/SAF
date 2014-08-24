/**
 * 
 */
package cn.salesuite.saf.imagecache;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import cn.salesuite.saf.executor.concurrent.BackgroundExecutor;
import cn.salesuite.saf.utils.BitmapHelper;

/**
 * @author Tony Shen
 *
 * 
 */
public class ImageLoader {
	
	MemoryCache memoryCache;
	DiskLruImageCache diskCache;
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    BackgroundExecutor backgroundExecutor;
    int stub_id;
    Handler handler=new Handler();
    private Context mContext;
    private final PriorityBlockingQueue<ImageRequest> mQueue = new PriorityBlockingQueue<ImageRequest>();
    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private boolean enableDiskCache = true;

	public ImageLoader(Context context,int default_img_id){
    	memoryCache = new MemoryCache();
    	diskCache = new DiskLruImageCache(context);
    	backgroundExecutor = new BackgroundExecutor(8);
        stub_id = default_img_id;
        this.mContext = context;
    }
    
    public ImageLoader(Context context,int default_img_id,String fileDir){
    	memoryCache = new MemoryCache();
    	diskCache = new DiskLruImageCache(context,fileDir);
    	backgroundExecutor = new BackgroundExecutor(8);
        stub_id = default_img_id;
        this.mContext = context;
    }
    
    /**
     * 显示图片，如果未能获取图片，则显示全局的默认图片
     * @param url
     * @param imageView
     */
    public void displayImage(String url, ImageView imageView) {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }
    
    /**
     * 显示图片，可自定义默认显示的图片
     * @param url
     * @param imageView
     * @param imageId 默认图片，可能区别与default_img_id，一个app只有一个default_img_id，imageId可有很多个
     */
    public void displayImage(String url, ImageView imageView, int imageId) {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else {
            queuePhoto(url, imageView, imageId);
            imageView.setImageResource(imageId);
        }
    }
    
    /**
     * 显示图片，可自定义默认显示的图片
     * @param url
     * @param imageView
     * @param options 图片带JobOptions选项，可变成画圆角图形
     */
    public void displayImage(String url, ImageView imageView, final JobOptions options) {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null) {
        	imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(url, imageView, stub_id,options);
            imageView.setImageResource(stub_id);
        }
    }

    /**
     * 显示图片，可自定义默认显示的图片
     * @param url
     * @param imageView
     * @param imageId
     * @param options 图片带JobOptions选项，可变成画圆角图形
     */
    public void displayImage(String url, ImageView imageView, int imageId,final JobOptions options) {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null) {
        	imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(url, imageView, imageId,options);
            imageView.setImageResource(imageId);
        }
    }
    
    /**
     * 根据url，删除cache里的图片
     * @param url
     */
    public void remove(String url) {
    	memoryCache.remove(url);
        String key = getDiskCacheKey(url);
        if (key==null) {
        	return;
        }
    	diskCache.remove(key);
    }
    
    private void queuePhoto(String url, ImageView imageView) {
        ImageRequest p=new ImageRequest(url, imageView);
        mQueue.add(p);
        backgroundExecutor.submit(new PhotosLoader());
    }
    
    private void queuePhoto(String url, ImageView imageView, int imageId) {
        ImageRequest p=new ImageRequest(url, imageView, imageId);
        mQueue.add(p);
        backgroundExecutor.submit(new PhotosLoader());
    }
    
    private void queuePhoto(String url, ImageView imageView, int imageId,
			JobOptions options) {
        ImageRequest p=new ImageRequest(url, imageView, imageId, options);
        mQueue.add(p);
        backgroundExecutor.submit(new PhotosLoader());
	}
    
    public Bitmap getBitmap(String url,ImageView imageView) {
        //from SD cache
        Bitmap b = getBitmapFromDiskCache(url);
        if(b!=null)
            return b;
        
        //from web
        downloadBitmap(url,new JobOptions(imageView));
        return memoryCache.get(url);
    }
    
    private Bitmap getBitmapFromDiskCache(final String urlString) {
    	if (enableDiskCache) {
            final String key = getDiskCacheKey(urlString);
            
            if (key==null) {
            	return null;
            }
            
            final Bitmap cachedBitmap = diskCache.getBitmap(key);

            if (cachedBitmap == null)
                return null;

            return cachedBitmap;
    	} else {
    		return null;
    	}

    }

    private static String getDiskCacheKey(final String urlString) {
        final String sanitizedKey = urlString.replaceAll("[^a-z0-9_]", "");
        if (sanitizedKey.length()>63) { // sanitizedKey > 63时，不用sd卡缓存用内存缓存。
        	return null;
        }
        return sanitizedKey.substring(0, Math.min(63, sanitizedKey.length()));
    }
    
    private void downloadBitmap(final String urlString, final JobOptions options) {
        final BitmapProcessor processor = new BitmapProcessor(mContext);
        final Bitmap bitmap = processor.decodeSampledBitmapFromUrl(urlString, options.requestedWidth, options.requestedHeight);

        if (bitmap == null) {
            Log.e("ImageLoader", "download Drawable got null");
            return;
        }

        addBitmapToCache(urlString, bitmap);
    }
    
    private void addBitmapToCache(final String key, final Bitmap bitmap) {
        memoryCache.put(key, bitmap);

        if (!enableDiskCache) {
            final String diskCacheKey = getDiskCacheKey(key);

            if ((diskCache != null) && !diskCache.containsKey(diskCacheKey)) {
                diskCache.put(diskCacheKey, bitmap);
            }
        }
    }
    
    //ImageRequest
    private class ImageRequest implements Comparable<ImageRequest>{
        public String url;
        public ImageView imageView;
        public int imageId;
        public JobOptions options;
        public Integer mSequence;
        
        public ImageRequest(String u, ImageView i){
            url=u; 
            imageView=i;
            this.imageId = stub_id;
            this.mSequence = getSequenceNumber();
        }
        
        public ImageRequest(String u, ImageView i, int imageId){
            url=u; 
            imageView=i;
            this.imageId = imageId;
            this.mSequence = getSequenceNumber();
        }
        
        public ImageRequest(String u, ImageView i, int imageId, JobOptions options){
            url=u; 
            imageView=i;
            this.imageId = imageId;
            this.options = options;
            this.mSequence = getSequenceNumber();
        }

		@Override
		public int compareTo(ImageRequest other) {
	        return this.mSequence - other.mSequence;
		}
    }
    
    class PhotosLoader implements Runnable {
        ImageRequest request;
        
        @Override
        public void run() {
        	try {
				request = mQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
            if(imageViewReused(request))
                return;
            Bitmap bmp=getBitmap(request.url,request.imageView);
            if (request.options!=null) {
            	memoryCache.put(request.url, BitmapHelper.roundCorners(bmp , request.options.cornerRadius));
            } else {
            	memoryCache.put(request.url, bmp);
            }
            if(imageViewReused(request))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, request);
            handler.post(bd);
        }
    }
    
    boolean imageViewReused(ImageRequest photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageRequest photoToLoad;
        
        public BitmapDisplayer(Bitmap b, ImageRequest p){
        	bitmap=b;
        	photoToLoad=p;
        }
        
        public void run(){
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null) {
            	if (photoToLoad.options!=null) {
            		photoToLoad.imageView.setImageBitmap(BitmapHelper.roundCorners(bitmap , photoToLoad.options.cornerRadius));
            	} else {
            		photoToLoad.imageView.setImageBitmap(bitmap);
            	}
            } 
            else
                photoToLoad.imageView.setImageResource(photoToLoad.imageId);
        }
    }
    
    public static class JobOptions {

        public int cornerRadius = 5;
        public int requestedWidth;
        public int requestedHeight;

        public JobOptions() {
            this(0, 0);
        }

        public JobOptions(final ImageView imgView) {
            this(imgView.getWidth(), imgView.getHeight());
        }

        public JobOptions(final int requestedWidth, final int requestedHeight) {
            this.requestedWidth = requestedWidth;
            this.requestedHeight = requestedHeight;
        }
    }

    /**
     * Sequence生成器
     * @return
     */
    public int getSequenceNumber() {
        return mSequenceGenerator.incrementAndGet();
    }
    
    /**
     * 清空所有缓存
     */
    public void clearCache() {
        memoryCache.clear();
        diskCache.clearCache();
    }
    
    /**
     * 清空内存中的缓存
     */
    public void clearMemCache() {
    	memoryCache.clear();
    }

	public MemoryCache getMemoryCache() {
		return memoryCache;
	}
	
    public boolean isEnableDiskCache() {
		return enableDiskCache;
	}

	public void setEnableDiskCache(boolean enableDiskCache) {
		this.enableDiskCache = enableDiskCache;
	}
}