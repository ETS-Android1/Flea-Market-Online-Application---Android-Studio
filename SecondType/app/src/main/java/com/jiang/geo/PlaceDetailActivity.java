package com.jiang.geo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

public class PlaceDetailActivity extends Activity implements View.OnClickListener {

    private Banner mBanner;
    private TextView name;
    private TextView introduce;
    private TextView address;
    private TextView score;
    private TextView phone;
    private ImageView back;
    private ImageView share;
    private ImageView scoreImg;
    private RatingBar mRatingBar;

    public static PlaceData mPlaceData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        if (mPlaceData == null) finish();
        mRatingBar = (RatingBar) findViewById(R.id.rating);
        mBanner = (Banner) findViewById(R.id.banner);
        name = (TextView) findViewById(R.id.name);
        introduce = (TextView) findViewById(R.id.introduce);
        address = (TextView) findViewById(R.id.address);
        score = (TextView) findViewById(R.id.score);
        phone = (TextView) findViewById(R.id.phone);
        back = (ImageView) findViewById(R.id.back);
        scoreImg = (ImageView) findViewById(R.id.pf);
        share = (ImageView) findViewById(R.id.share);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mPlaceData.getShareString());
                startActivity(Intent.createChooser(intent, "Share Place"));
            }
        });
        name.setText(mPlaceData.getName());
        introduce.setText(mPlaceData.getIntroduce());
        address.setText(mPlaceData.getAddress());
        score.setText(SPUtils.getFloat(mPlaceData.getName(), (float) mPlaceData.getScore()) + "");
        phone.setText(mPlaceData.getPhone());
        mRatingBar.setRating((float) mPlaceData.getScore());
        phone.setOnClickListener(this);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path.toString()).into(imageView);
            }
        });
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        if (mPlaceData.getImgs() != null) {
            mBanner.update(mPlaceData.getImgs());
        }
        scoreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final float score = SPUtils.getFloat(mPlaceData.getName(), (float) mPlaceData.getScore());
                View root = getLayoutInflater().inflate(R.layout.dialog_score, null);
                final RatingBar ratingBar = (RatingBar) root.findViewById(R.id.rating_bar);
                new AlertDialog.Builder(PlaceDetailActivity.this)
                        .setTitle("Score")
                        .setView(root)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
                                SPUtils.putFloat(mPlaceData.getName(), Float.parseFloat(df
                                        .format((score + ratingBar.getRating()) / 2.f)));
                                PlaceDetailActivity.this.score.setText
                                        (SPUtils.getFloat(mPlaceData.getName(), (float) mPlaceData.getScore()) + "");
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        String call = phone.getText().toString();
        if (!TextUtils.isEmpty(call)) {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + call));
            startActivity(dialIntent);
        }
    }
}
