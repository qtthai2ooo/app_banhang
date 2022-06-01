package com.qtthai.app_ban_hang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.activity.Trangchu;
import com.qtthai.app_ban_hang.model.Giohang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GiohangAdapter extends BaseAdapter {
    public GiohangAdapter(Context context, ArrayList<Giohang> arraygiohang) {
        this.context = context;
        this.arraygiohang = arraygiohang;
    }

    Context context;
    ArrayList<Giohang> arraygiohang;
    @Override
    public int getCount() {
        return arraygiohang.size();
    }

    @Override
    public Object getItem(int position) {
        return arraygiohang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        public TextView txttengiohang, txtgiagiohang;
        public ImageView imggiohang;
        public Button btnvalues,btnminus,btnplus;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_giohang,null);
            viewHolder.txttengiohang = convertView.findViewById(R.id.textviewtengiohang);
            viewHolder.txtgiagiohang = convertView.findViewById(R.id.textviewgiagiohang);
            viewHolder.imggiohang = convertView.findViewById(R.id.imageviewgiohang);
            viewHolder.btnminus = convertView.findViewById(R.id.buttonminus);
            viewHolder.btnvalues = convertView.findViewById(R.id.buttonvalues);
            viewHolder.btnplus = convertView.findViewById(R.id.buttonplus);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Giohang giohang = (Giohang) getItem(position);
        viewHolder.txttengiohang.setText(giohang.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiagiohang.setText(decimalFormat.format(giohang.getGiasp())+" VNĐ");
        Picasso.get().load(giohang.getHinhsp())
                .placeholder(R.drawable.noimage).error(R.drawable.error).into(viewHolder.imggiohang);
        viewHolder.btnvalues.setText(giohang.getSoluongsp()+"");
        int sl = Integer.parseInt(viewHolder.btnvalues.getText().toString());
        if (sl>=10){
            viewHolder.btnplus.setVisibility(View.INVISIBLE);
            viewHolder.btnminus.setVisibility(View.VISIBLE);
        }else if (sl<=1) {
            viewHolder.btnplus.setVisibility(View.VISIBLE);
            viewHolder.btnminus.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.btnplus.setVisibility(View.VISIBLE);
            viewHolder.btnminus.setVisibility(View.VISIBLE);
        }
        ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnplus.setOnClickListener(v -> {
            int slmoinhat = Integer.parseInt(finalViewHolder.btnvalues.getText().toString()) + 1;
            int slht = Trangchu.manggiohang.get(position).getSoluongsp();
            long giaht = Trangchu.manggiohang.get(position).getGiasp();
            Trangchu.manggiohang.get(position).setSoluongsp(slmoinhat);
            long giamoinhat = (giaht * slmoinhat) / slht;
            Trangchu.manggiohang.get(position).setGiasp(giamoinhat);
            DecimalFormat decimalFormat1 = new DecimalFormat("###,###,###");
            finalViewHolder.txtgiagiohang.setText(decimalFormat1.format(giamoinhat)+ " VNĐ");
            com.qtthai.app_ban_hang.activity.Giohang.EventUltil();
            if (slmoinhat > 9){
                finalViewHolder.btnplus.setVisibility(View.INVISIBLE);
                finalViewHolder.btnminus.setVisibility(View.VISIBLE);
                finalViewHolder.btnvalues.setText(String.valueOf(slmoinhat));
            }else {
                finalViewHolder.btnminus. setVisibility(View.VISIBLE);
                finalViewHolder.btnplus. setVisibility(View.VISIBLE);
                finalViewHolder.btnvalues.setText(String.valueOf(slmoinhat));
            }
        });
        viewHolder.btnminus.setOnClickListener(v -> {
            int slmoinhat = Integer.parseInt(finalViewHolder.btnvalues.getText().toString()) - 1;
            int slhientai = Trangchu.manggiohang.get(position).getSoluongsp();
            long giaht = Trangchu.manggiohang.get(position).getGiasp();
            Trangchu.manggiohang.get(position).setSoluongsp(slmoinhat);
            long giamoinhat = (giaht * slmoinhat) / slhientai;
            Trangchu.manggiohang.get(position).setGiasp(giamoinhat);
            DecimalFormat decimalFormat12 = new DecimalFormat("###,###,###");
            finalViewHolder.txtgiagiohang.setText(decimalFormat12.format(giamoinhat)+ " VNĐ");
            com.qtthai.app_ban_hang.activity.Giohang.EventUltil();
            if (slmoinhat < 2){
                finalViewHolder.btnminus.setVisibility(View.INVISIBLE);
                finalViewHolder.btnplus.setVisibility(View.VISIBLE);
                finalViewHolder.btnvalues.setText(String.valueOf(slmoinhat));
            }else {
                finalViewHolder.btnminus. setVisibility(View.VISIBLE);
                finalViewHolder.btnplus. setVisibility(View.VISIBLE);
                finalViewHolder.btnvalues.setText(String.valueOf(slmoinhat));
            }
        });
        return convertView;
    }
}
