package com.qtthai.app_ban_hang.adapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qtthai.app_ban_hang.R;
import com.qtthai.app_ban_hang.activity.CommentActivity;
import com.qtthai.app_ban_hang.model.Comment;
import com.qtthai.app_ban_hang.model.Sanpham;
import com.qtthai.app_ban_hang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder>{

    private Context context;
    private ArrayList<Comment> list;
    private SharedPreferences preferences;
    private ProgressDialog dialog;


    public CommentsAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_comment,parent,false);
        return new CommentsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder holder, int position) {
        Comment comment = list.get(position);
        Picasso.get().load(comment.getUser().getPhoto()).into(holder.imgProfile);
        holder.txtName.setText(comment.getUser().getUserName());
        holder.txtDate.setText(comment.getDate());
        holder.txtComment.setText(comment.getComment());

        if (preferences.getInt("id",0)!= 46){
            holder.btnDelete.setVisibility(View.GONE);
        }
        else {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v->{
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Ban có chắc muốn xoá?");
                builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteComment(comment.getId(),position);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            });
        }

    }

    private void deleteComment(int commentId,int position){
        dialog.setMessage("Đang xoá");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Server.DELETE_COMMENT, res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    list.remove(position);
                    notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        },err->{
            err.printStackTrace();
            dialog.dismiss();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",commentId+"");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CommentsHolder extends RecyclerView.ViewHolder{

        private CircleImageView imgProfile;
        private TextView txtName,txtDate,txtComment;
        private ImageButton btnDelete;

        public CommentsHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.imgCommentProfile);
            txtName = itemView.findViewById(R.id.txtCommentName);
            txtDate = itemView.findViewById(R.id.txtCommentDate);
            txtComment = itemView.findViewById(R.id.txtCommentText);
            btnDelete = itemView.findViewById(R.id.btnDeleteComment);
        }
    }
}
