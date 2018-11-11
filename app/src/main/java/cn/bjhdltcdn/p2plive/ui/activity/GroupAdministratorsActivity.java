//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.UpdateManagersEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetGroupUserListResponse;
//import cn.bjhdltcdn.p2plive.model.GroupUser;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.AddministratorsListAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//
///**
// * Created by ZHUDI on 2018/1/10.
// * 群管理员
// */
//
//public class GroupAdministratorsActivity extends BaseActivity implements BaseView {
//    private ListView listView;
//    private AddministratorsListAdapter addministratorsListAdapter;
//    private GroupPresenter groupPresenter;
//    private int indexPosition;//删除的下标
//    private ArrayList<GroupUser> addministrators = new ArrayList<>(1);
//    private long groupId;
//    private TextView tv_num;
//
//    public GroupPresenter getGroupPresenter() {
//        if (groupPresenter == null) {
//            groupPresenter = new GroupPresenter(this);
//        }
//        return groupPresenter;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//        setContentView(R.layout.activity_group_administraors);
//        groupId = getIntent().getLongExtra(Constants.Fields.GROUP_ID, 0);
//        setTitle();
//        initView();
//        requestData();
//    }
//
//    private void initView() {
//        TextView tv_add = findViewById(R.id.tv_add);
//        tv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(GroupAdministratorsActivity.this, AddGroupAdministratorsActivity.class);
//                intent.putExtra(Constants.Fields.GROUP_ID, groupId);
//                startActivity(intent);
//            }
//        });
//        tv_num = findViewById(R.id.tv_num);
//        listView = findViewById(R.id.listView);
//        addministratorsListAdapter = new AddministratorsListAdapter(new AddministratorsListAdapter.ItemClick() {
//
//            @Override
//            public void onitemDeltet(int position, long groupId, long baseUerId) {
//                indexPosition = position;
//                getGroupPresenter().deleteGroupManager(groupId, baseUerId);
//            }
//        });
//        listView.setAdapter(addministratorsListAdapter);
//    }
//
//    /**
//     * 请求数据
//     */
//    private void requestData() {
//        getGroupPresenter().getGroupUserList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), groupId, 20, 1);
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        switch (apiName) {
//            case InterfaceUrl.URL_DELETEGROUPMANAGER:
//                if (object instanceof BaseResponse) {
//                    BaseResponse baseResponse = (BaseResponse) object;
//                    if (baseResponse.getCode() == 200) {
//                        addministratorsListAdapter.getList().remove(indexPosition);
//                        addministratorsListAdapter.notifyDataSetChanged();
//                        tv_num.setText(addministratorsListAdapter.getCount() + "/3");
//                        EventBus.getDefault().post(new UpdateManagersEvent());
//                    }
//                }
//                break;
//            case InterfaceUrl.URL_GETGROUPUSERLIST:
//                if (object instanceof GetGroupUserListResponse) {
//                    GetGroupUserListResponse response = (GetGroupUserListResponse) object;
//                    if (response.getCode() == 200) {
//                        addministrators.clear();
//                        for (GroupUser groupUser : response.getGroupUserList()) {
//                            if (groupUser.getUserRole() == 2) {
//                                addministrators.add(groupUser);
//                            }
//                        }
//                        addministratorsListAdapter.setList(addministrators);
//                        tv_num.setText(addministrators.size() + "/3");
//                    }
//                }
//
//                break;
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onUpdateManagersThread(UpdateManagersEvent event) {
//        if (event == null) {
//            return;
//        }
//        requestData();
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
//    private void setTitle() {
//        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("群详情");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
