package cn.bjhdltcdn.p2plive.httpresponse;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bjhdltcdn.p2plive.model.HomeInfo;

/**
 * Created by ZHAI on 2018/3/26.
 */

public class FindFollowDetailResponse extends BaseResponse implements Parcelable {
    public HomeInfo homeInfo;

    public HomeInfo getHomeInfo() {
        return homeInfo;
    }

    public void setHomeInfo(HomeInfo homeInfo) {
        this.homeInfo = homeInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.homeInfo, flags);
    }

    public FindFollowDetailResponse() {
    }

    protected FindFollowDetailResponse(Parcel in) {
        this.homeInfo = in.readParcelable(HomeInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<FindFollowDetailResponse> CREATOR = new Parcelable.Creator<FindFollowDetailResponse>() {
        @Override
        public FindFollowDetailResponse createFromParcel(Parcel source) {
            return new FindFollowDetailResponse(source);
        }

        @Override
        public FindFollowDetailResponse[] newArray(int size) {
            return new FindFollowDetailResponse[size];
        }
    };
}
