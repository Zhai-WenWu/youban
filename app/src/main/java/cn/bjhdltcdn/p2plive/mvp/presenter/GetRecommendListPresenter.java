package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.httpresponse.GetCommonLabelInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHomeBannerListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPostListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetRecommendOrganListResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.GetRecommendListContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class GetRecommendListPresenter implements GetRecommendListContract.Presenter {

    private BaseView mView;

    public GetRecommendListPresenter(BaseView mView) {
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
    public void getPostList(long userId, long organId, int sort,long labelId, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("organId", organId);
        map.put("sort", sort);
        map.put("labelId", labelId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        String tag;
        if (mView != null){
            tag = mView.getClass().getSimpleName();
        }else{
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETPOSTLIST, tag, map, new JsonCallback<GetPostListResponse>(GetPostListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null && isNeedShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetPostListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETPOSTLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETPOSTLIST, response.getException());
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
    public void getHomeBannerList(long userId, int type) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("type", type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETHOMEBANNERLIST, tag, map, new JsonCallback<GetHomeBannerListResponse>(GetHomeBannerListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetHomeBannerListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOMEBANNERLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOMEBANNERLIST, response.getException());
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
    public void getRecommendOrganList(long userId, int pageSize, int pageNumber, final boolean show) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETRECOMMENDORGANLIST, tag, map, new JsonCallback<GetRecommendOrganListResponse>(GetRecommendOrganListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null && show) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetRecommendOrganListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETRECOMMENDORGANLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETRECOMMENDORGANLIST, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null && show) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCommonLabelInfo(long userId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETCOMMONLABELINFO, tag, map, new JsonCallback<GetCommonLabelInfoResponse>(GetCommonLabelInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetCommonLabelInfoResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCOMMONLABELINFO, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCOMMONLABELINFO, response.getException());
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
