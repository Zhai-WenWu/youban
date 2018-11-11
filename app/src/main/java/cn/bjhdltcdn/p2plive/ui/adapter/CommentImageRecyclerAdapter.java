package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 评论图片adapter
 */
public class CommentImageRecyclerAdapter extends BaseRecyclerAdapter {
    private List<Image> list;
    private Context mContext;
    private RequestOptions options;
    private OnDeleteItemClick onDeleteItemClick;

    public CommentImageRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
    }

    public void setOnDeleteItemClick(OnDeleteItemClick onDeleteItemClick) {
        this.onDeleteItemClick = onDeleteItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.comment_image_recyclerview_layout, null));
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        try {
            if (getItemCount() > 0) {
                if (holder instanceof ImageHolder) {
                    ImageHolder imageHolder = (ImageHolder) holder;
                    Image image = list.get(position);
                    String imageUrl = image.getImageUrl();
                    Glide.with(mContext).load(imageUrl).apply(options).into(imageHolder.imageView);

                    imageHolder.delect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onDeleteItemClick != null) {
                                onDeleteItemClick.deleteItemClick(position);
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            if (list.size() > 9) {
                return list.size() - 1;
            } else {
                return list.size();
            }
        } else {
            return 0;
        }
    }

    class ImageHolder extends BaseViewHolder {
        ImageView imageView, delect;

        public ImageHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            delect = itemView.findViewById(R.id.image_delect);

        }

    }

    public void setList(List<Image> insertList) {

        if (this.list == null) {
            this.list = new ArrayList<>(1);
        }

        if (this.list.size() > 0) {
            this.list.addAll(this.list.size(), insertList);
        } else {
            this.list.addAll(0, insertList);
        }

    }


    public List<Image> getList() {
        return list;
    }

    public interface OnDeleteItemClick {
        void deleteItemClick(int position);

    }

}