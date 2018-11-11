package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.Iterator;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.SearchLabelByContentResponse;
import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.PostLabelListAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import cn.bjhdltcdn.p2plive.widget.postsearchtagview.EditTag;

/**
 * Description:搜索添加标签
 * Data: 2018/8/6
 *
 * @author: zhudi
 */
public class PostLabelSearchActivity extends BaseActivity implements BaseView {

    /**
     * 输入内容View
     */
    private EditTag inputEditTextView;
    private AssociationPresenter associationPresenter;

    public AssociationPresenter getAssociationPresenter() {
        if (associationPresenter == null) {
            associationPresenter = new AssociationPresenter(this);
        }
        return associationPresenter;
    }

    /**
     * 下拉刷新
     */
    private TwinklingRefreshLayout refreshLayout;
    /**
     * 标签列表
     */
    private RecyclerView postLabelRecyclerView;
    private PostLabelListAdapter postLabelListAdapter;
    /**
     * 选择标签
     */
    private ArrayList<PostLabelInfo> selectPostLabelInfoList;
    /**
     * 搜索内容
     */
    private String editStr;
    /**
     * 标题栏
     */
    private TitleFragment titleFragment;
    /**
     * 当前登录用户的id
     */
    private long currentBaseUserId;
    private int pageSize = 20, pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_label_search);
        currentBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        setTitle();
        init();
        ArrayList<PostLabelInfo> parcelableArrayListExtra = getIntent().getParcelableArrayListExtra(Constants.Fields.POST_LABEL_LIST);
        if (parcelableArrayListExtra != null) {
            selectPostLabelInfoList = parcelableArrayListExtra;
            for (PostLabelInfo postLabelInfo : selectPostLabelInfoList) {
                inputEditTextView.addTag("# " + postLabelInfo.getLabelName() + "  Ｘ");
            }
        } else {
            selectPostLabelInfoList = new ArrayList<>(1);
        }
    }

    private void init() {
        inputEditTextView = findViewById(R.id.et_search);
        postLabelRecyclerView = findViewById(R.id.rv_label);
        refreshLayout = findViewById(R.id.refresh_layout_view);
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        final LoadingView loadingView = new LoadingView(getApplicationContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(postLabelRecyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                getAssociationPresenter().searchLabelByContent(currentBaseUserId, editStr, pageSize, pageNum);
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNum++;
                if (!loadingView.isOnLoadFinish()) {
                    getAssociationPresenter().searchLabelByContent(currentBaseUserId, editStr, pageSize, pageNum);
                    refreshLayout.finishLoadmore();
                }
            }
        });

        postLabelListAdapter = new PostLabelListAdapter();
        postLabelListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                PostLabelInfo postLabelInfo = postLabelListAdapter.getLabelInfoList().get(position);
                if (selectPostLabelInfoList.size() >= 3) {
                    Utils.showToastShortTime("最多可选3个标签");
                } else if (selectPostLabelInfoList.contains(postLabelInfo)) {
                    Utils.showToastShortTime("不能添加重复的标签");
                } else {
                    inputEditTextView.addTag("# " + postLabelInfo.getLabelName() + "  Ｘ");
                    postLabelListAdapter.getLabelInfoList().clear();
                    postLabelListAdapter.notifyDataSetChanged();
                    selectPostLabelInfoList.add(postLabelInfo);
                    setRightButtonTextColor();
                }
            }
        });

        postLabelRecyclerView.setAdapter(postLabelListAdapter);
        inputEditTextView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (selectPostLabelInfoList.size() >= 3) {
                    Utils.showToastShortTime("最多可选3个标签");
                    inputEditTextView.getEditText().getText().clear();
                    return;
                }
                if (null != editable) {
                    editStr = editable.toString().trim();
                    if (editStr.length() > 10) {
                        Utils.showToastShortTime("标签字数不能超过10个字");
                        editStr = editStr.substring(0, 10);
                        inputEditTextView.getEditText().setText(editStr);
                        inputEditTextView.getEditText().setSelection(10);
                        return;
                    }
                    if (!TextUtils.isEmpty(editStr)) {
                        getAssociationPresenter().searchLabelByContent(currentBaseUserId, editStr, pageSize, pageNum);
                    } else {
                        postLabelListAdapter.getLabelInfoList().clear();
                        postLabelListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        inputEditTextView.setTagDeletedCallback(new EditTag.TagDeletedCallback() {
            @Override
            public void onTagDelete(String deletedTagValue) {
                Iterator<PostLabelInfo> postLabelInfoIterator = selectPostLabelInfoList.iterator();
                while (postLabelInfoIterator.hasNext()) {
                    PostLabelInfo postLabelInfo = postLabelInfoIterator.next();
                    String labelStr = "# " + postLabelInfo.getLabelName() + "  Ｘ";
                    if (labelStr.equals(deletedTagValue)) {
                        postLabelInfoIterator.remove();
                        setRightButtonTextColor();
                    }
                }
            }
        });
    }

    /**
     * 设置右键颜色
     */
    private void setRightButtonTextColor() {
        if (selectPostLabelInfoList.size() > 0) {
            titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_ffb700));
            titleFragment.getRightView().setEnabled(true);
            inputEditTextView.getEditText().setHint("");
        } else {
            titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_999999));
            titleFragment.getRightView().setEnabled(false);
            inputEditTextView.getEditText().setHint("自定义标签，最多输入10个字");
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            Utils.showToastShortTime(e.getMessage());
            return;
        }

        if (InterfaceUrl.URL_SEARCHLABELBYCONTENT.equals(apiName)) {
            if (object instanceof SearchLabelByContentResponse) {
                SearchLabelByContentResponse response = (SearchLabelByContentResponse) object;
                if (response.getCode() == 200) {

                    if (pageNum == 1) {
                        refreshLayout.finishRefreshing();
                        PostLabelInfo postLabelInfo = new PostLabelInfo();
                        postLabelInfo.setLabelName(editStr);
                        response.getList().add(0, postLabelInfo);
                        postLabelListAdapter.setDate(editStr, response.getList());
                    } else {
                        refreshLayout.finishLoadmore();
                        postLabelListAdapter.addDate(response.getList());
                    }

                    if (response.getTotal() <= pageNum * pageSize) {
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void setTitle() {

        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.getRightView().setEnabled(false);
        titleFragment.setTitle("标签");
        titleFragment.setRightViewTitle("确定");
        titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_999999));

        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        titleFragment.setRightViewTitle(new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                if (!Utils.isAllowClick()) {
                    return;
                }
                Intent intent = new Intent(PostLabelSearchActivity.this, PublishActivity.class);
                intent.putParcelableArrayListExtra(Constants.Fields.POST_LABEL_LIST, selectPostLabelInfoList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (postLabelRecyclerView != null) {
            postLabelRecyclerView = null;
        }
        if (postLabelListAdapter != null) {
            postLabelListAdapter = null;
        }
        if (selectPostLabelInfoList != null) {
            selectPostLabelInfoList = null;
        }
    }
}
