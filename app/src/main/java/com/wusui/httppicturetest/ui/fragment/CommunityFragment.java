package com.wusui.httppicturetest.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wusui.httppicturetest.R;
import com.wusui.httppicturetest.Utils.FileUtils;
import com.wusui.httppicturetest.Utils.HttpUtils;
import com.wusui.httppicturetest.Utils.Utility;
import com.wusui.httppicturetest.callback.OnRcvScrollListener;
import com.wusui.httppicturetest.ui.activity.DetailActivity;
import com.wusui.httppicturetest.ui.adapter.PictureAdapter;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by fg on 2016/3/27.
 */
public class CommunityFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private static PictureAdapter mAdapter;
    private static List<Bitmap> mBitmaps = new ArrayList<>();

    public static final int LOAD_SUCCESS = 1;
    public static final int LOAD_ERROR = -1;
    public static final int LOAD_BITMAP = 2;

    public static final String FILE_NAME = "girl";
    public static final String FILE_NAME_END = ".jpg";
    private List<String> picUrls;
    public static int page = 0;


    // TODO 你的handler应该对Fragment弱引用呀，你对Activity干啥呐,下面是正确示范，但是我已经把它抽离了，你不用管了



    private static class MyHandler extends android.os.Handler {
        private final WeakReference<CommunityFragment> mFragment;

        public MyHandler(CommunityFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mFragment.get() != null) {
                switch (msg.what){
                    case LOAD_SUCCESS:
//                        Toast.makeText(getContext(), "网络加载成功", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                        break;
                    case LOAD_ERROR:
                        //Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                        break;
                    case LOAD_BITMAP:
                        int position = msg.arg1;
                        if (position == -1) {
                            mBitmaps.add(null);
                            return;
                        }
                        Bitmap bitmap = (Bitmap) msg.obj;
                        mBitmaps.set(position, bitmap);
                        mAdapter.notifyDataSetChanged();
                        break;
            }
        }

    }}

    private final MyHandler mHandler = new MyHandler(this);

    public void getPicture(int page) {
        requestGankJson(page);
    }



    // TODO 在onCreate中做一些和视图数据无关的操作

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        super.onCreateView(inflater, container, savedInstance);
        View communityView = inflater.inflate(R.layout.fragment_community, container,false);
        mRecyclerView = (RecyclerView)communityView.findViewById(R.id.id_recyclerview);
        initView();
        getPicture(0); // TODO 在第一次没滑到底的时候就应该先下载第0页的内容
        return communityView;
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter = new PictureAdapter(getActivity(), mBitmaps));
        mRecyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                // TODO 弹Toast的逻辑也应该放在外面
                Toast.makeText(getContext(), "滑动到底了", Toast.LENGTH_SHORT).show();
                getPicture(++page);
            }
        });
        mAdapter.setmOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("bitmap",mBitmaps.get(position));
                startActivity(intent);
            }


        });
    }

    private void requestGankJson(int page) {
        String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/" + page;

        HttpUtils.sendHttpRequest(url, new HttpUtils.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                mHandler.sendEmptyMessage(LOAD_SUCCESS);
                requestGirlsPics(response);
            }

            @Override
            public void onError(Exception e) {
                Message message = Message.obtain();
                message.what = LOAD_ERROR;
                message.obj = "JSON请求失败: " + e.toString();
                mHandler.sendMessage(message);
            }
        });
    }

    private void requestGirlsPics(String response) {
        picUrls = Utility.handlePictureResponse(response);
        for (int i = 0; i < picUrls.size(); i++) {
            Message message = Message.obtain();
            message.obj = null;
            message.what = LOAD_BITMAP;
            message.arg1 = -1;
            mHandler.sendMessage(message);
            getGirlPic(i);
        }
    }

    private void requestGirlsPic(String girlPicUrl, final int position) {
        HttpUtils.sendHttpRequestForBitmap(girlPicUrl, new HttpUtils.HttpForBitmapListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
                Message message = Message.obtain();
                message.obj = bitmap;
                message.what = LOAD_BITMAP;
                message.arg1 = position;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(String e) {
                Message message = Message.obtain();
                message.what = LOAD_ERROR;
                message.obj = "第" + position + "张图片请求失败: " + e;
                mHandler.sendMessage(message);
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
            public void onSuccess() {
            }

            @Override
            public void onFail() {
                Message message = Message.obtain();
                message.what = LOAD_ERROR;
                message.obj = "第" + position + "张图片保存失败";
                mHandler.sendMessage(message);
            }
        });
    }

    private void getGirlPic(final int position) {
        FileUtils.getFile(FILE_NAME + "_" + position + FILE_NAME_END, new FileUtils.GetImageListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Message message = Message.obtain();
                message.obj = null;
                message.what = LOAD_BITMAP;
                message.arg1 = -1;
                mHandler.sendMessage(message);
            }

            @Override
            public void onFail() {
                Message message = Message.obtain();
                message.what = LOAD_ERROR;
                message.obj = "第" + position + "张图片获取失败，开始下载";
                mHandler.sendMessage(message);
                requestGirlsPic(picUrls.get(position), position);
            }
        });
    }

}


