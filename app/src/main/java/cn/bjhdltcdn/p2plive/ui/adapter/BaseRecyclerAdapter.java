package cn.bjhdltcdn.p2plive.ui.adapter;


import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.ui.listener.LongItemListener;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 使用注意事项：
 * 子类在实现onBindViewHolder()方法时，
 * 需要子类调用super.onBindViewHolder(holder, position);
 * 目的是为了设置监听器，如果不需要监听器可以忽略。
 */
public class BaseRecyclerAdapter extends Adapter<ViewHolder> {

    private ItemListener onItemListener;
    private LongItemListener onLongListener;

    public ItemListener getOnItemListener() {
        return onItemListener;
    }

    public void setOnItemListener(ItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public LongItemListener getOnLongListener() {
        return onLongListener;
    }

    public void setOnLongListener(LongItemListener onLongListener) {
        this.onLongListener = onLongListener;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).setItemListener(getOnItemListener());
            ((BaseViewHolder) holder).setLongListener(getOnLongListener());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        return null;
    }


}
