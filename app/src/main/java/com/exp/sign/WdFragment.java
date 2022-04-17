package com.exp.sign;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiang.geo.R;

import java.util.Iterator;
import java.util.List;

public class WdFragment extends Fragment {

    private ImageView b1;
    private ImageView b2;
    private ImageView b3;
    private ImageView news;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_wd, container, false);
        b1 = (ImageView) root.findViewById(R.id.b1);
        b2 = (ImageView) root.findViewById(R.id.b2);
        b3 = (ImageView) root.findViewById(R.id.lt);
        news = (ImageView) root.findViewById(R.id.news);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FillInfoActivity.class)
                        .putExtra("username", App.un()));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CPWDActivity.class));
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatsAct.class));
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initMsgs();
    }

    private void initMsgs() {
        List<Communicate> list = App.app.getDaoSession().getCommunicateDao()
                .queryBuilder()
                .list();
        filter(list);
    }

    private void filter(List<Communicate> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        boolean hasNew = false;
        Iterator<Communicate> iterator = list.iterator();
        while (iterator.hasNext()) {
            Communicate communicate = iterator.next();
            if (communicate == null) {
                continue;
            }
            if (communicate.getR_id() == App.id() || communicate.getU_id() == App.id()) {
                if (!communicate.isRead()) {
                    hasNew = true;
                }
            } else {
                iterator.remove();
            }
        }
        if (hasNew) {
            news.setVisibility(View.VISIBLE);
        } else {
            news.setVisibility(View.GONE);
        }
    }
}
