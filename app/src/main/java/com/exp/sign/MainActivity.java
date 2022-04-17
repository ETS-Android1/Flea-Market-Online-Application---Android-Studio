package com.exp.sign;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jiang.geo.R;
import com.jiang.geo.util.LocationUtils;
import com.sqlite.greendao.BmobUserDao;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_go)
    Button btGo;
    @BindView(R.id.cv)
    CardView cv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.putong)
    RadioButton rbpt;
    @BindView(R.id.guanli)
    RadioButton rbgl;
    @BindView(R.id.loading)
    AVLoadingIndicatorView loadingIndicatorView;

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        instance = this;
        App.home(this);
        checkLocationPermission();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}, 100);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}, 100);
            }
            return false;

        } else {
            LocationUtils.location(this);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gotoHome() {
        instance = null;
        App.home(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                final String un = etUsername.getText().toString();
                final String pd = etPassword.getText().toString();
                if (TextUtils.isEmpty(un) || TextUtils.isEmpty(pd)) {
                    Toast.makeText(MainActivity.this, "Please enter a user name or password", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingIndicatorView.show();
                loadingIndicatorView.hide();
                List<BmobUser> list = App.app.getDaoSession().getBmobUserDao().queryBuilder()
                        .where(BmobUserDao.Properties.Username.eq(un), BmobUserDao.Properties.Password.eq(pd))
                        .list();
                if (list == null || list.size() == 0) {
                    Toast.makeText(MainActivity.this, "Wrong user name or password", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SPUtil.put(MainActivity.this, "id", list.get(0).getId());
                    SPUtil.put(MainActivity.this, "name", list.get(0).getName());
                    SPUtil.put(MainActivity.this, "username", un);
                    SPUtil.put(MainActivity.this, "password", pd);
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    gotoHome();
                }
                break;
        }
    }
}
