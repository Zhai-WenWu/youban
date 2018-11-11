package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.UpdateShopDetailEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhaiww on 2018/5/17.
 */

public class ApplyClerkActivity extends BaseActivity implements View.OnClickListener, BaseView {

    private ImageView iv_student_card;
    private ImageView iv_student_card_include_info;
    private PostOperationFragmentDialog dialog;
    private Intent intent;
    private RequestOptions options;
    private CircleImageView iv_user_icon;
    private TextView tv_user_name;
    private TextView tv_user_id;
    private GetStoreListPresenter getStoreListPresenter;
    private TextView tv_submission;
    private TextView tv_num_content;
    private long userId;
    private EditText et_phone_number;
    private EditText et_addr;
    private EditText et_remarks;
    private List<Image> list = new ArrayList<>();
    private String phoneNum;
    private String addr;
    private String remarks;
    private String frontImgStr;
    private String reverseImgStr;
    private ImagePresenter imagePresenter;
    private int TYPE;
    private long storeId;
    private boolean phoneNumNotEmputy;
    private boolean addrNotEmputy;
    private boolean remakeNumNotEmputy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_staff);
        storeId = getIntent().getLongExtra(Constants.Fields.STORE_ID, 0);
        options = new RequestOptions().centerCrop().skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        setTitle();
        initView();
        initData();
    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    private void initData() {
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        BaseUser baseUser = new GreenDaoUtils().getBaseUser(userId);
        Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), iv_user_icon);
        tv_user_name.setText(baseUser.getNickName());
        tv_user_id.setText("ID:" + baseUser.getUserId());
    }

    private void initView() {
        iv_student_card = findViewById(R.id.iv_student_card);
        tv_user_name = findViewById(R.id.tv_user_name);
        iv_user_icon = findViewById(R.id.iv_user_icon);
        tv_user_id = findViewById(R.id.tv_user_id);
        tv_submission = findViewById(R.id.tv_submission);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_remarks = findViewById(R.id.et_remarks);
        et_addr = findViewById(R.id.et_addr);
        tv_num_content = findViewById(R.id.tv_num_content);
        iv_student_card_include_info = findViewById(R.id.iv_student_card_include_info);
        iv_student_card.setOnClickListener(this);
        iv_student_card_include_info.setOnClickListener(this);
        tv_submission.setOnClickListener(this);
        et_phone_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    phoneNumNotEmputy = true;
                } else {
                    phoneNumNotEmputy = false;
                }

                if (phoneNumNotEmputy && addrNotEmputy && remakeNumNotEmputy && !StringUtils.isEmpty(frontImgStr) && !StringUtils.isEmpty(reverseImgStr)) {
                    tv_submission.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                } else {
                    tv_submission.setBackgroundResource(R.drawable.shape_round_10_solid_f4f4f4);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_addr.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    addrNotEmputy = true;
                } else {
                    addrNotEmputy = false;
                }
                if (phoneNumNotEmputy && addrNotEmputy && remakeNumNotEmputy && !StringUtils.isEmpty(frontImgStr) && !StringUtils.isEmpty(reverseImgStr)) {
                    tv_submission.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                } else {
                    tv_submission.setBackgroundResource(R.drawable.shape_round_10_solid_f4f4f4);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_remarks.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    remakeNumNotEmputy = true;
                } else {
                    remakeNumNotEmputy = false;
                }

                if (s.length() <= 500) {
                    tv_num_content.setText(s.length() + "/500");
                }

                if (phoneNumNotEmputy && addrNotEmputy && !StringUtils.isEmpty(frontImgStr) && !StringUtils.isEmpty(reverseImgStr)) {
                    tv_submission.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                } else {
                    tv_submission.setBackgroundResource(R.drawable.shape_round_10_solid_f4f4f4);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_student_card:

                TYPE = 1;
                PictureSelector.create(ApplyClerkActivity.this)
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
            case R.id.iv_student_card_include_info:

                TYPE = 2;
                PictureSelector.create(ApplyClerkActivity.this)
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
            case R.id.tv_submission:

                phoneNum = et_phone_number.getText().toString().trim();
                addr = et_addr.getText().toString().trim();
                remarks = et_remarks.getText().toString().trim();

                if (!StringUtils.isFormatedPhoneNumber(phoneNum)) {
                    Utils.showToastShortTime("请输入正确手机号");
                } else if (StringUtils.isEmpty(addr)) {
                    Utils.showToastShortTime("请输入地址");
                } else if (StringUtils.isEmpty(frontImgStr)) {
                    Utils.showToastShortTime("请上传学生证封面");
                } else if (StringUtils.isEmpty(reverseImgStr)) {
                    Utils.showToastShortTime("请上传学生证有你身份信息的页面");
                } else {
                    getImagePresenter().uploadImage(2, 19, userId, TimeUtils.date2String(new Date()), frontImgStr);//parentId无用
                    getImagePresenter().uploadImage(2, 20, userId, TimeUtils.date2String(new Date()), reverseImgStr);
                    ProgressDialogUtils.getInstance().showProgressDialog(this);
                    tv_submission.setClickable(false);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
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
                            if (TYPE == 1) {
                                frontImgStr = localMedia.getCompressPath();
                                RequestOptions options = new RequestOptions().centerCrop();
                                Glide.with(this).asBitmap().load(frontImgStr).apply(options).into(iv_student_card);
                            } else if (TYPE == 2) {
                                reverseImgStr = localMedia.getCompressPath();
                                RequestOptions options = new RequestOptions().centerCrop();
                                Glide.with(this).asBitmap().load(reverseImgStr).apply(options).into(iv_student_card_include_info);
                            }
                        }
                    }

                    if (phoneNumNotEmputy && addrNotEmputy && !StringUtils.isEmpty(frontImgStr) && !StringUtils.isEmpty(reverseImgStr)) {
                        tv_submission.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                    } else {
                        tv_submission.setBackgroundResource(R.drawable.shape_round_10_solid_f4f4f4);
                    }
                    break;
            }
        }
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("申请店员");
//        titleFragment.setRightView("查看招聘信息", R.color.color_ffb700, new ToolBarFragment.ViewOnclick() {
//            @Override
//            public void onClick() {
//                Intent intent = new Intent(ApplyClerkActivity.this, PostDetailActivity.class);
//                long postId = getIntent().getLongExtra(Constants.Fields.POST_ID, 0);
//                PostInfo postInfo = new PostInfo();
//                postInfo.setPostId(postId);
//                intent.putExtra(Constants.KEY.KEY_OBJECT, postInfo);
//                intent.putExtra(Constants.Fields.COME_IN_TYPE, 10);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_UPLOADIMAGE:
                if (object instanceof UploadImageResponse) {
                    UploadImageResponse uploadImageResponse = (UploadImageResponse) object;
                    if (uploadImageResponse.getCode() == 200) {
                        list.add(uploadImageResponse.getImage());
                        if (list.size() == 2) {
                            getStoreListPresenter().applyClert(userId, storeId, phoneNum, addr, remarks, list.get(0).getImageUrl(), list.get(1).getImageUrl());
                            list.clear();
                        }
                    } else {
                        Utils.showToastShortTime(uploadImageResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_APPLYCLERT:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                    if (baseResponse.getCode() == 200) {
                        EventBus.getDefault().post(new UpdateShopDetailEvent(1,null));
                        finish();
                    } else {
                        tv_submission.setClickable(true);
                    }
                }
                break;
        }
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
