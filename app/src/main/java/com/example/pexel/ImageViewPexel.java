package com.example.pexel;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.BundleCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.pexel.Model.WallpaperModel;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.IOException;

public class ImageViewPexel extends Fragment {
    private PhotoView photoView;
    private LottieAnimationView progressBar;
    private LottieAnimationView errorBar;
    private TextView errorMsg;

    private FloatingActionButton mFabadd,mFabDownload,mFabsetWallpaper;
    private Animation mOpen,mClose,mClockwise,mAniClockwise;

    boolean isOpen = false;

    public ImageViewPexel() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.image_view_fragment,container,false);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();

        Bundle bundle = this.getArguments();
        final String myVal = bundle.getString("KEY_VAL");

        photoView = view.findViewById(R.id.myPhotoView);
        progressBar = view.findViewById(R.id.progressBar);
        errorBar = view.findViewById(R.id.errorBar);
        errorMsg = view.findViewById(R.id.error_msg);

        //todo: id floating action button and animation
        mFabadd = view.findViewById(R.id.addFabID);
        mFabDownload = view.findViewById(R.id.downloadFabID);
        mFabsetWallpaper = view.findViewById(R.id.setWallpaperFabID);

        mOpen = AnimationUtils.loadAnimation(getContext(),R.anim.fab_open);
        mClose = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        mClockwise = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_clockwise);
        mAniClockwise = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_anticlockwise);


        Glide.with(this).load(myVal)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        errorBar.setVisibility(View.VISIBLE);
                        errorMsg.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        funShowBtn(view);
                        return false;
                    }
                }).into(photoView);



        mFabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager =(DownloadManager)getContext().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(myVal);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                downloadManager.enqueue(request);


                Toast.makeText(getContext(), "Start Downloading....", Toast.LENGTH_SHORT).show();
            }
        });
        mFabsetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
                try {
                    wallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), "Setting Wallpaper....", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
    public void funShowBtn(final View view){
        mFabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    mFabadd.startAnimation(mClockwise);
                    mFabDownload.startAnimation(mClose);
                    mFabsetWallpaper.startAnimation(mClose);

                    mFabDownload.setClickable(false);
                    mFabsetWallpaper.setClickable(false);


                    isOpen = false;
                }
                else {
                    mFabadd.startAnimation(mAniClockwise);
                    mFabDownload.startAnimation(mOpen);
                    mFabsetWallpaper.startAnimation(mOpen);

                    mFabDownload.setClickable(true);
                    mFabsetWallpaper.setClickable(true);

                    isOpen = true;
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
    }
}
