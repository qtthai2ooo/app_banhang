package com.qtthai.app_ban_hang.Admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.material.navigation.NavigationView;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.activity.DienThoaiActivity;
import com.qtthai.app_ban_hang.activity.Trangchu;
import com.qtthai.app_ban_hang.adapter.LoaispAdapter;
import com.qtthai.app_ban_hang.adapter.SanphamAdapter;
import com.qtthai.app_ban_hang.adapter.TaikhoanAdapter;
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

public class TrangChuAdmin extends AppCompatActivity {
    private SharedPreferences userPref;
    CircleImageView imguser;
    TextView textviewtenuser;
    Toolbar toolbar;

    NavigationView navigationView;
    ListView listView;
    DrawerLayout drawerLayout;
    ArrayList<Loaisp> mangloaisp;
    LoaispAdapter loaispAdapter;
    int id = 0;
    int solsp = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_admin);

        anhxa();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            actionbar();
            chonItemListView();
            getuser();
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
                    textviewtenuser.setText(user.getString("name"));

                    /*                    txtPostsCount.setText(arrayList.size()+"");*/
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

    private void chonItemListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(TrangChuAdmin.this, Trangchu.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShowToast_Short(getApplicationContext(),"không có mạng");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(TrangChuAdmin.this, QL_loaisp.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShowToast_Short(getApplicationContext(),"không có mạng");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(TrangChuAdmin.this, QL_sanpham.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShowToast_Short(getApplicationContext(),"không có mạng");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(TrangChuAdmin.this, QL_taikhoan.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShowToast_Short(getApplicationContext(),"không có mạng");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(TrangChuAdmin.this, QL_donhang.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShowToast_Short(getApplicationContext(),"không có mạng");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

            }
        });
    }



    private void actionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void anhxa() {
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        imguser = (CircleImageView) findViewById(R.id.imageviewanhuser);
        textviewtenuser = (TextView) findViewById(R.id.textviewtenuser);
        toolbar = (Toolbar) findViewById(R.id.toolbarmanhinhchinh);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        listView = (ListView) findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0,new Loaisp(0,"Trang bán hàng", "https://lh3.googleusercontent.com/aXVvjpy_HtUc46-Zs6Lj0061skyAkuu_a4HiN_9Gj48bcYVCx_HnfgL3HWxVcTypYDE"));
        mangloaisp.add(1,new Loaisp(1,"Loại sản phẩm", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQWY0gp8Fiw1u3Q4FaEbKnFDtuuxPV_dXQ75A&usqp=CAU"));
        mangloaisp.add(2,new Loaisp(2,"Sản phảm", "https://www.nicepng.com/png/detail/304-3048415_business-advice-product-icon-png.png"));
        mangloaisp.add(3,new Loaisp(3,"Tài khoản", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/User_icon_2.svg/768px-User_icon_2.svg.png"));
        mangloaisp.add(4,new Loaisp(4,"Đơn hàng", "https://noithatduongphong.com/upload/images/infor3_icon_2_1.png"));
        loaispAdapter = new LoaispAdapter(mangloaisp,getApplicationContext());
        listView.setAdapter(loaispAdapter);
    }
}
