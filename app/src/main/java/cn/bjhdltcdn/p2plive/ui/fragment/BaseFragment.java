package cn.bjhdltcdn.p2plive.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import cn.bjhdltcdn.p2plive.constants.Constants;


/**
 * Created by wenquan on 2016/6/6.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 是否初始化
     */
    private boolean isOnInit = true;
    /**
     * 是否对用户可见
     */
    private boolean isVisibleToUser = false;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && isSaveState()) {
            isOnInit = savedInstanceState.getBoolean(Constants.KEY.KEY_TAB_INIT);
        }

        if (isVisibleToUser) {
            onVisible(isOnInit);
            isOnInit = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;

        if (isVisibleToUser && getView() != null) {
            onVisible(isOnInit);
            isOnInit = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (isSaveState()) {
            outState.putBoolean(Constants.KEY.KEY_TAB_INIT, isOnInit);
        }
    }


    /**
     * 是否保存初始化状态
     *
     * @return
     */
    public boolean isSaveState() {
        return true;
    }

    /**
     * 监听Fragment是否显示，isInit是否初为首次初始化，当把Fragment加入TabFragment时使用
     *
     * 由于ViewPager是预加载，所以联网获取数据需要判断当前Fragment是否显示，然后在获取数据
     */
    protected abstract void onVisible(boolean isInit);


    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

}
