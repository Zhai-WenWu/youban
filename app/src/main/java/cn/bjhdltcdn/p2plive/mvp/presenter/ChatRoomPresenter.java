package cn.bjhdltcdn.p2plive.mvp.presenter;


import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindRoomDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetChatRoomListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetCurrentGoldStatisticResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetCurrentVoteDataResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOnWheatApplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOnWheatListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOnlineUserListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOwnerInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPropsListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetRoomListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetUserInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetWheatPropsDataResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GiftNumListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.InitiateVoteResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateChatRoomResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateRoomStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserStatusResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.ChatRoomContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by ZHUDI on 2017/12/11.
 */

public class ChatRoomPresenter implements ChatRoomContract.Presenter {
    private BaseView mView;

    public ChatRoomPresenter(BaseView mView) {
        this.mView = mView;
    }

    @Override
    public void cancelTag(String tag) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void updateRoomStatus(long userId, long roomId, String roomName, int status, int passwordType, String password, List<Long> labelList, List<String> customList) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("roomId", roomId);
        map.put("roomName", roomName);
        map.put("status", status);
        map.put("passwordType", passwordType);
        map.put("password", password);
        map.put("labelList", labelList);
        map.put("customList", customList);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEROOMSTATUS, tag, map, new JsonCallback<UpdateRoomStatusResponse>(UpdateRoomStatusResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<UpdateRoomStatusResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEROOMSTATUS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEROOMSTATUS, response.getException());
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
    public void updateUserStatus(long toUserId, long roomId, int type, int passwordType, String password) {
        Map map = new LinkedHashMap(1);
        map.put("toUserId", toUserId);
        map.put("roomId", roomId);
        map.put("type", type);
        map.put("passwordType", passwordType);
        map.put("password", password);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEUSERSTATUS, tag, map, new JsonCallback<UpdateUserStatusResponse>(UpdateUserStatusResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<UpdateUserStatusResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEUSERSTATUS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEUSERSTATUS, response.body());
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
    public void upOrDownWheat(long userId, long toUserId, long roomId, int wheat, int type) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("toUserId", toUserId);
        map.put("roomId", roomId);
        map.put("wheat", wheat);
        map.put("type", type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPORDOWNWHEAT, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPORDOWNWHEAT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPORDOWNWHEAT, response.body());
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
    public void controlUserCamera(long userId, long roomId, int type) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("roomId", roomId);
        map.put("type", type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_CONTROLUSERCAMERA, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CONTROLUSERCAMERA, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CONTROLUSERCAMERA, response.body());
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
    public void getOnWheatApplyList(long roomId) {
        Map map = new LinkedHashMap(1);
        map.put("roomId", roomId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETONWHEATPAALYLIST, tag, map, new JsonCallback<GetOnWheatApplyListResponse>(GetOnWheatApplyListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetOnWheatApplyListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETONWHEATPAALYLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETONWHEATPAALYLIST, response.body());
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
    public void getOnWheatList(long roomId) {
        Map map = new LinkedHashMap(1);
        map.put("roomId", roomId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETONWHEATLIST, tag, map, new JsonCallback<GetOnWheatListResponse>(GetOnWheatListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetOnWheatListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETONWHEATLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETONWHEATLIST, response.body());
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
    public void getCurrentVoteData(long userId, long roomId, long voteId, int sort) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("roomId", roomId);
        map.put("voteId", voteId);
        map.put("sort", sort);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETCURRENTVOTEDATA, tag, map, new JsonCallback<GetCurrentVoteDataResponse>(GetCurrentVoteDataResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetCurrentVoteDataResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCURRENTVOTEDATA, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCURRENTVOTEDATA, response.body());
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
    public void transferHosting(long roomId, int transfer, long userId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put("roomId", roomId);
        map.put("transfer", transfer);
        map.put("userId", userId);
        map.put("toUserId", toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_TRANSFERHOSTING, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_TRANSFERHOSTING, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_TRANSFERHOSTING, response.body());
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
    public void initiateVote(long roomId, long userId, int type, long voteId) {
        Map map = new LinkedHashMap(1);
        map.put("roomId", roomId);
        map.put("userId", userId);
        map.put("type", type);
        map.put("voteId", voteId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_INITIATEVOTE, tag, map, new JsonCallback<InitiateVoteResponse>(InitiateVoteResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<InitiateVoteResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_INITIATEVOTE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_INITIATEVOTE, response.body());
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
    public void getOwnerInfo(long roomId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.ROOM_ID, roomId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETOWNERINFO, tag, map, new JsonCallback<GetOwnerInfoResponse>(GetOwnerInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetOwnerInfoResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETOWNERINFO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETOWNERINFO, response.body());
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
    public void banningComments(long roomId, int type, long userId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put("roomId", roomId);
        map.put("type", type);
        map.put("userId", userId);
        map.put("toUserId", toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_BANNINGCOMMENTS, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_BANNINGCOMMENTS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_BANNINGCOMMENTS, response.body());
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
    public void attentionOperation(int type, long roomId, long fromUserId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put("roomId", roomId);
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
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_ATTENTIONOPERATION, response.body());
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
    public void presentedProps(long fromUserId, long toUserId, long propsId, int presentedType, int goldNum, int propsNum) {
        Map map = new LinkedHashMap(1);
        map.put("fromUserId", fromUserId);
        map.put("toUserId", toUserId);
        map.put("propsId", propsId);
        map.put("presentedType", presentedType);
        map.put("goldNum", goldNum);
        map.put("propsNum", propsNum);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_PRESENTEDPROPS, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_PRESENTEDPROPS, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_PRESENTEDPROPS, response.getException());
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
    public void getPropsList() {
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETPROPSLIST, tag, null, new JsonCallback<GetPropsListResponse>(GetPropsListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (response.body() instanceof GetPropsListResponse) {
                        final GetPropsListResponse getPropsListResponse = (GetPropsListResponse) response.body();
                        if (getPropsListResponse.getCode() == 200) {
                            GreenDaoUtils.getInstance().deleteAllProps(new GreenDaoUtils.ExecuteCallBack() {
                                @Override
                                public void callBack(Object object) {
                                    if (object instanceof Integer) {
                                        Integer integer = (Integer) object;
                                        if (integer.intValue() > 0) {
                                            GreenDaoUtils.getInstance().insertListProps(getPropsListResponse.getList());
                                            GreenDaoUtils.getInstance().insertListProps(getPropsListResponse.getRoomPropsList());
                                        }
                                    }
                                }
                            });

                        }
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

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
    public void checkOneToOneVideo(long userId, long answerUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ANSWERUSER_ID, answerUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_CHECKONETOONEVIDEO, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_CHECKONETOONEVIDEO, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CHECKONETOONEVIDEO, response.getException());
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
    public void getOnlineUserList(long roomId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.ROOM_ID, roomId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETONLINEUSERLIST, tag, map, new JsonCallback<GetOnlineUserListResponse>(GetOnlineUserListResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_GETONLINEUSERLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETONLINEUSERLIST, response.getException());
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
    public void giftNumList() {
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GIFTNUMLIST, tag, null, new JsonCallback<GiftNumListResponse>(GiftNumListResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_GIFTNUMLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GIFTNUMLIST, response.getException());
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
    public void presentedGifts(long fromUserId, long toUserId, long propsId, int propsNum, long parentId, long voteId, int presentedType, long goldNum) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.PROPS_ID, propsId);
        map.put(Constants.Fields.PROPS_NUM, propsNum);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.VOTE_ID, voteId);
        map.put(Constants.Fields.PRESENTED_TYPE, presentedType);
        map.put(Constants.Fields.GOLD_NUM, goldNum);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_PRESENTEDGIFTS, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_PRESENTEDGIFTS, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_PRESENTEDGIFTS, response.getException());
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
    public void getUserInfo(long userId, long toUserId, long roomId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("toUserId", toUserId);
        map.put("roomId", roomId);
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
    public void userVoting(long roomId, long userId, long toUserId, long voteId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.ROOM_ID, roomId);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.VOTE_ID, toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_USERVOTING, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_USERVOTING, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_USERVOTING, response.body());
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
    public void getWheatPropsData(long roomId, int type, long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.ROOM_ID, roomId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);

        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETWHEATPROPSDATA, tag, map, new JsonCallback<GetWheatPropsDataResponse>(GetWheatPropsDataResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetWheatPropsDataResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETWHEATPROPSDATA, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETWHEATPROPSDATA, response.body());
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
    public void getCurrentGoldStatistic(long roomId, long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.ROOM_ID, roomId);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETCURRENTGOLDSTATISTIC, tag, map, new JsonCallback<GetCurrentGoldStatisticResponse>(GetCurrentGoldStatisticResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetCurrentGoldStatisticResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCURRENTGOLDSTATISTIC, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCURRENTGOLDSTATISTIC, response.body());
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
    public void oneToOneVideo(long userId, long fromUserId, long toUserId, int callType, int type, String channelName) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.CALL_TYPE, callType);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.CHANNEL_NAME, channelName);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_ONETOONEVIDEO, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_ONETOONEVIDEO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_ONETOONEVIDEO, response.body());
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
    public void findRoomDetail(long userId, long roomId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ROOM_ID, roomId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDROOMDETAIL, tag, map, new JsonCallback<FindRoomDetailResponse>(FindRoomDetailResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<FindRoomDetailResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDROOMDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDROOMDETAIL, response.body());
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
    public void updateChatRoom(long userId, String chatId, String chatName, int status, int schoolLimit, int sexLimit, List<Long> labelList, List<String> customList) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.CHAT_ID, chatId);
        map.put(Constants.Fields.CHAT_NAME, chatName);
        map.put(Constants.Fields.STATUS, status);
        map.put(Constants.Fields.SCHOOL_LIMIT, schoolLimit);
        map.put(Constants.Fields.SEX_LIMIT, sexLimit);
        map.put(Constants.Fields.LABEL_LIST, labelList);
        map.put(Constants.Fields.CUSTOM_LIST, customList);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATECHATROOM, tag, map, new JsonCallback<UpdateChatRoomResponse>(UpdateChatRoomResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<UpdateChatRoomResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATECHATROOM, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATECHATROOM, response.getException());
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
    public void updateChatRoomUser(long userId, long toUserId, String chatId, int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.CHAT_ID, chatId);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATECHATROOMUSER, tag, map, new JsonCallback<UpdateChatRoomResponse>(UpdateChatRoomResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<UpdateChatRoomResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATECHATROOMUSER, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATECHATROOMUSER, response.getException());
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
    public void getRoomList(long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETROOMLIST, tag, map, new JsonCallback<GetRoomListResponse>(GetRoomListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetRoomListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETROOMLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETROOMLIST, response.getException());
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
    public void getChatRoomList(long userId, int pageSize, int pageNumber, int sort) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        map.put(Constants.Fields.SORT, sort);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETCHATROOMLIST, tag, map, new JsonCallback<GetChatRoomListResponse>(GetChatRoomListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetChatRoomListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCHATROOMLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCHATROOMLIST, response.getException());
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
    public void updateChatRoomLock(long userId, String chatId, int isLock) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.CHAT_ID, chatId);
        map.put(Constants.Fields.IS_LOCK, isLock);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATECHATROOMLOCK, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATECHATROOMLOCK, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATECHATROOMLOCK, response.getException());
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
