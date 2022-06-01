package com.qtthai.app_ban_hang.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qtthai.app_ban_hang.Admin.thongtin_loaisp;
import com.qtthai.app_ban_hang.Admin.thongtin_sp;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.activity.ChiTietSanPham;
import com.qtthai.app_ban_hang.model.Loaisp;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QLSanphamAdapter extends RecyclerView.Adapter<QLSanphamAdapter.ItemHolder> {
    Context context;
    private SharedPreferences preferences;


    public QLSanphamAdapter(Context context, ArrayList<Sanpham> arrayListdienthoai) {
        this.context = context;
        this.arrayListdienthoai = arrayListdienthoai;
        preferences = context.getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);

    }

    ArrayList<Sanpham> arrayListdienthoai;

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_qlsanpham,parent,false);
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
        holder.btnOptionsp.setVisibility(View.VISIBLE);
        holder.btnOptionsp.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.btnOptionsp);
            popupMenu.inflate(R.menu.menu_sp_options);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.sp_edit: {
                            Intent i = new Intent(context, thongtin_sp.class);
                            i.putExtra("idsp",arrayListdienthoai.get(position).getId());
                            i.putExtra("idloaisp",arrayListdienthoai.get(position).getIdsanpham()+"");
                            i.putExtra("tensp",arrayListdienthoai.get(position).getTensanpham());
                            i.putExtra("giasp",arrayListdienthoai.get(position).getGiasanpham()+"");
                            i.putExtra("motasp",arrayListdienthoai.get(position).getMotasanpham());
                            i.putExtra("hinhsp",arrayListdienthoai.get(position).getHinhanhsanpham());
                            context.startActivity(i);
                            return true;
                        }
                        case R.id.sp_delete: {
                            deletesp(arrayListdienthoai.get(position).getId(),position);
                            return true;
                        }
                        case R.id.sp_add: {
                            Intent i = new Intent(context, thongtin_sp.class);
                            context.startActivity(i);
                            return true;
                        }
                    }

                    return false;
                }
            });
            popupMenu.show();
        });

    }

    private void deletesp(int id, int position) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.xoasanpham, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    arrayListdienthoai.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id+"");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return arrayListdienthoai.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView txttendienthoai,txtgiadienthoai,txtmotadienthoai;
        public ImageView imgdienthoai;
        public ImageButton btnOptionsp;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txttendienthoai = (TextView) itemView.findViewById(R.id.textviewdienthoai);
            txtgiadienthoai = (TextView) itemView.findViewById(R.id.textviewgiadienthoai);
            txtmotadienthoai = (TextView) itemView.findViewById(R.id.textviewmotadienthoai);
            imgdienthoai = (ImageView) itemView.findViewById(R.id.imgdienthoai);
            btnOptionsp = (ImageButton) itemView.findViewById(R.id.btnOptionsp);
        }
    }
}
