package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.AttentionMessageEvent;
import cn.bjhdltcdn.p2plive.event.AttenttionResultEvent;
import cn.bjhdltcdn.p2plive.event.UpdatePayResultEvent;
import cn.bjhdltcdn.p2plive.event.UserAgeEvent;
import cn.bjhdltcdn.p2plive.event.UserIconEvent;
import cn.bjhdltcdn.p2plive.event.UserNameEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserVideoInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeCreateStoreAuthResponse;
import cn.bjhdltcdn.p2plive.model.OccupationIndexInfo;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ApplyForShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.AttentionOrFansActivity;
import cn.bjhdltcdn.p2plive.ui.activity.CreateShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.EditInfoActivity;
import cn.bjhdltcdn.p2plive.ui.activity.GroupListActivity;
import cn.bjhdltcdn.p2plive.ui.activity.MoreActivity;
import cn.bjhdltcdn.p2plive.ui.activity.MsgRemindActivity;
import cn.bjhdltcdn.p2plive.ui.activity.OrderListActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PostListActivity;
import cn.bjhdltcdn.p2plive.ui.activity.RealNameCertificationActivity;
import cn.bjhdltcdn.p2plive.ui.activity.RealNameCertificationAndShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.SellersWithdrawCashActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.StoreCategoryActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.UserIconDialog;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hu_PC on 2017/11/8.
 * 我的
 */

public class MyFragment extends BaseFragment implements BaseView, View.OnClickListener {
    private View rootView;
    private TextView tv_nickname, tv_age, tv_id, tv_location, tv_num_attention, tv_num_fans, tv_club, tv_my_saylove,
            tv_group, tv_activity, tv_public, tv_msg_remind, tv_more, tv_my_help, tv_order, tv_account;
    private CircleImageView img_icon;
    private ToggleButton togglebtn_call;
    private ImageView img_right, iv_attention;
    private UserPresenter userPresenter;
    private User user;
    private RelativeLayout rela_userinfo, rela_attention, rela_fans;
    private int attentionCount;
    private DiscoverPresenter discoverPresenter;
    private String userIcon;
    private int fansCount;
    private int status;//是否接收陌生人来电(1接收,2拒绝),
    private boolean editInfoNeedUpdata = false;
    private TextView tv_shop;
    private GetStoreListPresenter getStoreListPresenter;
    private long userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my, null);
        }

        EventBus.getDefault().register(this);
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
        setTitle();
        initView();
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public GetStoreListPresenter getGetStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }


    @Override
    protected void onVisible(boolean isInit) {

        Logger.d("isInit === " + isInit);

        if (isInit) {
            getUserPresenter().findUserVideoInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
            getUserPresenter().getUserInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
        }
    }

    private void initView() {
        tv_nickname = rootView.findViewById(R.id.tv_nickname);
        tv_order = rootView.findViewById(R.id.tv_order);
        tv_account = rootView.findViewById(R.id.tv_account);
        iv_attention = rootView.findViewById(R.id.iv_attention);
        tv_age = rootView.findViewById(R.id.tv_age);
        tv_id = rootView.findViewById(R.id.tv_id);
        img_icon = rootView.findViewById(R.id.img_icon);
        img_right = rootView.findViewById(R.id.img_right);
        tv_my_saylove = rootView.findViewById(R.id.tv_my_say_love);
        img_right.setVisibility(View.VISIBLE);
        tv_location = rootView.findViewById(R.id.tv_location);
        rela_userinfo = rootView.findViewById(R.id.rela_userinfo);
        rela_attention = rootView.findViewById(R.id.rela_attention);
        rela_fans = rootView.findViewById(R.id.rela_fans);
        tv_num_attention = rootView.findViewById(R.id.tv_num_attention);
        tv_num_fans = rootView.findViewById(R.id.tv_num_fans);
        tv_club = rootView.findViewById(R.id.tv_club);
        tv_group = rootView.findViewById(R.id.tv_group);
        tv_activity = rootView.findViewById(R.id.tv_activity);
        tv_public = rootView.findViewById(R.id.tv_public);
        tv_msg_remind = rootView.findViewById(R.id.tv_msg_remind);
        tv_more = rootView.findViewById(R.id.tv_more);
        tv_my_help = rootView.findViewById(R.id.tv_my_help);
        togglebtn_call = rootView.findViewById(R.id.togglebtn_call);
        tv_shop = rootView.findViewById(R.id.tv_shop);
        rela_userinfo.setOnClickListener(this);
        rela_attention.setOnClickListener(this);
        img_icon.setOnClickListener(this);
        rela_fans.setOnClickListener(this);
        tv_club.setOnClickListener(this);
        tv_group.setOnClickListener(this);
        tv_order.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        tv_activity.setOnClickListener(this);
        tv_public.setOnClickListener(this);
        tv_msg_remind.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        tv_my_saylove.setOnClickListener(this);
        tv_shop.setOnClickListener(this);
        tv_my_help.setOnClickListener(this);
        togglebtn_call.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getUserPresenter().updateUserVideoInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1);
                } else {
                    getUserPresenter().updateUserVideoInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 2);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rela_userinfo:
                if (user == null) {
                    return;
                }

                intent = new Intent(getContext(), EditInfoActivity.class);
                if (!editInfoNeedUpdata) {
                    intent.putExtra(Constants.Fields.USER, user);
                }
                startActivity(intent);
                break;
            case R.id.img_icon:
                UserIconDialog.newInstance(userIcon).show(getFragmentManager(), "dialog");
                break;
            case R.id.rela_attention:
                intent = new Intent(getContext(), AttentionOrFansActivity.class);
                intent.putExtra(Constants.Fields.TYPE, 0);
                startActivity(intent);
                break;
            case R.id.rela_fans:
                intent = new Intent(getContext(), AttentionOrFansActivity.class);
                intent.putExtra(Constants.Fields.TYPE, 1);
                startActivity(intent);
                iv_attention.setVisibility(View.GONE);
                break;
            case R.id.tv_club:
                break;
            case R.id.tv_group:
                intent = new Intent(getActivity(), GroupListActivity.class);
                intent.putExtra(Constants.Fields.TYPE, 1);
                startActivity(intent);
                break;
            case R.id.tv_activity:
                break;
            case R.id.tv_public:
                intent = new Intent(getContext(), PostListActivity.class);
                intent.putExtra(Constants.Fields.TYPE, 1);
                startActivity(intent);
                break;
            case R.id.tv_order:
                startActivity(new Intent(getContext(), OrderListActivity.class));
                break;
            case R.id.tv_account:
                Intent intent1 = new Intent(getContext(), SellersWithdrawCashActivity.class);
                intent1.putExtra("storeId", 0);
                startActivity(intent1);
                break;
            case R.id.tv_msg_remind:
                startActivity(new Intent(getContext(), MsgRemindActivity.class));
                break;
            case R.id.tv_more:
                intent = new Intent(getContext(), MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_my_help:
                break;
            case R.id.tv_my_say_love:
                break;
            case R.id.tv_shop:
                getGetStoreListPresenter().judgeCreateStoreAuth(userId);
                break;

        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETUSERINFO:
                if (object instanceof GetUserInfoResponse) {
                    GetUserInfoResponse getUserInfoResponse = (GetUserInfoResponse) object;
                    if (getUserInfoResponse.getCode() == 200) {
                        user = getUserInfoResponse.getUser();

                        if (user == null) {
                            return;
                        }

                        Utils.userToBaseUser(user);

                        // 认证状态
                        SafeSharePreferenceUtils.saveInt(Constants.Fields.AUTHSTATUS, user.getAuthStatus());

                        userIcon = user.getUserIcon();

                        String nickName = user.getNickName();
                        if (!TextUtils.isEmpty(nickName)) {
                            tv_nickname.setText(nickName);
                        }
                        int sex = user.getSex();
                        Drawable sexDrawable = null;
                        if (this.isAdded()) {
                            if (sex == 1) {
                                sexDrawable = getResources().getDrawable(R.mipmap.boy_icon);
                            } else if (sex == 2) {
                                sexDrawable = getResources().getDrawable(R.mipmap.girl_icon);
                            }
                        }
                        if (sexDrawable != null) {
                            sexDrawable.setBounds(0, 0, Utils.dp2px(12), Utils.dp2px(12));
                        }
                        tv_age.setCompoundDrawables(sexDrawable, null, null, null);
                        int age = user.getAge();
                        tv_age.setText(age + "岁");
                        userId = user.getUserId();
                        tv_id.setText("ID：" + userId);
                        String location = user.getLocation();
                        tv_location.setText(location);
                        attentionCount = user.getAttentionCount();
                        tv_num_attention.setText(String.valueOf(attentionCount));
                        fansCount = user.getFansCount();
                        tv_num_fans.setText(String.valueOf(fansCount));
                        RequestOptions options = new RequestOptions().centerCrop();
                        Glide.with(this).asBitmap().load(userIcon).apply(options).into(img_icon);
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATEUSERVIDEOINFO:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    if (baseResponse.getCode() == 200) {
                        if (status == 1) {
                            status = 2;
                        } else {
                            status = 1;
                        }
                    } else {
                        Utils.showToastShortTime(baseResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_FINDUSERVIDEOINFO:
                if (object instanceof FindUserVideoInfoResponse) {
                    FindUserVideoInfoResponse infoResponse = (FindUserVideoInfoResponse) object;
                    if (infoResponse.getCode() == 200) {
                        status = infoResponse.getStatus();
                        if (status == 1) {
                            togglebtn_call.setChecked(true);
                        } else {
                            togglebtn_call.setChecked(false);
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_JUDGECREATESTOREAUTH:
                if (object instanceof JudgeCreateStoreAuthResponse) {
                    JudgeCreateStoreAuthResponse judgeCreateStoreAuthResponse = (JudgeCreateStoreAuthResponse) object;
                    if (judgeCreateStoreAuthResponse.getCode() == 200) {
                        //1 没有用户店铺标签，跳转创建店铺标签;
//                    2 没有实名认证和申请开店记录;
//                    或开店申请拒绝同时实名认证被拒绝,调用实名认证整合接口;
//                    3 未实名认证或被拒绝,申请开店审核中或通过,跳转实名认证
//                    4 实名认证审核中或通过，未申请开店或申请被拒绝，跳转申请开店;
//                    5 实名认证审核中或通过，开店申请通过或审核中，跳转创建店铺;
//                    6 已开店并且实名认证和开店申请都通过,调用店铺详情

                        int isCreateStore=judgeCreateStoreAuthResponse.getIsCreateStore();
                        long storeId=judgeCreateStoreAuthResponse.getStoreId();
                        switch (isCreateStore){
                            case 1:
                                //用户没有选择开店标签
                                Intent intent1=new Intent(getActivity(),StoreCategoryActivity.class);
                                intent1.putExtra(Constants.Fields.STORE_ID,storeId);
                                startActivity(intent1);
                                break;
                            case 2:
                                //实名 开店两个都没都没申请 或者都拒绝
                                Intent intent2=new Intent(getActivity(),RealNameCertificationAndShopActivity.class);
                                intent2.putExtra(Constants.Fields.STORE_ID,storeId);
                                startActivity(intent2);
                                break;
                            case 3:
                                //跳转实名认证
                                Intent intent3=new Intent(getActivity(),RealNameCertificationActivity.class);
                                intent3.putExtra(Constants.Fields.STORE_ID,storeId);
                                startActivity(intent3);
                                break;
                            case 4:
                                //跳转申请开店界面
                                Intent intent4=new Intent(getActivity(), ApplyForShopActivity.class);
                                intent4.putExtra(Constants.Fields.STORE_ID,storeId);
                                startActivity(intent4);
                                break;
                            case 5:
                                //创建店铺
                                Intent intent5=new Intent(getActivity(),CreateShopActivity.class);
                                startActivity(intent5);
                                break;
                            case 6:
                                //店铺详情
                                Intent intent6=new Intent(getActivity(),ShopDetailActivity.class);
                                intent6.putExtra(Constants.Fields.STORE_ID,storeId);
                                startActivity(intent6);
                                break;
                        }

                    } else {
                        Utils.showToastShortTime(judgeCreateStoreAuthResponse.getMsg());
                    }
                }
                break;
        }
    }

    /**
     * 关注状态改变
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAttentionEvent(AttenttionResultEvent event) {
        if (event != null) {
            if (event.getType() == 1) {
                attentionCount += 1;
            } else if (event.getType() == 2) {
                attentionCount -= 1;
            }
            tv_num_attention.setText(String.valueOf(attentionCount));
        }
    }

    /**
     * 粉丝状态改变
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAttentionMessageEvent(AttentionMessageEvent event) {
        if (event != null) {
            iv_attention.setVisibility(View.VISIBLE);
            getUserPresenter().getUserInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
            /*fansCount += 1;
            tv_num_fans.setText(String.valueOf(fansCount));*/
        }
    }

    /**
     * 昵称改变
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserNameEvent(UserNameEvent event) {
        if (event != null) {
            getUserPresenter().getUserInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
        }
    }

    /**
     * 头像改变
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserIconEvent(UserIconEvent event) {
        if (event != null) {
            userIcon = event.getImgIconUrl();
            RequestOptions options = new RequestOptions().centerCrop();
            Glide.with(this).asBitmap().load(userIcon).apply(options).into(img_icon);
        }
    }

    /**
     * 相册改变
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserAgeEvent(UserAgeEvent event) {
        if (event != null) {
            editInfoNeedUpdata = true;
        }
    }

    /**
     * 充值改变
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAttentionEvent(UpdatePayResultEvent event) {
        if (event != null) {
            getUserPresenter().getUserInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
        }
    }

    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getChildFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_my);
        titleFragment.setRightViewTitle(R.drawable.customer_service, new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                RongIMutils.startToConversation(getActivity(), "1000", "友伴客服");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        OccupationIndexInfo.getInstent().setIndex(-1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (rootView != null) {
            rootView = null;
        }
    }

    @Override
    public void showLoading() {

        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());

    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }
}
