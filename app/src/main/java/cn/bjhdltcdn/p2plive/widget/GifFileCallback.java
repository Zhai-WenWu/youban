package cn.bjhdltcdn.p2plive.widget;

import android.content.Intent;
import android.net.Uri;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.IOException;

import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by xiawenquan on 18/3/8.
 */

public class GifFileCallback extends AbsCallback<File> {

    private GifFileConvert convert;

    private String suffix;



    public GifFileCallback(String suffix) throws IOException {
        this.suffix = suffix;
        this.convert = new GifFileConvert(suffix);
    }



    @Override
    public File convertResponse(okhttp3.Response response) throws Throwable {
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }

    @Override
    public void onSuccess(Response<File> response) {
        File file = response.body();
        if (file != null && file.exists()) {
            Utils.showToastShortTime("保存动图成功");

            // 最后通知图库更新
            App.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));

        } else {
            Utils.showToastShortTime("保存动图失败");
        }
    }
}
