//package cn.bjhdltcdn.p2plive.ui.dialog;
//
//import android.content.DialogInterface;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.style.ForegroundColorSpan;
//import android.util.ArrayMap;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.orhanobut.logger.Logger;
//
//import java.util.List;
//import java.util.Map;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSecondLabelListResponse;
//import cn.bjhdltcdn.p2plive.model.LabelInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.RoomLabelTabsRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
///**
// * 发布页面的二级标签选择框
// */
//public class PublishSecondLabelFragmentDialog extends DialogFragment implements BaseView, DialogInterface.OnKeyListener {
//
//    private View rootView;
//
//    private ProgressBar progressBar;
//    private TextView titleLabelView;
//    private DiscoverPresenter discoverPresenter;
//
//    private Map<Long, LabelInfo> labelInfoMap;
//
////    private long labelId;
//
////    private int selectNum;//（一级标签返回）选择的个数
//
//    private LabelInfo labelInfo;
//    private RoomLabelTabsRecyclerViewAdapter labelAdapter;
//
////    public void setList(long labelId, int selectNum) {
////        this.labelId = labelId;
////        this.selectNum = selectNum;
////    }
//
//
//    public void setLabelInfo(LabelInfo labelInfo) {
//        this.labelInfo = labelInfo;
//    }
//
//    private OnItemClickListener itemClickListener;
//
//    public void setItemClickListener(OnItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }
//
//    public DiscoverPresenter getDiscoverPresenter() {
//        if (discoverPresenter == null) {
//            discoverPresenter = new DiscoverPresenter(this);
//        }
//        return discoverPresenter;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.color_cc333333_dialog);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.fragment_publish_second_label_fragment_dialog_layout, null);
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
//
//        getDialog().setOnKeyListener(this);
//
//        return rootView;
//    }
//
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        // 关闭按钮
//        rootView.findViewById(R.id.delete_view).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        titleLabelView = rootView.findViewById(R.id.tv_label_title);
//        titleLabelView.setText(labelInfo.getLabelName());
//        progressBar = rootView.findViewById(R.id.progress_loading);
//
//        getDiscoverPresenter().getSecondLabelList(labelInfo.getLabelId());
//
//
//    }
//
//    @Override
//    public void dismiss() {
//        try {
//
//            if (itemClickListener != null) {
//                itemClickListener.onItemClick(labelInfoMap);
//            }
//
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
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 解决java.lang.IllegalStateException: Can not perform this action问题
//            final FragmentTransaction ft = manager.beginTransaction();
//            ft.add(this, tag);
//            ft.commitAllowingStateLoss();
//        }
//    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//        if (discoverPresenter != null) {
//            discoverPresenter.onDestroy();
//        }
//        discoverPresenter = null;
//
//        if (itemClickListener != null) {
//            itemClickListener = null;
//        }
//
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (!isAdded()) {
//            return;
//        }
//
//        if (InterfaceUrl.URL_GETSECONDLABELLIST.equals(apiName)) {
//            if (object instanceof GetSecondLabelListResponse) {
//                GetSecondLabelListResponse response = (GetSecondLabelListResponse) object;
//                if (response.getCode() == 200) {
//
//
//                    SpannableStringBuilder style = new SpannableStringBuilder();
//
//                    if (!StringUtils.isEmpty(response.getContent1())) {
//                        style.append(response.getContent());
//                        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_999999)), 0, response.getContent().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    }
//
//                    TextView tipTextView2 = rootView.findViewById(R.id.tip_text_view_2);
//                    tipTextView2.setText(style);
//
//                    //二级标签
//                    RecyclerView labelRecyclerView = rootView.findViewById(R.id.recycler_label);
//                    labelRecyclerView.setVisibility(View.VISIBLE);
//                    labelRecyclerView.setHasFixedSize(true);
////                    FlowLayoutManager layoutManager = new FlowLayoutManager(getActivity());
////                    layoutManager.setAutoMeasureEnabled(true);
////                    labelRecyclerView.setLayoutManager(layoutManager);
////                    layoutManager.setMargin(Utils.dp2px(5));
//
//                    GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 2);
////                    layoutManager.setAutoMeasureEnabled(true);
//                    labelRecyclerView.setLayoutManager(layoutManager);
//                    labelRecyclerView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(5), 2, false));
//                    labelRecyclerView.setNestedScrollingEnabled(false);
//
//                    labelAdapter = new RoomLabelTabsRecyclerViewAdapter(6);
//
//                    // 已经被选中的需要显示出来
//                    List<LabelInfo> labelList = response.getLabelList();
//                    labelAdapter.setList(labelList);
//                    labelRecyclerView.setAdapter(labelAdapter);
//
//                    labelAdapter.setOnClickListener(new RoomLabelTabsRecyclerViewAdapter.OnClickListener() {
//                        @Override
//                        public void onClick(int position) {
//
//                            LabelInfo labelInfo = labelAdapter.getItem(position);
//                            int selectItemCount = labelAdapter.getSelectItemCount();
//
//                            Logger.d("selectItemCount == " + selectItemCount + " ===getIsSelect()=== " + labelInfo.getIsSelect());
//                            int selectNum = PublishSecondLabelFragmentDialog.this.labelInfo.getSelectNum();
//                            if (selectItemCount > selectNum) {
//
//                                Utils.showToastShortTime("最多可选" + selectNum + "个标签");
//                                labelInfo.setIsSelect(0);
//                                labelAdapter.notifyItemChanged(position);
//                                return;
//
//                            }
//
//                            if (labelInfoMap == null) {
//                                labelInfoMap = new ArrayMap<>(1);
//                            }
//
//                            if (labelInfo.getIsSelect() == 1) {
//                                labelInfoMap.put(labelInfo.getLabelId(), labelInfo);
//                            } else {
//                                labelInfoMap.remove(labelInfo.getLabelId());
//                            }
//
//
//                        }
//                    });
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public void showLoading() {
//
//        if (progressBar != null) {
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//    @Override
//    public void hideLoading() {
//        if (progressBar != null) {
//            progressBar.setVisibility(View.GONE);
//        }
//    }
//
//    /**
//     * 监听返回键事件
//     * @param dialog
//     * @param keyCode
//     * @param event
//     * @return
//     */
//    @Override
//    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            dismiss();
//            return true;
//        } else {
//            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
//            return false;
//        }
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(Map<Long, LabelInfo> labelInfoMap);
//    }
//}
