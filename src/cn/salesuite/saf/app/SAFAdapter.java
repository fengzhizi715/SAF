/**
 * 
 */
package cn.salesuite.saf.app;

import java.util.ArrayList;
import java.util.List;

import cn.salesuite.saf.inject.Injector;

import android.view.View;
import android.widget.BaseAdapter;

/**
 * @author Tony Shen
 *
 */
public abstract class SAFAdapter<T> extends BaseAdapter {

	protected List<T> mList = null;
	
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
	
	public void add(T t) {
		if (t == null) {
			return;
		}
		
		if (mList == null) {
			mList = new ArrayList<T>();
		}
		
		mList.add(t);
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
	
	public void addToTop(T t) {
		if (t == null) {
			return;
		}
		
		if (mList == null) {
			mList = new ArrayList<T>();
		}
		
		mList.add(0, t);
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
	
	/**
	 * adapter中的viewholder继承SAFViewHolder就可以用@InjectView
	 * @author Tony Shen
	 *
	 */
	protected class SAFViewHolder {
        public SAFViewHolder(View convertView) {
			Injector.injectInto(this, convertView);
		}
	}
}
