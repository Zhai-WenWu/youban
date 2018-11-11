package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hu_PC on 2018/5/11.
 */

public class EvaluateCountInfo implements Parcelable {
    private int type;//类型(0全部,1满意,2不满意,3有图),
    private String name;//类型对应名称(0全部,1满意,2不满意,3有图),
    private int typeCount;//数量,
    private boolean isCheck;//客户端标志是否选中


    public EvaluateCountInfo() {
    }

    public EvaluateCountInfo(int type, String name, int typeCount) {
        this.type = type;
        this.name = name;
        this.typeCount = typeCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(int typeCount) {
        this.typeCount = typeCount;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeInt(this.typeCount);
    }

    protected EvaluateCountInfo(Parcel in) {
        this.type = in.readInt();
        this.name = in.readString();
        this.typeCount = in.readInt();
    }

    public static final Creator<EvaluateCountInfo> CREATOR = new Creator<EvaluateCountInfo>() {
        @Override
        public EvaluateCountInfo createFromParcel(Parcel source) {
            return new EvaluateCountInfo(source);
        }

        @Override
        public EvaluateCountInfo[] newArray(int size) {
            return new EvaluateCountInfo[size];
        }
    };
}
