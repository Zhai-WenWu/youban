package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 *  申请开店页面
 */
public class ApplyForShopActivity extends BaseActivity implements BaseView, View.OnClickListener {
    private TextView tv_tip_1, tv_tip_2;
    private RelativeLayout img_card_front_layout,img_card_reverse_layout;
    private ImageView img_card_front, img_card_reverse;
    private CardView card_submit;
    private GetStoreListPresenter getStoreListPresenter;
    private String frontImgStr;//身份证正面
    private String reverseImgStr;//身份证反面
    private int TYPE=0;//1: 身份证正面 2: 身份证反面
    private List<Image> list = new ArrayList<>(1);
    private ImagePresenter imagePresenter;
    private Intent intent;
    private RequestOptions options;
//    private PostOperationFragmentDialog dialog;
    private long userId;
    private TextView userNameTextView,userIdTextView,phoneNumTextView,rulesTextView;
//    private CheckBox rulesCheckBox;
    private ImageView userImageView;
    private int screenWidth;
    private long storeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_shop);
        Intent intent=getIntent();
        storeId=intent.getLongExtra(Constants.Fields.STORE_ID,0);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        options = new RequestOptions().centerCrop().skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        getStoreListPresenter = new GetStoreListPresenter(this);
        imagePresenter = new ImagePresenter(this);
        screenWidth= PlatformInfoUtils.getWidthOrHeight(this)[0];
        setTitle();
        init();
    }

    private void init() {
        userNameTextView= findViewById(R.id.name_text_view);
        userIdTextView= findViewById(R.id.id_text_view);
        phoneNumTextView= findViewById(R.id.phonenum_text_view);
        userImageView = findViewById(R.id.img_icon);
        GreenDaoUtils.getInstance().getBaseUser(userId, new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(final Object object) {
                ApplyForShopActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BaseUser currentBaseUser = (BaseUser) object;
                        if(currentBaseUser!=null){
                            userNameTextView.setText(currentBaseUser.getNickName());
                            userIdTextView.setText("ID:"+currentBaseUser.getUserId()+"");
                            phoneNumTextView.setText(currentBaseUser.getPhoneNumber()+"");
                            Glide.with(ApplyForShopActivity.this).asBitmap().load(currentBaseUser.getUserIcon()).apply(options).into(userImageView);
                        }
                    }
                });
            }
        });
        img_card_front_layout= findViewById(R.id.img_card_front_layout);
        img_card_reverse_layout= findViewById(R.id.img_card_reverse_layout);
        img_card_front_layout.getLayoutParams().height=(screenWidth-Utils.dp2px(20))*34/71;
        img_card_reverse_layout.getLayoutParams().height=(screenWidth-Utils.dp2px(20))*34/71;
        tv_tip_1= findViewById(R.id.text_view_tips1);
        tv_tip_2= findViewById(R.id.text_view_tips2);
        img_card_front = findViewById(R.id.img_card_front);
        img_card_reverse = findViewById(R.id.img_card_reverse);
        card_submit = findViewById(R.id.card_submit);
        card_submit.setEnabled(false);
//        rulesCheckBox=findViewById(R.id.rules_checkbox);
//        rulesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                submitEnable();
//            }
//        });
//        rulesTextView=findViewById(R.id.rules_view);
        img_card_front_layout.setOnClickListener(this);
        img_card_reverse_layout.setOnClickListener(this);
        card_submit.setOnClickListener(this);
//        rulesTextView.setOnClickListener(this);
    }

    public boolean submitEnable(){
        if(frontImgStr!=null&&!StringUtils.isEmpty(frontImgStr)&&reverseImgStr!=null&&!StringUtils.isEmpty(reverseImgStr)){
            card_submit.setEnabled(true);
            card_submit.setCardBackgroundColor(ApplyForShopActivity.this.getResources().getColor(R.color.color_ffee00));
            return true;
        }else{
            card_submit.setEnabled(false);
            card_submit.setCardBackgroundColor(ApplyForShopActivity.this.getResources().getColor(R.color.color_e6e6e6));
            return false;
        }
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            return;
        }
        switch (apiName) {
            case InterfaceUrl.URL_APPLYCREATESTORE:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                    if (baseResponse.getCode() == 200) {
                        if(storeId==0){
                            //未创建店铺
                            Intent intent=new Intent(ApplyForShopActivity.this,CreateShopActivity.class);
                            startActivity(intent);
                        }else{
                            //已创建店铺
                            Intent intent=new Intent(ApplyForShopActivity.this,ShopDetailActivity.class);
                            intent.putExtra(Constants.Fields.STORE_ID,storeId);
                            startActivity(intent);
                        }

                        finish();
                    }
                }
                break;

            case InterfaceUrl.URL_UPLOADIMAGE:
                if (object instanceof UploadImageResponse) {
                    UploadImageResponse uploadImageResponse = (UploadImageResponse) object;
                    if (uploadImageResponse.getCode() == 200) {
                        list.add(uploadImageResponse.getImage());
                        if (list.size() == 2) {
                            getStoreListPresenter.applyCreateStore(userId,frontImgStr,reverseImgStr);
                            list.clear();
                        }
                    } else {
                        Utils.showToastShortTime(uploadImageResponse.getMsg());
                    }
                }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_card_front_layout:
                TYPE=1;
                PictureSelector.create(ApplyForShopActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
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
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.img_card_reverse_layout:
                TYPE=2;
//                dialog = new PostOperationFragmentDialog();
//                dialog.setTextList("拍照", "取消");
//                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
//                    @Override
//                    public void onClick(int type) {
//
//                        switch (type) {
//                            case 1:
//                                intent = new Intent(ApplyForShopActivity.this, IDCardTakePhotoActivity.class);
//                                intent.putExtra(Constants.Fields.IDCARDPHOTOURL, "/IDCardFrontPhoto.jpg");
//                                startActivityForResult(intent, 1);
//                                break;
//                        }
//
//                    }
//                });
//                dialog.show(getSupportFragmentManager());
                PictureSelector.create(ApplyForShopActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
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
                        .forResult(PictureConfig.CHOOSE_REQUEST);

                break;
            case R.id.rules_view:
//                Intent intent = new Intent(ApplyForShopActivity.this, WXPayEntryActivity.class);
//                intent.putExtra(Constants.Action.ACTION_BROWSE, 4);
//                startActivity(intent);
                break;
            case R.id.card_submit:
//                if(!rulesCheckBox.isChecked()){
//                    Utils.showToastShortTime("请先同意“已阅读并执行此规章制度");
//                    return;
//                }
                if (TextUtils.isEmpty(frontImgStr) || TextUtils.isEmpty(reverseImgStr)) {
                    Utils.showToastShortTime("请按要求上传学生证");
                    return;
                } else {
                    imagePresenter.uploadImage(0, 19, userId, TimeUtils.date2String(new Date()), frontImgStr);
                    imagePresenter.uploadImage(0, 20, userId, TimeUtils.date2String(new Date()), reverseImgStr);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:// 选择头像
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    if (selectList != null || selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        if (localMedia.isCompressed()) {
                            if(TYPE==1){
                                frontImgStr = localMedia.getCompressPath();
                                RequestOptions options = new RequestOptions().centerCrop();
                                Glide.with(this).asBitmap().load(frontImgStr).apply(options).into(img_card_front);
                                img_card_front.getLayoutParams().width=screenWidth-Utils.dp2px(20);
                                img_card_front.getLayoutParams().height=(screenWidth-Utils.dp2px(20))*34/71;
                                tv_tip_1.setVisibility(View.GONE);
                                submitEnable();
                            }else if(TYPE==2){
                                reverseImgStr = localMedia.getCompressPath();
                                RequestOptions options = new RequestOptions().centerCrop();
                                Glide.with(this).asBitmap().load(reverseImgStr).apply(options).into(img_card_reverse);
                                img_card_reverse.getLayoutParams().width=screenWidth-Utils.dp2px(20);
                                img_card_reverse.getLayoutParams().height=(screenWidth-Utils.dp2px(20))*34/71;
                                tv_tip_2.setVisibility(View.GONE);
                                submitEnable();
                            }
                        }
                    }
                    break;
            }
        }
    }


    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("提交信息");
        titleFragment.setLeftView(new ToolBarFragment.ViewOnclick() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }



    @Override
    public void showLoading() {

        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }
}
