package com.exp.sign;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ItemAdapter;
import com.jiang.geo.R;
import com.sqlite.greendao.WenxueDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ZyFragment extends Fragment {

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
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Wenxue wenxue = mItemAdapter.getItem(position);
                if (!wenxue.getFp().equals(App.un())) {
                    return false;
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("Tips")
                        .setMessage("Are you sure to delete?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.app.getDaoSession().getWenxueDao().delete(wenxue);
                                Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                getData();
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
                return true;
            }
        });
        getData();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        List<Wenxue> wenxueList = App.app.getDaoSession().getWenxueDao().queryBuilder().list();
        if (wenxueList == null) {
            wenxueList = new ArrayList<>();
        }
        Collections.shuffle(wenxueList);
        mItemAdapter.setObjects(wenxueList);
    }

    public void sx() {
        final String[] items = {"Clothing", "Cosmetics", "Maternal and infant products", "Electronic equipment", "Home Depot", "Virtual goods", "Other"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(getActivity());
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    getData();
                    return;
                }
                List<Wenxue> wenxueList = App.app.getDaoSession().getWenxueDao().queryBuilder()
                        .where(WenxueDao.Properties.Fx.eq(items[which]))
                        .list();
                if (wenxueList == null) {
                    wenxueList = new ArrayList<>();
                }
                Collections.shuffle(wenxueList);
                mItemAdapter.setObjects(wenxueList);
            }
        });
        listDialog.show();
    }

    public void ss() {
        final String[] items = {"ALL", "SEARCH"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(getActivity());
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    getData();
                } else {
                    final EditText editText = new EditText(getActivity());
                    AlertDialog.Builder inputDialog =
                            new AlertDialog.Builder(getActivity());
                    inputDialog.setTitle("Enter keywords").setView(editText);
                    inputDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String key = editText.getText().toString();
                                    if (TextUtils.isEmpty(key)) {
                                        return;
                                    }
                                    List<Wenxue> wenxueList = App.app.getDaoSession().getWenxueDao().queryBuilder().list();
                                    if (wenxueList == null) {
                                        wenxueList = new ArrayList<>();
                                    }
                                    Collections.shuffle(wenxueList);
                                    Iterator<Wenxue> wenxueIterator = wenxueList.iterator();
                                    while (wenxueIterator.hasNext()) {
                                        Wenxue wenxue = wenxueIterator.next();
                                        if (wenxue.getDd().contains(key)
                                                || wenxue.getFx().contains(key)
                                                || wenxue.getMc().contains(key)
                                                || wenxue.getMs().contains(key)
                                                || wenxue.getSj().contains(key)
                                                || wenxue.getTp().contains(key)
                                                || wenxue.getZz().contains(key)) {

                                        } else {
                                            wenxueIterator.remove();
                                        }
                                    }
                                    mItemAdapter.setObjects(wenxueList);
                                }
                            }).show();
                }
            }
        });
        listDialog.show();
    }
}
