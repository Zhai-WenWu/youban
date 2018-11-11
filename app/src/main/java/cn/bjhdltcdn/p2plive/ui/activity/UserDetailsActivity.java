package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.JoinGroupEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetMyShareListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetReportTypeResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeIsCreateStoreResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.OccupationInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SchoolInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PkPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.SharePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeAdvertisementTabAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeAttentionRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.UserDetailTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.PostDetailCommentDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.ResouceImageDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.UserIconDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.BrandBusinessDetailAgentGoodsFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.UserFansFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.BrandBusinessDetailGoodsFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.BrandBusinessDetailEvaluateFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
import cn.bjhdltcdn.p2plive.widget.DrawableCenterTextView;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by zhaiww on 2017/11/18.
 */

public class UserDetailsActivity extends BaseActivity implements BaseView, View.OnClickListener {

    private UserPresenter userPresenter;
    private GroupPresenter groupPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    private SharePresenter sharePresenter;
    private GetCommentListPresenter getCommentListPresenter;
    private PostCommentListPresenter postCommentListPresenter;
    private CommonPresenter commonPresenter;
    private GetStoreListPresenter getStoreListPresenter;
    private float alpha;
    private ScrollView scroll_view;
    private RelativeLayout rv_title;
    private TabLayout tab_jing;
    private TabLayout tab_dong;
    private int[] location1;
    private int[] location2;
    public CustomViewPager mViewPager;
    private UserDetailTabFragmentAdapter adapter;
    private BrandBusinessDetailGoodsFragment brandBusinessDetailGoodsFragment;
    private BrandBusinessDetailEvaluateFragment brandBusinessDetailEvaluateFragment;
    private BrandBusinessDetailAgentGoodsFragment brandBusinessDetailAgentGoodsFragment;
    private UserFansFragment userFansFragment;
    private BaseUser baseUser;
    private ImageView img_icon;
    private TextView tv_username_top;
    private TextView tv_userid_top;
    private TextView tv_username_bottom;
    private TextView tv_userid_bottom;
    private TextView tv_age;
    private DrawableCenterTextView tv_location;
    private int attentionCount;
    private int fansCount;
    private TextView tv_occupation;
    private TextView tv_school;
    private TextView tv_sign;
    private CustomViewPager viewPager;
    private List<Image> imageList;
    private RelativeLayout rl_photo;
    boolean isRunning = true;
    private ImageView iv_back;
    private ImageView iv_more;
    private User user;
    private DrawableCenterTextView tv_attention;
    private int attentionType;
    private RecyclerView recycler_view_post;
    private HomeAttentionRecyclerViewAdapter postListAdapter;
    private ArrayList<HomeInfo> publishList;
    private LinearLayout ll_jing;
    private Button bt_publish_jing;
    private Button bt_shop_jing;
    private Button bt_attention_jing;
    private Button bt_fans_jing;
    private LinearLayout ll_dong;
    private Button bt_publish_dong;
    private Button bt_shop_dong;
    private Button bt_attention_dong;
    private Button bt_fans_dong;
    private DrawableCenterTextView rela_msg;
    private SmartRefreshLayout refreshLayout;
    private long userId;
    private int pageSize = 10;
    private int pageNum = 1;
    private GetMyShareListResponse getMyShareListResponse;
    private long storeId;
    private int postPosition;
    private PkPresenter pkPresenter;
    private boolean finishLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        userPresenter = new UserPresenter(this);
        groupPresenter = new GroupPresenter(this);
        chatRoomPresenter = new ChatRoomPresenter(this);
        sharePresenter = new SharePresenter(this);
        getCommentListPresenter = new GetCommentListPresenter(this);
        postCommentListPresenter = new PostCommentListPresenter(this);
        commonPresenter = new CommonPresenter(this);
        getStoreListPresenter = new GetStoreListPresenter(this);
        pkPresenter = new PkPresenter(this);
        setContentView(R.layout.activity_user_details);
        baseUser = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);
        if (baseUser == null) {
            return;
        }
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        userPresenter.getUserInfo(userId, baseUser.getUserId());
        getStoreListPresenter.judgeIsCreateStore(baseUser.getUserId());
        sharePresenter.getMyShareList(userId, baseUser.getUserId(), 10, 1, false);
        init();
        initRefresh();
        setTitle();
    }

    private void init() {
        scroll_view = findViewById(R.id.scroll_view);
        rv_title = findViewById(R.id.rv_title);
        tab_jing = findViewById(R.id.tab_jing);
        tab_dong = findViewById(R.id.tab_dong);
        mViewPager = findViewById(R.id.user_detail_view_page);
        img_icon = findViewById(R.id.img_icon);
        tv_username_top = findViewById(R.id.tv_username_top);
        tv_userid_top = findViewById(R.id.tv_userid_top);
        tv_username_bottom = findViewById(R.id.tv_username_bottom);
        tv_userid_bottom = findViewById(R.id.tv_userid_bottom);
        tv_age = findViewById(R.id.tv_age);
        tv_location = findViewById(R.id.tv_location);
        tv_occupation = findViewById(R.id.tv_occupation);
        tv_school = findViewById(R.id.tv_school);
        tv_sign = findViewById(R.id.tv_sign);
        viewPager = findViewById(R.id.advertisement_head_view_page);
        rl_photo = findViewById(R.id.rl_photo);
        iv_back = findViewById(R.id.iv_back);
        iv_more = findViewById(R.id.iv_more);
        tv_attention = findViewById(R.id.tv_attention);
        recycler_view_post = findViewById(R.id.recycler_view_post);
        ll_jing = findViewById(R.id.ll_jing);
        bt_publish_jing = findViewById(R.id.bt_publish_jing);
        bt_shop_jing = findViewById(R.id.bt_shop_jing);
        bt_attention_jing = findViewById(R.id.bt_attention_jing);
        bt_fans_jing = findViewById(R.id.bt_fans_jing);
        ll_dong = findViewById(R.id.ll_dong);
        bt_publish_dong = findViewById(R.id.bt_publish_dong);
        bt_shop_dong = findViewById(R.id.bt_shop_dong);
        bt_attention_dong = findViewById(R.id.bt_attention_dong);
        bt_fans_dong = findViewById(R.id.bt_fans_dong);
        rela_msg = findViewById(R.id.rela_msg);
        refreshLayout = findViewById(R.id.refreshLayout);
        iv_back.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        tv_attention.setOnClickListener(this);
        bt_publish_jing.setOnClickListener(this);
        bt_shop_jing.setOnClickListener(this);
        bt_attention_jing.setOnClickListener(this);
        bt_fans_jing.setOnClickListener(this);
        bt_publish_dong.setOnClickListener(this);
        bt_shop_dong.setOnClickListener(this);
        bt_attention_dong.setOnClickListener(this);
        bt_fans_dong.setOnClickListener(this);
        rela_msg.setOnClickListener(this);
        img_icon.setOnClickListener(this);
    }


    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                sharePresenter.getMyShareList(userId, baseUser.getUserId(), pageSize, pageNum, false);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                sharePresenter.getMyShareList(userId, baseUser.getUserId(), pageSize, pageNum, false);
            }
        });
    }

    private void initTab() {
        adapter = new UserDetailTabFragmentAdapter(getSupportFragmentManager());
        brandBusinessDetailGoodsFragment = new BrandBusinessDetailGoodsFragment();
        adapter.addFragment(brandBusinessDetailGoodsFragment, "发布");
        brandBusinessDetailEvaluateFragment = new BrandBusinessDetailEvaluateFragment();
        adapter.addFragment(brandBusinessDetailEvaluateFragment, "店铺");
        brandBusinessDetailAgentGoodsFragment = new BrandBusinessDetailAgentGoodsFragment();
        adapter.addFragment(brandBusinessDetailAgentGoodsFragment, "关注 " + attentionCount);
        userFansFragment = new UserFansFragment();
        adapter.addFragment(userFansFragment, "粉丝 " + fansCount);
        mViewPager.setAdapter(adapter);
        tab_jing.setupWithViewPager(mViewPager);
        tab_dong.setupWithViewPager(mViewPager);
        for (int i = 0; i < tab_dong.getTabCount(); i++) {
            TabLayout.Tab tab = tab_dong.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tab.setCustomView(tabView);
        }

        for (int i = 0; i < tab_jing.getTabCount(); i++) {
            TabLayout.Tab tab = tab_jing.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tab.setCustomView(tabView);
        }

        tab_jing.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_333333));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));

                switch (tab.getPosition()) {
                    case 1:
                        startShopActivity();
                        break;
                    case 2:
                        startAttentionActivity();
                        break;
                    case 3:
                        startFansActivity();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_999999));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab_dong.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_333333));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));

                switch (tab.getPosition()) {
                    case 1:
                        startShopActivity();
                        break;
                    case 2:
                        startAttentionActivity();
                        break;
                    case 3:
                        startFansActivity();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_999999));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void startAttentionActivity() {
        Intent intent = new Intent(UserDetailsActivity.this, AttentionOrFansActivity.class);
        intent.putExtra(Constants.Fields.TYPE, 0);
        intent.putExtra(Constants.Fields.TO_USER_ID, user.getUserId());
        startActivity(intent);
    }

    private void startShopActivity() {
        Intent intent6 = new Intent(UserDetailsActivity.this, ShopDetailActivity.class);
        intent6.putExtra(Constants.Fields.STORE_ID, storeId);
        startActivity(intent6);
    }

    private void startFansActivity() {
        Intent intent = new Intent(UserDetailsActivity.this, AttentionOrFansActivity.class);
        intent.putExtra(Constants.Fields.TYPE, 1);
        intent.putExtra(Constants.Fields.TO_USER_ID, user.getUserId());
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                final PostOperationFragmentDialog dialog = new PostOperationFragmentDialog();
                dialog.setTextList("拉黑", "举报", "取消");
                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {


                    @Override
                    public void onClick(int type) {
                        switch (type) {
                            case 1:
                                RongIM.getInstance().addToBlacklist(String.valueOf(user.getUserId()), new RongIMClient.OperationCallback() {
                                    @Override
                                    public void onSuccess() {
                                        userPresenter.pullBlackUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), user.getUserId());
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
                                break;
                            case 2:
                                commonPresenter.getReportType(1);
                                break;
                        }
                    }
                });

                dialog.show(getSupportFragmentManager());
                break;

            case R.id.tv_attention:
                userPresenter.attentionOperation(attentionType, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), user.getUserId());
                break;
            case R.id.img_icon:
                UserIconDialog.newInstance(user.getUserIcon()).show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.bt_shop_dong:
                startShopActivity();
                break;
            case R.id.bt_shop_jing:
                startShopActivity();
                break;
            case R.id.bt_attention_dong:
                startAttentionActivity();
                break;
            case R.id.bt_attention_jing:
                startAttentionActivity();
                break;
            case R.id.bt_fans_dong:
                startFansActivity();
                break;
            case R.id.bt_fans_jing:
                startFansActivity();
                break;
            case R.id.rela_msg:
                RongIMutils.startToConversation(this, baseUser.getUserId() + "", baseUser.getNickName());
                break;
        }
    }

    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }
        switch (apiName) {

            case InterfaceUrl.URL_GETUSERINFO:
                if (object instanceof GetUserInfoResponse) {
                    GetUserInfoResponse getUserInfoResponse = (GetUserInfoResponse) object;
                    if (getUserInfoResponse.getCode() == 200) {
                        user = getUserInfoResponse.getUser();
                        finishLoading = true;
                        initUserInfo();

                    } else if (getUserInfoResponse.getCode() == 201) {
                        finish();
                        Utils.showToastShortTime(getUserInfoResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_ATTENTIONOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    if (baseResponse.getCode() == 200) {
                        if (attentionType == 1) {
                            attentionType = 2;
                            tv_attention.setText("取消关注");
                            tv_attention.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(UserDetailsActivity.this, R.drawable.userdetail_un_atten), null, null, null);
                        } else if (attentionType == 2) {
                            attentionType = 1;
                            tv_attention.setText("关注");
                            tv_attention.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(UserDetailsActivity.this, R.drawable.userdetail_atten), null, null, null);
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_GETMYSHARELIST:
                if (object instanceof GetMyShareListResponse) {
                    getMyShareListResponse = (GetMyShareListResponse) object;
                    if (getMyShareListResponse.getCode() == 200) {
                        if (getMyShareListResponse.getList().size() > 0) {
                            publishList = getMyShareListResponse.getList();
                            initPublish();
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_GETREPORTTYPE:
                if (object instanceof GetReportTypeResponse) {
                    GetReportTypeResponse response = (GetReportTypeResponse) object;
                    final List<ReportType> reportTypeList = response.getReprotTypeList();
                    if (response.getCode() == 200 && reportTypeList != null && reportTypeList.size() > 0) {
                        PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
                        String[] str = new String[reportTypeList.size()];
                        for (int i = 0; i < reportTypeList.size(); i++) {
                            str[i] = reportTypeList.get(i).getReportName();
                        }
                        postDetailCommentDialog.setItemStr(str);
                        postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
                            @Override
                            public void itemClick(int position, String content) {
                                commonPresenter.reportOperation(user.getUserId(), 5, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), user.getUserId(), reportTypeList.get(position).getReportTypeId());
                            }
                        });
                        postDetailCommentDialog.show(getSupportFragmentManager());
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_REPORTOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
                break;
            case InterfaceUrl.URL_PULLBLACKUSER:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                    if (baseResponse.getCode() == 200) {
                        finish();
                    }
                }
                break;
            case InterfaceUrl.URL_JUDGEISCREATESTORE:
                if (object instanceof JudgeIsCreateStoreResponse) {
                    JudgeIsCreateStoreResponse response = (JudgeIsCreateStoreResponse) object;
                    StoreDetail storeDetail = response.getStoreDetail();
                    if (response.getCode() == 200 && storeDetail != null && storeDetail.getIsAuth() == 2) {
                        storeId = storeDetail.getStoreInfo().getStoreId();
                        bt_shop_dong.setVisibility(View.VISIBLE);
                        bt_shop_jing.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case InterfaceUrl.URL_POSTPRAISE:
                if (object instanceof PostPraiseResponse) {
                    PostPraiseResponse postPraiseResponse = (PostPraiseResponse) object;
                    Utils.showToastShortTime(postPraiseResponse.getMsg());
                    if (postPraiseResponse.getCode() == 200) {
                        int isPraise = postPraiseResponse.getIsPraise();
                        PostInfo postInfo = postListAdapter.getItem(postPosition).getPostInfo();
                        int praiseCount = postInfo.getPraiseCount();
                        if (isPraise == 1) {
                            postInfo.setPraiseCount(praiseCount + 1);
                            postInfo.setIsPraise(1);
                        } else if (isPraise == 2) {
                            postInfo.setPraiseCount(praiseCount - 1);
                            postInfo.setIsPraise(0);
                        }
                        postListAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }

    }

    private void initUserInfo() {

        if (user.getIsAttention() == 1) {
            tv_attention.setText("取消关注");
            tv_attention.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.userdetail_un_atten), null, null, null);
        } else if (user.getIsAttention() == 0) {
            tv_attention.setText("关注");
            tv_attention.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.userdetail_atten), null, null, null);
        }
        attentionType = user.getIsAttention() + 1;

        String userIcon = user.getUserIcon();
        Utils.ImageViewDisplayByUrl(userIcon, img_icon);

        String nickName = user.getNickName();
        if (!TextUtils.isEmpty(nickName)) {
            tv_username_bottom.setText(nickName);
            tv_username_top.setText(nickName);
        }

        int sex = user.getSex();
        Drawable sexDrawable = null;
        if (sex == 1) {
            sexDrawable = ContextCompat.getDrawable(UserDetailsActivity.this, R.mipmap.boy_icon);
        } else if (sex == 2) {
            sexDrawable = ContextCompat.getDrawable(UserDetailsActivity.this, R.mipmap.girl_icon);

        }

        if (sexDrawable != null) {
            sexDrawable.setBounds(0, 0, Utils.dp2px(12), Utils.dp2px(12));
            tv_age.setCompoundDrawables(sexDrawable, null, null, null);
        }

        bt_attention_dong.setText("关注 " + user.getAttentionCount());
        bt_attention_jing.setText("关注 " + user.getAttentionCount());
        bt_fans_dong.setText("粉丝 " + user.getFansCount());
        bt_fans_jing.setText("粉丝 " + user.getFansCount());

        int age = user.getAge();
        tv_age.setText(age + "岁");
        long userId = user.getUserId();
        tv_userid_bottom.setText("ID：" + userId);
        tv_userid_top.setText("ID：" + userId);
        String location = user.getLocation();
        tv_location.setText(location);
        attentionCount = user.getAttentionCount();
        fansCount = user.getFansCount();
        OccupationInfo occupationInfo = user.getOccupationInfo();
        if (occupationInfo != null) {
            tv_occupation.setText(occupationInfo.getOccupationName());
            SchoolInfo schoolInfo = user.getSchoolInfo();
            if (schoolInfo != null) {
                tv_school.setText(schoolInfo.getSchoolName());
            }
        }
        //签名
        String signature = user.getSignature();
        if (TextUtils.isEmpty(signature.trim()) || "null".equals(signature) || "NULL".equals(signature)) {
//            signature = "我就是我，不一样的烟火";
            tv_sign.setVisibility(View.GONE);
            findViewById(R.id.view_1).setVisibility(View.GONE);
        }
        tv_sign.setText(signature);

        //相册
        imageList = user.getImageList();
        initPhoto();
    }

    //发布
    private void initPublish() {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        postListAdapter = new HomeAttentionRecyclerViewAdapter(UserDetailsActivity.this);
        postListAdapter.setUserType(4);
        postListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                {
                    HomeInfo homeInfo = postListAdapter.getItem(position);
                    switch (homeInfo.getType()) {
                        case 1:
                        case 11:
                            //1帖子详情页
                            if (homeInfo.getPostInfo() != null) {
                                if (homeInfo.getPostInfo().getContentLimit() != 2) {
                                    Intent intent = new Intent(UserDetailsActivity.this, PostDetailActivity.class);
                                    intent.putExtra(Constants.KEY.KEY_OBJECT, homeInfo.getPostInfo());
                                    intent.putExtra(Constants.KEY.KEY_POSITION, position);
                                    startActivity(intent);
                                }
                            }
                            break;
                        case 2:
                        case 12:
                            //2圈子
                            if (homeInfo.getOrganizationInfo() != null) {
                            }
                            break;
                        case 3:
                        case 13:
                            //3活动详情页
                            break;
                        case 4:
                        case 14:
                            //4进入房间
                            RoomInfo roomInfo = homeInfo.getRoomInfo();
                            if (roomInfo != null) {
                                if (roomInfo.getPasswordType() == 0) {
                                    //不加密
                                    chatRoomPresenter.updateUserStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomInfo.getRoomId(), 0, roomInfo.getPasswordType(),
                                            roomInfo.getPassword());
                                } else if (roomInfo.getPasswordType() == 1) {
                                    //加密
                                    chatRoomPresenter.findRoomDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomInfo.getRoomId());
                                }
                            }
                            break;
                        case 5:
                        case 15:
                            //5进入pk播放视频界面
                            if (homeInfo.getPlayInfo() != null) {
                                pkPresenter.findPlayDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), homeInfo.getPlayInfo().getPlayId());
                            }
                            break;
                        case 8:
                        case 18:
                            //8进入表白详情
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        postListAdapter.setOnClick(new HomeAttentionRecyclerViewAdapter.OnClick() {
            @Override
            public void onPraise(long postId, int type, int position, int operationType) {
                postPosition = position;
                postCommentListPresenter.postPraise(userId, postId, type);
            }

            @Override
            public void onOrgain(long orgainId) {

            }

            @Override
            public void onDelete(long postId, int position) {

            }

            @Override
            public void onDeleteShare(long shareId, int position) {

            }

            @Override
            public void onJoinClick(int position, int type) {

            }

            @Override
            public void onApplyClerk(long storeId) {

            }
        });
        recycler_view_post.setHasFixedSize(true);
        recycler_view_post.setNestedScrollingEnabled(false);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(11));
        recycler_view_post.setLayoutManager(linearlayoutManager);
        recycler_view_post.addItemDecoration(linearLayoutSpaceItemDecoration);
        recycler_view_post.setAdapter(postListAdapter);
        if (publishList.size() < getMyShareListResponse.getTotal()) {
            refreshLayout.setEnableLoadMore(true);
        } else {
            refreshLayout.setEnableLoadMore(false);
        }
        if (pageNum == 1) {
            postListAdapter.setList(publishList, "");
        } else {
            postListAdapter.addList(publishList);
        }
    }

    //相册
    private void initPhoto() {

        if (imageList.size() == 0) {
            viewPager.setBackground(getResources().getDrawable(R.mipmap.store_default_bg));
            rl_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ResouceImageDialog().show(getSupportFragmentManager(),"dialog");
                }
            });
        }

        final LinearLayout llPointGroup;
        List<View> viewList = new ArrayList<View>();
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        llPointGroup.removeAllViews();
        View advertisementView;
        for (int i = 0; i < imageList.size(); i++) {
            advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
            ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
            Utils.ImageViewDisplayByUrl(imageList.get(i).getImageUrl(), advertisementImage);
            viewList.add(advertisementView);
            //每循环一次需要向LinearLayout中添加一个点的view对象
            View v = new View(App.getInstance());
            v.setBackgroundResource(R.drawable.point_bg);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
            params.leftMargin = 9;
            params.rightMargin = 9;
            v.setLayoutParams(params);
            v.setEnabled(false);
            llPointGroup.addView(v);
            advertisementImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageViewPageDialog.newInstance(imageList, imageList.size()).show(getSupportFragmentManager());
                }
            });
        }

        HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
        viewPager.setAdapter(homeAdvertisementTabAdapter);
        viewPager.setIsCanScroll(false);
        viewPager.setCurrentItem(0);
        if (imageList.size() > 1) {
            new Thread() {
                public void run() {
                    while (isRunning) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 往下跳一位
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Log.d("当前位置", "" + viewPager.getCurrentItem());
                                int position = viewPager.getCurrentItem() + 1 % imageList.size();
                                viewPager.setCurrentItem(position);
                            }
                        });
                    }
                }
            }.start();

        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < llPointGroup.getChildCount(); i++) {
                    View v = llPointGroup.getChildAt(i);
                    v.setEnabled(false);
                }
                View v = llPointGroup.getChildAt(position % imageList.size());
                v.setEnabled(true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_photo.getLayoutParams();
        int i = PlatformInfoUtils.getWidthOrHeight(this)[0] * 50 / 75;
        params.height = i;
        rl_photo.setLayoutParams(params);

        LinearLayout ll_icon = findViewById(R.id.ll_icon);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ll_icon.getLayoutParams());
        lp.setMargins(40, i - 75, 0, 0);
        ll_icon.setLayoutParams(lp);
    }

    private void setTitle() {
        alpha = (float) 0.0;
        location1 = new int[2];
        location2 = new int[2];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scroll_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int j, int i1, int i2, int i3) {
                    alpha = (float) i1 / 480;
                    if (alpha > 0 && alpha < 1) {
                        rv_title.setAlpha((float) alpha);
                    }
                    if (alpha == 0) {
                        rv_title.setAlpha(0);
                    }
                    if (alpha >= 1) {
                        rv_title.setAlpha(1);
                    }

                    ll_dong.getLocationOnScreen(location1);
                    ll_jing.getLocationOnScreen(location2);

                    if (location1[1] <= location2[1]) {
                        ll_jing.setVisibility(View.VISIBLE);
                    } else {
                        ll_jing.setVisibility(View.GONE);
                    }

                }
            });
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(JoinGroupEvent event) {
        if (event != null && event.getGroupItemPosition() <= 2) {
//            userPresenter.getUserInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), baseUser.getUserId());
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        if (finishLoading) {
            ProgressDialogUtils.getInstance().hideProgressDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        isRunning = false;

        if (groupPresenter != null) {
            groupPresenter.onDestroy();
            groupPresenter = null;
        }

        if (commonPresenter != null) {
            commonPresenter.onDestroy();
            commonPresenter = null;
        }

        if (chatRoomPresenter != null) {
            chatRoomPresenter.onDestroy();
            chatRoomPresenter = null;
        }


        if (getCommentListPresenter != null) {
            getCommentListPresenter.onDestroy();
            getCommentListPresenter = null;
        }

        if (postCommentListPresenter != null) {
            postCommentListPresenter.onDestroy();
            postCommentListPresenter = null;
        }

        if (sharePresenter != null) {
            sharePresenter.onDestroy();
            sharePresenter = null;
        }

    }
}
