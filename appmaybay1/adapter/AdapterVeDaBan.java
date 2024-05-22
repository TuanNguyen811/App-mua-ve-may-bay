package com.example.appmaybay1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmaybay1.R;
import com.example.appmaybay1.doiTuong.VeMayBayDaBan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterVeDaBan extends RecyclerView.Adapter<AdapterVeDaBan.VeDaBanHolder> {
    private ArrayList<VeMayBayDaBan> danhSachVeMayBayDaBan;
    Interface_vemaybaydaban interfaceVemaybaydaban;
    public AdapterVeDaBan(ArrayList<VeMayBayDaBan>veMayBayDaBans, Interface_vemaybaydaban interfaceVemaybaydaban){
        this.danhSachVeMayBayDaBan=veMayBayDaBans;
        this.interfaceVemaybaydaban=interfaceVemaybaydaban;
    }

    @NonNull
    @Override
    public VeDaBanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vemaybaydaban,parent,false);
        return new VeDaBanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VeDaBanHolder holder, int position) {
        VeMayBayDaBan veMayBayDaBan=danhSachVeMayBayDaBan.get(position);
        if(veMayBayDaBan==null){
            return;
        }
        holder.txt_tinhDi.setText(veMayBayDaBan.getTinhDi());
        holder.txt_tinhDen.setText(veMayBayDaBan.getTinhDen());
        holder.txt_soGhe.setText("Vị trí: "+veMayBayDaBan.getSoGhe());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(veMayBayDaBan.getNgayDi().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateString = sdf.format(calendar.getTime());
        holder.txt_thoiGian.setText(dateString);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceVemaybaydaban.layDuLieu(veMayBayDaBan);
            }
        });
    }
    @Override
    public int getItemCount() {
        if(danhSachVeMayBayDaBan!=null){
            return danhSachVeMayBayDaBan.size();
        }
        return 0;
    }

    public class VeDaBanHolder extends RecyclerView.ViewHolder{
        TextView txt_tinhDi,txt_tinhDen,txt_thoiGian,txt_soGhe;
        ConstraintLayout layout;
        public VeDaBanHolder(@NonNull View itemView) {
            super(itemView);
            txt_soGhe=itemView.findViewById(R.id.item_vedaban_soghe);
            txt_tinhDi=itemView.findViewById(R.id.item_vedaban_tinhdi);
            txt_tinhDen=itemView.findViewById(R.id.item_vedaban_tinhden);
            txt_thoiGian=itemView.findViewById(R.id.item_vedaban_thoigiandi);
            layout=itemView.findViewById(R.id.item_vedaban_layout);
        }
    }
}
