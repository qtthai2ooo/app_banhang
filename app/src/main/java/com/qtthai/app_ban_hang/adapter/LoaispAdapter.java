package com.qtthai.app_ban_hang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.model.Loaisp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoaispAdapter extends BaseAdapter {
    ArrayList<Loaisp> arrayListloaisp;

    public LoaispAdapter(ArrayList<Loaisp> arrayListloaisp, Context context) {
        this.arrayListloaisp = arrayListloaisp;
        this.context = context;
    }

    Context context;
    @Override
    public int getCount() {
        return arrayListloaisp.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListloaisp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHoldel{
        TextView textViewloaisp;
        ImageView imgloaisp;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoldel viewHoldel = null;
        if (convertView == null){
            viewHoldel = new ViewHoldel();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_loaisp,null);
            viewHoldel.textViewloaisp = (TextView) convertView.findViewById(R.id.textviewloaisp);
            viewHoldel.imgloaisp = (ImageView) convertView.findViewById(R.id.imageviewloaisp);
            convertView.setTag(viewHoldel);
        }else {
            viewHoldel = (ViewHoldel) convertView.getTag();
        }
        Loaisp loaisp = (Loaisp) getItem(position);
        viewHoldel.textViewloaisp.setText(loaisp.getTenloaisp());
        Picasso.get().load(loaisp.getHinhanhloaisp()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(viewHoldel.imgloaisp);
        return convertView;
    }
}
