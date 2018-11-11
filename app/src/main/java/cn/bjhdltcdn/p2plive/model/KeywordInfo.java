package cn.bjhdltcdn.p2plive.model;

/**
 * Created by Hu_PC on 2017/11/15.
 */

public class KeywordInfo {
    private long keywordId;//关键字ID,
    private String keywordName;//关键字名称,
    private int contentTotal;//关键字搜索次数,

    public long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(long keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public int getContentTotal() {
        return contentTotal;
    }

    public void setContentTotal(int contentTotal) {
        this.contentTotal = contentTotal;
    }
}
