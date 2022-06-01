package com.qtthai.app_ban_hang.Admin;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QL_taikhoan extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rdt;
    QLTaikhoanAdapter qlTaikhoanAdapter;
    ArrayList<Taikhoan> mangtk;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_taikhoan);
        Anhxa();
        Actiontoolbar();
        getdata();
    }

    private void getdata() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.Getuser, response -> {
            if (response != null){
                int ID = 0;
                String ten = "";
                String diachi = "";
                String dienthoai = "";
                String img = "";
                String email = "";
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ID = jsonObject.getInt("id");
                        ten = jsonObject.getString("name");
                        diachi = jsonObject.getString("diachi");
                        dienthoai = jsonObject.getString("dienthoai");
                        img = Server.img+"storage/profiles/"+jsonObject.getString("photo");
                        email = jsonObject.getString("email");
                        mangtk.add(new Taikhoan(ID,ten,diachi,dienthoai,img,email));
                        qlTaikhoanAdapter.notifyDataSetChanged();

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
        mangtk = new ArrayList<>();
        qlTaikhoanAdapter = new QLTaikhoanAdapter(getApplicationContext(),mangtk);
        rdt.setHasFixedSize(true);
        rdt.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rdt.setAdapter(qlTaikhoanAdapter);
    }
}
