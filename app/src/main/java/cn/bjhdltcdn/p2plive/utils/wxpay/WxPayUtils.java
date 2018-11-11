package cn.bjhdltcdn.p2plive.utils.wxpay;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by huwenhua on 2016/7/18.
 */
public class WxPayUtils {
    private static WxPayUtils wxPayUtils;
    private IWXAPI api;
    private WxPayUtils(){}


    public  static WxPayUtils getInstance() {
        if (wxPayUtils == null) {
            wxPayUtils = new WxPayUtils();
        }
        return wxPayUtils;
    }

    public void pay(Context context,String signData) {
        api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(Constants.KEY.KEY_WXPAY);
        Log.e("PAY_GET", "获取订单中...");
//        Toast.makeText(context, "获取订单中...", Toast.LENGTH_SHORT).show();
        try {
            if (!StringUtils.isEmpty(signData)) {
                JSONObject json = new JSONObject(signData);
                if (null != json && !json.has("retcode")) {
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    req.extData = "app data"; // optional
//                    Toast.makeText(context, "正常调起支付", Toast.LENGTH_SHORT).show();
                    Log.e("PAY_GET", "正常调起支付");
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    Log.e("PAY_GET", "返回错误" + json.getString("retmsg"));
                    Toast.makeText(context, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("PAY_GET", "服务器请求错误");
                Toast.makeText(context, "服务器请求错误", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
            Toast.makeText(context, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onDestroy(){

        if (api != null) {
            api.detach();
        }
        api = null;

        if (wxPayUtils != null) {
            wxPayUtils = null;
        }
    }

}

