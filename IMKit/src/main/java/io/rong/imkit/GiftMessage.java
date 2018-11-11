package io.rong.imkit;

/**
 * Created by Hu_PC on 2017/8/24.
 */
import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.MediaMessageContent;

@MessageTag(value = "RC:YouBanGiftMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class GiftMessage extends MessageContent {

    private String giftImgUrl;//礼物图片
    private String giftNmae;//礼物名称
    private int giftGold;//礼物金币
    private int giftNum;//礼物个数

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(giftImgUrl)) {
                jsonObj.put("giftImgUrl", giftImgUrl);
            } else {
                Log.d("GiftMessage", "GiftMessage is null");
            }
            if (!TextUtils.isEmpty(giftNmae)) {
                jsonObj.put("giftNmae", giftNmae);
            }
            jsonObj.put("giftGold", giftGold);
            jsonObj.put("giftNum", giftNum);
            if (getJSONUserInfo() != null)
                jsonObj.putOpt("user", getJSONUserInfo());
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GiftMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("giftImgUrl"))
                setGiftImgUrl(jsonObj.optString("giftImgUrl"));
            if (jsonObj.has("giftNmae"))
                setGiftNmae(jsonObj.optString("giftNmae"));
            if (jsonObj.has("giftGold")) {
                setGiftGold(jsonObj.optInt("giftGold"));
            }
            if (jsonObj.has("giftNum")) {
                setGiftNum(jsonObj.optInt("giftNum"));
            }
            if (jsonObj.has("user")) {
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

    public GiftMessage(){}



    public String getGiftImgUrl() {
        return giftImgUrl;
    }

    public void setGiftImgUrl(String giftImgUrl) {
        this.giftImgUrl = giftImgUrl;
    }

    public String getGiftNmae() {
        return giftNmae;
    }

    public void setGiftNmae(String giftNmae) {
        this.giftNmae = giftNmae;
    }

    public int getGiftGold() {
        return giftGold;
    }

    public void setGiftGold(int giftGold) {
        this.giftGold = giftGold;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.giftImgUrl);
        dest.writeString(this.giftNmae);
        dest.writeInt(this.giftGold);
        dest.writeInt(this.giftNum);
    }

    protected GiftMessage(Parcel in) {
        this.giftImgUrl = in.readString();
        this.giftNmae = in.readString();
        this.giftGold = in.readInt();
        this.giftNum = in.readInt();
    }

    public static final Creator<GiftMessage> CREATOR = new Creator<GiftMessage>() {
        @Override
        public GiftMessage createFromParcel(Parcel source) {
            return new GiftMessage(source);
        }

        @Override
        public GiftMessage[] newArray(int size) {
            return new GiftMessage[size];
        }
    };
}
