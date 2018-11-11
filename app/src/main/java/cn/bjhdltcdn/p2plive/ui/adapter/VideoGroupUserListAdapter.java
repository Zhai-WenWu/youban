package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ZHUDI on 2017/1/6.
 */

public class VideoGroupUserListAdapter extends BaseRecyclerAdapter {
    private List<RoomBaseUser> list;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.item_list_video_group_user, null);
        return new VideoGroupUserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof VideoGroupUserHolder) {
            VideoGroupUserHolder videoGroupUserHolder = (VideoGroupUserHolder) holder;
            RoomBaseUser baseUser = list.get(position);
            Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), videoGroupUserHolder.imageView);
            if (baseUser.getWheat() == 2) {
                videoGroupUserHolder.img_level.setVisibility(View.VISIBLE);
                videoGroupUserHolder.img_level.setImageResource(R.drawable.video_group_mic_status);
            } else {
//                int level = baseUser.get();
//                if (level == 0) {
//                    videoGroupUserHolder.img_level.setVisibility(View.GONE);
//                } else {
//                    videoGroupUserHolder.img_level.setVisibility(View.VISIBLE);
//                    if (level > 0 && level < 6) {
//                        videoGroupUserHolder.img_level.setImageResource(R.drawable.level_head_01);
//                    } else if (level > 10 && level < 16) {
//                        videoGroupUserHolder.img_level.setImageResource(R.drawable.level_head_02);
//                    } else if (level > 20 && level < 26) {
//                        videoGroupUserHolder.img_level.setImageResource(R.drawable.level_head_03);
//                    } else if (level > 30 && level < 36) {
//                        videoGroupUserHolder.img_level.setImageResource(R.drawable.level_head_04);
//                    } else if (level > 40 && level < 46) {
//                        videoGroupUserHolder.img_level.setImageResource(R.drawable.level_head_05);
//                    }
//                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setData(List<RoomBaseUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clearData() {
        if (getItemCount() > 0) {
            list.clear();
            list = null;
        }
    }

    public List<RoomBaseUser> getList() {
        return list;
    }

    class VideoGroupUserHolder extends BaseViewHolder {
        private CircleImageView imageView;
        private ImageView img_level;

        public VideoGroupUserHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.imageView);
            img_level = (ImageView) itemView.findViewById(R.id.img_level);
        }
    }
}
