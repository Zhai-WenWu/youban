package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.SearchSchoolListResponse;
import cn.bjhdltcdn.p2plive.model.SchoolInfo;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.CompleteInfoPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.SchoolListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.AskDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * 学校选择页面
 */

public class SchoolActivity extends BaseActivity implements BaseView {
    private EditText edit_school;
    private CompleteInfoPresenter completeInfoPresenter;
    private RecyclerView recycler_school;
    private SchoolListAdapter schoolListAdapter;
    private ImageView img_clean;
    private int pageSize = 20, pageNum = 1;
    private int type;//3注册完善资料
    private TwinklingRefreshLayout refreshLayout;
    private TitleFragment titleFragment;
    private UserPresenter userPresenter;
    private String editStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        completeInfoPresenter = new CompleteInfoPresenter(this);
        userPresenter = new UserPresenter(this);
        setTitle();
        init();

    }

    private void init() {
        edit_school = findViewById(R.id.edit_school);
        img_clean = findViewById(R.id.img_clean);
        recycler_school = findViewById(R.id.recycler_school);
        refreshLayout = findViewById(R.id.refresh_layout_view);
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        final LoadingView loadingView = new LoadingView(getApplicationContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recycler_school);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                completeInfoPresenter.searchSchoolList(edit_school.getText().toString(), pageSize, pageNum);
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNum++;
                if (!loadingView.isOnLoadFinish()) {
                    completeInfoPresenter.searchSchoolList(edit_school.getText().toString(), pageSize, pageNum);
                    refreshLayout.finishLoadmore();
                }
            }
        });

        recycler_school.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5f)));
        schoolListAdapter = new SchoolListAdapter();
        recycler_school.setAdapter(schoolListAdapter);
        schoolListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                SchoolInfo schoolInfo = schoolListAdapter.getList_school().get(position);
                Intent intent;
                if (type == 3) {
                    intent = new Intent(SchoolActivity.this, CompleteInfoActivity.class);
                } else {
                    intent = new Intent(SchoolActivity.this, EditInfoActivity.class);
                }
                intent.putExtra(Constants.KEY.KEY_OBJECT, schoolInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        img_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_school.setText("");
                img_clean.setVisibility(View.GONE);
                schoolListAdapter.setDate(new ArrayList<SchoolInfo>(1));
            }
        });
        edit_school.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (null != editable) {
                    editStr = editable.toString().trim();
                    if (!TextUtils.isEmpty(editStr)) {
                        img_clean.setVisibility(View.VISIBLE);
                        completeInfoPresenter.searchSchoolList(editStr, pageSize, pageNum);
                    } else {
                        img_clean.setVisibility(View.GONE);
                        titleFragment.setRightViewColor(R.color.color_999999);
                        titleFragment.getRightView().setClickable(false);
                    }
                }
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_SEARCHSCHOOLLIST:
                if (object instanceof SearchSchoolListResponse) {
                    SearchSchoolListResponse searchSchoolListResponse = (SearchSchoolListResponse) object;
                    if (searchSchoolListResponse.getCode() == 200) {

                        if (searchSchoolListResponse.getTotal() == 0) {
                            titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_orange));
                            titleFragment.getRightView().setClickable(true);
                        }

                        if (pageNum == 1) {
                            refreshLayout.finishRefreshing();
                            schoolListAdapter.setDate(searchSchoolListResponse.getSchoolList());
                        } else {
                            refreshLayout.finishLoadmore();
                            schoolListAdapter.addDate(searchSchoolListResponse.getSchoolList());
                        }

                        if (searchSchoolListResponse.getTotal() <= pageNum * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(false);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(true);
                        }
                    }
                }
                break;
        }
    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_school);
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });

        titleFragment.setRightViewTitle("保存");

        titleFragment.setRightViewTitle("保存", new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {

                new AskDialog.Builder()
                        .leftBtnText("取消")
                        .rightBtnText("确定")
                        .content("您好，系统会根据学校建立校友会，选择相同学校的用户将会自动加入；您在应用内其他公开圈子内发布的内容也将会同步到校友会中（私密圈子内的内容除外）。 学校选择后30天内不能更改，请确保学校名称正确，感谢您的使用！")
                        .rightClickListener(new AskDialog.OnRightClickListener() {
                            @Override
                            public void onClick() {
                                if (!TextUtils.isEmpty(editStr)) {
                                    SchoolInfo schoolInfo = new SchoolInfo();
                                    schoolInfo.setSchoolName(editStr);
                                    Intent intent;
                                    if (type == 3) {
                                        intent = new Intent(SchoolActivity.this, CompleteInfoActivity.class);
                                    } else {
                                        intent = new Intent(SchoolActivity.this, EditInfoActivity.class);
                                    }
                                    intent.putExtra(Constants.KEY.KEY_OBJECT, schoolInfo);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                        })
                        .build()
                        .show(getSupportFragmentManager());

            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
