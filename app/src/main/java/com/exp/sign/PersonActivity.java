package com.exp.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiang.geo.R;
import com.sqlite.greendao.BmobUserDao;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class PersonActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private TextView tvName;
    private TextView tvSex;
    private TextView tv;
    private TextView tv_dd;
    private TextView tvPhone;
    private TextView tvEmail;
    private Button exit;
    private Button cpwd;
    private AVLoadingIndicatorView loadingIndicatorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal);
        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loading);
        tvWelcome = (TextView) findViewById(R.id.tv_welcome);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tv = (TextView) findViewById(R.id.tv_);
        tv_dd = (TextView) findViewById(R.id.tv_dd);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        exit = (Button) findViewById(R.id.exit);
        cpwd = (Button) findViewById(R.id.cpwd);
        loadingIndicatorView.show();
        if (getIntent().getStringExtra("username")
                .equalsIgnoreCase((String) SPUtil.get(this, "username", ""))) {
            exit.setVisibility(View.VISIBLE);
            cpwd.setVisibility(View.VISIBLE);
        }

        List<BmobUser> list = App.app.getDaoSession().getBmobUserDao().queryBuilder()
                .where(BmobUserDao.Properties.Username.eq(getIntent().getStringExtra("username")))
                .list();
        loadingIndicatorView.hide();
        if (list != null && list.size() > 0) {
            BmobUser user = list.get(0);
            tvWelcome.setText("user：" + user.getUsername());
            tvName.setText(user.getName());
            tvSex.setText(user.getSex());
            tv.setText(user.getNumber());
            tvPhone.setText(user.getPhone());
            tvEmail.setText(user.getEmail());
            tv_dd.setText(user.getAddress());
        }

    }

    public void changePersonInfo(View view) {
        if (getIntent().getStringExtra("username")
                .equalsIgnoreCase((String) SPUtil.get(this, "username", ""))) {
            startActivity(new Intent(this, FillInfoActivity.class)
                    .putExtra("username", getIntent().getStringExtra("username")));
        } else {
            startActivity(new Intent(this, FillInfoActivity.class)
                    .putExtra("type", getIntent().getStringExtra("type"))
                    .putExtra("username", getIntent().getStringExtra("username"))
                    .putExtra("ccpwd", true));
        }
    }

    public void exit(View view) {
        SPUtil.put(this, "username", "");
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void changePwd(View view) {
        startActivity(new Intent(this, CPWDActivity.class));
    }
}
