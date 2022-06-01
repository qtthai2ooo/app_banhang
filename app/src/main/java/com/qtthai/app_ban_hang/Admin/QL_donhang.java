package com.qtthai.app_ban_hang.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.adapter.QLDonhangAdapter;
import com.qtthai.app_ban_hang.adapter.QLTaikhoanAdapter;
import com.qtthai.app_ban_hang.adapter.TaikhoanAdapter;
import com.qtthai.app_ban_hang.model.Donhang;
import com.qtthai.app_ban_hang.model.Lichsu;
import com.qtthai.app_ban_hang.model.Taikhoan;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.adapter.DienthoaiAdapter;
import com.qtthai.app_ban_hang.adapter.QLTaikhoanAdapter;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.model.Taikhoan;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import java.util.ArrayList;

public class QL_donhang extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rdt;
    QLDonhangAdapter qlDonhangAdapter;
    ArrayList<Donhang> mangdh;
    ArrayList<Taikhoan> mangtk;
    Taikhoan taikhoan;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_donhang);
        Anhxa();
        Actiontoolbar();
        getdata();
    }

    private void getdata() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.Getdonhang, response -> {
            int ID = 0;
            String ten = "";
            String diachi = "";
            String dienthoai = "";
            String imguser = "";
            String email = "";

            int madonhang = 0;
            int sosp = 0;
            int giasp = 0;
            String imgdh = "";
            String tensp = "";
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONArray ls = object.getJSONArray("chitietdonhang");
                    for (int i = 0; i < ls.length(); i++) {
                        JSONObject p = ls.getJSONObject(i);

                        JSONObject sp = p.getJSONObject("sanpham");
                        JSONObject us = p.getJSONObject("users");

                        madonhang = p.getInt("id");
                        sosp = p.getInt("soluongsanpham");
                        imgdh = Server.img+"storage/sanpham/"+sp.getString("hinhanhsanpham");
                        tensp = sp.getString("tensanpham");
                        giasp = sp.getInt("giasanpham");

                        ten = us.getString("name");
                        diachi = us.getString("diachi");
                        dienthoai = us.getString("dienthoai");
                        email = us.getString("email");
                        imguser = Server.img+"storage/profiles/"+us.getString("photo");

                        DateTimeFormatter inputFormatter = null;
                        LocalDate date = null;
                        DateTimeFormatter outputFormatter = null;
                        String formattedDate = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
                            outputFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY", Locale.ENGLISH);
                            date = LocalDate.parse(p.getString("updated_at"), inputFormatter);
                            formattedDate = outputFormatter.format(date);
                        }
                        mangdh.add(new Donhang(ten,diachi,dienthoai,imguser,email,ID,madonhang,tensp,imgdh,formattedDate,sosp,giasp));
                        qlDonhangAdapter.notifyDataSetChanged();

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> CheckConnection.ShowToast_Short(getApplicationContext(),error.toString())){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
        }
        return super.onOptionsItemSelected(item);
    }

    private void Actiontoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbardienthoai);
        rdt = (RecyclerView) findViewById(R.id.recyclerviewdienthoai);
        mangdh = new ArrayList<>();
        qlDonhangAdapter = new QLDonhangAdapter(getApplicationContext(),mangdh);
        rdt.setHasFixedSize(true);
        rdt.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rdt.setAdapter(qlDonhangAdapter);
    }
}
