package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAuthContentInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;

/**
 * 实名认证页面
 */
public class RealNameCertificationAndShopActivity extends BaseActivity implements BaseView, View.OnClickListener {
    private TextView tv_tip_1, tv_tip_2;
    private EditText edit_name_rela, edit_phonenumber, edit_id_card;
    private ImageView img_card_front, img_card_reverse,img_card_front_student, img_card_reverse_student;
    private CardView card_submit;
    private UserPresenter userPresenter;
    private String frontImgStr,frontImgStudentStr;//身份证正面
    private String reverseImgStr,reverseImgStudentStr;//身份证反面
    private boolean isSelectFront = true;//选择身份证正面
    private List<Image> list = new ArrayList<>(1);
    private ImagePresenter imagePresenter;
    private Intent intent;
    private RequestOptions options;
    private PostOperationFragmentDialog dialog;
    private int TYPE=0;//1: 学生证正面 2: 学生证反面
    private long storeId,userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_certification_and_shop);
        userId=SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        storeId=getIntent().getLongExtra(Constants.Fields.STORE_ID,0);
        options = new RequestOptions().centerCrop().skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        userPresenter = new UserPresenter(this);
        imagePresenter = new ImagePresenter(this);
        setTitle();
        init();
        userPresenter.getAuthContentInfo();
    }

    private void init() {
        tv_tip_1 = findViewById(R.id.tv_tip_1);
        tv_tip_2 = findViewById(R.id.tv_tip_2);
        edit_name_rela = findViewById(R.id.edit_name_rela);
        edit_phonenumber = findViewById(R.id.edit_phonenumber);
        edit_id_card = findViewById(R.id.edit_id_card);
        img_card_front = findViewById(R.id.img_card_front);
        img_card_reverse = findViewById(R.id.img_card_reverse);
        img_card_front_student = findViewById(R.id.img_card_front_student);
        img_card_reverse_student = findViewById(R.id.img_card_reverse_student);
        card_submit = findViewById(R.id.card_submit);
        img_card_front.setOnClickListener(this);
        img_card_reverse.setOnClickListener(this);
        img_card_front_student.setOnClickListener(this);
        img_card_reverse_student.setOnClickListener(this);
        card_submit.setOnClickListener(this);
        //String str = "请填写以下内容并提交，未提交实名认证或认证不通过的用户将无法进行<font color='#000000'>提现，开启聊天频道等操作。</font>";
        //tv_tip_1.setText(Html.fromHtml(str));
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
            case InterfaceUrl.URL_GETAUTHCONTENTINFO:
                if (object instanceof GetAuthContentInfoResponse) {
                    GetAuthContentInfoResponse getAuthContentInfoResponse = (GetAuthContentInfoResponse) object;
                    if (getAuthContentInfoResponse.getCode() == 200) {
                        String content1 = getAuthContentInfoResponse.getContent1();
                        if (!TextUtils.isEmpty(content1)) {
                            //tv_tip_1.setText(content1);
                            tv_tip_1.setText(content1);
                        }
                        String content2 = getAuthContentInfoResponse.getContent2();
                        if (!TextUtils.isEmpty(content2)) {
                            tv_tip_2.setText(content2);
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_SAVEREALNAMEAUTHINTEGRATIO:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                    hideLoading();
                    if (baseResponse.getCode() == 200) {
                        //实名认证状态(0未认证,1等待审核,2已认证,3审核失败)
                        SafeSharePreferenceUtils.saveInt(Constants.Fields.AUTHSTATUS, 1);
                        if(storeId==0){
                            //未创建店铺
                            Intent intent=new Intent(RealNameCertificationAndShopActivity.this,CreateShopActivity.class);
                            startActivity(intent);
                        }else{
                            //已创建店铺
                            Intent intent=new Intent(RealNameCertificationAndShopActivity.this,ShopDetailActivity.class);
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

                        if (list != null) {
                            list.add(uploadImageResponse.getImage());

                            if (list.size() == 4) {
                                userPresenter.saveRealNameAuthIntegration(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), getNameStr(),
                                        getPhoneStr(), getCardIdStr(), list,frontImgStudentStr,reverseImgStudentStr);
                                list.clear();
                            }

                        }

                    } else {
                        Utils.showToastShortTime(uploadImageResponse.getMsg());
                        hideLoading();
                    }

                    card_submit.setEnabled(true);

                }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_card_front:

                dialog = new PostOperationFragmentDialog();
                dialog.setTextList("拍照", "取消");
                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                    @Override
                    public void onClick(int type) {

                        switch (type) {
                            case 1:
                                intent = new Intent(RealNameCertificationAndShopActivity.this, IDCardTakePhotoActivity.class);
                                intent.putExtra(Constants.Fields.IDCARDPHOTOURL, "/IDCardFrontPhoto.jpg");
                                startActivityForResult(intent, 1);
                                break;
                        }

                    }
                });
                dialog.show(getSupportFragmentManager());

                break;
            case R.id.img_card_reverse:

                dialog = new PostOperationFragmentDialog();
                dialog.setTextList("拍照", "取消");
                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                    @Override
                    public void onClick(int type) {

                        switch (type) {
                            case 1:
                                isSelectFront = false;
                                intent = new Intent(RealNameCertificationAndShopActivity.this, IDCardTakePhotoActivity.class);
                                intent.putExtra(Constants.Fields.IDCARDPHOTOURL, "/IDCardReversePhoto.jpg");
                                startActivityForResult(intent, 1);
                                break;
                        }

                    }
                });
                dialog.show(getSupportFragmentManager());

                break;
            case R.id.img_card_front_student:
                TYPE=1;
                PictureSelector.create(RealNameCertificationAndShopActivity.this)
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
            case R.id.img_card_reverse_student:
                TYPE=2;
                PictureSelector.create(RealNameCertificationAndShopActivity.this)
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
            case R.id.card_submit:
                if (TextUtils.isEmpty(getNameStr())) {
                    Utils.showToastShortTime("名字不能为空");
                } else if (TextUtils.isEmpty(getPhoneStr())) {
                    Utils.showToastShortTime("手机号不能为空");
                } else if (!StringUtils.isFormatedPhoneNumber(getPhoneStr())) {
                    Utils.showToastShortTime("请输入正确的手机号");
                } else if (TextUtils.isEmpty(frontImgStr)) {
                    Utils.showToastShortTime("身份证号不能为空");
                } else if (!StringUtils.IDCardValidate(getCardIdStr())) {
                    Utils.showToastShortTime("请输入正确的的身份证号码");
                } else if (TextUtils.isEmpty(frontImgStr) || TextUtils.isEmpty(reverseImgStr)) {
                    Utils.showToastShortTime("请按要求上传身份证");
                }else if (TextUtils.isEmpty(frontImgStudentStr) || TextUtils.isEmpty(reverseImgStudentStr)) {
                    Utils.showToastShortTime("请按要求上传学生证");
                } else {

                    card_submit.setEnabled(false);

                    if (list != null) {
                        list.clear();
                    }

                    showLoading();

                    //身份证 parentId无用
                    imagePresenter.uploadImage(2, 8, userId, TimeUtils.date2String(new Date()), frontImgStr);
                    imagePresenter.uploadImage(2, 9, userId, TimeUtils.date2String(new Date()), reverseImgStr);
                    //学生证
                    imagePresenter.uploadImage(0, 19, userId, TimeUtils.date2String(new Date()), frontImgStudentStr);
                    imagePresenter.uploadImage(0, 20, userId, TimeUtils.date2String(new Date()), reverseImgStudentStr);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            String idCardUrl = data.getStringExtra("URL");
            if (idCardUrl.contains("IDCardFrontPhoto")) {
                frontImgStr = idCardUrl;
                frontImgStr = frontImgStr.substring(7, frontImgStr.length());
                Glide.with(this).asBitmap().load(idCardUrl).apply(options).into(img_card_front);
            } else if (idCardUrl.contains("IDCardReversePhoto")) {
                reverseImgStr = idCardUrl;
                reverseImgStr = reverseImgStr.substring(7, reverseImgStr.length());
                Glide.with(this).asBitmap().load(idCardUrl).apply(options).into(img_card_reverse);
            }
        }
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
                                frontImgStudentStr = localMedia.getCompressPath();
                                RequestOptions options = new RequestOptions().centerCrop();
                                Glide.with(this).asBitmap().load(frontImgStudentStr).apply(options).into(img_card_front_student);
                            }else if(TYPE==2){
                                reverseImgStudentStr = localMedia.getCompressPath();
                                RequestOptions options = new RequestOptions().centerCrop();
                                Glide.with(this).asBitmap().load(reverseImgStudentStr).apply(options).into(img_card_reverse_student);
                            }
                        }
                    }
                    break;
            }
        }
    }


    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("实名认证");
    }

    /**
     * 获取名字
     *
     * @return
     */
    private String getNameStr() {
        return edit_name_rela.getText().toString();
    }

    /**
     * 获取手机号
     *
     * @return
     */
    private String getPhoneStr() {
        return edit_phonenumber.getText().toString();
    }

    /**
     * 获取身份证号
     *
     * @return
     */
    private String getCardIdStr() {
        return edit_id_card.getText().toString();
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
