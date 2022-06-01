package com.qtthai.app_ban_hang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class QLLoaispAdapter extends RecyclerView.Adapter<QLLoaispAdapter.ItemHolder> {
    Context context;
    private SharedPreferences preferences;

    public QLLoaispAdapter(Context context, ArrayList<Loaisp> mangloaisp) {
        this.context = context;
        this.mangloaisp = mangloaisp;
        preferences = context.getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
    }

    ArrayList<Loaisp> mangloaisp;

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_ql_loaisp,parent,false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Loaisp loaisp = mangloaisp.get(position);
        holder.txtqltenloaisp.setText(loaisp.getTenloaisp());
        Picasso.get().load(loaisp.getHinhanhloaisp()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(holder.imgqlloaisp);

        holder.btnOptionloaisp.setVisibility(View.VISIBLE);


        holder.btnOptionloaisp.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(context,holder.btnOptionloaisp);
            popupMenu.inflate(R.menu.menu_loaisp_options);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.loaisp_edit: {
                            Intent i = new Intent(context, thongtin_loaisp.class);
                            i.putExtra("idloaisp",mangloaisp.get(position).getId());
                            i.putExtra("position",position);
                            i.putExtra("tenloaisp",mangloaisp.get(position).getTenloaisp());
                            i.putExtra("hinhloaisp",mangloaisp.get(position).getHinhanhloaisp());
                            context.startActivity(i);
                            return true;
                        }
                        case R.id.loaisp_delete: {
                            deleteloaisp(mangloaisp.get(position).getId(),position);
                            return true;
                        }
                        case R.id.loaisp_add: {
                            Intent i = new Intent(context, thongtin_loaisp.class);
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

    private void deleteloaisp(int id, int position) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.xoaloaisanpham, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    mangloaisp.remove(position);
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
        return mangloaisp.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView txtqltenloaisp,txtqlsosp;
        public ImageView imgqlloaisp;
        public ImageButton btnOptionloaisp;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtqltenloaisp = (TextView) itemView.findViewById(R.id.ql_tenloaisp);
/*            txtqlsosp = (TextView) itemView.findViewById(R.id.ql_sosp);*/
            imgqlloaisp = (ImageView) itemView.findViewById(R.id.imgqlloaisp);
            btnOptionloaisp = (ImageButton) itemView.findViewById(R.id.btnOptionloaisp);
            btnOptionloaisp.setVisibility(View.GONE);
        }
    }
}
