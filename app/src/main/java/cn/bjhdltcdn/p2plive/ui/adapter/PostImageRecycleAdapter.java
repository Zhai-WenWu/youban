package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

public class PostImageRecycleAdapter extends BaseRecyclerAdapter {
    private List<Image> list;
    // 列数
    private int mSpanCount;
    // 分割线的宽度
    private int mSpaceWidth;
    private RequestOptions options;
    private int screenWidth;

    public PostImageRecycleAdapter(Context context, List<Image> list, int mSpanCount, int mSpaceWidth) {
        this.list = list;
        this.mSpanCount = mSpanCount;
        this.mSpaceWidth = mSpaceWidth;
        options = new RequestOptions()
//                .transform(new GlideRoundTransform( 9))
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        screenWidth=PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0];
    }

    public void setList(List<Image> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.post_img_list_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        ImageHolder viewHolder = (ImageHolder) holder;
        Image image = list.get(position);
        String thumbnailUrl = image.getThumbnailUrl();
        ImageView imageView=viewHolder.imageView;
        Glide.with(App.getInstance()).load(thumbnailUrl).apply(options).into(imageView);

        if (list.size() > 3) {
            if (position == 2) {
                //最后一张展示总个数
                viewHolder.imgeNumView.setText(list.size() + "");
                viewHolder.imgeNumView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imgeNumView.setVisibility(View.GONE);
            }
        } else {
            viewHolder.imgeNumView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            if (list.size() > 3) {
                return 3;
            } else {
                return list.size();
            }
        } else {
            return 0;
        }
    }

    class ImageHolder extends BaseViewHolder {
        ImageView imageView;
        TextView imgeNumView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imgeNumView = (TextView) itemView.findViewById(R.id.image_num_tv);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = (screenWidth - 2 * mSpaceWidth - 2 * Utils.dp2px(10)) / mSpanCount;
            layoutParams.height = layoutParams.width;

        }

    }
}