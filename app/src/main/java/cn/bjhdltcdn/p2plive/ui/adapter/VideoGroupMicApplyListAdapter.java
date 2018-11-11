package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.event.VideoGroupMicApplyEvent;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.utils.Utils;


/**
 * Created by ZHUDI on 2017/1/12.
 */

public class VideoGroupMicApplyListAdapter extends BaseAdapter {
    private Context context;
    private List<RoomBaseUser> list;

    public VideoGroupMicApplyListAdapter(Context context, List<RoomBaseUser> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_video_group_mic_apply, null);
            holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.img_status_apply = (ImageView) convertView.findViewById(R.id.img_status_apply);
            holder.tv_agree = (TextView) convertView.findViewById(R.id.tv_agree);
            holder.tv_refuse = (TextView) convertView.findViewById(R.id.tv_refuse);
            holder.line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final RoomBaseUser baseUser = (RoomBaseUser) getItem(position);
        Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), holder.img_icon);
        holder.tv_name.setText(baseUser.getNickName());
        if (baseUser.getType() == 1) {
            holder.img_status_apply.setImageResource(R.drawable.video_group_apply_status_video);
        } else if (baseUser.getType() == 2) {
            holder.img_status_apply.setImageResource(R.drawable.video_group_apply_status_voice);
        }
        holder.tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoGroupMicApplyEvent(2, baseUser, baseUser.getType()));
            }
        });
        holder.tv_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoGroupMicApplyEvent(4, baseUser));
            }
        });

        if (position == getCount() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView img_icon, img_status_apply;
        TextView tv_name, tv_agree, tv_refuse;
        View line;
    }

    public void setDate(RoomBaseUser baseUser) {
        for (int i = 0; i < list.size(); i++) {
            if (baseUser.getUserId()==list.get(i).getUserId()) {
                list.remove(i);
            }
        }
        notifyDataSetChanged();
    }
}
