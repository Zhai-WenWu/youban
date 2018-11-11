package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Description:
 * Data: 2018/9/18
 *
 * @author: zhudi
 */
public interface BrandshopContract {

    interface Presenter extends BasePresenter {

        /**
         * 查询全部品牌商店铺列表接口
         *
         * @param pageNumber 当前页
         * @param pageSize   每页显示的数量
         */
        void getBrandShopInfoList(int pageNumber, int pageSize);
    }
}
