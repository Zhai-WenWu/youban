//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.IdRes;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHobbyListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SendActivityInviationResponse;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.SaveActivePresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.SendInviationHobbyRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.RongIMutils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
///**
// * 我的礼物
// */
//public class SendInviationSelectConditionActivity extends BaseActivity  implements BaseView {
//    private SaveActivePresenter mPresenter;
//    private AssociationPresenter associationPresenter;
//    private List<HobbyInfo> hobbyInfoList;
//    private RecyclerView recyclerView;
//    private SendInviationHobbyRecyclerViewAdapter sendInviationHobbyRecyclerViewAdapter;
//    private BaseUser baseUser;
//    private Long userId,activityId;
//    private ActivityInfo activityInfo;
//    private List<Long> hobbyIds;
//    private RadioGroup sexGroup;
//    private RadioButton allSexRadioButton,boyRadioButton,girlRadioButton;
//    private int selectSex=0;
//    private LinearLayout randomGroup;
//    private CheckBox randomThreeRadioButton,randomFiveButton,randomTenButton;
//    private int sendNumber=0;
//    private TextView destineTextView,destineNumTextView;
//    private TextView sendTextView;
//    private List<Long> selectAlumnusPerson,selectCityPerson,selectPerson;
//    private boolean needShowLoading = true;
//    private int comeInType=2;//1:从一建发布进入2：从附近活动3:从圈子详情进入
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_send_inviation_select_condition);
//        userId= SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        mPresenter = new SaveActivePresenter(this);
//        associationPresenter=new AssociationPresenter(this);
//        associationPresenter.getHobbyList(userId,0,1,needShowLoading);
//        activityId=getIntent().getLongExtra(Constants.Fields.ACTIVITY_ID,0);
//        comeInType= getIntent().getIntExtra("comeInType", 2);
//        activityInfo=new ActivityInfo();
//        activityInfo.setActivityId(activityId);
//        GreenDaoUtils.getInstance().getBaseUser(userId, new GreenDaoUtils.ExecuteCallBack() {
//            @Override
//            public void callBack(final Object object) {
//                SendInviationSelectConditionActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        baseUser = (BaseUser) object;
//                        activityInfo.setBaseUser(baseUser);
//                    }
//                });
//            }
//        });
//        selectPerson=new ArrayList<Long>();
//        setTitle();
//        init();
//    }
//
//    private void init() {
//        recyclerView= (RecyclerView)findViewById(R.id.hobby_recycler_view);
//        sendInviationHobbyRecyclerViewAdapter=new SendInviationHobbyRecyclerViewAdapter(this);
//        recyclerView.setHasFixedSize(true);
//        GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 3);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(13), 3, false));
//        recyclerView.setAdapter(sendInviationHobbyRecyclerViewAdapter);
//        sexGroup= (RadioGroup) findViewById(R.id.radio_sex);
//        allSexRadioButton= (RadioButton)findViewById(R.id.radio_all_sex);
//        boyRadioButton= (RadioButton) findViewById(R.id.radio_boy);
//        girlRadioButton= (RadioButton) findViewById(R.id.radio_girl);
//        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                if(checkedId==allSexRadioButton.getId()){
//                    selectSex=0;
//                }else  if(checkedId==boyRadioButton.getId()){
//                    selectSex=1;
//                }else{
//                    selectSex=2;
//                }
//            }
//        });
//        randomGroup= (LinearLayout) findViewById(R.id.radio_random);
//        randomThreeRadioButton= (CheckBox) findViewById(R.id.radio_random_three);
//        randomFiveButton= (CheckBox) findViewById(R.id.radio_random_five);
//        randomTenButton= (CheckBox) findViewById(R.id.radio_random_ten);
//        randomThreeRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(buttonView.isPressed()){
//                    if(isChecked){
//                        sendNumber=3;
//                        Log.e("sendNumber",sendNumber+"");
//                        randomFiveButton.setChecked(false);
//                        randomTenButton.setChecked(false);
//                    }else{
//                        sendNumber=0;
//                        Log.e("sendNumber",sendNumber+"");
//                    }
//                }
//            }
//        });
//        randomFiveButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(buttonView.isPressed()){
//                    if(isChecked){
//                        sendNumber=5;
//                        Log.e("sendNumber",sendNumber+"");
//                        randomThreeRadioButton.setChecked(false);
//                        randomTenButton.setChecked(false);
//                    }else{
//                        sendNumber=0;
//                        Log.e("sendNumber",sendNumber+"");
//                    }
//                }
//            }
//        });
//        randomTenButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(buttonView.isPressed()){
//                    if(isChecked){
//                        sendNumber=10;
//                        Log.e("sendNumber",sendNumber+"");
//                        randomFiveButton.setChecked(false);
//                        randomThreeRadioButton.setChecked(false);
//                    }else{
//                        sendNumber=0;
//                        Log.e("sendNumber",sendNumber+"");
//                    }
//                }
//            }
//        });
//        destineTextView=findViewById(R.id.tv_destine);
//        destineNumTextView=findViewById(R.id.tv_destine_num);
//        destineTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到指定人界面
//                if (hobbyIds == null) {
//                    hobbyIds = new ArrayList<Long>();
//                }
//                hobbyIds=sendInviationHobbyRecyclerViewAdapter.getSelectList();
//                if(hobbyIds.size()<1){
//                    Utils.showToastShortTime("请选择小伙伴类型");
//                }else {
//                    Intent intent = new Intent(SendInviationSelectConditionActivity.this, SendInviationSelectPersonActivity.class);
//                    intent.putExtra(Constants.Fields.HOBBY_IDS, (Serializable) hobbyIds);
//                    intent.putExtra(Constants.Fields.SEX_LIMIT, selectSex);
//                    intent.putExtra(Constants.Fields.SEND_NUMBER, sendNumber);
//                    intent.putExtra(Constants.Fields.SELECT_ALUMNUS_PERSON, (Serializable) selectAlumnusPerson);
//                    intent.putExtra(Constants.Fields.SELECT_CITY_PERSON, (Serializable) selectCityPerson);
//                    intent.putExtra("selectmaxnum", selectPerson.size());
//                    startActivityForResult(intent, 1);
//                }
//            }
//        });
//        sendTextView=findViewById(R.id.send_text_view);
//        sendTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    //发送邀请函
//                    if (hobbyIds == null) {
//                        hobbyIds = new ArrayList<Long>();
//                    }
//                    hobbyIds=sendInviationHobbyRecyclerViewAdapter.getSelectList();
//                    if(hobbyIds.size()<1){
//                        Utils.showToastShortTime("请选择小伙伴类型");
//                    }else if(sendNumber<1&&selectPerson.size()<1){
//                        Utils.showToastShortTime("请选择要发送邀请函的用户");
//                    }else{
//                        mPresenter.sendActivityInvitation(userId,activityId,hobbyIds,selectSex,sendNumber,0,2,selectPerson,0,0);
//                    }
//              }
//        });
//    }
//
//    private void setTitle() {
//        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("邀请函");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (InterfaceUrl.URL_GETHOBBYLIST.equals(apiName)) {
//            if (object instanceof GetHobbyListResponse) {
//                GetHobbyListResponse response = (GetHobbyListResponse) object;
//                if (response.getCode() == 200) {
//                    hobbyInfoList = response.getHobbyList();
//                    sendInviationHobbyRecyclerViewAdapter.setList(hobbyInfoList);
//                    sendInviationHobbyRecyclerViewAdapter.notifyDataSetChanged();
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        }else if (InterfaceUrl.URL_SENDACTIVITYINVITATION.equals(apiName)) {
//            if (object instanceof SendActivityInviationResponse) {
//                SendActivityInviationResponse response = (SendActivityInviationResponse) object;
//                if (response.getCode() == 200) {
//                    Utils.showToastShortTime(response.getMsg());
//                    if(selectPerson!=null){
//                        final int size=selectPerson.size();
//                        for(int i=0;i<size;i++){
//                            //一个userId一秒钟最多允许发送4条消息，多发会被服务器屏蔽，所以在本地设置300毫秒的延迟发送就可以了。
//                            if(i<4){
//                                final BaseUser toBaseUser=new BaseUser();
//                                toBaseUser.setUserId(selectPerson.get(i));
//                                Log.e("i",i+"   ");
//                                RongIMutils.sendSharedMessage("", 40004,toBaseUser, activityInfo);
//                            }else if(i>=4&&i<7){
//                                if(i==4){
//                                    final int forSize=size>7?7:size;
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            for (int j=4;j<forSize;j++){
//                                                final BaseUser toBaseUser=new BaseUser();
//                                                toBaseUser.setUserId(selectPerson.get(j));
//                                                Log.e("j",j+"   "+forSize);
//                                                RongIMutils.sendSharedMessage("", 40004,toBaseUser, activityInfo);
//                                            }
//                                        }
//                                    },1000);
//                                }
//                            }else if(i>=7&&i<10){
//                                if(i==7){
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            for (int k = 7; k< size; k++){
//                                                final BaseUser toBaseUser=new BaseUser();
//                                                toBaseUser.setUserId(selectPerson.get(k));
//                                                Log.e("k",k+"   "+size);
//                                                RongIMutils.sendSharedMessage("", 40004,toBaseUser, activityInfo);
//                                            }
//                                        }
//                                    },1000);
//                                }
//                            }
//
//                        }
//                    }
//                    if(comeInType==1){
//                        //一建发布进入   成功后   跳转到活动列表页面
//                        Intent intent = new Intent(this, ActiveListActivity.class);
//                        intent.putExtra(Constants.Fields.COME_IN_TYPE, 1);
//                        intent.putExtra(Constants.Fields.TO_USER_ID, 0);
//                        startActivity(intent);
//                    }
//                    finish();
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case 1:
//                    destineNumTextView.setText("（"+data.getIntExtra("destinenum",0)+"）人");
//                    selectAlumnusPerson= (List<Long>) data.getSerializableExtra(Constants.Fields.SELECT_ALUMNUS_PERSON);
//                    selectCityPerson= (List<Long>) data.getSerializableExtra(Constants.Fields.SELECT_CITY_PERSON);
//                    if(selectPerson==null){
//                        selectPerson=new ArrayList<Long>();
//                    }else{
//                        selectPerson.clear();
//                    }
//                    selectPerson.addAll(selectAlumnusPerson);
//                    selectPerson.addAll(selectAlumnusPerson.size(),selectCityPerson);
//                    break;
//
//            }
//        }
//    }
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//}
