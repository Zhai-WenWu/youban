package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.presenter.SaveActivePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.PhotoAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhaiww on 2018/5/7.
 */

public class ImageAlbumFragment extends BaseFragment implements BaseView {

    private List<Image> imageList = new ArrayList<>();//初始图片集合
    private int selectPosition = -1;
    private View rootView;
    private RecyclerView recyclerPhoto;
    private int maxImageCount=9;
    int serviceSize;
    private SaveActivePresenter saveActivePresenter;
    private List<Image> imageList2 = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private final int INTENT_PHOTO_CODE = 4;
    private int type;//删除图片用的type 类型(21-->店铺宣传图片,22-->商品图片,其他不用传值),

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void onVisible(boolean isInit) {

    }

    public void setMaxImageCount(int maxImageCount) {
        this.maxImageCount = maxImageCount;
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

    /**
     * @param imageList 初始图片集合
     */
    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
        serviceSize = imageList.size();
    }

    public void clearList() {
        initPhoto();
    }


    /**
     * @return 返回改变后的图片集合
     */
    public List<Image> getNewImageList() {
        return photoAdapter.getmList();
    }

    public List<Image> getImageList() {
        return imageList;
    }

    private void initPhoto() {

        photoAdapter = new PhotoAdapter(getActivity(),maxImageCount);
        if (imageList == null) {
            imageList = new ArrayList<Image>();
        }
        // 网络图片为空
        if (imageList == null || imageList.size() == 0 || (photoAdapter.getMAX_ITEM_COUNT() - imageList.size()) > 0) {
            // 添加"add"图片(PhotoAdapter.MAX_ITEM_COUNT - curr
            Image image = new Image();
            image.setImageUrl(PhotoAdapter.ITEM_ADD);
            int position = photoAdapter.getItemCount() - 1 > -1 ? photoAdapter.getItemCount() - 1 : 0;
            photoAdapter.addItemData(position, image, false);

        }

        for (int i = 0; i < imageList.size(); i++) {

            if (imageList.get(i).getImageUrl().startsWith("http://")) {
                photoAdapter.addItemData(i, imageList.get(i), false);
            }
        }


        photoAdapter.notifyDataSetChanged();


        recyclerPhoto.setAdapter(photoAdapter);
        photoAdapter.setCloseCheckListener(new PhotoAdapter.CloseCheckListener() {

            @Override
            public void onCloseClick(int position) {
                //当前有多少张图片
                serviceSize -= 1;
                if (onImageNumChange != null) {
                    onImageNumChange.OnImageNum(serviceSize);
                }

                if (photoAdapter.getItemCount() > position) {
                    Image image = photoAdapter.getmList().get(position);
                    if (image.getImageId() > 0 || image.getImageUrl().startsWith("http://")) {// 网络图片
                        selectPosition = position;
                        // 调用网络接口
                        getSaveActivePresenter().deleteImage(image.getImageId(),type);
                    } else if (!image.getImageUrl().equals(PhotoAdapter.ITEM_ADD)) {// 本地有效图片
                        // lastUploadPosition -= 1;
                        photoAdapter.getmList().remove(position);
                        photoAdapter.notifyDataSetChanged();

                        if (photoAdapter.checkIsExistAddItem()) {
                            checkAddItem();

                        }

                    }
                }
            }

        });

        photoAdapter.setOnItemListener(new ItemListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (photoAdapter.getItemCount() > position) {
                    Image image = photoAdapter.getmList().get(position);
                    List<Image> imageList = photoAdapter.getmList();
                    imageList2.clear();
                    if (imageList.get(imageList.size() - 1).getImageUrl().equals(PhotoAdapter.ITEM_ADD)) {
                        for (int i = 0; i < imageList.size() - 1; i++) {
                            imageList2.add(imageList.get(i));
                        }
                    } else {
                        imageList2.addAll(imageList);
                    }
                    if (image.getImageUrl().equals(PhotoAdapter.ITEM_ADD)) {
                        PictureSelector.create(ImageAlbumFragment.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxImageCount - imageList2.size())// 最大图片选择数量 int
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
                    } else {
                        ImageViewPageDialog.newInstance(imageList2, position).show(getActivity().getSupportFragmentManager());
                    }
                }
            }
        });

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

                                photoAdapter.addItemData(photoAdapter.getItemCount() - 1, image, true);

                                serviceSize += 1;
                                if (onImageNumChange != null) {
                                    onImageNumChange.OnImageNum(serviceSize);
                                }
                            }
                        }
                    }

                    break;
            }
        }
    }

    private void checkAddItem() {
        // 添加"add"图片
        Image image2 = new Image();
        image2.setImageUrl(PhotoAdapter.ITEM_ADD);
        int position2 = photoAdapter.getItemCount() - 1 > -1 ? photoAdapter.getItemCount() : 0;
        photoAdapter.addItemData(position2, image2, true);
    }

    public SaveActivePresenter getSaveActivePresenter() {
        if (saveActivePresenter == null) {
            saveActivePresenter = new SaveActivePresenter(this);
        }
        return saveActivePresenter;
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_DELETEIMAGE:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    Utils.showToastShortTime(response.getMsg());

                    if (response.getCode() == 200) {

                        if (selectPosition > -1 && photoAdapter.getItemCount() > selectPosition) {
                            photoAdapter.getmList().remove(selectPosition);
                            photoAdapter.notifyDataSetChanged();

                            if (photoAdapter.checkIsExistAddItem()) {
                                // 添加"add"图片
                                checkAddItem();
                            }

                        }
                        selectPosition = -1;


                    }
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
