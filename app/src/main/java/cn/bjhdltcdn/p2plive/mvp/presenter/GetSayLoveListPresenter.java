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
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSayLoveListResponse;
//import cn.bjhdltcdn.p2plive.mvp.contract.GetSayLoveListContract;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//
//public class GetSayLoveListPresenter implements GetSayLoveListContract.Presenter {
//
//    private BaseView mView;
//
//    public GetSayLoveListPresenter(BaseView mView) {
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
//    public void getSayLoveList(long userId, int sort, int pageSize, int pageNumber, final boolean isNeedShowLoadin) {
//        Map map = new LinkedHashMap(1);
//        map.put(Constants.Fields.USER_ID, userId);
//        map.put(Constants.Fields.SORT, sort);
//        map.put(Constants.Fields.PAGE_SIZE, pageSize);
//        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_GETSAYLOVELIST, tag, map, new JsonCallback<GetSayLoveListResponse>(GetSayLoveListResponse.class) {
//
//                @Override
//                public void onStart(Request request) {
//                    super.onStart(request);
//                    if (mView != null && isNeedShowLoadin) {
//                        mView.showLoading();
//                    }
//                }
//
//                @Override
//                public void onSuccess(Response<GetSayLoveListResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETSAYLOVELIST, response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETSAYLOVELIST, response.getException());
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
//    public void deleteSayLove(long userId, long sayLoveId) {
//        Map map = new LinkedHashMap(1);
//        map.put(Constants.Fields.USER_ID, userId);
//        map.put(Constants.Fields.SAY_LOVE_ID, sayLoveId);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_DELETESAYLOVE, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {
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
//                public void onSuccess(Response<BaseResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_DELETESAYLOVE, response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_DELETESAYLOVE, response.getException());
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
//    public void getMySayLoveList(long userId, long toUserId, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
//        Map map = new LinkedHashMap(1);
//        map.put(Constants.Fields.USER_ID, userId);
//        map.put(Constants.Fields.TO_USER_ID, toUserId);
//        map.put(Constants.Fields.PAGE_SIZE, pageSize);
//        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_GETMYSAYLOVELIST, tag, map, new JsonCallback<GetSayLoveListResponse>(GetSayLoveListResponse.class) {
//
//                @Override
//                public void onStart(Request request) {
//                    super.onStart(request);
//                    if (mView != null & isNeedShowLoading) {
//                        mView.showLoading();
//                    }
//                }
//
//                @Override
//                public void onSuccess(Response<GetSayLoveListResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETMYSAYLOVELIST, response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETMYSAYLOVELIST, response.getException());
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
