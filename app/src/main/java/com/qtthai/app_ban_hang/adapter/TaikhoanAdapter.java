package com.qtthai.app_ban_hang.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.activity.ChiTietSanPham;
import com.qtthai.app_ban_hang.model.Lichsu;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.model.User;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TaikhoanAdapter extends RecyclerView.Adapter<TaikhoanAdapter.ItemHolder> {
    Context context;

    public TaikhoanAdapter(Context context, ArrayList<Lichsu> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    ArrayList<Lichsu> arrayList;

    @NonNull
    @Override
    public TaikhoanAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_lichsu,parent,false);
        TaikhoanAdapter.ItemHolder itemHolder = new TaikhoanAdapter.ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaikhoanAdapter.ItemHolder holder, int position) {
        Lichsu lichsu = arrayList.get(position);
        holder.txttenlichsu.setText(lichsu.getTensanpham());
        holder.txtngaydatlichsu.setText(lichsu.getDate());
        holder.txtsoluonglichsu.setText("Số lượng : "+lichsu.getSoluongsanpham());
/*        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgiadienthoai.setText("Giá :"+decimalFormat.format(sanpham.getGiasanpham())+" VNĐ");
        holder.txtmotadienthoai.setMaxLines(2);
        holder.txtmotadienthoai.setEllipsize(TextUtils.TruncateAt.END);
        holder.txtmotadienthoai.setText(sanpham.getMotasanpham());*/
        Picasso.get().load(arrayList.get(position).getHinhanhsanpham()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(holder.imglichsu);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView txtAccountName,txtsoluonglichsu,txttenlichsu,txtngaydatlichsu;
        public ImageView imgAccountProfile,imglichsu;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtAccountName = (TextView) itemView.findViewById(R.id.txtAccountName);
            txtsoluonglichsu = (TextView) itemView.findViewById(R.id.textviewsoluonglichsu);
            txtngaydatlichsu = (TextView) itemView.findViewById(R.id.textviewdatelichsu);
            txttenlichsu = (TextView) itemView.findViewById(R.id.textviewtenlichsu);
            imglichsu = itemView.findViewById(R.id.imglichsu);
            imgAccountProfile = (ImageView) itemView.findViewById(R.id.imgAccountProfile);
            itemView.setOnClickListener(v -> {

            });
        }
    }
}
