//package cn.bjhdltcdn.p2plive.ui.dialog;
//
//
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.orhanobut.logger.Logger;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSecondTypeBySearchListResponse;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationTypeListItemAdapter;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.tagview.TagContainerLayout;
//import cn.bjhdltcdn.p2plive.widget.tagview.TagView;
//
///**
// * 类型自定义对话框
// */
//public class TypeAddFragmentDialog extends DialogFragment implements BaseView {
//
//
//    private View rootView;
//    private ClickListener clickListener;
//    private TextView textView2;
//    private TextView textView1;
//    private TextView textView4;
//    private RecyclerView recycleView;
//    private AssociationTypeListItemAdapter adapter;
//    private int selectPosition = -1;
//    private AssociationPresenter presenter;
//    private EditText editText;
//    private TagContainerLayout tagContainerLayout1;
//    private TagContainerLayout tagContainerLayout;
//
//    private List<HobbyInfo> hobbyList;
//
//    /**
//     * 被选中的标签
//     */
//    private HobbyInfo hobbyInfo;
//    private View deleteView;
//
//    public AssociationPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//        return presenter;
//    }
//
//
//    public ClickListener getClickListener() {
//        return clickListener;
//    }
//
//    public void setClickListener(ClickListener clickListener) {
//        this.clickListener = clickListener;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.color_cc333333_dialog);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.fragment_type_add_fragment_dialog, container, false);
//
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//
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
//        return rootView;
//
//
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        final TextView commitView = rootView.findViewById(R.id.btn_commit);
//        // 完成按钮
//        commitView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if ("搜索".equals(commitView.getText().toString())) {
//                    String trim = editText.getText().toString().trim();
//                    if (TextUtils.isEmpty(trim)) {
//                        Utils.showToastShortTime("请输入自建圈子的标签");
//                    } else {
//                        commitView.setText("创建");
//                        searchTags();
//                    }
//                } else if ("创建".equals(commitView.getText().toString())) {
//                    if (clickListener != null) {
//                        clickListener.onClick(hobbyInfo);
//                        dismiss();
//                    }
//                }
//
//            }
//        });
//
//        editText = rootView.findViewById(R.id.edit_view);
//        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        //必须设置setSingleLine(true)或者android:inputType="text"，否则搜索设置不生效
//        editText.setSingleLine(true);
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch (actionId) {
//                    case EditorInfo.IME_ACTION_DONE:
//                        Logger.e("IME_ACTION_DONE");
//
//                        if ("搜索".equals(commitView.getText().toString())) {
//                            commitView.setText("创建");
//                        }
//                        searchTags();
//
//                        return true;
//
//                    case EditorInfo.IME_ACTION_GO:
//                        Logger.e("IME_ACTION_GO");
//
//                        break;
//                    case EditorInfo.IME_ACTION_NEXT:
//                        Logger.e("IME_ACTION_NEXT");
//                        break;
//                    case EditorInfo.IME_ACTION_NONE:
//                        Logger.e("IME_ACTION_NONE");
//                        break;
//                    case EditorInfo.IME_ACTION_PREVIOUS:
//                        Logger.e("IME_ACTION_PREVIOUS");
//                        break;
//                    case EditorInfo.IME_ACTION_SEARCH:
//                        Logger.e("IME_ACTION_SEARCH");
//                        break;
//                    case EditorInfo.IME_ACTION_SEND:
//                        Logger.e("IME_ACTION_SEND");
//                        break;
//                    case EditorInfo.IME_ACTION_UNSPECIFIED:
//                        Logger.e("IME_ACTION_UNSPECIFIED");
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
//
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s == null || s.length() == 0) {
//                    if (tagContainerLayout1.getChildCount() > 0) {
//                        tagContainerLayout1.removeAllViews();
//                    }
//                    if (tagContainerLayout.getChildCount() > 0) {
//                        tagContainerLayout.removeAllViews();
//                    }
//                    if (adapter.getItemCount() > 0) {
//                        adapter.setList(new ArrayList<OrganizationInfo>(1));
//                    }
//                    textView1.setVisibility(View.GONE);
//                    textView2.setVisibility(View.GONE);
//                    textView4.setVisibility(View.GONE);
//                    tagContainerLayout.setVisibility(View.GONE);
//                    tagContainerLayout1.setVisibility(View.GONE);
//                    recycleView.setVisibility(View.GONE);
//                    commitView.setText("搜索");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        deleteView = rootView.findViewById(R.id.delete_view);
//        deleteView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editText.setText("");
//            }
//        });
//
//
//        textView1 = rootView.findViewById(R.id.text_view_1);
//
//
//        textView2 = rootView.findViewById(R.id.text_view_2);
//        tagContainerLayout1 = rootView.findViewById(R.id.tag_container_view_1);
//        tagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
//            @Override
//            public void onTagClick(int position, String text) {
//
//                setSelectStatusView(position, 1);
//
//            }
//
//            @Override
//            public void onTagLongClick(int position, String text) {
//
//            }
//
//            @Override
//            public void onTagCrossClick(int position) {
//
//            }
//        });
//
//        tagContainerLayout = rootView.findViewById(R.id.tag_container_view);
//        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
//            @Override
//            public void onTagClick(int position, String text) {
//
//                setSelectStatusView(position, 2);
//
//            }
//
//            @Override
//            public void onTagLongClick(int position, String text) {
//
//            }
//
//            @Override
//            public void onTagCrossClick(int position) {
//
//            }
//        });
//
//        textView4 = rootView.findViewById(R.id.text_view_4);
//
//        recycleView = rootView.findViewById(R.id.recycle_view);
//        recycleView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(App.getInstance());
//        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager2.setAutoMeasureEnabled(true);
//        recycleView.setLayoutManager(layoutManager2);
//        recycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1)));
//        recycleView.setNestedScrollingEnabled(false);
//
//        adapter = new AssociationTypeListItemAdapter();
//        recycleView.setAdapter(adapter);
//        adapter.setBtnClickListener(new AssociationTypeListItemAdapter.BtnClickListener() {
//            @Override
//            public void onClick(final OrganizationInfo organizationInfo, final int position) {
//
//                //入圈验证(1-->直接加入,2-->申请同意后可加入),
//                int joinLimit = organizationInfo.getJoinLimit();
//
//                if (joinLimit == 2) {
//                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                    dialog.setText("", "本圈子为私密圈子，需要先发送申请，管理员同意后才能加入，发送？", "取消", "发送");
//                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            //取消
//                        }
//
//                        @Override
//                        public void onRightClick() {
//                            //申请
//                            selectPosition = position;
//                            getPresenter().joinOrganization(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getInt(Constants.Fields.USER_ID, 0), 2);
//                        }
//                    });
//                    dialog.show(getChildFragmentManager());
//                } else {
//                    selectPosition = position;
//                    getPresenter().joinOrganization(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getInt(Constants.Fields.USER_ID, 0), 1);
//                }
//
//            }
//        });
//
//        adapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
//
//    }
//
//    /**
//     * 根据输入内容搜索标签
//     */
//    private void searchTags() {
//        String text = editText.getText().toString().trim();
//        if (!StringUtils.isEmpty(text)) {
//            getPresenter().getSecondTypeBySearchList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), text);
//
//            textView1.setText("您要创建的圈子标签:");
//            textView1.setVisibility(View.VISIBLE);
//            ArrayList searchWords = new ArrayList(1);
//            HobbyInfo hobbyInfo = new HobbyInfo();
//            hobbyInfo.setHobbyId(-1);
//            hobbyInfo.setHobbyName(text);
//            searchWords.add(hobbyInfo);
//            tagContainerLayout1.setVisibility(View.VISIBLE);
//            tagContainerLayout1.setTags(searchWords);
//
//            setSelectStatusView(0, 1);
//
//        }
//
//        KeyBoardUtils.closeKeybord(editText, App.getInstance());
//    }
//
//    /**
//     * 更改状态
//     *
//     * @param selectPosition 选择的item
//     * @param type           1 搜索关键字tag ； 2 服务器tag
//     */
//    private void setSelectStatusView(int selectPosition, int type) {
//
//        int childCount = tagContainerLayout1.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            TagView tagView = (TagView) tagContainerLayout1.getChildAt(i);
//            tagView.setTagBackgroundColor(getResources().getColor(R.color.color_fafafa));
//            tagView.invalidate();
//        }
//
//        childCount = tagContainerLayout.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            TagView tagView = (TagView) tagContainerLayout.getChildAt(i);
//            tagView.setTagBackgroundColor(getResources().getColor(R.color.color_fafafa));
//            tagView.invalidate();
//        }
//
//        TagView tagView = null;
//        if (type == 1) {
//            tagView = (TagView) tagContainerLayout1.getChildAt(selectPosition);
//            Object object = tagContainerLayout1.getTags().get(selectPosition);
//            if (object instanceof HobbyInfo) {
//                hobbyInfo = (HobbyInfo) object;
//            }
//
//        } else {
//            tagView = (TagView) tagContainerLayout.getChildAt(selectPosition);
//
//            if (hobbyList != null && hobbyList.size() > selectPosition) {
//                hobbyInfo = hobbyList.get(selectPosition);
//            }
//
//        }
//
//        if (tagView != null) {
//            tagView.setTagBackgroundColor(getResources().getColor(R.color.color_ffee00));
//            tagView.invalidate();
//        }
//
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
//            final FragmentTransaction ft = manager.beginTransaction();
//            ft.add(this, tag);
//            ft.commitAllowingStateLoss();
//        }
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
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
//        if (InterfaceUrl.URL_JOINORGANIZATION.equals(apiName)) {
//
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
//                // 申请中状态
//                if (response.getCode() == 202) {
//                    if (selectPosition > -1 && adapter.getList().size() > 0) {
//                        adapter.getList().get(selectPosition).setUserRole(4);
//                        adapter.notifyItemChanged(selectPosition);
//                    }
//                } else if (response.getCode() == 200) {// 申请成功状态
//                    if (selectPosition > -1 && adapter.getList().size() > 0) {
//                        adapter.getList().get(selectPosition).setUserRole(3);
//                        adapter.notifyItemChanged(selectPosition);
//                    }
//                }
//
//                selectPosition = -1;
//            }
//
//        } else if (InterfaceUrl.URL_GETSECONDTYPEBYSEARCHLIST.equals(apiName)) {
//            if (object instanceof GetSecondTypeBySearchListResponse) {
//                GetSecondTypeBySearchListResponse response = (GetSecondTypeBySearchListResponse) object;
//                if (response.getCode() == 200) {
//
//                    final String text = editText.getText().toString().trim();
//
//                    hobbyList = response.getHobbyList();
//                    if (tagContainerLayout != null && tagContainerLayout.getChildCount() > 0) {
//                        tagContainerLayout.removeAllViews();
//                    }
//
//                    for (HobbyInfo hobbyInfo : hobbyList) {
//                        if (hobbyInfo.getHobbyName().equals(text)) {
//                            hobbyList.remove(hobbyInfo);
//                            break;
//                        }
//                    }
//
//                    if (hobbyList.size() == 0) {
//                        textView2.setVisibility(View.GONE);
//                        tagContainerLayout.setVisibility(View.GONE);
//                    } else {
//                        textView2.setVisibility(View.VISIBLE);
//                        textView2.setText("其他相关的圈子标签");
//                        tagContainerLayout.setVisibility(View.VISIBLE);
//                        tagContainerLayout.setListTags(hobbyList);
//                    }
//
//                    if (response.getOrganList().size() > 0) {
//                        textView4.setVisibility(View.VISIBLE);
//                        textView4.setText("已有“" + text + "”相关圈子，您可要直接加入:");
//                        recycleView.setVisibility(View.VISIBLE);
//                        adapter.setList(response.getOrganList());
//                    } else {
//                        textView4.setVisibility(View.INVISIBLE);
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//            }
//        }
//    }
//
//    @Override
//    public void showLoading() {
//
//
//    }
//
//    @Override
//    public void hideLoading() {
//
//    }
//
//
//    public interface ClickListener {
//        void onClick(Object object);
//    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//        if (presenter != null) {
//            presenter.onDestroy();
//        }
//        presenter = null;
//
//        if (hobbyList != null) {
//            hobbyList.clear();
//        }
//        hobbyList = null;
//    }
//}
