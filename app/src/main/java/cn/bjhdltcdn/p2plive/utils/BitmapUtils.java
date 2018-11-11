package cn.bjhdltcdn.p2plive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.View;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import cn.bjhdltcdn.p2plive.app.App;


/**
 * Created by wenquan on 2015/10/22.
 */
public class BitmapUtils {

    public static File doParseToFile(String path) {

        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;

        int mMaxWidth = 1080;
        int mMaxHeight = 1920;
        Bitmap bitmap = null;

        // If we have to resize this image, first get the natural bounds.
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, decodeOptions);
        int actualWidth = decodeOptions.outWidth;
        int actualHeight = decodeOptions.outHeight;
        String mime = decodeOptions.outMimeType;
        Logger.v("mime ====== " + mime);

        Logger.v("actualWidth * actualHeight =bitmap图片实际宽高= " + actualWidth + " * " + actualHeight);

        if (actualHeight == -1 || actualWidth == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(path);
                actualWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                actualHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度

                Logger.v("actualWidth * actualHeight =ExifInterface图片实际宽高= " + actualWidth + " * " + actualHeight);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            double scale = 1;//scale=1表示不缩放
            if (actualWidth > actualHeight && actualWidth > mMaxWidth) {//如果宽度大的话根据宽度固定大小缩放
                scale = Double.valueOf(actualWidth) / Double.valueOf(mMaxWidth);
            } else if (actualWidth < actualHeight && actualHeight > mMaxHeight) {//如果高度高的话根据宽度固定大小缩放
                scale = Double.valueOf(actualHeight) / Double.valueOf(mMaxHeight);
            }

            Logger.d("scale ==缩放比== " + scale);

            if (scale < 1) {
                scale = 1;
            }

            int desiredWidth = (int) (actualWidth / scale);
            int desiredHeight = (int) (actualHeight / scale);

            Logger.d("width =scale=最终= " + desiredWidth + " ; height =scale=最终= " + desiredHeight);

            // Decode to the nearest power of two scaling factor.
            decodeOptions.inJustDecodeBounds = false;

            decodeOptions.outWidth = desiredWidth;
            decodeOptions.outHeight = desiredHeight;

            decodeOptions.inSampleSize = (int) scale;
            Bitmap tempBitmap = BitmapFactory.decodeFile(path, decodeOptions);

            if (tempBitmap != null) {
                Logger.v("tempBitmap.getWidth() =第一次生成图片== " + tempBitmap.getWidth() + " ;tempBitmap.getHeight()  =第一次生成图片== " + tempBitmap.getHeight());
            }

            // If necessary, scale down to the maximal acceptable size.
            if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                Logger.v("缩放操作==========");
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }

            if (tempBitmap != null) {
                Logger.v("tempBitmap.getWidth() =最终生成图片== " + tempBitmap.getWidth() + " ;tempBitmap.getHeight()  =最终生成图片== " + tempBitmap.getHeight());
            }

            int degree = getBitmapDegree(path);
            Logger.v("degree ===角度=== " + degree);
            if (degree != 0 && bitmap != null) {
                bitmap = rotateBitmapByDegree(bitmap, degree);
            }

            if (bitmap == null) {
                Logger.v("图片压缩失败....");
                return null;
            } else {
                return FileUtils.saveImageToFile(bitmap, mime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static byte[] doParse(String path) {

        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;

        int mMaxWidth = 1080;
        int mMaxHeight = 1920;
        Bitmap bitmap = null;

        // If we have to resize this image, first get the natural bounds.
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, decodeOptions);
        int actualWidth = decodeOptions.outWidth;
        int actualHeight = decodeOptions.outHeight;
        String mime = decodeOptions.outMimeType;
        Logger.v("mime ====== " + mime);

        Logger.v("actualWidth * actualHeight =bitmap图片实际宽高= " + actualWidth + " * " + actualHeight);

        if (actualHeight == -1 || actualWidth == -1) {
            try {
                ExifInterface exifInterface = new ExifInterface(path);
                actualWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                actualHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度

                Logger.v("actualWidth * actualHeight =ExifInterface图片实际宽高= " + actualWidth + " * " + actualHeight);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            double scale = 1;//scale=1表示不缩放
            if (actualWidth > actualHeight && actualWidth > mMaxWidth) {//如果宽度大的话根据宽度固定大小缩放
                scale = Double.valueOf(actualWidth) / Double.valueOf(mMaxWidth);
            } else if (actualWidth < actualHeight && actualHeight > mMaxHeight) {//如果高度高的话根据宽度固定大小缩放
                scale = Double.valueOf(actualHeight) / Double.valueOf(mMaxHeight);
            }

            Logger.d("scale ==缩放比== " + scale);

            if (scale < 1) {
                scale = 1;
            }

            int desiredWidth = (int) (actualWidth / scale);
            int desiredHeight = (int) (actualHeight / scale);

            Logger.d("width =scale=最终= " + desiredWidth + " ; height =scale=最终= " + desiredHeight);

            // Decode to the nearest power of two scaling factor.
            decodeOptions.inJustDecodeBounds = false;

            decodeOptions.outWidth = desiredWidth;
            decodeOptions.outHeight = desiredHeight;
            decodeOptions.inSampleSize = (int) scale;
            Bitmap tempBitmap = BitmapFactory.decodeFile(path, decodeOptions);

            if (tempBitmap != null) {
                Logger.v("tempBitmap.getWidth() =第一次生成图片== " + tempBitmap.getWidth() + " ;tempBitmap.getHeight()  =第一次生成图片== " + tempBitmap.getHeight());
            }

            // If necessary, scale down to the maximal acceptable size.
            if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                Logger.v("缩放操作==========");
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }

            if (tempBitmap != null) {
                Logger.v("tempBitmap.getWidth() =最终生成图片== " + tempBitmap.getWidth() + " ;tempBitmap.getHeight()  =最终生成图片== " + tempBitmap.getHeight());
            }

            int degree = getBitmapDegree(path);
            Logger.v("degree ===角度=== " + degree);
            if (degree != 0 && bitmap != null) {
                bitmap = rotateBitmapByDegree(bitmap, degree);
            }

            if (bitmap == null) {
                Logger.v("图片压缩失败....");
                return null;
            } else {
                return bitmap2Bytes(bitmap, mime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * The real guts of parseNetworkResponse. Broken out for readability.
     */
    public static byte[] doParse(String path, int mMaxWidth, int mMaxHeight, ImageView.ScaleType mScaleType) {
        try {

            BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
            decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap bitmap = null;
            if (mMaxWidth == 0 && mMaxHeight == 0) {
                // 720 * 1280 高清；1080 * 1920 超清
                mMaxWidth = PlatformInfoUtils.getWidthOrHeight(App.getInstance())[0];
                mMaxHeight = PlatformInfoUtils.getWidthOrHeight(App.getInstance())[1];
            }

            {
                // If we have to resize this image, first get the natural bounds.
                decodeOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, decodeOptions);
                int actualWidth = decodeOptions.outWidth;
                int actualHeight = decodeOptions.outHeight;
                String mime = decodeOptions.outMimeType;
                Logger.v("mime ====== " + mime);

                Logger.v("actualWidth * actualHeight =bitmap图片实际宽高= " + actualWidth + " * " + actualHeight);

                if (actualHeight == -1 || actualWidth == -1) {
                    try {
                        ExifInterface exifInterface = new ExifInterface(path);
                        actualWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                        actualHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度

                        Logger.v("actualWidth * actualHeight =ExifInterface图片实际宽高= " + actualWidth + " * " + actualHeight);

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                // Then compute the dimensions we would ideally like to decode to.
                int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight, actualWidth, actualHeight, mScaleType);
                int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth, actualHeight, actualWidth, mScaleType);
                Logger.v("desiredWidth === " + desiredWidth + " ;desiredHeight === " + desiredHeight);

                // Decode to the nearest power of two scaling factor.
                decodeOptions.inJustDecodeBounds = false;

                decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
                Bitmap tempBitmap = BitmapFactory.decodeFile(path, decodeOptions);
                // If necessary, scale down to the maximal acceptable size.
                if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                    Logger.v("tempBitmap.getWidth() === " + tempBitmap.getWidth() + " ;tempBitmap.getHeight()  === " + tempBitmap.getHeight());
                    if (tempBitmap.getWidth() > mMaxWidth || tempBitmap.getHeight() > mMaxHeight) {
                        desiredWidth = (int) (tempBitmap.getWidth() * 0.75);
                        desiredHeight = (int) (tempBitmap.getHeight() * 0.75);
                    }

                    Logger.v("desiredWidth ==0.75== " + desiredWidth + " ;desiredHeight ==0.75== " + desiredHeight);

                    bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                    tempBitmap.recycle();
                } else {
                    bitmap = tempBitmap;
                }

                int degree = getBitmapDegree(path);
                Logger.v("degree ===角度=== " + degree);
                if (degree != 0 && bitmap != null) {
                    bitmap = rotateBitmapByDegree(bitmap, degree);
                }

                if (bitmap == null) {
                    Logger.v("图片压缩失败....");
                    return null;
                } else {
                    return bitmap2Bytes(bitmap, mime);
                }
            }
        } catch (Error e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Scales one side of a rectangle to fit aspect ratio.
     *
     * @param maxPrimary      Maximum size of the primary dimension (i.e. width for
     *                        max width), or zero to maintain aspect ratio with secondary
     *                        dimension
     * @param maxSecondary    Maximum size of the secondary dimension, or zero to
     *                        maintain aspect ratio with primary dimension
     * @param actualPrimary   Actual size of the primary dimension
     * @param actualSecondary Actual size of the secondary dimension
     * @param scaleType       The ScaleType used to calculate the needed image size.
     */
    public static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary, ImageView.ScaleType scaleType) {

        // If no dominant value at all, just return the actual.
        if ((maxPrimary == 0) && (maxSecondary == 0)) {
            return actualPrimary;
        }

        // If ScaleType.FIT_XY fill the whole rectangle, ignore ratio.
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            if (maxPrimary == 0) {
                return actualPrimary;
            }
            return maxPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;

        // If ScaleType.CENTER_CROP fill the whole rectangle, preserve aspect ratio.
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            if ((resized * ratio) < maxSecondary) {
                resized = (int) (maxSecondary / ratio);
            }
            return resized;
        }

        if ((resized * ratio) > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    public static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    public static byte[] bitmap2Bytes(Bitmap bm, String mime) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 95;
        if (!StringUtils.isEmpty(mime) && mime.toLowerCase().contains("png".toLowerCase())) {
            format = Bitmap.CompressFormat.PNG;
            quality = 85;
        }
        bm.compress(format, quality, baos);
        if (bm != null) {
            bm.recycle();
            bm = null;
        }
        return baos.toByteArray();
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 获取图片类型
     *
     * @param path
     * @return
     */
    public static String getMIMe(String path) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, decodeOptions);
        String mime = decodeOptions.outMimeType;
        Logger.v("mime ====== " + mime);
        return mime;
    }

    /**
     * 对View进行量测，布局后截图
     *
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static int getBitmapWidth(Context context) {
        return 500/*getScreenWidth(context) / 3 + getScreenWidth(context) / 4*/;
    }

    public static int getBitmapHeight(Context context) {
        return 500/*getScreenHeight(context) / 4 + getScreenHeight(context) / 4*/;
    }


}
