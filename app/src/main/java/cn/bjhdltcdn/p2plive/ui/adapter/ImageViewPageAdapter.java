package cn.bjhdltcdn.p2plive.ui.adapter;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.ProductImage;
import cn.bjhdltcdn.p2plive.model.StoreImage;
import cn.bjhdltcdn.p2plive.ui.dialog.PostDetailCommentDialog;
import cn.bjhdltcdn.p2plive.utils.FileUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.GifFileCallback;


/**
 * Created by wenquan on 2015/10/23.
 */
public class ImageViewPageAdapter extends PagerAdapter {

    private List<Object> mList;

    private DialogFragment dialogFragment;
    private RequestOptions options;
    private RequestOptions gifOptions;

    private Bitmap[] bitmap = new Bitmap[9];
    private int Position;

    public ImageViewPageAdapter(List<Object> mList, DialogFragment dialogFragment) {
        this.mList = mList;
        if(mList!=null&&mList.size()>0){
            bitmap = new Bitmap[mList.size()];
        }
        this.dialogFragment = dialogFragment;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);

        gifOptions = new RequestOptions().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).fitCenter().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    }

    public void setPosition(int position) {
        Position = position;
    }

    @Override
    public int getCount() {
        return mList != null && mList.size() > 0 ? mList.size() : 0;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        if (getCount() > 0) {
            final View itemView = View.inflate(App.getInstance(), R.layout.image_view_page_layout, null);
            final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
//            /**
//             * 1. SetScaleType(ImageView.ScaleType.CENTER);按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
//             * 2. SetScaleType(ImageView.ScaleType.CENTER_CROP);按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
//             * 3. setScaleType(ImageView.ScaleType.CENTER_INSIDE);将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
//             * 4. setScaleType(ImageView.ScaleType.FIT_CENTER);把图片按比例扩大/缩小到View的宽度，居中显示
//             * 5. FIT_START, FIT_END在图片缩放效果上与FIT_CENTER一样，只是显示的位置不同，FIT_START是置于顶部，FIT_CENTER居中，FIT_END置于底部。
//             * 6. FIT_XY 不按比例缩放图片，目标是把图片塞满整个View。
//             */

            final SubsamplingScaleImageView largeImageView = itemView.findViewById(R.id.imageView);
            final ImageView imageView = itemView.findViewById(R.id.image_view);
            largeImageView.setEnabled(true);
            largeImageView.setMinScale(0.5F);//最小显示比例
            largeImageView.setMaxScale(10.0F);//最大显示比例（太大了图片显示会失真，因为一般微博长图的宽度不会太宽）

            Object o = mList.get(position);
            String imageUrl = null;
            if (o instanceof Image) {
                Image image = (Image) o;
                imageUrl = image.getImageUrl();
            }else if(o instanceof ProductImage){
                ProductImage image = (ProductImage) o;
                imageUrl = image.getImageUrl();
            }else if(o instanceof StoreImage){
                StoreImage image = (StoreImage) o;
                imageUrl = image.getImageUrl();
            }

            Logger.d("position ==== " + position + "  === imageUrl === " + imageUrl);

            if (!StringUtils.isEmpty(imageUrl) && imageUrl.toLowerCase().endsWith(".gif")) {

                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                if(largeImageView != null){
                    largeImageView.setVisibility(View.GONE);
                }
                final String finalImageUrl1 = imageUrl;
                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        showPostDetailCommentDialog(progressBar, finalImageUrl1);

                        return false;
                    }
                });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bitmap != null ) {
                            bitmap = null;
                        }

                        if (dialogFragment != null) {
                            dialogFragment.dismiss();
                        }
                    }
                });

                Glide.with(App.getInstance()).load(imageUrl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        return false;
                    }
                }).apply(gifOptions).into(imageView);

            } else {
                Glide.with(App.getInstance()).asBitmap().load(imageUrl)
                        .apply(options)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                                if(dialogFragment != null && dialogFragment.isAdded()){
                                    if (resource != null) {
                                        if (progressBar != null) {
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        if (largeImageView != null) {
                                            largeImageView.setImage(ImageSource.bitmap(resource));
                                        }

                                        if (bitmap != null && bitmap.length > 0) {
                                            bitmap[position] = resource;
                                        }

                                    }

                                }


                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                if (largeImageView != null) {
                                    largeImageView.setImage(ImageSource.resource(R.mipmap.error_bg));
                                }

                            }
                        });
            }


            final String finalImageUrl = imageUrl;
            largeImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPostDetailCommentDialog(progressBar, finalImageUrl);
                    return false;
                }
            });
            largeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (bitmap != null ) {
                        bitmap = null;
                    }

                    if (dialogFragment != null) {
                        dialogFragment.dismiss();
                    }
                }
            });
            container.addView(itemView);
            return itemView;
        }
        return null;
    }

    private void showPostDetailCommentDialog(final ProgressBar progressBar, final String imageUrl){
        PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
        postDetailCommentDialog.setItemStr(new String[]{"保存到手机"});
        postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
            @Override
            public void itemClick(int position, String content) {
                if (progressBar != null && progressBar.getVisibility() == View.GONE && bitmap[Position] != null) {
                    FileUtils.saveImageToGallery(App.getInstance(), bitmap[Position]);
                } else if (imageUrl.toLowerCase().endsWith(".gif")){

                    String suffix = imageUrl.substring(imageUrl.lastIndexOf("."),imageUrl.length());

                    try {
                        ApiData.getInstance().saveGifFile(imageUrl,new GifFileCallback(suffix));
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (dialogFragment != null && dialogFragment.isAdded()) {
                            Utils.showToastShortTime("保存动图失败");
                        }

                    }
                }
            }
        });
        postDetailCommentDialog.show(dialogFragment.getFragmentManager());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
