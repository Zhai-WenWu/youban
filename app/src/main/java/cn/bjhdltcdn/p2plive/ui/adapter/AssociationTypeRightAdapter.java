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
 * Created by xiawenquan on 17/11/20.
 */

public class AssociationTypeRightAdapter extends BaseRecyclerAdapter {

    private List<HobbyInfo> list;

    public AssociationTypeRightAdapter(List<HobbyInfo> list) {
        this.list = list;
    }


    public List<HobbyInfo> getList() {
        return list;
    }

    public void setList(List<HobbyInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(HobbyInfo hobbyInfo) {
        if (this.list == null) {
            return;
        }
        this.list.add(hobbyInfo);
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
            if (hobbyInfo.getHobbyId() == -1) { // 添加item

                itemViewHolder.layoutView1.setVisibility(View.INVISIBLE);
                itemViewHolder.layoutView2.setVisibility(View.VISIBLE);


            } else {

                itemViewHolder.layoutView1.setVisibility(View.VISIBLE);
                itemViewHolder.layoutView2.setVisibility(View.INVISIBLE);

                // 名称
                itemViewHolder.textView.setText(hobbyInfo.getHobbyName());

                // 圈体个数
                itemViewHolder.textView2.setText(hobbyInfo.getTotal() + "个圈子");

                int visibility = View.INVISIBLE;
                int drawableId = 0;
                if (list.get(position).isLocalSelectItem()) {
                    drawableId = R.mipmap.selected_icon;
                    visibility = View.VISIBLE;
                }

                itemViewHolder.imageView.setImageResource(drawableId);
                itemViewHolder.imageView.setVisibility(visibility);

            }

            // new icon
            if (hobbyInfo.getSecondInterestType() == 2) {
                itemViewHolder.popView.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.popView.setVisibility(View.INVISIBLE);
            }


        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = View.inflate(App.getInstance(), R.layout.adapter_association_type_list_item_add_layout, null);
        ItemViewHolder holder = new ItemViewHolder(itemView);

        return holder;
    }


    static class ItemViewHolder extends BaseViewHolder {

        private ImageView imageView;
        private TextView textView;
        private TextView textView2;
        private View popView;

        private View layoutView1;
        private View layoutView2;

        public ItemViewHolder(View itemView) {
            super(itemView);

            layoutView1 = itemView.findViewById(R.id.layout_view_1);
            layoutView2 = itemView.findViewById(R.id.layout_view_2);

            imageView = itemView.findViewById(R.id.image_view);

            textView = itemView.findViewById(R.id.text_view);
            textView2 = itemView.findViewById(R.id.text_view_2);
            popView = itemView.findViewById(R.id.pop_view);


        }
    }
}
