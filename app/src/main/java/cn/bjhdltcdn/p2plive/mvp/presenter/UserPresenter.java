package cn.bjhdltcdn.p2plive.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.AnonymousMsgEvent;
import cn.bjhdltcdn.p2plive.event.AttenttionResultEvent;
import cn.bjhdltcdn.p2plive.httpresponse.AttentionStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.CheckIsDeleteCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindTokenByUserIdResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserAuthStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserVideoInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAnonymityFriendListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAnonymityUserResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAttentionOrFollowerListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAuthContentInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetBlackListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetFriendListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetJoinActionListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLetterPropsListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLoginRecommendListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetMyTransactionResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.MyAccountResponse;
import cn.bjhdltcdn.p2plive.httpresponse.MyPostListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.MyPropsListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.QueryUserBalanceResponse;
import cn.bjhdltcdn.p2plive.httpresponse.VersionResponse;
import cn.bjhdltcdn.p2plive.model.ChatBaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.contract.UserContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * UserPresenter 样例
 */

public class UserPresenter implements UserContract.Presenter {

    private BaseView mView;

    public UserPresenter(BaseView mView) {
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
    public void updateUserLocation(long userId, String longitude, String latitude, String province, String city, String district, String addr) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.LONGITUDE, longitude);
        map.put(Constants.Fields.LATITUDE, latitude);
        map.put(Constants.Fields.LONGITUDE, longitude);
        map.put(Constants.Fields.PROVINCE, province);
        map.put(Constants.Fields.CITY, city);
        map.put(Constants.Fields.ADDRESS, addr);
        map.put(Constants.Fields.DISTRICT, district);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATE_USER_LOCATION, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATE_USER_LOCATION, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATE_USER_LOCATION, response.getException());
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
    public void getFriendList(long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETFRIENDLIST, tag, map, new JsonCallback<GetFriendListResponse>(GetFriendListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETFRIENDLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETFRIENDLIST, response.getException());
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
    public void attentionOperation(int type, long fromUserId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_ATTENTIONOPERATION, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_ATTENTIONOPERATION, response.body());

                        if (response.body().getMsg().equals("取消关注成功")) {
                            EventBus.getDefault().post(new AttenttionResultEvent(2));
                        } else if (response.body().getMsg().equals("关注成功")) {
                            EventBus.getDefault().post(new AttenttionResultEvent(1));
                        }
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_ATTENTIONOPERATION, response.getException());
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
    public void getUserInfo(long userId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("toUserId", toUserId);
        com.orhanobut.logger.Logger.d("user" + userId + "touser" + toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETUSERINFO, tag, map, new JsonCallback<GetUserInfoResponse>(GetUserInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetUserInfoResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETUSERINFO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETUSERINFO, response.body());
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
    public void getJoinActionList(long userId, long toUserId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("toUserId", toUserId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETJOINACTIONLIST, tag, map, new JsonCallback<GetJoinActionListResponse>(GetJoinActionListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetJoinActionListResponse> response) {
                    Log.e("asdgasdjogasdgi", response.toString());
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETJOINACTIONLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETJOINACTIONLIST, response.body());
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
    public void getPublishPostList(long userId, long toUserId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("toUserId", toUserId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETPUBLISHPOSTLIST, tag, map, new JsonCallback<MyPostListResponse>(MyPostListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<MyPostListResponse> response) {
                    Log.e("asdgasdjogasdgi", response.toString());
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETPUBLISHPOSTLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETPUBLISHPOSTLIST, response.body());
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
    public void myAccount(long userId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_MYACCOUNT, tag, map, new JsonCallback<MyAccountResponse>(MyAccountResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<MyAccountResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_MYACCOUNT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_MYACCOUNT, response.body());
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
    public void myTransactionRecordList(long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETTRANSACTIONRECORDLIST, tag, map, new JsonCallback<GetMyTransactionResponse>(GetMyTransactionResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetMyTransactionResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETTRANSACTIONRECORDLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETTRANSACTIONRECORDLIST, response.body());
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
    public void getUpgradeVersion(String version, int platform) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.VERSION, version);
        map.put(Constants.Fields.PLATFORM, 1);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETUPGRADEVERSION, tag, map, new JsonCallback<VersionResponse>(VersionResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<VersionResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETUPGRADEVERSION, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETUPGRADEVERSION, response.body());
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
    public void myPropsList(long userId, int type, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_MYPROPSLIST, tag, map, new JsonCallback<MyPropsListResponse>(MyPropsListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<MyPropsListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_MYPROPSLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_MYPROPSLIST, response.body());
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
    public void getBlackList(long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETBLACKLIST, tag, map, new JsonCallback<GetBlackListResponse>(GetBlackListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetBlackListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETBLACKLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETBLACKLIST, response.body());
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
    public void pullBlackUser(long fromUserId, final long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_PULLBLACKUSER, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(final Response<BaseResponse> response) {

                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, String.valueOf(toUserId), new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_PULLBLACKUSER, response.body());
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_PULLBLACKUSER, response.body());
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
    public void removeBlackList(long blackId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.BLACK_ID, blackId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_REMOVEBLACKLIST, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_REMOVEBLACKLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_REMOVEBLACKLIST, response.body());
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
    public void attentionStatus(long fromUserId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_ATTENTIONSTATUS, tag, map, new JsonCallback<AttentionStatusResponse>(AttentionStatusResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<AttentionStatusResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_ATTENTIONSTATUS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_ATTENTIONSTATUS, response.body());
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
    public void getAttentionOrFollowerList(int type, long userId, long toUserId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETATTENTIONORFOLLOWERLIST, tag, map, new JsonCallback<GetAttentionOrFollowerListResponse>(GetAttentionOrFollowerListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetAttentionOrFollowerListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETATTENTIONORFOLLOWERLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETATTENTIONORFOLLOWERLIST, response.body());
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
    public void getAuthContentInfo() {
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETAUTHCONTENTINFO, tag, null, new JsonCallback<GetAuthContentInfoResponse>(GetAuthContentInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetAuthContentInfoResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETAUTHCONTENTINFO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETAUTHCONTENTINFO, response.body());
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
    public void saveRealNameAuth(long userId, String realName, String phoneNumber, String certNumber, List<Image> imageList) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("realName", realName);
        map.put("phoneNumber", phoneNumber);
        map.put("certNumber", certNumber);
        map.put("imageList", imageList);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEREALNAMEAUTH, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEREALNAMEAUTH, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEREALNAMEAUTH, response.body());
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
    public void saveRealNameAuthIntegration(long userId, String realName, String phoneNumber, String certNumber, List<Image> imageList, String cardFrontImg, String cardBackImg) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("realName", realName);
        map.put("phoneNumber", phoneNumber);
        map.put("certNumber", certNumber);
        map.put("imageList", imageList);
        map.put("cardFrontImg", cardFrontImg);
        map.put("cardBackImg", cardBackImg);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEREALNAMEAUTHINTEGRATIO, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEREALNAMEAUTHINTEGRATIO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEREALNAMEAUTHINTEGRATIO, response.body());
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
    public void findTokenByUserId(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDTOKENBYUSERID, tag, map, new JsonCallback<FindTokenByUserIdResponse>(FindTokenByUserIdResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<FindTokenByUserIdResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDTOKENBYUSERID, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDTOKENBYUSERID, response.body());
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
    public void queryUserBalance(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_QUERYUSERBALANCE, tag, map, new JsonCallback<QueryUserBalanceResponse>(QueryUserBalanceResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<QueryUserBalanceResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_QUERYUSERBALANCE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_QUERYUSERBALANCE, response.body());
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
    public void findUserVideoInfo(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDUSERVIDEOINFO, tag, map, new JsonCallback<FindUserVideoInfoResponse>(FindUserVideoInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<FindUserVideoInfoResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDUSERVIDEOINFO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDUSERVIDEOINFO, response.body());
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
    public void updateUserVideoInfo(long userId, int status) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STATUS, status);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEUSERVIDEOINFO, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEUSERVIDEOINFO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEUSERVIDEOINFO, response.getException());
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
    public void getLetterPropsList() {
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETLETTERPROPSLIST, tag, null, new JsonCallback<GetLetterPropsListResponse>(GetLetterPropsListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }

                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETLETTERPROPSLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETLETTERPROPSLIST, response.getException());
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
    public void letterPresentedGifts(long fromUserId, long toUserId, long propsId, int propsNum, int presentedType, int goldNum) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.PROPS_ID, propsId);
        map.put(Constants.Fields.PROPS_NUM, propsNum);
        map.put(Constants.Fields.PRESENTED_TYPE, presentedType);
        map.put(Constants.Fields.GOLD_NUM, goldNum);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_LETTERPRESENTEDGIFTS, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_LETTERPRESENTEDGIFTS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_LETTERPRESENTEDGIFTS, response.getException());
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
    public void findUserAuthStatus(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDUSERAUTHSTATUS, tag, map, new JsonCallback<FindUserAuthStatusResponse>(FindUserAuthStatusResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<FindUserAuthStatusResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDUSERAUTHSTATUS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDUSERAUTHSTATUS, response.getException());
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
    public void checkIsDeleteComment(long userId, long commentId, int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.COMMENT_ID, commentId);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_CHECKISDELETECOMMENT, tag, map, new JsonCallback<CheckIsDeleteCommentResponse>(CheckIsDeleteCommentResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<CheckIsDeleteCommentResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CHECKISDELETECOMMENT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CHECKISDELETECOMMENT, response.getException());
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
    public void judgeUserSchool(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_JUDGEUSERSCHOOL, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_JUDGEUSERSCHOOL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JUDGEUSERSCHOOL, response.getException());
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
    public void saveSchool(long userId, long schoolId, String schoolName) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.SCHOOL_ID, schoolId);
        map.put(Constants.Fields.SCHOOL_NAME, schoolName);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVESCHOOL, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_SAVESCHOOL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVESCHOOL, response.getException());
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
    public void getLoginRecommendList(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETLOGINRECOMMENDLIST, tag, map, new JsonCallback<GetLoginRecommendListResponse>(GetLoginRecommendListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetLoginRecommendListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETLOGINRECOMMENDLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETLOGINRECOMMENDLIST, response.getException());
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
    public void sendAnonymousMsg(long fromUserId, long toUserId, int type, String content, String file) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.FILE, file);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().uploadImage(InterfaceUrl.URL_SENDANONYMOUSMSG, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_SENDANONYMOUSMSG, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SENDANONYMOUSMSG, response.getException());
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
    public void getAnonymityUser(long fromUserId, long toUserId) {
        final Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETANONYMITYUSER, tag, map, new JsonCallback<GetAnonymityUserResponse>(GetAnonymityUserResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetAnonymityUserResponse> response) {
                    if (mView != null) {
                        GetAnonymityUserResponse getAnonymityUserResponse = response.body();
                        if (getAnonymityUserResponse.getCode() == 200) {
                            final ChatBaseUser chatBaseUser = getAnonymityUserResponse.getChatBaseUser();
                            final ChatBaseUser toChatBaseUser = getAnonymityUserResponse.getToChatBaseUser();
                            if (chatBaseUser != null && toChatBaseUser != null) {
                                Context context = null;
                                if (mView instanceof Fragment) {
                                    context = ((Fragment) mView).getActivity();
                                } else if (mView instanceof Activity) {
                                    context = (Activity) mView;
                                }
                                RongIMutils.startToConversation(context, toChatBaseUser.getAnonymityId(), toChatBaseUser.getNickName());
                                UserInfo userInfo = new UserInfo(String.valueOf(chatBaseUser.getUserId()), chatBaseUser.getNickName(), Uri.parse(chatBaseUser.getUserIcon()));
                                RongIM.getInstance().refreshUserInfoCache(userInfo);
                                UserInfo toUserInfo = new UserInfo(String.valueOf(toChatBaseUser.getAnonymityId()), toChatBaseUser.getNickName(), Uri.parse(toChatBaseUser.getUserIcon()));
                                RongIM.getInstance().refreshUserInfoCache(toUserInfo);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        EventBus.getDefault().post(new AnonymousMsgEvent(chatBaseUser, toChatBaseUser));
                                    }
                                }, 1000);
                                if (!SafeSharePreferenceUtils.getBoolean(Constants.Fields.IS_ANONYMOUS_PRIVATE_TOAST_SHOW, false)) {
                                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.IS_ANONYMOUS_PRIVATE_TOAST_SHOW, true);
                                    Utils.showToastShortTime("匿名聊天将随机给您生成新的头像和昵称");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETANONYMITYUSER, response.getException());
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
    public void saveAnonymityUser(long fromUserId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEANONYMITYUSER, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_SAVEANONYMITYUSER, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEANONYMITYUSER, response.getException());
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
    public void getAnonymityFriendList(long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETANONYMITYFRIENDLIST, tag, map, new JsonCallback<GetAnonymityFriendListResponse>(GetAnonymityFriendListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetAnonymityFriendListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETANONYMITYFRIENDLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETANONYMITYFRIENDLIST, response.getException());
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
    public void deleteAnonymityUser(long fromUserId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELETEANONYMITYUSER, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_DELETEANONYMITYUSER, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETEANONYMITYUSER, response.getException());
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
    public void updateAnonymityUser(long userId, String nickName) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.NICK_NAME, nickName);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEANONYMITYUSER, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_UPDATEANONYMITYUSER, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEANONYMITYUSER, response.getException());
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
