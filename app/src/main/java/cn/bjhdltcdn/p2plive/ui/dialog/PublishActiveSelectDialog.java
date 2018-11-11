//package cn.bjhdltcdn.p2plive.ui.dialog;
//
//import android.content.DialogInterface;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.annotation.IdRes;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHobbyListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetJoinOrganListResponse;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.ActiveJoinOrgainRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.ActivePublishHobbyRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
///**
// * Created by huwnehua on 2017/12/14.
// */
//public class PublishActiveSelectDialog extends DialogFragment implements BaseView {
//
//    private View rootView;
//    private RecyclerView onLineRecycleView, offlineRecycleView, orgainRecycleView;
//    private TextView online_tv, offline_tv, orgain_tv, only_orgain_tv, cancel_tv, ok_tv;
//    private ActivePublishHobbyRecyclerViewAdapter onlineHobbyAdapter;
//    private ActivePublishHobbyRecyclerViewAdapter offlineHobbyAdapter;
//    private AssociationPresenter associationPresenter;
//    private long userId;
//    private List<HobbyInfo> hobbyInfoList;
//    private List<OrganizationInfo> organizationInfoList = new ArrayList<>();
//    private List<OrganizationInfo> organizationInfoList1;
//    private ActiveJoinOrgainRecyclerViewAdapter organListAdapter;
//    private TextView orgainAll_tv;
//    private long orgainId;
//    private RadioGroup sexGroup;
//    private RadioButton allSexRadioButton, boyRadioButton, girlRadioButton;
//    private int selectSex = 0;
//    /**
//     * 1:从一建发布进入
//     * 2：从附近活动
//     * 3:从圈子详情进入
//     */
//    private int comeInType = 2;
//
//    public void setOrgainId(long orgainId) {
//        this.orgainId = orgainId;
//    }
//
//    public void setComeInType(int comeInType) {
//        this.comeInType = comeInType;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//
//        rootView = inflater.inflate(R.layout.dialog_publish_active_select_layout, null);
//        // 触摸内容区域外的需要关闭对话框
//        rootView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (v instanceof ViewGroup) {
//                    View layoutView = ((ViewGroup) v).getChildAt(0);
//                    if (layoutView != null) {
//                        float y = event.getY();
//                        float x = event.getX();
//                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
//                        if (!rect.contains((int) x, (int) y)) {
//                            dismiss();
//                        }
//                        rect.setEmpty();
//                        rect = null;
//                    }
//                }
//                return false;
//            }
//        });
//        initView();
//
//        return rootView;
//    }
//
//    private void initView() {
//        associationPresenter = new AssociationPresenter(this);
//        online_tv = (TextView) rootView.findViewById(R.id.tv_online);
//        offline_tv = (TextView) rootView.findViewById(R.id.tv_offline);
//        orgain_tv = (TextView) rootView.findViewById(R.id.tv_orgain);
//        only_orgain_tv = (TextView) rootView.findViewById(R.id.tv_only_orgain);
//        orgainAll_tv = (TextView) rootView.findViewById(R.id.orgain_all);
//        ok_tv = (TextView) rootView.findViewById(R.id.tv_ok);
//        ok_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        cancel_tv = (TextView) rootView.findViewById(R.id.tv_cancel);
//        cancel_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        onLineRecycleView = (RecyclerView) rootView.findViewById(R.id.online_recycler_view);
//        onlineHobbyAdapter = new ActivePublishHobbyRecyclerViewAdapter(this.getActivity());
//        onLineRecycleView.setHasFixedSize(true);
//        GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 3);
//        onLineRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(15), 3, false));
//        onLineRecycleView.setLayoutManager(layoutManager);
//        onLineRecycleView.setAdapter(onlineHobbyAdapter);
//        onlineHobbyAdapter.setOnSelectClick(new ActivePublishHobbyRecyclerViewAdapter.OnSelectClick() {
//            @Override
//            public void onSelect() {
//                if (onlineHobbyAdapter.getSelectList().size() > 0) {
//                    //线下活动不可用
//                    offlineHobbyAdapter.setEnable(false);
//                    ok_tv.setEnabled(true);
//                    ok_tv.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//                } else {
//                    //线下活动可用
//                    offlineHobbyAdapter.setEnable(true);
//                    ok_tv.setEnabled(false);
//                    ok_tv.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//                }
//            }
//        });
//
//        offlineRecycleView = (RecyclerView) rootView.findViewById(R.id.offline_recycler_view);
//        offlineHobbyAdapter = new ActivePublishHobbyRecyclerViewAdapter(this.getActivity());
//        offlineRecycleView.setHasFixedSize(true);
//        GridLayoutManager layoutManager1 = new GridLayoutManager(App.getInstance(), 3);
//        offlineRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(15), 3, false));
//        offlineRecycleView.setLayoutManager(layoutManager1);
//        offlineRecycleView.setAdapter(offlineHobbyAdapter);
//        offlineHobbyAdapter.setOnSelectClick(new ActivePublishHobbyRecyclerViewAdapter.OnSelectClick() {
//            @Override
//            public void onSelect() {
//                if (offlineHobbyAdapter.getSelectList().size() > 0) {
//                    //线下活动不可用
//                    onlineHobbyAdapter.setEnable(false);
//                    ok_tv.setEnabled(true);
//                    ok_tv.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//                } else {
//                    //线下活动可用
//                    onlineHobbyAdapter.setEnable(true);
//                    ok_tv.setEnabled(false);
//                    ok_tv.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//                }
//            }
//        });
//
//
//        sexGroup = (RadioGroup) rootView.findViewById(R.id.radio_sex);
//        allSexRadioButton = (RadioButton) rootView.findViewById(R.id.radio_all_sex);
//        boyRadioButton = (RadioButton) rootView.findViewById(R.id.radio_boy);
//        girlRadioButton = (RadioButton) rootView.findViewById(R.id.radio_girl);
//        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if (checkedId == allSexRadioButton.getId()) {
//                    selectSex = 0;
//                } else if (checkedId == boyRadioButton.getId()) {
//                    selectSex = 1;
//                } else {
//                    selectSex = 2;
//                }
//            }
//        });
//
//
//        orgainRecycleView = (RecyclerView) rootView.findViewById(R.id.orgain_recycler_view);
//        organListAdapter = new ActiveJoinOrgainRecyclerViewAdapter(this.getActivity());
//        orgainRecycleView.setHasFixedSize(true);
//        GridLayoutManager layoutManager2 = new GridLayoutManager(App.getInstance(), 4);
//        orgainRecycleView.setLayoutManager(layoutManager2);
//        orgainRecycleView.setAdapter(organListAdapter);
//        organListAdapter.setOnSelectClick(new ActiveJoinOrgainRecyclerViewAdapter.OnSelectClick() {
//            @Override
//            public void onSelect() {
//                if (organListAdapter.getSelectList().size() > 0) {
//                    //选中圈子
//                    orgainAll_tv.setBackgroundResource(R.drawable.shape_round_20_stroke_d8d8d8_solid_ffffff);
//                    orgainAll_tv.setTextColor(getResources().getColor(R.color.color_999999));
//                } else {
//                    //选中不限
//                    orgainAll_tv.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);
//                    orgainAll_tv.setTextColor(getResources().getColor(R.color.color_333333));
//                }
//            }
//        });
//        orgainAll_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (organListAdapter.getSelectList().size() > 0) {
//                    orgainAll_tv.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);
//                    orgainAll_tv.setTextColor(getResources().getColor(R.color.color_333333));
//                    organListAdapter.reset();
//                }
//            }
//        });
//        if (orgainId > 0) {
//            //从活动列表进入
//            orgainAll_tv.setBackgroundResource(R.drawable.shape_round_20_stroke_d8d8d8_solid_ffffff);
//            orgainAll_tv.setTextColor(getResources().getColor(R.color.color_999999));
//        } else {
//            //从圈子详情进入
//            orgainAll_tv.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);
//            orgainAll_tv.setTextColor(getResources().getColor(R.color.color_333333));
//        }
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        associationPresenter.getHobbyList(userId, 0, 2, false);
//        associationPresenter.getJoinOrganList(userId, userId, orgainId, 2, 0, 0, false);
//    }
//
//    @Override
//    public void dismiss() {
//        try {
//            super.dismissAllowingStateLoss();
//        } catch (Exception e) {
//            e.printStackTrace();
//            super.dismiss();
//        }
//    }
//
//    public void show(FragmentManager manager) {
//        show(manager, "dialog");
//    }
//
//    @Override
//    public void show(FragmentManager manager, String tag) {
//        try {
//            super.show(manager, tag);
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            // 解决java.lang.IllegalStateException: Can not perform this action问题
//            final android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
//            ft.add(this, tag);
//            ft.commitAllowingStateLoss();
//        }
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (InterfaceUrl.URL_GETHOBBYLIST.equals(apiName)) {
//            if (object instanceof GetHobbyListResponse) {
//                GetHobbyListResponse response = (GetHobbyListResponse) object;
//                if (response.getCode() == 200) {
//                    hobbyInfoList = response.getHobbyList();
//                    //发起活动弹窗
//                    List<HobbyInfo> onlineList = new ArrayList<HobbyInfo>(), offlineList = new ArrayList<HobbyInfo>();
//                    for (int i = 0; i < hobbyInfoList.size(); i++) {
//                        HobbyInfo hobbyInfo = hobbyInfoList.get(i);
//                        if (hobbyInfo.getHobbyType() == 2) {
//                            onlineList.add(hobbyInfo);
//                        } else {
//                            offlineList.add(hobbyInfo);
//                        }
//                    }
//                    onlineHobbyAdapter.setList(onlineList);
//                    onlineHobbyAdapter.notifyDataSetChanged();
//                    offlineHobbyAdapter.setList(offlineList);
//                    offlineHobbyAdapter.notifyDataSetChanged();
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        } else if (InterfaceUrl.URL_GETJOINORGANLIST.equals(apiName)) {
//            if (object instanceof GetJoinOrganListResponse) {
//                GetJoinOrganListResponse response = (GetJoinOrganListResponse) object;
//                if (response.getCode() == 200) {
//                    organizationInfoList1 = response.getList();
//                    for (int i = 0; i < organizationInfoList1.size(); i++) {
//                        OrganizationInfo organizationInfo = organizationInfoList1.get(i);
//                        if (organizationInfo.getType() == 3) {
//                            continue;
//                        }
//                        organizationInfoList.add(organizationInfo);
//                    }
//                    //发起活动弹窗
//                    organListAdapter.setList(organizationInfoList);
//                    organListAdapter.notifyDataSetChanged();
//                    if (organizationInfoList != null && organizationInfoList.size() > 0) {
//                        only_orgain_tv.setVisibility(View.VISIBLE);
//                    } else {
//                        only_orgain_tv.setVisibility(View.GONE);
//                    }
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        }
//    }
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
//    public interface SelectItemListener {
//        void confirmItemClick();
//    }
//}
