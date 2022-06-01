package com.qtthai.app_ban_hang.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.qtthai.app_ban_hang.adapter.DienthoaiAdapter;
import com.qtthai.app_ban_hang.adapter.LoaispAdapter;
import com.qtthai.app_ban_hang.adapter.QLLoaispAdapter;
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

public class QL_loaisp extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rdt;
    QLLoaispAdapter qlLoaispAdapter;
    ArrayList<Loaisp> mangloaisp;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_loaisp);
        Anhxa();
        Actiontoolbar();
        getdulieuloaisp();
    }


    private void getdulieuloaisp() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.loaisp, response -> {
            if (response != null){
                int id = 0;
                String tenloaisp = "";
                String hinhanhloaisp = "";
                for (int i=0; i<response.length(); i++){

                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        tenloaisp = jsonObject.getString("tenloaisanpham");
                        hinhanhloaisp = Server.img+"storage/loaisanpham/"+jsonObject.getString("hinhanhloaisanpham");
                        mangloaisp.add(new Loaisp(id,tenloaisp,hinhanhloaisp));
                        qlLoaispAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        requestQueue.add(jsonArrayRequest);
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
        rdt = (RecyclerView) findViewById(R.id.recyclerviewqlloaisp);
        mangloaisp = new ArrayList<>();
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        qlLoaispAdapter = new QLLoaispAdapter(getApplicationContext(),mangloaisp);
        rdt.setHasFixedSize(true);
        rdt.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rdt.setAdapter(qlLoaispAdapter);

    }
}
