package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.CountInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 首页附近列表
 */

public class HomeNearByRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<CountInfo> list;
    private Activity mActivity;
    private RequestOptions options;
    private int screenWidth;

    public HomeNearByRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<CountInfo>();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);

        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
    }

    public void setList(List<CountInfo> list) {
        this.list = list;
    }

    public CountInfo getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        NearbyHolder holder = new NearbyHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_bearby_recycle_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof NearbyHolder) {
                final NearbyHolder nearbyHolder = (NearbyHolder) holder;
                ImageView nearbyImg = nearbyHolder.nearbyImg;
                TextView nearbyTextView = nearbyHolder.nearbyTextView;
                TextView nearbyNewNumTextView = nearbyHolder.nearbyNewNumTextView;
                //设置用户头像正方形
                CountInfo countInfo = list.get(position);
//                if(!countInfo.getNearImg().equals(nearbyImg.getTag())){//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
//                    nearbyImg.setTag(null);//需要清空tag，否则报错
                    Utils.CornerImageViewDisplayByUrl(countInfo.getNearImg(), nearbyImg,9);
//                    nearbyImg.setTag(countInfo.getNearImg());
//                }
                nearbyTextView.setText(countInfo.getContent());
                if (countInfo.getUpdateCount() > 0) {
                    nearbyNewNumTextView.setText(countInfo.getUpdateCount() + "条更新");
                    nearbyNewNumTextView.setVisibility(View.VISIBLE);
                } else {
                    nearbyNewNumTextView.setVisibility(View.GONE);
                }
                nearbyHolder.contentTextView.setText(countInfo.getDescription());
                if (position == 0 || position == 1) {
                    nearbyHolder.topView.setVisibility(View.VISIBLE);
                } else {
                    nearbyHolder.topView.setVisibility(View.GONE);
                }

                if (position == list.size() - 1) {
                    nearbyHolder.bottomView.setVisibility(View.VISIBLE);
                } else {
                    nearbyHolder.bottomView.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class NearbyHolder extends BaseViewHolder {
        ImageView nearbyImg;
        TextView nearbyTextView, nearbyNewNumTextView, contentTextView;
        View topView, bottomView;

        public NearbyHolder(View itemView) {
            super(itemView);
            topView = itemView.findViewById(R.id.top_view);
            bottomView = itemView.findViewById(R.id.bottom_view);
            nearbyImg = (ImageView) itemView.findViewById(R.id.nearby_image);
            nearbyTextView = (TextView) itemView.findViewById(R.id.nearby_name_text);
            nearbyNewNumTextView = (TextView) itemView.findViewById(R.id.nearby_new_num_text);
            contentTextView = (TextView) itemView.findViewById(R.id.nearby_content_text);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) nearbyImg.getLayoutParams();
            layoutParams.height = (screenWidth - Utils.dp2px(13 * 3)) / 2;
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