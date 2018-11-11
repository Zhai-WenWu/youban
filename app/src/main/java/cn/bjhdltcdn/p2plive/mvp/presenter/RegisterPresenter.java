package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetVerificationCodeResponse;
import cn.bjhdltcdn.p2plive.httpresponse.RegisterResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.mvp.contract.RegisterContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;


public class RegisterPresenter implements RegisterContract.Presenter {

    private BaseView mView;

    public RegisterPresenter(BaseView mView) {
        this.mView = mView;
    }


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

    @Override
    public void register(String userName, final String password, String verificationCode) {
        Map map = new LinkedHashMap(1);
        map.put("userName", userName);
        map.put("password", password);
        map.put("verificationCode", verificationCode);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_REGISTER, tag, map, new JsonCallback<RegisterResponse>(RegisterResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }


                }

                @Override
                public void onSuccess(Response<RegisterResponse> response) {


                    if (response.body() instanceof RegisterResponse) {
                        final RegisterResponse registerResponse = (RegisterResponse) response.body();
                        if (registerResponse.getCode() == 200) {

                            // 连接融云sdk
                            RongIMutils.connect(registerResponse.getToken());
                            final BaseUser baseUser = Utils.userToBaseUser(registerResponse.getUser());

                            // 保存用户基本信息
                            GreenDaoUtils.getInstance().insertBaseUser(baseUser);

                            SafeSharePreferenceUtils.saveString(Constants.KEY.KEY_SYS_TOKEN, registerResponse.getSysToken());
                            SafeSharePreferenceUtils.saveString(Constants.KEY.KEY_SIGNATURE, registerResponse.getUser().getSignature());
                            SafeSharePreferenceUtils.saveLong(Constants.Fields.USER_ID, registerResponse.getUser().getUserId());
                            SafeSharePreferenceUtils.saveString(Constants.Fields.PHONE_NUMBER, registerResponse.getUser().getUserName());
                            SafeSharePreferenceUtils.saveString(Constants.Fields.PASS_WORD, password);
                            SafeSharePreferenceUtils.saveInt(Constants.Fields.LOGIN_TYPE, registerResponse.getUser().getUserType());

                        }
                    }

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_REGISTER, response.body());
                    }


                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_REGISTER, response.body());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindPhoneNumber(long userId, String phoneNumber, String verificationCode) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PHONE_NUMBER, phoneNumber);
        map.put(Constants.Fields.VERIFICATION_CODE, verificationCode);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_BINDPHONENUMBER, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }


                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_BINDPHONENUMBER, response.body());
                    }


                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_BINDPHONENUMBER, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVerificationCode(String phoneNumber) {
        Map map = new LinkedHashMap(1);
        map.put("phoneNumber", phoneNumber);
        String tag = mView.getClass().getSimpleName();
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETVERIFICATIONCODE, tag, map, new JsonCallback<GetVerificationCodeResponse>(GetVerificationCodeResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetVerificationCodeResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETVERIFICATIONCODE, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETVERIFICATIONCODE, response.body());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
