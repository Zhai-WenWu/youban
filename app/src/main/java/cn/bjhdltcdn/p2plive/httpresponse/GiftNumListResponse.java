package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.LinkedHashMap;

/**
 * Created by ZHUDI on 2017/1/4.
 */

public class GiftNumListResponse extends BaseResponse {
    private LinkedHashMap giftNumMap;//{Map对象<1,一心一意>}

    public LinkedHashMap getGiftNumMap() {
        return giftNumMap;
    }

    public void setGiftNumMap(LinkedHashMap giftNumMap) {
        this.giftNumMap = giftNumMap;
    }
}
