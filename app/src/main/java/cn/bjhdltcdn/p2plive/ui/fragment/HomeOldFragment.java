//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.orhanobut.logger.Logger;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.PublishSuccessLabelFragmentDialogEvent;
//import cn.bjhdltcdn.p2plive.event.UpdateHomeNewEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHomeNewCountResponse;
//import cn.bjhdltcdn.p2plive.model.LabelInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetHomeNearbyPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.HomeSearchActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PublishActivity;
//import cn.bjhdltcdn.p2plive.ui.adapter.HomeTabFragmentAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.PublishSuccessLabelFragmentDialog;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
//
///**
// * Created by Hu_PC on 2017/11/8.
// * 首页
// */
//
//public class HomeOldFragment extends BaseFragment implements BaseView {
//    private View rootView;
//    private AppCompatActivity mActivity;
//    private CustomViewPager mViewPager;
//    private TabLayout mTabLayout;
//    private HomeTabFragmentAdapter adapter;
//    private EditText searchEditText;
//    private GetHomeNearbyPresenter presenter;
//    private View attentionNewView, nearNewView;
//    private ImageView publishView;
//
//    private Runnable successLabelFragmentDialogRunnable;
//    private PublishSuccessLabelFragmentDialog successLabelFragmentDialog;
//
//    private GetHomeNearbyPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new GetHomeNearbyPresenter(this);
//        }
//        return presenter;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mActivity = (AppCompatActivity) context;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_home_old, null);
//        }
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView();
//    }
//
//    @Override
//    protected void onVisible(boolean isInit) {
//        getPresenter().getHomeNewCount(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//    }
//
//    private void initView() {
//        searchEditText = rootView.findViewById(R.id.search_edittext);
//        searchEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到搜索界面
//                startActivity(new Intent(mActivity, HomeSearchActivity.class));
//            }
//        });
//        mViewPager = (CustomViewPager) rootView.findViewById(R.id.home_view_page);
//        mViewPager.setIsCanScroll(true);
//        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
//
//        adapter = new HomeTabFragmentAdapter(mActivity.getSupportFragmentManager());
//
//        HomeAttentionFragment homeAttentionFragment = new HomeAttentionFragment();
//        adapter.addFragment(homeAttentionFragment, "关注");
//
//        HomeRecommendFragment homeRecommendFragment = new HomeRecommendFragment();
//        adapter.addFragment(homeRecommendFragment, "推荐");
//
//
//        HomeNearByFragment homeNearByFragment = new HomeNearByFragment();
//        adapter.addFragment(homeNearByFragment, "频道");
//
//
//        mViewPager.setOffscreenPageLimit(3);
//        mViewPager.setAdapter(adapter);
//        mTabLayout.setupWithViewPager(mViewPager);
//        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = mTabLayout.getTabAt(i);
//            View tabView = adapter.getTabView(i);
//            tab.setCustomView(tabView);
//            if (i == 0) {
//                attentionNewView = tabView.findViewById(R.id.tab_home_pop_view);
//            } else if (i == 2) {
//                nearNewView = tabView.findViewById(R.id.tab_home_pop_view);
//            }
//
//        }
//        mViewPager.setCurrentItem(1);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
//                textView.setTextColor(getResources().getColor(R.color.color_333333));
//
//                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
//                tabIndicatorView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
//
//                mViewPager.setCurrentItem(tab.getPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
//                textView.setTextColor(getResources().getColor(R.color.color_999999));
//
//                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
//                tabIndicatorView.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
////                KLog.d("onTabReselected()");
//            }
//        });
//
//        publishView = rootView.findViewById(R.id.publish_view);
//        publishView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //一键发布界面
//                Intent intent = new Intent(getActivity(), PublishActivity.class);
//                intent.putExtra(Constants.Fields.TYPE, 1);
//                startActivity(intent);
//            }
//        });
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (InterfaceUrl.URL_GETHOMENEWCOUNT.equals(apiName)) {
//            if (object instanceof GetHomeNewCountResponse) {
//                GetHomeNewCountResponse response = (GetHomeNewCountResponse) object;
//                if (response.getCode() == 200) {
//                    if (response.getNearCount() > 0) {
//                        //展示附近小红点
//                        nearNewView.setVisibility(View.VISIBLE);
//                    }
//                    if (response.getAttentionCount() > 0) {
//                        //展示关注小红点
//                        attentionNewView.setVisibility(View.VISIBLE);
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(UpdateHomeNewEvent event) {
//        if (event == null) {
//            return;
//        }
//        if (event.getType() == 1) {
//            //清除关注小红点
//            attentionNewView.setVisibility(View.GONE);
//        } else if (event.getType() == 2) {
//            //清除附近小红点
//            nearNewView.setVisibility(View.GONE);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(PublishSuccessLabelFragmentDialogEvent event) {
//
//        try {
//            List<LabelInfo> labelInfoList = new ArrayList<>(1);
//
//
//            if (event.getLabelId() > 0) {
//                LabelInfo labelInfo3 = new LabelInfo();
//                labelInfo3.setIsSelect(1);
//                labelInfo3.setLabelName("赛事专区");
//                labelInfoList.add(0, labelInfo3);
//            }
//
//
//            LabelInfo labelInfo = new LabelInfo();
//            labelInfo.setIsSelect(1);
//            labelInfo.setLabelName("学校校友录");
//            labelInfoList.add(labelInfo);
//
//            // 发布成功结果提示框
//            successLabelFragmentDialog = new PublishSuccessLabelFragmentDialog();
//
//            Logger.d("event.getList() === " + event.getList());
//
//            if (event.getList() != null) {
//                for (int i = 0; i < event.getList().size(); i++) {
//                    LabelInfo labelInfo2 = new LabelInfo();
//                    labelInfo2.setIsSelect(1);
//                    labelInfo2.setLabelName(event.getList().get(i).getOrganName());
//                    labelInfoList.add(labelInfo2);
//                }
//            }
//
//
//            successLabelFragmentDialog.setLabelInfoList(labelInfoList);
//            successLabelFragmentDialog.show(getChildFragmentManager());
//
//
//            successLabelFragmentDialogRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    if (successLabelFragmentDialog != null && successLabelFragmentDialog.isAdded()) {
//                        successLabelFragmentDialog.dismiss();
//                    }
//                }
//            };
//
//            rootView.postDelayed(successLabelFragmentDialogRunnable,5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    @Override
//    public void showLoading() {
//
//    }
//
//    @Override
//    public void hideLoading() {
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//
//        if (rootView != null && successLabelFragmentDialogRunnable != null) {
//            rootView.removeCallbacks(successLabelFragmentDialogRunnable);
//        }
//
//    }
//
//
//}
