package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.AttenttionEvent;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by ZHUDI on 2017/11/20.
 */

public class AttentionOrFansListAdapter extends BaseRecyclerAdapter {
    private List<User> list_user;
    private Activity mActivity;

    public AttentionOrFansListAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void setData(List<User> list_user) {
        this.list_user = list_user;
        notifyDataSetChanged();
    }

    public List<User> getList_user() {
        return list_user;
    }

    @Override
    public int getItemCount() {
        return list_user == null ? 0 : list_user.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_fans, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                final User user = list_user.get(position);
                if (user != null) {
                    RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.ic_launcher);
                    Glide.with(App.getInstance()).asBitmap().load(user.getUserIcon()).apply(options).into(imageHolder.img_icon);
                    imageHolder.tv_name.setText(user.getNickName());

                    if (user.getIsAttention() == 1) {
//                        imageHolder.img_select.setSelected(true);
                        imageHolder.tv_select.setText("取消关注");
                    } else {
//                        imageHolder.img_select.setSelected(false);
                        imageHolder.tv_select.setText("关注");
                    }
                    imageHolder.tv_select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            user.setIsAttention(user.getIsAttention() != 1 ? 1 : 0);
                            EventBus.getDefault().post(new AttenttionEvent(user.getIsAttention() == 1 ? 1 : 2, user.getUserId()));
                            //notifyItemChanged(position);
                        }
                    });
                    imageHolder.img_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, user));

                        }
                    });
                }
            }
        }

    }

    class ImageHolder extends BaseViewHolder {
        private ImageView img_icon, img_select;
        private TextView tv_name;
        private TextView tv_select;

        public ImageHolder(View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            img_select = itemView.findViewById(R.id.img_select);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_select = itemView.findViewById(R.id.tv_select);
        }
    }
}
