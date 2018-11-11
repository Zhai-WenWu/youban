package io.rong.imkit;

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
import io.rong.imlib.model.UserInfo;
import io.rong.message.MediaMessageContent;
import io.rong.message.TextMessage;

/**
 * Created by xiawenquan on 17/12/28.
 */
@MessageTag(value = "RC:YouBanApplyCreateStoreResultMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class YouBanApplyCreateStoreResultMessage extends TextMessage {

    public static final Creator<YouBanApplyCreateStoreResultMessage> CREATOR = new Creator<YouBanApplyCreateStoreResultMessage>() {
        public YouBanApplyCreateStoreResultMessage createFromParcel(Parcel source) {
            return new YouBanApplyCreateStoreResultMessage(source);
        }

        public YouBanApplyCreateStoreResultMessage[] newArray(int size) {
            return new YouBanApplyCreateStoreResultMessage[size];
        }
    };


    protected YouBanApplyCreateStoreResultMessage() {
        super();
    }

    public YouBanApplyCreateStoreResultMessage(byte[] data) {
        super(data);
    }

    public YouBanApplyCreateStoreResultMessage(Parcel in) {
        super(in);
    }

    public YouBanApplyCreateStoreResultMessage(String content) {
        super(content);
    }
}
