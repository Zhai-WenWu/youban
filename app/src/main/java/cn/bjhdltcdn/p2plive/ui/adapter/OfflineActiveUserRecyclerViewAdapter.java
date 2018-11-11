package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 活动详情报名用户列表
 */

public class OfflineActiveUserRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<BaseUser> list;
    private Activity mActivity;
    private RequestOptions options;
    private int screenWidth;

    public OfflineActiveUserRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<BaseUser>();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_user_icon)
                .error(R.mipmap.error_user_icon);
        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
    }

    public void setList(List<BaseUser> list) {
        this.list = list;
        if (list != null) {
            this.list=list;
        }
    }

    public void addItem(BaseUser baseUser){
        if (list != null) {
            list.add(baseUser);
        }
        notifyDataSetChanged();
    }

    public void removeItem(long userId){
        if (list != null) {
            for (int i=0;i<list.size();i++){
                if(list.get(i).getUserId()==userId){
                    list.remove(i);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }



    public BaseUser getItem(int position){
        return list == null ? null : list.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        UseryHolder holder = new UseryHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.active_user_recycle_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof UseryHolder) {
                final UseryHolder useryHolder = (UseryHolder) holder;
                ImageView userImg = useryHolder.userImg;
                //设置用户头像正方形
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) useryHolder.userImg.getLayoutParams();
                layoutParams.height = (screenWidth- Utils.dp2px(20+17*6))/ 7;
                BaseUser baseUser=list.get(position);
                if(baseUser!=null){
                    Glide.with(mActivity).load(baseUser.getUserIcon()).apply(options).into(userImg);
                }
                int sex = baseUser.getSex();
                if (sex == 1) {
                    //男性
                    useryHolder.sexImg.setImageResource(R.mipmap.boy_active_icon);
                } else if (sex == 2) {
                    //女性
                    useryHolder.sexImg.setImageResource(R.mipmap.girl_active_icon);
                }
                if(position==0){
                    useryHolder.organizerImg.setVisibility(View.VISIBLE);
                }else{
                    useryHolder.organizerImg.setVisibility(View.GONE);
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class UseryHolder extends BaseViewHolder {
        ImageView userImg,organizerImg,sexImg;

        public UseryHolder(View itemView) {
            super(itemView);
            userImg = (ImageView) itemView.findViewById(R.id.user_image);
            organizerImg= (ImageView) itemView.findViewById(R.id.active_organizer_img);
            sexImg= (ImageView) itemView.findViewById(R.id.sex_img);
        }

    }

    public void onDestroy(){
        if (mActivity != null) {
            mActivity = null;
        }

        if (list != null) {
            list.clear();
        }
        list = null;
    }

}