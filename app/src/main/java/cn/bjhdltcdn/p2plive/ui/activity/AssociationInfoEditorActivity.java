//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
//import com.orhanobut.logger.Logger;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.Date;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.AssociationInfoEditorEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.dialog.AskDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
//import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;
//
///**
// * 圈子编辑资料
// */
//public class AssociationInfoEditorActivity extends BaseActivity implements BaseView {
//    private ToolBarFragment titleFragment;
//    private ImageView imageView;
//    private OrganizationInfo organizationInfo;
//    // 选择的图片路径
//    private String selectFileUrl;
//    // 性别限制(1-->不限,2-->仅男生,3-->仅女生)
//    private int sexLimit;
//
//    // 是否设为私密圈子(1公开,2私密)
//    private int authLimit;
//
//    // 圈子内容限制(1-->全部可见,2-->仅圈友可见)
////    private int contentLimit;
//    // 匿名限制(1-->允许匿名,2-->不允许匿名)
//    private int anonymousLimit;
//
//    private ImagePresenter imagePresenter;
//
//    private AssociationPresenter presenter;
//    private EditText OrganNameEditText;
//
//    private int type;
//
//    private int isCreateChatInfo;
//    private int isCloseChatInfo;
//
//    int contentLimit;
//    private int isClickCreatChatHome = 1;
//
//    public AssociationPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//        return presenter;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_association_info_editor_layout);
//
//        organizationInfo = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);
//
//        setTitle();
//        initView();
//
//    }
//
//    private void setTitle() {
//        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView(getResources().getString(R.string.title_association_info_editor));
//        titleFragment.setRightView("完成", R.color.color_ffb700, new ToolBarFragment.ViewOnclick() {
//            @Override
//            public void onClick() {
//
//                String organNameText = OrganNameEditText.getText().toString().trim();
//                if (StringUtils.isEmpty(organNameText)) {
//                    Utils.showToastShortTime("请填写圈子名称");
//                    return;
//                }
//
//                if (!organNameText.equals(organizationInfo.getOrganName())) {
//                    getPresenter().judgeOrganName(organNameText);
//                    return;
//                }
//
//                nextOption();
//
//
//            }
//        });
//    }
//
//    /**
//     * 最后上传数据
//     */
//    private void nextOption() {
//        CheckBox checkBox = findViewById(R.id.checkbox_1);
//
//        if (!checkBox.isChecked()) {
//            Utils.showToastShortTime("请选择同意圈子管理规范按钮");
//            return;
//        }
//
//        if (StringUtils.isEmpty(organizationInfo.getOrganImg()) && StringUtils.isEmpty(selectFileUrl)) {
//            Utils.showToastShortTime("请选择圈子封面图片");
//            return;
//        }
//
//        EditText editIntroduction = findViewById(R.id.edit_introduction);
//        String textIntro = editIntroduction.getText().toString().trim();
//
//        if (StringUtils.isEmpty(textIntro)) {
//            textIntro = organizationInfo.getDescription();
//        }
//
//        String organNameText = OrganNameEditText.getText().toString().trim();
//
//        getPresenter().updateOrganization(organizationInfo.getOrganId(), "", organNameText, sexLimit, 0, contentLimit, authLimit, anonymousLimit, textIntro, isCreateChatInfo, isCloseChatInfo, type);
//
//    }
//
//    private void initView() {
//        // 选择图片
//        imageView = findViewById(R.id.image_view);
//        final Switch switch_custom = findViewById(R.id.switch_custom);
//        final Switch switch_no_name = findViewById(R.id.switch_no_name);
//        switch_no_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    type = 3;
//                    switch_custom.setChecked(false);
//                } else {
//                    switch_custom.setChecked(true);
//                    type = 0;
//                }
//
//            }
//        });
//
//        switch_custom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    type = 0;
//                    switch_no_name.setChecked(false);
//                } else {
//                    type = 3;
//                    switch_no_name.setChecked(true);
//                }
//            }
//        });
//
//        final Switch switch_creat_no_name_hoom = findViewById(R.id.switch_creat_no_name_hoom);
//        switch_creat_no_name_hoom.setChecked(organizationInfo.getChatId() > 0 ? true : false);
//        switch_creat_no_name_hoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                isClickCreatChatHome = 5;
//                if (isChecked) {
//                    if (organizationInfo.getChatId() > 0) {
//                        isCreateChatInfo = 0;
//                        isCloseChatInfo = 0;
//                    } else {
//                        Utils.showToastLongTime("聊天室开放时间21:00-24:00，邀约好友一起来尬聊吧！");
//                        isCreateChatInfo = 1;
//                        isCloseChatInfo = 0;
//                    }
//
//                } else {
//                    if (organizationInfo.getChatId() > 0) {
//                        new AskDialog.Builder()
//                                .content("关闭代表解散此圈子的匿名聊天房间，是否确认关闭？")
//                                .title("关闭匿名聊天房间")
//                                .leftBtnText("取消")
//                                .rightBtnText("关闭")
//                                .leftClickListener(new AskDialog.OnLeftClickListener() {
//                                    @Override
//                                    public void onClick() {
//                                        switch_creat_no_name_hoom.setChecked(true);
//                                    }
//                                })
//                                .rightClickListener(new AskDialog.OnRightClickListener() {
//                                    @Override
//                                    public void onClick() {
//                                        isCreateChatInfo = 0;
//                                        isCloseChatInfo = 1;
//                                    }
//                                })
//                                .build()
//                                .show(getSupportFragmentManager());
//                    } else {
//                        isCreateChatInfo = 0;
//                        isCloseChatInfo = 0;
//                    }
//
//                }
//            }
//        });
//
//        RadioGroup radiogroup_sex = findViewById(R.id.radiogroup_sex);
//        final RadioButton radio_btn_1 = findViewById(R.id.radio_btn_1);
//        final RadioButton radio_btn_2 = findViewById(R.id.radio_btn_2);
//        final RadioButton radio_btn_3 = findViewById(R.id.radio_btn_3);
//        RadioGroup radiogroup_content = findViewById(R.id.radiogroup_content);
//        final RadioButton radio_all = findViewById(R.id.radio_all);
//        final RadioButton radio_btn_circle = findViewById(R.id.radio_btn_circle);
//
//        //性别限制(1-->不限,2-->仅男生,3-->仅女生)
//
//        switch (organizationInfo.getSexLimit()) {// 性别限制(1-->不限,2-->仅男生,3-->仅女生)
//            case 1:
//                radio_btn_1.setChecked(true);
//                radio_btn_2.setChecked(false);
//                radio_btn_3.setChecked(false);
//
//                break;
//
//            case 2:
//                radio_btn_1.setChecked(false);
//                radio_btn_2.setChecked(true);
//                radio_btn_3.setChecked(false);
//
//                break;
//
//            case 3:
//
//                radio_btn_1.setChecked(false);
//                radio_btn_2.setChecked(false);
//                radio_btn_3.setChecked(true);
//
//                break;
//
//        }
//        contentLimit = organizationInfo.getContentLimit();
//        switch (contentLimit) {// 性别限制(1-->不限,2-->仅男生,3-->仅女生)
//            case 1:
//                radio_all.setChecked(true);
//                radio_btn_circle.setChecked(false);
//                break;
//
//            case 2:
//                radio_all.setChecked(false);
//                radio_btn_circle.setChecked(true);
//                break;
//
//        }
//
//        radiogroup_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == radio_btn_1.getId()) {
//                    sexLimit = 1;
//                } else if (checkedId == radio_btn_2.getId()) {
//                    sexLimit = 2;
//                } else if (checkedId == radio_btn_3.getId()) {
//                    sexLimit = 3;
//                }
//            }
//        });
//
//        radiogroup_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == radio_all.getId()) {
//                    contentLimit = 1;
//                } else if (checkedId == radio_btn_circle.getId()) {
//                    contentLimit = 2;
//                }
//            }
//        });
//
//        if (organizationInfo != null) {
//
//            type = organizationInfo.getType();
//            isCreateChatInfo = organizationInfo.getIsCreateChatInfo();
//
//            if (organizationInfo.getType() == 3) {
//                findViewById(R.id.rl_all_no_name).setVisibility(View.GONE);
//                findViewById(R.id.rl_custom).setVisibility(View.GONE);
//            }
//
//            RequestOptions options = new RequestOptions();
//            options.placeholder(R.mipmap.add_image_icon);
//            options.error(R.mipmap.error_bg);
//            options.centerCrop();
//            Glide.with(this).load(organizationInfo.getOrganImg()).apply(options).into(imageView);
//
//            findViewById(R.id.text_view_tips2).setVisibility(View.INVISIBLE);
//
//            OrganNameEditText = findViewById(R.id.edit_view);
//            // 昵称
//            if (!StringUtils.isEmpty(organizationInfo.getOrganName())) {
//                OrganNameEditText.setText(organizationInfo.getOrganName());
//
////                editText.clearFocus();
////                editText.setEnabled(false);
////
//            }
//
//            EditText editText = findViewById(R.id.edit_introduction);
//            final TextView textNumView = findViewById(R.id.content_num_view);
//            // 描述
//            if (!StringUtils.isEmpty(organizationInfo.getDescription())) {
//                editText.setHint(organizationInfo.getDescription());
//            }
//
//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (s.length() <= 500) {
//                        textNumView.setText(s.length() + "/200");
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//        }
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PictureSelector.create(AssociationInfoEditorActivity.this)
//                        .openGallery(PictureMimeType.ofImage())
//                        .maxSelectNum(1)// 最大图片选择数量 int
//                        .minSelectNum(1)// 最小选择数量 int
//                        .imageSpanCount(4)// 每行显示个数 int
//                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                        .previewImage(true)// 是否可预览图片 true or false
//                        .isCamera(true)// 是否显示拍照按钮 true or false
//                        .enableCrop(true)// 是否裁剪 true or false
//                        .compress(true)// 是否压缩 true or false
//                        .glideOverride(Utils.dp2px(110), Utils.dp2px(110))// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .isGif(true)// 是否显示gif图片 true or false
//                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                        .openClickSound(true)// 是否开启点击声音 true or false
//                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                        .minimumCompressSize(100)// 小于100kb的图片不压缩
//                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                        .cropWH(Utils.dp2px(110), Utils.dp2px(110))// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                        .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
//                        .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
//            }
//        });
//
//        TextView textView = findViewById(R.id.text_view_4);
//        TextView textViewSecond = findViewById(R.id.text_view_5);
//        // 二级标签
//        textView.setText(organizationInfo.getHobbyName());
//        textViewSecond.setText("-" + organizationInfo.getSecondHobbyName());
//
//
//        sexLimit = organizationInfo.getSexLimit();
//        authLimit = organizationInfo.getJoinLimit();
//        anonymousLimit = organizationInfo.getAnonymousLimit();
//
//        // 是否允许网友匿名发布内容
//        CheckBox switchView = findViewById(R.id.checkbox_no_name_can_publish);
//
//        //匿名限制(1-->允许匿名,2-->不允许匿名)
//        switchView.setChecked(anonymousLimit == 1 ? true : false);
//        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    anonymousLimit = 1;
//                } else {
//                    anonymousLimit = 2;
//                }
//                Logger.d("isChecked ==== " + isChecked + " === contentLimit== " + anonymousLimit);
//            }
//        });
//
//        // 同意按钮
//        CheckBox checkBox = findViewById(R.id.checkbox_view);
//        checkBox.setChecked(true);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    titleFragment.getRightView().setEnabled(true);
//                    titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_ffb700));
//                } else {
//                    titleFragment.getRightView().setEnabled(false);
//                    titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_666666));
//                }
//            }
//        });
//
//        findViewById(R.id.society_protocol_view_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO 查看圈子管理规范
//                Intent intent = new Intent(AssociationInfoEditorActivity.this, WXPayEntryActivity.class);
//                intent.putExtra(Constants.Action.ACTION_BROWSE, 2);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    private ImagePresenter getImagePresenter() {
//        if (imagePresenter == null) {
//            imagePresenter = new ImagePresenter(this);
//        }
//
//        return imagePresenter;
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case PictureConfig.CHOOSE_REQUEST:
//                    // 图片选择结果回调
//                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//
//                    if (selectList != null || selectList.size() > 0) {
//                        LocalMedia localMedia = selectList.get(0);
//                        if (localMedia.isCompressed()) {
//                            selectFileUrl = localMedia.getCompressPath();
//
//                            // 绑定选择过的图片
//                            RequestOptions options = new RequestOptions();
//                            options.centerCrop();
//                            Glide.with(this).asBitmap().load(selectFileUrl).apply(options).into(imageView);
//
//                        }
//
//                        findViewById(R.id.text_view_tips2).setVisibility(View.INVISIBLE);
//                    }
//
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (isFinishing()) {
//            return;
//        }
//
//
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//
//
//        if (InterfaceUrl.URL_UPDATEORGANIZATION.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {
//
//                    // 上传图片
//                    if (!StringUtils.isEmpty(selectFileUrl)) {
//                        getImagePresenter().uploadImage(organizationInfo.getOrganId(), 3, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), TimeUtils.date2String(new Date()), selectFileUrl);
//                    }
//
//                    // 通知其他页面更新圈子资料
//                    EventBus.getDefault().post(new AssociationInfoEditorEvent(isClickCreatChatHome));
//
//                    finish();
//                }
//            }
//        } else if (InterfaceUrl.URL_JUDGEORGANNAME.equals(apiName)) {
//
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() == 200) {
//
//                    nextOption();
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//
//        }
//
//
//    }
//
//    @Override
//    public void showLoading() {
//
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (presenter != null) {
//            presenter.onDestroy();
//        }
//        presenter = null;
//
//    }
//}
