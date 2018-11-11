//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.HobbySelectEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.ChangedUserInfoResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHobbyListResponse;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.User;
//import cn.bjhdltcdn.p2plive.mvp.presenter.CompleteInfoPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.HobbyListAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//
///**
// * Created by ZHUDI on 2017/11/20.
// */
//
//public class HobbyActivity extends BaseActivity implements BaseView {
//    private RecyclerView recycler_hobby;
//    private TextView tv_ok;
//    private CompleteInfoPresenter completeInfoPresenter;
//    private ImagePresenter imagePresenter;
//    private HobbyListAdapter hobbyListAdapter;
//    private User user;
//    private int type;//1.注册后完善资料2.修改资料
//    private TitleFragment titleFragment;
//    private List<HobbyInfo> list_hobby;
//    private int hobbyListNeedReq;//2.EditInfo未点确定再次进入
//    private List<HobbyInfo> hobbyList;
//    private UserPresenter userPresenter;
//
//    public UserPresenter getUserPresenter() {
//        if (userPresenter == null) {
//            userPresenter = new UserPresenter(this);
//        }
//        return userPresenter;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
//        hobbyListNeedReq = getIntent().getIntExtra(Constants.Fields.HOBBYLISTNEEDREQ, 0);
//        if (type == 1) {
//            setContentView(R.layout.activity_hobby_completeinfo);
//            user = getIntent().getParcelableExtra(Constants.Fields.USER);
//        } else if (type == 2) {
//            setContentView(R.layout.activity_hobby_editinfo);
//            setTitle();
//        }
//        EventBus.getDefault().register(this);
//        completeInfoPresenter = new CompleteInfoPresenter(this);
//        imagePresenter = new ImagePresenter(this);
//        init();
//        completeInfoPresenter.getHobbyList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1);
//    }
//
//    private void init() {
//        recycler_hobby = findViewById(R.id.recycler_hobby);
//        recycler_hobby.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recycler_hobby.setLayoutManager(linearLayoutManager);
//        hobbyListAdapter = new HobbyListAdapter();
//        hobbyListAdapter.setType(type);
//        recycler_hobby.setAdapter(hobbyListAdapter);
//        recycler_hobby.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(15)));
//        if (type == 1) {
//            tv_ok = findViewById(R.id.tv_ok);
//
//            tv_ok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    List<HobbyInfo> list_hobby_selected = hobbyListAdapter.getList_hobby_selected();
//                    if (list_hobby_selected != null && list_hobby_selected.size() > 0) {
//                        user.setHobbyList(list_hobby_selected);
//                        completeInfoPresenter.changedUserInfo(user);
//                        getUserPresenter().saveSchool(user.getUserId(), user.getSchoolInfo().getSchoolId(), user.getSchoolInfo().getSchoolName());
//                        // 上传图片
//                        if (!StringUtils.isEmpty(user.getUserIcon())) {
//                            imagePresenter.uploadImage(user.getUserId(), 1, user.getUserId(), TimeUtils.date2String(new Date()), user.getUserIcon());
//                        }
//                    }
//                }
//            });
//
//        }
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        switch (apiName) {
//            case InterfaceUrl.URL_GETHOBBYLIST:
//                if (object instanceof GetHobbyListResponse) {
//                    GetHobbyListResponse getHobbyListResponse = (GetHobbyListResponse) object;
//                    if (getHobbyListResponse.getCode() == 200) {
//                        hobbyList = getHobbyListResponse.getHobbyList();
//                        if (hobbyListNeedReq == 2) {
//                            list_hobby = getIntent().getParcelableArrayListExtra(Constants.KEY.KEY_OBJECT);
//                            for (int i = 0; i < hobbyList.size(); i++) {
//                                for (int j = 0; j < list_hobby.size(); j++) {
//                                    if (hobbyList.get(i).getHobbyId() == list_hobby.get(j).getHobbyId()) {
//                                        hobbyList.get(i).setIsSelect(1);
//                                        //hobbyInfo.setIsCheck(1);
//                                        break;
//                                    } else {
//                                        hobbyList.get(i).setIsSelect(0);
//                                        //hobbyInfo.setIsCheck(0);
//                                    }
//                                }
//                            }
//                        }
//                        hobbyListAdapter.setData(hobbyList);
//                    }
//                }
//
//                break;
//            case InterfaceUrl.URL_CHANGEDUSERINFO:
//                if (object instanceof ChangedUserInfoResponse) {
//                    ChangedUserInfoResponse changedUserInfoResponse = (ChangedUserInfoResponse) object;
//                    if (changedUserInfoResponse.getCode() == 200) {
//                        startActivity(new Intent(HobbyActivity.this, MainActivity.class));
//                        finish();
//                    }
//                }
//                break;
//
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onHobbySelectEvent(HobbySelectEvent event) {
//        if (event != null) {
//
//            if (type == 1) {
//                if (hobbyListAdapter.getList_hobby_selected().size() == 0) {
//                    tv_ok.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//                    tv_ok.setEnabled(false);
//                } else {
//                    tv_ok.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//                    tv_ok.setEnabled(true);
//                }
//            } else if (type == 2) {
//                if (hobbyListAdapter.getList_hobby_selected().size() == 0) {
//                    titleFragment.setRightViewColor(R.color.color_666666);
//                    titleFragment.getRightView().setEnabled(false);
//                } else {
//                    titleFragment.setRightViewColor(R.color.color_ffb700);
//                    titleFragment.getRightView().setEnabled(true);
//                }
//            }
//        }
//    }
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_hobby);
//        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//        titleFragment.setRightViewTitle("保存", new TitleFragment.RightViewClick() {
//            @Override
//            public void onClick() {
//                Intent intent = new Intent(HobbyActivity.this, EditInfoActivity.class);
//                intent.putParcelableArrayListExtra(Constants.KEY.KEY_OBJECT, (ArrayList<? extends Parcelable>) hobbyListAdapter.getList_hobby_selected());
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
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
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
