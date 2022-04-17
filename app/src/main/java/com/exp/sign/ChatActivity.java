package com.exp.sign;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiang.geo.R;
import com.sqlite.greendao.CommunicateDao;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    public static final int INTERVAL = 2500;

    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;

    private List<ChatMsg> msgList = new ArrayList();

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            initMsgs();
        }
    };

    long id1, id2;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            postDelayed(mRunnable, INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);

        setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id1 = getIntent().getLongExtra("id",0L);

        id2 = App.id();

        if (id2 < id1) {
            long t = id2;
            id2 = id1;
            id1 = t;
        }

        adapter = new MsgAdapter(ChatActivity.this, R.layout.msg_item, msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        final long finalId = id1;
        final long finalId1 = id2;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    List<Communicate> list = App.app.getDaoSession().getCommunicateDao()
                            .queryBuilder()
                            .where(CommunicateDao.Properties.R_id.eq(finalId))
                            .where(CommunicateDao.Properties.U_id.eq(finalId1))
                            .list();
                    if (list != null && list.size() > 0) {
                        final Communicate old = list.get(0);
                        final String oldstr = old.getMsg();
                        List<ChatMsg> mmm = old.convertToMsg();
                        final ChatMsg newc = new ChatMsg(System.currentTimeMillis() + "", App.id(), content);
                        mmm.add(newc);
                        old.setRead(0);
                        old.addReadFlag(App.intid());
                        old.setMsg(JSONArray.toJSONString(mmm));
                        App.app.getDaoSession().getCommunicateDao()
                                .update(old);
                        msgList.add(newc);
                        adapter.notifyDataSetChanged();
                        msgListView.setSelection(msgList.size());
                        inputText.setText("");
                    } else {
                        Communicate news = new Communicate();
                        news.setId(System.currentTimeMillis());
                        news.setMsg("");
                        news.setRead(0);
                        news.addReadFlag(App.intid());
                        news.setR_id(finalId);
                        news.setU_id(finalId1);
                        List<ChatMsg> msg = new ArrayList<>();
                        final ChatMsg newc = new ChatMsg(System.currentTimeMillis() + "", App.id(), content);
                        msg.add(newc);
                        news.setMsg(JSON.toJSONString(msg));
                        App.app.getDaoSession().getCommunicateDao()
                                .insert(news);
                        msgList.add(newc);
                        adapter.notifyDataSetChanged();
                        msgListView.setSelection(msgList.size());
                        inputText.setText("");

                    }
                }
            }
        });
        mHandler.postDelayed(mRunnable, 100L);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMsgs() {
        List<Communicate> list = App.app.getDaoSession().getCommunicateDao()
                .queryBuilder()
                .where(CommunicateDao.Properties.R_id.eq(id1))
                .where(CommunicateDao.Properties.U_id.eq(id2))
                .list();
        if (list != null && list.size() > 0) {
            if (!list.get(0).isRead()) {
                list.get(0).addReadFlag(App.intid());
                App.app.getDaoSession()
                        .getCommunicateDao()
                        .update(list.get(0));
            }
            msgList.clear();
            msgList.addAll(list.get(0).convertToMsg());
            adapter.notifyDataSetChanged();
            msgListView.setSelection(msgList.size());
        }
    }

}