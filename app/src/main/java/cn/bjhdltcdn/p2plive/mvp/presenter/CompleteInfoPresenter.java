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
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.ChangedUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHobbyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOccupationListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SearchSchoolListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserLocationResponse;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.contract.CompleteInfoContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public class CompleteInfoPresenter implements CompleteInfoContract.Presenter {
    private BaseView mView;

    public CompleteInfoPresenter(BaseView mView) {
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
    public void changedUserInfo(User user) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER, user);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_CHANGEDUSERINFO, tag, map, new JsonCallback<ChangedUserInfoResponse>(ChangedUserInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<ChangedUserInfoResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CHANGEDUSERINFO, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CHANGEDUSERINFO, response.body());
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
    public void getHobbyList(long userId, int moduleType) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.MODULE_TYPE, moduleType);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETHOBBYLIST, tag, map, new JsonCallback<GetHobbyListResponse>(GetHobbyListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetHobbyListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOBBYLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOBBYLIST, response.body());
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
    public void getOccupationList(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETOCCUPATIONLIST, tag, map, new JsonCallback<GetOccupationListResponse>(GetOccupationListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetOccupationListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETOCCUPATIONLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETOCCUPATIONLIST, response.body());
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
    public void ResetPassword(long userId, String oldPassword, String newPassword) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.OLDPWD, oldPassword);
        map.put(Constants.Fields.NEWPWD, newPassword);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_RESETPASSWORD, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {
                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_RESETPASSWORD, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_RESETPASSWORD, response.body());
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
    public void searchSchoolList(String content, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SEARCHSCHOOLLIST, tag, map, new JsonCallback<SearchSchoolListResponse>(SearchSchoolListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<SearchSchoolListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SEARCHSCHOOLLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SEARCHSCHOOLLIST, response.body());
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
    public void updateUserLocation(long userId, String longitude, String latitude, String province, String city, String district, String add) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.LONGITUDE, longitude);
        map.put(Constants.Fields.LATITUDE, latitude);
        map.put(Constants.Fields.PROVINCE, province);
        map.put(Constants.Fields.CITY, city);
        map.put(Constants.Fields.DISTRICT, district);
        map.put(Constants.Fields.ADD, add);

        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEUSERLOCATION, tag, map, new JsonCallback<UpdateUserLocationResponse>(UpdateUserLocationResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<UpdateUserLocationResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEUSERLOCATION, response);
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEUSERLOCATION, response);
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
}
