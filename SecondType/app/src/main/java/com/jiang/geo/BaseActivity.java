package com.jiang.geo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setHomeTrue() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    protected abstract int getContentId();
}
