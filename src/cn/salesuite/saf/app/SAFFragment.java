/**
 * 
 */
package cn.salesuite.saf.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.salesuite.saf.utils.SAFUtils;
import cn.salesuite.saf.utils.ToastUtils;

/**
 * @author Tony Shen
 *
 */
public class SAFFragment extends Fragment {

	/**
     * Fragment 所在的 FragmentActivity
     */
	public Activity mContext;
    
    public String TAG;
    
	@Override
    public void onAttach(Activity activity) {
		super.onAttach(activity);
        this.mContext = activity;
    }
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        TAG = SAFUtils.makeLogTag(this.getClass());
	}
    
	/**
	 * @param message toast的内容
	 */
	protected void toast(String message) {
		ToastUtils.showShort(mContext, message);
	}

	/**
	 * @param resId toast的内容来自String.xml
	 */
	protected void toast(int resId) {
		ToastUtils.showShort(mContext, resId);
	}
}
