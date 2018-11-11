package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.SearchLabelByContentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.savePostResponse;
import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.mvp.contract.AssociationContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * 圈子模块控制器
 */

public class AssociationPresenter implements AssociationContract.Presenter {

    private BaseView mView;

    public AssociationPresenter(BaseView mView) {
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
    public void searchLabelByContent(long userId, String content, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);

        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_SEARCHLABELBYCONTENT, tag, map, new JsonCallback<SearchLabelByContentResponse>(SearchLabelByContentResponse.class) {


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
                    mView.updateView(InterfaceUrl.URL_SEARCHLABELBYCONTENT, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_SEARCHLABELBYCONTENT, response.getException());
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

    }

    @Override
    public void savePost(long userId, long toUserId, String content, int isAnonymous, int topicType, String videoUrl, String videoImageUrl, List<PostLabelInfo> postLabelList, final boolean isLoading, long postId, long storeId,int isRecurit) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.IS_ANONYMOUS, isAnonymous);
        map.put(Constants.Fields.TOPIC_TYPE, topicType);
        map.put(Constants.Fields.VIDEO_URL, videoUrl);
        map.put(Constants.Fields.VIDEO_IMAGE_URL, videoImageUrl);
        map.put(Constants.Fields.POST_LABEL_LIST, postLabelList);
        map.put(Constants.Fields.POST_ID, postId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put(Constants.Fields.IS_RECURIT, isRecurit);
        String tag = mView.getClass().getSimpleName();


        ApiData.getInstance().postData(InterfaceUrl.URL_SAVEPOST, tag, map, new JsonCallback<savePostResponse>(savePostResponse.class) {


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
                    mView.updateView(InterfaceUrl.URL_SAVEPOST, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_SAVEPOST, response.getException());
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


}
