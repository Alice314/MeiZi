package com.wusui.httppicturetest.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wusui.httppicturetest.R;
import com.wusui.httppicturetest.callback.OnRcvScrollListener;
import com.wusui.httppicturetest.model.GirlModel;
import com.wusui.httppicturetest.ui.adapter.PictureAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fg on 2016/3/27.
 */
public class CommunityFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private PictureAdapter mAdapter;
    private List<Bitmap> mBitmaps = new ArrayList<>();
    private GirlModel mGirlModel;

    public static int page = 0;

    // TODO 你的handler应该对Fragment弱引用呀，你对Activity干啥呐,下面是正确示范，但是我已经把它抽离了，你不用管了
    /*
    private final Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<CommunityFragment> mFragment;

        public MyHandler(CommunityFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mFragment.get() == null) {
                return;
            }
            mFragment.get().doInUIThread(msg);
        }
    }

    private void doInUIThread(Message msg) {
        switch (msg.what){
            case LOAD_SUCCESS:
                Toast.makeText(getContext(), "网络加载成功", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                break;
            case LOAD_ERROR:
                //Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                break;
            case LOAD_BITMAP:
                Bitmap bitmap = (Bitmap) msg.obj;
                int position = msg.arg1;
                mBitmaps.set(position, bitmap);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }*/

    // TODO 在onCreate中做一些和视图数据无关的操作
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGirlModel = new GirlModel(new GirlModel.GetGirlListener() {
            @Override
            public void onLoadSuccess() {
                Toast.makeText(getContext(), "网络加载成功", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadError(String e) {
                //Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadBitmap(Bitmap bitmap, int position) {
                if (position == -1) {
                    mBitmaps.add(null);
                    return;
                }
                mBitmaps.set(position, bitmap);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        super.onCreateView(inflater, container, savedInstance);
        View communityView = inflater.inflate(R.layout.fragment_community, container,false);
        mRecyclerView = (RecyclerView)communityView.findViewById(R.id.id_recyclerview);
        initView();
        mGirlModel.getPicture(0); // TODO 在第一次没滑到底的时候就应该先下载第0页的内容
        return communityView;
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter = new PictureAdapter(getActivity(), mBitmaps));
        mRecyclerView.addOnScrollListener(new OnRcvScrollListener(){
            @Override
            public void onBottom() {
                // TODO 弹Toast的逻辑也应该放在外面
                Toast.makeText(getContext(), "滑动到底了", Toast.LENGTH_SHORT).show();
                mGirlModel.getPicture(++page);
            }
        });
    }
}
