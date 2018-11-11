package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.LoginEvent;
import cn.bjhdltcdn.p2plive.httpresponse.CheckPhoneNumberResponse;
import cn.bjhdltcdn.p2plive.httpresponse.ForgetPasswordResponse;
import cn.bjhdltcdn.p2plive.httpresponse.LoginByThirdPartyResponse;
import cn.bjhdltcdn.p2plive.httpresponse.LoginResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.mvp.contract.LoginContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

public class LoginPresenter implements LoginContract.Presenter, PlatformActionListener {

    private BaseView mView;

    public LoginPresenter(BaseView mView) {
        this.mView = mView;
    }

    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private Platform plat;

    @Override
    public void cancelTag(String tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    @Override
    public void onDestroy() {
        if (mView != null) {
            mView = null;
        }
    }

    /**
     * shareSdk授权
     *
     * @param name
     */
    public void authorize(String name) {
        plat = ShareSDK.getPlatform(name);
        plat.setPlatformActionListener(this);
        // plat.SSOSetting(true);
        plat.removeAccount(true);
        plat.showUser(null);
        ShareSDK.removeCookieOnAuthorize(true);
    }

    @Override
    public void loginByPhone(String userName, final String password, final int userType) {
        Map map = new LinkedHashMap(1);
        map.put("userName", userName);
        map.put("password", password);
        map.put("userType", userType);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_LOGINBYPHONE, tag, map, new JsonCallback<LoginResponse>(LoginResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<LoginResponse> object) {

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_LOGINBYPHONE, object.body());
                    }

                    if (object.body() instanceof LoginResponse) {
                        final LoginResponse response = object.body();
                        if (response.getCode() == 200) {
                            // 连接融云sdk
                            RongIMutils.connect(response.getToken());
                            final BaseUser baseUser = Utils.userToBaseUser(response.getUser());

                            // 保存用户基本信息
                            GreenDaoUtils.getInstance().insertBaseUser(baseUser);

                            SafeSharePreferenceUtils.saveString(Constants.KEY.KEY_SYS_TOKEN, response.getSysToken());
                            SafeSharePreferenceUtils.saveString(Constants.KEY.KEY_SIGNATURE, response.getUser().getSignature());
                            SafeSharePreferenceUtils.saveLong(Constants.Fields.USER_ID, response.getUser().getUserId());
                            SafeSharePreferenceUtils.saveString(Constants.Fields.PHONE_NUMBER, response.getUser().getUserName());
                            SafeSharePreferenceUtils.saveString(Constants.Fields.PASS_WORD, password);
                            SafeSharePreferenceUtils.saveInt(Constants.Fields.LOGIN_TYPE, userType);
                            SafeSharePreferenceUtils.saveInt(Constants.Fields.AUTHSTATUS, response.getUser().getAuthStatus());

                        }
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_LOGINBYPHONE, response.getException());
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginByThirdParty(String userName, String location, int sex, String userIcon, String birthday, String nickName, final int userType, String uniqueId) {
        Map map = new LinkedHashMap(1);
        map.put("userName", userName);
        map.put("location", location);
        map.put("sex", sex);
        map.put("userIcon", userIcon);
        map.put("birthday", birthday);
        map.put("nickName", nickName);
        map.put("userType", userType);
        map.put("uniqueId", uniqueId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_LOGINBYTHIRDPARTY, tag, map, new JsonCallback<LoginByThirdPartyResponse>(LoginByThirdPartyResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }

                }

                @Override
                public void onSuccess(Response<LoginByThirdPartyResponse> object) {

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_LOGINBYTHIRDPARTY, object.body());
                    }

                    if (object.body() instanceof LoginByThirdPartyResponse) {
                        final LoginByThirdPartyResponse response = object.body();
                        if (response.getCode() == 200) {
                            // 连接融云sdk
                            RongIMutils.connect(response.getToken());

                            // 保存用户基本信息
                            final BaseUser baseUser = Utils.userToBaseUser(response.getUser());
                            GreenDaoUtils.getInstance().insertBaseUser(baseUser);

                            SafeSharePreferenceUtils.saveString(Constants.KEY.KEY_SYS_TOKEN, response.getSysToken());
                            SafeSharePreferenceUtils.saveLong(Constants.Fields.USER_ID, response.getUser().getUserId());
                            SafeSharePreferenceUtils.saveString(Constants.KEY.KEY_SIGNATURE, response.getUser().getSignature());
                            SafeSharePreferenceUtils.saveInt(Constants.Fields.AUTHSTATUS, response.getUser().getAuthStatus());

                        }

                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_LOGINBYTHIRDPARTY, response.body());
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void forgetPassword(String userName, String newPassword, String verificationCode) {
        Map map = new LinkedHashMap(1);
        map.put("userName", userName);
        map.put("newPassword", newPassword);
        map.put("verificationCode", verificationCode);
        String tag = mView.getClass().getSimpleName();
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FORGETPASSWORD, tag, map, new JsonCallback<ForgetPasswordResponse>(ForgetPasswordResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<ForgetPasswordResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FORGETPASSWORD, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FORGETPASSWORD, response.body());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkPhoneNumber(String phoneNumber) {
        Map map = new LinkedHashMap(1);
        map.put("phoneNumber", phoneNumber);
        String tag = mView.getClass().getSimpleName();
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_CHECKPHONENUMBER, tag, map, new JsonCallback<CheckPhoneNumberResponse>(CheckPhoneNumberResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<CheckPhoneNumberResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CHECKPHONENUMBER, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CHECKPHONENUMBER, response.body());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (i == Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();// 获取数平台数据DB
            EventBus.getDefault().post(new LoginEvent(MSG_AUTH_COMPLETE, platDB));

        }

        if (plat != null) {
            plat.setPlatformActionListener(null);
            plat = null;
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

        if (i == Platform.ACTION_USER_INFOR) {
            EventBus.getDefault().post(new LoginEvent(MSG_AUTH_ERROR, throwable));
        }
        if (plat != null) {
            plat.setPlatformActionListener(null);
            plat = null;
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (i == Platform.ACTION_USER_INFOR) {

            EventBus.getDefault().post(new LoginEvent(MSG_AUTH_CANCEL, null));
        }
        if (plat != null) {
            plat.setPlatformActionListener(null);
            plat = null;
        }

    }
}
