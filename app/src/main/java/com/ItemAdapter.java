package com;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exp.sign.Wenxue;
import com.jiang.geo.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends BaseAdapter {

    private List<Wenxue> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public void setObjects(List<Wenxue> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public ItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Wenxue getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final Wenxue object, ViewHolder holder) {
        holder.title.setText(object.getMc());
        Glide.with(context).load(object.getTp()).into(holder.img);
    }

    protected class ViewHolder {
        private TextView title;
        private ImageView img;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
