package com.exp.sign;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jiang.geo.R;
import com.sqlite.greendao.BmobUserDao;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.ButterKnife;

public class FillInfoActivity extends AppCompatActivity {

    private CardView cvAdd;
    private EditText etName;
    private RadioButton male;
    private RadioButton female;
    private EditText etNo;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etAddress;
    private Button btGo;
    private FloatingActionButton fab;
    private LinearLayout llCpwd;
    private EditText etPwd;
    private AVLoadingIndicatorView loadingIndicatorView;

    private String username;
    private BmobUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        ButterKnife.bind(this);

        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loading);
        loadingIndicatorView.show();
        cvAdd = (CardView) findViewById(R.id.cv_add);
        etName = (EditText) findViewById(R.id.et_name);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        etNo = (EditText) findViewById(R.id.et_no);
        etAddress = (EditText) findViewById(R.id.et_address);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etEmail = (EditText) findViewById(R.id.et_email);
        btGo = (Button) findViewById(R.id.bt_go);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        llCpwd = (LinearLayout) findViewById(R.id.ll_cpwd);
        etPwd = (EditText) findViewById(R.id.et_pwd);

        username = getIntent().getStringExtra("username");

        if(getIntent().getBooleanExtra("ccpwd",false)){
            llCpwd.setVisibility(View.VISIBLE);
        }

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
            }
        });

        loadingIndicatorView.hide();
        List<BmobUser> list = App.app.getDaoSession().getBmobUserDao().queryBuilder()
                .where(BmobUserDao.Properties.Username.eq(username))
                .list();
        if(list!=null && list.size() > 0){
            user = list.get(0);
            etPwd.setText(user.getPassword());
            etEmail.setText(user.getEmail());
            etName.setText(user.getName());
            etNo.setText(user.getNumber());
            etPhone.setText(user.getPhone());
            if(user.getSex().equalsIgnoreCase("女")){
                female.setChecked(true);
            }
        }

    }

    public void next(View view) {

        final String name = etName.getText().toString();
        final String email = etEmail.getText().toString();
        final String sex = male.isChecked()?"male":"female";
        final String phone = etPhone.getText().toString();
        final String no = etNo.getText().toString();
        final String pwd = etPwd.getText().toString();
        final String add = etAddress.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(pwd)
                || TextUtils.isEmpty(sex)
                || TextUtils.isEmpty(add)
                || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(no)) {
            Toast.makeText(this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingIndicatorView.show();
        user.setPassword(pwd);
        user.setName(name);
        user.setEmail(email);
        user.setNumber(no);
        user.setPhone(phone);
        user.setSex(sex);
        user.setAddress(add);
        App.app.getDaoSession().getBmobUserDao().update(user);
        Toast.makeText(FillInfoActivity.this, "Modified successfully", Toast.LENGTH_SHORT).show();
        gotoHome();
    }

    private void gotoHome() {
        finish();
    }
}
