//package cn.bjhdltcdn.p2plive.mvp.presenter;
//
//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.model.Response;
//import com.lzy.okgo.request.base.Request;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import cn.bjhdltcdn.p2plive.api.ApiData;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.callback.JsonCallback;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.GetNearOrganListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetNearPersonListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetOfflineActiveListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
//import cn.bjhdltcdn.p2plive.mvp.contract.GetNearOrganListContract;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//
//public class GetNearOrganListPresenter implements GetNearOrganListContract.Presenter {
//
//    private BaseView mView ;
//
//    public GetNearOrganListPresenter(BaseView mView) {
//        this.mView = mView;
//    }
//
//
//    @Override
//    public void cancelTag(String tag) {
//        OkGo.getInstance().cancelTag(tag);
//    }
//
//    @Override
//    public void onDestroy() {
//        if (mView != null) {
//            mView = null;
//        }
//    }
//
//    @Override
//    public void getNearOrganList(long userId,int pageSize,int pageNumber){
//        Map map = new LinkedHashMap(1);
//        map.put("userId",userId);
//        map.put("pageSize",pageSize);
//        map.put("pageNumber",pageNumber);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_GETNEARORGANLIST,tag, map, new JsonCallback<GetNearOrganListResponse>(GetNearOrganListResponse.class) {
//
//                @Override
//                public void onStart(Request request) {
//                    super.onStart(request);
//                    if (mView != null) {
//                        mView.showLoading();
//                    }
//                }
//
//                @Override
//                public void onSuccess(Response<GetNearOrganListResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETNEARORGANLIST,response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETNEARORGANLIST,response.getException());
//                    }
//
//
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    if (mView != null) {
//                        mView.hideLoading();
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void getNearPersonList(long userId, int type,int sexLimit, int pageSize, int pageNumber) {
//        Map map = new LinkedHashMap(1);
//        map.put(Constants.Fields.USER_ID,userId);
//        map.put(Constants.Fields.TYPE,type);
//        map.put(Constants.Fields.SEX_LIMIT,sexLimit);
//        map.put(Constants.Fields.PAGE_SIZE,pageSize);
//        map.put(Constants.Fields.PAGE_NUMBER,pageNumber);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_GETNEARPERSONLIST,tag, map, new JsonCallback<GetNearPersonListResponse>(GetNearPersonListResponse.class) {
//
//                @Override
//                public void onStart(Request request) {
//                    super.onStart(request);
//                    if (mView != null) {
//                        mView.showLoading();
//                    }
//                }
//
//                @Override
//                public void onSuccess(Response<GetNearPersonListResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETNEARPERSONLIST,response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETNEARPERSONLIST,response.getException());
//                    }
//
//
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    if (mView != null) {
//                        mView.hideLoading();
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void getInvitationActionList(long userId, long toUserId, int pageSize, int pageNumber) {
//        Map map = new LinkedHashMap(1);
//        map.put(Constants.Fields.USER_ID,userId);
//        map.put(Constants.Fields.TO_USER_ID,toUserId);
//        map.put(Constants.Fields.PAGE_SIZE,pageSize);
//        map.put(Constants.Fields.PAGE_NUMBER,pageNumber);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_GETINVITATIONACTIONLIST,tag, map, new JsonCallback<GetOfflineActiveListResponse>(GetOfflineActiveListResponse.class) {
//
//                @Override
//                public void onStart(Request request) {
//                    super.onStart(request);
//                    if (mView != null) {
//                        mView.showLoading();
//                    }
//                }
//
//                @Override
//                public void onSuccess(Response<GetOfflineActiveListResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETINVITATIONACTIONLIST,response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETINVITATIONACTIONLIST,response.getException());
//                    }
//
//
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    if (mView != null) {
//                        mView.hideLoading();
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void sendNearInvitation(long userId, long toUserId, long activityId) {
//        Map map = new LinkedHashMap(1);
//        map.put(Constants.Fields.USER_ID,userId);
//        map.put(Constants.Fields.TO_USER_ID,toUserId);
//        map.put(Constants.Fields.ACTIVITY_ID,activityId);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_SENDNEARINVITATION,tag, map, new JsonCallback<NoParameterResponse>(NoParameterResponse.class) {
//
//                @Override
//                public void onStart(Request request) {
//                    super.onStart(request);
//                    if (mView != null) {
//                        mView.showLoading();
//                    }
//                }
//
//                @Override
//                public void onSuccess(Response<NoParameterResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_SENDNEARINVITATION,response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_SENDNEARINVITATION,response.getException());
//                    }
//
//
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    if (mView != null) {
//                        mView.hideLoading();
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
