package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by ZHUDI on 2017/11/17.
 */

public interface DiscoverContract {
    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 查询发现页数据接口
         *
         * @param userId     操作用户id
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         */
        void getDiscoverList(long userId, int pageSize, int pageNumber, boolean isNeedShowLoading);

        /**
         * 查询标签接口
         *
         * @param type 类型(1房间,2比赛,3首页排序,4试用报告,5用户订单排序,6店铺订单排序,7校园店排序(本校/同城/周边),8校园店类别,9助力打call,10送货方式,11聊天室)默认1
         */
        void getLabelList(int type);

        /**
         * 查询PK列表接口
         */
        void getPKList(long userId, long launchId, int pageSize, int pageNumber);

        /**
         * 查询PK列表接口
         */
        void getSavePlayInfo(long userId, long launchId, String playTitle, String videoUrl, String videoImageUrl);

        /**
         * 发起PK挑战接口
         *
         * @param userId        用户Id
         * @param title         发起标题
         * @param description   发起描述
         * @param playTitle     挑战标题
         * @param videoUrl      挑战视频路径
         * @param videoImageUrl 挑战图片地址
         */
        void saveLaunchPlay(long userId, String title, String description, String playTitle, String videoUrl, String videoImageUrl);

        /**
         * 发起PK挑战接口
         *
         * @param userId 用户Id
         * @param sort   排序
         */
        void getAllPlayList(long userId, int sort, int pageSize, int pageNumber, boolean needShowLoading);


        /**
         * 查询二级标签接口
         * @param labelId
         */
        void getSecondLabelList(long labelId);
    }
}
