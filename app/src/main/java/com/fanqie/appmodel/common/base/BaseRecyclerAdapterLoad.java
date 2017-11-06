package com.fanqie.appmodel.common.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * adapter的基类
 */
public abstract class BaseRecyclerAdapterLoad<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public BaseRecyclerAdapterLoad(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mDatas = datas;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        BaseRecyclerViewHolder viewHolder = BaseRecyclerViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {

        convert(holder, position);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(BaseRecyclerViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
