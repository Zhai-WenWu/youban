package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetOccupationListResponse;
import cn.bjhdltcdn.p2plive.model.OccupationIndexInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.CompleteInfoPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.OccupationListAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * Created by ZHUDI on 2017/11/20.
 */

public class OccupationActivity extends BaseActivity implements BaseView {
    private RecyclerView recycler_work;
    private CompleteInfoPresenter completeInfoPresenter;
    private OccupationListAdapter occupationListAdapter;
    private TitleFragment titleFragment;
    private int type;//2注册完善资料
    private View lastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
//        EventBus.getDefault().register(this);
        completeInfoPresenter = new CompleteInfoPresenter(this);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        setTitle();
        init();
        completeInfoPresenter.getOccupationList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
    }

    private void init() {
        recycler_work = findViewById(R.id.recycler_work);
        recycler_work.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5F)));
        occupationListAdapter = new OccupationListAdapter();
        occupationListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                occupationListAdapter.getWorkInfoList().get(occupationListAdapter.getIndexPositon()).setIsSelect(0);
                occupationListAdapter.getWorkInfoList().get(position).setIsSelect(1);
                occupationListAdapter.notifyItemChanged(occupationListAdapter.getIndexPositon());
                titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_orange));
                view.setSelected(true);
                occupationListAdapter.setIndexPositon(position);
                if (lastView != null && lastView != view) {
                    lastView.setSelected(false);
                }
                lastView = view;
//                occupationListAdapter.notifyItemChanged(position);
            }
        });
        recycler_work.setAdapter(occupationListAdapter);
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
//            case InterfaceUrl.URL_CHANGEDUSERINFO:
//                if (object instanceof ChangedUserInfoResponse) {
//                    ChangedUserInfoResponse changedUserInfoResponse = (ChangedUserInfoResponse) object;
//                    Utils.showToastShortTime(changedUserInfoResponse.getMsg());
//                    finish();
//                }
//                break;
            case InterfaceUrl.URL_GETOCCUPATIONLIST:
                if (object instanceof GetOccupationListResponse) {
                    GetOccupationListResponse getOccupationListResponse = (GetOccupationListResponse) object;
                    if (getOccupationListResponse.getCode() == 200) {
                        occupationListAdapter.setDate(getOccupationListResponse.getList());
                    }
                }
                break;
        }

    }

    private void saveOccupation() {
//        User user = new User();
        Intent intent = null;
        if (type == 2) {//注册完善资料
            intent = new Intent(OccupationActivity.this, CompleteInfoActivity.class);
        } else {
            intent = new Intent(OccupationActivity.this, EditInfoActivity.class);
        }
//        if (TextUtils.isEmpty(occupationListAdapter.getEditStr())) {
        if (occupationListAdapter.getWorkInfoList() != null) {
            if (occupationListAdapter.getWorkInfoList().get(occupationListAdapter.getIndexPositon()).getIsSelect() == 0) {
                Utils.showToastShortTime("请选择职业");
            } else {
                intent.putExtra(Constants.KEY.KEY_OBJECT, occupationListAdapter.getWorkInfoList().get(occupationListAdapter.getIndexPositon()));
                OccupationIndexInfo.getInstent().setIndex(occupationListAdapter.getIndexPositon());
//                user.setOccupationInfo(occupationListAdapter.getWorkInfoList().get(occupationListAdapter.getIndexPositon()));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
//        }
//        else {
//            intent.putExtra(Constants.KEY.KEY_OBJECT, occupationListAdapter.getWorkInfoList().get(occupationListAdapter.getIndexPositon()));
//            OccupationIndexInfo.getInstent().setIndex(occupationListAdapter.getIndexPositon());
////            user.setOccupationInfo(occupationListAdapter.getWorkInfoList().get(occupationListAdapter.getIndexPositon()));
//        }

//        completeInfoPresenter.changedUserInfo(user);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onAttentionEvent(OccuPationClickEvent event) {
//        if (event != null) {
//            titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_orange));
//        }
//    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_occupation);
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        titleFragment.setRightViewTitle("完成", new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                saveOccupation();
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
