/**
 * 
 */
package cn.salesuite.saf.route;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import cn.salesuite.saf.route.RouterParameter.RouterOptions;

/**
 * 要使用Router功能时，必须在application中做好router的映射<br>
 * 从某个Activity跳转到SecondActivity需要传递user、password2个参数，需要做这样的映射
 * <pre>
 * <code>
 * Router.getInstance().map("user/:user/password/:password", SecondActivity.class);
 * </code>
 * </pre>
 * 跳转时可以增加动画效果
 * <pre>
 * <code>
 * RouterOptions options = new RouterOptions();
 * options.enterAnim = R.anim.slide_left_in;
 * options.exitAnim = R.anim.slide_right_out;
 * Router.getInstance().map("user/:user/password/:password", SecondActivity.class, options);
 * </code>
 * </pre>
 * Intent Router可以完成各个Intent之间的跳转，类似rails的router功能
 * @author Tony Shen
 *
 */
public class Router {
	
	public static final int DEFAULT_CACHE_SIZE = 1024;
	
	private Context context;
	private LruCache<String, RouterParameter> cachedRoutes = new LruCache<String, RouterParameter>(DEFAULT_CACHE_SIZE); // 缓存跳转的参数
	private final Map<String, RouterOptions> routes = new HashMap<String, RouterOptions>();                             // 存放Intent之间跳转的route
	
	private static final Router router = new Router();
	
	private Router() {
	}
	
	public static Router getInstance() {
		return router;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}
	
	/**
	 * @param format 形如"user/:user/password/:password"这样的格式，其中user、password为参数名
	 * @param clazz
	 */
	public void map(String format, Class<? extends Activity> clazz) {
		this.map(format, clazz, null);
	}

	public void map(String format, Class<? extends Activity> clazz,RouterOptions options) {
		if (options == null) {
			options = new RouterOptions();
		}
		options.clazz = clazz;
		this.routes.put(format, options);
	}

	/**
	 * 跳转到网页，如下：
	 * <pre>
	 * <code>
	 * Router.getInstance().openURI("http://www.g.cn");
	 * </code>
	 * </pre>
	 * 
	 * 调用系统电话，如下：
	 * <pre>
	 * <code>
	 * Router.getInstance().openURI("tel://18662430000");
	 * </code>
	 * </pre>
	 * 
	 * 调用手机上的地图app，打开地图，如下：
	 * <pre>
	 * <code>
	 * Router.getInstance().openURI("geo:0,0?q=31,121");
	 * </code>
	 * </pre>
	 * @param url
	 */
	public void openURI(String url) {
		this.openURI(url,this.context);
	}
	
	public void openURI(String url,Context context) {
		this.openURI(url, context, null);
	}
	
	public void openURI(String url,Context context,Bundle extras) {
		openURI(url,context,extras,Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	
	public void openURI(String url,Context context,Bundle extras, int flags) {
		openURI(url,context,extras,null,flags);
	}

	public void openURI(String url,Context context,Bundle extras, RouterPoint point, int flags) {
		if (context == null) {
			throw new RouterException("You need to supply a context for Router " + this.toString());
		}
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		this.addFlagsToIntent(intent, context, flags);
		if (extras != null) {
			intent.putExtras(extras);
		}
		
		// frankswu : 在start之前增加相应的接口可以做类似埋点的工作
		if (point != null) {
			point.beforeRouter(url, extras);
		}
		
		context.startActivity(intent);
		
		if (point != null) {
			point.afterRouter(url, extras);
		}
	}
	
	/**
	 * 跳转到某个activity并传值
	 * <pre>
	 * <code>
	 * Router.getInstance().open("user/fengzhizi715/password/715");
	 * </code>
	 * </pre>
	 * @param url
	 */
	public void open(String url) {
		this.open(url,null);
	}
	
	/**
	 * 跳转到某个activity并传值,router跳转前的先判断是否满足跳转的条件,doCheck()返回false表示不跳转，true表示进行跳转到下一个activity
	 * <pre>
	 * <code>
	 * Router.getInstance().open("user/fengzhizi715/password/715",new RouterChecker(){
     *
	 *	 public boolean doCheck() {
	 *		 return false;
	 *	 }
	 * });
	 * </code>
	 * </pre>
	 * @param url
	 * @param checker
	 */
	public void open(String url,RouterChecker checker) {
		this.open(url,this.context,checker);
	}
	
	public void open(String url,Context context,RouterChecker checker) {
		this.open(url, context, null,checker);
	}
	
	public void open(String url,Context context,RouterPoint point) {
		this.open(url, context, null ,point);
	}
	
	public void open(String url,Context context,Bundle extras) {
		open(url,context,extras,Intent.FLAG_ACTIVITY_NEW_TASK,null,null);    // 默认的跳转类型,将Activity放到一个新的Task中
	}
	
	public void open(String url,Context context,Bundle extras,RouterChecker checker) {
		open(url,context,extras,Intent.FLAG_ACTIVITY_NEW_TASK,checker,null); // 默认的跳转类型,将Activity放到一个新的Task中
	}
	
	public void open(String url,Context context,Bundle extras,RouterPoint point) {
		open(url,context,extras,Intent.FLAG_ACTIVITY_NEW_TASK,null,point);   // 默认的跳转类型,将Activity放到一个新的Task中
	}
	
	public void open(String url,Context context,Bundle extras,int flags,RouterChecker checker, RouterPoint point) {
		if (context == null) {
			throw new RouterException("You need to supply a context for Router "+ this.toString());
		}
		
		if (checker!=null && !checker.doCheck()) {
			return;
		}
		
		RouterParameter param = parseUrl(url);
		RouterOptions options = param.routerOptions;
		
		Intent intent = this.parseRouterParameter(param);
		if (intent == null) {
			return;
		}
		if (extras != null) {
			intent.putExtras(extras);
		}
		this.addFlagsToIntent(intent, context, flags);
		
		// frankswu : 在start之前增加相应的接口可以做类似埋点的工作
		if (point != null) {
			point.beforeRouter(url, extras);
		}
		
		context.startActivity(intent);
		
		if (point != null) {
			point.afterRouter(url, extras);
		}
		
		if (options.enterAnim>0 && options.exitAnim>0) {
			((Activity)context).overridePendingTransition(options.enterAnim, options.exitAnim);
		}
	}
	
	public void openFragment(FragmentOptions fragmentOptions,int containerViewId) {
		if (!(fragmentOptions != null
				&& fragmentOptions.mFragmentInstnace != null
				&& fragmentOptions.fragmentManager != null))
			return;
		
		if (fragmentOptions.mArg!=null) {
			fragmentOptions.mFragmentInstnace.setArguments(fragmentOptions.mArg);
		}
		
		fragmentOptions.fragmentManager.beginTransaction().replace(containerViewId , fragmentOptions.mFragmentInstnace).addToBackStack(null).commit();
	}
	
	public void openFragment(String url,FragmentOptions fragmentOptions,int containerViewId,RouterPoint point) {
		if (!(fragmentOptions != null
				&& fragmentOptions.mFragmentInstnace != null
				&& fragmentOptions.fragmentManager != null))
			return;
		
		Fragment fragment = fragmentOptions.mFragmentInstnace;
		fragment = parseFragmentUrl(url,fragmentOptions);
		
		// frankswu : 在fragment跳转之前增加相应的接口可以做类似埋点的工作
		if (point != null) {
			point.beforeRouter(url, fragmentOptions.mArg);
		}
		
		fragmentOptions.fragmentManager.beginTransaction().replace(containerViewId , fragment).addToBackStack(null).commit();
		
		if (point != null) {
			point.afterRouter(url, fragmentOptions.mArg);
		}
	}
	
	private Fragment parseFragmentUrl(String url, FragmentOptions fragmentOptions) {
		String[] givenParts = url.split("/");
		int length = givenParts.length;
		
		if (length > 0) {
			if (fragmentOptions.mArg==null) {
				fragmentOptions.mArg = new Bundle();
			}
			
			for (int i=0;i<length;i=i+2){
				fragmentOptions.mArg.putString(givenParts[i], givenParts[i+1]);
			}
			
			fragmentOptions.mFragmentInstnace.setArguments(fragmentOptions.mArg);
		}

		return fragmentOptions.mFragmentInstnace;
	}

	/**
	 * intent增加额外的flag
	 * @param intent
	 * @param context
	 * @param flags
	 */
	private void addFlagsToIntent(Intent intent, Context context,int flags) {
		intent.addFlags(flags);
	}

	private Intent parseRouterParameter(RouterParameter param) {
		RouterOptions options = param.routerOptions;
		Intent intent = new Intent();

		for (Entry<String, String> entry : param.openParams.entrySet()) {
			intent.putExtra(entry.getKey(), entry.getValue());
		}
		
		intent.setClass(context, options.clazz);
		
		return intent;
	}

	private RouterParameter parseUrl(String url) {
		if (this.cachedRoutes.get(url) != null) {
			return this.cachedRoutes.get(url);
		}

		String[] givenParts = url.split("/");

		RouterOptions openOptions = null;
		RouterParameter openParams = null;
		for (Entry<String, RouterOptions> entry : this.routes.entrySet()) {
			String routerUrl = entry.getKey();
			RouterOptions routerOptions = entry.getValue();
			String[] routerParts = routerUrl.split("/");

			if (routerParts.length != givenParts.length) {
				continue;
			}

			Map<String, String> givenParams = urlToParamsMap(givenParts, routerParts);
			if (givenParams == null) {
				continue;
			}

			openOptions = routerOptions;
			openParams = new RouterParameter();
			openParams.openParams = givenParams;
			openParams.routerOptions = routerOptions;
			break;
		}

		if (openOptions == null || openParams == null) {
			throw new RouterException("No route found for url " + url);
		}

		this.cachedRoutes.put(url, openParams);
		return openParams;
	}

	private Map<String, String> urlToParamsMap(String[] givenUrlSegments, String[] routerUrlSegments) {
		Map<String, String> formatParams = new HashMap<String, String>();
		int length = routerUrlSegments.length;
		for (int index = 0; index < length; index++) {
			String routerPart = routerUrlSegments[index];
			String givenPart = givenUrlSegments[index];

			if (routerPart.charAt(0) == ':') {
				String key = routerPart.substring(1, routerPart.length());
				formatParams.put(key, givenPart);
				continue;
			}

			if (!routerPart.equals(givenPart)) { // 奇数个参数进行参数名的匹配，如果不相等则退出
				return null;
			}
		}

		return formatParams;
	}
	
	/**
	 * 退出系统时，清空缓存数据
	 */
	public void clear() {
		cachedRoutes.evictAll();
	}
	
	/**
	 * 
	 * @author Tony Shen
	 *
	 */
	public interface RouterChecker {
		//router跳转前的先判断是否满足跳转的条件,false表示不跳转，true表示进行跳转到下一个activity
		boolean doCheck(); 
	}

	/**
	 * 提供埋点的便利接口，支持open、openURI和openFragment方法
	 * @author frankswu
	 *
	 */
	public interface RouterPoint {

		void beforeRouter(String url,Bundle extras);

		void afterRouter(String url,Bundle extras);

	}
}
