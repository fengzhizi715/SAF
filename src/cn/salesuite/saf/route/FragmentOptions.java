/**
 * 
 */
package cn.salesuite.saf.route;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * @author Tony Shen
 *
 */
public class FragmentOptions {

	public FragmentManager fragmentManager;
	public Bundle mArg;
	public Fragment mFragmentInstnace = null;
	
	public FragmentOptions(FragmentManager fragmentManager,Fragment fragment) {
		this.fragmentManager = fragmentManager;
		this.mFragmentInstnace = fragment;
	}
	
	public FragmentOptions(FragmentManager fragmentManager,Fragment fragment,Bundle mArg) {
		this.fragmentManager = fragmentManager;
		this.mFragmentInstnace = fragment;
		this.mArg = mArg;
	}
}
