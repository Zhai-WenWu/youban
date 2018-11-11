package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by ZHUDI on 2017/9/22.
 */

public class PostLabelListAdapter extends BaseRecyclerAdapter {
    private List<PostLabelInfo> labelInfoList;
    private String editStr;

    public void setDate(String editStr, List<PostLabelInfo> labelInfoList) {
        this.editStr = editStr;
        this.labelInfoList = labelInfoList;
        notifyDataSetChanged();
    }

    public List<PostLabelInfo> getLabelInfoList() {
        return labelInfoList;
    }

    @Override
    public int getItemCount() {
        return labelInfoList != null ? labelInfoList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_post_label, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                PostLabelInfo postLabelInfo = labelInfoList.get(position);
                if (!TextUtils.isEmpty(postLabelInfo.getLabelName())) {
                    if (postLabelInfo.getPostLabelId() == 0) {
                        itemViewHolder.labelNameView.setTextColor(App.getInstance().getResources().getColor(R.color.color_ffb700));
                        itemViewHolder.labelNumView.setText("生成自定义标签");
                        itemViewHolder.labelNameView.setText("#" + postLabelInfo.getLabelName());
                    } else {
                        SpannableString labelName = StringUtils.matcherSearchTitle(App.getInstance().getResources().getColor(R.color.color_ffb700),  postLabelInfo.getLabelName(), editStr);
                        itemViewHolder.labelNumView.setText(postLabelInfo.getTotal() + "条发布");
                        itemViewHolder.labelNameView.setText(labelName);
                    }

                }
            }
        }

    }

    public void addDate(List<PostLabelInfo> labelInfoList) {
        this.labelInfoList.addAll(labelInfoList);
        notifyDataSetChanged();
    }

    class ItemViewHolder extends BaseViewHolder {

        TextView labelNameView;
        TextView labelNumView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            labelNameView = itemView.findViewById(R.id.tv_label_name);
            labelNumView = itemView.findViewById(R.id.tv_label_total);
        }
    }

}
