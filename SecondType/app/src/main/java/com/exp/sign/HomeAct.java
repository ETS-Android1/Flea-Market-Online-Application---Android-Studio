package com.exp.sign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jiang.geo.R;

public class HomeAct extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout com;
    private TextView zy;
    private TextView wd;
    private ZyFragment mZyFragment;
    private WdFragment mWdFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccc);
        com = (FrameLayout) findViewById(R.id.com);
        zy = (TextView) findViewById(R.id.zy);
        wd = (TextView) findViewById(R.id.wd);
        getFragmentManager().beginTransaction().add(com.getId(), mWdFragment = new WdFragment()).commitAllowingStateLoss();
        getFragmentManager().beginTransaction().add(com.getId(), mZyFragment = new ZyFragment()).commitAllowingStateLoss();
        getFragmentManager().beginTransaction().hide(mWdFragment).commitAllowingStateLoss();
        zy.setOnClickListener(this);
        wd.setOnClickListener(this);
        checkLocationPermission();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}, 100);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}, 100);
            }
            return false;

        } else
            return true;
    }

    MenuItem mMenuItemSx;
    MenuItem mMenuItemSs;
    MenuItem mMenuItemFb;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit, menu);
        mMenuItemSx = menu.findItem(R.id.sx);
        mMenuItemSs = menu.findItem(R.id.ss);
        mMenuItemFb = menu.findItem(R.id.fb);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.exit) {
            SPUtil.put(this,"username", "");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        if(item.getItemId() == R.id.ss) {
            mZyFragment.ss();
            return true;
        }
        if(item.getItemId() == R.id.sx) {
            mZyFragment.sx();
            return true;
        }
        if(item.getItemId() == R.id.fb) {
            startActivity(new Intent(this, AddItem.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().beginTransaction().hide(mZyFragment).commitAllowingStateLoss();
        getFragmentManager().beginTransaction().hide(mWdFragment).commitAllowingStateLoss();
        zy.setTextColor(0xff333333);
        wd.setTextColor(0xff333333);
        if(v == zy) {
            mMenuItemSx.setVisible(true);
            mMenuItemSs.setVisible(true);
            mMenuItemFb.setVisible(true);
            zy.setTextColor(getResources().getColor(R.color.colorAccent));
            getFragmentManager().beginTransaction().show(mZyFragment).commitAllowingStateLoss();
        }
        if(v == wd) {
            mMenuItemSx.setVisible(false);
            mMenuItemSs.setVisible(false);
            mMenuItemFb.setVisible(false);
            wd.setTextColor(getResources().getColor(R.color.colorAccent));
            getFragmentManager().beginTransaction().show(mWdFragment).commitAllowingStateLoss();
        }
    }
}
