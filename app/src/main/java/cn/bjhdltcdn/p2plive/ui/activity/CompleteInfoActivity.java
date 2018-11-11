package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.ChangedUserInfoResponse;
import cn.bjhdltcdn.p2plive.model.OccupationInfo;
import cn.bjhdltcdn.p2plive.model.SchoolInfo;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.CompleteInfoPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.SexSelectDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bjhdltcdn.p2plive.utils.DateUtils.getAge;
import static cn.bjhdltcdn.p2plive.utils.DateUtils.getFormatDataString;

/**
 * Created by ZHUDI on 2017/11/15.
 * 注册后完成资料
 */

public class CompleteInfoActivity extends BaseActivity implements BaseView, View.OnClickListener {
    private TextView tv_icon;
    private CircleImageView img_icon;
    private EditText edit_nickname;
    private CardView card_sex_women, card_sex_man;
    private ImageView img_select_women, img_select_man;
    private RelativeLayout rela_birth, rela_work, rela_school;
    private TextView tv_birth, tv_work, tv_school, tv_finish;
    private OccupationInfo occupationInfo;
    private SchoolInfo schoolInfo;
    private String birthStr;
    private int sex = 0;//1 女 2 男
    private CompleteInfoPresenter completeInfoPresenter;

    public CompleteInfoPresenter getCompleteInfoPresenter() {
        if (completeInfoPresenter == null) {
            completeInfoPresenter = new CompleteInfoPresenter(this);
        }
        return completeInfoPresenter;
    }

    private UserPresenter userPresenter;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    private ImagePresenter imagePresenter;

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    // 选择的图片路径
    private String selectFileUrl;
    private long userId;
    private String nickName;//第三方登录传过来
    private final int INTENT_OCCUPATION_CODE = 2, INTENT_SCHOOL_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeinfo);
        userId = getIntent().getLongExtra(Constants.Fields.USER_ID, 0);
        selectFileUrl = getIntent().getStringExtra(Constants.Fields.USER_ICON);
        nickName = getIntent().getStringExtra(Constants.Fields.NICK_NAME);
        sex = getIntent().getIntExtra(Constants.Fields.SEX, 0);
        birthStr = getIntent().getStringExtra(Constants.Fields.BIRTHDAY);
        occupationInfo = getIntent().getParcelableExtra(Constants.Fields.OCCUPATION_INFO);
        schoolInfo = getIntent().getParcelableExtra(Constants.Fields.SCHOOL_INFO);
        setTitle();
        init();
    }

    private void init() {
        tv_icon = findViewById(R.id.tv_icon);
        img_icon = findViewById(R.id.img_icon);
        edit_nickname = findViewById(R.id.edit_nickname);
        card_sex_women = findViewById(R.id.card_sex_women);
        card_sex_man = findViewById(R.id.card_sex_man);
        img_select_women = findViewById(R.id.img_select_women);
        img_select_man = findViewById(R.id.img_select_man);
        rela_birth = findViewById(R.id.rela_birth);
        rela_work = findViewById(R.id.rela_work);
        rela_school = findViewById(R.id.rela_school);
        tv_birth = findViewById(R.id.tv_birth);
        tv_work = findViewById(R.id.tv_work);
        tv_school = findViewById(R.id.tv_school);
        tv_finish = findViewById(R.id.tv_finish);
        if (!TextUtils.isEmpty(selectFileUrl)) {
            tv_icon.setVisibility(View.INVISIBLE);
            img_icon.setVisibility(View.VISIBLE);
            Utils.ImageViewDisplayByUrl(selectFileUrl, img_icon);
        }
        if (!TextUtils.isEmpty(nickName)) {
            edit_nickname.setText(nickName);
        }
        if (sex == 1) {
            img_select_women.setVisibility(View.GONE);
            img_select_man.setVisibility(View.VISIBLE);
        } else if (sex == 2) {
            img_select_women.setVisibility(View.VISIBLE);
            img_select_man.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(birthStr)) {
            Date date = DateUtils.getFromatDate(birthStr, DateUtils.DATE_FORMAT_1);
            try {
                if (getAge(date) <= 0) {
                    tv_birth.setText("0岁");
                } else {
                    tv_birth.setText(getAge(date) + "岁");
                }
                buttonHightColor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (occupationInfo != null) {
            tv_work.setText(occupationInfo.getOccupationName());
        }
        if (schoolInfo != null) {
            tv_school.setText(schoolInfo.getSchoolName());
        }
        tv_icon.setOnClickListener(this);
        img_icon.setOnClickListener(this);
        card_sex_women.setOnClickListener(this);
        card_sex_man.setOnClickListener(this);
        rela_birth.setOnClickListener(this);
        rela_work.setOnClickListener(this);
        rela_school.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        edit_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonHightColor();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_icon:
            case R.id.img_icon:
                PictureSelector.create(CompleteInfoActivity.this)
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
            case R.id.card_sex_women:
                if (sex == 0) {
                    sex = 2;
                    img_select_women.setVisibility(View.VISIBLE);
                    img_select_man.setVisibility(View.GONE);
                    dialogShow();
                }
                buttonHightColor();
                break;
            case R.id.card_sex_man:
                if (sex == 0) {
                    sex = 1;
                    img_select_women.setVisibility(View.GONE);
                    img_select_man.setVisibility(View.VISIBLE);
                    dialogShow();
                }
                buttonHightColor();
                break;

            case R.id.rela_birth:
                Calendar selectedDate = Calendar.getInstance();//系统当前时间
                Calendar startDate = Calendar.getInstance();
                startDate.set(1950, 0, 1, 0, 0);
                Calendar endDate = Calendar.getInstance();
                endDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                TimePickerView pvTime = new TimePickerView.Builder(CompleteInfoActivity.this, new TimePickerView.OnTimeSelectListener() {


                    @Override
                    public void onTimeSelect(Date date, View v, boolean isCheck) {//选中事件回调
                        birthStr = getFormatDataString(date, DateUtils.DATE_FORMAT_10);
                        try {
                            if (getAge(date) <= 0) {
                                tv_birth.setText("0岁");
                            } else {
                                tv_birth.setText(getAge(date) + "岁");
                            }
                            buttonHightColor();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
//                 .setSubmitColor(R.color.color_333333)//确定按钮文字颜色
//                 .setCancelColor(R.color.color_999999)//取消按钮文字颜色
//                 .setTitleBgColor(R.color.color_e6e6e6)//标题背景颜色 Night mode
                        .setTitleText("出生日期")
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
                intent.putExtra(Constants.Fields.TYPE, INTENT_OCCUPATION_CODE);
                startActivityForResult(intent, INTENT_OCCUPATION_CODE);
                break;
            case R.id.rela_school:
                intent = new Intent(this, SchoolActivity.class);
                intent.putExtra(Constants.Fields.TYPE, INTENT_SCHOOL_CODE);
                startActivityForResult(intent, INTENT_SCHOOL_CODE);
                break;
            case R.id.tv_finish:
                String nameStr = edit_nickname.getText().toString();
                if (TextUtils.isEmpty(selectFileUrl)) {
                    Utils.showToastShortTime("头像不能为空");
                } else if (TextUtils.isEmpty(nameStr)) {
                    Utils.showToastShortTime("昵称不能为空");
                } else if (sex == 0) {
                    Utils.showToastShortTime("性别未选择");
                } else if (TextUtils.isEmpty(birthStr)) {
                    Utils.showToastShortTime("年龄未选择");
                } else if (schoolInfo == null) {
                    Utils.showToastShortTime("学校未选择");
                } else if (occupationInfo == null) {
                    Utils.showToastShortTime("职业未选择");
                } else {
                    User user = new User();
                    user.setUserId(userId);
                    user.setUserIcon(selectFileUrl);
                    user.setNickName(nameStr);
                    user.setSex(sex);
                    user.setBirthday(birthStr);
                    user.setSignature("");
                    user.setOccupationInfo(occupationInfo);
                    user.setSchoolInfo(schoolInfo);
                    getCompleteInfoPresenter().changedUserInfo(user);
                    getUserPresenter().saveSchool(user.getUserId(), user.getSchoolInfo().getSchoolId(), user.getSchoolInfo().getSchoolName());
                    // 上传图片
                    if (!StringUtils.isEmpty(user.getUserIcon())) {
                        getImagePresenter().uploadImage(user.getUserId(), 1, user.getUserId(), TimeUtils.date2String(new Date()), user.getUserIcon());
                    }
                }
                break;

        }
    }

    /**
     * 完成按钮高亮
     */
    private void buttonHightColor() {
        if (!TextUtils.isEmpty(selectFileUrl) && !TextUtils.isEmpty(edit_nickname.getText().toString()) && sex != 0
                && !TextUtils.isEmpty(birthStr) && schoolInfo != null && occupationInfo != null) {
            tv_finish.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
        } else {
            tv_finish.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_e6e6e6));
        }
    }

    private void dialogShow() {
        SexSelectDialog sexSelectDialog = new SexSelectDialog();
        sexSelectDialog.setItemClick(new SexSelectDialog.ItemClick() {
            @Override
            public void itemClick() {
                sex = 0;
                img_select_women.setVisibility(View.GONE);
                img_select_man.setVisibility(View.GONE);
            }
        });
        sexSelectDialog.show(getSupportFragmentManager());
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_CHANGEDUSERINFO:
                if (object instanceof ChangedUserInfoResponse) {
                    ChangedUserInfoResponse changedUserInfoResponse = (ChangedUserInfoResponse) object;
                    if (changedUserInfoResponse.getCode() == 200) {
                        startActivity(new Intent(CompleteInfoActivity.this, MainActivity.class));
                        finish();
                    }
                }
                break;
        }
    }

    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_complete_info);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
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
                            tv_icon.setVisibility(View.INVISIBLE);
                            img_icon.setVisibility(View.VISIBLE);
                            Utils.ImageViewDisplayByUrl(selectFileUrl, img_icon);
                            buttonHightColor();
                        }
                    }
                    break;
                case INTENT_OCCUPATION_CODE:
                    occupationInfo = data.getParcelableExtra(Constants.KEY.KEY_OBJECT);
                    tv_work.setText(occupationInfo.getOccupationName());
                    buttonHightColor();
                    break;
                case INTENT_SCHOOL_CODE:
                    schoolInfo = data.getParcelableExtra(Constants.KEY.KEY_OBJECT);
                    tv_school.setText(schoolInfo.getSchoolName());
                    buttonHightColor();
                    break;
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

}
