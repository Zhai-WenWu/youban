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

public class HobbyListAdapter extends BaseRecyclerAdapter {
    private List<HobbyInfo> list_hobby;
    private List<HobbyInfo> list_hobby_selected;//已选择的兴趣
    private int type;//1.注册后完善资料2.修改资料

    public void setType(int type) {
        this.type = type;
    }

    public void setData(List<HobbyInfo> hobbyInfos) {
        this.list_hobby = hobbyInfos;
        this.list_hobby_selected = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list_hobby == null ? 0 : list_hobby.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_hobby, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                final HobbyInfo hobbyInfo = list_hobby.get(position);
                final RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.ic_launcher);
                if (hobbyInfo.getIsSelect() == 1) {
                    if (!list_hobby_selected.contains(hobbyInfo)) {
                        imageHolder.img_select.setVisibility(View.VISIBLE);
                        list_hobby_selected.add(hobbyInfo);
                        EventBus.getDefault().post(new HobbySelectEvent());//按钮改变背景颜色
                    }
                    Glide.with(App.getInstance()).asBitmap().load(hobbyInfo.getHobbyImg()).apply(options).into(imageHolder.img_icon);
                } else {
                    if (list_hobby_selected.contains(hobbyInfo)) {
                        imageHolder.img_select.setVisibility(View.INVISIBLE);
                        list_hobby_selected.remove(hobbyInfo);
                        EventBus.getDefault().post(new HobbySelectEvent());//按钮改变背景颜色
                    }
                    Glide.with(App.getInstance()).asBitmap().load(hobbyInfo.getHobbyGrayImg()).apply(options).into(imageHolder.img_icon);
                }
                imageHolder.tv_name.setText(hobbyInfo.getHobbyName());
                imageHolder.tv_desc.setText(hobbyInfo.getHobbyDesc());
                imageHolder.tv_name.setSelected(hobbyInfo.getIsSelect() == 0 ? false : true);
                imageHolder.setItemListener(new ItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (type == 1) {
                            imageHolder.img_select.setVisibility(View.VISIBLE);
                        }
                        EventBus.getDefault().post(new HobbySelectEvent());//按钮改变背景颜色
                        hobbyInfo.setIsSelect(hobbyInfo.getIsSelect() == 0 ? 1 : 0);
                        notifyItemChanged(position);
                    }
                });
            }
        }

    }

    public List<HobbyInfo> getList_hobby_selected() {
        return list_hobby_selected;
    }

    class ImageHolder extends BaseViewHolder {
        private ImageView img_icon, img_select;
        private TextView tv_name, tv_desc;

        public ImageHolder(View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            img_select = itemView.findViewById(R.id.img_select);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
        }
    }
}
