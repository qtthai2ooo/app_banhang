package com.qtthai.app_ban_hang.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
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
import com.qtthai.app_ban_hang.Admin.thongtin_sp;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.model.Taikhoan;
import com.qtthai.app_ban_hang.model.User;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class QLTaikhoanAdapter extends RecyclerView.Adapter<QLTaikhoanAdapter.ItemHolder> {
    Context context;
    private SharedPreferences preferences;


    public QLTaikhoanAdapter(Context context, ArrayList<Taikhoan> arrayListtaikhoan) {
        this.context = context;
        this.arrayListtaikhoan = arrayListtaikhoan;
        preferences = context.getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);

    }

    ArrayList<Taikhoan> arrayListtaikhoan;

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_qltaikhoan,parent,false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QLTaikhoanAdapter.ItemHolder holder, int position) {
        Taikhoan taikhoan = arrayListtaikhoan.get(position);
        holder.txtqltentaikhoan.setText(taikhoan.getTen());
        holder.txtqlemailtaikhoan.setText(taikhoan.getEmail());
        holder.txtqlsdttaikhoan.setText(taikhoan.getDienthoai());
        holder.txtqldiachitaikhoan.setText(taikhoan.getDiachi());
        Picasso.get().load(taikhoan.getPhoto()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(holder.imgqltaikkhoan);
        holder.btnOptiontaikhoan.setVisibility(View.VISIBLE);
        holder.btnOptiontaikhoan.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.btnOptiontaikhoan);
            popupMenu.inflate(R.menu.menu_taikhoan);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.delete: {
                            deletesp(arrayListtaikhoan.get(position).getId(),position);
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
        StringRequest request = new StringRequest(Request.Method.POST, Server.DeleteUser, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    arrayListtaikhoan.remove(position);
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
        return arrayListtaikhoan.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView txtqltentaikhoan,txtqlemailtaikhoan,txtqlsdttaikhoan,txtqldiachitaikhoan;
        public ImageView imgqltaikkhoan;
        public ImageButton btnOptiontaikhoan;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtqltentaikhoan = (TextView) itemView.findViewById(R.id.txtqltentaikhoan);
            txtqlemailtaikhoan = (TextView) itemView.findViewById(R.id.txtqlemailtaikhoan);
            txtqlsdttaikhoan = (TextView) itemView.findViewById(R.id.txtqlsdttaikhoan);
            txtqldiachitaikhoan = (TextView) itemView.findViewById(R.id.txtqldiachitaikhoan);
            imgqltaikkhoan = (ImageView) itemView.findViewById(R.id.imgqltaikhoan);
            btnOptiontaikhoan = (ImageButton) itemView.findViewById(R.id.btnOptiontaikhoan);
        }
    }
}