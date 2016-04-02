package com.wusui.httppicturetest.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.wusui.httppicturetest.Utils.FileUtils;
import com.wusui.httppicturetest.Utils.HttpUtils;
import com.wusui.httppicturetest.Utils.Utility;

import java.io.InputStream;
import java.util.List;

/**
 * TODO 下载与保存妹子图的逻辑，给你整个抽离出来了，这些逻辑fragment也是不关心的，其实还可以更优，以后再讲嘛，一口气也吃不成胖子
 */
public class GirlModel {

    public interface GetGirlListener {
        void onLoadSuccess();
        void onLoadError(String e);
        void onLoadBitmap(Bitmap bitmap, int position);
    }

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_SUCCESS:
                    listener.onLoadSuccess();
                    break;
                case LOAD_ERROR:
                    listener.onLoadError((String) msg.obj);
                    break;
                case LOAD_BITMAP:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;
                    listener.onLoadBitmap(bitmap, position);
                    break;
            }
        }
    };

    public static final int LOAD_SUCCESS = 1;
    public static final int LOAD_ERROR = -1;
    public static final int LOAD_BITMAP = 2;

    public static final String FILE_NAME = "girl";
    public static final String FILE_NAME_END = ".jpg";
    private List<String> picUrls;
    private GetGirlListener listener;

    public GirlModel(GetGirlListener listener) {
        this.listener = listener;
    }

    public void getPicture(int page) {
        requestGankJson(page);
    }

    private void requestGankJson(int page) {
        String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/" + page;

        HttpUtils.sendHttpRequest(url, new HttpUtils.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                mMainHandler.sendEmptyMessage(LOAD_SUCCESS);
                requestGirlsPics(response);
            }

            @Override
            public void onError(Exception e) {
                Message message = new Message();
                message.what = LOAD_ERROR;
                message.obj = "JSON请求失败: " + e.toString();
                mMainHandler.sendMessage(message);
            }
        });
    }

    private void requestGirlsPics(String response) {
        picUrls = Utility.handlePictureResponse(response);
        for (int i = 0; i < picUrls.size(); i++) {
            Message message = new Message();
            message.obj = null;
            message.what = LOAD_BITMAP;
            message.arg1 = -1;
            mMainHandler.sendMessage(message);
            getGirlPic(i);
        }
    }

    private void requestGirlsPic(String girlPicUrl, final int position) {
        HttpUtils.sendHttpRequestForBitmap(girlPicUrl, new HttpUtils.HttpForBitmapListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
                Message message = new Message();
                message.obj = bitmap;
                message.what = LOAD_BITMAP;
                message.arg1 = position;
                mMainHandler.sendMessage(message);
            }

            @Override
            public void onError(String e) {
                Message message = new Message();
                message.what = LOAD_ERROR;
                message.obj = "第" + position + "张图片请求失败: " + e;
                mMainHandler.sendMessage(message);
            }

            @Override
            public void onFinish(InputStream inputStream) {
                saveGirlsPic(inputStream, position);
            }
        });
    }


    private void saveGirlsPic(InputStream inputStream, final int position) {
        FileUtils.saveFile(inputStream, FILE_NAME + "_" + position + FILE_NAME_END, new FileUtils.SaveFileListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFail() {
                Message message = new Message();
                message.what = LOAD_ERROR;
                message.obj = "第" + position + "张图片保存失败";
                mMainHandler.sendMessage(message);
            }
        });
    }

    private void getGirlPic(final int position) {
        FileUtils.getFile(FILE_NAME + "_" + position + FILE_NAME_END, new FileUtils.GetImageListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Message message = new Message();
                message.obj = null;
                message.what = LOAD_BITMAP;
                message.arg1 = -1;
                mMainHandler.sendMessage(message);
            }

            @Override
            public void onFail() {
                Message message = new Message();
                message.what = LOAD_ERROR;
                message.obj = "第" + position + "张图片获取失败，开始下载";
                mMainHandler.sendMessage(message);
                requestGirlsPic(picUrls.get(position), position);
            }
        });
    }
}
