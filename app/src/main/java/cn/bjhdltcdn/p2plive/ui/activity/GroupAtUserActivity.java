package cn.bjhdltcdn.p2plive.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.RongEditLisenterEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupUserListResponse;
import cn.bjhdltcdn.p2plive.model.AddressBook;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.GroupUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.AddressBookListAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imlib.model.UserInfo;

/**
 * @群成员
 */
public class GroupAtUserActivity extends BaseActivity implements BaseView {
    private static final String INDEX_STRING_TOP = "↑";

    private View emptyView;
    private TextView empty_tv;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SuspensionDecoration mDecoration;

    private AddressBookListAdapter adapter;

    private List<AddressBook> list;

    private int userRole;
    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    private GroupPresenter presenter;

    public GroupPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GroupPresenter(this);
        }
        return presenter;
    }

    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_at_user);
        setTitle();
        initView();
        groupId = getIntent().getLongExtra(Constants.Fields.GROUP_ID, 0);
        getPresenter().getGroupUserList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), groupId, 0, 0);
    }

    private void initView() {

        if (list == null) {
            list = new ArrayList<>(1);
        }

        //使用indexBar
        mTvSideBarHint = findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = findViewById(R.id.indexBar);//IndexBar

        recyclerView = findViewById(R.id.recycle_view);
        emptyView = findViewById(R.id.empty_view);
        empty_tv = findViewById(R.id.empty_textView);
        layoutManager = new LinearLayoutManager(App.getInstance());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AddressBookListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    if (userRole == 1 || userRole == 2) {
                        EventBus.getDefault().post(new RongEditLisenterEvent(groupId));
                    } else {
                        Utils.showToastShortTime("只有群主和管理员才能使用此功能");
                        return;
                    }
                } else {
                    AddressBook addressBook = adapter.getList().get(position);
                    if (addressBook == null) {
                        return;
                    }
                    UserInfo userInfo = new UserInfo(String.valueOf(addressBook.getUserId()), addressBook.getNickName(), Uri.parse(addressBook.getUserIcon()));
                    RongMentionManager.getInstance().mentionMember(userInfo);
                }
                finish();

            }
        });

        recyclerView.addItemDecoration(mDecoration = new

                SuspensionDecoration(App.getInstance(), list));
        //如果add两个，那么按照先后顺序，依次渲染。
        recyclerView.addItemDecoration(new

                LinearLayoutSpaceItemDecoration(Utils.dp2px(1), true));

        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .

                        setNeedRealIndex(true)//设置需要真实的索引
                .

                        setmLayoutManager(layoutManager);//设置RecyclerView的LayoutManager

    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            if (e instanceof SocketTimeoutException) {
                Utils.showToastShortTime("网络连接超时");
                return;
            }
            Utils.showToastShortTime(e.getMessage());
            return;
        }

        if (InterfaceUrl.URL_GETGROUPUSERLIST.equals(apiName)) {
            if (object instanceof GetGroupUserListResponse) {
                GetGroupUserListResponse response = (GetGroupUserListResponse) object;
                if (response.getCode() == 200) {
                    setData(response.getGroupUserList());
                    if (response.getGroupUserList().size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(response.getBlankHint())) {
                            empty_tv.setText(response.getBlankHint());
                        }
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }

            }
        }
    }

    private void setData(List<GroupUser> mList) {
        if (list == null) {
            list = new ArrayList<>(1);
        } else {
            list.clear();
        }

        AddressBook addressBook = new AddressBook();
        addressBook.setNickName("所有人");
        addressBook.setTop(true);
        addressBook.setBaseIndexTag(INDEX_STRING_TOP);
        list.add(addressBook);
        for (GroupUser groupUser : mList) {
            BaseUser baseUser = groupUser.getBaseUser();
            addressBook = new AddressBook();
            if (baseUser != null) {
                //是当前用户
                if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    userRole = groupUser.getUserRole();
                }
                addressBook.setNickName(baseUser.getNickName());
                addressBook.setUserId(baseUser.getUserId());
                addressBook.setUserIcon(baseUser.getUserIcon());
                list.add(addressBook);
            }
        }

        adapter.setList(list);

        mIndexBar.setmSourceDatas(list)//设置数据
                .invalidate();

        mDecoration.setmDatas(list);
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("选择提醒的人");
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
    public void onDestroy() {
        super.onDestroy();
        if (layoutManager != null) {
            layoutManager.removeAllViews();
            layoutManager = null;
        }


        if (list != null) {
            list.clear();
        }
        list = null;
    }
}
