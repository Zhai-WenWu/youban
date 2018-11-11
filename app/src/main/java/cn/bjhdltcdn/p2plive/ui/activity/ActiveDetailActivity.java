//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Message;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.style.ImageSpan;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.request.RequestOptions;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
//import cn.bjhdltcdn.p2plive.event.UpdateActiveDetailEvent;
//import cn.bjhdltcdn.p2plive.event.UpdateActiveListEvent;
//import cn.bjhdltcdn.p2plive.handler.AdvertisementHandlerInActivity;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.FindActiveDetailResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetReportTypeResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.JoinActiveResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.JoinGroupResponse;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.Group;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.ReportType;
//import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.FindActiveDetailPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.ActiveDetailOrgainRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.ActiveListHobbyRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.HomeAdvertisementTabAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.OfflineActiveUserRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveJoinSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.PostDetailCommentDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.RongIMutils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
///**
// * Created by Hu_PC on 2017/11/9.
// * 活动详情页
// */
//
//public class ActiveDetailActivity extends BaseActivity implements BaseView {
//    private FindActiveDetailPresenter presenter;
//    private GroupPresenter groupPresenter;
//    private CommonPresenter commonPresenter;
//    private RecyclerView recycleView, orgainRecycleView;
//    private OfflineActiveUserRecyclerViewAdapter recyclerAdapter;
//    private ActiveDetailOrgainRecyclerViewAdapter orgainRecyclerAapter;
//    private TextView orgain_itp_tv;
//    private TitleFragment titleFragment;
//    private RelativeLayout joinGroupLayout, createGroupLayout;
//    private TextView status_tv, active_type_tv, content_tv, group_tip_view, group_create_view, group_name_tv, group_join_tv, time_tv, location_tv, location_text, charge_text, charge_tv, sex_text, sex_tv, join_tv, join_num_tv;
//    private RecyclerView hobbyRecycleView;
//    private ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter;
//    public CustomViewPager viewPager;
//    private RequestOptions options;
//    private View headView;
//    public AdvertisementHandlerInActivity handler;
//    private int previousPosition = 0;
//    private long userId, activityId;
//    private int userRole, status, joinNumber, activityNumber;//用户角色(1组织用户,2普通用户)用户不在活动中返回0,
//    private Group group;
//    private ActivityInfo activityInfo;
//    private int position;
//    private int isExistGroup;//当前用户是否在群中(0不在,1在,2申请中),
//    private int groupMode;//成员进群方式(1直接入群,2申请进群),
//    private String defaultImg;//默认图片
//    private LinearLayout editActiveLayout;
//    private String joinActivMsg;
//    private boolean joinActive;
//    private boolean needShowLoading = true;
//    private TextView shareTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_active_detail);
//        handler = new AdvertisementHandlerInActivity(new WeakReference<BaseActivity>(this));
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        activityId = getIntent().getLongExtra(Constants.KEY.KEY_ACTIVITY_ID, 0);//
//        position = getIntent().getIntExtra(Constants.Fields.POSITION, -1);
//        options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg);
//        initView();
//        setTitle();
//        commonPresenter = new CommonPresenter(this);
//        presenter = new FindActiveDetailPresenter(this);
//        groupPresenter = new GroupPresenter(this);
//        presenter.findActiveDetail(userId, activityId);
//        EventBus.getDefault().register(this);
//    }
//
//    private void initView() {
//        headView = findViewById(R.id.active_img_layout);
//        createGroupLayout = findViewById(R.id.active_create_group_layout);
//        joinGroupLayout = findViewById(R.id.active_join_group_layout);
//        status_tv = findViewById(R.id.active_state_tv);
//        active_type_tv = findViewById(R.id.active_type_tv);
//        hobbyRecycleView = findViewById(R.id.recycler_hobby);
//        hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(this);
//        hobbyRecycleView.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(App.getInstance(), 3);
//        gridLayoutManager2.setAutoMeasureEnabled(true);
//        hobbyRecycleView.setLayoutManager(gridLayoutManager2);
//        hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 3, false));
//        hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
//        group_tip_view = findViewById(R.id.active_group_tip_tv);
//        group_create_view = findViewById(R.id.create_group_tv);
//        group_create_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActiveDetailActivity.this, GroupCreateActivity.class);
//                intent.putExtra(Constants.KEY.KEY_OBJECT, activityInfo);
//                startActivity(intent);
//            }
//        });
//        group_name_tv = findViewById(R.id.active_group_tv);
//        group_join_tv = findViewById(R.id.active_group_join_text);
//        group_join_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isExistGroup == 0) {
//                    //加入群组
//                    groupPresenter.joinGroup(userId, group.getGroupId(), groupMode);
//                } else if (isExistGroup == 1) {
//                    //发起群聊
//                    RongIMutils.startGroupChat(ActiveDetailActivity.this, group.getGroupId() + "", group.getGroupName());
//
//                }
//            }
//        });
//        time_tv = findViewById(R.id.active_time_tv);
//        location_tv = findViewById(R.id.active_place_tv);
//        location_text = findViewById(R.id.active_place_text);
//        charge_text = findViewById(R.id.active_charge_text);
//        charge_tv = findViewById(R.id.active_charge_tv);
//        sex_text = findViewById(R.id.active_sex_text);
//        sex_tv = findViewById(R.id.active_sex_tv);
//        join_num_tv = findViewById(R.id.active_user_num_tv);
//        //圈子列表
//        orgain_itp_tv = findViewById(R.id.orgain_text);
//        orgainRecycleView = (RecyclerView) findViewById(R.id.orgain_recycler_view);
//        orgainRecyclerAapter = new ActiveDetailOrgainRecyclerViewAdapter(this);
//        orgainRecycleView.setHasFixedSize(true);
//        GridLayoutManager layoutManager2 = new GridLayoutManager(App.getInstance(), 4);
//        orgainRecycleView.setLayoutManager(layoutManager2);
//        orgainRecycleView.setAdapter(orgainRecyclerAapter);
//        orgainRecyclerAapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳到圈子详情
//                OrganizationInfo organizationInfo = orgainRecyclerAapter.getItem(position);
//                startActivity(new Intent(ActiveDetailActivity.this, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//
//            }
//        });
//        //参加人员列表
//        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerAdapter = new OfflineActiveUserRecyclerViewAdapter(this);
//        recycleView.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 7);
//        GridLayoutSpacesItemDecoration gridLayoutSpacesItemDecoration = new GridLayoutSpacesItemDecoration(Utils.dp2px(17), 7, false);
//        recycleView.setLayoutManager(gridLayoutManager);
//        recycleView.addItemDecoration(gridLayoutSpacesItemDecoration);
//        recycleView.setAdapter(recyclerAdapter);
//        recyclerAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到用户详情页
//                if (recyclerAdapter.getItem(position).getUserId() != userId) {
//                    //跳到用户详情
//                    startActivity(new Intent(ActiveDetailActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, recyclerAdapter.getItem(position)));
//                }
//            }
//        });
//        content_tv = findViewById(R.id.active_content_tv);
//        shareTextView = findViewById(R.id.share_text_view);
//        join_tv = findViewById(R.id.join_tv);
//        editActiveLayout = findViewById(R.id.edit_active_layout);
//
//        shareTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //弹出分享弹框
//                String imgUrl = "";
//                if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                    imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//                } else {
//                    imgUrl = defaultImg;
//                }
//                ShareUtil.getInstance().setDefaultImg(defaultImg);
//                ShareUtil.getInstance().showShare(ActiveDetailActivity.this, ShareUtil.ACTIVE, activityInfo.getActivityId(), activityInfo, "", "",
//                        "活动找人：“" + activityInfo.getTheme() + "”邀你一起来玩儿！"
//                        , imgUrl, true);
//
//            }
//        });
//
//    }
//
//    public void setActivityData(ActivityInfo ActivityInfo, Group groupInfo) {
//        if (ActivityInfo != null) {
//
//            List<HobbyInfo> hobbyInfoList = activityInfo.getHobbyList();
//            if (hobbyInfoList != null && hobbyInfoList.size() > 0) {
//                hobbyRecyclerViewAdapter.setList(hobbyInfoList);
//                hobbyRecyclerViewAdapter.notifyDataSetChanged();
//            }
//
//
//            content_tv.setText(activityInfo.getTheme());
//
//            time_tv.setText(activityInfo.getActivityTime());
//            ActivityLocationInfo activityLocationInfo = activityInfo.getLocationInfo();
//            if (activityLocationInfo != null && activityInfo.getType() == 1) {
//                location_tv.setText(activityLocationInfo.getAddr());
//                location_tv.setVisibility(View.VISIBLE);
//                location_text.setVisibility(View.VISIBLE);
//            } else {
//                location_tv.setVisibility(View.GONE);
//                location_text.setVisibility(View.GONE);
//            }
//            int charge = activityInfo.getActivityPrice();
//            if (charge >= 0) {
//                charge_text.setVisibility(View.VISIBLE);
//                charge_tv.setVisibility(View.VISIBLE);
//                charge_tv.setText(charge + "元/人");
//            } else {
//                charge_text.setVisibility(View.GONE);
//                charge_tv.setVisibility(View.GONE);
//            }
//            final int sexLimit = activityInfo.getSexLimit();//性别限制(0不限,1仅限男生,2仅限女生),
//            if (sexLimit == 1) {
//                sex_text.setVisibility(View.VISIBLE);
//                sex_tv.setVisibility(View.VISIBLE);
//                sex_tv.setText("仅限男生");
//            } else if (sexLimit == 2) {
//                sex_text.setVisibility(View.VISIBLE);
//                sex_tv.setVisibility(View.VISIBLE);
//                sex_tv.setText("仅限女生");
//            } else {
//                sex_text.setVisibility(View.GONE);
//                sex_tv.setVisibility(View.GONE);
//                sex_tv.setText("不限性别");
//            }
//            //赋值
//            //"status":活动状态(1报名中,2报名结束),
//            status = activityInfo.getStatus();
//            userRole = activityInfo.getUserRole();
//
//            joinNumber = activityInfo.getJoinNumber();
//            activityNumber = activityInfo.getActivityNumber();
//            SpannableString spannableString;
//            if (activityNumber > 0) {
//                spannableString = new SpannableString("#" + activityInfo.getTotalMen() + "人，#" + activityInfo.getTotalWomen() + "人 （" + joinNumber + "/" + activityNumber + "）");
//            } else {
//                spannableString = new SpannableString("#" + activityInfo.getTotalMen() + "人，#" + activityInfo.getTotalWomen() + "人");
//            }
//            // 男性图标替换
//            ImageSpan imgBoySpan = new ImageSpan(this, R.mipmap.boy_icon);
//            int statIndex = 0;
//            int endIndex = statIndex + 1;
//            spannableString.setSpan(imgBoySpan, statIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            // 女性图标替换
//            statIndex = endIndex + 2 + String.valueOf(activityInfo.getTotalMen()).length();
//            endIndex = statIndex + 1;
//            ImageSpan imgGirlSpan = new ImageSpan(this, R.mipmap.girl_icon);
//            spannableString.setSpan(imgGirlSpan, statIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            join_num_tv.setText(spannableString);
//
//            join_tv.setVisibility(View.VISIBLE);
//            join_tv.setEnabled(true);
//            //用户角色(1组织用户,2普通用户)用户不在活动中返回0,
//            if (status == 1) {
//                status_tv.setText("活动中");
//                if (userRole == 0) {
//                    if (activityNumber >= 0) {
//                        if (joinNumber < activityNumber) {
//                            join_tv.setText("我要报名");
//                        } else {
//                            join_tv.setText("人数已满");
//                            join_tv.setEnabled(false);
//                        }
//                    } else {
//                        join_tv.setText("我要报名");
//                    }
//                } else if (userRole == 2) {
//                    join_tv.setText("退出活动");
//                } else {
//                    join_tv.setVisibility(View.GONE);
//                }
//            } else {
//                status_tv.setText("活动结束");
//                if (userRole == 1) {
//                    join_tv.setText("删除活动");
//                    join_tv.setEnabled(true);
//                } else {
//                    join_tv.setVisibility(View.GONE);
//                    join_tv.setEnabled(false);
//                }
//            }
//            group = groupInfo;
//            if (status == 1) {
//                group_tip_view.setVisibility(View.VISIBLE);
//                if (group == null) {
//                    //没有群
//                    if (userRole == 1) {
//                        joinGroupLayout.setVisibility(View.GONE);
//                        createGroupLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        joinGroupLayout.setVisibility(View.GONE);
//                        createGroupLayout.setVisibility(View.GONE);
//                        group_tip_view.setVisibility(View.GONE);
//                    }
//
//                } else {
//                    //已有群
//                    isExistGroup = group.getIsExistGroup();
//                    groupMode = group.getGroupMode();
//                    joinGroupLayout.setVisibility(View.VISIBLE);
//                    createGroupLayout.setVisibility(View.GONE);
//                    group_name_tv.setText(group.getGroupName());
//                    //当前用户是否在群中(0不在,1在,2申请中),
//                    if (isExistGroup == 0) {
//                        group_join_tv.setText("加入群");
//                        group_join_tv.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
//                    } else if (isExistGroup == 1) {
//                        group_join_tv.setText("发起群聊");
//                        group_join_tv.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
//                    } else {
//                        group_join_tv.setText("申请中");
//                        group_join_tv.setBackground(getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
//                    }
//
//                }
//            } else {
//                //活动结束群布局隐藏
//                joinGroupLayout.setVisibility(View.GONE);
//                createGroupLayout.setVisibility(View.GONE);
//                group_tip_view.setVisibility(View.GONE);
//            }
//
//
//            join_tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (userRole == 0) {
//                        //报名活动
//                        if (sexLimit == 0) {
//                            if (activityInfo.getIsAllowedJoin() == 2) {
//                                Utils.showToastShortTime("本活动仅限圈子成员加入");
//                            } else {
//                                presenter.joinActive(userId, activityId);
//                            }
//                        } else {
//                            //性别有限制
//                            GreenDaoUtils.getInstance().getBaseUser(userId, new GreenDaoUtils.ExecuteCallBack() {
//                                @Override
//                                public void callBack(final Object object) {
//                                    ActiveDetailActivity.this.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            BaseUser currentBaseUser = (BaseUser) object;
//                                            if (sexLimit == 1) {
//                                                //仅限男生参加
//                                                if (currentBaseUser.getSex() == 1) {
//                                                    if (activityInfo.getIsAllowedJoin() == 2) {
//                                                        Utils.showToastShortTime("本活动仅限圈子成员加入");
//                                                    } else {
//                                                        presenter.joinActive(userId, activityId);
//                                                    }
//                                                } else {
//                                                    if (activityInfo.getIsAllowedJoin() == 2) {
//                                                        Utils.showToastShortTime("本活动仅限圈子成员加入");
//                                                    } else {
//                                                        Utils.showToastShortTime("本活动仅限男生加入");
//                                                    }
//                                                }
//                                            } else if (sexLimit == 2) {
//                                                // 仅限女生参加
//                                                if (currentBaseUser.getSex() == 2) {
//                                                    if (activityInfo.getIsAllowedJoin() == 2) {
//                                                        Utils.showToastShortTime("本活动仅限圈子成员加入");
//                                                    } else {
//                                                        presenter.joinActive(userId, activityId);
//                                                    }
//                                                } else {
//                                                    if (activityInfo.getIsAllowedJoin() == 2) {
//                                                        Utils.showToastShortTime("本活动仅限圈子成员加入");
//                                                    } else {
//                                                        Utils.showToastShortTime("本活动仅限女生加入");
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    });
//
//                                }
//                            });
//                        }
//                    } else if (userRole == 1) {
//                        //删除活动
//                        DelectTipDialog dialog = new DelectTipDialog();
//                        dialog.setTitleStr("确定要删除此活动？");
//                        dialog.setItemClick(new DelectTipDialog.ItemClick() {
//                            @Override
//                            public void itemClick() {
//                                presenter.deleteActive(userId, activityId);
//                            }
//                        });
//                        dialog.show(getSupportFragmentManager());
//                    } else {
//                        //退出活动
//                        presenter.signOutActive(userId, activityId);
//                    }
//
//                }
//            });
//
//            if (userRole == 1 && status == 1) {
//                editActiveLayout.setVisibility(View.VISIBLE);
//                findViewById(R.id.charge_active_tv).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //修改活动
//                        if (joinNumber < 2) {
//                            if (activityInfo != null) {
//                                Intent intent = new Intent(new Intent(ActiveDetailActivity.this, BootUpActivity.class));
//                                intent.putExtra("updateActivityType", 2);
//                                intent.putExtra("activityInfo", activityInfo);
//                                startActivity(intent);
//                            }
//                        } else {
//                            //有其他人报名时：不可修改活动。
//                            ActiveEditTipDialog dialog = new ActiveEditTipDialog();
//                            dialog.setItemText("本活动已有小伙伴报名啦，不能修改和删除！", "我知道了");
//                            dialog.show(getSupportFragmentManager());
//                        }
//                    }
//                });
//                findViewById(R.id.delete_active_tv).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //删除活动
//                        if (joinNumber < 2) {
//                            //没其他人报名时：可删除。
//                            DelectTipDialog dialog = new DelectTipDialog();
//                            dialog.setTitleStr("确定要删除此活动？");
//                            dialog.setItemClick(new DelectTipDialog.ItemClick() {
//                                @Override
//                                public void itemClick() {
//                                    presenter.deleteActive(userId, activityId);
//                                }
//                            });
//                            dialog.show(getSupportFragmentManager());
//                        } else {
//                            //有其他人报名时：不可修改活动。
//                            ActiveEditTipDialog dialog = new ActiveEditTipDialog();
//                            dialog.setItemText("本活动已有小伙伴报名啦，不能修改和删除！", "我知道了");
//                            dialog.show(getSupportFragmentManager());
//                        }
//                    }
//                });
//
//            }
//
//        }
//    }
//
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_active_detail);
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
////        titleFragment.setRightViewTitle(R.mipmap.share_icon,"", new TitleFragment.RightViewClick() {
////            @Override
////            public void onClick() {
////                            //弹出分享弹框
////                            String imgUrl="";
////                            if(activityInfo.getImageList()!=null&&activityInfo.getImageList().size()>0){
////                                imgUrl=activityInfo.getImageList().get(0).getThumbnailUrl();
////                            }else{
////                                imgUrl=defaultImg;
////                            }
////                            ShareUtil.getInstance().setDefaultImg(defaultImg);
////                            ShareUtil.getInstance().showShare(ActiveDetailActivity.this,ShareUtil.ACTIVE,activityInfo.getActivityId(),activityInfo,"","",
////                                    "活动找人：“"+activityInfo.getTheme()+"”邀你一起来玩儿！"
////                                    ,imgUrl,true);
////
////                            //弹出举报弹框
//////                            commonPresenter.getReportType();
////                    }
////                });
//
//    }
//
//    public void setHeaderViewData(List<Image> list, String defaultImg) {
//        try {
//            options = new RequestOptions()
//                    .centerCrop()
//                    .transform(new GlideRoundTransform(9))
//                    .placeholder(R.mipmap.error_bg)
//                    .error(R.mipmap.error_bg);
//            List<Image> arrayList = null;
//            final LinearLayout llPointGroup;
//            if (arrayList == null) {
//                arrayList = new ArrayList<Image>();
//            }
//            arrayList.clear();
//            if (list != null && list.size() > 0) {
//                arrayList.addAll(list);
//            } else {
//                Image image = new Image(defaultImg);
//                image.setThumbnailUrl(defaultImg);
//                arrayList.add(image);
//            }
//            List<View> viewList = new ArrayList<View>();
//            viewPager = (CustomViewPager) headView.findViewById(R.id.advertisement_head_view_page);
//            llPointGroup = (LinearLayout) headView.findViewById(R.id.ll_point_group);
//            llPointGroup.removeAllViews();
//            View advertisementView;
//            String activeImgUrl = "";
//            for (int i = 0; i < arrayList.size(); i++) {
//                advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
//                ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
//                final Image image = arrayList.get(i);
//                activeImgUrl = image.getImageUrl();
//                Utils.CornerImageViewDisplayByUrl(activeImgUrl, advertisementImage);
//                viewList.add(advertisementView);
//                final List<Image> finalArrayList1 = arrayList;
//                final int finalI = i;
//                advertisementImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ImageViewPageDialog.newInstance(finalArrayList1, finalI).show(getSupportFragmentManager());
//                    }
//                });
//                if (arrayList.size() == 1) {
//                    break;
//                }
//                //每循环一次需要向LinearLayout中添加一个点的view对象
//                View v = new View(App.getInstance());
//                v.setBackgroundResource(R.drawable.point_bg);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
//                params.leftMargin = 9;
//                params.rightMargin = 9;
//                v.setLayoutParams(params);
//                v.setEnabled(false);
//                llPointGroup.addView(v);
//            }
//            HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
//            viewPager.setAdapter(homeAdvertisementTabAdapter);
//            viewPager.setIsCanScroll(false);
//            viewPager.setCurrentItem(0);
//            if (arrayList.size() > 1) {
//                llPointGroup.getChildAt(previousPosition).setEnabled(true);
//                //开始轮播效果
//                handler.sendEmptyMessageDelayed(AdvertisementHandlerInActivity.MSG_UPDATE_IMAGE, AdvertisementHandlerInActivity.MSG_DELAY);
//            }
//
//            final List<Image> finalArrayList = arrayList;
//            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//                @Override
//                public void onPageSelected(int arg1) {
//                    int position = arg1 % finalArrayList.size();
//                    // 把当前选中的点给切换了, 还有描述信息也切换
//                    if (llPointGroup != null) {
//                        if (llPointGroup.getChildAt(previousPosition) != null) {
//                            llPointGroup.getChildAt(previousPosition).setEnabled(false);
//                        }
//                        if (llPointGroup.getChildAt(position) != null) {
//                            llPointGroup.getChildAt(position).setEnabled(true);
//                        }
//                    }
//                    // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
//                    previousPosition = position;
//                    handler.sendMessage(Message.obtain(handler, AdvertisementHandlerInActivity.MSG_PAGE_CHANGED, arg1, 0));
//                }
//
//                @Override
//                public void onPageScrolled(int arg0, float arg1, int arg2) {
//                }
//
//                //覆写该方法实现轮播效果的暂停和恢复
//                @Override
//                public void onPageScrollStateChanged(int arg0) {
//                    // 当页面的状态改变时将调用此方法
//                    //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
//                    switch (arg0) {
//                        case ViewPager.SCROLL_STATE_DRAGGING:
//                            // 正在拖动页面时执行此处
//                            handler.sendEmptyMessage(AdvertisementHandlerInActivity.MSG_KEEP_SILENT);
//                            break;
//                        case ViewPager.SCROLL_STATE_IDLE:
//                            // 未拖动页面时执行此处
//                            handler.sendEmptyMessageDelayed(AdvertisementHandlerInActivity.MSG_UPDATE_IMAGE, AdvertisementHandlerInActivity.MSG_DELAY);
//                            break;
//                        default:
//                            break;
//                    }
//                }
//
//            });
//
//
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) headView.getLayoutParams();
//            params.height = PlatformInfoUtils.getWidthOrHeight(this)[0] * 40 / 71;
//            headView.setLayoutParams(params);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (apiName.equals(InterfaceUrl.URL_FINDACTIVEDETAIL)) {
//            if (object instanceof FindActiveDetailResponse) {
//                FindActiveDetailResponse findActiveDetailResponse = (FindActiveDetailResponse) object;
//                if (findActiveDetailResponse.getCode() == 200) {
//                    activityInfo = findActiveDetailResponse.getActiveInfo();
//                    if (activityInfo.getIsDel() == 1) {
//                        Utils.showToastShortTime(findActiveDetailResponse.getMsg());
//                        finish();
//                    } else {
//                        List<BaseUser> baseUserList = findActiveDetailResponse.getJoinPersonList();
//                        if (baseUserList != null) {
//                            recyclerAdapter.setList(baseUserList);
//                            recyclerAdapter.notifyDataSetChanged();
//
//                        }
//                        group = findActiveDetailResponse.getGroupInfo();
//                        if (activityInfo != null) {
//                            setActivityData(activityInfo, findActiveDetailResponse.getGroupInfo());
//                            List<OrganizationInfo> organizationInfoList = activityInfo.getOrganList();
//                            if (organizationInfoList != null && organizationInfoList.size() > 0) {
//                                orgainRecyclerAapter.setList(organizationInfoList);
//                                orgainRecyclerAapter.notifyDataSetChanged();
//                                orgain_itp_tv.setVisibility(View.VISIBLE);
//                            } else {
//                                orgain_itp_tv.setVisibility(View.GONE);
//                            }
//                            if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                                setHeaderViewData(activityInfo.getImageList(), "");
//                            } else {
//                                setHeaderViewData(activityInfo.getImageList(), findActiveDetailResponse.getDefaultImg());
//                            }
//                            defaultImg = findActiveDetailResponse.getDefaultImg();
//
//                        }
//                        //弹出报名后弹窗
//                        if (joinActive) {
//                            if (group != null) {
//                                ActiveJoinSuccessTipDialog dialog = new ActiveJoinSuccessTipDialog();
//                                dialog.setText("", joinActivMsg, "稍后聊天", "发起群聊");
//                                dialog.setGravity(Gravity.LEFT);
//                                dialog.setOnClickListener(new ActiveJoinSuccessTipDialog.OnClickListener() {
//                                    @Override
//                                    public void onLeftClick() {
//                                        //取消
//                                    }
//
//                                    @Override
//                                    public void onRightClick() {
//                                        //发起群聊
//                                        RongIMutils.startGroupChat(ActiveDetailActivity.this, group.getGroupId() + "", group.getGroupName());
//                                    }
//                                });
//                                dialog.show(getSupportFragmentManager());
//                            } else {
//                                Utils.showToastShortTime(joinActivMsg);
//                            }
//                            joinActive = false;
//                        }
//                    }
//
//                } else {
//                    Utils.showToastShortTime(findActiveDetailResponse.getMsg());
//                }
//
//
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_DELETEACTIVE)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                if (baseResponse.getCode() == 200) {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                    if (position >= 0) {
//                        EventBus.getDefault().post(new UpdateActiveListEvent(2, position, true, 0));
//                    }
//                    finish();
//                } else {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_JOINACTIVE)) {
//            if (object instanceof JoinActiveResponse) {
//                JoinActiveResponse joinActiveResponse = (JoinActiveResponse) object;
//                if (joinActiveResponse.getCode() == 200) {
//                    joinActive = true;
//                    joinActivMsg = joinActiveResponse.getMsg();
//                    //刷新人数列表
////                    joinNumber++;
////                    if(activityNumber>0){
////                        join_num_tv.setText("报名"+joinNumber+"/"+activityNumber+"人");
////                    }else{
////                        join_num_tv.setText("报名"+joinNumber+"人");
////                    }
////                    recyclerAdapter.addItem(joinActiveResponse.getBaseUser());
////                    join_tv.setText("退出活动");
////                    userRole=2;
//                    presenter.findActiveDetail(userId, activityId);
//                    if (position >= 0) {
//                        EventBus.getDefault().post(new UpdateActiveListEvent(2, position, false, joinNumber + 1));
//                    }
//                } else {
//                    Utils.showToastShortTime(joinActiveResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_SIGNOUTACTIVE)) {
//            if (object instanceof JoinActiveResponse) {
//                JoinActiveResponse joinActiveResponse = (JoinActiveResponse) object;
//                if (joinActiveResponse.getCode() == 200) {
//                    Utils.showToastShortTime(joinActiveResponse.getMsg());
//                    //刷新人数列表
//                    joinNumber--;
//                    if (activityNumber > 0) {
//                        join_num_tv.setText("（" + joinNumber + "/" + activityNumber + "）");
//                        join_num_tv.setVisibility(View.VISIBLE);
//                    } else {
//                        join_num_tv.setVisibility(View.GONE);
//                    }
//                    recyclerAdapter.removeItem(userId);
//                    join_tv.setText("我要报名");
//                    userRole = 0;
////                        presenter.findActiveDetail(userId,activityId);
//                    if (position >= 0) {
//                        EventBus.getDefault().post(new UpdateActiveListEvent(2, position, false, joinNumber));
//                    }
//                } else {
//                    Utils.showToastShortTime(joinActiveResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_JOIN_GROUP)) {
//            if (object instanceof JoinGroupResponse) {
//                JoinGroupResponse baseResponse = (JoinGroupResponse) object;
//                if (baseResponse.getCode() == 200) {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                    if (groupMode == 1) {
//                        isExistGroup = 1;
//                        group_join_tv.setText("发起群聊");
//                        group_join_tv.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
//                    } else {
//                        isExistGroup = 2;
//                        group_join_tv.setText("申请中");
//                        group_join_tv.setBackground(getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
//                        Utils.showToastShortTime(baseResponse.getMsg());
//                    }
//                } else {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_GETREPORTTYPE.equals(apiName)) {
//            if (object instanceof GetReportTypeResponse) {
//                GetReportTypeResponse response = (GetReportTypeResponse) object;
//                final List<ReportType> reportTypeList = response.getReprotTypeList();
//                if (response.getCode() == 200 && reportTypeList != null && reportTypeList.size() > 0) {
//                    PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
//                    String[] str = new String[reportTypeList.size()];
//                    for (int i = 0; i < reportTypeList.size(); i++) {
//                        str[i] = reportTypeList.get(i).getReportName();
//                    }
//                    postDetailCommentDialog.setItemStr(str);
//                    postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
//                        @Override
//                        public void itemClick(int position, String content) {
//                            //举报活动
//                            commonPresenter.reportOperation(activityInfo.getActivityId(), 9, userId, 0, reportTypeList.get(position).getReportTypeId());
//
//                        }
//                    });
//                    postDetailCommentDialog.show(getSupportFragmentManager());
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        }
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
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(UpdateActiveDetailEvent event) {
//        if (event == null) {
//            return;
//        }
//        presenter.findActiveDetail(userId, activityId);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (handler != null) {
//            handler.clearData();
//            handler.removeCallbacksAndMessages(null);
//            handler = null;
//        }
//        if (viewPager != null) {
//            viewPager.removeAllViews();
//            viewPager = null;
//        }
//        EventBus.getDefault().unregister(this);
//    }
//
//
//}
