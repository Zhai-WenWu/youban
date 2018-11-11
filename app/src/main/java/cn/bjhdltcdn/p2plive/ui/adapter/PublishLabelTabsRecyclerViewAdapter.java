package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 房间标签列表
 */

public class PublishLabelTabsRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<LabelInfo> list;

    public PublishLabelTabsRecyclerViewAdapter() {
        list = new ArrayList<>(1);
    }

    public void setList(List<LabelInfo> list) {
        this.list = list;
        if (list != null) {
            this.list = list;
        }
    }

    public List<LabelInfo> getList() {
        return list;
    }


    public LabelInfo getItem(int position) {
        return list == null ? null : list.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.public_label_tab_item_layout4, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                final TextView tabTextView = imageHolder.tabTextView;

                final LabelInfo keywordInfo = list.get(position);

                String labelName = keywordInfo.getLabelName();
                tabTextView.setText(labelName);
                tabTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));

                if (keywordInfo.getIsSelect() == 1) {
                    tabTextView.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);
                } else {
                    tabTextView.setBackgroundResource(R.drawable.shape_round_20_stroke_d8d8d8_solid_fafafa);
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImageHolder extends BaseViewHolder {
        TextView tabTextView;

        public ImageHolder(View itemView) {
            super(itemView);
            tabTextView = itemView.findViewById(R.id.tv_label);
        }

    }

    public void onDestroy() {
        if (list != null) {
            list.clear();
        }
        list = null;
    }

    public interface OnClickListener {
        void onClick(int position);
    }


}