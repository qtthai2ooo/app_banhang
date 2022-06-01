package com.qtthai.app_ban_hang.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.model.Donhang;
import com.qtthai.app_ban_hang.model.Lichsu;
import com.qtthai.app_ban_hang.model.Taikhoan;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QLDonhangAdapter extends RecyclerView.Adapter<QLDonhangAdapter.ItemHolder> {
    Context context;
    private SharedPreferences preferences;


    public QLDonhangAdapter(Context context, ArrayList<Donhang> arrayListdonhang) {
        this.context = context;
        this.arrayListdonhang = arrayListdonhang;
        preferences = context.getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);

    }

    ArrayList<Donhang> arrayListdonhang;

    @NonNull
    @Override
    public QLDonhangAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_qldonhang,parent,false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QLDonhangAdapter.ItemHolder holder, int position) {
        Donhang donhang = arrayListdonhang.get(position);
        holder.textviewtenlichsu.setText(donhang.getTensanpham());
        holder.textviewsoluonglichsu.setText(donhang.getSoluongsanpham()+"");
        holder.textviewdatelichsu.setText(donhang.getDate());
        holder.txtmadonhang.setText(donhang.getId()+"");
        Picasso.get().load(donhang.getHinhanhsanpham()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(holder.imglichsu);


        holder.txtqltentaikhoan.setText(donhang.getTentaikhoan());
        holder.txtqlemailtaikhoan.setText(donhang.getEmailtaikhoan());
        holder.txtqlsdttaikhoan.setText(donhang.getDienthoaitaikhoan());
        holder.txtqldiachitaikhoan.setText(donhang.getDiachitaikhoan());
        Picasso.get().load(donhang.getPhototaikhoan()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(holder.imgqltaikkhoan);
        holder.btnOptiondonhang.setVisibility(View.VISIBLE);
        holder.btnOptiondonhang.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.btnOptiondonhang);
            popupMenu.inflate(R.menu.menu_taikhoan);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.delete: {
                            deletesp(arrayListdonhang.get(position).getId(),position);
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
        StringRequest request = new StringRequest(Request.Method.POST, Server.Deletedonhang, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    arrayListdonhang.remove(position);
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
        return arrayListdonhang.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView textviewtenlichsu ,textviewsoluonglichsu,textviewdatelichsu, txtmadonhang;
        public TextView txtqltentaikhoan,txtqlemailtaikhoan,txtqlsdttaikhoan,txtqldiachitaikhoan;
        public ImageView imgqltaikkhoan;
        public ImageView imglichsu;
        public ImageButton btnOptiondonhang;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            textviewtenlichsu = (TextView) itemView.findViewById(R.id.textviewtenlichsu);
            textviewsoluonglichsu = (TextView) itemView.findViewById(R.id.textviewsoluonglichsu);
            textviewdatelichsu = (TextView) itemView.findViewById(R.id.textviewdatelichsu);
            txtmadonhang = (TextView) itemView.findViewById(R.id.txtmadonhang);
            imglichsu = (ImageView) itemView.findViewById(R.id.imglichsu);

            txtqltentaikhoan = (TextView) itemView.findViewById(R.id.txtqltentaikhoan);
            txtqlemailtaikhoan = (TextView) itemView.findViewById(R.id.txtqlemailtaikhoan);
            txtqlsdttaikhoan = (TextView) itemView.findViewById(R.id.txtqlsdttaikhoan);
            txtqldiachitaikhoan = (TextView) itemView.findViewById(R.id.txtqldiachitaikhoan);
            imgqltaikkhoan = (ImageView) itemView.findViewById(R.id.imgqltaikhoan);
            btnOptiondonhang = (ImageButton) itemView.findViewById(R.id.btnOptiondonhang);
        }
    }
}
