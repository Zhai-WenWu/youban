package cn.bjhdltcdn.p2plive.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.ChatInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by ZHUDI on 2017/9/22.
 */

public class AnonymousChatRoomListAdapter extends BaseRecyclerAdapter {
    private List<ChatInfo> list_room;
    private onClick onClick;

    public void setOnClick(AnonymousChatRoomListAdapter.onClick onClick) {
        this.onClick = onClick;
    }

    public void setDate(List<ChatInfo> list_room) {
        this.list_room = list_room;
        notifyDataSetChanged();
    }

    public void updateList(List<ChatInfo> list_room) {
        if (list_room == null) {
            return;
        }

        if (this.list_room == null) {
            this.list_room = new ArrayList<>();
        }
        this.list_room.addAll(list_room);
        notifyDataSetChanged();
    }

    public List<ChatInfo> getList_room() {
        return list_room;
    }

    @Override
    public int getItemCount() {
        return list_room != null ? list_room.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_chatroom_anonymous, null);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final ChatInfo chatInfo = list_room.get(position);
                if (chatInfo.getIsLock() == 1) {
                    itemViewHolder.lockView.setImageResource(R.drawable.lock_chatroom);
                } else if (chatInfo.getIsLock() == 0) {
                    itemViewHolder.lockView.setImageResource(R.drawable.unlock_chatroom);
                }
                itemViewHolder.lockView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chatInfo.getUserRole() == 1) {
                            onClick.onClick(position, chatInfo.getIsLock());
                        }
                    }
                });
                List<LabelInfo> labelList = chatInfo.getLabelList();
                if (labelList != null && labelList.size() > 0) {
                    itemViewHolder.tv_tag.setVisibility(View.VISIBLE);
                    itemViewHolder.tagIcon.setVisibility(View.VISIBLE);
                    StringBuffer labelString = new StringBuffer();
                    for (int i = 0; i < labelList.size(); i++) {
                        String info = labelList.get(i).getLabelName();
                        labelString.append(info + "    ");
                    }
                    itemViewHolder.tv_tag.setText(labelString.toString());
                } else {
                    itemViewHolder.tagIcon.setVisibility(View.GONE);
                    itemViewHolder.tv_tag.setVisibility(View.GONE);
                }

                itemViewHolder.tv_num_people.setText(chatInfo.getOnlineNumber() + "人");
                if (!TextUtils.isEmpty(chatInfo.getBackgroundUrl())) {
                    itemViewHolder.bgView.setBackgroundColor(Color.parseColor(chatInfo.getBackgroundUrl()));
                }
                if (!TextUtils.isEmpty(chatInfo.getChatIcon())) {
                    Utils.ImageViewDisplayByUrl(chatInfo.getChatIcon(), itemViewHolder.chatIcon);
                }
                if (!TextUtils.isEmpty(chatInfo.getChatName())) {
                    itemViewHolder.nameView.setText(chatInfo.getChatName());
                }

                String cityAndSchool;
                String schoolName = chatInfo.getBaseUser().getSchoolName();
                if (!TextUtils.isEmpty(schoolName) && !"null".equals(schoolName)) {
                    cityAndSchool = schoolName;
                    String city = chatInfo.getBaseUser().getCity();
                    if (!TextUtils.isEmpty(city) && !"null".equals(city)) {
                        cityAndSchool = city + "　" + schoolName;
                    }
                    itemViewHolder.cityAndSchoolView.setVisibility(View.VISIBLE);
                    itemViewHolder.cityAndSchoolView.setText(cityAndSchool);
                } else {
                    itemViewHolder.cityAndSchoolView.setVisibility(View.GONE);
                }
            }
        }

    }

    class ItemViewHolder extends BaseViewHolder {
        private ImageView chatIcon, tagIcon, lockView;
        private TextView tv_tag, tv_num_people;
        private TextView nameView;
        private TextView cityAndSchoolView;
        private RelativeLayout bgView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tv_name);
            chatIcon = itemView.findViewById(R.id.iv_icon);
            tagIcon = itemView.findViewById(R.id.img_tag);
            tv_tag = itemView.findViewById(R.id.tv_tag);
            lockView = itemView.findViewById(R.id.iv_lock_chatroom);
            tv_num_people = itemView.findViewById(R.id.tv_num_people);
            cityAndSchoolView = itemView.findViewById(R.id.tv_city_school);
            bgView = itemView.findViewById(R.id.rl_bg);
        }
    }

    public interface onClick {
        void onClick(int position, int isLock);
    }
}
