package cn.bjhdltcdn.p2plive.widget.imkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.ArrayMap;

import com.lzy.okgo.model.Response;

import java.util.Map;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetBaseUserInfoResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.ui.activity.VideoChatActivity;
import cn.bjhdltcdn.p2plive.utils.Utils;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by Hu_PC on 2017/8/24.
 */

public class CallPlugin implements IPluginModule {

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.plugin_video_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        return "呼叫Ta";
    }

    @Override
    public void onClick(final Fragment fragment, final RongExtension rongExtension) {
        Map map = new ArrayMap(1);
        map.put(Constants.Fields.USER_ID, rongExtension.getTargetId());
        ApiData.getInstance().postData(InterfaceUrl.URL_GETBASEUSERINFO, "", map, new JsonCallback<GetBaseUserInfoResponse>(GetBaseUserInfoResponse.class) {
            @Override
            public void onSuccess(Response response) {
                if (response.body() instanceof GetBaseUserInfoResponse) {
                    GetBaseUserInfoResponse getBaseUserInfoResponse = (GetBaseUserInfoResponse) response.body();
                    if (getBaseUserInfoResponse.getCode() == 200) {

                        BaseUser baseUser = getBaseUserInfoResponse.getUser();
                        if (baseUser == null) {
                            return;
                        }
                        Intent intent = new Intent(App.getInstance(), VideoChatActivity.class);
                        intent.putExtra(Constants.Fields.BASEUSER, baseUser);
                        intent.putExtra(Constants.Fields.TYPE, 5);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.Fields.ROOMNUMBER, Utils.getRoomNumber());
                        App.getInstance().startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

}
