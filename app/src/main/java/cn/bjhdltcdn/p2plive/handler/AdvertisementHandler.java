package cn.bjhdltcdn.p2plive.handler;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;

import java.lang.ref.WeakReference;

import cn.bjhdltcdn.p2plive.ui.activity.MainActivity;
import cn.bjhdltcdn.p2plive.ui.fragment.BaseFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.HomeBannerFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.HomeRecommendFragment;

/**
 * Created by huwenhua on 2016/10/13.
 */

public class AdvertisementHandler extends Handler {
    /**
     * 请求更新显示的View。 
     */
    public static final int MSG_UPDATE_IMAGE  = 1;
    /**
     * 请求暂停轮播。 
     */
    public static final int MSG_KEEP_SILENT   = 2;
    /**
     * 请求恢复轮播。 
     */
    public static final int MSG_BREAK_SILENT  = 3;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。 
     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页， 
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。 
     */
    public static final int MSG_PAGE_CHANGED  = 4;

    //轮播间隔时间  
    public static final long MSG_DELAY = 5000;

    //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等  
    private WeakReference<BaseFragment> weakReference;
    private int SquareCurrentItem = 0;
    private int homeBannerCurrentItem = 0;
    HomeRecommendFragment homeFragment = null;
    HomeBannerFragment homeBannerFragment=null;

    public AdvertisementHandler(WeakReference<BaseFragment> wk){
        weakReference = wk;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.d("advertisementHandler", "receive message " + msg.what);
        BaseFragment baseFragment = weakReference.get();
        if (baseFragment==null){
            //Activity已经回收，无需再处理UI了  
            return ;
        }

        if(baseFragment instanceof HomeRecommendFragment){
            homeFragment=(HomeRecommendFragment)baseFragment;
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (homeFragment.handler.hasMessages(MSG_UPDATE_IMAGE)){
                homeFragment.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    SquareCurrentItem++;
                    Log.d("advertisementHandler", "SquareCurrentItem=" + SquareCurrentItem);
                    homeFragment.viewPager.setCurrentItem(SquareCurrentItem);
                    Log.d("advertisementHandler", "准备下次播放");
//                    //准备下次播放
//                    homeFragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    homeFragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    SquareCurrentItem = msg.arg1;
                    //准备下次播放
                    homeFragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                default:
                    break;
            }
        }else if(baseFragment instanceof HomeBannerFragment){
            homeBannerFragment=(HomeBannerFragment)baseFragment;
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (homeBannerFragment.handler.hasMessages(MSG_UPDATE_IMAGE)){
                homeBannerFragment.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    homeBannerCurrentItem++;
                    Log.d("advertisementHandler", "homeBannerCurrentItem=" + homeBannerCurrentItem);
                    homeBannerFragment.viewPager.setCurrentItem(homeBannerCurrentItem);
                    Log.d("advertisementHandler", "准备下次播放");
//                    //准备下次播放
//                    homeBannerFragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    homeBannerFragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    homeBannerCurrentItem = msg.arg1;
                    //准备下次播放
                    homeBannerFragment.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                default:
                    break;
            }
        }

    }

    public void clearData(){
        if (weakReference != null) {
            weakReference.clear();
            weakReference = null;
        }

        if (homeFragment != null) {
            homeFragment = null;
        }

        if (homeBannerFragment != null) {
            homeBannerFragment = null;
        }

    }

}
