package com.example.pexel.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pexel.ImageViewPexel;
import com.example.pexel.Model.WallpaperModel;
import com.example.pexel.R;
import com.example.pexel.onClickInterface;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {

    private Context mContext;
    private List<WallpaperModel> wallpaperModelList;
    private onClickInterface onClickInterface;

    public WallpaperAdapter(Context mContext,List<WallpaperModel> wallpaperModelList,onClickInterface onClickInterface) {
        this.mContext  = mContext;
        this.wallpaperModelList = wallpaperModelList;
        this.onClickInterface = onClickInterface;
    }
    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_view,parent,false);
        return new WallpaperViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, final int position) {
        Glide.with(mContext).load(wallpaperModelList.get(position)
        .getmMediumImageUrl()).into(holder.mImageView);

     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity mAppCompatActivity = (AppCompatActivity) v.getContext();
                *//* ImageViewPexel mImageViewPexel = new ImageViewPexel();
                mAppCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recyclerViewID, ImageViewPexel).addToBackStack(null).commit();*//*
               *//*Bundle bundle = new Bundle();
               bundle.putString("ImageLink",wallpaperModelList.get(position).getmLargeImageUrl());
              ImageViewPexel imageViewPexel = new ImageViewPexel();
              imageViewPexel.setArguments(bundle);
              mAppCompatActivity.getSupportFragmentManager().beginTransaction()
                      .replace(R.id.fragmentContainer,ImageViewPexel).commit();*/


    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public WallpaperViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageViewItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    onClickInterface.onItemClick(wallpaperModelList.get(position));
                }
            });
        }
    }
}

