package cn.bjhdltcdn.p2plive.ui.adapter;

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

public class PostPublishImageRecycleAdapter extends BaseRecyclerAdapter {
    private List<Image> list;
    private AppCompatActivity mActivity;
    private boolean isDelete = false;
    private final int ITEM_TYPE_1 = 1;
    private final int ITEM_TYPE_2 = 2;
    private int screenWidth;
    private RequestOptions options;
    private OnDeleteItemClick onDeleteItemClick;

    public PostPublishImageRecycleAdapter(List<Image> list, AppCompatActivity mActivity) {
        this.list = list;

        this.mActivity = mActivity;

        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
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
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.post_publish_image_recyclerview_layout, null));
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null && list.size() > 0) {
            Image info = list.get(position);
            if (info.getThumbnailUrl().contains("photo_up_icon")) {
                return ITEM_TYPE_1;
            }
            return ITEM_TYPE_2;
        }
        return super.getItemViewType(position);
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        try {
            if (getItemCount() > 0) {
                if (holder instanceof ImageHolder) {
                    ImageHolder imageHolder = (ImageHolder) holder;
                    int itemType = getItemViewType(position);
                    Image image = list.get(position);
                    // 用此字段标记图片位置
                    String thumbnailUrl = image.getThumbnailUrl();
                    switch (itemType) {
                        case ITEM_TYPE_1:
                            imageHolder.imageView.setImageResource(R.mipmap.post_publish_add_img_icon);
                            break;

                        case ITEM_TYPE_2:
                            Glide.with(mActivity).load(thumbnailUrl).apply(options).into(imageHolder.imageView);
                            break;
                    }


                    if (isDelete) {
                        String temp = list.get(position).getThumbnailUrl();
                        if ((!StringUtils.isEmpty(temp)) && (!temp.contains("icon://"))) {
                            imageHolder.delect.setVisibility(View.VISIBLE);
                        } else {
                            imageHolder.delect.setVisibility(View.GONE);
                        }
                    } else {
                        imageHolder.delect.setVisibility(View.GONE);
                    }

                    imageHolder.delect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DelectTipDialog dialog = new DelectTipDialog();
                            dialog.setTitleStr("确定删除照片？");
                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
                                @Override
                                public void itemClick() {

                                    if (onDeleteItemClick != null) {
                                        onDeleteItemClick.deleteItemClick(position);
                                    }

                                }
                            });
                            dialog.show(mActivity.getSupportFragmentManager());
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

            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            delect = (ImageView) itemView.findViewById(R.id.image_delect);


            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.height = (screenWidth - Utils.dp2px(10) * 4) / 3-Utils.dp2px(8);
            imageView.setLayoutParams(layoutParams);

        }

    }

    public void setList(List<Image> insertList) {

        if (this.list == null) {
            this.list = new ArrayList<>(1);
        }

        if (this.list.size() > 0) {
            this.list.addAll(this.list.size(), insertList);
        }else {
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