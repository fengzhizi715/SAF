/**
 * 
 */
package cn.salesuite.saf.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.salesuite.saf.utils.SAFUtil;

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
        
        TAG = SAFUtil.makeLogTag(this.getClass());
	}
}
