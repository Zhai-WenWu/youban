package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by ZHUDI on 2017/9/22.
 */

public class VideoChatRoomListAdapter extends BaseRecyclerAdapter {
    private List<RoomInfo> list_room;


    public void setDate(List<RoomInfo> list_room) {
        this.list_room = list_room;
        notifyDataSetChanged();
    }

    public void updateList(List<RoomInfo> list_room) {
        if (list_room == null) {
            return;
        }

        if (this.list_room == null) {
            this.list_room = new ArrayList<>();
        }
        this.list_room.addAll(list_room);
        notifyDataSetChanged();
    }

    public List<RoomInfo> getList_room() {
        return list_room;
    }

    @Override
    public int getItemCount() {
        return list_room != null ? list_room.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_chatroom_video, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                RoomInfo roomInfo = list_room.get(position);
                final BaseUser baseUser = roomInfo.getBaseUser();
                if (baseUser != null) {
                    Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), itemViewHolder.img_icon);
                    itemViewHolder.img_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(App.getInstance(), UserDetailsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Constants.KEY.KEY_OBJECT, baseUser);
                            App.getInstance().startActivity(intent);
                        }
                    });
                    itemViewHolder.tv_nickname.setText(baseUser.getNickName());
                    itemViewHolder.tv_age.setText(baseUser.getAge() + "岁");
                    int sex = baseUser.getSex();//1男2女
                    Drawable drawable = null;
                    if (sex == 1) {
                        drawable = App.getInstance().getResources().getDrawable(R.mipmap.boy_icon);
                    } else if (sex == 2) {
                        drawable = App.getInstance().getResources().getDrawable(R.mipmap.girl_icon);
                    }
                    if (drawable != null) {
                        drawable.setBounds(0, 0, Utils.dp2px(12), Utils.dp2px(12));
                    }
                    itemViewHolder.tv_age.setCompoundDrawables(drawable, null, null, null);
                    if (TextUtils.isEmpty(baseUser.getSchoolName())) {
                        itemViewHolder.schoolNameView.setVisibility(View.GONE);
                    } else {
                        itemViewHolder.schoolNameView.setVisibility(View.VISIBLE);
                        itemViewHolder.schoolNameView.setText(baseUser.getSchoolName());
                    }

                    if (baseUser.getIsSchoolmate() == 1) {//不是校友
                        itemViewHolder.tv_schoolmate.setVisibility(View.GONE);
                    } else if (baseUser.getIsSchoolmate() == 2) {//是校友
                        itemViewHolder.tv_schoolmate.setVisibility(View.VISIBLE);
                    }
                    String cityAndDistance = null;
                    String city = baseUser.getCity();
                    String distance = baseUser.getDistance();
                    if (!TextUtils.isEmpty(city) && !"null".equals(city)) {
                        cityAndDistance = city;
                        if (!TextUtils.isEmpty(distance) && !"null".equals(distance) && !"0km".equals(distance)) {
                            cityAndDistance = city + "　" + distance;
                        }
                    } else {
                        if (!TextUtils.isEmpty(distance) && !"null".equals(distance) && !"0km".equals(distance)) {
                            cityAndDistance = distance;
                        }
                    }
                    if (!TextUtils.isEmpty(cityAndDistance)) {
                        itemViewHolder.tv_distance.setVisibility(View.VISIBLE);
                        itemViewHolder.tv_distance.setText(cityAndDistance);
                    } else {
                        itemViewHolder.tv_distance.setVisibility(View.GONE);
                    }

                }
                List<String> labelList = roomInfo.getLabelList();
                if (labelList != null && labelList.size() > 0) {
                    itemViewHolder.tv_tag.setVisibility(View.VISIBLE);
                    itemViewHolder.img_tag.setVisibility(View.VISIBLE);
                    StringBuffer labelString = new StringBuffer();
                    for (int i = 0; i < labelList.size(); i++) {
                        String info = labelList.get(i);
                        labelString.append(info + "    ");
                    }
                    itemViewHolder.tv_tag.setText(labelString.toString());
                } else {
                    itemViewHolder.img_tag.setVisibility(View.GONE);
                    itemViewHolder.tv_tag.setVisibility(View.GONE);
                }

                itemViewHolder.tv_num_people.setText(roomInfo.getOnlineNumber() + "人");
                if (!TextUtils.isEmpty(roomInfo.getRoomName())) {
                    itemViewHolder.tv_content.setText(roomInfo.getRoomName());
                }
                Utils.CornerImageViewDisplayByUrl(roomInfo.getBackgroundUrl(), itemViewHolder.img_bg,9);

            }
        }

    }

    class ItemViewHolder extends BaseViewHolder {
        private ImageView img_icon, img_tag, img_bg;
        private TextView tv_nickname, tv_age, tv_schoolmate, tv_distance, tv_tag, tv_num_people, tv_content;
        private TextView schoolNameView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            img_tag = itemView.findViewById(R.id.img_tag);
            img_bg = itemView.findViewById(R.id.img_bg);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_schoolmate = itemView.findViewById(R.id.tv_schoolmate);
            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_tag = itemView.findViewById(R.id.tv_tag);
            tv_age = itemView.findViewById(R.id.tv_age);
            schoolNameView = itemView.findViewById(R.id.tv_school);
            tv_num_people = itemView.findViewById(R.id.tv_num_people);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

}
