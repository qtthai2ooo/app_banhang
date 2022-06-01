package com.qtthai.app_ban_hang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.adapter.GiohangAdapter;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.qtthai.app_ban_hang.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Giohang extends AppCompatActivity {
    private SharedPreferences userPref;
    private BottomNavigationView navigationView;
    ListView lvgiohang;
    TextView txtthongbao;
    static TextView txttongtien;
    Toolbar toolbargiohang;
    Button btnthanhtoan,btntieptucmua;
    GiohangAdapter giohangAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        Anhxa();
        ActionToolbar();
        Checkdata();
        EventUltil();
        Cachonitemlistview();
        EventButton();
    }

    private void EventButton() {
        btntieptucmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Trangchu.class);
                startActivity(intent);
            }
        });
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Trangchu.manggiohang.size()>0){
                   /*     RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.themdonhang, new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String madonhang) {}
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                String token = userPref.getString("token","");
                                HashMap<String,String> map = new HashMap<>();
                                map.put("Authorization","Bearer "+token);
                                return map;
                            }

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> hashMap = new HashMap<String,String>();

                                hashMap.put("sodienthoai","12345678");
                                return hashMap;
                            }
                        };
                        requestQueue.add(stringRequest);
*/




                        RequestQueue requestQueuel = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequestl = new StringRequest(Request.Method.POST, Server.themchitietdonhang, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Trangchu.manggiohang.clear();
                                CheckConnection.ShowToast_Short(getApplicationContext(),"Thành công");
                                Intent intent = new Intent(getApplicationContext(),Trangchu.class);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                String token = userPref.getString("token","");
                                HashMap<String,String> map = new HashMap<>();
                                map.put("Authorization","Bearer "+token);
                                return map;
                            }

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                JSONArray jsonArray = new JSONArray();
                                for (int i=0;i<Trangchu.manggiohang.size();i++){
                                    JSONObject jsonObject = new JSONObject();
                                    try {
/*                                        jsonObject.put("madonhang",i);*/
                                        jsonObject.put("masanpham",Trangchu.manggiohang.get(i).getIdsp());
/*                                        jsonObject.put("tensanpham",Trangchu.manggiohang.get(i).getTensp());
                                        jsonObject.put("giasanpham",Trangchu.manggiohang.get(i).getGiasp());
                                        jsonObject.put("hinhanhsanpham",Trangchu.manggiohang.get(i).getHinhsp());*/
                                        jsonObject.put("soluongsanpham",Trangchu.manggiohang.get(i).getSoluongsp());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    jsonArray.put(jsonObject);

                                }

                                HashMap<String,String> hashMap = new HashMap<String, String>();
                                hashMap.put("json",jsonArray.toString());

                                return hashMap;
                            }
                        };
                        requestQueuel.add(stringRequestl);

                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(),"chưa có sản phẩm thanh toán");
                }
            }
        });
    }

    private void Cachonitemlistview() {
        lvgiohang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Giohang.this);
                builder.setTitle("Xác nhận xoá sản phẩm");
                builder.setMessage("Xoá sản phẩm");
                builder.setPositiveButton("có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Trangchu.manggiohang.size()<=0){
                            txtthongbao.setVisibility(View.VISIBLE);
                        }else {
                            Trangchu.manggiohang.remove(position);
                            giohangAdapter.notifyDataSetChanged();
                            EventUltil();
                            if (Trangchu.manggiohang.size()<=0){
                                txtthongbao.setVisibility(View.VISIBLE);
                            }else {
                                txtthongbao.setVisibility(View.INVISIBLE);
                                giohangAdapter.notifyDataSetChanged();
                                EventUltil();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        giohangAdapter.notifyDataSetChanged();
                        EventUltil();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public static void EventUltil() {
        long tongtien = 0;
        for (int i=0;i<Trangchu.manggiohang.size();i++){
            tongtien += Trangchu.manggiohang.get(i).getGiasp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txttongtien.setText(decimalFormat.format(tongtien)+" VNĐ");
    }

    private void Checkdata() {
        if (Trangchu.manggiohang.size() <= 0){
            giohangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.VISIBLE);
            lvgiohang.setVisibility(View.INVISIBLE);
        }else {
            giohangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.INVISIBLE);
            lvgiohang.setVisibility(View.VISIBLE);
        }
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbargiohang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbargiohang.setNavigationOnClickListener(v -> finish());
    }

    private void Anhxa() {
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        lvgiohang = (ListView) findViewById(R.id.listviewgiohang);
        txtthongbao = (TextView) findViewById(R.id.textviewthongbao);
        txttongtien = (TextView) findViewById(R.id.textviewtongtien);
        toolbargiohang = (Toolbar) findViewById(R.id.toolbargiohang);
        btnthanhtoan = (Button) findViewById(R.id.buttonthanhtoangiohang);
        btntieptucmua = (Button) findViewById(R.id.buttontieptucmuahang);
        giohangAdapter = new GiohangAdapter(Giohang.this,Trangchu.manggiohang);
        lvgiohang.setAdapter(giohangAdapter);
        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setSelectedItemId(R.id.menugiohang);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_home: {
                        Intent intent = new Intent(Giohang.this,Trangchu.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.item_account: {
                        Intent intent = new Intent(Giohang.this,TaikhoanActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menugiohang:
                }

                return  true;
            }
        });
        
    }
}
