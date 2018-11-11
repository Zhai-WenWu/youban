package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetBrandShopInfoListResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.BrandshopContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Description:
 * Data: 2018/9/18
 *
 * @author: zhudi
 */
public class BrandShopPresenter implements BrandshopContract.Presenter {

    private BaseView mView;

    public BrandShopPresenter(BaseView mView) {
        this.mView = mView;
    }

    @Override
    public void getBrandShopInfoList(int pageNumber, int pageSize) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETBRANDSHOPINFOLIST, tag, map, new JsonCallback<GetBrandShopInfoListResponse>(GetBrandShopInfoListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetBrandShopInfoListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETBRANDSHOPINFOLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETBRANDSHOPINFOLIST, response.getException());
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
    public void cancelTag(String tag) {

    }

    @Override
    public void onDestroy() {

    }
}
