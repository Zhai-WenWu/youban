package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.GroupUser;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

public class AddGroupAdministratorsAdapter extends BaseRecyclerAdapter {
    private List<GroupUser> groupUserList = new ArrayList<>();
    private List<Long> managerIdsList;
    private List<Long> delGroupUserIdsList;
    private List<GroupUser> managerList;
    private int selectNum = 0;
    private OnItemClick onItemClick;
    /**
     * 已设置的管理员数量
     */
    private int maxSelectNum = 0;
    /**
     * 1,设置群管理员 2删除用户
     */
    private int type;


    public AddGroupAdministratorsAdapter(int type, OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
        this.type = type;
        if (type == 2) {
            delGroupUserIdsList = new ArrayList<>(1);
        }
    }

    /**
     * 条目是否可以点击
     */
    private boolean itemClickAble = true;

    public void setItemClickAble(boolean itemClickAble) {
        this.itemClickAble = itemClickAble;
    }

    public void setGroupUserList(List<GroupUser> groupUserList) {
        for (GroupUser groupUser : groupUserList) {
            if (groupUser.getUserRole() != 1) {//非群主
                this.groupUserList.add(groupUser);
            }
        }
    }

    public void addList(List<GroupUser> groupUserList) {
        if (groupUserList == null) {
            return;
        }

        if (this.groupUserList == null) {
            return;
        }

        this.groupUserList.addAll(getItemCount() - 2, groupUserList);
        notifyDataSetChanged();
    }

    public List<GroupUser> getGroupUserList() {
        return groupUserList;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    @Override
    public int getItemCount() {
        return groupUserList == null ? 0 : groupUserList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        return new ItemViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.add_administrator_list_item_layout, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final GroupUser groupUser = groupUserList.get(position);
                BaseUser baseUser = groupUser.getBaseUser();
                Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), itemViewHolder.memberUserImg);
                itemViewHolder.memberName.setText(baseUser.getNickName());
                if (itemClickAble) {
                    itemViewHolder.checkBox.setButtonDrawable(R.drawable.selector_group_checkbox);
                }else{
                    itemViewHolder.checkBox.setButtonDrawable(R.drawable.selector_group_checkbox_c);
                }
                itemViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {// 设置checkBox点击事件
                    @Override
                    public void onClick(View v) {
                        if (type == 1) {
                            if (itemClickAble) {
                                // 判断checkBox的勾选状态
                                if (itemViewHolder.checkBox.isChecked()) {
                                    if (selectNum >= (3 - maxSelectNum)) {
                                        Utils.showToastShortTime("最多设置3名管理员！");
                                    } else {
                                        // 手动设置每个checkBox的勾选状态
                                        groupUserList.get(position).setUserRole(2);
                                        selectNum++;
                                    }
                                } else {
                                    // 手动设置每个checkBox的勾选状态
                                    groupUserList.get(position).setUserRole(3);
                                    selectNum--;
                                }
                                onItemClick.onItemClick(selectNum + maxSelectNum);
                            }
                        } else {
                            //没有就添加，有就删除
                            if (itemViewHolder.checkBox.isChecked()) {
                                groupUser.setIsSelect(1);
                                if (!delGroupUserIdsList.contains(groupUser.getUserId())) {
                                    delGroupUserIdsList.add(groupUser.getUserId());
                                }
                            } else {
                                groupUser.setIsSelect(0);
                                if (delGroupUserIdsList.contains(groupUser.getUserId())) {
                                    delGroupUserIdsList.remove(groupUser.getUserId());
                                }
                            }
                        }

                        notifyItemChanged(position);
                    }
                });
                itemViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickAble) {
                            itemViewHolder.checkBox.performClick();
                        }
                    }
                });
                if (type == 1) {
                    if (groupUser.getUserRole() == 2) {
                        itemViewHolder.checkBox.setChecked(true);
                    } else {
                        itemViewHolder.checkBox.setChecked(false);
                    }
                } else {
                    if (groupUser.getIsSelect() == 1) {
                        itemViewHolder.checkBox.setChecked(true);
                    } else {
                        itemViewHolder.checkBox.setChecked(false);
                    }
                }

            }
        }
    }


    class ItemViewHolder extends BaseViewHolder {
        RelativeLayout rootView;
        TextView memberName;
        CheckBox checkBox;
        ImageView memberUserImg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootView);
            checkBox = itemView.findViewById(R.id.checkbox_service);
            memberUserImg = itemView.findViewById(R.id.member_user_img_view);
            memberName = itemView.findViewById(R.id.member_name_view);
        }
    }

    /**
     * 获取设置管理员的id
     *
     * @return
     */
    public List<Long> getManagerIdsList() {
        if (managerIdsList == null) {
            managerIdsList = new ArrayList<>(1);
        }
        managerIdsList.clear();
        for (int i = 0; i < groupUserList.size(); i++) {
            GroupUser groupUser = groupUserList.get(i);
            if (groupUser.getUserRole() == 2) {
                managerIdsList.add(groupUser.getBaseUser().getUserId());
            }
        }
        return managerIdsList;
    }


    /**
     * 获取删除群成员的id
     *
     * @return
     */
    public List<Long> getDelGroupUserIdsList() {
        return delGroupUserIdsList;
    }

    public List<GroupUser> getManagerList() {
        if (managerList == null) {
            managerList = new ArrayList<>(1);
        }
        managerList.clear();
        for (int i = 0; i < groupUserList.size(); i++) {
            GroupUser groupUser = groupUserList.get(i);
            if (groupUser.getUserRole() == 2) {
                managerList.add(groupUser);
            }
        }
        return managerList;
    }

    public interface OnItemClick {
        void onItemClick(int selectNum);
    }
}
