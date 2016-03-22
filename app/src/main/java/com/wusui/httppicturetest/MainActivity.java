package com.wusui.httppicturetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView image;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private static List<Bitmap>mbitmaps = new ArrayList<>();
    private PictureAdapter mAdapter;
    
    private static final int LOAD_SUCCESS = 1;
    private static final int LOAD_ERROR = -1;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case LOAD_SUCCESS:
                    File file = new File(Environment.getExternalStorageDirectory(),"girl.jpg");
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file.getPath());
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        mbitmaps.add(bitmap);
                        mAdapter.notifyDataSetChanged();
                        //image.setImageBitmap((Bitmap)msg.obj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case LOAD_ERROR:
                    Toast.makeText(mContext,"加载失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        //image = (ImageView)findViewById(R.id.image);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView)findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new PictureAdapter(MainActivity.this,mbitmaps));
        mAdapter.notifyDataSetChanged();
        //image = (ImageView)findViewById(R.id.image);
    }

    public void show(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPicture();
            }
        }).start();
    }

    private void getPicture() {
        URL url = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            url = new URL("http://ww2.sinaimg.cn/large/7a8aed7bjw1f25gtggxqjj20f00b9tb5.jpg");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            Log.d("MainActivity",conn.getResponseCode()+"");
            if (conn.getResponseCode() == 200){
                is = conn.getInputStream();

                File file = new File(Environment.getExternalStorageDirectory(),"girl.jpg");
                fos = new FileOutputStream(file);
                Log.d("MainActivity","感谢上帝，这里没有bug！！！！");
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }
                fos.flush();
                handler.sendEmptyMessage(LOAD_SUCCESS);
            }
        }catch (Exception e){
            handler.sendEmptyMessage(LOAD_ERROR);
            e.printStackTrace();
        }finally {
            try{
                if (is != null){
                    is.close();
                }
                if (fos != null){
                    fos.close();
                }
            }catch (Exception e){
                handler.sendEmptyMessage(LOAD_ERROR);
                e.printStackTrace();
            }
        }
    }
}
