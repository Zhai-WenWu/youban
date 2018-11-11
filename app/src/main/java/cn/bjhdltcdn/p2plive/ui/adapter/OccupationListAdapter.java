package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.OccupationIndexInfo;
import cn.bjhdltcdn.p2plive.model.OccupationInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by ZHUDI on 2017/9/22.
 */

public class OccupationListAdapter extends BaseRecyclerAdapter {
    private List<OccupationInfo> workInfoList;
    private int indexPositon;

    public void setDate(List<OccupationInfo> workInfoList) {
        this.workInfoList = workInfoList;
        int clickedIndex = OccupationIndexInfo.getInstent().getIndex();
        if (clickedIndex >= 0) {
            for (int i = 0; i < workInfoList.size(); i++) {
                workInfoList.get(i).setIsSelect(0);
            }
            workInfoList.get(clickedIndex).setIsSelect(1);
        }
        notifyDataSetChanged();
    }

    public void setIndexPositon(int indexPositon) {
        this.indexPositon = indexPositon;
    }
    public int getIndexPositon() {
        return indexPositon;
    }

    public List<OccupationInfo> getWorkInfoList() {
        return workInfoList;
    }

    @Override
    public int getItemCount() {
        return workInfoList != null ? workInfoList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.item_list_occupation, null);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final OccupationInfo occupationInfo = workInfoList.get(position);
                itemViewHolder.tv_occupation.setText(occupationInfo.getOccupationName());

                if (occupationInfo.getIsSelect() == 0) {
//                    itemViewHolder.img_occupation.setVisibility(View.GONE);
                    itemViewHolder.img_occupation.setSelected(false);
                } else {
                    indexPositon = position;
                    itemViewHolder.img_occupation.setSelected(true);
//                    itemViewHolder.img_occupation.setVisibility(View.VISIBLE);
                }

//                itemViewHolder.setItemListener(new ItemListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        if (occupationInfo != null) {
//                            workInfoList.get(indexPositon).setIsSelect(0);
//                            occupationInfo.setIsSelect(1);
//
//                            EventBus.getDefault().post(new OccuPationClickEvent());
//                            notifyDataSetChanged();
//                        }
//                    }
//                });
            }
        }

    }


    public class ItemViewHolder extends BaseViewHolder {
        RelativeLayout rela_occupation;
        TextView tv_occupation;
        ImageView img_occupation;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rela_occupation = itemView.findViewById(R.id.rela_occupation);
            tv_occupation = itemView.findViewById(R.id.tv_occupation);
            img_occupation = itemView.findViewById(R.id.img_occupation);
        }
    }

}
