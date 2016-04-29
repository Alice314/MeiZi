package com.wusui.httppicturetest.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wusui.httppicturetest.R;

import java.util.List;

/**
 * Created by fg on 2016/3/21.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder> {

    private Context mContext;
    // TODO 驼峰命名法啊！mbitmaps是什么鬼
    private List<Bitmap> mBitmaps;

    public PictureAdapter(Context context, List<Bitmap> mBitmaps) {
        mContext = context;
        this.mBitmaps = mBitmaps;
    }


    @Override
    public PictureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(final PictureAdapter.MyViewHolder holder, int position) {
        if (mBitmaps.get(position) != null) {
            holder.imageView.setImageBitmap(mBitmaps.get(position));
        } else {

            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        if (mOnItemClickListener != null){
           holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.imageView,pos);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mBitmaps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);

    }
    private OnItemClickListener mOnItemClickListener;
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
