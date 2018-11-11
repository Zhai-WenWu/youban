package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 首页推荐圈子列表
 */

public class HomeRecommendAssoiationRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<OrganizationInfo> list;
    private Activity mActivity;
    private RequestOptions options;
    private OnClick onClick;

    public HomeRecommendAssoiationRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<OrganizationInfo>();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setList(List<OrganizationInfo> list) {
        this.list = list;
    }

    public OrganizationInfo getItem(int position){
        return list == null ? null : list.get(position);
    }

    public List<OrganizationInfo> getList() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_recommend_assoiction_recycle_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                ImageView assoicationImg = imageHolder.assoicationImg;
                TextView assoicationUserNumTextView = imageHolder.assoicationUserNumTextView;
                TextView assoicationPostNumTextView = imageHolder.assoicationPostNumTextView;
                TextView recommendAssoicationNameTextView = imageHolder.recommendAssoicationNameTextView;
                final TextView recommendAssoicationJoinTextView = imageHolder.recommendAssoicationJoinTextView;
                OrganizationInfo organizationInfo=list.get(position);
                Glide.with(mActivity).asBitmap().load(organizationInfo.getOrganImg()).apply(options).into(assoicationImg);
                assoicationUserNumTextView.setText("成员 "+organizationInfo.getMemberCount());
                assoicationPostNumTextView.setText("帖子 "+organizationInfo.getPostCount());
                recommendAssoicationNameTextView.setText(organizationInfo.getOrganName());
                int type=organizationInfo.getType();
                if(type==1){
                    //官方圈子
                    imageHolder.hotImg.setVisibility(View.VISIBLE);
                }else{
                    //普通圈子
                    imageHolder.hotImg.setVisibility(View.GONE);
                }
                int userRole=organizationInfo.getUserRole();
                final int joinLimit=organizationInfo.getJoinLimit();
                if(userRole==0){
                    //用户不在圈子
                    recommendAssoicationJoinTextView.setVisibility(View.VISIBLE);
                    recommendAssoicationJoinTextView.setEnabled(true);
                    if(joinLimit==1){
                        recommendAssoicationJoinTextView.setText("加入");
                    }else {
                        recommendAssoicationJoinTextView.setText("加入");
                    }
                    recommendAssoicationJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                    recommendAssoicationJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                }else if(userRole==4){
                    recommendAssoicationJoinTextView.setText("申请中");
                    recommendAssoicationJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                    recommendAssoicationJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
                    recommendAssoicationJoinTextView.setEnabled(false);
                }else{
                    //用户在圈子
                    recommendAssoicationJoinTextView.setText("已加入");
                    recommendAssoicationJoinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                    recommendAssoicationJoinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
                    recommendAssoicationJoinTextView.setEnabled(false);
                }
                recommendAssoicationJoinTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      onClick.onJoinClick(position,joinLimit);

                    }
                });

            }
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImageHolder extends BaseViewHolder {
        ImageView assoicationImg,hotImg;
        TextView assoicationUserNumTextView,assoicationPostNumTextView,recommendAssoicationNameTextView,recommendAssoicationJoinTextView;

        public ImageHolder(View itemView) {
            super(itemView);
            assoicationImg = (ImageView) itemView.findViewById(R.id.recommend_assoication_image);
            hotImg= (ImageView) itemView.findViewById(R.id.hot_image);
            assoicationUserNumTextView = (TextView) itemView.findViewById(R.id.assoication_user_num_text);
            assoicationPostNumTextView = (TextView) itemView.findViewById(R.id.assoication_post_num_text);
            recommendAssoicationNameTextView = (TextView) itemView.findViewById(R.id.recommend_assoication_name_text);
            recommendAssoicationJoinTextView = (TextView) itemView.findViewById(R.id.recommend_assoication_join_text);

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

    public interface OnClick{
        void onJoinClick(int position,int type);
    }

}