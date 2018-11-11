package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.SelectAddressBookEvent;
import cn.bjhdltcdn.p2plive.model.BlackUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.ui.activity.ConversationActivity;
import cn.bjhdltcdn.p2plive.ui.activity.GroupDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.GroupListActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiawenquan on 17/12/1.
 */

public class GroupListItemAdapter extends BaseRecyclerAdapter {

    private List<Group> list;
    private RequestOptions options;

    public List<Group> getList() {
        return list;
    }

    /**
     * 1 我加入的群组
     * 2 Ta加入的群组
     */
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public void setList(List<Group> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addDataList(List<Group> list) {
        if (list == null) {
            return;
        }

        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setItemAddClick(OnItemClick listener) {
        this.onItemClick = listener;
    }

    public GroupListItemAdapter() {
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_group_icon);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            final Group group = list.get(position);
            final int isExistGroup = group.getIsExistGroup();

            // 群封面
            Glide.with(App.getInstance()).asBitmap().load(group.getGroupImg()).apply(options).into(itemViewHolder.circleImageView);

            // 昵称
            itemViewHolder.nickNameView.setText(group.getGroupName());

            // 人数
            itemViewHolder.countView.setText(group.getNumber() + "人");

            if (type == 2) {
                itemViewHolder.tv_add_group.setVisibility(View.VISIBLE);
                if (isExistGroup == 0) {
                    itemViewHolder.tv_add_group.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                    itemViewHolder.tv_add_group.setText("加入群");
                    itemViewHolder.rightView.setVisibility(View.GONE);
                } else if (isExistGroup == 1) {
                    itemViewHolder.tv_add_group.setBackgroundResource(R.drawable.shape_round_5_stroke_d8d8d8_solid_ffffff);
                    itemViewHolder.tv_add_group.setText("发起群聊");
                } else if (isExistGroup == 2) {
                    itemViewHolder.tv_add_group.setBackgroundResource(R.drawable.shape_round_5_stroke_d8d8d8_solid_ffffff);
                    itemViewHolder.tv_add_group.setText("申请中");
                }

                itemViewHolder.tv_add_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (list.get(position).getGroupMode() == 2) {
                            onItemClick.ItemClick(isExistGroup, position);
                        }else {
                            onItemClick.ItemClick(isExistGroup, position);
                        }
                    }
                });
            }

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.group_list_item_layout, null);
        return new ItemViewHolder(itemView);
    }

    public void setExistGroup(int groupItemPosition, int IsExistGroup) {
        list.get(groupItemPosition).setIsExistGroup(IsExistGroup);
        notifyItemChanged(groupItemPosition);
    }


    static class ItemViewHolder extends BaseViewHolder {

        CircleImageView circleImageView;
        TextView nickNameView;
        TextView countView, tv_add_group;
        ImageView rightView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circle_image_view);
            nickNameView = itemView.findViewById(R.id.nicke_text_view);
            countView = itemView.findViewById(R.id.count_view);
            tv_add_group = itemView.findViewById(R.id.tv_add_group);
            rightView = itemView.findViewById(R.id.arrow_right_view);


        }
    }


    OnItemClick onItemClick;


    public interface OnItemClick {

        void ItemClick(int isExistGroup, int position);
    }

}
