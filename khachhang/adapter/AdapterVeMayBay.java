package com.example.khachhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhang.R;
import com.example.khachhang.doiTuong.VeMayBay;
import com.example.khachhang.my_interfave.IClickItemVeMayBay;

import java.io.DataInput;
import java.nio.file.attribute.FileAttribute;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterVeMayBay extends RecyclerView.Adapter<AdapterVeMayBay.VeMayBayHolder> implements Filterable {
    private List<VeMayBay>danhSachVeMayBay;
    private List<VeMayBay>getDanhSachVeMayBayOLD;
    private IClickItemVeMayBay iClickItemVeMayBay;
    private Context context;

    public AdapterVeMayBay(Context context,List<VeMayBay> danhSachVeMayBay, IClickItemVeMayBay iClickItemVeMayBay) {
        this.context=context;
        this.danhSachVeMayBay = danhSachVeMayBay;
        this.iClickItemVeMayBay = iClickItemVeMayBay;
        this.getDanhSachVeMayBayOLD=danhSachVeMayBay;
    }

    @NonNull
    @Override
    public VeMayBayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // tạo viewhole với layout item
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vemaybay,parent,false);
        return new VeMayBayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VeMayBayHolder holder, int position) {
        final VeMayBay veMayBay=danhSachVeMayBay.get(position);
        if (veMayBay == null) {
            return;
        }
        //lay gia tri thowi gian
        SimpleDateFormat spdGio=new SimpleDateFormat("HH:mm");
        SimpleDateFormat spdNgay=new SimpleDateFormat("dd/MM/yyyy");
        holder.txt_thoiGianDi.setText(spdGio.format(veMayBay.getThoiGianDi().getTime()));
        holder.txt_thoiGianDen.setText(spdGio.format(veMayBay.getThoiGianDen().getTime()));
        holder.txt_ngayDi.setText(spdNgay.format(veMayBay.getThoiGianDi().getTime()));
        // tinh tong thoi fin
        float di=veMayBay.getThoiGianDi().getTimeInMillis();
        float den=veMayBay.getThoiGianDen().getTimeInMillis();
        float tong=(den-di)/(60*60*1000);
        //
        DecimalFormat df=new DecimalFormat("#.#h");
        String strTong=df.format(tong);
        holder.txt_tongThoiGian.setText(strTong);
        //casc thuoc tinh con lai
        holder.txt_maTinhDi.setText(veMayBay.getTenTinhDi());
        holder.txt_maTinhDen.setText(veMayBay.getTenTinhDen());
        holder.txt_mayBay.setText(veMayBay.getMaMayBay());
        //dinh gdang tin
        float giaVe=(float) veMayBay.getGiaVe();
        DecimalFormat dfGia=new DecimalFormat("#,###đ");
        String StrGia=dfGia.format(giaVe);
        holder.txt_giaTien.setText(StrGia);
        //
        holder.txt_xemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemVeMayBay.traDuLieuVeMayBay(veMayBay);
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemVeMayBay.traDuLieuVeMayBay(veMayBay);
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachVeMayBay == null ? 0 : danhSachVeMayBay.size();
    }
    public class VeMayBayHolder extends RecyclerView.ViewHolder{
        //khai báo thuộc tính
        TextView txt_thoiGianDi, txt_thoiGianDen,txt_tongThoiGian, txt_maTinhDi, txt_maTinhDen,txt_ngayDi;
        TextView txt_mayBay, txt_giaTien, txt_xemChiTiet;
        ConstraintLayout item;
        public VeMayBayHolder(@NonNull View itemView) {
            super(itemView);
            txt_thoiGianDi      =(TextView)itemView.findViewById(R.id.txt_dongveMB_giodi );
            txt_thoiGianDen     =(TextView)itemView.findViewById(R.id.txt_dongveMB_gioden);
            txt_tongThoiGian    =(TextView)itemView.findViewById(R.id.txt_dongveMB_thoigiandi);
            txt_maTinhDi        =(TextView)itemView.findViewById(R.id.txt_dongveMB_matinhdi);
            txt_maTinhDen       =(TextView)itemView.findViewById(R.id.txt_dongveMB_matinhden);
            txt_ngayDi          =(TextView)itemView.findViewById(R.id.txt_dongveMB_ngaydi);
            txt_mayBay          =(TextView)itemView.findViewById(R.id.txt_dongveMB_tenmaybay);
            txt_giaTien         =(TextView)itemView.findViewById(R.id.txt_dongveMB_giave);
            txt_xemChiTiet      =(TextView)itemView.findViewById(R.id.txt_dongveMB_xemthem);
            item=(ConstraintLayout) itemView.findViewById(R.id.item_dongveMB_item);
            // anh xa
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // Dữ liệu vào để tìm kiếm
                String strSearch = constraint.toString();
               if(strSearch.isEmpty()){
                   danhSachVeMayBay=getDanhSachVeMayBayOLD;
               }else{
                   List<VeMayBay>list=new ArrayList<>();
                   for (VeMayBay veMayBay:danhSachVeMayBay){
                       if(veMayBay.getTenTinhDen().toLowerCase().contains(strSearch.toLowerCase())){
                           list.add(veMayBay);
                       }
                   }
                   danhSachVeMayBay=list;
               }
                FilterResults filterResults = new FilterResults();
                filterResults.values = danhSachVeMayBay;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                danhSachVeMayBay=(List<VeMayBay>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
