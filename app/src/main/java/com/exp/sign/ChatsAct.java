package com.exp.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jiang.geo.R;

import java.util.Iterator;
import java.util.List;

public class ChatsAct extends AppCompatActivity {

    public static final int INTERVAL = 10000;

    public static boolean sIsNormalUser = false;

    private long rid;
    private long uid;

    private ListView list;
    private ArrayAdapter<Communicate> mCommunicateArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);
        setTitle("Chat list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.list);
        mCommunicateArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(mCommunicateArrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Communicate communicate = mCommunicateArrayAdapter.getItem(i);
                long id = 0L;
                if(communicate.getU_id() == App.id()) {
                    id = communicate.getR_id();
                } else {
                    id = communicate.getU_id();
                }
                startActivity(new Intent(ChatsAct.this, ChatActivity.class)
                    .putExtra("id", id));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMsgs();
    }

    private void initMsgs() {
        List<Communicate> list = App.app.getDaoSession().getCommunicateDao()
                .queryBuilder()
                .list();
        filter(list);
        if (list != null && list.size() > 0) {
            mCommunicateArrayAdapter.clear();
            mCommunicateArrayAdapter.addAll(list);
            mCommunicateArrayAdapter.notifyDataSetChanged();
        }
    }

    private void filter(List<Communicate> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Iterator<Communicate> iterator = list.iterator();
        while (iterator.hasNext()) {
            Communicate communicate = iterator.next();
            if (communicate == null) {
                continue;
            }
            if (communicate.getR_id() == App.id() || communicate.getU_id() == App.id()) {
            } else {
                iterator.remove();
            }
        }
    }

}
