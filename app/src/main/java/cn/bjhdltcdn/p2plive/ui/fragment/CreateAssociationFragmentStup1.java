//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
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
//import cn.bjhdltcdn.p2plive.httpresponse.SaveOrganizationtResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.CreateAssociationActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.GroupCreateActivity;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.utils.DateUtils;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;
//
///**
// * 创建圈子第一步
// */
//public class CreateAssociationFragmentStup1 extends Fragment implements BaseView {
//
//    private View rootView;
//
//    private ImageView imageView;
//    // 选择的图片路径
//    private String selectFileUrl;
//
//    private AssociationPresenter presenter;
//
//
//    private CreateAssociationActivity activity;
//    private ImagePresenter imagePresenter;
//
//    private long relationId;
//    private EditText editView;
//
//    private TextView contentNumView;
//    private TextView nextView;
//    private CheckBox checkBox;
//    private EditText editIntroduction;
//    private boolean needShowLoading = true;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        activity = (CreateAssociationActivity) context;
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.fragment_create_association_stup1_layout, container, false);
//
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//
//        return rootView;
//
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        setBtnVisibility(true);
//
//        // 选择图片
//        imageView = rootView.findViewById(R.id.image_view);
//        showImage();
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PictureSelector.create(CreateAssociationFragmentStup1.this)
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
//
//        // 圈子所属类别
//        rootView.findViewById(R.id.line_view_4).setVisibility(View.VISIBLE);
//        View textViewTips = rootView.findViewById(R.id.text_view_tips);
//        TextView textView4 = textViewTips.findViewById(R.id.text_view_4);
//        TextView textView5 = textViewTips.findViewById(R.id.text_view_5);
//
//        if (!StringUtils.isEmpty(activity.getCustomName())) {
//            textViewTips.setVisibility(View.VISIBLE);
//            if (activity.getFirstHobbyInfo() != null) {
//                textView4.setText(activity.getFirstHobbyInfo().getHobbyName());
//                textView5.setText("-" + activity.getCustomName());
//            } else {
//                textView4.setText(activity.getCustomName());
//            }
//
//        } else if (activity.getFirstHobbyInfo() != null && activity.getSecondHobbyInfo() != null) {
//            textViewTips.setVisibility(View.VISIBLE);
//            textView4.setText(activity.getFirstHobbyInfo().getHobbyName());
//            textView5.setText("-" + activity.getSecondHobbyInfo().getHobbyName());
//        }
//
//        contentNumView = rootView.findViewById(R.id.content_num_view);
//        editView = rootView.findViewById(R.id.edit_view);
//        editView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                checkNextBtnView();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//        // 简介
//        editIntroduction = rootView.findViewById(R.id.edit_introduction);
//        editIntroduction.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() <= 500) {
//                    contentNumView.setText(s.length() + "/200");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                checkNextBtnView();
//
//            }
//        });
//
//        // 同意按钮
//        checkBox = rootView.findViewById(R.id.checkbox_1);
//        checkBox.setChecked(true);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                checkNextBtnView();
//            }
//        });
//
//        rootView.findViewById(R.id.society_protocol_view).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO 查看圈子管理规范
//                Intent intent = new Intent(getActivity(), WXPayEntryActivity.class);
//                intent.putExtra(Constants.Action.ACTION_BROWSE, 2);
//                startActivity(intent);
//            }
//        });
//
//        nextView = rootView.findViewById(R.id.next_btn_view);
//        nextView.setText("创建");
//        nextView.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//        nextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (StringUtils.isEmpty(selectFileUrl)) {
//                    Utils.showToastShortTime("请上传圈子头像");
//                    return;
//                }
//
//                String text = editView.getText().toString().trim();
//                if (StringUtils.isEmpty(text)) {
//                    Utils.showToastShortTime("请填写圈子名称");
//                    return;
//                }
//
//                // 保存名字
//                ((CreateAssociationActivity) getActivity()).setAssociationName(text);
//
//
//                String textIntro = editIntroduction.getText().toString().trim();
////                if (StringUtils.isEmpty(textIntro)) {
////                    Utils.showToastShortTime("请填写圈子简介");
////                    return;
////                }
//
//                // 保存简介
//                ((CreateAssociationActivity) getActivity()).setAssociationIntroduction(textIntro);
//
//
//                getPresenter().saveOrganizationt("", activity.getAssociationName(), activity.getFirstHobbyInfo().getHobbyId(), activity.getSecondHobbyInfo() == null ? 0 : activity.getSecondHobbyInfo().getHobbyId(), activity.getCustomName(), activity.getAssociationIntroduction(), activity.getIsCreateChatInfo(), activity.getAnonymousLimit(), activity.getType(), activity.getSexLimit(), activity.getContentLimit());
//
//
//            }
//        });
//    }
//
//
//    public AssociationPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//        return presenter;
//    }
//
//
//    // 检查状态
//    private void checkNextBtnView() {
//        // 圈子昵称
//        String nickName = editView.getText().toString().trim();
//        if (checkBox.isChecked() && !StringUtils.isEmpty(selectFileUrl) && !StringUtils.isEmpty(nickName)) {
//            nextView.setEnabled(true);
//            nextView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//        } else {
//            nextView.setEnabled(false);
//            nextView.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//        }
//
//
//    }
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
//                            showImage();
//                            checkNextBtnView();
//
//                            // 保存选择的图片
//                            ((CreateAssociationActivity) getActivity()).setSelectFileUrl(selectFileUrl);
//
//                        }
//                    }
//
//                    break;
//            }
//        }
//
//    }
//
//
//    private void showImage() {
//
//        if (!StringUtils.isEmpty(selectFileUrl)) {
//            // 隐藏掉文字
//            TextView textViewTips2 = rootView.findViewById(R.id.text_view_tips2);
//            textViewTips2.setText("圈子头像");
//            textViewTips2.setVisibility(View.GONE);
//        }
//
//        // 绑定选择过的图片
//        RequestOptions options = new RequestOptions();
//        options.diskCacheStrategy(DiskCacheStrategy.ALL);
//        options.placeholder(R.mipmap.add_image_icon);
//        Glide.with(this).load(selectFileUrl).apply(options).into(imageView);
//
//    }
//
//    public ImagePresenter getImagePresenter() {
//        if (imagePresenter == null) {
//            imagePresenter = new ImagePresenter(this);
//        }
//        return imagePresenter;
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (!isAdded()) {
//            return;
//        }
//
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//
//        if (InterfaceUrl.URL_SAVEORGANIZATIONT.equals(apiName)) {
//            if (object instanceof SaveOrganizationtResponse) {
//                final SaveOrganizationtResponse response = (SaveOrganizationtResponse) object;
//
//                if (response.getCode() == 200) {
//                    relationId = response.getOrganId();
//                    getImagePresenter().uploadImage(response.getOrganId(), 3, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), DateUtils.getFormatDataString(new Date(), DateUtils.DATE_FORMAT_2), activity.getSelectFileUrl());
//
//                    if (activity.getType() == 3) {
//                        activity.finish();
//                        return;
//                    }
//
//                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                    dialog.setText("创建圈子成功！", "为方便圈友沟通，您可创建一个圈子交流群，或稍后在圈子页创建。", "稍后创建", "立即创建");
//                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            EventBus.getDefault().post(new AssociationInfoEditorEvent(1));
//                            //取消
//                            activity.finish();
//                        }
//
//                        @Override
//                        public void onRightClick() {
//
//                            OrganizationInfo organizationInfo = new OrganizationInfo();
//                            organizationInfo.setOrganId(relationId);
//                            String text = editView.getText().toString().trim();
//                            organizationInfo.setOrganName(text);
//                            if (activity.getSelectFileUrl() != null) {
//                                organizationInfo.setOrganImg(activity.getSelectFileUrl());
//                            }
//                            startActivity(new Intent(activity, GroupCreateActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//
//
//                            EventBus.getDefault().post(new AssociationInfoEditorEvent(1));
//                            activity.finish();
//                        }
//                    });
//                    dialog.show(getChildFragmentManager());
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_UPLOADIMAGE.equals(apiName)) {
//            if (object instanceof UploadImageResponse) {
//                final UploadImageResponse response = (UploadImageResponse) object;
//
//                if (response.getCode() == 200) {
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 设置按钮是否可见
//     *
//     * @param isVisibility
//     */
//    public void setBtnVisibility(boolean isVisibility) {
//        rootView.findViewById(R.id.layout_view_5).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
//        rootView.findViewById(R.id.line_view_2).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
//        rootView.findViewById(R.id.next_btn_view).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
//
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        if (imageView != null) {
//            imageView = null;
//        }
//
//        if (selectFileUrl != null) {
//            selectFileUrl = null;
//        }
//
//        if (presenter != null) {
//            presenter.onDestroy();
//            presenter = null;
//        }
//
//        if (imagePresenter != null) {
//            imagePresenter.onDestroy();
//            imagePresenter = null;
//        }
//
//
//        if (rootView != null) {
//            rootView = null;
//        }
//
//    }
//
//
//}
