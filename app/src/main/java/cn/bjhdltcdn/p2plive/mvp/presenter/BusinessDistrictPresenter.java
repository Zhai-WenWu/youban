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
import cn.bjhdltcdn.p2plive.httpresponse.GetOrganizationListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetTradAreaListResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.BusinessDistrictContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by xiawenquan on 18/5/28.
 */

public class BusinessDistrictPresenter implements BusinessDistrictContract.Presenter {

    private BaseView mView;

    public BusinessDistrictPresenter(BaseView mView) {
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
    public void getTradAreaList(long userId) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_GETTRADAREALIST, tag, map, new JsonCallback<GetTradAreaListResponse>(GetTradAreaListResponse.class) {


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
                    mView.updateView(InterfaceUrl.URL_GETTRADAREALIST, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETTRADAREALIST, response.getException());
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
}
