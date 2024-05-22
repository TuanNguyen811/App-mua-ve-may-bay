package com.example.appmaybay1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaybay1.MucActivity.Activity_mayBay;
import com.example.appmaybay1.R;
import com.example.appmaybay1.doiTuong.MayBay;

import java.util.ArrayList;

public class AdapterMayBay extends BaseAdapter {
    private Activity_mayBay context;
    private int layout;
    ArrayList<MayBay> danhSachMayBay;

    public AdapterMayBay(Activity_mayBay context, int layout, ArrayList<MayBay> danhSachMayBay) {
        this.context = context;
        this.layout = layout;
        this.danhSachMayBay = danhSachMayBay;
    }

    @Override
    public int getCount() {
        return danhSachMayBay.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolderMayBay{
        TextView txt_tenMayBay,txt_soGheMayaBay;
        ImageView im_edit;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderMayBay holder;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);
            holder=new ViewHolderMayBay();
            holder.txt_tenMayBay=(TextView) convertView.findViewById(R.id.txt_dongmaybay_tenmaybay);
            holder.txt_soGheMayaBay=(TextView) convertView.findViewById(R.id.txt_dongmaybay_soghecon);
            holder.im_edit=(ImageView) convertView.findViewById(R.id.im_dongmaybay_edit);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolderMayBay) convertView.getTag();
        }
        MayBay mayBay=danhSachMayBay.get(position);
        holder.txt_soGheMayaBay.setText(String.valueOf(mayBay.getSoGhe()));
        holder.txt_tenMayBay.setText(mayBay.getTenMayBay()+" ("+mayBay.getMaMayBay()+")");

        holder.im_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.dialogThongTinMayBay(mayBay,"update");
            }
        });

        return convertView;
    }
}
