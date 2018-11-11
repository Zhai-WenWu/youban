package cn.bjhdltcdn.p2plive.widget;

import com.lzy.okgo.convert.Converter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.bjhdltcdn.p2plive.utils.FileUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xiawenquan on 18/3/8.
 */

public class GifFileConvert implements Converter<File> {

    private File dest;

    public GifFileConvert(String suffix) throws IOException {
        this.dest = FileUtils.getExternalStoragePublicDirectory();

        if (dest != null) {
            if (!dest.exists()) {
                dest.mkdirs();
            }

            dest = new File(dest,System.currentTimeMillis() + (StringUtils.isEmpty(suffix) ? ".gif" : suffix));

            if (!dest.exists() && dest.isFile()) {
                dest.createNewFile();
            }
        }
    }

    @Override
    public File convertResponse(Response response) throws Throwable {

        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        return parse(body.byteStream());

    }

    private File parse(InputStream inputStream) throws IOException {

        if (dest == null) {
            return null;
        }

        Logger.d("dest === " + dest);

        InputStream input = null;
        OutputStream output = null;
        try {
            input = inputStream;
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }


        }

        return dest;

    }
}
