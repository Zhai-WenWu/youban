package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.PkCommentEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetPlayCommentListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PlayCommentResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.PlayComment;
import cn.bjhdltcdn.p2plive.model.PlayInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.PkPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.PkCommentListAdapter;
import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * Pk评论
 */
public class PkCommentDialog extends DialogFragment implements BaseView {

    private View rootView;
    private List<PlayComment> mList;
    private PkCommentListAdapter adapter;
    private PkPresenter pkPresenter;
    private PlayInfo playInfo;
    private TextView sendView;
    private TextView tvNum;
    private int size;
    private ListView listView;
    private String editInputText;
    private EditText editInput;
    private int reply = -1;
    private TwinklingRefreshLayout refreshLayout;
    private int pageSize = 20;
    private int pageNumber = 1;
    private int total;
    private int position;

    public void setList(List<PlayComment> list, PlayInfo playInfo) {
        this.mList = list;
        this.playInfo = playInfo;
    }

    public void addList(PlayComment playComment) {
        mList.add(0, playComment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.dialog_pk_comment_layout, null);
        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof ViewGroup) {
                    View layoutView = ((ViewGroup) v).getChildAt(0);
                    if (layoutView != null) {
                        float y = event.getY();
                        float x = event.getX();
                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
                        if (!rect.contains((int) x, (int) y)) {
                            dismiss();
                        }
                        rect.setEmpty();
                        rect = null;
                    }
                }
                return false;
            }
        });

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPkPresenter().getPlayCommentList(playInfo.getPlayId(), 1, pageSize, pageNumber);

        tvNum = rootView.findViewById(R.id.tv_num);
        sendView = rootView.findViewById(R.id.send_view);
        ImageView ivClose = rootView.findViewById(R.id.iv_close);
        listView = rootView.findViewById(R.id.list_view);
        editInput = rootView.findViewById(R.id.reply_edit_input);
        adapter = new PkCommentListAdapter(this);
        listView.setAdapter(adapter);
        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    sendView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                    sendView.setTextColor(getResources().getColor(R.color.color_333333));
                } else {
                    sendView.setBackgroundResource(R.drawable.shape_round_10_solid_fff69f);
                    sendView.setTextColor(getResources().getColor(R.color.color_999999));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 刷新框架
        refreshLayout = rootView.findViewById(R.id.refresh_layout_view);

        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(getActivity());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(listView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                pageNumber = 1;
                getPkPresenter().getPlayCommentList(playInfo.getPlayId(), 1, pageSize, pageNumber);

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                pageNumber += 1;
                getPkPresenter().getPlayCommentList(playInfo.getPlayId(), 1, pageSize, pageNumber);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            private BaseUser fromBaseUser;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                BaseUser toBaseUser = adapter.getmList().get(i).getToBaseUser();
                if (adapter.getmList().get(i).getType() == 2) {//点击回复条目
                    editInput.setHint("回复: " + toBaseUser.getNickName());
                    reply = i;
                } else if (adapter.getmList().get(i).getType() == 1 && adapter.getmList().get(i).getFromBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    //点击评论条目但不是本人评论的
                    editInput.setHint("回复: " + adapter.getmList().get(i).getFromBaseUser().getNickName());
                    reply = i;
                } else if (adapter.getmList().get(i).getType() == 1 && adapter.getmList().get(i).getFromBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    //点击自己的评论条目
                    editInput.setHint("说点什么吧...");
                    reply = -1;
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

        sendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editInputText = editInput.getText().toString();
                if (editInputText.length() == 0) {
                    Utils.showToastShortTime("内容不能为空");
                } else if (reply == -1) {
                    getPkPresenter().playComment(playInfo.getPlayId(), editInputText, 1, playInfo.getBaseUser().getUserId(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
                    sendView.setEnabled(false);
                    editInput.setText("");
                } else if (adapter.getmList().get(position).getType() == 2) {
                    getPkPresenter().playComment(playInfo.getPlayId(), editInputText, 2, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), adapter.getmList().get(position).getToBaseUser().getUserId());
                    sendView.setEnabled(false);
                    editInput.setText("");
                } else if (adapter.getmList().get(position).getType() == 1) {
                    getPkPresenter().playComment(playInfo.getPlayId(), editInputText, 2, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), adapter.getmList().get(position).getFromBaseUser().getUserId());
                    sendView.setEnabled(false);
                    editInput.setText("");
                }

            }
        });
    }

    private void initData() {
        tvNum.setText(total + "条评论");

        if (listView != null) {
            adapter.setList(mList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_PLAYCOMMENT:
                if (object instanceof PlayCommentResponse) {
                    PlayCommentResponse playComment = (PlayCommentResponse) object;
                    if (!TextUtils.isEmpty(playComment.getMsg())) {
                        Utils.showToastShortTime(playComment.getMsg());
                    }
                    //直接关闭dialog，还要什么自行车
                    if (playComment.getCode() == 200) {
                        EventBus.getDefault().post(new PkCommentEvent());
                    }
                    /* itemClick.itemClick();
                    adapter.addList(playComment.getPlayComment());
                    tvNum.setText(adapter.getmList().size() + "条评论");
                    sendView.setEnabled(true);*/

                    KeyBoardUtils.closeKeybord(editInput, getActivity());

                    this.dismiss();
                }
                break;
            case InterfaceUrl.URL_GETPLAYCOMMENTLIST:
                if (object instanceof GetPlayCommentListResponse) {
                    GetPlayCommentListResponse getPlayCommentListResponse = (GetPlayCommentListResponse) object;
                    if (getPlayCommentListResponse.getCode() == 200) {
                        mList = getPlayCommentListResponse.getList();
                        total = getPlayCommentListResponse.getTotal();

                        if (pageNumber == 1) {
                            initData();
                        } else {
                            adapter.getmList().addAll(getPlayCommentListResponse.getList());
                            adapter.notifyDataSetChanged();
                        }
                        if (getPlayCommentListResponse.getTotal() <= pageNumber * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(false);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(true);
                        }
                    }
                }
        }
    }

    public PkPresenter getPkPresenter() {
        if (pkPresenter == null) {
            pkPresenter = new PkPresenter(this);
        }
        return pkPresenter;
    }


    @Override
    public void dismiss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            super.dismiss();
        }
    }

    public void show(FragmentManager manager) {
        show(manager, "dialog");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            // 解决java.lang.IllegalStateException: Can not perform this action问题
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public void setItemClickListener(ItemClickListener itemClick) {
        this.itemClick = itemClick;
    }

    public ItemClickListener itemClick;


    public interface ItemClickListener {
        void itemClick();
    }
}
