package cn.salesuite.saf.recyclerview;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Tony Shen on 2016/11/27.
 */

public abstract class OnItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnItemClickListener(RecyclerView view) {
        this.recyclerView = view;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView.ViewHolder holder, int position);

    public abstract void onLongPress(RecyclerView.ViewHolder holder, int position);

    class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                int position = recyclerView.indexOfChild(child);
                onItemClick(recyclerView.getChildViewHolder(child), position);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                int position = recyclerView.indexOfChild(child);
                OnItemClickListener.this.onLongPress(recyclerView.getChildViewHolder(child), position);
            }
        }
    }
}
