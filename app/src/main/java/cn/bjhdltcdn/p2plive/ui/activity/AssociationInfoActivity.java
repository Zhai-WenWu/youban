//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.style.ImageSpan;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.orhanobut.logger.Logger;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.AssociationInfoEditorEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindOrganizationDetailResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindOrganizationMemberResponse;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.OrganizationMemberAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//
///**
// * 圈子资料
// */
//public class AssociationInfoActivity extends BaseActivity implements BaseView {
//
//    private ToolBarFragment titleFragment;
//
//    private AssociationPresenter presenter;
//
//    // 圈子对象
//    private OrganizationInfo organizationInfo;
//
//    private int pageSiz = 5;
//    private int pageNumber = 1;
//
//    private RecyclerView recyclerView;
//    private Switch mSwitchView2;
//    /**
//     * 是否公开圈子(默认为0--->公开,1--->隐藏)
//     */
//    private int isPublic;
//    private int position;
//    private boolean needShowLoading = true;
//    private View layoutView9;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        EventBus.getDefault().register(this);
//
//        setContentView(R.layout.activity_association_info_layout);
//
//        organizationInfo = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);
//        position = getIntent().getIntExtra(Constants.Fields.POSITION, -1);
//
//        setTitle();
//
//        initView();
//
//        // 获取圈子成员
//        getPresenter().findOrganizationMember(organizationInfo.getOrganId(), pageSiz, pageNumber);
//
//        // 获取最新资料
//        getPresenter().findOrganizationDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId());
//
//    }
//
//    private void initView() {
//        //成员展示
//        int myUserRole = organizationInfo.getUserRole();
//        layoutView9 = findViewById(R.id.layout_view_9);
//
//        if (organizationInfo.getType() == 3) {
//            findViewById(R.id.recycle_view).setVisibility(View.GONE);
//            findViewById(R.id.arrow_right_view).setVisibility(View.GONE);
//            layoutView9.setVisibility(View.GONE);
//        } else if (organizationInfo.getType() != 2) {
//            if (myUserRole != 1 && myUserRole != 2) {
//                findViewById(R.id.recycle_view).setVisibility(View.GONE);
//                findViewById(R.id.arrow_right_view).setVisibility(View.GONE);
//                layoutView9.setVisibility(View.GONE);
//            }
//        }
//
//
//        ImageView imageView = findViewById(R.id.image_view);
//        // 圈子封面
//        Glide.with(this).load(organizationInfo.getOrganImg()).apply(new RequestOptions().centerCrop().transform(new GlideRoundTransform(9)).placeholder(R.mipmap.error_bg)).into(imageView);
//
//        // 圈子所属类型
//        TextView layoutView7 = findViewById(R.id.layout_view_7);
//        layoutView7.setText(organizationInfo.getHobbyName());
//
//        TextView layoutView8 = findViewById(R.id.layout_view_8);
//        layoutView8.setText("- " + organizationInfo.getSecondHobbyName());
//
//        // 圈子名称
//        TextView nickeTextView = findViewById(R.id.nicke_text_view);
//        nickeTextView.setText(organizationInfo.getOrganName());
//
//        // 圈子id
//        TextView idTextView = findViewById(R.id.id_text_view);
//        idTextView.setText("ID:" + organizationInfo.getOrganId());
//        // 圈子简介
//        TextView descTextView = findViewById(R.id.desc_text_view);
//        descTextView.setText(organizationInfo.getDescription());
//
//
//        /**************************权限控制****************************/
//        //性别限制(1-->不限,2-->仅男生,3-->仅女生),
//        TextView sexLimitView = findViewById(R.id.sex_limit_view);
//        switch (organizationInfo.getSexLimit()) {
//            case 1:
//                sexLimitView.setText("不限");
//                break;
//
//            case 2:
//                sexLimitView.setText("仅限男生");
//                break;
//
//            case 3:
//                sexLimitView.setText("仅限女生");
//                break;
//
//        }
//
//        // 是否设为私密圈子(1公开,2私密)
//        int contentLimit = organizationInfo.getContentLimit();
//        int type = organizationInfo.getType();
//        TextView joinLimitView = findViewById(R.id.join_limit_view);
//
//        StringBuilder stringBuilder = new StringBuilder();
//        if (type == 3) {
//            stringBuilder.append("匿名圈子,");
//        } else {
//            stringBuilder.append("自定义圈子,");
//        }
//
//        if (contentLimit == 1) {
//            stringBuilder.append("内容全部公开");
//        } else {
//            stringBuilder.append("内容仅圈友可见");
//        }
//        joinLimitView.setText(stringBuilder.toString());
//
//        //匿名限制(1-->允许匿名,2-->不允许匿名)
//        TextView anonymousLimitView = findViewById(R.id.anonymous_limit_view);
//        switch (organizationInfo.getAnonymousLimit()) {
//            case 1:
//                anonymousLimitView.setText("开启");
//                break;
//
//            case 2:
//                anonymousLimitView.setText("关闭");
//                break;
//        }
//
//        // 圈子分享
//        View layoutView4 = findViewById(R.id.layout_view_4);
//        layoutView4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String imgUrl = "";
//                if (!StringUtils.isEmpty(organizationInfo.getOrganImg())) {
//                    imgUrl = organizationInfo.getOrganImg();
//                }
//                ShareUtil.getInstance().showShare(AssociationInfoActivity.this, ShareUtil.ORGAIN, organizationInfo.getOrganId(), organizationInfo, "", "", "分享兴趣圈子“" + organizationInfo.getOrganName() + "，我们在友伴等你加入！", imgUrl, true);
//
//            }
//        });
//
//        // 活跃度
//        ImageView imgLevel = findViewById(R.id.img_level);
//        Utils.getActiveLevel(imgLevel, organizationInfo.getActiveLevel());
//
//        // 允许别人看到我加入本圈子
//        mSwitchView2 = findViewById(R.id.img_no_disturb2);
//        mSwitchView2.setChecked(organizationInfo.getIsPublic() == 0 ? true : false);
//        mSwitchView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                boolean isChecked = mSwitchView2.isChecked();
//                // 是否公开圈子(默认为0--->公开,1--->隐藏)
//                isPublic = isChecked ? 0 : 1;
//
//                Logger.d("mSwitchView2  ==isPublic== " + isPublic + "  ==isChecked== " + isChecked);
//
//                getPresenter().updateUserOrganIsPublic(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId(), isPublic);
//
//
//            }
//        });
//
//
//        // 圈子黑名单
//        layoutView9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(AssociationInfoActivity.this, AssociationBlackListActivity.class).putExtra(Constants.Fields.ORGAN_ID, organizationInfo.getOrganId()));
//            }
//        });
//
//
//        // 退出按钮
//        Button logoutView = findViewById(R.id.logout_view);
//        logoutView.setVisibility(View.VISIBLE);
//
//        // 圈子黑名单
//        findViewById(R.id.view_line_5).setVisibility(View.GONE);
//
//
//        switch (organizationInfo.getUserRole()) {
//            case 1:// 圈主
//                logoutView.setText("解散圈子");
//                titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_999999));
//                titleFragment.setRightView("编辑", new ToolBarFragment.ViewOnclick() {
//                    @Override
//                    public void onClick() {
//                        startActivity(new Intent(AssociationInfoActivity.this, AssociationInfoEditorActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//                    }
//                });
//
//                findViewById(R.id.view_line_5).setVisibility(View.VISIBLE);
//
//                break;
//
//            case 2:// 管理员
//                logoutView.setText("退出圈子");
//
//                findViewById(R.id.view_line_5).setVisibility(View.VISIBLE);
//
//                break;
//
//            case 3:// 普通成员
//                logoutView.setText("退出圈子");
//
//                break;
//
//            default:
//                logoutView.setVisibility(View.GONE);
//
//                //圈子内容限制(1-->全部可见,2-->仅圈友可见)
//                Logger.d("contentLimit == " + organizationInfo.getContentLimit());
//                if (organizationInfo.getContentLimit() == 2) {
////                    findViewById(R.id.view_line).setVisibility(View.GONE);
////                    findViewById(R.id.member_text_view).setVisibility(View.GONE);
//                    findViewById(R.id.recycle_view).setVisibility(View.GONE);
//                    findViewById(R.id.arrow_right_view).setVisibility(View.GONE);
//                    findViewById(R.id.layout_view_2).setVisibility(View.GONE);
//                    findViewById(R.id.img_level).setVisibility(View.GONE);
//                }
//                findViewById(R.id.view_line_2).setVisibility(View.GONE);
//                findViewById(R.id.layout_view_2).setVisibility(View.GONE);
//                findViewById(R.id.img_level).setVisibility(View.GONE);
//
//                findViewById(R.id.line_view_2).setVisibility(View.GONE);
//                findViewById(R.id.tv_no_disturb2_view).setVisibility(View.GONE);
//
//
//                break;
//
//
//        }
//
//        // TODO 2018-03-30 需求更改: 0 可以退出，1 不能退出
//        if (organizationInfo.getIsSelect() == 1) {
//            logoutView.setVisibility(View.GONE);
//        }
//
//
//        logoutView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                //所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                switch (organizationInfo.getUserRole()) {
//                    case 1:
//                        dialog.setText("", "解散圈子后，圈子内的全部数据将被清除，确定要解散吗？", "确定解散", "我再想想");
//                        break;
//
//                    case 2:
//                        dialog.setText("", "确定要退出这个圈子吗？", "确定退出", "我再想想");
//                        break;
//
//                    case 3:
//                        dialog.setText("", "确定要退出这个圈子吗？", "确定退出", "我再想想");
//                        break;
//                }
//
//
//                dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                    @Override
//                    public void onLeftClick() {
//                        //取消
//                        //所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//                        switch (organizationInfo.getUserRole()) {
//
//                            case 1:
//                                getPresenter().disbandOrganization(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//
//                                break;
//
//                            case 2:
//                                getPresenter().signOutOrganization(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//                                break;
//
//                            case 3:
//                                getPresenter().signOutOrganization(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onRightClick() {
//
//                    }
//                });
//                dialog.show(getSupportFragmentManager());
//
//            }
//        });
//
//
//    }
//
//
//    private void setTitle() {
//        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("圈子资料");
//    }
//
//
//    private AssociationPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//
//        return presenter;
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (isFinishing()) {
//            return;
//        }
//
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//
//        if (InterfaceUrl.URL_FINDORGANIZATIONMEMBER.equals(apiName)) {
//            if (object instanceof FindOrganizationMemberResponse) {
//                FindOrganizationMemberResponse response = (FindOrganizationMemberResponse) object;
//
//                if (response.getCode() == 200) {
//                    // 圈子成员
//                    recyclerView = findViewById(R.id.recycle_view);
//                    recyclerView.setHasFixedSize(true);
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
//                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                    layoutManager.setAutoMeasureEnabled(true);
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10), false));
//
//                    final OrganizationMemberAdapter adapter = new OrganizationMemberAdapter(response.getMemberList());
//                    recyclerView.setAdapter(adapter);
//                    adapter.setOnItemListener(new ItemListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            // TODO 查看群成员信息
//                            BaseUser baseUser = adapter.getList().get(position).getBaseUser();
//                            startActivity(new Intent(AssociationInfoActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                        }
//                    });
//
//                    // 人数
//                    TextView countTextView = findViewById(R.id.member_text_view);
//
//                    SpannableString spannableString = new SpannableString("(" + response.getTotal() + "人：# " + organizationInfo.getTotalMen() + "人，# " + organizationInfo.getTotalWomen() + "人)");
//                    // 男性图标替换
//                    ImageSpan imgBoySpan = new ImageSpan(this, R.mipmap.boy_icon);
//                    int statIndex = 1 + String.valueOf(response.getTotal()).length() + 2;
//                    int endIndex = statIndex + 1;
//                    spannableString.setSpan(imgBoySpan, statIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    // 女性图标替换
//                    statIndex = endIndex + 1 + String.valueOf(organizationInfo.getTotalMen()).length() + 2;
//                    endIndex = statIndex + 1;
//                    ImageSpan imgGirlSpan = new ImageSpan(this, R.mipmap.girl_icon);
//                    spannableString.setSpan(imgGirlSpan, statIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    countTextView.setText(spannableString);
//
//
//                    // 如果人数太少则隐藏箭头按钮
//                    if (response.getTotal() <= 0) {
//                        findViewById(R.id.arrow_right_view).setVisibility(View.GONE);
//                    } else {
//                        findViewById(R.id.arrow_right_view).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                startActivity(new Intent(AssociationInfoActivity.this, OrganizationListMemberActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//                            }
//                        });
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_SIGNOUTORGANIZATION.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//
//                BaseResponse response = (BaseResponse) object;
//
//                Utils.showToastShortTime(response.getMsg());
//
//                if (response.getCode() == 200) {
//                    AssociationInfoEditorEvent associationInfoEditorEvent = new AssociationInfoEditorEvent(2);
//                    associationInfoEditorEvent.setPosition(position);
//                    EventBus.getDefault().post(associationInfoEditorEvent);
//
//                    finish();
//
//                }
//
//            }
//        } else if (InterfaceUrl.URL_FINDORGANIZATIONDETAIL.equals(apiName)) {
//            if (object instanceof FindOrganizationDetailResponse) {
//                FindOrganizationDetailResponse response = (FindOrganizationDetailResponse) object;
//                if (response.getCode() == 200) {
//                    organizationInfo = response.getOrganizationInfo();
//                    initView();
//                }
//            }
//        } else if (InterfaceUrl.URL_DISBANDORGANIZATION.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//
//                BaseResponse response = (BaseResponse) object;
//
//                Utils.showToastShortTime(response.getMsg());
//
//                if (response.getCode() == 200) {
//                    AssociationInfoEditorEvent associationInfoEditorEvent = new AssociationInfoEditorEvent(organizationInfo.getOrganId(), 2);
//                    associationInfoEditorEvent.setPosition(position);
//                    EventBus.getDefault().post(associationInfoEditorEvent);
//
//                    finish();
//
//                }
//
//            }
//        } else if (InterfaceUrl.URL_UPDATEUSERORGANISPUBLIC.equals(apiName)) {
//            if (object instanceof BaseResponse) {
//
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() != 200) {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        }
//
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void callBackEvent(AssociationInfoEditorEvent evnet) {
//
//        if (evnet == null) {
//            return;
//        }
//
//        if (evnet.getType() == 2) {
//            finish();
//            return;
//        }
//
//        // 获取最新资料
//        getPresenter().findOrganizationDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), organizationInfo.getOrganId());
//
//    }
//
//    @Override
//    public void showLoading() {
//        if (needShowLoading) {
//            ProgressDialogUtils.getInstance().showProgressDialog(this);
//        }
//        needShowLoading = false;
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
//        EventBus.getDefault().unregister(this);
//
//        if (presenter != null) {
//            presenter.onDestroy();
//            presenter = null;
//        }
//    }
//}
