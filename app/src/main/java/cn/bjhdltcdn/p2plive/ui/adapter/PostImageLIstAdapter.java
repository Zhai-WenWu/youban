package cn.bjhdltcdn.p2plive.ui.adapter;

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
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;

public class PostImageLIstAdapter extends BaseRecyclerAdapter {
    private List<Image> list;
    // 列数
    private int mSpanCount;
    // 分割线的宽度
    private int mSpaceWidth;
    private RequestOptions options;

    public PostImageLIstAdapter(List<Image> list, int mSpanCount, int mSpaceWidth) {
        this.list = list;
        this.mSpanCount = mSpanCount;
        this.mSpaceWidth = mSpaceWidth;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
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
        Glide.with(App.getInstance()).load(image.getImageUrl()).apply(options).into(viewHolder.imageView);
        if(position==2&&list.size()>3){
          //最后一张展示总个数
            viewHolder.imgeNumView.setText(list.size()+"");
            viewHolder.imgeNumView.setVisibility(View.VISIBLE);
        }else{
            viewHolder.imgeNumView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ? 3 : 0;
    }

    class ImageHolder extends BaseViewHolder {
        ImageView imageView;
        TextView imgeNumView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imgeNumView= (TextView) itemView.findViewById(R.id.image_num_tv);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = (PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0] - 2 * mSpaceWidth) / mSpanCount;
            layoutParams.height = layoutParams.width;

        }

    }
}