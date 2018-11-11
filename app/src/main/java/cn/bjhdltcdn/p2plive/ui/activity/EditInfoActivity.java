package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.ImageUploaderEvent;
import cn.bjhdltcdn.p2plive.event.UserAgeEvent;
import cn.bjhdltcdn.p2plive.event.UserIconEvent;
import cn.bjhdltcdn.p2plive.event.UserNameEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.ChangedUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.OccupationIndexInfo;
import cn.bjhdltcdn.p2plive.model.OccupationInfo;
import cn.bjhdltcdn.p2plive.model.SchoolInfo;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.CompleteInfoPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.SaveActivePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ImageAlbumFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
import cn.bjhdltcdn.p2plive.widget.tagview.TagContainerLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bjhdltcdn.p2plive.utils.DateUtils.getFormatDataString;

/**
 * Created by ZHUDI on 2017/11/15.
 * 编辑个人资料
 */

public class EditInfoActivity extends BaseActivity implements BaseView, View.OnClickListener {
    private EditText edit_nickname, edit_sign;
    private TextView radio_sex_man, radio_sex_women;
    private RelativeLayout linear_sign, tv_hobby, rg_sex;
    private CircleImageView img_icon;
    private RecyclerView rvPhoto, recycler_hobby;
    private View view_hobby;
    private RelativeLayout rela_birth, rela_work, rela_school, rl_photos;
    private TextView tv_birth, tv_work, tv_school, tv_finish, tv_phptp_num;
    private int sex = 0;//1 女 2 男
    private CompleteInfoPresenter completeInfoPresenter;
    private ImagePresenter imagePresenter;
    private TagContainerLayout tagContainerLayout;
    private User currentUser;
    // 选择的图片路径
    private String selectFileUrl;
    private final int INTENT_HOBBY_CODE = 1, INTENT_OCCUPATION_CODE = 2, INTENT_SCHOOL_CODE = 3, INTENT_PHOTO_CODE = 4;
    private List<HobbyInfo> list_hobby;//选择的兴趣爱好列表
    private OccupationInfo occupationInfo;
    private SchoolInfo schoolInfo;
    private SaveActivePresenter saveActivePresenter;
    private int lastUploadPosition = -1;

    private int selectPosition = -1;
    private UserPresenter userPresenter;
    private TitleFragment titleFragment;
    private int serviceSize;
    private String nameStr;
    private int hobbyListNeedReq = 1;
    private boolean myFragmentNeedRefresh = false;
    private FrameLayout fl_photo;
    private ImageAlbumFragment mFragment;
    /**
     * 匿名view
     */
    private RelativeLayout anonymousLayoutView;
    private EditText anonymousView;
    /**
     * 旧的匿名名称
     */
    private String oldAnonymousName;

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public CompleteInfoPresenter getCompleteInfoPresenter() {
        if (completeInfoPresenter == null) {
            completeInfoPresenter = new CompleteInfoPresenter(this);
        }
        return completeInfoPresenter;
    }

    public SaveActivePresenter getSaveActivePresenter() {
        if (saveActivePresenter == null) {
            saveActivePresenter = new SaveActivePresenter(this);
        }
        return saveActivePresenter;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);
        EventBus.getDefault().register(this);
        setTitle();
        init();
        currentUser = getIntent().getParcelableExtra(Constants.Fields.USER);
        if (currentUser == null) {
            getUserPresenter().getUserInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
        } else {
            setData();
        }
    }


    private void init() {
        //recycler_hobby = findViewById(R.id.recycler_hobby);
        img_icon = findViewById(R.id.img_icon);
        tagContainerLayout = findViewById(R.id.tag_container_view);
        edit_nickname = findViewById(R.id.edit_nickname);
        edit_sign = findViewById(R.id.edit_sign);
        radio_sex_man = findViewById(R.id.radio_sex_man);
        rvPhoto = findViewById(R.id.recycler_photo);
        radio_sex_women = findViewById(R.id.radio_sex_women);
        anonymousLayoutView = findViewById(R.id.ll_anonymous);
        anonymousView = findViewById(R.id.et_anonymous);
        rela_birth = findViewById(R.id.rela_birth);
        rela_work = findViewById(R.id.rela_work);
        rela_school = findViewById(R.id.rela_school);
        tv_phptp_num = findViewById(R.id.tv_phptp_num);
        tv_birth = findViewById(R.id.tv_birth);
        tv_work = findViewById(R.id.tv_work);
        tv_school = findViewById(R.id.tv_school);
        tv_finish = findViewById(R.id.tv_finish);
        view_hobby = findViewById(R.id.view_hobby);
        rg_sex = findViewById(R.id.rg_sex);
        fl_photo = findViewById(R.id.fl_photo);
        img_icon.setOnClickListener(this);
        view_hobby.setOnClickListener(this);
        rela_birth.setOnClickListener(this);
        rela_work.setOnClickListener(this);
        rela_school.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        rg_sex.setOnClickListener(this);
        radio_sex_man.setOnClickListener(this);
        radio_sex_women.setOnClickListener(this);
        //recycler_hobby.setOnClickListener(this);
        GridLayoutSpacesItemDecoration gridLayoutSpacesItemDecoration = new GridLayoutSpacesItemDecoration(Utils.dp2px(10), 2, false);


        edit_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleFragment.isAdded()) {
                    if (s.length() > 0) {
                        titleFragment.setRightViewColor(R.color.color_ffb700);
                    } else {
                        titleFragment.setRightViewColor(R.color.color_999999);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void initFragment() {
        mFragment = (ImageAlbumFragment) getSupportFragmentManager().findFragmentById(R.id.fl_photo);
        if (mFragment == null) {
            mFragment = new ImageAlbumFragment();
        }

        mFragment.setOnImageNumChange(new ImageAlbumFragment.OnImageNumChange() {
            @Override
            public void OnImageNum(int num) {
                String rechargeStr = String.format(getResources().getString(R.string.str_num_photo), num);
                tv_phptp_num.setText(rechargeStr);
            }
        });
        mFragment.setImageList(currentUser.getImageList());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.fl_photo);
    }

    /**
     * 填充数据
     */
    private void setData() {
        initFragment();
        String rechargeStr = String.format(getResources().getString(R.string.str_num_photo), currentUser.getImageList().size());
        tv_phptp_num.setText(rechargeStr);
        RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.ic_launcher);
        if (!this.isFinishing()) {
            Glide.with(this).asBitmap().load(currentUser.getUserIcon()).apply(options).into(img_icon);
        }
        edit_nickname.setText(currentUser.getNickName());
        if (currentUser.getSex() == 1) {
            radio_sex_man.setBackground(getResources().getDrawable(R.drawable.shape_round_25_solid_fff69f));
        } else if (currentUser.getSex() == 2) {
            radio_sex_women.setBackground(getResources().getDrawable(R.drawable.shape_round_25_solid_fff69f));
        }
        oldAnonymousName = currentUser.getAnonymityName();
        if (TextUtils.isEmpty(oldAnonymousName)) {
            anonymousLayoutView.setVisibility(View.GONE);
        } else {
            anonymousLayoutView.setVisibility(View.VISIBLE);
            anonymousView.setText(oldAnonymousName);
        }

        String signature = currentUser.getSignature();

        if (!TextUtils.isEmpty(signature) && !signature.equals("null") && !signature.equals("NULL")) {
            edit_sign.setText(currentUser.getSignature());
        }
        if (!TextUtils.isEmpty(currentUser.getBirthday())) {
            tv_birth.setText(currentUser.getBirthday());
        }
        if (currentUser.getOccupationInfo() != null && !TextUtils.isEmpty(currentUser.getOccupationInfo().getOccupationName())) {
            tv_work.setText(currentUser.getOccupationInfo().getOccupationName());
        }
        if (currentUser.getSchoolInfo() != null && !TextUtils.isEmpty(currentUser.getSchoolInfo().getSchoolName())) {
            tv_school.setText(currentUser.getSchoolInfo().getSchoolName());
        }

//        List<HobbyInfo> hobbyList = currentUser.getHobbyList();
//        if (hobbyList != null) {
//            for (int i = 0; i < hobbyList.size(); i++) {
//                HobbyInfo hobbyInfo = hobbyList.get(i);
//                View itemView = View.inflate(App.getInstance(), R.layout.item_list_hobby_user_details, null);
//                TextView textView = itemView.findViewById(R.id.tv_name);
//                ImageView imgIcon = itemView.findViewById(R.id.img_icon);
//                tagContainerLayout.addView(itemView);
//
//                // 名称
//                textView.setText(hobbyInfo.getHobbyName());
//
//                Glide.with(this).load(hobbyList.get(i).getHobbyImg()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(imgIcon);
//
//
//            }
//        }


    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_icon:
                PictureSelector.create(EditInfoActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .glideOverride(Utils.dp2px(110), Utils.dp2px(110))// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .isGif(true)// 是否显示gif图片 true or false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        .openClickSound(true)// 是否开启点击声音 true or false
                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        .cropWH(Utils.dp2px(110), Utils.dp2px(110))// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                        .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                        .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
//            case R.id.view_hobby:
//                intent = new Intent(this, HobbyActivity.class);
//                intent.putExtra(Constants.Fields.TYPE, 2);
//                intent.putExtra(Constants.Fields.HOBBYLISTNEEDREQ, hobbyListNeedReq);
//                intent.putParcelableArrayListExtra(Constants.KEY.KEY_OBJECT, (ArrayList<? extends Parcelable>) list_hobby);
//                startActivityForResult(intent, INTENT_HOBBY_CODE);
//                break;
            case R.id.rela_birth:
                Calendar selectedDate = Calendar.getInstance();//系统当前时间
                Calendar startDate = Calendar.getInstance();
                startDate.set(1950, 0, 1, 0, 0);
                Calendar endDate = Calendar.getInstance();
                endDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                TimePickerView pvTime = new TimePickerView.Builder(EditInfoActivity.this, new TimePickerView.OnTimeSelectListener() {


                    @Override
                    public void onTimeSelect(Date date, View v, boolean isCheck) {//选中事件回调
                        if (isCheck) {
                            tv_birth.setText(getFormatDataString(date, DateUtils.DATE_FORMAT_9));
                        } else {
                            Calendar sc = Calendar.getInstance();
                            sc.setTime(date);
                            tv_birth.setText(getFormatDataString(date, DateUtils.DATE_FORMAT_10));
                            myFragmentNeedRefresh = true;
                        }
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                        .isCenterLabel(false)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setDate(selectedDate)
                        .setRangDate(startDate, endDate)
                        .setShow_Current_Time(false)
                        .setLabel("年", "月", "日", ":00", "分", "秒")//默认设置为年月日时分秒
                        .build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
            case R.id.rela_work:
                intent = new Intent(this, OccupationActivity.class);
                startActivityForResult(intent, INTENT_OCCUPATION_CODE);
                break;
            case R.id.rela_school:

                getUserPresenter().judgeUserSchool(currentUser != null ? currentUser.getUserId() : SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));

                break;
            case R.id.radio_sex_man:
                Utils.showToastShortTime("性别已选定，无法修改");
                break;
            case R.id.radio_sex_women:
                Utils.showToastShortTime("性别已选定，无法修改");
                break;
            default:

        }
    }


    public void finishClick() {
        nameStr = edit_nickname.getText().toString();
        String newAnonymousName = anonymousView.getText().toString();
        if (TextUtils.isEmpty(nameStr)) {
            Utils.showToastShortTime("昵称不能为空");
        } else if (anonymousLayoutView.getVisibility() == View.VISIBLE && TextUtils.isEmpty(newAnonymousName)) {
            Utils.showToastShortTime("匿名名称不能为空");
        } else {
            User user = new User();
            user.setUserId(currentUser.getUserId());
            user.setNickName(nameStr);
            String signStr = edit_sign.getText().toString();
            if (!TextUtils.isEmpty(signStr)) {
                user.setSignature(signStr);
            } else {
                user.setSignature("");
            }

//            if (list_hobby != null && list_hobby.size() > 0) {
//                //hobbyListNeedReq = 1;
//                user.setHobbyList(list_hobby);
//            }
            String birthStr = tv_birth.getText().toString();
            if (!TextUtils.isEmpty(birthStr) && !birthStr.equals(currentUser.getBirthday())) {
                user.setBirthday(birthStr);
            } else {
                user.setBirthday("");
            }
            if (occupationInfo != null) {
                user.setOccupationInfo(occupationInfo);
            }
            OccupationIndexInfo.getInstent().setIndex(-1);
            if (schoolInfo != null) {
                user.setSchoolInfo(schoolInfo);
            }
            if (!TextUtils.isEmpty(newAnonymousName) && !oldAnonymousName.equals(newAnonymousName)) {
                getUserPresenter().updateAnonymityUser(currentUser.getUserId(), newAnonymousName);
            }
            getCompleteInfoPresenter().changedUserInfo(user);
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_CHANGEDUSERINFO:
                if (object instanceof ChangedUserInfoResponse) {
                    final ChangedUserInfoResponse changedUserInfoResponse = (ChangedUserInfoResponse) object;
                    if (changedUserInfoResponse.getCode() == 200) {
                        UserNameEvent userNameEvent = new UserNameEvent();
                        userNameEvent.setUserName(nameStr);
                        EventBus.getDefault().post(userNameEvent);
                        Utils.showToastShortTime("信息修改成功");
                        // 保存用户基本信息
                        GreenDaoUtils.getInstance().insertBaseUser(Utils.userToBaseUser(changedUserInfoResponse.getUser()));
                        // 遍历本地有效图片并且上传
                        if (mFragment.getNewImageList().size() > 0) {
                            EventBus.getDefault().post(new UserAgeEvent());
                            UserIconEvent userIconEvent = new UserIconEvent();
                            EventBus.getDefault().post(userIconEvent);
                            for (int i = 0; i < mFragment.getNewImageList().size(); i++) {
                                Image image = mFragment.getNewImageList().get(i);
                                // 过滤"add"图片
                                if (image.getImageUrl().contains("add")) {
                                    continue;
                                }

                                tv_finish.setClickable(false);

                                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                                //网络图片
                                String imageUrl = null;
                                if (image.getImageId() > 0) {
                                    imageUrl = null;
                                } else {
                                    imageUrl = image.getImageUrl();
                                }
                                getImagePresenter().uploadImages(userId, 2, userId, TimeUtils.date2String(new Date()), imageUrl, image.getImageId(), i + 1, mFragment.getNewImageList().size() - 1);

                            }
                        }
                        finish();

                    }
                }
                break;

            case InterfaceUrl.URL_GETUSERINFO:
                if (object instanceof GetUserInfoResponse) {
                    GetUserInfoResponse getUserInfoResponse = (GetUserInfoResponse) object;
                    if (getUserInfoResponse.getCode() == 200) {
                        currentUser = getUserInfoResponse.getUser();
                        setData();
                    }
                }

                break;

            case InterfaceUrl.URL_JUDGEUSERSCHOOL:

                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {

                        Intent intent = new Intent(this, SchoolActivity.class);
                        startActivityForResult(intent, INTENT_SCHOOL_CODE);

                    } else {

                        Utils.showToastShortTime(response.getMsg());

                    }

                }

                break;

            case InterfaceUrl.URL_SAVESCHOOL:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() != 200) {
                        Utils.showToastShortTime(response.getMsg());
                    }

                }
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageUploaderEvent(ImageUploaderEvent event) {
        if (event != null) {
            Object tag1 = event.getTag();
            if (tag1 instanceof UploadImageResponse) {
                UploadImageResponse tag = (UploadImageResponse) tag1;
                if (event.getPosition() > -1 && lastUploadPosition > -1) {
                    Image image1 = mFragment.getNewImageList().get(lastUploadPosition);
                    if (image1.getOrderNum() == (tag.getImage().getOrderNum())) {
                        Utils.showToastShortTime("图片上传完成");
                        getUserPresenter().getUserInfo(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
                        tv_finish.setClickable(true);
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:// 选择头像
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    if (selectList != null || selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        if (localMedia.isCompressed()) {
                            selectFileUrl = localMedia.getCompressPath();
                            img_icon.setVisibility(View.VISIBLE);
                            RequestOptions options = new RequestOptions().centerCrop();
                            Glide.with(this).asBitmap().load(selectFileUrl).apply(options).into(img_icon);
                            long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                            getImagePresenter().uploadImage(userId, 1, userId, TimeUtils.date2String(new Date()), selectFileUrl);

                        }
                    }
                    break;
//                case INTENT_HOBBY_CODE:
//                    list_hobby = data.getParcelableArrayListExtra(Constants.KEY.KEY_OBJECT);
//                    hobbyListNeedReq = 2;
//                    tagContainerLayout.removeAllViews();
//                    for (int i = 0; i < list_hobby.size(); i++) {
//                        HobbyInfo hobbyInfo = list_hobby.get(i);
//                        View itemView = View.inflate(App.getInstance(), R.layout.item_list_hobby_user_details, null);
//                        TextView textView = itemView.findViewById(R.id.tv_name);
//                        ImageView imgIcon = itemView.findViewById(R.id.img_icon);
//                        tagContainerLayout.addView(itemView);
//
//                        // 名称
//                        textView.setText(hobbyInfo.getHobbyName());
//
//                        Glide.with(this).load(list_hobby.get(i).getHobbyImg()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(imgIcon);
//
//
//                    }
//                    view_hobby.setFocusable(true);
//                    view_hobby.setFocusableInTouchMode(true);
//                    view_hobby.requestFocus();
//                    break;
                case INTENT_OCCUPATION_CODE:
                    occupationInfo = data.getParcelableExtra(Constants.KEY.KEY_OBJECT);
                    tv_work.setText(occupationInfo.getOccupationName());
                    break;
                case INTENT_SCHOOL_CODE:
                    schoolInfo = data.getParcelableExtra(Constants.KEY.KEY_OBJECT);
                    if (schoolInfo != null) {

                        tv_school.setText(schoolInfo.getSchoolName());

                        getUserPresenter().saveSchool(currentUser != null ? currentUser.getUserId() : SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), schoolInfo.getSchoolId(), schoolInfo.getSchoolName());

                    }


                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_complete_info);
        titleFragment.setRightViewTitle("完成", new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                titleFragment.getRightView().setEnabled(false);
                finishClick();
            }
        });
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

}
