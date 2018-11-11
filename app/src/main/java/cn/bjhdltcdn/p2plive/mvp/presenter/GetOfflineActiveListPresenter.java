//package cn.bjhdltcdn.p2plive.mvp.presenter;
//
//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.model.Response;
//import com.lzy.okgo.request.base.Request;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import cn.bjhdltcdn.p2plive.api.ApiData;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.callback.JsonCallback;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.GetOfflineActiveListResponse;
//import cn.bjhdltcdn.p2plive.mvp.contract.GetOfflineActiveListContract;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//
//
//public class GetOfflineActiveListPresenter implements GetOfflineActiveListContract.Presenter {
//
//    private BaseView mView;
//
//    public GetOfflineActiveListPresenter(BaseView mView) {
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
//    public void getOfflineActiveList(long userId, int activeType, List<Long> hobbyIds, int sort, int publisherLimit, int sexLimit, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
//        Map map = new LinkedHashMap(1);
//        map.put("userId", userId);
//        map.put("activeType", activeType);
//        map.put("hobbyIds", hobbyIds);
//        map.put("sort", sort);
//        map.put("publisherLimit", publisherLimit);
//        map.put("sexLimit", sexLimit);
//        map.put("pageSize", pageSize);
//        map.put("pageNumber", pageNumber);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_GETOFFLINEACTIVELIST, tag, map, new JsonCallback<GetOfflineActiveListResponse>(GetOfflineActiveListResponse.class) {
//
//                @Override
//                public void onStart(Request request) {
//                    super.onStart(request);
//                    if (mView != null && isNeedShowLoading) {
//                        mView.showLoading();
//                    }
//                }
//
//                @Override
//                public void onSuccess(Response<GetOfflineActiveListResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETOFFLINEACTIVELIST, response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETOFFLINEACTIVELIST, response.getException());
//                    }
//
//
//                }
//
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
//    public void getJoinActionList(long userId, long toUserId, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
//        Map map = new LinkedHashMap(1);
//        map.put(Constants.Fields.USER_ID, userId);
//        map.put(Constants.Fields.TO_USER_ID, toUserId);
//        map.put(Constants.Fields.PAGE_SIZE, pageSize);
//        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
//        String tag = mView.getClass().getSimpleName();
//
//        try {
//            ApiData.getInstance().postData(InterfaceUrl.URL_GETJOINACTIONLIST, tag, map, new JsonCallback<GetOfflineActiveListResponse>(GetOfflineActiveListResponse.class) {
//
//                @Override
//                public void onStart(Request request) {
//                    super.onStart(request);
//                    if (mView != null && isNeedShowLoading) {
//                        mView.showLoading();
//                    }
//                }
//
//                @Override
//                public void onSuccess(Response<GetOfflineActiveListResponse> response) {
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETJOINACTIONLIST, response.body());
//                    }
//                }
//
//                @Override
//                public void onError(Response response) {
//                    super.onError(response);
//
//                    if (mView != null) {
//                        mView.updateView(InterfaceUrl.URL_GETJOINACTIONLIST, response.getException());
//                    }
//
//
//                }
//
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
