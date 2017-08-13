package com.jackeysun.library.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackey on 2017/7/23.
 */

public abstract class AbsAdapter<T, H extends AbsViewHolder> extends BaseAdapter {

    protected List<T> mDatas;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected int layoutId;
    protected H holder;

    public AbsAdapter(Context context, int layoutId) {
        this.mContext = context;
        this.mDatas = new ArrayList<>();
        this.layoutId = layoutId;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    protected void setItem(List<T> mDatas) {
        this.mDatas.clear();
        for (T data : mDatas) {
            this.mDatas.add(data);
        }
        this.notifyDataSetChanged();
    }

    protected void addItems(List<T> mDatas) {
        for (T data : mDatas) {
            addItem(data);
        }
        this.notifyDataSetChanged();
    }

    private void addItem(T data) {
        mDatas.add(data);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        holder = null;
        if (view == null) {
            holder = newViewHolder();
            view = mLayoutInflater.inflate(layoutId, null);
            view.setTag(holder);
            holder.initItemView(view);
        } else {
            holder = (H) view.getTag();
        }
        initData(mDatas.get(i), holder);
        return view;
    }

    protected abstract void initData(T data, H holder);


    public abstract H newViewHolder();
}
