package com.qtthai.app_ban_hang.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.activity.DienThoaiActivity;
import com.qtthai.app_ban_hang.adapter.DienthoaiAdapter;
import com.qtthai.app_ban_hang.adapter.LoaispAdapter;
import com.qtthai.app_ban_hang.adapter.QLSanphamAdapter;
import com.qtthai.app_ban_hang.adapter.SanphamAdapter;
import com.qtthai.app_ban_hang.model.Loaisp;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.qtthai.app_ban_hang.R;

public class QL_sanpham extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rdt;
    QLSanphamAdapter qlSanphamAdapter;
    ArrayList<Sanpham> mangdt;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_sanpham);
        Anhxa();
        Actiontoolbar();
        getdata();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.qtthai.app_ban_hang.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getdata() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,Server.qlsanpham, response -> {
            if (response != null){
                int ID = 0;
                String tensp = "";
                String imgsp = "";
                Integer giasp = 0;
                String motasp = "";
                int idsp = 0;

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ID = jsonObject.getInt("id");
                        tensp = jsonObject.getString("tensanpham");
                        giasp = jsonObject.getInt("giasanpham");
                        imgsp = Server.img+"storage/sanpham/"+jsonObject.getString("hinhanhsanpham");
                        motasp = jsonObject.getString("motasanpham");
                        idsp = jsonObject.getInt("idsanpham");
                        mangdt.add(new Sanpham(ID,tensp,giasp,imgsp,motasp,idsp));
                        qlSanphamAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        toolbar = (Toolbar) findViewById(R.id.toolbardienthoai);
        rdt = (RecyclerView) findViewById(R.id.recyclerviewdienthoai);
        mangdt = new ArrayList<>();
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        qlSanphamAdapter = new QLSanphamAdapter(getApplicationContext(),mangdt);
        rdt.setHasFixedSize(true);
        rdt.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rdt.setAdapter(qlSanphamAdapter);

    }
}
