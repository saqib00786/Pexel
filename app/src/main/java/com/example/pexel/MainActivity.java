package com.example.pexel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pexel.Adapter.WallpaperAdapter;
import com.example.pexel.Model.WallpaperModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarException;

public class MainActivity extends AppCompatActivity implements onClickInterface {

    RecyclerView mRecyclerView;
    WallpaperAdapter mWallpaperAdapter;
    List<WallpaperModel> mWallpaperModelList;

    ImageViewPexel imageViewPexel;
    int mPageNumber=1;

    Boolean isScrolling = false;
    int mCurrentItem,mTotalItem,mScrollOutItem;

    String mUrl = "https://api.pexels.com/v1/curated/?page="+mPageNumber+"&per_page=80";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#2196F3"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        mRecyclerView = findViewById(R.id.recyclerViewID);
        mWallpaperModelList = new ArrayList<>();
        mWallpaperAdapter = new WallpaperAdapter(this,mWallpaperModelList,  this);

        mRecyclerView.setAdapter(mWallpaperAdapter);

        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCurrentItem = mGridLayoutManager.getChildCount();
                mTotalItem = mGridLayoutManager.getItemCount();
                mScrollOutItem = mGridLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (mCurrentItem+mScrollOutItem==mTotalItem)){
                    isScrolling=false;
                    getWallpaperPexel();
                }
            }
        });
        getWallpaperPexel();
    }

    public void getWallpaperPexel() {
        StringRequest mRequest = new StringRequest(Request.Method.GET, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            JSONArray mJsonArray = mJsonObject.getJSONArray("photos");

                            int mLength = mJsonArray.length();

                            for(int i=0; i<mLength; i++){
                                JSONObject mObject = mJsonArray.getJSONObject(i);

                                int id = mObject.getInt("id");

                                JSONObject mImageObj = mObject.getJSONObject("src");
                                String mediumUrl = mImageObj.getString("original");
                                String largeUrl = mImageObj.getString("medium");

                                WallpaperModel mWallpapeModel = new WallpaperModel(id,mediumUrl,largeUrl);
                                mWallpaperModelList.add(mWallpapeModel);

                            }
                            mWallpaperAdapter.notifyDataSetChanged();
                            mPageNumber++;
                        }catch (JSONException e){

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: Not Working Properly");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> mParam = new HashMap<>();
                mParam.put("Authorization","563492ad6f91700001000001748aa91ee93e42caa89bf47d009ca247");

                return mParam;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(mRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.searcBarID){
            final AlertDialog.Builder mAlerdialogBuilder = new AlertDialog.Builder(this);
            final EditText mText = new EditText(this);
            mText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            mAlerdialogBuilder.setMessage("Enter Category e.g nature");
            mAlerdialogBuilder.setTitle("Pexel Search Wallpaper");

            mAlerdialogBuilder.setView(mText);
            mAlerdialogBuilder.setPositiveButtonIcon(getDrawable(R.drawable.ok));
            mAlerdialogBuilder.setNegativeButtonIcon(getDrawable(R.drawable.dismiss));
            mAlerdialogBuilder.setPositiveButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String query = mText.getText().toString().toLowerCase();
                    mUrl = "https://api.pexels.com/v1/search/?page="+mPageNumber+"&per_page=80&query="+query;
                    mWallpaperModelList.clear();
                    getWallpaperPexel();
                }
            }).setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(WallpaperModel wallpaperModel) {
        imageViewPexel = new ImageViewPexel();
        String mLargId= wallpaperModel.getmLargeImageUrl().toString();
        Bundle bundle = new Bundle();
        bundle.putString("KEY_VAL",mLargId);
        imageViewPexel.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,imageViewPexel)
                .addToBackStack("BackStack").commit();

    }
}