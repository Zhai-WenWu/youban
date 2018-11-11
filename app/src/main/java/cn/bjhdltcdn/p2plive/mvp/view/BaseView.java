package cn.bjhdltcdn.p2plive.mvp.view;

import android.app.Activity;

public interface BaseView {

    /**
     * UI回调
     * @param apiName 接口名称
     * @param object 返回数据类型
     */
    void updateView(String apiName, Object object);

    /**
     * 显示正在加载进度框
     */
    void showLoading();
    /**
     * 隐藏正在加载进度框
     */
    void hideLoading();

}