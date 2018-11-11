package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface GetRecommendListContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 查询帖子列表
         * @param organId 圈子Id(Id=0查询全部),
         * @param sort 排序(1最新发布,2离我最近,3最热帖子),
         * @param labelId 标签ID(新版本使用),
         * @param pageSize 页面大小
         * @param pageNumber 第几页,
         * @param b
         */
        void getPostList(long userId, long organId, int sort,long labelId, int pageSize, int pageNumber, boolean b);

        /**
         * 获取首页banner列表
         * @param userId 用户Id
         * @param type 类型 (1 banner,2 社团详情,3 发现页,4 商圈 5 官方轮播图片 ),
         */
        void getHomeBannerList(long userId, int type);

        /**
         * 获取首页推荐圈子接口
         * @param userId 用户Id,
         * @param pageSize 页面大小,
         * @param pageNumber 第几页,
         * @param show 是否展示加载框
         */
        void getRecommendOrganList(long userId,int pageSize, int pageNumber,boolean show);

        /**
         * 查询帖子常驻标签接口
         * @param userId 用户Id
         */
        void getCommonLabelInfo(long userId);

    }
}