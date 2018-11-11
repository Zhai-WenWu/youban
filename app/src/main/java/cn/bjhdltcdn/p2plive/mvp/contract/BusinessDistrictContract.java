package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by xiawenquan on 18/5/28.
 */

public interface BusinessDistrictContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 查询首页商圈列表接口
         * @param userId 用户id
         */
        void getTradAreaList(long userId);


    }
}
