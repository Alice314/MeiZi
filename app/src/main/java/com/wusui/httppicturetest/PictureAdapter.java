package com.wusui.httppicturetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by fg on 2016/3/21.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder> {

    private Context mContext;
    private List<Bitmap>mbitmaps;

    public PictureAdapter(Context context,List<Bitmap>mbitmaps){
        mContext = context;
        this.mbitmaps = mbitmaps;
    }
    @Override
    public PictureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_picture,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final PictureAdapter.MyViewHolder holder, int position) {
        holder.imageView.setImageBitmap(mbitmaps.get(position));
//        if (mOnItemClickListener != null){
//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickListener.onItemClick(holder.imageView,pos);
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return mbitmaps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image);

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
