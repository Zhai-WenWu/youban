package cn.bjhdltcdn.p2plive.callback;

import android.text.TextUtils;

import com.lzy.okgo.callback.AbsCallback;
import com.orhanobut.logger.Logger;

import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import okhttp3.ResponseBody;

/**
 * Created by xiawenquan on 17/4/12.
 */

public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Class<T> calzz;

    public JsonCallback(Class<T> calzz) {
        this.calzz = calzz;
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {

        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        String responseStr = response.body().string();

        Logger.json(responseStr);

        if (TextUtils.isEmpty(responseStr)) {
            // 默认给的数据
            responseStr = "{\n" +
                    "    \"code\":0,\n" +
                    "    \"msg\": \"\"\n" +
                    "}";
        }


        T t = JsonUtil.getObjectMapper().readValue(responseStr, calzz);
        response.close();

        if (calzz != null) {
            calzz = null;
        }

        return t;

    }
}
