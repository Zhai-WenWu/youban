//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextWatcher;
//import android.text.style.ForegroundColorSpan;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bigkoo.pickerview.OptionsPickerView;
//import com.bigkoo.pickerview.TimePickerView;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.UpdateActiveDetailEvent;
//import cn.bjhdltcdn.p2plive.event.UpdateActiveListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetActiveTypeListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SaveActiveResponse;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
//import cn.bjhdltcdn.p2plive.model.ActivityNumber;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.UserLocation;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.SaveActivePresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.ActiveListHobbyRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.ActivePublishImageRecycleAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.utils.DateUtils;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
//import static cn.bjhdltcdn.p2plive.utils.DateUtils.getFormatDataString;
//
///**
// * Created by Hu_PC on 2017/11/9.
// * 发起活动页
// */
//
//public class BootUpActivity extends BaseActivity implements BaseView {
//
//    private SaveActivePresenter presenter;
//    private ImagePresenter imagePresenter;
//    private RelativeLayout addImgLayout;
//    private ActivityInfo activityInfo;
//    private long userId;
//    private List<HobbyInfo> hobbyInfoList;
//    private List<OrganizationInfo> organizationInfoList;
//    private long orgainId;
//    private EditText themeEdit;
//    private List<Image> list_img;
//    private int type = 2;//2:线上活动 1：线下活动
//    private RelativeLayout placeLayout, optionalLayout;
//    private TextView time_tv, place_tv, usernum_tv, charge_tv, imgnum_tv, addImgTextView, tv_num_tv;
//    private RecyclerView recyclerView;
//    private ActivePublishImageRecycleAdapter adapter;
//    private int maxSelectNum = 4;
//    private int updateActivityType;//1：发布活动 2：修改活动
//    private int comeInType = 2;//1:从一建发布进入2：从附近活动3:从圈子详情进入
//    private int deletePosition;//删除图片的位置
//    private ArrayList<String> options2Items_01 = new ArrayList<>();
//    private TitleFragment titleFragment;
//    private ActivityLocationInfo activityLocationInfo;
//    private boolean isShowCurrentTime;
//    private ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter;
//    private RecyclerView hobbyRecycleView;
//    private int selectSex = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_launch_active);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        presenter = new SaveActivePresenter(this);
//        imagePresenter = new ImagePresenter(this);
//        activityInfo = getIntent().getParcelableExtra("activityInfo");
//        hobbyInfoList = getIntent().getParcelableArrayListExtra("hobbyInfolist");
//        organizationInfoList = getIntent().getParcelableArrayListExtra("organizationInfoList");
//        orgainId = getIntent().getLongExtra("orgainId", 0);
//        selectSex = getIntent().getIntExtra("selectSex", 0);
//        if (hobbyInfoList == null) {
//            hobbyInfoList = activityInfo.getHobbyList();
//        }
//        if (organizationInfoList == null) {
//            organizationInfoList = activityInfo.getOrganList();
//        }
//        updateActivityType = getIntent().getIntExtra("updateActivityType", 0);
//        comeInType = getIntent().getIntExtra("comeInType", 2);
//        type = hobbyInfoList.get(0).getHobbyType();
//        if (type == 1) {
//            isShowCurrentTime = false;
//        } else {
//            isShowCurrentTime = true;
//        }
//        setTitle();
//        initView();
//
//    }
//
//    private void initView() {
//        themeEdit = findViewById(R.id.active_content_tv);
//        tv_num_tv = (TextView) findViewById(R.id.tv_num_content);
//        themeEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() <= 200) {
//                    tv_num_tv.setText(s.length() + "/200");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        placeLayout = findViewById(R.id.active_place_layout);
//        optionalLayout = findViewById(R.id.active_optional_layout);
//        if (type == 1) {
//            placeLayout.setVisibility(View.VISIBLE);
//            optionalLayout.setVisibility(View.VISIBLE);
//        } else {
//            placeLayout.setVisibility(View.GONE);
//            optionalLayout.setVisibility(View.GONE);
//        }
//        if (!App.getInstance().isLoctionSuccess()) {
//            //定位失败 弹出弹窗
////            ActiveLaunchSuccessTipDialog dialog=new ActiveLaunchSuccessTipDialog();
////            dialog.setText("定位服务未开启", "请在手机设置中开启定位服务", "返回", "开启定位");
////            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
////                @Override
////                public void onLeftClick() {
////                    //取消
////                    finish();
////                }
////
////                @Override
////                public void onRightClick() {
////                    //开启定位
////                    Utils.goAppDetailSettingIntent(BootUpActivity.this);
////                    finish();
////                }
////            });
////            dialog.show(getSupportFragmentManager());
//            ActiveEditTipDialog networkDialog = new ActiveEditTipDialog();
//            networkDialog.setItemText("获取位置失败，请检查您的网络，或到“设置”中开启定位！", "我知道了");
//            networkDialog.setItemClick(new ActiveEditTipDialog.ItemClick() {
//                @Override
//                public void itemClick() {
//                    finish();
//                }
//            });
//            networkDialog.show(getSupportFragmentManager());
//            return;
//        }
//        time_tv = findViewById(R.id.active_time_tv);
//        time_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar selectedDate = Calendar.getInstance();//系统当前时间
//                Calendar startDate = Calendar.getInstance();
//                startDate.set(startDate.get(Calendar.YEAR), 0, 1, 0, 0);
//                Calendar endDate = Calendar.getInstance();
//                endDate.set(startDate.get(Calendar.YEAR) + 1, 11, 31);
//                TimePickerView pvTime = new TimePickerView.Builder(BootUpActivity.this, new TimePickerView.OnTimeSelectListener() {
//                    @Override
//                    public void onTimeSelect(Date date, View v, boolean isCheck) {//选中事件回调
//                        if (isCheck) {
//                            time_tv.setText("【" + getFormatDataString(date, DateUtils.DATE_FORMAT_9) + "】");
//                        } else {
//                            Calendar c = Calendar.getInstance();//
//                            int mYear = c.get(Calendar.YEAR); // 获取当前年份
//                            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
//                            int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
//                            int mHour = c.get(Calendar.HOUR_OF_DAY);//时
//
//                            Calendar sc = Calendar.getInstance();
//                            sc.setTime(date);
//                            int sYear = sc.get(Calendar.YEAR); // 获取选择年份
//                            int sMonth = sc.get(Calendar.MONTH) + 1;// 获取选择月份
//                            int sDay = sc.get(Calendar.DAY_OF_MONTH);// 获取选择日期
//                            int sHour = sc.get(Calendar.HOUR_OF_DAY);//获取选择时
//
//                            long selectTime = sc.getTime().getTime() / 1000;
//                            long currentTime = c.getTime().getTime() / 1000;
//                            long rangeTime = 30 * 24 * 60 * 60;
//                            if (selectTime >= currentTime) {
//                                if ((selectTime - currentTime) > rangeTime) {
//                                    Utils.showToastShortTime("只能发布30天之内的活动");
//                                } else {
//                                    time_tv.setText("【" + getFormatDataString(date, DateUtils.DATE_FORMAT_8) + ":00】");
//                                }
//                            } else {
//                                Utils.showToastShortTime("已过去的时间无效");
//                            }
//
////                            if(sYear<mYear){
////                                Utils.showToastShortTime("已过去的时间无效");
////                            }else if(sYear==mYear){
////                                if(sMonth<mMonth){
////                                    Utils.showToastShortTime("已过去的时间无效");
////                                }else if(sMonth==mMonth){
////                                    if(sDay<mDay){
////                                        Utils.showToastShortTime("已过去的时间无效");
////                                    }else if(sDay==mDay){
////                                        if(sHour<=mHour){
////                                            Utils.showToastShortTime("已过去的时间无效");
////                                        }else{
////                                            time_tv.setText("【"+getFormatDataString(date,DateUtils.DATE_FORMAT_8)+":00】");
////                                        }
////                                    }else{
////                                        time_tv.setText("【"+getFormatDataString(date,DateUtils.DATE_FORMAT_8)+":00】");
////                                    }
////                                }else{
////                                    time_tv.setText("【"+getFormatDataString(date,DateUtils.DATE_FORMAT_8)+":00】");
////                                }
////                            }else{
////                                time_tv.setText("【"+getFormatDataString(date,DateUtils.DATE_FORMAT_8)+":00】");
////                            }
//
//                        }
//                    }
//                })
//                        .setType(new boolean[]{true, true, true, true, false, false})// 默认全部显示
//                        .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                        .setDate(selectedDate)
//                        .setRangDate(startDate, endDate)
//                        .setShow_Current_Time(isShowCurrentTime)
//                        .setLabel("年", "月", "日", ":00", "分", "秒")//默认设置为年月日时分秒
//                        .build();
//                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
//                pvTime.show();
//            }
//        });
//        place_tv = findViewById(R.id.active_place_tv);
//        place_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BootUpActivity.this, ActiveSelectLocationActivity.class);
//                startActivityForResult(intent, 1);
//            }
//        });
//        usernum_tv = findViewById(R.id.active_user_num_tv);
//        usernum_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (options2Items_01.size() < 1) {
//                    presenter.getActiveTypeList();
//                } else {
//                    OptionsPickerView pvOptions = new OptionsPickerView.Builder(BootUpActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
//                        @Override
//                        public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                            //返回的分别是三个级别的选中位置
//                            String tx = options2Items_01.get(options1);
//                            usernum_tv.setText("【" + tx + "】");
//                        }
//                    })
//                            .build();
//                    pvOptions.setPicker(options2Items_01);
//                    pvOptions.setSelectOptions(options2Items_01.size() / 2);
//                    pvOptions.show();
//                }
//            }
//        });
//        charge_tv = findViewById(R.id.active_user_charge_edit);
//        recyclerView = findViewById(R.id.active_img_recycleview);
//        adapter = new ActivePublishImageRecycleAdapter(this);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
//        linearLayoutManager.setAutoMeasureEnabled(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnDelectItemClick(new ActivePublishImageRecycleAdapter.OnDelectItemClick() {
//            @Override
//            public void delectItemClick(int position) {
//                //删除图片
//                if (updateActivityType == 2) {
//                    //修改活动
//                    if (adapter.getItem(position).getYesOrNoUpload() == 0) {
//                        //删除已上传的图片调用删除图片接口
//                        deletePosition = position;
//                        presenter.deleteImage(adapter.getItem(position).getImageId(),0);
//                    } else {
//                        adapter.removeItem(position);
//                        maxSelectNum++;
//                        if (maxSelectNum > 0) {
//                            addImgLayout.setVisibility(View.VISIBLE);
//                        } else {
//                            addImgLayout.setVisibility(View.GONE);
//                        }
//                        imgnum_tv.setText((4 - maxSelectNum) + "/" + 4);
//                    }
//                } else {
//                    //发布活动
//                    adapter.removeItem(position);
//                    maxSelectNum++;
//                    if (maxSelectNum > 0) {
//                        addImgLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        addImgLayout.setVisibility(View.GONE);
//                    }
//                    imgnum_tv.setText((4 - maxSelectNum) + "/" + 4);
//                }
//            }
//        });
//        imgnum_tv = findViewById(R.id.img_num_tv);
//        hobbyRecycleView = findViewById(R.id.recycler_hobby);
//        hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(this);
//        hobbyRecyclerViewAdapter.setList(hobbyInfoList);
//        hobbyRecycleView.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(App.getInstance(), 4);
//        gridLayoutManager2.setAutoMeasureEnabled(true);
//        hobbyRecycleView.setLayoutManager(gridLayoutManager2);
//        hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 4, false));
//        hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
//        addImgLayout = findViewById(R.id.add_img_layout);
//        addImgTextView = findViewById(R.id.active_add_img_tv);
//        SpannableStringBuilder style = new SpannableStringBuilder();
//        style.append("添加图片（选填）");
//        style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_666666)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        addImgTextView.setText(style);
//
//        addImgLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //调起选择图片
//                PictureSelector.create(BootUpActivity.this)
//                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                        .compress(true) // 是否压缩 true or false
//                        .maxSelectNum(maxSelectNum)
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
//            }
//        });
//        list_img = new ArrayList<>(1);
//
//        if (updateActivityType == 2) {
//            themeEdit.setText(activityInfo.getTheme());
//            time_tv.setText("【" + activityInfo.getActivityTime() + "】");
//            activityLocationInfo = activityInfo.getLocationInfo();
//            if (activityLocationInfo != null && activityInfo.getType() == 1) {
//                place_tv.setText("【" + activityLocationInfo.getAddr() + "】");
//            }
//            if (activityInfo.getActivityNumber() > 0) {
//                usernum_tv.setText("【" + activityInfo.getActivityNumber() + "人】");
//            } else {
//                usernum_tv.setText("【选择人数】");
//            }
//            if (activityInfo.getActivityPrice() >= 0) {
//                charge_tv.setText(activityInfo.getActivityPrice() + "");
//            } else {
//                charge_tv.setText("");
//            }
//
//            imgnum_tv.setText(activityInfo.getImageList().size() + "/" + 4);
//            maxSelectNum = 4 - activityInfo.getImageList().size();
//            adapter.setList(activityInfo.getImageList());
//            titleFragment.setRightViewTitle("保存");
//        } else {
//            activityInfo = new ActivityInfo();
//            titleFragment.setRightViewTitle("发布");
//        }
//    }
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        if (updateActivityType == 2) {
//            //修改活动
//            titleFragment.setTitle(R.string.title_launch_active_edit);
//        } else {
//            if (type == 1) {
//                //线下活动
//                titleFragment.setTitle(R.string.title_launch_active);
//            } else {
//                //线上活动
//                titleFragment.setTitle(R.string.title_launch_active);
//            }
//        }
//
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//        titleFragment.setRightViewColor(R.color.color_ffb700);
//        titleFragment.setRightViewTitle("", new TitleFragment.RightViewClick() {
//            @Override
//            public void onClick() {
//                String theme = themeEdit.getText().toString();
//                String timeStr = time_tv.getText().toString();
//                if (StringUtils.isEmpty(theme)) {
//                    Utils.showToastShortTime("请填写活动主题或简介");
//                    return;
//                }
//                if (timeStr.contains("时间")) {
//                    Utils.showToastShortTime("请选择时间");
//                    return;
//                }
//                //位置
//                if (type == 1) {
//                    //线下活动
//                    if (activityLocationInfo == null) {
//                        Utils.showToastShortTime("请选择地点");
//                        return;
//                    }
//                    activityInfo.setLocationInfo(activityLocationInfo);
//                } else {
//                    //线上活动
//                    UserLocation userLocation = App.getInstance().getUserLocation();
//                    if (userLocation == null) {
//                        //定位失败 弹出弹窗
//                        ActiveEditTipDialog networkDialog = new ActiveEditTipDialog();
//                        networkDialog.setItemText("哎呀，您的网络不给力，获取不到定位。请重新登录获取定位吧", "我知道了");
//                        networkDialog.setItemClick(new ActiveEditTipDialog.ItemClick() {
//                            @Override
//                            public void itemClick() {
//                                finish();
//                            }
//                        });
//                        networkDialog.show(getSupportFragmentManager());
//                        return;
//                    } else {
//                        activityLocationInfo = new ActivityLocationInfo();
//                        activityLocationInfo.setAddr(userLocation.getAddr());
//                        activityLocationInfo.setLatitude(userLocation.getLatitude());
//                        activityLocationInfo.setLongitude(userLocation.getLongitude());
//                        activityLocationInfo.setCity(userLocation.getCity());
//                        activityLocationInfo.setProvince(userLocation.getProvince());
//                        activityLocationInfo.setDistrict(userLocation.getDistrict());
//                        activityInfo.setLocationInfo(activityLocationInfo);
//                    }
//
//                }
//                String num = usernum_tv.getText().toString();
//                int number = -1;
//                try {
//                    number = Integer.parseInt(num.substring(1, num.length() - 2));
//                } catch (Exception e) {
//
//                }
//                int charge = -1;
//                try {
//                    charge = Integer.parseInt(charge_tv.getText().toString());
//                } catch (Exception e) {
//
//                }
//                //主题
//                activityInfo.setTheme(theme);
//                //时间
//                String time = timeStr.substring(1, timeStr.length() - 1);
//                activityInfo.setActivityTime(time);
//
//
//                //人数
//                activityInfo.setActivityNumber(number);
//                //费用
//                activityInfo.setActivityPrice(charge);
//
//                if (updateActivityType == 2) {
//                    //修改活动
//                    presenter.updateActive(userId, activityInfo);
//                } else {
//                    //发布活动
//                    activityInfo.setType(type);
//                    activityInfo.setHobbyList(hobbyInfoList);
//                    activityInfo.setOrganList(organizationInfoList);
//                    activityInfo.setSexLimit(selectSex);
//                    if (organizationInfoList != null && organizationInfoList.size() > 0) {
//                        activityInfo.setPersonnelLimits(1);
//                    }
//                    presenter.saveActive(userId, orgainId, activityInfo);
//
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (isFinishing()) {
//            return;
//        }
//
//        if (apiName.equals(InterfaceUrl.URL_SAVEACTIVE)) {
//            if (object instanceof SaveActiveResponse) {
//                final SaveActiveResponse saveActiveResponse = (SaveActiveResponse) object;
//                int code = saveActiveResponse.getCode();
//                if (code == 200) {
//                    //发起活动成功
//                    ActiveSuccessTipDialog dialog = new ActiveSuccessTipDialog();
//                    dialog.setItemText(saveActiveResponse.getMsg(), "发送邀请函");
//                    dialog.setComeInType(comeInType);
//                    dialog.setGravity(Gravity.LEFT);
//                    dialog.setItemClick(new ActiveSuccessTipDialog.ItemClick() {
//                        @Override
//                        public void itemClick() {
//                            Intent intent = new Intent(BootUpActivity.this, SendInviationSelectConditionActivity.class);
//                            intent.putExtra(Constants.Fields.ACTIVITY_ID, saveActiveResponse.getActivityId());
//                            intent.putExtra("comeInType", comeInType);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                    dialog.show(getSupportFragmentManager());
//                    EventBus.getDefault().post(new UpdateActiveListEvent(1));
//                    EventBus.getDefault().post(new UpdateActiveListEvent(3));
//                    //上传图片
//                    if (adapter.getList() != null && adapter.getList().size() > 0) {
//                        for (int i = 0; i < adapter.getList().size(); i++) {
//                            Image image = adapter.getList().get(i);
//                            imagePresenter.uploadImages(saveActiveResponse.getActivityId(), 4, userId, getFormatDataString(new java.util.Date(), DateUtils.DATE_FORMAT_2), image.getImageUrl(), 0, i + 1, adapter.getList().size());
//                        }
//                    }
//
//                } else {
//                    Utils.showToastShortTime(saveActiveResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_UPDATEACTIVE)) {
//            if (object instanceof NoParameterResponse) {
//                NoParameterResponse noParameterResponse = (NoParameterResponse) object;
//                int code = noParameterResponse.getCode();
//                if (code == 200) {
//                    //修改活动成功
//                    EventBus.getDefault().post(new UpdateActiveDetailEvent());
//                    EventBus.getDefault().post(new UpdateActiveListEvent(1));
//                    finish();
//                    //上传图片
//                    if (adapter.getUploadList() != null && adapter.getList().size() > 0) {
//                        for (int i = 0; i < adapter.getList().size(); i++) {
//                            Image image = adapter.getList().get(i);
//                            if (image.getYesOrNoUpload() == 1) {
//                                //新加的图片
//                                imagePresenter.uploadImages(activityInfo.getActivityId(), 4, userId, getFormatDataString(new java.util.Date(), DateUtils.DATE_FORMAT_2), image.getImageUrl(), 0, i + 1, adapter.getList().size());
//                            } else {
//                                //之前的图片
//                                imagePresenter.uploadImages(activityInfo.getActivityId(), 4, userId, getFormatDataString(new java.util.Date(), DateUtils.DATE_FORMAT_2), null, image.getImageId(), i + 1, adapter.getList().size());
//                            }
//                        }
//                    }
//                } else {
//                    Utils.showToastShortTime(noParameterResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_GETACTIVETYPELIST)) {
//            if (object instanceof GetActiveTypeListResponse) {
//                GetActiveTypeListResponse getActiveTypeListResponse = (GetActiveTypeListResponse) object;
//                int code = getActiveTypeListResponse.getCode();
//                if (code == 200) {
//                    List<ActivityNumber> activityNumberList = getActiveTypeListResponse.getPersonList();
//                    if (activityNumberList != null) {
//                        for (int i = 0; i < activityNumberList.size(); i++) {
//                            options2Items_01.add(activityNumberList.get(i).getNumberDesc());
//                        }
//                        OptionsPickerView pvOptions = new OptionsPickerView.Builder(BootUpActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
//                            @Override
//                            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                                //返回的分别是三个级别的选中位置
//                                String tx = options2Items_01.get(options1);
//                                usernum_tv.setText("【" + tx + "】");
//                            }
//                        })
//                                .setSubmitColor(R.color.color_333333)//确定按钮文字颜色
//                                .setCancelColor(R.color.color_999999)//取消按钮文字颜色
////                                .setTitleBgColor(R.color.color_e6e6e6)//标题背景颜色 Night mode
//                                .build();
//                        pvOptions.setPicker(options2Items_01);
//                        pvOptions.setSelectOptions(options2Items_01.size() / 2);
//                        pvOptions.show();
//                    }
//
//                } else {
//                    Utils.showToastShortTime(getActiveTypeListResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_DELETEIMAGE)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                int code = baseResponse.getCode();
//                if (code == 200) {
//                    //删除图片成功
//                    adapter.removeItem(deletePosition);
//                    maxSelectNum++;
//                    if (maxSelectNum > 0) {
//                        addImgLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        addImgLayout.setVisibility(View.GONE);
//                    }
//                    imgnum_tv.setText((4 - maxSelectNum) + "/" + 4);
//                } else {
//                    Utils.showToastShortTime(baseResponse.getMsg());
//                }
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
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case PictureConfig.CHOOSE_REQUEST:
//                    // 图片选择结果回调
//                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    if (list_img != null) {
//                        list_img.clear();
//                    }
//                    for (LocalMedia info : selectList) {
//                        Image image = new Image();
//                        image.setImageUrl(info.getPath());
//                        image.setThumbnailUrl(info.getCompressPath());
////                        image.setPhotographTime(DateUtils.getFormatDataString(info.getAddedTime()));
//                        if (updateActivityType == 2) {
//                            image.setYesOrNoUpload(1);
//                        }
//                        list_img.add(image);
//                        maxSelectNum--;
//                    }
//                    adapter.addList(list_img);
//                    if (maxSelectNum > 0) {
//                        addImgLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        addImgLayout.setVisibility(View.GONE);
//                    }
//                    imgnum_tv.setText((4 - maxSelectNum) + "/" + 4);
//                    selectList.clear();
//                    break;
//                case 1:
//                    activityLocationInfo = data.getParcelableExtra("location");
//                    place_tv.setText("【" + activityLocationInfo.getAddr() + "】");
//                    break;
//
//            }
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (presenter != null) {
//            presenter.onDestroy();
//        }
//        presenter = null;
//
//        if (imagePresenter != null) {
//            imagePresenter.onDestroy();
//        }
//        imagePresenter = null;
//
//        if (addImgLayout != null) {
//            addImgLayout.removeAllViews();
//        }
//        addImgLayout = null;
//
//        if (activityInfo != null) {
//            activityInfo = null;
//        }
//
//        if (hobbyInfoList != null) {
//            hobbyInfoList.clear();
//        }
//        hobbyInfoList = null;
//
//        if (organizationInfoList != null) {
//            organizationInfoList.clear();
//        }
//        organizationInfoList = null;
//
//        if (themeEdit != null){
//            themeEdit = null;
//        }
//
//        if (list_img != null) {
//            list_img.clear();
//        }
//        list_img = null;
//
//        if (optionalLayout != null) {
//            optionalLayout.removeAllViews();
//        }
//        optionalLayout = null;
//
//        if (time_tv != null) {
//            time_tv = null ;
//        }
//
//        if (place_tv != null) {
//            place_tv = null ;
//        }
//
//        if (usernum_tv != null) {
//            usernum_tv = null ;
//        }
//
//        if (charge_tv != null) {
//            charge_tv = null ;
//        }
//
//        if (imgnum_tv != null) {
//            imgnum_tv = null ;
//        }
//
//        if (addImgTextView != null) {
//            addImgTextView = null ;
//        }
//
//        if (tv_num_tv != null) {
//            tv_num_tv = null ;
//        }
//
//        if (recyclerView != null) {
//            recyclerView.removeAllViews();
//        }
//        recyclerView = null;
//
//        if (adapter != null) {
//            if (adapter.getItemCount() > 0) {
//                adapter.getList().clear();
//            }
//            adapter = null;
//        }
//
//        if (options2Items_01 != null) {
//            options2Items_01.clear();
//        }
//        options2Items_01 = null;
//
//        if (titleFragment != null) {
//            titleFragment = null;
//        }
//
//        if (activityLocationInfo != null) {
//            activityLocationInfo = null;
//        }
//
//        if (hobbyRecyclerViewAdapter != null) {
//            if (hobbyRecyclerViewAdapter.getItemCount() > 0) {
//                hobbyRecyclerViewAdapter.getList().clear();
//            }
//            hobbyRecyclerViewAdapter = null;
//        }
//
//        if (hobbyRecycleView != null) {
//            hobbyRecycleView.removeAllViews();
//        }
//        hobbyRecycleView = null;
//
//
//    }
//
//
//}
