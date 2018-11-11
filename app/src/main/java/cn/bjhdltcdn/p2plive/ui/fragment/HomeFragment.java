package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateRefreshTImeEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHomeBannerListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHomeNewCountListResponse;
import cn.bjhdltcdn.p2plive.model.CountInfo;
import cn.bjhdltcdn.p2plive.model.RecommendInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetHomeNearbyPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.BrandShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ChatRoomActivity;
import cn.bjhdltcdn.p2plive.ui.activity.HomeAttentionActivity;
import cn.bjhdltcdn.p2plive.ui.activity.HomeSearchActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PublishActivity;
import cn.bjhdltcdn.p2plive.ui.activity.StoreActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeTabRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.PublishSuccessLabelFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * Created by Hu_PC on 2017/11/8.
 * 首页
 */

public class HomeFragment extends BaseFragment implements BaseView {
    private View rootView;
    private AppCompatActivity mActivity;
    private EditText searchEditText;
    private GetHomeNearbyPresenter presenter;
    private GetRecommendListPresenter getRecommendListPresenter;
    private RecyclerView tabRecycleView;
    private HomeTabRecyclerViewAdapter homeTabRecyclerViewAdapter;
    private Runnable successLabelFragmentDialogRunnable;
    private PublishSuccessLabelFragmentDialog successLabelFragmentDialog;
    private long userId;
    private int currentPosition;
    private boolean forward = true;

    private GetHomeNearbyPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GetHomeNearbyPresenter(this);
        }
        return presenter;
    }

    public GetRecommendListPresenter getGetRecommendListPresenter() {
        if (getRecommendListPresenter == null) {
            getRecommendListPresenter = new GetRecommendListPresenter(this);
        }
        return getRecommendListPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        initView();
    }

    @Override
    protected void onVisible(boolean isInit) {
        getPresenter().getHomeNewCountList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
    }

    private void initView() {
        ImageView publishView = rootView.findViewById(R.id.iv_public);
        publishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一键发布界面
                Intent intent = new Intent(getActivity(), PublishActivity.class);
                startActivity(intent);
            }
        });

        searchEditText = rootView.findViewById(R.id.search_edittext);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到搜索界面
                startActivity(new Intent(mActivity, HomeSearchActivity.class));
            }
        });
        homeTabRecyclerViewAdapter = new HomeTabRecyclerViewAdapter(mActivity);
        tabRecycleView = (RecyclerView) rootView.findViewById(R.id.home_tab_recycler_view);
        tabRecycleView.setHasFixedSize(true);

        tabRecycleView.setAdapter(homeTabRecyclerViewAdapter);
        homeTabRecyclerViewAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentPosition = position;
                CountInfo countInfo = homeTabRecyclerViewAdapter.getItem(position);
                int type = countInfo.getType();//(0推荐1线下活动,2在玩儿啥(圈子),3表白邂逅,4帮帮忙,5关注,6发现,7商圈,8聊天室),
                if (forward == false) {
                    return;
                }
                switch (type) {
                    case 0:
                        //推荐
                        break;
                    case 1:
                        //活动
                        break;
                    case 2:
                        //附近圈子
                        break;
                    case 3:
                        //表白
                        break;
                    case 4:
                        //帮帮忙
                        break;
                    case 5:
                        //关注
                        startActivity(new Intent(mActivity, HomeAttentionActivity.class));
                        break;
                    case 6:
                        //视频
                        break;
                    case 7:
                        //商圈
                        String gotoUrl = countInfo.getDescription();
                        if (!TextUtils.isEmpty(gotoUrl)) {
                            Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
                            intent.putExtra(Constants.Action.ACTION_EXTRA, 6);
                            intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + userId);
                            startActivity(intent);
                        }

//                        String gotoUrl = "http://39.105.98.17:8090/h5-project/view/lottery/index.html#/product-list";
//                        Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
//                        intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + userId);
//                        startActivity(intent);

//                        getGetRecommendListPresenter().getHomeBannerList(userId, 4);
//                        mActivity.startActivity(new Intent(mActivity, ShopActivity.class));
//                        mActivity.startActivity(new Intent(mActivity, BusinessDistrictActivity.class));

                        break;
                    case 8:
                        //聊天室
                        mActivity.startActivity(new Intent(mActivity, ChatRoomActivity.class));
//                        gotoUrl = "http://39.105.98.17:8090/h5-project/view/lottery/index.html#/";
//                        intent = new Intent(getContext(), WXPayEntryActivity.class);
//                        intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + userId);
//                        startActivity(intent);
                        break;
                    case 9:
                        mActivity.startActivity(new Intent(mActivity, StoreActivity.class));
                        break;
                    case 10:
                        mActivity.startActivity(new Intent(mActivity, BrandShopActivity.class));
                        //品牌商
                        break;
                    default:
                        break;

                }
                //更新刷新时间
                forward = false;
                getPresenter().updateRefreshTime(userId, type);
            }
        });

        HomeRecommendFragment mFragment = new HomeRecommendFragment();
        ActivityUtils.addFragmentToActivity(mActivity.getSupportFragmentManager(), mFragment, R.id.video_frame);

    }


    @Override
    public void updateView(String apiName, Object object) {
        if (InterfaceUrl.URL_GETHOMENEWCOUNTLIST.equals(apiName)) {
            if (object instanceof GetHomeNewCountListResponse) {
                GetHomeNewCountListResponse response = (GetHomeNewCountListResponse) object;
                if (response.getCode() == 200) {
                    List<CountInfo> countInfoList = response.getList();
                    if (countInfoList != null && countInfoList.size() > 0) {
                        if (countInfoList.size() > 4) {
                            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.HORIZONTAL, false);
                            tabRecycleView.setLayoutManager(linearlayoutManager);
                        } else {
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), countInfoList.size());
                            tabRecycleView.setLayoutManager(gridLayoutManager);
                        }
                        homeTabRecyclerViewAdapter.setCountInfoList(countInfoList);
                        homeTabRecyclerViewAdapter.notifyDataSetChanged();
                    }

                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }

        } else if (apiName.equals(InterfaceUrl.URL_UPDATEREFRESHTIME)) {
            forward = true;
            if (object instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) object;
                if (baseResponse.getCode() == 200) {
                    List<CountInfo> countInfolist = homeTabRecyclerViewAdapter.getCountInfoList();
                    if (countInfolist != null && countInfolist.size() > 0) {
                        countInfolist.get(currentPosition).setUpdateCount(0);
                    }
                    homeTabRecyclerViewAdapter.setCountInfoList(countInfolist);
                    homeTabRecyclerViewAdapter.notifyItemChanged(currentPosition);
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_GETHOMEBANNERLIST)) {
            if (object instanceof GetHomeBannerListResponse) {
                GetHomeBannerListResponse getHomeBannerListResponse = (GetHomeBannerListResponse) object;
                if (getHomeBannerListResponse.getCode() == 200) {
                    List<RecommendInfo> recommendInfoList = getHomeBannerListResponse.getList();
                    if (recommendInfoList != null && recommendInfoList.size() > 0) {
                        RecommendInfo recommendInfo = recommendInfoList.get(0);
                        if (recommendInfo != null) {
                            String gotoUrl = recommendInfo.getGotoUrl();
                            if (!TextUtils.isEmpty(gotoUrl)) {
                                Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
                                intent.putExtra(Constants.Action.ACTION_EXTRA, 6);
                                intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + userId);
                                startActivity(intent);
                            }
                        }

                    }
                } else {
                    Utils.showToastShortTime(getHomeBannerListResponse.getMsg());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateRefreshTImeEvent event) {
        if (event == null) {
            return;
        }
        forward = true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (rootView != null && successLabelFragmentDialogRunnable != null) {
            rootView.removeCallbacks(successLabelFragmentDialogRunnable);
        }

    }


}
