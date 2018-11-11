package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHomeNewCountListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHomeNewCountResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetNewCountListResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.GetHomeNearbyContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class GetHomeNearbyPresenter implements GetHomeNearbyContract.Presenter {

    private BaseView mView ;

    public GetHomeNearbyPresenter(BaseView mView) {
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
    public void getNewCountList(long userId) {
        Map map = new LinkedHashMap(1);
        map.put("userId",userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETNEWCOUNTLIST,tag, map, new JsonCallback<GetNewCountListResponse>(GetNewCountListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
//                    if (mView != null) {
//                        mView.showLoading();
//                    }
                }

                @Override
                public void onSuccess(Response<GetNewCountListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETNEWCOUNTLIST,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETNEWCOUNTLIST,response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
//                    if (mView != null) {
//                        mView.hideLoading();
//                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRefreshTime(long userId, int type) {
        Map map = new LinkedHashMap(1);
        map.put("userId",userId);
        map.put("type",type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEREFRESHTIME,tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEREFRESHTIME,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEREFRESHTIME,response.getException());
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
    public void getHomeNewCount(long userId) {
        Map map = new LinkedHashMap(1);
        map.put("userId",userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETHOMENEWCOUNT,tag, map, new JsonCallback<GetHomeNewCountResponse>(GetHomeNewCountResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetHomeNewCountResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOMENEWCOUNT,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOMENEWCOUNT,response.getException());
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
    public void getHomeNewCountList(long userId) {
        Map map = new LinkedHashMap(1);
        map.put("userId",userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETHOMENEWCOUNTLIST,tag, map, new JsonCallback<GetHomeNewCountListResponse>(GetHomeNewCountListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetHomeNewCountListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOMENEWCOUNTLIST,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOMENEWCOUNTLIST,response.getException());
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
