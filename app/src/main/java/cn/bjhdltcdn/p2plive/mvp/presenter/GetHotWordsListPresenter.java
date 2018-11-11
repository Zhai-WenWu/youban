package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.httpresponse.GetHotWordsListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetSearchDataListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SearchKeywordListResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.GetHotWordsListContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class GetHotWordsListPresenter implements GetHotWordsListContract.Presenter {

    private BaseView mView ;

    public GetHotWordsListPresenter(BaseView mView) {
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
    public void getHotSearchFieldList() {
        Map map = new LinkedHashMap(1);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETHOTWORDSLIST,tag, map, new JsonCallback<GetHotWordsListResponse>(GetHotWordsListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetHotWordsListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOTWORDSLIST,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETHOTWORDSLIST,response.body());
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
    public void searchKeywordList(long userId, String content) {
        Map map = new LinkedHashMap(1);
        map.put("userId",userId);
        map.put("content",content);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SEARCHKEYWORDLIST,tag, map, new JsonCallback<SearchKeywordListResponse>(SearchKeywordListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<SearchKeywordListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SEARCHKEYWORDLIST,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SEARCHKEYWORDLIST,response.body());
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
    public void getSearchDataList(long userId, String content, int type, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put("userId",userId);
        map.put("content",content);
        map.put("type",type);
        map.put("pageSize",pageSize);
        map.put("pageNumber",pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETSEARCHDATALIST,tag, map, new JsonCallback<GetSearchDataListResponse>(GetSearchDataListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetSearchDataListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSEARCHDATALIST,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSEARCHDATALIST,response.body());
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
