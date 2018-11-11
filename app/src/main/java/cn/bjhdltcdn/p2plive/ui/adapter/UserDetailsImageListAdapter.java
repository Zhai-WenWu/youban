package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by ZHUDI on 2017/11/20.
 */

public class UserDetailsImageListAdapter extends BaseRecyclerAdapter {
    private List<Image> list_image;
    private Activity mActivity;
    private FragmentManager supportFragmentManager;

    public UserDetailsImageListAdapter(List<Image> list_image, Activity activity, FragmentManager supportFragmentManager) {
        this.list_image = list_image;
        this.mActivity = activity;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public int getItemCount() {
        return list_image == null ? 0 : list_image.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_image_user_details, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                ImageHolder imageHolder = (ImageHolder) holder;
                Image image = list_image.get(position);
                RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.ic_launcher);
                Glide.with(App.getInstance()).asBitmap().load(image.getImageUrl()).apply(options).into(imageHolder.imageView);
                imageHolder.setItemListener(new ItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ImageViewPageDialog.newInstance(list_image, position).show(supportFragmentManager);
                        /*Intent intent = new Intent(mActivity, PhotoAlbumActivity.class);
                        intent.putExtra(Constants.Fields.PHOTO_ALBUM, (Serializable) list_image);
                        intent.putExtra(Constants.Fields.PAGE_NUMBER,position);
                        mActivity.startActivity(intent);*/
                    }
                });
            }
        }
    }

    class ImageHolder extends BaseViewHolder {
        private ImageView imageView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
