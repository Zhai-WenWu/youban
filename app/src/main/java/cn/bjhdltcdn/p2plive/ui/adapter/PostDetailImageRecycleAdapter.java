package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;

public class PostDetailImageRecycleAdapter extends BaseRecyclerAdapter {
    private List<Image> list;
    private RequestOptions options;
    private int screenWidth;

    private ImageViewPageDialogClick imageViewPageDialogClick;

    public void setImageViewPageDialogClick(ImageViewPageDialogClick imageViewPageDialogClick) {
        this.imageViewPageDialogClick = imageViewPageDialogClick;
    }

    public PostDetailImageRecycleAdapter(Context context) {
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        screenWidth = PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0];
    }

    public void setList(List<Image> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.post_detail_img_recycle_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final ImageHolder viewHolder = (ImageHolder) holder;
        Image image = list.get(position);

        viewHolder.imageView.setImageResource(R.mipmap.error_bg);
        // 显示缩列图
        Glide.with(App.getInstance()).load(image.getImageUrl()).apply(options).into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (resource != null) {
                    int imageWidth = resource.getIntrinsicWidth();
                    int imageHeight = resource.getIntrinsicHeight();
                    int height = screenWidth * imageHeight / imageWidth;
                    ViewGroup.LayoutParams para = viewHolder.imageView.getLayoutParams();
                    para.height = height;
                    para.width = screenWidth;
                    viewHolder.imageView.setImageDrawable(resource);
                }

            }
        });

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewPageDialogClick != null) {
                    imageViewPageDialogClick.onClick(list, position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    class ImageHolder extends BaseViewHolder {
        ImageView imageView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }

    }

    public interface ImageViewPageDialogClick {
        void onClick(List<Image> mList, int currentItem);
    }
}