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
import com.qtthai.app_ban_hang.model.Loaisp;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DienthoaiAdapter extends RecyclerView.Adapter<DienthoaiAdapter.ItemHolder> {
    Context context;

    public DienthoaiAdapter(Context context, ArrayList<Sanpham> arrayListdienthoai) {
        this.context = context;
        this.arrayListdienthoai = arrayListdienthoai;
    }

    ArrayList<Sanpham> arrayListdienthoai;

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_dienthoai,parent,false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Sanpham sanpham = arrayListdienthoai.get(position);
        holder.txttendienthoai.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgiadienthoai.setText("Giá :"+decimalFormat.format(sanpham.getGiasanpham())+" VNĐ");
        holder.txtmotadienthoai.setMaxLines(2);
        holder.txtmotadienthoai.setEllipsize(TextUtils.TruncateAt.END);
        holder.txtmotadienthoai.setText(sanpham.getMotasanpham());
        Picasso.get().load(sanpham.getHinhanhsanpham()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(holder.imgdienthoai);
    }

    @Override
    public int getItemCount() {
        return arrayListdienthoai.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView txttendienthoai,txtgiadienthoai,txtmotadienthoai;
        public ImageView imgdienthoai;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txttendienthoai = (TextView) itemView.findViewById(R.id.textviewdienthoai);
            txtgiadienthoai = (TextView) itemView.findViewById(R.id.textviewgiadienthoai);
            txtmotadienthoai = (TextView) itemView.findViewById(R.id.textviewmotadienthoai);
            imgdienthoai = (ImageView) itemView.findViewById(R.id.imgdienthoai);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",arrayListdienthoai.get(getPosition()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CheckConnection.ShowToast_Short(context,arrayListdienthoai.get(getPosition()).getTensanpham());
                context.startActivity(intent);
            });
        }
    }
}
