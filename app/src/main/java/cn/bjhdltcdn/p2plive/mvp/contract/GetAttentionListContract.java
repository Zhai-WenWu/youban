package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface GetAttentionListContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 查询我的关注接
         * @param userId 用户Id,
         * @param pageSize 每页大小,
         * @param pageNumber  第几页,
         * @param version  版本(1.0新版本),
         */
        void getAttentionList(long userId,int pageSize,int pageNumber,String version);

    }
}