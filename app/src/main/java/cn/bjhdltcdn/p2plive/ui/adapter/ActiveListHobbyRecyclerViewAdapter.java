package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 活动了表兴趣标签列表
 */

public class ActiveListHobbyRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<HobbyInfo> list;
    private Activity mActivity;
    private RequestOptions options;

    public ActiveListHobbyRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<HobbyInfo>();
        options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setList(List<HobbyInfo> list) {
        this.list = list;
        if (list != null) {
            this.list = list;
        }
    }

    public List<HobbyInfo> getList() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.active_list_hobby_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                final TextView tabTextView = imageHolder.tabTextView;
                final HobbyInfo hobbyInfo = list.get(position);
                tabTextView.setText(hobbyInfo.getHobbyName());
                Glide.with(mActivity).load(hobbyInfo.getHobbyImg()).apply(options).into(imageHolder.tabImg);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImageHolder extends BaseViewHolder {
        TextView tabTextView;
        ImageView tabImg;

        public ImageHolder(View itemView) {
            super(itemView);
            tabTextView = (TextView) itemView.findViewById(R.id.tv_label);
            tabImg = (ImageView) itemView.findViewById(R.id.label_img);
        }

    }


    public void onDestroy() {
        if (mActivity != null) {
            mActivity = null;
        }

        if (list != null) {
            list.clear();
        }
        list = null;
    }

}