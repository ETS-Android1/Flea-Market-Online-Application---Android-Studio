package com.exp.sign;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ItemAdapter;
import com.jiang.geo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SjFragment extends Fragment {

    private ListView list;
    private ItemAdapter mItemAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_zy, container, false);
        list = (ListView) root.findViewById(R.id.list);
        list.setAdapter(mItemAdapter = new ItemAdapter(getActivity()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Wenxue wenxue = mItemAdapter.getItem(position);
                startActivity(new Intent(getActivity(), AddItem.class)
                        .putExtra("data", wenxue));

            }
        });
        return root;
    }

    public void g() {
        List<Wenxue> wenxueList = App.app.getDaoSession().getWenxueDao().queryBuilder().list();
        if(wenxueList == null) {
            wenxueList = new ArrayList<>();
        }
        Iterator<Wenxue> wx = wenxueList.iterator();
        while (wx.hasNext()) {
            Wenxue wenxue = wx.next();
            if((Boolean) SPUtil.get(getActivity(), (String) SPUtil.get(getActivity(), "username", "") + wenxue.getId(), false)) {

            }else {
                wx.remove();
            }
        }
        Collections.shuffle(wenxueList);
        mItemAdapter.setObjects(wenxueList);
    }
}
