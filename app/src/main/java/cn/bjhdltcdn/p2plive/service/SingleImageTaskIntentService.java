package cn.bjhdltcdn.p2plive.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.utils.BitmapUtils;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.NetUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;


/**
 * 图片按照顺序单张上传
 */
public class SingleImageTaskIntentService extends IntentService {


    public SingleImageTaskIntentService() {
        super("SingleImageTaskIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_LOW);

            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, "id").build();

            startForeground(1, notification);
        }

    }

    public static void startUploadImg(long parentId, int type, long userId, String photoGraphTime, String fileUrl, long imageId, int orderNum, int total) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PHOTOGRAPH_TIME, photoGraphTime);
        map.put(Constants.Fields.FILE, fileUrl);
        map.put(Constants.Fields.ORDERNUM, orderNum);
        map.put(Constants.Fields.IMAGE_ID, imageId);
        map.put(Constants.Fields.TOTAL, total);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY.KEY_EXTRA, (Serializable) map);

        Intent intent = new Intent(App.getInstance(), SingleImageTaskIntentService.class);
        intent.putExtras(bundle);

        intent.setAction(Constants.Action.ACTION_UPLOAD_IMG);
        if (Build.VERSION.SDK_INT >= 26) {
            App.getInstance().startForegroundService(intent);
        } else {
            // Pre-O behavior.
            App.getInstance().startService(intent);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.Action.ACTION_UPLOAD_IMG.equals(action)) {

                boolean isConnected = NetUtils.isConnected(this);
                if (!isConnected) {
                    Utils.showToastShortTime("当前网络不可用");
                }

                Logger.d("开始上传任务..............................");

                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object object = bundle.getSerializable(Constants.KEY.KEY_EXTRA);
                    if (object != null) {
                        if (object instanceof Map) {
                            Map map = (Map) object;

                            UploadImageResponse response = null;
                            HttpURLConnection con = null;
                            try {

                                String end = "\r\n";
                                String twoHyphens = "--";
                                String boundary = "****************************" + System.currentTimeMillis();

                                String fileUrl = (String) map.get(Constants.Fields.FILE);

                                File uploadFile = new File(fileUrl);
                                String mime = BitmapUtils.getMIMe(fileUrl);

//                                if (mime.contains("bmp")) {// 转换为JPEG图片
//                                    uploadFile = FileUtils.CompressImageToJPEG(fileUrl);
//                                }

                                Logger.d("上传路径 === " + fileUrl);

                                String requestUrl = ApiData.getInstance().getHost() + InterfaceUrl.URL_UPLOADIMAGE + ".do";

                                Logger.v("request<========requestUrl===============" + requestUrl);

                                URL url = new URL(requestUrl);
                                con = (HttpURLConnection) url.openConnection();
                                /* 允许Input、Output，不使用Cache */
                                con.setDoOutput(true);
                                con.setUseCaches(false);
                                //设置连接主机超时（单位：毫秒）
                                con.setConnectTimeout(10000);
                                //设置从主机读取数据超时（单位：毫秒）五分钟时长
                                con.setReadTimeout(300000);

                                /* 设置传送的method=POST */
                                con.setRequestMethod("POST");
                                /* setRequestProperty 添加头信息*/
                                con.setRequestProperty("Connection", "Keep-Alive");
                                con.setRequestProperty("Charset", "UTF-8");
                                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                                con.connect();

                                //写完HTTP请求头后根据HTTP协议再写一个回车换行
                                /* 设置DataOutputStream */
                                DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                                //写完HTTP请求头后根据HTTP协议再写一个回车换行
                                ds.write(end.getBytes());
                                //把所有文本类型的实体数据发送出来
                                StringBuilder textEntity = new StringBuilder();
                                if (map != null && map.size() > 0) {
                                    Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
                                    while (entries.hasNext()) {
                                        Map.Entry<String, Object> entry = entries.next();
                                        if (Constants.Fields.FILE.equals(entry.getKey())) {
                                            continue;
                                        }

                                        if (entry.getValue() != null) {
                                            textEntity.append(twoHyphens);
                                            textEntity.append(boundary);
                                            textEntity.append(end);
                                            textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                                            textEntity.append(entry.getValue());
                                            textEntity.append(end);
                                        }
                                        // 请求原始数据
                                        Logger.d(entry.getKey() + "=====" + entry.getValue());

                                    }

                                }

                                textEntity.append(twoHyphens);
                                textEntity.append(boundary);
                                textEntity.append(end);
                                textEntity.append("Content-Disposition: form-data;name=\"" + "file" + "\";filename=\"" + uploadFile.getName() + "\"\r\n");
                                textEntity.append("Content-Type: image/" + mime + "\r\n\r\n");
                                /**
                                 * 这里重点注意：
                                 * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                                 * filename是文件的名字，包含后缀名的 比如:abc.png
                                 */

                                ds.write(textEntity.toString().getBytes());

                                Logger.d("textEntity ==== " + textEntity.toString());

                                if (uploadFile.exists() && uploadFile.isFile()){
                                    Logger.d("uploadFile.length() ==== " + uploadFile.length());
                                    // 文件大于100k就压缩
                                    if (uploadFile.length() <= 100 * 1024) {

                                        InputStream in = null;
                                        byte[] tempBytes = null;
                                        try {
                                            //System.out.println("以字节为单位读取文件内容，一次读取2K字节：");
                                            // 一次读多个字节
                                            tempBytes = new byte[2 * 1024];
                                            int byteRead = 0;
                                            in = new FileInputStream(uploadFile);
                                            // 读入多个字节到字节数组中，tempBytes 为一次读入的字节数
                                            while ((byteRead = in.read(tempBytes)) != -1) {
                                                ds.write(tempBytes,0,byteRead);
                                            }

                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        } finally {
                                            if (in != null) {
                                                try {
                                                    in.close();
                                                } catch (IOException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }

                                            if (tempBytes != null) {
                                                tempBytes = null;
                                            }
                                        }

                                    } else {

                                        //TODO  开始压缩图片
//                                        byte[] imageBytes = BitmapUtils.doParse(fileUrl, 0, 0, ImageView.ScaleType.CENTER_INSIDE);
                                        byte[] imageBytes = BitmapUtils.doParse(fileUrl);

                                        if (imageBytes == null || imageBytes.length <= 0) {

                                            ds.flush();
                                            ds.close();

                                            if (con != null) {
                                                con.disconnect();
                                            }
                                            Utils.showToastShortTime("图片压缩失败");

                                            return;
                                        }

                                        ds.write(imageBytes);

                                    }
                                }

                                ds.writeBytes(end);
                                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                                ds.flush();
                                ds.close();

                                int code = con.getResponseCode();
                                Logger.e("code ===" + code);


                                /* 取得Response内容 */
                                StringBuffer buffer1 = new StringBuffer();
                                InputStream is = con.getInputStream();
                                if (is != null) {

                                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                    String lineText = null;
                                    // 一次读入一行，直到读入null为文件结束
                                    while ((!StringUtils.isEmpty(lineText = reader.readLine()))) {
                                        buffer1.append(lineText.trim());
                                    }

                                    reader.close();
                                }

                                is.close();
                                con.disconnect();

                                String responseStr = buffer1.toString().trim();
                                Logger.d("responseStr == " + responseStr);

                                if (!StringUtils.isEmpty(responseStr)) {

                                    response = JsonUtil.getObjectMapper().readValue(responseStr, UploadImageResponse.class);

                                    if (response == null || response.getCode() != 200) {
                                        Logger.d("有一张图片上传失败! === " + fileUrl);
                                    } else {
                                        Logger.d("图片上传成功! === " + fileUrl);

                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                Logger.d("上传图片过程中发生错误了! === ");

                            } finally {
                                if (con != null) {
                                    con.disconnect();
                                }

                                if (response != null) {
                                    response = null;
                                }

                                Logger.d("结束上传任务..............................");
                            }

                        }
                    }
                }
            }
        }
    }

    public void uploadImage() {

    }

}
