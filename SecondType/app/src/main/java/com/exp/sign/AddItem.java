package com.exp.sign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.jiang.geo.DistanceMapsActivity;
import com.jiang.geo.R;
import com.jiang.geo.util.AndroidUIUtil;
import com.jiang.geo.util.LocationUtils;
import com.sqlite.greendao.BmobUserDao;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.util.List;

public class AddItem extends AppCompatActivity {

    private EditText etMc;
    private EditText etZz;
    private EditText etSj;
    private EditText etDd;
    private EditText etFl;
    private ImageView img;
    private Wenxue mWenxue;
    private String file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        etMc = (EditText) findViewById(R.id.et_mc);
        etZz = (EditText) findViewById(R.id.et_zz);
        etSj = (EditText) findViewById(R.id.et_sj);
        etDd = (EditText) findViewById(R.id.et_dd);
        etFl = (EditText) findViewById(R.id.et_fl);
        etFl.setFocusable(View.FOCUSABLE_AUTO);
        etFl.setFocusableInTouchMode(false);
        etFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"Clothing", "Cosmetics", "Maternal and infant products", "Electronic equipment", "Home Depot", "Virtual goods", "Other"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(AddItem.this);
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etFl.setText(items[which]);
                    }
                });
                listDialog.show();
            }
        });
        mWenxue = getIntent().getParcelableExtra("data");
        img = (ImageView) findViewById(R.id.img);

        if(LocationUtils.sLastLocation != null) {
            etDd.setText("" + LocationUtils.sLastLocation.getLatitude() + ", " + LocationUtils.sLastLocation.getLongitude());
        }

        if (mWenxue != null) {
            etMc.setText(mWenxue.getMc());
            etZz.setText(mWenxue.getZz());
            etSj.setText(mWenxue.getSj());
            etFl.setText(mWenxue.getFx());
            etDd.setText(mWenxue.getDd());
            file = mWenxue.getTp();
            Glide.with(this).load(mWenxue.getTp()).into(img);
            etMc.setSelection(mWenxue.getMc().length());
            if (!App.un().equals(mWenxue.getFp())) {
                etMc.setEnabled(false);
                etZz.setEnabled(false);
                etSj.setEnabled(false);
                etDd.setEnabled(false);
                findViewById(R.id.ok).setVisibility(View.GONE);
                getSupportActionBar().setTitle("Product details");
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(AddItem.this, BgAct.class)
                                .putExtra("img", mWenxue.getTp()));
                    }
                });
                if(LocationUtils.sLastLocation != null && mWenxue != null) {
                    DistanceMapsActivity.l2 = new LatLng(mWenxue.getLat(), mWenxue.getLng());
                    String msg = "" + AndroidUIUtil.getDistance(DistanceMapsActivity.l1.longitude, DistanceMapsActivity.l1.latitude, DistanceMapsActivity.l2.longitude, DistanceMapsActivity.l2.latitude);
                    etDd.setText("" + LocationUtils.sLastLocation.getLatitude() + ", " + LocationUtils.sLastLocation.getLongitude() + " (" + msg + ")");
                }
            } else {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new net.alhazmy13.mediapicker.Image.ImagePicker.Builder(AddItem.this)
                                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                                .directory(ImagePicker.Directory.DEFAULT)
                                .extension(ImagePicker.Extension.PNG)
                                .scale(600, 600)
                                .allowMultipleImages(false)
                                .enableDebuggingMode(true)
                                .build();
                    }
                });
            }
        } else {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImagePicker.Builder(AddItem.this)
                            .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                            .directory(ImagePicker.Directory.DEFAULT)
                            .extension(ImagePicker.Extension.PNG)
                            .scale(600, 600)
                            .allowMultipleImages(false)
                            .enableDebuggingMode(true)
                            .build();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getParcelableExtra("data") != null &&
                !((Wenxue) getIntent().getParcelableExtra("data")).getFp().equals(App.un())) {
            getMenuInflater().inflate(R.menu.dt, menu);
        }
        return true;
    }

    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.jl) {
            if(LocationUtils.sLastLocation == null) {
                return true;
            }
            DistanceMapsActivity.l2 = new LatLng(mWenxue.getLat(), mWenxue.getLng());
            startActivity(new Intent(this, DistanceMapsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.dh) {
            List<BmobUser> list = App.app.getDaoSession().getBmobUserDao()
                    .queryBuilder()
                    .where(BmobUserDao.Properties.Username.eq(mWenxue.getFp()))
                    .build()
                    .list();
            if (list != null && !list.isEmpty()) {
                callPhone(list.get(0).getPhone());
            }
            return true;
        }
        if (item.getItemId() == R.id.lt) {
            try {
                startActivity(new Intent(this, ChatActivity.class)
                    .putExtra("id", App.app.getDaoSession().getBmobUserDao()
                            .queryBuilder()
                            .where(BmobUserDao.Properties.Username.eq(mWenxue.getFp()))
                            .list().get(0).getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            file = mPaths.get(0);
            Glide.with(this).load(file).into(img);
        }
    }

    public void ok(View view) {
        String mc = etMc.getText().toString();
        String zz = etZz.getText().toString();
        String sj = etSj.getText().toString();
        String dd = etDd.getText().toString();
        String fl = etFl.getText().toString();
        if (
                TextUtils.isEmpty(mc) ||
                        TextUtils.isEmpty(zz) ||
                        TextUtils.isEmpty(sj) ||
                        TextUtils.isEmpty(dd) ||
                        fl.equals("Click Select") ||
                        TextUtils.isEmpty(file)
        ) {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mWenxue == null) {
            if(LocationUtils.sLastLocation == null) {
                Toast.makeText(this, "Failed to locate. Please make sure to re publish after enabling the location", Toast.LENGTH_SHORT).show();
                return;
            }
            mWenxue = new Wenxue();
            mWenxue.setId(System.currentTimeMillis());
            mWenxue.setDd(dd);
            mWenxue.setMc(mc);
            mWenxue.setSj(sj);
            mWenxue.setTp(file);
            mWenxue.setZz(zz);
            mWenxue.setFx(fl);
            mWenxue.setLat(LocationUtils.sLastLocation.getLatitude());
            mWenxue.setLng(LocationUtils.sLastLocation.getLongitude());
            mWenxue.setFp(App.un());
            App.app.getDaoSession().getWenxueDao().insert(mWenxue);
            Toast.makeText(this, "Published successfully", Toast.LENGTH_SHORT).show();
        } else {
            mWenxue.setDd(dd);
            mWenxue.setMc(mc);
            mWenxue.setFx(fl);
            mWenxue.setSj(sj);
            mWenxue.setFx(fl);
            mWenxue.setTp(file);
            mWenxue.setZz(zz);
            mWenxue.setLat(LocationUtils.sLastLocation.getLatitude());
            mWenxue.setLng(LocationUtils.sLastLocation.getLongitude());
            App.app.getDaoSession().getWenxueDao().update(mWenxue);
            Toast.makeText(this, "Modified successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
