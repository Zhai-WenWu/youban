package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindHelpDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetClassmateHelpListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHelpCommentListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.HelpPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveClassmateHelpResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.ClassMateHelpContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by ZHAI on 2018/2/27.
 */

public class ClassMateHelpPresenter implements ClassMateHelpContract.Presenter {
    private BaseView mView;

    public ClassMateHelpPresenter(BaseView mView) {
        this.mView = mView;
    }

    @Override
    public void cancelTag(String tag) {

    }

    @Override
    public void onDestroy() {
        if (mView != null) {
            mView = null;
        }
    }

    @Override
    public void getClassmateHelpList(long userId, int sort, int pageSize, int pageNumber, final boolean needShowLoading) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_GETCLASSMATEHELPLIST, tag, map, new JsonCallback<GetClassmateHelpListResponse>(GetClassmateHelpListResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
                if (mView != null && needShowLoading) {
                    mView.showLoading();
                }
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETCLASSMATEHELPLIST, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETCLASSMATEHELPLIST, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
                mView.hideLoading();
            }


        });
    }

    @Override
    public void getPublishHelpList(long userId, long toUserId, int pageSize, int pageNumber, final boolean needShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);

        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_GETPUBLISHHELPLIST, tag, map, new JsonCallback<GetClassmateHelpListResponse>(GetClassmateHelpListResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
                if (mView != null && needShowLoading) {
                    mView.showLoading();
                }
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETPUBLISHHELPLIST, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETPUBLISHHELPLIST, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
                mView.hideLoading();
            }


        });
    }

    @Override
    public void helpPraise(long userId, long helpId, int type) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.HELP_ID, helpId);
        map.put(Constants.Fields.TYPE, type);

        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_HELPPRAISE, tag, map, new JsonCallback<HelpPraiseResponse>(HelpPraiseResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_HELPPRAISE, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_HELPPRAISE, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });
    }

    @Override
    public void deleteHelp(long userId, long helpId) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.HELP_ID, helpId);

        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_DELETEHELP, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_DELETEHELP, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_DELETEHELP, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });
    }

    @Override
    public void saveClassmateHelp(long userId, long toUserId, String content, int helpType, String videoUrl, String videoImageUrl, long labelId, long[] secondLabelIds, final boolean isLoading, long helpId) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.HELPTYPE, helpType);
        map.put(Constants.Fields.VIDEO_URL, videoUrl);
        map.put(Constants.Fields.VIDEO_IMAGE_URL, videoImageUrl);
        map.put(Constants.Fields.LABEL_ID, labelId);
        map.put(Constants.Fields.SECOND_LABEL_IDS, secondLabelIds);
        map.put(Constants.Fields.LOCATIONINFO, App.getInstance().getUserLocation());
        map.put(Constants.Fields.HELP_ID, helpId);

        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_SAVECLASSMATEHELP, tag, map, new JsonCallback<SaveClassmateHelpResponse>(SaveClassmateHelpResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
                if (isLoading) {
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_SAVECLASSMATEHELP, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_SAVECLASSMATEHELP, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
                if (isLoading) {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }

            }


        });
    }

    @Override
    public void getHelpCommentList(long userId, long helpId, int sort, int pageSize, int pageNumber) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.HELP_ID, helpId);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);

        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_GETHELPCOMMENTLIST, tag, map, new JsonCallback<GetHelpCommentListResponse>(GetHelpCommentListResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETHELPCOMMENTLIST, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETHELPCOMMENTLIST, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });
    }

    @Override
    public void deleteHelpComment(long userId, long commentId) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.COMMENT_ID, commentId);

        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_DELETEHELPCOMMENT, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_DELETEHELPCOMMENT, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_DELETEHELPCOMMENT, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });
    }

    @Override
    public void findHelpDetail(long userId, long helpId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.HELP_ID, helpId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDHELPDETAIL, tag, map, new JsonCallback<FindHelpDetailResponse>(FindHelpDetailResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_FINDHELPDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDHELPDETAIL, response.getException());
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
