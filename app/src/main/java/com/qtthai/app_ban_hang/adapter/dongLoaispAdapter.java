package com.qtthai.app_ban_hang.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.activity.ChiTietSanPham;
import com.qtthai.app_ban_hang.activity.DienThoaiActivity;
import com.qtthai.app_ban_hang.model.Loaisp;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class dongLoaispAdapter extends RecyclerView.Adapter<dongLoaispAdapter.ItemHolder> {
    Context context;

    public dongLoaispAdapter(ArrayList<Loaisp> arrayListloaisp, Context context) {
        this.context = context;
        this.arrayListloaisp = arrayListloaisp;
    }

    ArrayList<Loaisp> arrayListloaisp;

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_loaisp,parent,false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Loaisp loaisp = arrayListloaisp.get(position);
        holder.textViewloaisp.setText(loaisp.getTenloaisp());
        Picasso.get().load(loaisp.getHinhanhloaisp()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(holder.imgloaisp);
    }

    @Override
    public int getItemCount() {
        return arrayListloaisp.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView textViewloaisp;
        public ImageView imgloaisp;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewloaisp = (TextView) itemView.findViewById(R.id.txt_loaisp);
            imgloaisp = (ImageView) itemView.findViewById(R.id.imgqlloaisp);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DienThoaiActivity.class);
                String a = String.valueOf(arrayListloaisp.get(getPosition()).getId());
                String tenloaisp = String.valueOf(arrayListloaisp.get(getPosition()).getTenloaisp());
                intent.putExtra("idloaisanpham",a);
                intent.putExtra("tenloaisanpham",tenloaisp);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CheckConnection.ShowToast_Short(context,arrayListloaisp.get(getPosition()).getId()+"");
                context.startActivity(intent);
            });
        }
    }
}
