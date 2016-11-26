package cn.salesuite.saf.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Tony Shen on 16/2/2.
 */
public abstract class SAFRecyclerAdapter <M, VH extends SAFViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<M> mList;
}
