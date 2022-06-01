package com.qtthai.app_ban_hang.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.adapter.LoaispAdapter;
import com.qtthai.app_ban_hang.adapter.SanphamAdapter;
import com.qtthai.app_ban_hang.adapter.TaikhoanAdapter;
import com.qtthai.app_ban_hang.adapter.dongLoaispAdapter;
import com.qtthai.app_ban_hang.model.Giohang;
import com.qtthai.app_ban_hang.model.Lichsu;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Trangchu extends AppCompatActivity {
    private SharedPreferences userPref;
    private BottomNavigationView navigationView;
    CircleImageView imguser;
/*    TextView textviewtenuser;*/
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    RecyclerView recyclerViewloaisp;
    ArrayList<Loaisp> mangloaisp;
    LoaispAdapter loaispAdapter;
    dongLoaispAdapter dongLoaispAdapter;
    int id = 0;
    int solsp = 2;
    String tenloaisp = "";
    String hinhanhloaisp = "";
    ArrayList<Sanpham> mangsp;

    SanphamAdapter sanphamAdapter;
    public static ArrayList<Giohang> manggiohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        anhxa();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            getuser();
            actionbar();
            quangcao();
            getdulieuloaisp();
            getdulieuspmoi();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(),"kiểm tra kết nối");
            finish();

        }
    }

    private void getuser() {
        StringRequest request = new StringRequest(Request.Method.GET, Server.MY_POST, res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                     JSONObject user = object.getJSONObject("user");
/*                    textviewtenuser.setText(user.getString("name"));

                    *//*                    txtPostsCount.setText(arrayList.size()+"");*/
                    Picasso.get().load(Server.img+"storage/profiles/"+user.getString("photo")).into(imguser);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {
            error.printStackTrace();
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
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
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(Trangchu.this,DienThoaiActivity.class);
                String tim = String.valueOf(query);
                intent.putExtra("timkiem",tim);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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


    private void getdulieuspmoi() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.spmoi, response -> {
            if (response != null){
                int ID = 0;
                String tensp = "";
                String imgsp = "";
                Integer giasp = 0;
                String motasp = "";
                int idsp = 0;
                for (int i=0; i<response.length(); i++){
                    CheckConnection.ShowToast_Short(getApplicationContext(),"hayday");
                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        ID = jsonObject.getInt("id");
                        tensp = jsonObject.getString("tensanpham");
                        giasp = jsonObject.getInt("giasanpham");
                        imgsp = Server.img+"storage/sanpham/"+jsonObject.getString("hinhanhsanpham");
                        motasp = jsonObject.getString("motasanpham");
                        idsp = jsonObject.getInt("idsanpham");
                        mangsp.add(new Sanpham(ID,tensp,giasp,imgsp,motasp,idsp));
                        sanphamAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, error -> CheckConnection.ShowToast_Short(getApplicationContext(),error.toString())){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    private void getdulieuloaisp() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.loaisp, response -> {
            if (response != null){
                for (int i=0; i<response.length(); i++){

                    try {

                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        tenloaisp = jsonObject.getString("tenloaisanpham");
                        hinhanhloaisp = Server.img+"storage/loaisanpham/"+jsonObject.getString("hinhanhloaisanpham");
                        mangloaisp.add(new Loaisp(id,tenloaisp,hinhanhloaisp));
                        dongLoaispAdapter.notifyDataSetChanged();
                        loaispAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, error -> CheckConnection.ShowToast_Short(getApplicationContext(),error.toString())){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    private void quangcao() {
        ArrayList<String> qc = new ArrayList<>();
        qc.add("https://cdn.tgdd.vn/Products/Images/44/198790/Feature/198790-FT-spec-720x333.jpg");
        qc.add("https://beeseo.vn/wp-content/uploads/2018/02/thiet-ke-ban-do-dien-tu.jpg");
        qc.add("https://beeseo.vn/wp-content/uploads/2018/02/kinh-doanh-my-pham.jpg");
        for (int i=0; i<qc.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(qc.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);

        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    private void actionbar() {
        setSupportActionBar(toolbar);


    }

    private void anhxa() {
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        imguser = (CircleImageView) findViewById(R.id.imageviewanhuser);
/*        textviewtenuser = (TextView) findViewById(R.id.textviewtenuser);*/
        toolbar = (Toolbar) findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewlipple);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerViewloaisp = (RecyclerView) findViewById(R.id.recyclerviewloaisp);

        mangloaisp = new ArrayList<>();
       loaispAdapter = new LoaispAdapter(mangloaisp ,getApplicationContext());

        dongLoaispAdapter = new dongLoaispAdapter(mangloaisp ,getApplicationContext());
        recyclerViewloaisp.setHasFixedSize(true);
        recyclerViewloaisp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewloaisp.setAdapter(dongLoaispAdapter);


        mangsp = new ArrayList<>();
        sanphamAdapter = new SanphamAdapter(getApplicationContext(),mangsp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(sanphamAdapter);
        if (manggiohang != null ){

        }else {
            manggiohang = new ArrayList<>();
        }
        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_home: {
                          break;
                    }
                    case R.id.item_account: {
                            Intent intent = new Intent(Trangchu.this,TaikhoanActivity.class);
                            startActivity(intent);
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
}
