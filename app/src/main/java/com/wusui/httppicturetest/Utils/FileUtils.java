package com.wusui.httppicturetest.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fg on 2016/3/25.
 */
public class FileUtils {

    public interface SaveFileListener {
        void onSuccess();
        void onFail();
    }

    public interface GetImageListener {
        void onSuccess(Bitmap bitmap);
        void onFail();
    }

    /**
     * 通过文件名，从储存卡的根目录获得文件
     * @param fileName 文件名
     * @param listener 回调
     */
    public static void getFile(final String fileName, final GetImageListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                InputStream fis;
                try {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                   /* options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getPath(), options);
                    int reqWidth = options.outWidth;
                    int reqHeight = options.outHeight;
                    options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);*/
                    fis = new FileInputStream(file.getPath());
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    if (listener != null) {
                        if (bitmap != null)
                            listener.onSuccess(bitmap);
                        else listener.onFail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) listener.onFail();
                }
            }
        }).start();
    }

    /**
     * 通过文件名把String类型写入文件（文件在储存卡的根目录下）
     * @param string 待写入的字符串
     * @param fileName 待写入的文件名
     * @param listener 回调
     */
    public static void saveFile(String string, String fileName, SaveFileListener listener) {
        saveFile(string, Environment.getExternalStorageDirectory(), fileName, listener);
    }
    /**
     *
     * @param string
     * @param url
     * @param fileName
     * @param listener
     */
    public static void saveFile(String string, File url, String fileName, SaveFileListener listener) {

        String str = "";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());

        saveFile(inputStream, url, fileName, listener);
    }

    public static void saveFile(InputStream inputStream, String fileName, SaveFileListener listener) {
        saveFile(inputStream, Environment.getExternalStorageDirectory(), fileName, listener);
    }

    public static void saveFile(final InputStream inputStream, final File url, final String fileName, final SaveFileListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                File file = new File(url, fileName);
                try {
                    fos = new FileOutputStream(file);

                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    if (listener != null) listener.onSuccess();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) listener.onFail();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        if (listener != null) listener.onFail();
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
