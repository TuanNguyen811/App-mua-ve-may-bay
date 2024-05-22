package com.example.appmaybay1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaybay1.MucActivity.MainActivity_trangchu;
import com.example.appmaybay1.R;
import com.example.appmaybay1.doiTuong.VeMayBay;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterVeMayBay extends BaseAdapter {
    private MainActivity_trangchu context;
    private int layout;
    ArrayList<VeMayBay>danhSachVeMayBay;

    public AdapterVeMayBay(MainActivity_trangchu context, int layout, ArrayList<VeMayBay> danhSachVeMayBay) {
        this.context = context;
        this.layout = layout;
        this.danhSachVeMayBay = danhSachVeMayBay;
    }

    @Override
    public int getCount() {
        return danhSachVeMayBay.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewholderVeMayBay{
        TextView txt_gioDen,txt_gioDi,txt_maTinhDi,txt_maTinhDen,txt_thoiGianbay,txt_ngayBay;
        TextView txt_tenMaybay,txt_giaVe,txt_xemChiTiet;
        ImageView im_anhDaiDien;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewholderVeMayBay holder;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);
            holder=new ViewholderVeMayBay();
            holder.txt_gioDen =(TextView) convertView.findViewById(R.id.txt_dongveMB_gioden);
            holder.txt_gioDi =(TextView) convertView.findViewById(R.id.txt_dongveMB_giodi);
            holder.txt_maTinhDi =(TextView) convertView.findViewById(R.id.txt_dongveMB_matinhdi);
            holder.txt_maTinhDen =(TextView) convertView.findViewById(R.id.txt_dongveMB_matinhden);
            holder.txt_thoiGianbay =(TextView) convertView.findViewById(R.id.txt_dongveMB_thoigiandi);
            holder.txt_ngayBay =(TextView) convertView.findViewById(R.id.txt_dongveMB_ngaydi);
            holder.txt_tenMaybay =(TextView) convertView.findViewById(R.id.txt_dongveMB_tenmaybay);
            holder.txt_giaVe =(TextView) convertView.findViewById(R.id.txt_dongveMB_giave);
            holder.txt_xemChiTiet =(TextView) convertView.findViewById(R.id.txt_dongveMB_xemthem);
            holder.im_anhDaiDien=(ImageView) convertView.findViewById(R.id.im_dongvemaybay_anhnen);
            convertView.setTag(holder);
        }else{
            holder=(ViewholderVeMayBay) convertView.getTag();
        }
        VeMayBay veMayBay=danhSachVeMayBay.get(position);

        // lay thoi gian
        SimpleDateFormat simpleDateFormatGio =new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormatNgay =new SimpleDateFormat("dd/MM/yyyy");
        holder.txt_gioDen.setText(simpleDateFormatGio.format(veMayBay.getThoiGianDen().getTime()));
        holder.txt_gioDi.setText(simpleDateFormatGio.format(veMayBay.getThoiGianDi().getTime()));
        holder.txt_ngayBay.setText(simpleDateFormatNgay.format(veMayBay.getThoiGianDi().getTime()));
        // tinh thoi gian
        float di=veMayBay.getThoiGianDi().getTimeInMillis();
        float den=veMayBay.getThoiGianDen().getTimeInMillis();
        float tongthoigianbay=(den-di)/(60*60*1000);
        //định dạng số
        DecimalFormat df=new DecimalFormat("#.#h");
        String formatNuber=df.format(tongthoigianbay);
        holder.txt_thoiGianbay.setText(formatNuber);
        // cac thuoc tinh con lai
        // mã tỉnh đi đến
        holder.txt_maTinhDi.setText(veMayBay.getTenTinhDi());
        holder.txt_maTinhDen.setText(veMayBay.getTenTinhDen());
        //tên máy
        holder.txt_tenMaybay.setText(veMayBay.getMaMayBay());
        //Định dạng tiền
        float giaved=(float) veMayBay.getGiaVe();
        DecimalFormat dfgiaTien=new DecimalFormat("#,###đ");
        String giave=dfgiaTien.format(giaved);
        holder.txt_giaVe.setText(giave);

        holder.txt_xemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Xem thong tin chi tet", Toast.LENGTH_SHORT).show();

            }
        });

        holder.txt_xemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeMayBay veMayBay1=danhSachVeMayBay.get(position);
                context.thongTinVeMayBay(veMayBay1,"update");
            }
        });
        return convertView;
    }
}
