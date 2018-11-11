package cn.bjhdltcdn.p2plive.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ClosePayActivityEvent;
import cn.bjhdltcdn.p2plive.event.GoodsPaySuccessEvent;
import cn.bjhdltcdn.p2plive.event.UpdatePayResultEvent;
import cn.bjhdltcdn.p2plive.event.ZfbPayResultEvent;
import cn.bjhdltcdn.p2plive.ui.activity.BaseActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.TipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.BrowserFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.GoodsPayFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.PayFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.alipay.ZfbPayUtils;
import cn.bjhdltcdn.p2plive.utils.wxpay.WxPayUtils;

/**
 * 微信客户端回调activity
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;
    private PayFragment payFragment;
    private BrowserFragment browserFragment;
    private GoodsPayFragment goodsPayFragment;
    private int type;//1:app支付界面0：加载网页界面2:商品支付界面

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_pay_entry_layout);
        EventBus.getDefault().register(this);

        api = WXAPIFactory.createWXAPI(this, Constants.KEY.KEY_WXPAY);
        api.handleIntent(getIntent(), this);

        int type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        if (type == 1) {
            payFragment = (PayFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (payFragment == null) {
                payFragment = new PayFragment();
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), payFragment, R.id.content_frame);

        } else if (type == 0) {

            browserFragment = (BrowserFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (browserFragment == null) {
                browserFragment = new BrowserFragment();
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), browserFragment, R.id.content_frame);

        } else if (type == 2) {
            goodsPayFragment = (GoodsPayFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (goodsPayFragment == null) {
                goodsPayFragment = new GoodsPayFragment();
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), goodsPayFragment, R.id.content_frame);

        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(payFragment!=null){
                TipDialog tipDialog=new TipDialog();
                switch (resp.errCode){
                    case 0:
                        tipDialog.setContent("支付结果：成功");
                        EventBus.getDefault().post(new UpdatePayResultEvent());
                        break;
                    case -1:
                        tipDialog.setContent("支付结果：失败");
                        break;
                    case -2:
                        tipDialog.setContent("支付结果：取消支付");
                        break;
                    default:
                        tipDialog.setContent("支付结果：失败");
                        break;
                }
                tipDialog.show(getSupportFragmentManager());
            }else if(browserFragment!=null){
                browserFragment.JavaJs(1,resp.errCode+"");
            }
        }
    }


	//接收充值成功消息
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMainThread(ClosePayActivityEvent event) {
		if (event == null) {
			return;
		}
		if(payFragment!=null){
			finish();
		}
		if(goodsPayFragment!=null){
		    EventBus.getDefault().post(new GoodsPaySuccessEvent(goodsPayFragment.getProductOrder()));
            finish();
        }

	}

    //浏览器支付宝结果回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ZfbPayResultEvent event) {
        if (event == null) {
            return;
        }
        if(browserFragment!=null){
            browserFragment.JavaJs(2,event.getCode());
        }
    }

    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (browserFragment != null) {
            return browserFragment.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (browserFragment != null) {
            browserFragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (api != null) {
            api.detach();
        }
        api = null;

        WxPayUtils.getInstance().onDestroy();
        ZfbPayUtils.getInstance().onDestroy();

        if (browserFragment != null) {
            browserFragment = null;
        }

        if (payFragment != null) {
            payFragment = null;
        }

        if (goodsPayFragment != null) {
            goodsPayFragment = null;
        }

    }

}