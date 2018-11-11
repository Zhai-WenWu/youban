package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.SecondLabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 店铺列表类别标签列表
 */

public class ShopCategoryRecyclerViewAdapter extends BaseRecyclerAdapter {
    private FirstLabelInfo firstLabelInfo;
    private Activity mActivity;
    private RequestOptions options;

    public ShopCategoryRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public FirstLabelInfo getFirstLabelInfo() {
        return firstLabelInfo;
    }

    public void setFirstLabelInfo(FirstLabelInfo firstLabelInfo) {
        this.firstLabelInfo = firstLabelInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.shop_recycler_category_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                final TextView tabTextView = imageHolder.tabTextView;
                if(firstLabelInfo!=null){
                    if(position==0){
                        tabTextView.setText(firstLabelInfo.getFirstLabelName());
                        tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                        imageHolder.categoryImageView.setVisibility(View.VISIBLE);
                        Utils.ImageViewDisplayByUrl(firstLabelInfo.getImageUrl(),imageHolder.categoryImageView);
                    }else {
                        tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                        imageHolder.categoryImageView.setVisibility(View.GONE);
                        List<SecondLabelInfo> secondLabelInfoList = firstLabelInfo.getList();
                        if (secondLabelInfoList != null && secondLabelInfoList.size() > 0) {
                            final SecondLabelInfo secondLabelInfo = secondLabelInfoList.get(position-1);
                            tabTextView.setText("# " + secondLabelInfo.getSecondLabelName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return firstLabelInfo == null ? 0 : firstLabelInfo.getList().size()+1;
    }

    class ImageHolder extends BaseViewHolder {
        TextView tabTextView;
        ImageView categoryImageView;
        public ImageHolder(View itemView) {
            super(itemView);
            tabTextView = (TextView) itemView.findViewById(R.id.post_label_two_text);
            categoryImageView= (ImageView) itemView.findViewById(R.id.category_image_view);
        }

    }


    public void onDestroy() {
        if (mActivity != null) {
            mActivity = null;
        }

    }

}