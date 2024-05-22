package com.example.appmaybay1.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaybay1.MucActivity.Activity_diaChi;
import com.example.appmaybay1.R;
import com.example.appmaybay1.doiTuong.DiaChi;

import java.util.List;

public class AdapterDiaChi extends BaseAdapter {
    private Activity_diaChi context;
    private int layout;
    private List<DiaChi> danhSachDiaChi;
    public AdapterDiaChi(Activity_diaChi context, int layout, List<DiaChi> danhSachDiaChi) {
        this.context = context;
        this.layout = layout;
        this.danhSachDiaChi = danhSachDiaChi;
    }
    @Override
    public int getCount() {
        return danhSachDiaChi.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolderDiaChi{
        TextView txt_ten,txt_tenSanBay;
        ImageView im_edit;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderDiaChi holder;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);
            holder=new ViewHolderDiaChi();
            holder.txt_ten=(TextView) convertView.findViewById(R.id.txt_dongdiachi_ten);
            holder.txt_tenSanBay=(TextView) convertView.findViewById(R.id.txt_dongdiachi_tensanbay);
            holder.im_edit=(ImageView)convertView.findViewById(R.id.im_dongdiachi_edit);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolderDiaChi) convertView.getTag();
        }
        DiaChi diaChi=danhSachDiaChi.get(position);
        holder.txt_ten.setText(diaChi.getTenDiaChi());
        holder.txt_tenSanBay.setText(diaChi.getMaDiaChi() +" - " +diaChi.getTenSanBay());
        holder.im_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.dialogThongTinDiaChi(diaChi,"update");
            }
        });
        return convertView;
    }

}
