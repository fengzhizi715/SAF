package cn.salesuite.saf.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Tony Shen on 16/2/2.
 */
public abstract class SAFRecyclerAdapter <M, VH extends SAFViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<M> mList;

    @Override
    public void onBindViewHolder(SAFViewHolder holder, int position)  {

        bindCustomViewHolder((VH) holder, position);
    }

    /**
     * 绑定自定义的ViewHolder
     *
     * @param holder ViewHolder
     * @param position 位置
     */
    protected abstract void bindCustomViewHolder(VH holder, int position);

    @Override
    public int getItemCount() {

        return mList == null ? 0 : mList.size();
    }

    public List<M> getList() {
        return mList;
    }
}
