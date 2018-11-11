package cn.bjhdltcdn.p2plive.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ReportType implements Parcelable {

    private long reportTypeId ;
    private String reportName ;
    private String reprotDesc ;
    private String addTime ;


    public long getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(long reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReprotDesc() {
        return reprotDesc;
    }

    public void setReprotDesc(String reprotDesc) {
        this.reprotDesc = reprotDesc;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.reportTypeId);
        dest.writeString(this.reportName);
        dest.writeString(this.reprotDesc);
        dest.writeString(this.addTime);
    }

    public ReportType() {
    }

    protected ReportType(Parcel in) {
        this.reportTypeId = in.readLong();
        this.reportName = in.readString();
        this.reprotDesc = in.readString();
        this.addTime = in.readString();
    }

    public static final Parcelable.Creator<ReportType> CREATOR = new Parcelable.Creator<ReportType>() {
        @Override
        public ReportType createFromParcel(Parcel source) {
            return new ReportType(source);
        }

        @Override
        public ReportType[] newArray(int size) {
            return new ReportType[size];
        }
    };
}
