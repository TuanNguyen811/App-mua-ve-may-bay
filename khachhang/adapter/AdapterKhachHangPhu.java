package com.example.khachhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khachhang.MucActivity.Activity_ThanhToan;
import com.example.khachhang.R;
import com.example.khachhang.doiTuong.KhachHang;
import com.example.khachhang.doiTuong.KhachHangPhu;

import java.util.ArrayList;

public class AdapterKhachHangPhu extends BaseAdapter {
    private Activity_ThanhToan context;
    private int layout;
    private ArrayList<KhachHangPhu> danhSachKhachHangPhu;

    public AdapterKhachHangPhu(Activity_ThanhToan context, int layout, ArrayList<KhachHangPhu> danhSachKhachHangPhu) {
        this.context = context;
        this.layout = layout;
        this.danhSachKhachHangPhu = danhSachKhachHangPhu;
    }

    @Override
    public int getCount() {
        return danhSachKhachHangPhu.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);
        TextView txt_ten=(TextView) convertView.findViewById(R.id.txt_itemkhachhangphu_ten);
        TextView txt_sdt=(TextView) convertView.findViewById(R.id.txt_itemkhachhangphu_sdt);
        ImageView bt_edit=(ImageView)convertView.findViewById(R.id.imbt_itemkhachhangphu_edt);

        KhachHangPhu khachHangPhu=danhSachKhachHangPhu.get(position);
        txt_sdt.setText("0"+khachHangPhu.getSdt());
        txt_ten.setText(khachHangPhu.getTen());
        return convertView;
    }
}
