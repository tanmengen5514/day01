package com.tanmengen.en.tanmengenqi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.tanmengen.en.tanmengenqi.R;
import com.tanmengen.en.tanmengenqi.beans.FuliBean;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 * Created by en on 2019/9/3.
 */

public class MyAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<String> banners;
    private ArrayList<FuliBean.DataBean.DatasBean> list;

    public MyAdapter(Context context, ArrayList<String> banners, ArrayList<FuliBean.DataBean.DatasBean> list) {
        this.context = context;
        this.banners = banners;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_my_banner, parent, false);
            return new MyBanner(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        int i = getItemViewType(position);
        if (i == 1) {
            MyBanner holder1 = (MyBanner) holder;
            holder1.banner_item.setImages(banners).setImageLoader(new MyImage()).start();
        } else {
            int index = 0;
            if (banners.size()>0) {
                index = position - 1;
            }
            MyViewHolder holder2 = (MyViewHolder) holder;
            if (position-1 % 2 == 0) {
                holder2.tv_item.setText(list.get(position-1).getTitle());
                Glide.with(context).load(list.get(position-1).getEnvelopePic())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(holder2.iv_item);

            } else{
                holder2.tv_item.setText(list.get(position-1).getAuthor());
                Glide.with(context).load(list.get(position-1).getEnvelopePic())
                        .into(holder2.iv_item);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (myOnClickLinstener!=null){
                  myOnClickLinstener.onClick(position);
              }
            }
        });
    }
    public interface MyOnClickLinstener{
        void onClick(int position);
    }
    private MyOnClickLinstener myOnClickLinstener;

    public void setMyOnClickLinstener(MyOnClickLinstener myOnClickLinstener) {
        this.myOnClickLinstener = myOnClickLinstener;
    }

    class MyImage extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (banners.size() > 0) {
            return list.size() + 1;
        } else {
            return list.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && banners.size() > 0) {
            return 1;
        } else {
            return 2;
        }
    }

    class MyBanner extends RecyclerView.ViewHolder {
        Banner banner_item;

        public MyBanner(View itemView) {
            super(itemView);
            banner_item = itemView.findViewById(R.id.banner_item);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item;
        ImageView iv_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item = itemView.findViewById(R.id.tv_item);
            iv_item = itemView.findViewById(R.id.iv_item);
        }
    }
}
