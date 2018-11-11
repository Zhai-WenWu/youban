package cn.bjhdltcdn.p2plive.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.adapter.BaseRecyclerAdapter;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 群成员列表适配器
 */

public class GroupMemberListAdapter extends BaseRecyclerAdapter {

    private RequestOptions options;
    /**
     * 是否删除模式
     */
//    private boolean isDelectMode = false;
    private final int ADD_ICON_ROLE = -1, DEL_ICON_ROLE = -2;
    private List<GroupUser> list;

//    private ViewClickListener viewClickListener;

//    public void setViewClickListener(ViewClickListener viewClickListener) {
//        this.viewClickListener = viewClickListener;
//    }

//    public void setDelectMode(boolean delectMode) {
//        isDelectMode = delectMode;
//    }
//
//    public boolean isDelectMode() {
//        return isDelectMode;
//    }

    public List<GroupUser> getList() {
        return list;
    }

    public void setList(List<GroupUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getADD_ICON_ROLE() {
        return ADD_ICON_ROLE;
    }

    public int getDEL_ICON_ROLE() {
        return DEL_ICON_ROLE;
    }

    /**
     * 用于分页加载
     *
     * @param list
     */
    public void addList(List<GroupUser> list) {
        if (list == null) {
            return;
        }

        if (this.list == null) {
            return;
        }

        this.list.addAll(getItemCount() - 2, list);
        notifyDataSetChanged();
    }

    /**
     * 显示添加删除item
     */
    public void setDeleteItem() {
        if (this.list == null) {
            return;
        }

        GroupUser addroupUser = new GroupUser();
        addroupUser.setUserRole(ADD_ICON_ROLE);
        this.list.add(addroupUser);
        GroupUser groupUser = new GroupUser();
        groupUser.setUserRole(DEL_ICON_ROLE);
        this.list.add(groupUser);
        notifyDataSetChanged();

    }

    public GroupMemberListAdapter() {
        options = new RequestOptions().placeholder(R.mipmap.error_user_icon).centerCrop();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ItemViewHolder) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            GroupUser groupUser = list.get(position);

            // 头像
            if (groupUser.getBaseUser() != null) {
                Glide.with(App.getInstance()).asBitmap().load(groupUser.getBaseUser().getUserIcon()).apply(options).into(itemViewHolder.circleImageView);
                itemViewHolder.tv_nickname.setVisibility(View.VISIBLE);
                itemViewHolder.tv_nickname.setText(groupUser.getBaseUser().getNickName());
            } else {
                itemViewHolder.tv_nickname.setVisibility(View.GONE);
                if (groupUser.getUserRole() == DEL_ICON_ROLE) {
                    Glide.with(App.getInstance()).asBitmap().load(R.mipmap.delete_group_member_icon).into(itemViewHolder.circleImageView);
                } else if (groupUser.getUserRole() == ADD_ICON_ROLE) {
                    Glide.with(App.getInstance()).asBitmap().load(R.mipmap.add_group_member_icon).into(itemViewHolder.circleImageView);
                }
            }
            // 是否是管理员
            switch (groupUser.getUserRole()) {
                case 1:

                    itemViewHolder.nickNameView.setVisibility(View.VISIBLE);
                    itemViewHolder.nickNameView.setText("群主");

                    break;

                case 2:

                    itemViewHolder.nickNameView.setVisibility(View.VISIBLE);
                    itemViewHolder.nickNameView.setText("管理员");

                    break;

                case 3:
                    itemViewHolder.nickNameView.setVisibility(View.GONE);
                    itemViewHolder.nickNameView.setText("");

                    break;

                default:// 处理删除按钮
                    itemViewHolder.nickNameView.setVisibility(View.GONE);

                    break;

            }

//            // 删除模式下
//            itemViewHolder.deleteView.setVisibility(View.GONE);
//            itemViewHolder.deleteView.setOnClickListener(null);
//            if (isDelectMode) {
//                itemViewHolder.deleteView.setImageResource(R.mipmap.delete_group_member_icon);
//                if (groupUser.getBaseUser() != null && groupUser.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) && groupUser.getUserRole() != 1) {
//                    itemViewHolder.deleteView.setVisibility(View.VISIBLE);
//
//                    itemViewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if (viewClickListener != null) {
//                                viewClickListener.onClick(position);
//                            }
//
//                        }
//                    });
//
//                } else {
//                    itemViewHolder.deleteView.setVisibility(View.INVISIBLE);
//                }
//
//
//            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = View.inflate(App.getInstance(), R.layout.group_member_list_item_adapter_layout, null);

        return new ItemViewHolder(itemView);
    }


    static class ItemViewHolder extends BaseViewHolder {

        CircleImageView circleImageView;
        TextView nickNameView;
        ImageView deleteView;
        TextView tv_nickname;

        public ItemViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circle_image_view);
            nickNameView = itemView.findViewById(R.id.nicke_text_view);
            deleteView = itemView.findViewById(R.id.delete_view);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
        }
    }

//    public interface ViewClickListener {
//        void onClick(int position);
//    }
}
