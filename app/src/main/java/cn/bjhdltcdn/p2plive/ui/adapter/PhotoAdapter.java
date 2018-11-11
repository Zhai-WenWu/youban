package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.TransactionRecord;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.DiviceSizeUtil;

/**
 * Created by ZHAI on 2017/12/18.
 */

public class PhotoAdapter extends BaseRecyclerAdapter {
    private Activity mActivity;
    private List<Image> mList = new ArrayList<>();
    private boolean hasAdd = false;
    private RequestOptions options;

    public int MAX_ITEM_COUNT = 9;
    public static final String ITEM_ADD = "url://add_icon";

    public PhotoAdapter(Activity activity,int maxItemCount) {
        options = new RequestOptions().centerCrop()
        .placeholder(R.mipmap.error_bg)
        .error(R.mipmap.error_bg);
        this.mActivity = activity;
        MAX_ITEM_COUNT = maxItemCount;
    }

    public int getMAX_ITEM_COUNT() {
        return MAX_ITEM_COUNT;
    }

    public List<Image> getmList() {
        return mList;
    }

    @Override
    public long getItemId(int i) {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        PhotoAdapter.ViewHolder itemViewHolder = (PhotoAdapter.ViewHolder) holder;
        String imaUrl = "";

        /*if (mList.get(position) instanceof String) {
            imaUrl = (String) mList.get(position);
        } else {*/
            Image image = (Image) mList.get(position);
            imaUrl = image.getImageUrl();

        if (ITEM_ADD.equals(imaUrl)) {
            itemViewHolder.iv_close.setVisibility(View.INVISIBLE);
            itemViewHolder.iv.setImageResource(R.drawable.galary_add_photo);
            itemViewHolder.iv_close.setOnClickListener(null);
        } else {
            itemViewHolder.iv.setClickable(false);
            itemViewHolder.iv_close.setVisibility(View.VISIBLE);
            Glide.with(mActivity).asBitmap().load(imaUrl).apply(options).into(itemViewHolder.iv);
            itemViewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeCheckListener.onCloseClick(position);
                }
            });
        }

    }

    public void addItemData(int position,Image image, boolean isNotify) {

        if (image == null) {
            return;
        }

        if (this.mList == null) {
            this.mList = new ArrayList<>(1);
        }

        if (MAX_ITEM_COUNT - getItemCount() > 0) {
            // 正常加入item
            getmList().add(position, image);
            if (isNotify) {
                notifyDataSetChanged();
            }


        } else {
            // 判断边界问题
            // 判断最后一张是否是"add"图片
            Object object = getmList().get(getItemCount() - 1);
            if (object instanceof Image) {
                Image image1 = (Image) object;
                if (image1.getImageUrl().contains(ITEM_ADD)) {
                    getmList().remove(getItemCount() - 1);
                    getmList().add(position, image);
                    if (isNotify) {
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }


    public boolean checkIsExistAddItem(){
        if (MAX_ITEM_COUNT - getItemCount() >= 0) {
            // 判断最后一张是否是"add"图片
            Object object = getmList().get(getItemCount() - 1);
            if (object instanceof Image) {
                Image image1 = (Image) object;
                if (!image1.getImageUrl().contains(ITEM_ADD)) {
                    return true;
                }
            }
        }
       return false;
    }

    class ViewHolder extends BaseViewHolder {
        ImageView iv;
        ImageView iv_close;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            iv_close = itemView.findViewById(R.id.iv_close);
        }
    }

    CloseCheckListener closeCheckListener;

    public void setCloseCheckListener(CloseCheckListener listener) {
        this.closeCheckListener = listener;
    }


    public interface CloseCheckListener {

        void onCloseClick(int position);
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        PhotoAdapter.ViewHolder holder = new PhotoAdapter.ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.photo_recyclerview_layout, null));


        return holder;
    }

}
