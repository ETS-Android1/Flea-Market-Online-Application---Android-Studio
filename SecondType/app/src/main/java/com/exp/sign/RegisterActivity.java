package com.exp.sign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jiang.geo.R;
import com.sqlite.greendao.BmobUserDao;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.exp.sign.MainActivity.instance;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cv_add)
    CardView cvAdd;

    @BindView(R.id.et_username)
    EditText editText;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repeatpassword)
    EditText etReaptPassword;

    @BindView(R.id.male)
    RadioButton male;

    @BindView(R.id.putong)
    RadioButton rbpt;
    @BindView(R.id.guanli)
    RadioButton rbgl;

    @BindView(R.id.et_no)
    EditText etno;
    @BindView(R.id.et_name)
    EditText etname;
    @BindView(R.id.et_phone)
    EditText etphone;
    @BindView(R.id.et_email)
    EditText etemail;
    @BindView(R.id.loading)
    AVLoadingIndicatorView loadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        loadingIndicatorView.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    public void next(View view) {
        final String username = editText.getText().toString();
        final String password = etPassword.getText().toString();
        String repeatPassword = etReaptPassword.getText().toString();
        String no = etno.getText().toString();
        final String name = etname.getText().toString();
        String phone = etphone.getText().toString();
        String email = etemail.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)
                || TextUtils.isEmpty(no) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)|| TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!repeatPassword.equals(password)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if("admin".equals(username)) {
            Toast.makeText(this, "用户名非法，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        String sex = "男";
        if (!male.isChecked())
            sex = "女";
        loadingIndicatorView.show();
        final BmobUser user = new BmobUser();
        user.setId(System.currentTimeMillis());
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setName(name);
        user.setNumber(no);
        user.setEmail(email);
        user.setPhone(phone);
        List<BmobUser> list = App.app.getDaoSession().getBmobUserDao().queryBuilder()
                .where(BmobUserDao.Properties.Username.eq(username))
                .list();
        if (list != null && list.size() > 0) {
            loadingIndicatorView.hide();
            Toast.makeText(RegisterActivity.this, "User already exists, please change user name", Toast.LENGTH_SHORT).show();
            return;
        }
        App.app.getDaoSession().getBmobUserDao().insertOrReplace(user);
        loadingIndicatorView.hide();
        SPUtil.put(RegisterActivity.this, "id", user.getId());
        SPUtil.put(RegisterActivity.this, "name", name);
        SPUtil.put(RegisterActivity.this, "username", username);
        SPUtil.put(RegisterActivity.this, "password", password);
        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        gotoHome();
    }

    private void gotoHome() {
        if (instance != null) {
            instance.finish();
            instance = null;
        }
        App.home(this);
    }
}
