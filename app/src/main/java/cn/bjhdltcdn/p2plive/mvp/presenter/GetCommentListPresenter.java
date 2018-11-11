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
import cn.bjhdltcdn.p2plive.httpresponse.FindSayLoveDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetCommentListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SayLoveCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SayLovePraiseResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.GetCommentListContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class GetCommentListPresenter implements GetCommentListContract.Presenter {

    private BaseView mView;

    public GetCommentListPresenter(BaseView mView) {
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
    public void findSayLoveDetail(long userId, long sayLoveId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("sayLoveId", sayLoveId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDSAYLOVEDETAIL, tag, map, new JsonCallback<FindSayLoveDetailResponse>(FindSayLoveDetailResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<FindSayLoveDetailResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDSAYLOVEDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDSAYLOVEDETAIL, response.body());
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
    public void getCommentList(long userId, long parentId, int sort, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETCOMMENTLIST, tag, map, new JsonCallback<GetCommentListResponse>(GetCommentListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null && isNeedShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetCommentListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCOMMENTLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETCOMMENTLIST, response.body());
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
    public void sayLoveComment(long sayLoveId, String content, int type, long toUserId, long fromUserId, long commentId) {
        Map map = new LinkedHashMap(1);
        map.put("sayLoveId", sayLoveId);
        map.put("content", content);
        map.put("type", type);
        map.put("toUserId", toUserId);
        map.put("fromUserId", fromUserId);
        map.put("parentId", commentId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAYLOVECOMMENT, tag, map, new JsonCallback<SayLoveCommentResponse>(SayLoveCommentResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<SayLoveCommentResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAYLOVECOMMENT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAYLOVECOMMENT, response.body());
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
    public void sayLovePraise(long sayLoveId, int type, long userId) {
        Map map = new LinkedHashMap(1);
        map.put("sayLoveId", sayLoveId);
        map.put("type", type);
        map.put("userId", userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAYLOVEPRAISE, tag, map, new JsonCallback<SayLovePraiseResponse>(SayLovePraiseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<SayLovePraiseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAYLOVEPRAISE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAYLOVEPRAISE, response.body());
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
    public void deleteSayLoveComment(long commentId) {
        Map map = new LinkedHashMap(1);
        map.put("commentId", commentId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELETESAYLOVECOMMENT, tag, map, new JsonCallback<NoParameterResponse>(NoParameterResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<NoParameterResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETESAYLOVECOMMENT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETESAYLOVECOMMENT, response.body());
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
    public void deleteSayLove(long userId, long sayLoveId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("sayLoveId", sayLoveId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELETESAYLOVE, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_DELETESAYLOVE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETESAYLOVE, response.body());
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
