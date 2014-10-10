/**
 * 
 */
package cn.salesuite.saf.app;

import java.util.List;

import android.widget.BaseAdapter;

/**
 * @author Tony Shen
 *
 */
public abstract class SAFAdapter<T> extends BaseAdapter {

	private List<T> mList = null;
	
	public List<T> getList(){
		return mList;
	}
	
	public void setData(List<T> list) {
		if (list == null) {
			return;
		}
		
		mList = list;
		notifyDataSetChanged();
	}
	
	public void add(List<T> list) {
		if (list == null) {
			return;
		}
		
		if (mList == null) {
			mList = list;
			notifyDataSetChanged();
			return;
		}
		
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void addToTopList(List<T> list) {
		if (list == null) {
			return;
		}
		
		if (mList == null) {
			mList = list;
			notifyDataSetChanged();
			return;
		}
		
		mList.addAll(0, list);
		notifyDataSetChanged();
	}

	public void clear() {
		if (mList!=null) {
			mList.clear();
		}

		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if (mList!=null) {
			return mList.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
	    if (mList == null) {
	    	return null;
	    }
		if(position > mList.size()-1){
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
