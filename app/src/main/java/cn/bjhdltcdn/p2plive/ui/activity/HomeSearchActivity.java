package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetHotWordsListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JoinGroupResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SearchKeywordListResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetHotWordsListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.SearchListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.tagview.TagContainerLayout;
import cn.bjhdltcdn.p2plive.widget.tagview.TagView;

/**
 * 首页搜索列表
 */
public class HomeSearchActivity extends BaseActivity implements BaseView {
    private GetHotWordsListPresenter presenter;
    private GroupPresenter groupPresenter;
    private ListView listView;
    private SearchListAdapter searchListAdapter;
    private View emptyView;
    private TextView searchTextView;
    private RelativeLayout keyTipTextLayout;
    private EditText searchContentEdit;
    private ImageView delectTextImg;
    private TagContainerLayout tagContainerLayout;
    private Long userId;
    private TextView empty_tv;
    private int isExistGroup;//当前用户是否在群中(0不在,1在,2申请中),
    private int groupMode;//成员进群方式(1直接入群,2申请进群),
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        initView();
        presenter = new GetHotWordsListPresenter(this);
        groupPresenter = new GroupPresenter(this);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        presenter.getHotSearchFieldList();
    }

    public void initView() {
        searchTextView = findViewById(R.id.search_textview);
        searchContentEdit = findViewById(R.id.search_edittext);
        emptyView = findViewById(R.id.empty_view);
        empty_tv = findViewById(R.id.empty_textView);
        listView = (ListView) findViewById(R.id.list_view);
        searchListAdapter = new SearchListAdapter(this);
        listView.setAdapter(searchListAdapter);

        searchListAdapter.setOnClick(new SearchListAdapter.OnClick() {
            @Override
            public void onMoreClick(int type) {
                //查看更多
                Intent intent = new Intent(HomeSearchActivity.this, SearchMoreListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("content", searchContentEdit.getText().toString());
                startActivity(intent);
            }

            @Override
            public void joinGroupClick(final Group group, int posi) {
                isExistGroup = group.getIsExistGroup();
                groupMode = group.getGroupMode();
                position = posi;
                if (isExistGroup == 0) {
                    if (groupMode == 2) {
                        //申请加入群组
                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("", "本群为私密群，需申请同意后才能加入，现在要申请入群吗？", "取消", "申请进群");
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                                //取消
                            }

                            @Override
                            public void onRightClick() {
                                //申请
                                groupPresenter.joinGroup(userId, group.getGroupId(), groupMode);
                            }
                        });
                        dialog.show(getSupportFragmentManager());
                    } else if (groupMode == 1) {
                        //直接加入群组
                        groupPresenter.joinGroup(userId, group.getGroupId(), groupMode);
                    }
                } else if (isExistGroup == 1) {
                    //发起群聊
                    RongIMutils.startGroupChat(HomeSearchActivity.this, group.getGroupId() + "", group.getGroupName());

                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = searchListAdapter.getItem(position);
                if (object instanceof OrganizationInfo) {
                    //跳到圈子详情
                    OrganizationInfo organizationInfo = (OrganizationInfo) object;
                } else if (object instanceof ActivityInfo) {
                    ActivityInfo ActivityInfo = (ActivityInfo) object;
                    //跳到活动详情
                } else if (object instanceof BaseUser) {
                    //跳到用户详情
                    BaseUser baseUser = (BaseUser) object;
                    if (baseUser.getUserId() != userId) {
                        //跳到用户详情
                        startActivity(new Intent(HomeSearchActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                    }
                } else if (object instanceof Group) {
                    Group group = (Group) object;
                    //没有作用
                }
            }
        });
        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索
                finish();
            }
        });
        searchContentEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchContentEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    String searchKey = searchContentEdit.getText().toString();
                    if (!StringUtils.isEmpty(searchKey)) {
                        presenter.searchKeywordList(userId, searchKey);
                    } else {
                        Utils.showToastShortTime("请输入搜索内容");
                    }
                    return true;
                }
                return false;
            }
        });
        delectTextImg = findViewById(R.id.delete_view);
        delectTextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContentEdit.setText("");
            }
        });
        emptyView.setVisibility(View.GONE);
        keyTipTextLayout = findViewById(R.id.key_layout);
        tagContainerLayout = findViewById(R.id.tag_container_view);
        listView.setVisibility(View.GONE);
        tagContainerLayout.setVisibility(View.VISIBLE);
        keyTipTextLayout.setVisibility(View.VISIBLE);
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                //搜索
                presenter.searchKeywordList(userId, text);
                searchContentEdit.setText(text);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_GETHOTWORDSLIST)) {
            if (object instanceof GetHotWordsListResponse) {
                GetHotWordsListResponse getHotSearchFieldListResponse = (GetHotWordsListResponse) object;
                if (getHotSearchFieldListResponse.getCode() == 200) {
                    List keywordInfoList = getHotSearchFieldListResponse.getList();
                    if (keywordInfoList != null && keywordInfoList.size() > 0) {
                        tagContainerLayout.setVisibility(View.VISIBLE);
                        keyTipTextLayout.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        tagContainerLayout.setTags(keywordInfoList);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        tagContainerLayout.setVisibility(View.GONE);
                        keyTipTextLayout.setVisibility(View.GONE);
                    }
                } else {
                    Utils.showToastShortTime(getHotSearchFieldListResponse.getMsg());
                }

            }
        } else if (apiName.equals(InterfaceUrl.URL_SEARCHKEYWORDLIST)) {
            if (object instanceof SearchKeywordListResponse) {
                SearchKeywordListResponse searchKeywordListResponse = (SearchKeywordListResponse) object;
                if (searchKeywordListResponse.getCode() == 200) {
                    List<OrganizationInfo> organizationInfoList = searchKeywordListResponse.getOrganList();
                    List<Group> groupList = searchKeywordListResponse.getGroupList();
                    List<BaseUser> baseUserList = searchKeywordListResponse.getUserList();
                    List<ActivityInfo> ActivityInfoList = searchKeywordListResponse.getActiveList();
                    if (organizationInfoList != null && groupList != null && baseUserList != null && ActivityInfoList != null) {
                        if (organizationInfoList.size() > 0 || groupList.size() > 0 || baseUserList.size() > 0 || ActivityInfoList.size() > 0) {
                            emptyView.setVisibility(View.GONE);
                        } else {
                            emptyView.setVisibility(View.VISIBLE);
                            empty_tv.setText(searchKeywordListResponse.getBlankHint());
                        }

                        tagContainerLayout.setVisibility(View.GONE);
                        keyTipTextLayout.setVisibility(View.GONE);
                        if (listView != null) {
                            listView.setVisibility(View.VISIBLE);
                        }
                        searchListAdapter.setList(organizationInfoList, baseUserList, groupList, ActivityInfoList, searchKeywordListResponse.getDefaultImg());
                        searchListAdapter.notifyDataSetChanged();
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        empty_tv.setText(searchKeywordListResponse.getBlankHint());
                    }

                } else {
                    Utils.showToastShortTime(searchKeywordListResponse.getMsg());
                }

            }
        } else if (apiName.equals(InterfaceUrl.URL_JOIN_GROUP)) {
            if (object instanceof JoinGroupResponse) {
                JoinGroupResponse baseResponse = (JoinGroupResponse) object;
                if (baseResponse.getCode() == 200) {
                    Utils.showToastShortTime(baseResponse.getMsg());
                    if (groupMode == 1) {
                        Group group = (Group) searchListAdapter.getItem(position);
                        group.setIsExistGroup(1);
                        searchListAdapter.notifyDataSetChanged();
                    } else {
                        Group group = (Group) searchListAdapter.getItem(position);
                        group.setIsExistGroup(2);
                        searchListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchListAdapter != null) {
            searchListAdapter = null;
        }
        if (listView != null) {
            listView = null;
        }
    }
}
