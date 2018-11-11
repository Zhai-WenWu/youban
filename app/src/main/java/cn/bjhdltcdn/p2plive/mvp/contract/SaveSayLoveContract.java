package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface SaveSayLoveContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         *  发起表白接口
         * @param content 表白内容,
         *@param userId 发起用户Id,
         *@param isAnonymous 是否匿名(1匿名,2不匿名),
         *@param confessionType 表白类型(1-->普通图文，2--->视频),
         *@param videoUrl 表白视频路径(类型为视频使用),
         *@param videoImageUrl 表白图片地址(类型为视频使用),
         *@param sayLoveId 表白Id(跟拍时传入值,其他默认0)
         */

        void saveSayLove(long userId, long toUserId, String content, int isAnonymous, int confessionType, String videoUrl, String videoImageUrl, long labelId, long[] secondLabelIds, long sayLoveId);

        /**
         * 视频截图（M3U8 截图）
         *
         * @param source  (必填)视频的存储地址
         * @param save_as (必填)截图保存地址
         * @param point   (必填)截图时间点，格式为 HH:MM:SS
         * @param width   图片的宽度
         * @param height  图片的高度
         */

        void upYunSnapshot(String source, String save_as, String point,int width,int height);


    }
}