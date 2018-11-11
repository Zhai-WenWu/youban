package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.ui.dialog.DelectDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;

public class ActivePublishImageRecycleAdapter extends BaseRecyclerAdapter {
    private List<Image> list;
    private List<Image> uploadList;
    private RequestOptions options;
    private OnDelectItemClick onDelectItemClick;
    private AppCompatActivity mActivity;
    private int screenWidth;
    public ActivePublishImageRecycleAdapter(AppCompatActivity activity) {
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        mActivity=activity;
        screenWidth= PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0];
    }

    public void addList(List<Image> insertList) {
        if (this.list == null) {
            this.list=new ArrayList<Image>();
        }
            if (this.list.size() > 0) {
                this.list.addAll(this.list.size(), insertList);
            }else{
                this.list.addAll(0, insertList);
            }
        notifyDataSetChanged();
    }

    public void setList(List<Image> list) {
        this.list = list;
    }

    public void removeItem(int position){
        if(list!=null){
            list.remove(position);
            notifyDataSetChanged();
        }

    }

    public Image getItem(int position){
        return list.get(position);
    }


    public List<Image> getList() {
        return list;
    }

   public List<Image> getUploadList(){
        if(uploadList==null){
            uploadList=new ArrayList<Image>();
        }else{
            uploadList.clear();
        }
        if(list!=null)
        {
           for (int i=0;i<list.size();i++){
               if(list.get(i).getYesOrNoUpload()==1){
                   uploadList.add(list.get(i));
               }
           }
        }
        return uploadList;
   }

    public void setOnDelectItemClick(OnDelectItemClick onDelectItemClick) {
        this.onDelectItemClick = onDelectItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.active_publish_img_recycle_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final ImageHolder viewHolder = (ImageHolder) holder;
        Image image = list.get(position);
//        Glide.with(App.getInstance()).load(image.getImageUrl()).apply(options).into(viewHolder.imageView);
        Glide.with(App.getInstance()).load(image.getImageUrl()).apply(options).into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                int imageWidth = resource.getIntrinsicWidth();
                int imageHeight = resource.getIntrinsicHeight();
                int height = screenWidth * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = viewHolder.imageView.getLayoutParams();
                para.height = height;
                para.width = screenWidth;
                viewHolder.imageView.setImageDrawable(resource);
            }
        });
        viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelectTipDialog dialog = new DelectTipDialog();
                dialog.setTitleStr("确定删除图片吗？");
                dialog.setItemClick(new DelectTipDialog.ItemClick() {
                    @Override
                    public void itemClick() {
                        onDelectItemClick.delectItemClick(position);
                    }
                });
                dialog.show(mActivity.getSupportFragmentManager());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null && list.size() > 0) ?list.size() : 0;
    }

    class ImageHolder extends BaseViewHolder {
        ImageView imageView,deleteImg;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            deleteImg= (ImageView) itemView.findViewById(R.id.close_img);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//            layoutParams.width = PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0] ;
//            layoutParams.height = layoutParams.width/2;

        }

    }

    public interface OnDelectItemClick {
        void delectItemClick(int position);

    }
}