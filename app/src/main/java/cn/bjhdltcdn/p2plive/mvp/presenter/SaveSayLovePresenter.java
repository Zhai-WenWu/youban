package cn.bjhdltcdn.p2plive.mvp.presenter;

import android.os.Handler;
import android.os.Looper;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.SaveSayLoveContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.UpyunScreenShootUtils;
import okhttp3.Call;

public class SaveSayLovePresenter implements SaveSayLoveContract.Presenter {

    private BaseView mView;

    public SaveSayLovePresenter(BaseView mView) {
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
    public void saveSayLove(long userId, long toUserId, String content, int isAnonymous, int confessionType, String videoUrl, String videoImageUrl, long labelId, long[] secondLabelIds, long sayLoveId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put("content", content);
        map.put("content", content);
        map.put("isAnonymous", isAnonymous);
        map.put("confessionType", confessionType);
        map.put("videoUrl", videoUrl);
        map.put("videoImageUrl", videoImageUrl);
        map.put(Constants.Fields.LABEL_ID, labelId);
        map.put(Constants.Fields.SECOND_LABEL_IDS, secondLabelIds);
        map.put(Constants.Fields.SAY_LOVE_ID, sayLoveId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVESAYLOVE, tag, map, new JsonCallback<NoParameterResponse>(NoParameterResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_SAVESAYLOVE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVESAYLOVE, response.body());
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
    public void upYunSnapshot(String source, String save_as, String point, int width, int height) {

        try {
            if (mView != null) {
                mView.showLoading();
            }

            UpyunScreenShootUtils.process(source, save_as, point, width, height, new okhttp3.Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
//                    KLog.e(e);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (mView != null) {
                                mView.hideLoading();
                            }

                        }
                    });

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPYUN_SNAPSHOT, null);
                    }

                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (mView != null) {
                                mView.hideLoading();
                            }

                        }
                    });

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPYUN_SNAPSHOT, response);
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
