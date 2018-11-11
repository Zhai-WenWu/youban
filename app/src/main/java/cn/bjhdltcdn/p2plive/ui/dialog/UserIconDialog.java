package cn.bjhdltcdn.p2plive.ui.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.utils.FileUtils;

/**
 * Created by ZHAI on 2018/1/2.
 */

public class UserIconDialog extends DialogFragment {

    private View rootView;
    private String avatarUrl;
    private Activity mContext;
    private SubsamplingScaleImageView imageview;
    private LinearLayout ll_icon_dialog;


    public static UserIconDialog newInstance(String avatarUrl) {
        UserIconDialog f = new UserIconDialog();
        Bundle args = new Bundle();
        args.putString(Constants.Action.ACTION_POSITION_EXTRA, avatarUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_icon_image, null);
        initView();
        return rootView;
    }

    private void initView() {
        imageview = (SubsamplingScaleImageView) rootView.findViewById(R.id.simple_image);
        ll_icon_dialog = (LinearLayout) rootView.findViewById(R.id.ll_icon_dialog);

        Bundle bundle = getArguments();
        avatarUrl = bundle.getString(Constants.Action.ACTION_POSITION_EXTRA);

        RequestOptions options = new RequestOptions();
        options.centerCrop().placeholder(R.mipmap.error_group_icon);
        final Bitmap[] bitmap = {null};
        Glide.with(this).asBitmap().load(avatarUrl).apply(options).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                if (!isAdded()) {
                    return;
                }

                if (resource != null) {
                    imageview.setImage(ImageSource.bitmap(resource),new ImageViewState(1.2F, new PointF(0, 0), 0));
                }

                bitmap[0] = resource;
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);

            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //setParams();

        imageview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
                postDetailCommentDialog.setItemStr(new String[]{"保存到手机"});
                postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
                    @Override
                    public void itemClick(int position, String content) {
                        if (bitmap[0] != null) {
                            FileUtils.saveImageToGallery(App.getInstance(), bitmap[0]);
                        }
                    }
                });
                // postDetailCommentDialog.show(this);
                postDetailCommentDialog.show(getFragmentManager());
                return false;
            }
        });

    }

    @Override
    public void dismiss() {
        super.dismiss();
        //ImageViewPageDialog.super.dismissAllowingStateLoss();

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            // 解决java.lang.IllegalStateException: Can not perform this action问题
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }
}
