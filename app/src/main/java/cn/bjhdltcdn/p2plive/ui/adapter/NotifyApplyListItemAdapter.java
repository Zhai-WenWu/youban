package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.GroupApply;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.OrganApply;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiawenquan on 17/12/26.
 */

public class NotifyApplyListItemAdapter extends BaseRecyclerAdapter {

    /**
     * 群同意和拒绝通知item
     */
    public static final int GROUP_ITEM_7 = 7;

    /**
     * 圈子申请item
     */
    public static final int ORGANIZATION_ITEM_6 = 6;


    private List<HomeInfo> list;

    public List<HomeInfo> getList() {
        return list;
    }

    public void setList(List<HomeInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<HomeInfo> list) {
        if (list == null) {
            return;
        }

        if (this.list == null) {
            return;
        }

        int count = this.list.size();
        this.list.addAll(list);
        notifyItemRangeChanged(count - 1, list.size());

    }


    private RequestOptions options;

    public NotifyApplyListItemAdapter() {
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon);
    }

    private UserIconClickListener userIconClickListener;
    private ApplyOptionsClickListener applyOptionsClickListener;

    public void setUserIconClickListener(UserIconClickListener userIconClickListener) {
        this.userIconClickListener = userIconClickListener;
    }

    public void setApplyOptionsClickListener(ApplyOptionsClickListener applyOptionsClickListener) {
        this.applyOptionsClickListener = applyOptionsClickListener;
    }


    @Override
    public int getItemViewType(int position) {

        if (getItemCount() > 0) {
            HomeInfo homeInfo = list.get(position);
            //类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请)
            switch (homeInfo.getType()) {

                case ORGANIZATION_ITEM_6:
                    return ORGANIZATION_ITEM_6;

                case GROUP_ITEM_7:
                    return GROUP_ITEM_7;


            }
        }
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;

            Object object = list.get(position);
            if (object instanceof HomeInfo) {
                HomeInfo homeInfo = (HomeInfo) object;
                if (homeInfo != null) {


                    //类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请)
                    switch (homeInfo.getType()) {

                        case ORGANIZATION_ITEM_6:
                            //圈子申请
                            final OrganApply organApply6 = homeInfo.getOrganApply();
                            if (organApply6 != null) {
                                final BaseUser baseUser = organApply6.getBaseUser();
                                if (baseUser != null) {
                                    // 申请人头像
                                    Glide.with(App.getInstance()).asBitmap().load(baseUser.getUserIcon()).apply(options).into(viewHolder.circleImageView);
                                    viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (userIconClickListener != null) {
                                                userIconClickListener.onClick(baseUser,organApply6.getType());
                                            }
                                        }
                                    });
                                    // 申请人昵称
                                    viewHolder.nickNameView.setText(baseUser.getNickName());

                                }

                                // 申请加入的群
                                viewHolder.tagView.setText("申请加入“" + organApply6.getOrganName() + "”" + "圈子");

                                // 申请同意按钮
                                viewHolder.okImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (applyOptionsClickListener != null) {
                                            applyOptionsClickListener.onClick(3, organApply6.getApplyId(), baseUser.getUserId(), position);
                                        }
                                    }
                                });

                                // 申请拒绝按钮
                                viewHolder.cancelImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (applyOptionsClickListener != null) {
                                            applyOptionsClickListener.onClick(4, organApply6.getApplyId(), baseUser.getUserId(), position);
                                        }
                                    }
                                });


                                viewHolder.okImageView.setVisibility(View.GONE);
                                viewHolder.cancelImageView.setVisibility(View.GONE);
                                viewHolder.resultView.setVisibility(View.VISIBLE);
                                // 审核状态(1-->未审核,2-->同意,3-->拒绝)
                                switch (organApply6.getStatus()) {
                                    case 1:
                                        viewHolder.okImageView.setVisibility(View.VISIBLE);
                                        viewHolder.cancelImageView.setVisibility(View.VISIBLE);
                                        viewHolder.resultView.setVisibility(View.GONE);
                                        break;

                                    case 2:
                                        viewHolder.resultView.setText("已同意");
                                        break;

                                    case 3:
                                        viewHolder.resultView.setText("已拒绝");

                                        break;

                                }

                                // 添加时间
                                viewHolder.addTimeView.setText(organApply6.getAddTime());

                            }

                            break;

                        case GROUP_ITEM_7:

                            //群组申请
                            final GroupApply groupApply7 = homeInfo.getGroupApply();
                            if (groupApply7 != null) {

                                final BaseUser baseUser = groupApply7.getBaseUser();
                                if (baseUser != null) {
                                    // 申请人头像
                                    Glide.with(App.getInstance()).asBitmap().load(baseUser.getUserIcon()).apply(options).into(viewHolder.circleImageView);
                                    viewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (userIconClickListener != null) {
                                                userIconClickListener.onClick(baseUser, 0);
                                            }

                                        }
                                    });
                                    // 申请人昵称
                                    viewHolder.nickNameView.setText(baseUser.getNickName());

                                }

                                // 申请加入的群
                                viewHolder.tagView.setText("申请加入“" + groupApply7.getGroupName() + "”" + "群");

                                // 申请同意按钮
                                viewHolder.okImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (applyOptionsClickListener != null) {
                                            applyOptionsClickListener.onClick(1, groupApply7.getApplyId(), baseUser.getUserId(), position);
                                        }
                                    }
                                });

                                // 申请拒绝按钮
                                viewHolder.cancelImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (applyOptionsClickListener != null) {
                                            applyOptionsClickListener.onClick(2, groupApply7.getApplyId(), baseUser.getUserId(), position);
                                        }
                                    }
                                });


                                viewHolder.okImageView.setVisibility(View.GONE);
                                viewHolder.cancelImageView.setVisibility(View.GONE);
                                viewHolder.resultView.setVisibility(View.VISIBLE);
                                // 审核(0-->未审核,1-->同意,2-->拒绝),
                                switch (groupApply7.getStatus()) {
                                    case 0:
                                        viewHolder.okImageView.setVisibility(View.VISIBLE);
                                        viewHolder.cancelImageView.setVisibility(View.VISIBLE);
                                        viewHolder.resultView.setVisibility(View.GONE);
                                        break;

                                    case 1:
                                        viewHolder.resultView.setText("已同意");
                                        break;

                                    case 2:
                                        viewHolder.resultView.setText("已拒绝");

                                        break;

                                }

                                // 添加时间
                                viewHolder.addTimeView.setText(groupApply7.getAddTime());


                            }

                            break;
                    }

                }
            }

        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = null;
        switch (itemType) {


            case GROUP_ITEM_7:
            case ORGANIZATION_ITEM_6:
                itemView = View.inflate(App.getInstance(), R.layout.group_apply_list_item_layout, null);
                break;
        }

        if (itemView == null) {
            return null;
        }

        return new ItemViewHolder(itemView);
    }

    static class ItemViewHolder extends BaseViewHolder {

        CircleImageView circleImageView;
        ImageView okImageView;
        ImageView cancelImageView;
        TextView nickNameView;
        TextView tagView;
        TextView resultView;
        TextView addTimeView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circle_image_view);
            okImageView = itemView.findViewById(R.id.ok_image_view);
            cancelImageView = itemView.findViewById(R.id.cancel_image_view);
            nickNameView = itemView.findViewById(R.id.nicke_text_view);
            tagView = itemView.findViewById(R.id.tag_view);
            resultView = itemView.findViewById(R.id.result_view);
            addTimeView = itemView.findViewById(R.id.add_time_view);
        }
    }


    public interface UserIconClickListener {
        /**
         * 点击事件
         *
         * @param baseUser 用户信息
         * @param type
         */
        void onClick(BaseUser baseUser, int type);
    }

    public interface ApplyOptionsClickListener {
        /**
         * 点击事件
         *
         * @param optionsType 操作类型 群：1 同意 ， 2 拒绝 ；圈子 3 同意，4拒绝
         * @param applyId     申请id
         * @param userId      用户id
         * @param position    选择的item
         */
        void onClick(int optionsType, long applyId, long userId, int position);
    }

}
