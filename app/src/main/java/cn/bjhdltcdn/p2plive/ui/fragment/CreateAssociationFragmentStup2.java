//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import org.greenrobot.eventbus.EventBus;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.CreateAssociationEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHobbyListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSecondTypeListResponse;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.CreateAssociationActivity;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationTypeLeftAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationTypeRightAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.TypeAddFragmentDialog;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class CreateAssociationFragmentStup2 extends Fragment implements BaseView {
//
//
//    private View rootView;
//
//    private AssociationPresenter presenter;
//
//    private AssociationTypeLeftAdapter leftAdapter;
//    private RecyclerView recycleView;
//
//    private RecyclerView rightRecycleView;
//    private AssociationTypeRightAdapter rightAdapter;
//
//
//    private int selectIndex = -1;
//    private int selectRightIndex = -1;
//
//    private Button nextBtnView;
//
//    private TextView tipsTextView;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.fragment_create_association_stup2_layout, container, false);
//
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//
//        return rootView;
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        setBtnVisibility(true);
//
//        recycleView = rootView.findViewById(R.id.left_recycle_view);
//        recycleView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        layoutManager.setAutoMeasureEnabled(true);
//        recycleView.setLayoutManager(layoutManager);
//        recycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10), false));
//
//        leftAdapter = new AssociationTypeLeftAdapter(null);
//        recycleView.setAdapter(leftAdapter);
//
//        leftAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                selectRightIndex = -1;
//
//                if (selectIndex == position) {
//                    return;
//                }
//
//                if (selectIndex > -1) {
//                    // 重置上一次选中的效果
//                    HobbyInfo selectHobbyInfo = leftAdapter.getList().get(selectIndex);
//                    selectHobbyInfo.setLocalSelectItem(false);
//                    leftAdapter.notifyItemChanged(selectIndex);
//                }
//
//                // 执行选中效果
//                HobbyInfo hobbyInfo = leftAdapter.getList().get(position);
//                hobbyInfo.setLocalSelectItem(true);
//                leftAdapter.notifyItemChanged(position);
//
//                selectIndex = position;
//
//                // 保存一级标签id
//                ((CreateAssociationActivity) getActivity()).setFirstHobbyInfo(hobbyInfo);
//
//                getPresenter().getSecondTypeList(hobbyInfo.getHobbyId());
//
//                tipsTextView.setText(hobbyInfo.getHobbyName());
//
//            }
//        });
//
//        ((CreateAssociationActivity) getActivity()).setSecondHobbyInfo(null);
//        ((CreateAssociationActivity) getActivity()).setCustomName(null);
//
//
//        // 显示文案
//        tipsTextView = rootView.findViewById(R.id.text_view);
//
//
//        rightRecycleView = rootView.findViewById(R.id.right_recycle_view);
//        rightRecycleView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(App.getInstance());
//        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager2.setAutoMeasureEnabled(true);
//        rightRecycleView.setLayoutManager(layoutManager2);
//
//        rightRecycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1)));
//
//        rightAdapter = new AssociationTypeRightAdapter(null);
//        rightRecycleView.setAdapter(rightAdapter);
//        rightAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, final int position) {
//
//
//                if (selectRightIndex == position) {
//                    return;
//                }
//
//                if (selectRightIndex > -1) {
//                    // 重置上一次选中的效果
//                    HobbyInfo selectHobbyInfo = rightAdapter.getList().get(selectRightIndex);
//                    selectHobbyInfo.setLocalSelectItem(false);
//                    rightAdapter.notifyItemChanged(selectRightIndex);
//
//                    nextBtnView.setEnabled(false);
//                    nextBtnView.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//
//                }
//
//                if (position == 0) {// 点击最前一个item
//                    final HobbyInfo hobbyInfo = rightAdapter.getList().get(position);
//                    if (hobbyInfo.getHobbyId() < 0) {
//
//                        TypeAddFragmentDialog typeAddFragmentDialog = new TypeAddFragmentDialog();
//                        typeAddFragmentDialog.setClickListener(new TypeAddFragmentDialog.ClickListener() {
//                            @Override
//                            public void onClick(Object object) {
//
//                                if (object == null) {
//                                    return;
//                                }
//
//                                if (object instanceof String) {
//                                    // 保存二级标签文字
//                                    ((CreateAssociationActivity) getActivity()).setCustomName((String) object);
//
//                                } else if (object instanceof HobbyInfo) {
//                                    HobbyInfo hobbyInfo2 = (HobbyInfo) object;
//                                    if (hobbyInfo2.getHobbyId() > 0) {
//                                        // 保存二级标签
//                                        ((CreateAssociationActivity) getActivity()).setSecondHobbyInfo(hobbyInfo2);
//                                    } else {
//                                        ((CreateAssociationActivity) getActivity()).setCustomName(hobbyInfo2.getHobbyName());
//                                    }
//
//                                }
//
//                                if (!StringUtils.isEmpty(((CreateAssociationActivity) getActivity()).getCustomName())) {
//                                    EventBus.getDefault().post(new CreateAssociationEvent(2));
//                                } else if (((CreateAssociationActivity) getActivity()).getSecondHobbyInfo() != null) {
//                                    EventBus.getDefault().post(new CreateAssociationEvent(2));
//                                } else {
//                                    Utils.showToastShortTime("请选择类别");
//                                }
//
//                            }
//                        });
//
//                        typeAddFragmentDialog.show(getChildFragmentManager());
//
//                        return;
//                    }
//
//                }
//
//
//                // 执行选中效果
//                HobbyInfo hobbyInfo2 = rightAdapter.getList().get(position);
//
//
//                hobbyInfo2.setLocalSelectItem(true);
//                rightAdapter.notifyItemChanged(position);
//
//                selectRightIndex = position;
//
//                // 保存二级标签
//                ((CreateAssociationActivity) getActivity()).setSecondHobbyInfo(hobbyInfo2);
//
//                nextBtnView.setEnabled(true);
//                nextBtnView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//
//            }
//        });
//
//
//        nextBtnView = rootView.findViewById(R.id.stup2_next_btn_view);
//        nextBtnView.setEnabled(false);
//        nextBtnView.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//        nextBtnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String customName = ((CreateAssociationActivity) getActivity()).getCustomName();
//                if (selectIndex > -1 && !StringUtils.isEmpty(customName)) {
//                    EventBus.getDefault().post(new CreateAssociationEvent(2));
//                } else if (selectIndex > -1 && selectRightIndex > -1) {
//                    EventBus.getDefault().post(new CreateAssociationEvent(2));
//                } else {
//                    Utils.showToastShortTime("请选择类别");
//                }
//
//
//            }
//        });
//
//        // 获取一级标签
//        getPresenter().getHobbyList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 0, 1,false);
//
//    }
//
//    public AssociationPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//        return presenter;
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (!isAdded()) {
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
//        if (InterfaceUrl.URL_GETHOBBYLIST.equals(apiName)) {
//
//            if (object instanceof GetHobbyListResponse) {
//                GetHobbyListResponse response = (GetHobbyListResponse) object;
//                if (response.getCode() == 200) {
//                    leftAdapter.setList(response.getHobbyList());
//
//                    if (leftAdapter.getItemCount() > 0) {
//                        HobbyInfo hobbyInfo = leftAdapter.getList().get(0);
//                        // 执行选中效果
//                        hobbyInfo.setLocalSelectItem(true);
//                        leftAdapter.notifyItemChanged(0);
//
//                        selectIndex = 0;
//
//                        selectRightIndex = -1;
//
//                        // 保存一级标签id
//                        ((CreateAssociationActivity) getActivity()).setFirstHobbyInfo(hobbyInfo);
//
//                        getPresenter().getSecondTypeList(hobbyInfo.getHobbyId());
//
//                        tipsTextView.setText(hobbyInfo.getHobbyName());
//
//                    }
//
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        } else if (InterfaceUrl.URL_GETSECONDHOBBYLIST.equals(apiName)) {
//            if (object instanceof GetSecondTypeListResponse) {
//                GetSecondTypeListResponse response = (GetSecondTypeListResponse) object;
//
//                if (response.getCode() == 200) {
//                    HobbyInfo hobbyInfo = new HobbyInfo();
//                    hobbyInfo.setHobbyId(-1);
//                    response.getHobbyList().add(0,hobbyInfo);
//                    rightAdapter.setList(response.getHobbyList());
//
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
//        rootView.findViewById(R.id.stup2_next_btn_view).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//
//        if (presenter != null) {
//            presenter = null;
//        }
//
//        if (leftAdapter != null) {
//            leftAdapter = null;
//        }
//
//        if (rightAdapter != null) {
//            rightAdapter = null;
//        }
//
//        if (recycleView != null) {
//            recycleView = null;
//        }
//
//        if (rightRecycleView != null) {
//            rightRecycleView = null;
//        }
//
//    }
//
//
//}
