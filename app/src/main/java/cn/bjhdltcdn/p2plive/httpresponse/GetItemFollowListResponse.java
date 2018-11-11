package cn.bjhdltcdn.p2plive.httpresponse;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HomeInfo;

/**
 * Created by ZHAI on 2018/3/26.
 */

public class GetItemFollowListResponse extends BaseResponse implements Parcelable {
    public List<HomeInfo> list;
    public int total;
    private int status;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<HomeInfo> getList() {
        return list;
    }

    public void setList(List<HomeInfo> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public GetItemFollowListResponse() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
        dest.writeInt(this.total);
        dest.writeInt(this.status);
    }

    protected GetItemFollowListResponse(Parcel in) {
        this.list = in.createTypedArrayList(HomeInfo.CREATOR);
        this.total = in.readInt();
        this.status = in.readInt();
    }

    public static final Creator<GetItemFollowListResponse> CREATOR = new Creator<GetItemFollowListResponse>() {
        @Override
        public GetItemFollowListResponse createFromParcel(Parcel source) {
            return new GetItemFollowListResponse(source);
        }

        @Override
        public GetItemFollowListResponse[] newArray(int size) {
            return new GetItemFollowListResponse[size];
        }
    };
}
