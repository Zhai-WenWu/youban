package cn.bjhdltcdn.p2plive.utils;

import com.upyun.library.utils.Base64Coder;
import com.upyun.library.utils.UpYunUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.constants.Constants;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by xiawenquan on 17/8/11.
 */

public class UpyunScreenShootUtils {

    public static void process(String source, String save_as, String point, int width, int height, Callback callback) throws Exception {

//        String source = "/sample.mp4";
//        String save_as = "/screenShoot.jpg";
//        String point = "00:00:10";


        JSONObject object = new JSONObject();

        object.put("source", source);
        object.put("save_as", save_as);
        object.put("point", point);
        if (width > 0 && height > 0) {
            object.put("size", width + "x" + height);
        }


        OkHttpClient client = new OkHttpClient.Builder()
                            .readTimeout(30L, TimeUnit.SECONDS)
                            .connectTimeout(30L, TimeUnit.SECONDS)
                            .writeTimeout(30L, TimeUnit.SECONDS)
//                            .addInterceptor(new LoggerInterceptor("OkHttpClient", BuildConfig.LOG_DEBUG))
                            .build();


        MediaType type = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(type, object.toString().getBytes());

        String date = getGMTDate();

        String sign = sign("POST", date, "/" + Constants.KEY.BUCKET_NAME + "/snapshot", Constants.KEY.OPERATOR_NAME, md5(Constants.KEY.OPERATOR_PWD), null).trim();

        Request.Builder builder = new Request.Builder()
                .url("http://p1.api.upyun.com/" + Constants.KEY.BUCKET_NAME + "/snapshot")
                .post(body)
                .header("Date", date)
                .header("Authorization", sign);

        client.newCall(builder.build()).enqueue(callback);

    }

    public static String sign(String method, String date, String path, String userName, String password, String md5) throws Exception {

        StringBuilder sb = new StringBuilder();
        String sp = "&";
        sb.append(method);
        sb.append(sp);
//        sb.append("/" + bucket + path);
        sb.append(path);

        sb.append(sp);
        sb.append(date);

        if (md5 != null) {
            sb.append(sp);
            sb.append(md5);
        }
        String raw = sb.toString().trim();
        byte[] hmac = null;
        hmac = UpYunUtils.calculateRFC2104HMACRaw(password, raw);

        if (hmac != null) {
            return "UPYUN " + userName + ":" + Base64Coder.encodeLines(hmac);
        }
        return null;
    }

    /**
     * 获取 GMT 格式时间戳
     *
     * @return GMT 格式时间戳
     */
    public static String getGMTDate() {
        SimpleDateFormat formater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        formater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formater.format(new Date());
    }

    /**
     * 计算md5Ø
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MessageDigest不支持MD5Util", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
