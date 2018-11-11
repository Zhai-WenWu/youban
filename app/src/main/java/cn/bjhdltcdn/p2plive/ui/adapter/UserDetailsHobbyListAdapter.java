package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.event.HobbySelectEvent;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by ZHUDI on 2017/11/20.
 */

public class UserDetailsHobbyListAdapter extends BaseRecyclerAdapter {
    private List<HobbyInfo> list_hobby;

    public UserDetailsHobbyListAdapter(List<HobbyInfo> hobbyList) {
        this.list_hobby = hobbyList;
    }

    public void setList_hobby(List<HobbyInfo> list_hobby) {
        this.list_hobby = list_hobby;
    }

    @Override
    public int getItemCount() {
        return list_hobby == null ? 0 : list_hobby.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_hobby_user_details, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                ImageHolder imageHolder = (ImageHolder) holder;
                HobbyInfo hobbyInfo = list_hobby.get(position);
                RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.error_bg);
                Glide.with(App.getInstance()).asBitmap().load(hobbyInfo.getHobbyImg()).apply(options).into(imageHolder.img_icon);
                imageHolder.tv_name.setText(hobbyInfo.getHobbyName());
            }
        }

    }

    class ImageHolder extends BaseViewHolder {
        private ImageView img_icon;
        private TextView tv_name;

        public ImageHolder(View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
