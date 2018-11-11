package cn.bjhdltcdn.p2plive.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;


/**
 * Created by wenquan on 2015/10/15.
 */
public class FileUtils {


    /**
     * 获取sdcard路径
     *
     * @return
     */
    public static File getExternalStoragePublicDirectory() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
        String fileName = "bjhdltcdn";
        File appDir = new File(file, fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;

    }


    /**
     * 复制文件
     *
     * @param source 源文件
     * @param dest   目标文件
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

        } finally {
            inputChannel.close();
            outputChannel.close();
        }

    }


    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
        String fileName = "bjhdltcdn";
        File appDir = new File(file, fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        fileName = System.currentTimeMillis() + ".jpg";
        File currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
        Toast.makeText(App.getInstance(), "保存图片成功!", Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取sdcard的目录路径
     *
     * @return
     */
    public static String getExternalStorageDirectoryPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            File dir = Environment.getExternalStorageDirectory();
            return dir.getAbsolutePath() + "/Android/data/" + App.getInstance().getPackageName();
        } else {// /data/data/PackageName
            return App.getInstance().getFilesDir().getParent();
        }

    }

    /**
     * 把bitmap转换为文件
     *
     * @param bmp
     * @return
     */
    public static File saveImageToFile(Bitmap bmp, String mime) {
        // 首先保存图片
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
        String fileName = "bjhdltcdn";
        File appDir = new File(file, fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        String suffix = "jpeg";
        if (!StringUtils.isEmpty(mime)) {
            // 文件后缀名
            if (mime.endsWith("bmp") || mime.endsWith("webp")) {
                suffix = "jpeg";
            } else {
                suffix = mime.substring(mime.lastIndexOf("/") + 1, mime.length());
            }
        }

        fileName = "scale_" + System.currentTimeMillis() + "." + suffix;
        File currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

                if (bmp != null && bmp.isRecycled()) {
                    bmp.recycle();
                    bmp = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return currentFile;
    }


    /**
     * 清楚缓存
     * @param context
     */
    public static void clearWebViewCache(final Context context) {

        GreenDaoUtils.getInstance().getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                File file = context.getApplicationContext().getCacheDir().getAbsoluteFile();
                deleteFile(file);
            }
        });


    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {

        Logger.d("delete file path=" + file.getAbsolutePath());
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Logger.d("delete file no exists " + file.getAbsolutePath());
        }

    }
}
