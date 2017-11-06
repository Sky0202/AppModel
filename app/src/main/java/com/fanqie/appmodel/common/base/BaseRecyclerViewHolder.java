package com.fanqie.appmodel.common.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 创建时间：2017/5/17 17:12  描述：
 * <p>
 * 基于 recyclerView 的 ViewHolder
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public BaseRecyclerViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }

    public static BaseRecyclerViewHolder get(Context context, ViewGroup parent, int layoutId) {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        BaseRecyclerViewHolder viewHolder = new BaseRecyclerViewHolder(context, itemView, parent);
        return viewHolder;

    }

    // 设置文字
    public BaseRecyclerViewHolder setText(int viewId, String text) {

        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    // 设置图片
    public BaseRecyclerViewHolder setImageResource(int viewId, int resId) {

        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    // 设置点击监听
    public BaseRecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {

        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }


    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;

    }

}
