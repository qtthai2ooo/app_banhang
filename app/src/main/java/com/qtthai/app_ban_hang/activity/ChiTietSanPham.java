package com.qtthai.app_ban_hang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.model.Giohang;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import static com.qtthai.app_ban_hang.R.id.toolbarchitietsp;

public class ChiTietSanPham extends AppCompatActivity {
    private BottomNavigationView navigationView;
    Toolbar toolbarchitietsp;
    ImageView imgchitietsp;
    TextView txtten, txtgia, txtmota;
    Spinner spinner;
    Button buttondatmua;
    int Id = 0;
    String tenchitietsp = "";
    String imgsp = "";
    Integer giachitietsp = 0;
    String motachitietsp = "";
    int idsp = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        Anhxa();
        ActionToolbar();
        getthongtinsp();
        Eventspiner();
        EventButton();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void EventButton() {
        buttondatmua.setOnClickListener(v -> {
         if (Trangchu.manggiohang.size() > 0){
             int sl = Integer.parseInt(spinner.getSelectedItem().toString());
             boolean exists = false;
             for (int i=0; i<Trangchu.manggiohang.size(); i++){
                 if (Trangchu.manggiohang.get(i).getIdsp() == Id){
                     Trangchu.manggiohang.get(i).setSoluongsp(Trangchu.manggiohang.get(i).getSoluongsp() + sl);
                     if (Trangchu.manggiohang.get(i).getSoluongsp() >=10 ){
                         Trangchu.manggiohang.get(i).setSoluongsp(10);
                     }
                     Trangchu.manggiohang.get(i).setGiasp(giachitietsp*Trangchu.manggiohang.get(i).getSoluongsp());
                     exists = true;
                 }
             }
             if (exists == false){
                 int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                 long Giamoi = soluong*giachitietsp;
                 Trangchu.manggiohang.add(new Giohang(Id,tenchitietsp,Giamoi,imgsp,soluong));
             }
         }else {
             int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
             long Giamoi = soluong*giachitietsp;
             Trangchu.manggiohang.add(new Giohang(Id,tenchitietsp,Giamoi,imgsp,soluong));
         }
            Intent intent = new Intent(getApplicationContext(), com.qtthai.app_ban_hang.activity.Giohang.class);
            startActivity(intent);
        });
    }

    private void Eventspiner() {
        Integer[] soluong = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,soluong);
        spinner.setAdapter(arrayAdapter);
    }

    private void getthongtinsp() {

        Sanpham sanpham = (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");
        Id = sanpham.getId();
        tenchitietsp = sanpham.getTensanpham();
        imgsp = sanpham.getHinhanhsanpham();
        giachitietsp = sanpham.getGiasanpham();
        motachitietsp = sanpham.getMotasanpham();
        idsp = sanpham.getIdsanpham();
        txtten.setText(tenchitietsp);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtgia.setText("Giá : " +decimalFormat.format(giachitietsp)+" VNĐ");
        txtmota.setText(motachitietsp);
        Picasso.get().load(imgsp)
                .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgchitietsp);

    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarchitietsp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarchitietsp.setNavigationOnClickListener(v -> {
           finish();
        });
    }

    private void Anhxa() {
        toolbarchitietsp = (Toolbar) findViewById(R.id.toolbarchitietsp);
        imgchitietsp = (ImageView) findViewById(R.id.imgchitietsanpham);
        txtten = (TextView) findViewById(R.id.textviewtenspchitiet);
        txtgia = (TextView) findViewById(R.id.textviewgiaspchitiet);
        txtmota = (TextView) findViewById(R.id.textviewmotaspchitiet);
        spinner = (Spinner) findViewById(R.id.spinner);
        buttondatmua = (Button) findViewById(R.id.buttonthemhang);
        Sanpham sanpham = (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");
        Id = sanpham.getId();
        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_home: {
                        Intent intent = new Intent(ChiTietSanPham.this,Trangchu.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.item_account: {
                        Intent intent = new Intent(ChiTietSanPham.this,TaikhoanActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menugiohang: {
                        Intent intent = new Intent(getApplicationContext(), com.qtthai.app_ban_hang.activity.Giohang.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.item_comment: {
                        Intent intent = new Intent(ChiTietSanPham.this, CommentActivity.class);
                        intent.putExtra("sanpham_id",Id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    }
                }

                return  true;
            }
        });
    }
}
