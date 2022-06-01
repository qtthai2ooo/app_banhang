package com.qtthai.app_ban_hang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.adapter.TaikhoanAdapter;
import com.qtthai.app_ban_hang.model.Lichsu;
import com.qtthai.app_ban_hang.model.Post;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.model.Taikhoan;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

public class TaikhoanActivity extends AppCompatActivity {
    private View view;
    private BottomNavigationView navigationView;
    private Toolbar toolbar;
    private CircleImageView imgProfile;
    private TextView txtName,txtdiachi,txtdienthoai,txtemailtaikhoan,txtsodonhang;
    private Button btnEditAccount;
    private RecyclerView recyclerView;
    private ArrayList<Lichsu> arrayList;
    private ArrayList<Taikhoan> artk;
    private SharedPreferences preferences;
    private TaikhoanAdapter adapter;
    private String imgUrl = "";
    private String hoten = "";
    private String sodt = "";
    private String diachi = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taikhoan);
        init();
        getData();
    }


    private void init() {
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbarAccount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        imgProfile = (CircleImageView) findViewById(R.id.imgAccountProfile);
        txtName = (TextView) findViewById(R.id.txtAccountName);
        txtdiachi = (TextView) findViewById(R.id.diachitaikhoan);
        txtsodonhang = (TextView) findViewById(R.id.donhangtaikhoan) ;
        txtdienthoai = (TextView) findViewById(R.id.dienthoaitaikkhoan);
        txtemailtaikhoan = (TextView) findViewById(R.id.emailtaikhoan);
        recyclerView = findViewById(R.id.recyclerAccount);
        btnEditAccount = findViewById(R.id.btnEditAccount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        btnEditAccount.setOnClickListener(v->{
            Intent i = new Intent(TaikhoanActivity.this, UserInfoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("imgUrl",imgUrl);
            i.putExtra("hoten",hoten);
            i.putExtra("sodt",sodt);
            i.putExtra("diachi",diachi);
            startActivity(i);
        });

        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setSelectedItemId(R.id.item_account);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_home: {
                        Intent intent = new Intent(TaikhoanActivity.this,Trangchu.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.item_account: {
                        break;
                    }
                    case R.id.menugiohang:
                        Intent intent = new Intent(getApplicationContext(), com.qtthai.app_ban_hang.activity.Giohang.class);
                        startActivity(intent);
                }

                return  true;
            }
        });
    }

    private void getData() {
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, Server.MY_POST, res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    JSONArray ls = object.getJSONArray("chitietdonhang");
                    for (int i = 0; i < ls.length(); i++) {
                        txtsodonhang.setText(i+1+" Đơn hàng");
                        JSONObject p = ls.getJSONObject(i);
                        JSONObject sp = p.getJSONObject("sanpham");
                        Lichsu lichsu  = new Lichsu();
                        lichsu.setHinhanhsanpham(Server.img+"storage/sanpham/"+sp.getString("hinhanhsanpham"));
                        lichsu.setTensanpham(sp.getString("tensanpham"));
                        lichsu.setSoluongsanpham(p.getInt("soluongsanpham"));

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


                        lichsu.setDate("Ngày đặt : "+formattedDate);
                        arrayList.add(lichsu);

                    }
                    JSONObject user = object.getJSONObject("user");
                    txtName.setText(user.getString("name"));
                    hoten = user.getString("name");
                    txtdiachi.setText(user.getString("diachi"));
                    diachi = user.getString("diachi");
                    txtdienthoai.setText(user.getString("dienthoai"));
                    sodt = user.getString("dienthoai");
                    txtemailtaikhoan.setText(user.getString("email"));
/*                    txtPostsCount.setText(arrayList.size()+"");*/
                    Picasso.get().load(Server.img+"storage/profiles/"+user.getString("photo")).into(imgProfile);
                    adapter = new TaikhoanAdapter(getApplicationContext(),arrayList);
                    recyclerView.setAdapter(adapter);
                    imgUrl = Server.img+"storage/profiles/"+user.getString("photo");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {
            error.printStackTrace();
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_logout:
                CheckConnection.ShowToast_Short(getApplicationContext(),"hayday");
                {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Bạn muốn thoát?");
                builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        StringRequest request = new StringRequest(Request.Method.GET,Server.LOGOUT,res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(TaikhoanActivity.this,AuthActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        },error -> {
            error.printStackTrace();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


}
