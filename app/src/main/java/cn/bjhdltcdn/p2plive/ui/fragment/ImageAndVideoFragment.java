package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.presenter.SaveActivePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.LocalVideoPlayActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PublishVideoActivity;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.ImageAndVideoAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditSelectDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.UserIconDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.BitmapUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.UriUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhaiww on 2018/5/7.
 */

public class ImageAndVideoFragment extends BaseFragment implements BaseView {

    private List<Image> imageList = new ArrayList<>();//初始图片集合
    private String videoPath;
    private int selectPosition = -1;
    private View rootView;
    private RecyclerView recyclerPhoto;
    private SaveActivePresenter saveActivePresenter;
    private ImageAndVideoAdapter imageAndVideoAdapter;
    private int MAX_IMAGE_NUM = 9;
    private final int INTENT_PHOTO_CODE = 4;
    private final int VIDEO_PICKER_CODE = 2;
    private final int VIDEO_CODE = 1;
    private String videoSuffixs[] = {"mp4", "avi", "mov", "mkv", "flv", "f4v", "rmvb"};
    private int imgWidth, imgHeight;
    private String videoImgUrl;
    private long imageId;//删除图片的id
    private int type;//删除图片用的type 类型(21-->店铺宣传图片,22-->商品图片,其他不用传值),

    public void setType(int type) {
        this.type = type;
    }

    /**
     * @param MAX_IMAGE_NUM 显示最大图片数量
     */
    public void setMaxImageNum(int MAX_IMAGE_NUM) {
        this.MAX_IMAGE_NUM = MAX_IMAGE_NUM;
    }

    /**
     * @param imageList   初始化服务器图片集合
     * @param imageId     视频id
     * @param videoPath   视频路径
     * @param videoImgUrl 初始化服务器视频缩略图路径
     */
    public void setServerData(List<Image> imageList, String videoPath, String videoImgUrl, long imageId) {
        this.imageList = imageList;
        this.videoPath = videoPath;
        this.videoImgUrl = videoImgUrl;
        this.imageId = imageId;
    }

    /**
     * @return 获取图片集合
     */
    public List<Image> getNewImageList() {
        List<Image> newImageList = new ArrayList<>();
        newImageList.clear();
        List<Image> imageList = imageAndVideoAdapter.getmList();
        for (int i = 0; i < imageAndVideoAdapter.getmList().size(); i++) {
            if (imageList.get(i).getImageUrl().equals(imageAndVideoAdapter.THIS_IMAGE_IS_VIDEO) || imageList.get(i).getImageUrl().equals(imageAndVideoAdapter.ITEM_ADD) || imageList.get(i).getImageUrl().equals(imageAndVideoAdapter.ITEM_ADD_VIDEO)) {
                continue;
            }
            newImageList.add(imageAndVideoAdapter.getmList().get(i));

        }
        return newImageList;
    }


    /**
     * @return 获取需要上传到服务器的视频路径
     */
    public String getVideoUrl() {
        String thumbnailUrl = "";
        if (imageAndVideoAdapter.getmList().get(0).getImageUrl().equals(imageAndVideoAdapter.THIS_IMAGE_IS_VIDEO)) {
            thumbnailUrl = imageAndVideoAdapter.getmList().get(0).getThumbnailUrl();
            if (thumbnailUrl != null) {
                if (thumbnailUrl.startsWith("http")) {
                    thumbnailUrl = "";
                }
            }
        }
        return thumbnailUrl;
    }

    /**
     * @return 获取本界面上展示的视频路径
     */
    public String getVideoUrlLocal() {
        String thumbnailUrl = "";
        if (imageAndVideoAdapter.getmList().get(0).getImageUrl().equals(imageAndVideoAdapter.THIS_IMAGE_IS_VIDEO)) {
            thumbnailUrl = imageAndVideoAdapter.getmList().get(0).getThumbnailUrl();
        }
        return thumbnailUrl;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    @Override
    protected void onVisible(boolean isInit) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_image_album_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerPhoto = rootView.findViewById(R.id.recycler_photo);
        initPhoto();
    }

    private void initPhoto() {

        imageAndVideoAdapter = new ImageAndVideoAdapter(getActivity());
        imageAndVideoAdapter.setMaxImageNum(MAX_IMAGE_NUM);
        imageAndVideoAdapter.addItemData(false, null);

        for (int i = 0; i < imageList.size(); i++) {
            if (imageList.get(i).getImageUrl().startsWith("http://")) {
                imageAndVideoAdapter.addItemData(false, imageList.get(i));
            }
        }

        //服务器视频是否为空
        if (videoPath != null) {
            if (!StringUtils.isEmpty(videoPath.trim())) {
                Image image = new Image();
                image.setImageId(imageId);
                image.setImageUrl(imageAndVideoAdapter.THIS_IMAGE_IS_VIDEO);
                image.setThumbnailUrl(videoPath);
                image.setPhotographTime(videoImgUrl);
                imageAndVideoAdapter.addItemData(true, image);
            }
        }

        imageAndVideoAdapter.notifyDataSetChanged();
        recyclerPhoto.setAdapter(imageAndVideoAdapter);

        imageAndVideoAdapter.setCloseCheckListener(new ImageAndVideoAdapter.CloseCheckListener() {
            @Override
            public void onCloseClick(int position) {
                Image image = imageAndVideoAdapter.getmList().get(position);
                if (image.getImageUrl().startsWith("http://")) {// 网络图片
                    selectPosition = position;
                    // 调用网络接口
                    getSaveActivePresenter().deleteImage(image.getImageId(), 0);
                }

                String thumbnailUrl = image.getThumbnailUrl();
                if (thumbnailUrl != null) {
                    if (image.getThumbnailUrl().startsWith("http://")) {
                        getSaveActivePresenter().deleteImage(image.getImageId(), type);
                    }
                }
            }
        });

        imageAndVideoAdapter.setOnItemListener(new ItemListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (imageAndVideoAdapter.getItemCount() > position) {
                    Image image = imageAndVideoAdapter.getmList().get(position);
                    List<Image> imageList = imageAndVideoAdapter.getmList();
                    int imageCount;

                    if (imageList.get(0).getImageUrl().equals(imageAndVideoAdapter.THIS_IMAGE_IS_VIDEO)) {//如果第一个是视频
                        if (imageList.get(imageAndVideoAdapter.getItemCount() - 1).getImageUrl().equals(imageAndVideoAdapter.ITEM_ADD)) {
                            imageCount = imageAndVideoAdapter.getItemCount() - 2;
                        } else {
                            imageCount = imageAndVideoAdapter.getItemCount() - 1;
                        }
                    } else {//最后一个是添加视频
                        if (imageList.get(imageAndVideoAdapter.getItemCount() - 2).getImageUrl().equals(imageAndVideoAdapter.ITEM_ADD)) {
                            imageCount = imageAndVideoAdapter.getItemCount() - 2;
                        } else {
                            imageCount = imageAndVideoAdapter.getItemCount() - 1;
                        }
                    }

                    if (image.getImageUrl().equals(imageAndVideoAdapter.ITEM_ADD)) {
                        PictureSelector.create(ImageAndVideoFragment.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(MAX_IMAGE_NUM - imageCount)// 最大图片选择数量 int
                                .minSelectNum(1)// 最小选择数量 int
                                .imageSpanCount(4)// 每行显示个数 int
                                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                                .previewImage(true)// 是否可预览图片 true or false
                                .isCamera(true)// 是否显示拍照按钮 true or false
                                .enableCrop(false)// 是否裁剪 true or false
                                .compress(true)// 是否压缩 true or false
                                .glideOverride(Utils.dp2px(110), Utils.dp2px(110))// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                                .isGif(true)// 是否显示gif图片 true or false
                                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                                .openClickSound(true)// 是否开启点击声音 true or false
                                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                                .minimumCompressSize(100)// 小于100kb的图片不压缩
                                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                                .cropWH(Utils.dp2px(110), Utils.dp2px(110))// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                                .forResult(INTENT_PHOTO_CODE);
                    } else if (image.getImageUrl().equals(imageAndVideoAdapter.ITEM_ADD_VIDEO)) {
                        ActiveEditSelectDialog dialog = new ActiveEditSelectDialog();
                        dialog.setItemText("拍摄", "从手机相册选择");
                        dialog.setItemClick(new ActiveEditSelectDialog.ItemClick() {
                            @Override
                            public void itemClick(int position) {
                                switch (position) {
                                    case 1:
                                        //拍摄
                                        Intent intent = new Intent(getActivity(), PublishVideoActivity.class);
                                        intent.putExtra(Constants.Fields.TYPE, 1);
                                        startActivityForResult(intent, VIDEO_CODE);
                                        break;
                                    case 2:
                                        //从手机相册选择
                                        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                        pickIntent.setType("video/*");
                                        pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                        startActivityForResult(pickIntent, VIDEO_PICKER_CODE);
                                        break;
                                    default:
                                }
                            }
                        });
                        dialog.show(getFragmentManager());
                    } else if (image.getImageUrl().equals(imageAndVideoAdapter.THIS_IMAGE_IS_VIDEO)) {
                        //ImageViewPageDialog.newInstance(imageList2, position).show(getActivity().getSupportFragmentManager());
                        if (videoPath.startsWith("http")) {
                            Intent intent = new Intent(getActivity(), VideoPlayFullScreenActivity.class);
                            intent.putExtra(Constants.Fields.VIDEO_PATH, videoPath);
                            intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL, videoImgUrl);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), LocalVideoPlayActivity.class);
                            intent.putExtra(Constants.Fields.VIDEO_PATH, image.getThumbnailUrl());
                            getActivity().startActivity(intent);
                        }
                    } else {
                        UserIconDialog.newInstance(image.getImageUrl()).show(getFragmentManager(), "dialog");
//                        ImageViewPageDialog.newInstance(getNewImageList(), position).show(getActivity().getSupportFragmentManager());
                    }
                }
            }


        });

    }

    public SaveActivePresenter getSaveActivePresenter() {
        if (saveActivePresenter == null) {
            saveActivePresenter = new SaveActivePresenter(this);
        }
        return saveActivePresenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case INTENT_PHOTO_CODE:// 选择相册
                    // 图片选择结果回调
                    List<LocalMedia> seleList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    if (seleList != null || seleList.size() > 0) {
                        for (int i = 0; i < seleList.size(); i++) {
                            LocalMedia localMedia = seleList.get(i);
                            if (localMedia.isCompressed()) {
                                String selectFileUrl = localMedia.getCompressPath();
                                Image image = new Image();
                                image.setImageUrl(selectFileUrl);

                                imageAndVideoAdapter.addItemData(false, image);
                            }
                        }
                    }

                    break;
                case VIDEO_CODE:
                case VIDEO_PICKER_CODE:
                    if (VIDEO_PICKER_CODE == requestCode) {
                        Uri selectedMediaUri = data.getData();
                        videoPath = UriUtils.getFileAbsolutePath(App.getInstance(), selectedMediaUri);
                    } else if (VIDEO_CODE == requestCode) {
                        videoPath = data.getStringExtra(Constants.KEY.KEY_EXTRA);
                    }

                    Log.e("videoPath ==== ", videoPath);

                    if (StringUtils.isEmpty(videoPath)) {
                        Log.e("==文件路径是null== ", videoPath);
                        return;
                    }

                    File file = new File(videoPath);
                    Log.e("file.length() ==== ", file.length() + "");

                    if (file.length() <= 0) {
                        Utils.showToastShortTime("对不起，您选择的文件无效");
                        return;
                    }

                    // 视频类型
                    try {
                        String suffix = videoPath.substring(videoPath.lastIndexOf(".") + 1, videoPath.length());
                        Log.e("suffix ==== ", suffix);
                        if (StringUtils.isEmpty(suffix)) {
                            Utils.showToastShortTime("对不起，您选择的文件格式无效");
                            return;
                        }

                        // 判断文件是否是视频文件
                        boolean isVideo = false;
                        for (int i = 0; i < videoSuffixs.length; i++) {
                            if (videoSuffixs[i].equals(suffix)) {
                                isVideo = true;
                                break;
                            }
                        }


                        if (!isVideo) {
                            Utils.showToastShortTime("视频文件格式(" + suffix + ")不正确");
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showToastShortTime("对不起，系统解析文件错误");
                        return;
                    }

                    if (file.length() / (1024 * 1024) > 50) {
                        Utils.showToastShortTime("文件过大，请不要超过50M");
                        return;
                    }

                    if (!StringUtils.isEmpty(videoPath)) {
                        Image image = new Image();
                        image.setImageUrl(imageAndVideoAdapter.THIS_IMAGE_IS_VIDEO);
                        image.setThumbnailUrl(videoPath);
                        imageAndVideoAdapter.addItemData(true, image);

                        // 视频缩略图
                        RequestOptions options = new RequestOptions().centerCrop();
                        Glide.with(App.getInstance()).asBitmap().load(videoPath).apply(options).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                if (resource != null) {
                                    int width = resource.getWidth();
                                    int height = resource.getHeight();

                                    Logger.d("width =原图1== " + width + " ; height =原图1== " + height);

                                    float ww = BitmapUtils.getBitmapWidth(App.getInstance());
                                    float hh = BitmapUtils.getBitmapHeight(App.getInstance());
                                    Logger.d("ww ==指定大小== " + ww + " ; hh ==指定大小== " + hh);

                                    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                                    double scale = 1;//scale=1表示不缩放
                                    if (width > height && width > ww) {//如果宽度大的话根据宽度固定大小缩放
                                        scale = width / ww;
                                    } else if (width < height && height > hh) {//如果高度高的话根据宽度固定大小缩放
                                        scale = height / hh;
                                    }

                                    Logger.d("scale ==缩放比== " + scale);

                                    if (scale < 1) {
                                        scale = 1;
                                    }

                                    width = (int) (width / scale);
                                    height = (int) (height / scale);

                                    Logger.d("width =scale=最终= " + width + " ; height =scale=最终= " + height);

                                    imgWidth = width;
                                    imgHeight = height;


                                }
                            }
                        });

                    } else {
                        Utils.showToastShortTime("没有选择视频文件!");
                    }

                    break;
            }
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_DELETEIMAGE:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    Utils.showToastShortTime(response.getMsg());
                }
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public OnImageNumChange onImageNumChange;

    //删除、增加图片时的回调
    public void setOnImageNumChange(OnImageNumChange onImageNumChange) {
        this.onImageNumChange = onImageNumChange;
    }

    public interface OnImageNumChange {
        void OnImageNum(int num);
    }
}
