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
import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 发布帖子已选择标签
 */

public class PublishLabelRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<PostLabelInfo> selectPostLabelInfoList;
    private int isSelectCount;

    public PublishLabelRecyclerViewAdapter() {
        selectPostLabelInfoList = new ArrayList<>(1);
    }

    public void setSelectPostLabelInfoList(List<PostLabelInfo> selectPostLabelInfoList) {
        this.selectPostLabelInfoList = selectPostLabelInfoList;
        notifyDataSetChanged();
    }

    public List<PostLabelInfo> getSelectPostLabelInfoList() {
        return selectPostLabelInfoList;
    }


    /**
     * 获取选中的数量
     *
     * @return
     */
    public int getSelectItemCount() {
        isSelectCount = 0;
        if (getItemCount() > 0) {
            for (PostLabelInfo info : selectPostLabelInfoList) {
                if (info.getIsSelect() == 1) {
                    isSelectCount++;
                }
            }
        }
        return isSelectCount;
    }


    public PostLabelInfo getItem(int position) {
        return selectPostLabelInfoList == null ? null : selectPostLabelInfoList.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.public_label_select_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                ImageHolder imageHolder = (ImageHolder) holder;
                TextView tabTextView = imageHolder.tabTextView;

                PostLabelInfo postLabelInfo = selectPostLabelInfoList.get(position);

                String labelName = postLabelInfo.getLabelName();
                tabTextView.setText("# " + labelName + "  X");

            }
        }

    }

    @Override
    public int getItemCount() {
        return selectPostLabelInfoList == null ? 0 : selectPostLabelInfoList.size();
    }

    class ImageHolder extends BaseViewHolder {
        TextView tabTextView;

        public ImageHolder(View itemView) {
            super(itemView);
            tabTextView = itemView.findViewById(R.id.tv_label);
        }

    }


    public void onDestroy() {
        if (selectPostLabelInfoList != null) {
            selectPostLabelInfoList.clear();
        }
        selectPostLabelInfoList = null;
    }

    public interface OnClickListener {
        void onClick(int position);
    }


}