package cn.bjhdltcdn.p2plive.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.ui.dialog.PostDetailCommentDialog;
import cn.bjhdltcdn.p2plive.utils.FileUtils;

/**
 * Created by xiawenquan on 18/1/8.
 */

public class ImageViewPageFragment extends BaseFragment {

    private View rootView;

    private Image image;

    private RequestOptions options;
    private Bitmap[] bitmap = {null};

    private ProgressBar progressBar;
    private SubsamplingScaleImageView largeImageView;


    public static ImageViewPageFragment newInstance(Image image) {
        ImageViewPageFragment fragment = new ImageViewPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY.KEY_OBJECT, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            image = getArguments().getParcelable(Constants.KEY.KEY_OBJECT);
        }

        Logger.d("image ====>>>>> " + image);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.image_view_page_layout, container, false);

        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        Logger.d("onCreateView() ==== ");

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        largeImageView = (SubsamplingScaleImageView) rootView.findViewById(R.id.imageView);

        largeImageView.setEnabled(true);


        return rootView;

    }

    @Override
    protected void onVisible(boolean isInit) {

        Logger.d("isInit ==== " + isInit);

        String imageUrl = image == null ? "" : image.getImageUrl();

        Logger.d("  === imageUrl === " + imageUrl);

        Glide.with(App.getInstance()).asBitmap().load(imageUrl)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                        if (resource != null) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }

                            if (largeImageView != null) {
                                largeImageView.setImage(ImageSource.bitmap(resource),new ImageViewState(1.2F, new PointF(0, 0), 0));
                            }

                            if (bitmap != null && bitmap.length > 0) {
                                bitmap[0] = resource;
                            }

                        }


                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

        largeImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
                postDetailCommentDialog.setItemStr(new String[]{"保存到手机"});
                postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
                    @Override
                    public void itemClick(int position, String content) {
                        if (progressBar != null && progressBar.getVisibility() == View.GONE && bitmap[0] != null) {
                            FileUtils.saveImageToGallery(App.getInstance(), bitmap[0]);
                        }
                    }
                });
                postDetailCommentDialog.show(getFragmentManager());
                return false;
            }
        });

        largeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitmap != null && bitmap[0] != null) {
                    bitmap = null;
                }


            }
        });

    }
}
