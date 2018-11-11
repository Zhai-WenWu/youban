package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.model.SecondLabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

public class PostLabelInfoRecycleAdapter extends BaseRecyclerAdapter {
    private List<PostLabelInfo> list;
    private float paddingRight;
    /**
     * type为1时 是首页热门内容标签
     */
    private int type;

    public PostLabelInfoRecycleAdapter(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    public PostLabelInfoRecycleAdapter(float paddingRight, int type) {
        this.paddingRight = paddingRight;
        this.type = type;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void setList(List<PostLabelInfo> list) {
        this.list = list;
    }


    public PostLabelInfo getItem(int position) {
        // TODO Auto-generated method stub
        if (list != null && list.size() > 0) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * @return the mList
     */
    public List<PostLabelInfo> getList() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView;
        if (type == 1) {
            itemView = View.inflate(App.getInstance(), R.layout.public_label_tab_item_layou2, null);
        } else {
            itemView = View.inflate(App.getInstance(), R.layout.post_labelinfo_recycle_item_layout, null);
        }
        return new ItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {

            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                PostLabelInfo postLabelInfo = list.get(position);
                itemViewHolder.labelName.setText("# " + postLabelInfo.getLabelName());
                if (type == 1) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) itemViewHolder.labelName.getLayoutParams();
                    layoutParams.setMargins(0,0,Utils.dp2px(paddingRight),0);
                    itemViewHolder.labelName.setLayoutParams(layoutParams);
                } else {
                    itemViewHolder.labelName.setPadding(0, 0, Utils.dp2px(paddingRight), 0);
                }
            }

        }

    }

    class ItemViewHolder extends BaseViewHolder {
        TextView labelName;

        public ItemViewHolder(View itemView) {
            super(itemView);
                labelName = itemView.findViewById(R.id.post_label_view);
        }
    }


}
