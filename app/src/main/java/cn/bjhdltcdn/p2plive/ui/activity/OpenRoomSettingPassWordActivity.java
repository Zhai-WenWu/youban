package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateRoomStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserStatusResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 开启房间设置密码页面
 */
public class OpenRoomSettingPassWordActivity extends BaseActivity implements BaseView {
    private ChatRoomPresenter chatRoomPresenter;
    private ImagePresenter imagePresenter;
    private RoomInfo roomInfo;
    //房间主题
    private String roomTopciName;
    private String selectFileUrl;
    private int type;//1开启房间2进入房间
    private List<Long> keywordList;
    private BaseUser currentBaseUser;
    private TextView nickNameTextView;
    private ImageView bgImageView;
    private CircleImageView userImageView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Utils.ImageViewDisplayByUrl(currentBaseUser.getUserIcon(), bgImageView);
                    // 用户头像
                    Utils.ImageViewDisplayByUrl(currentBaseUser.getUserIcon(), userImageView);
                    // 昵称
                    nickNameTextView.setText(currentBaseUser.getNickName());
                    break;
            }
        }
    };

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        if (type == 1) {
            roomTopciName = getIntent().getStringExtra(Constants.Fields.TITLE);
            selectFileUrl = getIntent().getStringExtra(Constants.Fields.IMAGEPATH);
            keywordList = (List<Long>) getIntent().getSerializableExtra(Constants.Fields.EXTRA);
//        customList = getIntent().getStringArrayListExtra(Constants.KEY_CUSTOM_LIST);
        } else if (type == 2) {
            roomInfo = getIntent().getParcelableExtra(Constants.Fields.ROOMINFO);
        }

        setContentView(R.layout.activity_open_group_video_room_setting_pass_word);
        initView();
    }

    private void initView() {

        // 关闭按钮
        ImageView closeImageView = findViewById(R.id.close_img);
        // 圆形头像
        userImageView = findViewById(R.id.user_imge_view);
        // 模糊背景
        bgImageView = findViewById(R.id.bg_img);
        ImageView bgTopImageView = findViewById(R.id.bg_top_img);
        Rect outRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bgImageView.getLayoutParams();
        params.height = outRect.bottom - outRect.top;
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) bgTopImageView.getLayoutParams();
        params2.height = outRect.bottom - outRect.top;

        // 模糊面板
        Drawable bgTopImgViewDrawable = bgTopImageView.getBackground();
        if (bgTopImgViewDrawable != null) {
            bgTopImgViewDrawable.mutate().setAlpha((int) (255 * 0.69));
        }

        // 昵称控件
        nickNameTextView = findViewById(R.id.nickname_text_view);
        // 密码控件
        final EditText passwordEditText = findViewById(R.id.password_edit_text);

        // 立即进入按钮
        final Button button = findViewById(R.id.btn_view);
        if (type == 1) {
            long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
            GreenDaoUtils.getInstance().getBaseUser(userId, new GreenDaoUtils.ExecuteCallBack() {
                @Override
                public void callBack(Object object) {
                    currentBaseUser = (BaseUser) object;

                    // 背景
//        Glide.with(get).load(baseUser.getUserIcon()).placeholder(R.mipmap.touxiang)
//                .crossFade(1000)
//                .bitmapTransform(new BlurTransformation(getCurrentActivity(), 15, 1))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
//                .into(bgImageView);

                    handler.sendEmptyMessage(1);

                }
            });
        } else if (type == 2) {
            BaseUser baseUser = roomInfo.getBaseUser();
            Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), bgImageView);
            // 用户头像
            Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), userImageView);

            nickNameTextView.setText("主持人：" + baseUser.getNickName());
            TextView tv_tips = findViewById(R.id.tips_view);
            tv_tips.setText("输入密码进入房间页面");
        }


        // 关闭按钮
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    //进入房间按钮不可用
                    button.setEnabled(false);
                    button.setTextColor(getResources().getColor(R.color.white));
                    button.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_80a09f9f));
                } else {
                    //进入房间按钮可用
                    button.setEnabled(true);
                    button.setTextColor(getResources().getColor(R.color.color_333333));
                    button.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 立即进入事件
        final long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mPassword = passwordEditText.getText().toString();

                if (!StringUtils.isFormatedGroupVideoRoomPassword(mPassword)) {
                    Utils.showToastShortTime("请输入6-10位数字或字母");
                } else {
                    if (type == 1) {
                        getChatRoomPresenter().updateRoomStatus(userId, 0, roomTopciName, 0, 1, mPassword, keywordList, null);
                    } else if (type == 2) {
                        getChatRoomPresenter().updateUserStatus(userId, roomInfo.getRoomId(), 0, roomInfo.getPasswordType(),
                                mPassword);
                    }
                }

            }
        });

    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_UPDATEROOMSTATUS:
                if (object instanceof UpdateRoomStatusResponse) {
                    UpdateRoomStatusResponse response = (UpdateRoomStatusResponse) object;
                    if (response.getCode() == 200) {
                        Intent intent = new Intent(this, GroupVideoActivity.class);
                        intent.putExtra(Constants.Fields.ROOMINFO, response.getRoomInfo());
                        getImagePresenter().uploadImage(response.getRoomInfo().getRoomId(), 7, response.getRoomInfo().getToBaseUser().getUserId(), TimeUtils.date2String(new Date()), selectFileUrl);
                        startActivity(intent);
                        finish();
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                        if (response.getCode() == 203) {//未实名认证
                            startActivity(new Intent(this, RealNameCertificationActivity.class));
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATEUSERSTATUS://进入房间
                if (object instanceof UpdateUserStatusResponse) {
                    UpdateUserStatusResponse response = (UpdateUserStatusResponse) object;
                    if (response.getCode() == 200) {
                        Intent intent = new Intent(OpenRoomSettingPassWordActivity.this, GroupVideoActivity.class);
                        intent.putExtra(Constants.Fields.ROOMINFO, response.getRoomInfo());
                        startActivity(intent);
                        finish();
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(this);
            handler = null;
        }
        if (chatRoomPresenter != null) {
            chatRoomPresenter = null;
        }
        if (imagePresenter != null) {
            imagePresenter = null;
        }
        if (keywordList != null) {
            keywordList = null;
        }
    }
}
