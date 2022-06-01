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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.model.Loaisp;
import com.qtthai.app_ban_hang.ultil.CheckConnection;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class thongtin_sp extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    private int position =0, idsp= 0;
    private Spinner spinner;
    private ArrayList<String> mangsanpham;
    ArrayList<Loaisp> mangloaisp;
    private EditText txt_qlmotasp, motasp;
    private int idloaisp;
    private TextInputLayout layouttensp,layoutgiasp;
    private TextInputEditText txttensp,txtgiasp;
    private Button btn_saveqlsp;
    private static final int GALLERY_CHANGE_IMG = 0;
    private ImageView imgsanpham;
    private Bitmap bitmap = null;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin_sp);
        anhxa();
    }


    private void anhxa() {
        sharedPreferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        mangsanpham = new ArrayList<String>();
        mangloaisp = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinner_loaisp);
        spinner.setOnItemSelectedListener(this);
        getData();
        imgsanpham = findViewById(R.id.img_qlsp);
        txt_qlmotasp = findViewById(R.id.txt_qlmotasp);
        layouttensp = findViewById(R.id.txtLayouttensp);
        layoutgiasp = findViewById(R.id.txtLayoutgiasp);
        txttensp = findViewById(R.id.txttensp);
        txtgiasp = findViewById(R.id.txtgiasp);
        btn_saveqlsp = findViewById(R.id.btn_saveqlsp);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        idsp = getIntent().getIntExtra("idsp",0);
        if (idsp != 0){
            txttensp.setText(getIntent().getStringExtra("tensp"));
            txtgiasp.setText(getIntent().getStringExtra("giasp"));
            txt_qlmotasp.setText(getIntent().getStringExtra("motasp"));
            String img = getIntent().getStringExtra("hinhsp");
            Picasso.get().load(img).placeholder(R.drawable.noimage)
                    .error(R.drawable.error).into(imgsanpham);
            btn_saveqlsp.setOnClickListener(v->{
                if (validate()){
                    update();
                }
            });
        }else {
            btn_saveqlsp.setOnClickListener(v -> {
                if (validate()) {
                    save();
                }
            });
        }
    }

    private void update() {
        dialog.setMessage("Đang lưu");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Server.suasanpham, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    startActivity(new Intent(thongtin_sp.this, QL_sanpham.class));
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
                String token = sharedPreferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",idsp+"");
                map.put("idsanpham",idloaisp+"");
                map.put("tensanpham",txttensp.getText().toString().trim());
                map.put("giasanpham",txtgiasp.getText().toString().trim());
                map.put("motasanpham",txt_qlmotasp.getText().toString().trim());
                map.put("hinhanhsanpham",bitmapToString(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(thongtin_sp.this);
        queue.add(request);

    }

    private void getData() {
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
                        mangsanpham.add(tenloaisp);
                        mangloaisp.add(new Loaisp(id,tenloaisp,hinhanhloaisp));
                        spinner.setAdapter(new ArrayAdapter<String>(thongtin_sp.this, android.R.layout.simple_spinner_dropdown_item, mangsanpham));
                        if (idsp != 0){
                            int pos = Integer.parseInt(getIntent().getStringExtra("idloaisp"));
                            if (id == pos){
                                spinner.setSelection(i);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, error -> CheckConnection.ShowToast_Short(getApplicationContext(),error.toString())){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    private void save() {
        dialog.setMessage("Đang lưu");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Server.themsanpham, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    startActivity(new Intent(thongtin_sp.this, QL_sanpham.class));
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
                String token = sharedPreferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("idsanpham",idloaisp+"");
                map.put("tensanpham",txttensp.getText().toString().trim());
                map.put("giasanpham",txtgiasp.getText().toString().trim());
                map.put("motasanpham",txt_qlmotasp.getText().toString().trim());
                map.put("hinhanhsanpham",bitmapToString(bitmap));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(thongtin_sp.this);
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


    private boolean validate() {
        if (txttensp.getText().toString().isEmpty()){
            layouttensp.setErrorEnabled(true);
            layouttensp.setError("chưa nhập tên sản phẩm");
            return false;
        }
        if (txtgiasp.getText().toString().isEmpty()){
            layoutgiasp.setErrorEnabled(true);
            layoutgiasp.setError("chưa nhập giá sản phẩm");
            return false;
        }
        if (txt_qlmotasp.getText().toString().isEmpty()){
            Toast.makeText(this, "Chưa nhập mô tả sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        idloaisp = mangloaisp.get(position).getId();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
