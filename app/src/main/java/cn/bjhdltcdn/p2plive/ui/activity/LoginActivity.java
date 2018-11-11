package cn.bjhdltcdn.p2plive.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.LoginEvent;
import cn.bjhdltcdn.p2plive.httpresponse.LoginByThirdPartyResponse;
import cn.bjhdltcdn.p2plive.httpresponse.LoginResponse;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.LoginPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.PermissionApplicationUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static com.mob.MobSDK.getContext;

public class LoginActivity extends AppCompatActivity implements BaseView {
    private LoginPresenter loginPresenter;
    private TextView tv_register, tv_forgetpassword, tv_agree, tv_login;
    private ImageView img_qq, img_wechat;
    private EditText edit_phone, edit_password;
    private int loginType = 0;//1手机号，2 QQ 3 微信，
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private PermissionApplicationUtils permissionApplicationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_login);
        initView();
        loginPresenter = new LoginPresenter(this);
        permissionApplicationUtils = new PermissionApplicationUtils();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        initView();

        loginPresenter = new LoginPresenter(this);

    }

    public void initView() {
        tv_forgetpassword = findViewById(R.id.tv_forgetpassword);
        tv_register = findViewById(R.id.tv_register);
        tv_agree = findViewById(R.id.tv_agree);
        tv_login = findViewById(R.id.tv_login);
        img_qq = findViewById(R.id.img_qq);
        img_wechat = findViewById(R.id.img_wechat);
        edit_phone = findViewById(R.id.edit_phone);
        edit_password = findViewById(R.id.edit_password);
        String phoneStr = SafeSharePreferenceUtils.getString(Constants.Fields.PHONE_NUMBER, "");
        if (!TextUtils.isEmpty(phoneStr)) {
            edit_phone.setText(phoneStr);
            tv_login.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
        }
        String pwdStr = SafeSharePreferenceUtils.getString(Constants.Fields.PASS_WORD, "");
        if (!TextUtils.isEmpty(pwdStr)) {
            edit_password.setText(pwdStr);
        }

        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, WXPayEntryActivity.class);
                intent.putExtra(Constants.Action.ACTION_BROWSE, 1);
                startActivity(intent);
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        tv_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳到忘记密码界面
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType = 1;
                permissionApplicationUtils.checkPermission(LoginActivity.this, getPermissionCallBack());
            }
        });
        img_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType = 2;
                permissionApplicationUtils.checkPermission(LoginActivity.this, getPermissionCallBack());
            }
        });

        img_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType = 3;
                permissionApplicationUtils.checkPermission(LoginActivity.this, getPermissionCallBack());
            }
        });
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tv_login.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 第三方登陆数据回调
     * 注：commplete执行两次，第一次授权，thirdId为空；
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThirdLoginEvent(LoginEvent event) {
        if (event == null) {
            return;
        }
        int msgCode = event.getMsgCode();
        Object obj = event.getObj();
        switch (msgCode) {
            case MSG_AUTH_COMPLETE:
                PlatformDb res = (PlatformDb) obj;
                String thirdId = null;
                if (loginType == 3) {
                    thirdId = res.get("unionid");//微信登陆获取
                } else if (loginType == 2) {
                    thirdId = res.getUserId(); //QQ
                }
                String nickName = res.getUserName();//昵称
                String userIcon = res.getUserIcon();// 头像链接
                String province = res.get("province");
                String city = res.get("city");
                if (!TextUtils.isEmpty(thirdId)) {
                    //用于自动登录
                    SafeSharePreferenceUtils.saveString(Constants.Fields.THIRD_ID, thirdId);
                    loginPresenter.loginByThirdParty(thirdId, province + city, 0, userIcon, "", nickName, loginType, thirdId);
                }
                break;
            case MSG_AUTH_CANCEL:
                Utils.showToastShortTime("取消操作");
                break;
            case MSG_AUTH_ERROR:
                String expName = obj.getClass().getSimpleName();
                if ("WechatClientNotExistException".equals(expName)
                        || "WechatTimelineNotSupportedException".equals(expName)
                        || "WechatFavoriteNotSupportedException".equals(expName)) {
                    Utils.showToastShortTime("请安装微信客户端");
                } else {
                    Utils.showToastShortTime("获取信息失败，请重新登陆");

                }
                break;
            default:
        }
    }


    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);

            return;
        }

        switch (apiName) {
            case InterfaceUrl.URL_LOGINBYPHONE:
                if (object instanceof LoginResponse) {
                    final LoginResponse response = (LoginResponse) object;
                    if (response.getCode() == 200) {
                        SafeSharePreferenceUtils.saveInt(Constants.Fields.LOGIN_TYPE, loginType);
                        User user = response.getUser();
                        if (user != null) {
                            //兴趣爱好必选
                            if (TextUtils.isEmpty(user.getBirthday()) || user.getOccupationInfo() == null ||
                                    user.getSchoolInfo() == null) {
                                Intent intent = new Intent(LoginActivity.this, CompleteInfoActivity.class);
                                intent.putExtra(Constants.Fields.USER_ID, user.getUserId());
                                intent.putExtra(Constants.Fields.USER_ICON, user.getUserIcon());
                                intent.putExtra(Constants.Fields.NICK_NAME, user.getNickName());
                                intent.putExtra(Constants.Fields.SEX, user.getSex());
                                intent.putExtra(Constants.Fields.BIRTHDAY, user.getBirthday());
                                intent.putExtra(Constants.Fields.OCCUPATION_INFO, user.getOccupationInfo());
                                intent.putExtra(Constants.Fields.SCHOOL_INFO, user.getSchoolInfo());
                                startActivity(intent);
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            hideLoading();
                            MobclickAgent.onProfileSignIn("Phone", response.getUser().getUserId() + "");

                            finish();
                        }
                    } else {
                        hideLoading();
                        Utils.showToastShortTime(response.getMsg());
                    }
                }

                break;
            case InterfaceUrl.URL_LOGINBYTHIRDPARTY:
                if (object instanceof LoginByThirdPartyResponse) {
                    final LoginByThirdPartyResponse response = (LoginByThirdPartyResponse) object;
                    if (response.getCode() == 200) {
                        SafeSharePreferenceUtils.saveInt(Constants.Fields.LOGIN_TYPE, loginType);
                        //兴趣爱好必选
                        User user = response.getUser();
                        if (response.getUser() != null) {
                            if (TextUtils.isEmpty(user.getPhoneNumber())) {
                                Intent intent = new Intent(LoginActivity.this, BindPhoneNumberActivity.class);
                                intent.putExtra(Constants.Fields.EXTRA, user);
                                startActivity(intent);
                            } else if (TextUtils.isEmpty(user.getBirthday()) || user.getOccupationInfo() == null ||
                                    user.getSchoolInfo() == null) {
                                Intent intent = new Intent(LoginActivity.this, CompleteInfoActivity.class);
                                intent.putExtra(Constants.Fields.USER_ID, user.getUserId());
                                intent.putExtra(Constants.Fields.USER_ICON, user.getUserIcon());
                                intent.putExtra(Constants.Fields.NICK_NAME, user.getNickName());
                                intent.putExtra(Constants.Fields.SEX, user.getSex());
                                intent.putExtra(Constants.Fields.BIRTHDAY, user.getBirthday());
                                intent.putExtra(Constants.Fields.OCCUPATION_INFO, user.getOccupationInfo());
                                intent.putExtra(Constants.Fields.SCHOOL_INFO, user.getSchoolInfo());
                                startActivity(intent);
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            hideLoading();
                            //1手机号，2 QQ 3 微信
                            MobclickAgent.onProfileSignIn(loginType == 2 ? "QQ" : "WX", response.getUser().getUserId() + "");

                            finish();
                        }
                    } else {
                        hideLoading();
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
            default:
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


    public void showPermissionDialog() {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("权限申请")
                .setMessage("未得到用户授权，软件无法使用")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionApplicationUtils.permissionResult(requestCode, permissions, grantResults, getPermissionCallBack());
    }

    @NonNull
    private PermissionApplicationUtils.IperssionCallBack getPermissionCallBack() {
        return new PermissionApplicationUtils.IperssionCallBack() {
            @Override
            public void isSuccess() {
                if (loginType == 1) {
                    String phoneNumber = edit_phone.getText().toString();
                    String passWord = edit_password.getText().toString();
                    if (TextUtils.isEmpty(phoneNumber)) {
                        Utils.showToastShortTime("手机号为空");
                    } else if (!StringUtils.isFormatedPhoneNumber(phoneNumber)) {
                        Utils.showToastShortTime("手机号输入错误");
                    } else if (TextUtils.isEmpty(passWord)) {
                        Utils.showToastShortTime("密码为空");
                    } else if (!StringUtils.isFormatedPassword(passWord)) {
                        Utils.showToastShortTime("密码是由6-20位字母,数字和符号组成");
                    } else {
                        loginPresenter.loginByPhone(phoneNumber, passWord, loginType);
                    }
                } else if (loginType == 3) {
                    loginPresenter.authorize(Wechat.NAME);
                } else if (loginType == 2) {
                    loginPresenter.authorize(QQ.NAME);
                }
            }

            @Override
            public void isFalure() {
                if (!LoginActivity.this.isFinishing()) {
                    showPermissionDialog();
                }
            }
        };
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        try {
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
