package com.qtthai.app_ban_hang.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.activity.Trangchu;
import com.qtthai.app_ban_hang.activity.UserInfoActivity;
import com.qtthai.app_ban_hang.model.Loaisp;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class thongtin_loaisp extends AppCompatActivity {

    private int position =0, id= 0;
    private EditText txt_qltenloaisp;
    private Button btn_saveqlloaisp;
    private ImageView imgsanpham;
    private Bitmap bitmap = null;
    private static final int GALLERY_CHANGE_IMG = 3;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin_loaisp);
        anhxa();
    }

    private void anhxa() {
        sharedPreferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        imgsanpham = findViewById(R.id.img_qlloaisp);
        txt_qltenloaisp = findViewById(R.id.txt_qltenloaisp);
        btn_saveqlloaisp = findViewById(R.id.btn_saveqlloaisp);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        id = getIntent().getIntExtra("idloaisp",0);
        position = getIntent().getIntExtra("position",0);

        if(id != 0){
            txt_qltenloaisp.setText(getIntent().getStringExtra("tenloaisp"));

            String img = getIntent().getStringExtra("hinhloaisp");
            Picasso.get().load(img).placeholder(R.drawable.noimage)
                    .error(R.drawable.error).into(imgsanpham);
            btn_saveqlloaisp.setOnClickListener(v->{
                if (!txt_qltenloaisp.getText().toString().isEmpty()){
                    update();
                }else {
                    Toast.makeText(this, "Chưa nhập tên loại sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });

        } else{

            btn_saveqlloaisp.setOnClickListener(v->{
                if (!txt_qltenloaisp.getText().toString().isEmpty()){
                    save();
                }else {
                    Toast.makeText(this, "Chưa nhập tên loại sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });

        }

        imgsanpham.setImageURI(getIntent().getData());
/*
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),getIntent().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }

    private void update() {
        dialog.setMessage("Đang lưu");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,Server.sualoaisanpham,response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(thongtin_loaisp.this, QL_loaisp.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        },error -> {
            error.printStackTrace();
            dialog.dismiss();
        }){

            // add token to header


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            // add params

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id+"");
                map.put("tenloaisanpham",txt_qltenloaisp.getText().toString().trim());
                map.put("hinhanhloaisanpham",bitmapToString(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(thongtin_loaisp.this);
        queue.add(request);

    }

    private void save() {
        dialog.setMessage("Đang lưu");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,Server.themloaisanpham,response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(thongtin_loaisp.this, QL_loaisp.class));
                    finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        },error -> {
            error.printStackTrace();
            dialog.dismiss();
        }){

            // add token to header


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            // add params

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("tenloaisanpham",txt_qltenloaisp.getText().toString().trim());
                map.put("hinhanhloaisanpham",bitmapToString(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(thongtin_loaisp.this);
        queue.add(request);

    }



    private String bitmapToString(Bitmap bitmap) {
        if (bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array,Base64.DEFAULT);
        }

        return "";
    }


    public void cancel(View view) {
        super.onBackPressed();
    }

    public void changePhoto(View view) {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,GALLERY_CHANGE_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CHANGE_IMG && resultCode==RESULT_OK){
            Uri imgUri = data.getData();
            imgsanpham.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
