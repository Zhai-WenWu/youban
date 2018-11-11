package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface GetHotWordsListContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 查询热门搜索关键字列表
         */
        void getHotSearchFieldList();

        /**
         * 关键字搜索
         * @param userId  用户Id,
         * @param content 输入的内容,
         */
        void searchKeywordList(long userId, String content);

        /**
         * 查询关键字列表
         * @param userId 用户Id
         * @param content 输入的内容,
         * @param type 类型(1圈子,2群组,3用户,4线下活动),
         * @param pageSize 页面大小,
         * @param pageNumber 第几页,
         */
        void getSearchDataList(long userId, String content, int type, int pageSize, int pageNumber);
    }
}