package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by ZHAI on 2017/12/18.
 */

public class ImageAndVideoAdapter extends BaseRecyclerAdapter {
    private Activity mActivity;
    private List<Image> mList = new ArrayList<>();
    private boolean hasAdd = false;
    private RequestOptions options;
    private int MAX_IMAGE_NUM = 9;
    public static final int MAX_ITEM_COUNT = 9;
    public static final String ITEM_ADD = "url://add_icon";
    public static final String ITEM_ADD_VIDEO = "url://add_icon_video";
    public static final String THIS_IMAGE_IS_VIDEO = "this_image_is_video";

    public void setMaxImageNum(int MAX_IMAGE_NUM) {
        this.MAX_IMAGE_NUM = MAX_IMAGE_NUM;
    }

    public ImageAndVideoAdapter(Activity activity) {
        options = new RequestOptions().centerCrop()
                .error(R.mipmap.error_bg)
                .placeholder(R.mipmap.error_bg);
        this.mActivity = activity;
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
        ImageAndVideoAdapter.ViewHolder itemViewHolder = (ImageAndVideoAdapter.ViewHolder) holder;
        String imaUrl = "";
        Image image = (Image) mList.get(position);
        imaUrl = image.getImageUrl();

        if (ITEM_ADD.equals(imaUrl)) {
            itemViewHolder.iv_close.setVisibility(View.INVISIBLE);
            itemViewHolder.iv.setImageResource(R.drawable.galary_add_photo);
            itemViewHolder.iv_close.setOnClickListener(null);
            itemViewHolder.video_play_view.setVisibility(View.GONE);
        } else if (ITEM_ADD_VIDEO.equals(imaUrl)) {
            itemViewHolder.iv_close.setVisibility(View.INVISIBLE);
            itemViewHolder.iv.setImageResource(R.drawable.add_video);
            itemViewHolder.iv_close.setOnClickListener(null);
            itemViewHolder.video_play_view.setVisibility(View.GONE);
        } else {
            itemViewHolder.iv.setClickable(false);
            itemViewHolder.iv_close.setVisibility(View.VISIBLE);
            if (imaUrl.equals(THIS_IMAGE_IS_VIDEO)) {
                String thumbnailUrl = image.getThumbnailUrl();
                if (thumbnailUrl.startsWith("http")) {
                    Glide.with(mActivity).asBitmap().load(image.getPhotographTime()).apply(options).into(itemViewHolder.iv);
                } else {
                    Glide.with(mActivity).load(image.getThumbnailUrl()).apply(options).thumbnail(0.1f).into(itemViewHolder.iv);
                }
                itemViewHolder.video_play_view.setVisibility(View.VISIBLE);
            } else {
                Glide.with(mActivity).asBitmap().load(imaUrl).apply(options).into(itemViewHolder.iv);
                itemViewHolder.video_play_view.setVisibility(View.GONE);
            }
            itemViewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeCheckListener.onCloseClick(position);
                    mList.remove(position);
                    checkAddIconShow();
                }
            });
        }

    }

    public void addItemData(boolean isVideo, Image image) {

        if (isVideo) {
            if (image != null) {
                mList.add(0, image);
            }
        } else {
            if (image != null) {
                mList.add(image);
            }
        }

        checkAddIconShow();
    }

    private void checkAddIconShow() {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getImageUrl().equals(ITEM_ADD)) {
                mList.remove(i);
            }
        }
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getImageUrl().equals(ITEM_ADD_VIDEO)) {
                mList.remove(i);
            }
        }


        //第一个是视频
        if (mList.size() == 0) {
            Image image2 = new Image();
            image2.setImageUrl(ITEM_ADD);
            mList.add(image2);

            Image image3 = new Image();
            image3.setImageUrl(ITEM_ADD_VIDEO);
            mList.add(image3);
        } else {
            if (mList.get(0).getImageUrl().equals(THIS_IMAGE_IS_VIDEO)) {
                //最后一个是添加图片
                if (mList.size() < MAX_IMAGE_NUM + 1) {
                    Image image2 = new Image();
                    image2.setImageUrl(ITEM_ADD);
                    mList.add(image2);
                }

            } else {//最后一个是添加视频，倒数第二个是添加图片
                if (mList.size() < MAX_IMAGE_NUM) {
                    Image image2 = new Image();
                    image2.setImageUrl(ITEM_ADD);
                    mList.add(image2);
                }
                Image image3 = new Image();
                image3.setImageUrl(ITEM_ADD_VIDEO);
                mList.add(image3);

            }
        }

        notifyDataSetChanged();
    }


    class ViewHolder extends BaseViewHolder {
        ImageView iv;
        ImageView iv_close;
        ImageView video_play_view;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            iv_close = itemView.findViewById(R.id.iv_close);
            video_play_view = itemView.findViewById(R.id.video_play_view);
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

        ImageAndVideoAdapter.ViewHolder holder = new ImageAndVideoAdapter.ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.photo_recyclerview_layout, null));


        return holder;
    }

}
