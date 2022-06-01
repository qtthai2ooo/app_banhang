package com.qtthai.app_ban_hang.activity;

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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    private TextInputLayout layoutName,layoutdienthoai, layoutdiachi;
    private TextInputEditText txtName,txtdiachi,txtdienthoai;
    private TextView txtSelectPhoto;
    private Button btnContinue;
    private CircleImageView circleImageView;
    private  static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        layoutdiachi = findViewById(R.id.txtLayoutdiachi);
        layoutdienthoai = findViewById(R.id.txtLayoutdienthoai);
        layoutName = findViewById(R.id.txtLayoutNameUserInfo);
        txtName = findViewById(R.id.txtNameUserInfo);
        txtdiachi = findViewById(R.id.txtdiachi);
        txtdienthoai = findViewById(R.id.txtdienthoai);
        txtSelectPhoto = findViewById(R.id.txtSelectPhoto);
        btnContinue = findViewById(R.id.btnContinue);
        circleImageView = findViewById(R.id.imgUserInfo);

        Intent intent = getIntent();
        String img = intent.getStringExtra("imgUrl");
        String hoten = intent.getStringExtra("hoten");
        String sodt = intent.getStringExtra("sodt");
        String diachi = intent.getStringExtra("diachi");
        Picasso.get().load(img).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(circleImageView);
        txtName.setText(hoten);
        txtdiachi.setText(diachi);
        txtdienthoai.setText(sodt);

        txtSelectPhoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i,GALLERY_ADD_PROFILE);
        });

        btnContinue.setOnClickListener(v -> {
            if (validate()){
                saveUserInfo();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ADD_PROFILE && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            circleImageView.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate() {
        if (txtName.getText().toString().isEmpty()){
            layoutName.setErrorEnabled(true);
            layoutName.setError("chưa nhập tên");
            return false;
        }
        if (txtdiachi.getText().toString().isEmpty()){
            layoutdiachi.setErrorEnabled(true);
            layoutdiachi.setError("chưa nhập địa chỉ");
            return false;
        }
        if (txtdienthoai.getText().toString().isEmpty()){
            layoutdienthoai.setErrorEnabled(true);
            layoutdienthoai.setError("chưa nhập số điện thoại");
            return false;
        }
        return true;
    }


    private void saveUserInfo() {
        dialog.setMessage("Saving");
        dialog.show();
        String name = txtName.getText().toString().trim();
        String diachi = txtdiachi.getText().toString().trim();
        String dienthoai = txtdienthoai.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, Server.SAVE_USER_INFO, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("photo",object.getString("photo"));
                    editor.apply();
                    startActivity(new Intent(UserInfoActivity.this,Trangchu.class));
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token", "");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("name",name);
                map.put("lastname","test");
                map.put("diachi",diachi);
                map.put("dienthoai",dienthoai);
                map.put("photo",bitmapToString(bitmap));
                return map;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(UserInfoActivity.this);
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
}
