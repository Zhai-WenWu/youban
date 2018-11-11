package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ZHUDI on 2017/3/6.
 */

public class TransferHostingAdapter extends BaseAdapter {
    private Context context;
    private List<RoomBaseUser> list;

    public TransferHostingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_transferhosting, null);
            holder.img_icon = (CircleImageView) convertView.findViewById(R.id.img_icon);
            holder.tv_userrole = (TextView) convertView.findViewById(R.id.tv_userrole);
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RoomBaseUser baseUser = (RoomBaseUser) getItem(position);
        if (baseUser != null) {
            String url = baseUser.getUserIcon();
            Utils.ImageViewDisplayByUrl(url, holder.img_icon);
            holder.tv_nickname.setText(baseUser.getNickName());
            if (baseUser.getUserRole() == 2) {
                holder.tv_userrole.setText("管理员");
            } else if (baseUser.getUserRole() == 4) {
                holder.tv_userrole.setText("房主");
            } else if (baseUser.getWheatId() != 0) {
                holder.tv_userrole.setText((baseUser.getWheatId() + 1) + "号麦");
            }
            if (baseUser.getTransfer() == 11) {
                holder.tv_nickname.setTextColor(context.getResources().getColor(R.color.color_999999));
            } else {
                holder.tv_nickname.setTextColor(context.getResources().getColor(R.color.color_00c1f5));
            }
        }
        return convertView;
    }

    public void setData(List<RoomBaseUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        CircleImageView img_icon;
        TextView tv_userrole, tv_nickname;
    }
}
