package com.qtthai.app_ban_hang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.qtthai.app_ban_hang.R;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    private int images[] = {
            R.drawable.happy_shop,
            R.drawable.all,
            R.drawable.triet_hoc,
    };

    private String titles[] ={
            "Chào mừng",
            "Tận hưởng",
            "Bản quyền "
    };
    private String descs[] ={
            "Tới App bán hàng Happyshop",
            "Việc mua hàng nhanh chóng, tiện lợi ngay tại nhà",
            "Quách Trọng Thái /n Lớp: 18IT1 /n MSV: 18IT035"
    };
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_pager, container, false);
        ImageView imageView =v.findViewById(R.id.imgViewPager);
        TextView txtTitle =v.findViewById(R.id.txtTitleViewPager);
        TextView txtDesc =v.findViewById(R.id.txtDescViewPager);

        imageView.setImageResource(images[position]);
        txtTitle.setText(titles[position]);
        txtDesc.setText(descs[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
