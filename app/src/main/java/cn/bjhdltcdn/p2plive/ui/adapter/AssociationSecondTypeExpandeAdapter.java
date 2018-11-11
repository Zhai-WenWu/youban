package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by xiawenquan on 17/11/29.
 */

public class AssociationSecondTypeExpandeAdapter extends BaseRecyclerAdapter {

    private List<HobbyInfo> list;

    public AssociationSecondTypeExpandeAdapter(List<HobbyInfo> list) {
        this.list = list;
    }

    public List<HobbyInfo> getList() {
        return list;
    }

    public void setList(List<HobbyInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ItemViewHolder) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            HobbyInfo hobbyInfo = list.get(position);

            if (hobbyInfo.getIsSelect() == 1) {// 已选中
                itemViewHolder.textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                itemViewHolder.textView.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);

            } else {
                itemViewHolder.textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                itemViewHolder.textView.setBackgroundResource(R.drawable.shape_round_40_stroke_d8d8d8_solid_fafafa);
            }

            itemViewHolder.textView.setText(hobbyInfo.getHobbyName());

            if (hobbyInfo.getSecondInterestType() == 2) {
                itemViewHolder.popView.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.popView.setVisibility(View.GONE);
            }


        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = View.inflate(App.getInstance(), R.layout.organization_second_list_item_layout, null);

        return new ItemViewHolder(itemView);
    }


    static class ItemViewHolder extends BaseViewHolder {

        public TextView textView;
        public ImageView popView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
            popView = itemView.findViewById(R.id.pop_view);

        }
    }

}
