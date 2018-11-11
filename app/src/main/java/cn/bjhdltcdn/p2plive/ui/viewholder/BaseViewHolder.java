package cn.bjhdltcdn.p2plive.ui.viewholder;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.ui.listener.LongItemListener;

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

	private ItemListener itemListener ;
	private LongItemListener longListener ;

	public ItemListener getItemListener() {
		return itemListener;
	}

	public void setItemListener(ItemListener itemListener) {
		this.itemListener = itemListener;
	}

	public LongItemListener getLongListener() {
		return longListener;
	}

	public void setLongListener(LongItemListener longListener) {
		this.longListener = longListener;
	}

	public BaseViewHolder(View itemView) {
		super(itemView);
		// TODO Auto-generated constructor stub
		if(itemView != null){
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		if(getLongListener() != null){
			longListener.onItemLongClick(v, getLayoutPosition());
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(getItemListener() != null){
			itemListener.onItemClick(v, getLayoutPosition());
		}
	}



}
