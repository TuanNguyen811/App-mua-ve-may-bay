package com.example.khachhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.VideoView;
import java.text.Normalizer;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhang.R;
import com.example.khachhang.doiTuong.DiaChi;
import com.example.khachhang.my_interfave.IClickItem;

import java.net.InterfaceAddress;
import java.net.URI;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiaChiAdapter extends RecyclerView.Adapter<DiaChiAdapter.DiaChiHolder> implements Filterable {
    private List<DiaChi> danhSachDiaChi;
    private List<DiaChi> danhSachDiaChiOLD;
    private IClickItem iClickItem;
    public DiaChiAdapter(List<DiaChi> danhSachDiaChi,IClickItem iClickItem) {
        this.iClickItem=iClickItem;
        this.danhSachDiaChi = danhSachDiaChi;
        this.danhSachDiaChiOLD=danhSachDiaChi;
    }
    @NonNull
    @Override
    public DiaChiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // tạo viewhole với layout item
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diachi,parent,false);
        return new DiaChiHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DiaChiHolder holder, int position) {
        final DiaChi diaChi=danhSachDiaChi.get(position);
        if(diaChi==null){
            return;
        }
        holder.txt_tenDiaChi.setText(diaChi.getTenDiaChi());
        holder.txt_tenSanBay.setText(diaChi.getTenSanBay());
        holder.txt_maTinh.setText(diaChi.getMaDiaChi()+" - ");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iClickItem!=null){
                    iClickItem.layDuLieuDiaChi(diaChi);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        if(danhSachDiaChi!=null){
            return danhSachDiaChi.size();
        }
        return 0;
    }
    public class DiaChiHolder extends RecyclerView.ViewHolder{
        TextView txt_tenDiaChi,txt_tenSanBay,txt_maTinh;
        ConstraintLayout layout;
        public DiaChiHolder(@NonNull View itemView) {
            super(itemView);
            txt_tenDiaChi=itemView.findViewById(R.id.txt_dongdiachi_tendiachi);
            txt_tenSanBay=itemView.findViewById(R.id.txt_dongdiachi_tensanbay);
            txt_maTinh=itemView.findViewById(R.id.txt_dongdiachi_matinh);
            layout=(ConstraintLayout)itemView.findViewById(R.id.layout_item_diachi);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint == null ? "" : removeDiacritics(constraint.toString().toLowerCase());
                List<DiaChi> list = new ArrayList<>();
                if(strSearch.isEmpty()){
                    list = new ArrayList<>(danhSachDiaChiOLD);
                } else {
                    for (DiaChi diaChi : danhSachDiaChiOLD){
                        String strtenTinh = removeDiacritics(diaChi.getTenDiaChi().toLowerCase());
                        String strtenSanBay = removeDiacritics(diaChi.getTenSanBay().toLowerCase());
                            if(strtenTinh.contains(strSearch)|| diaChi.getMaDiaChi().toLowerCase().contains(strSearch) || strtenSanBay.contains(strSearch)){
                            list.add(diaChi);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values instanceof List) {
                    danhSachDiaChi = (List<DiaChi>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public String removeDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("\\p{M}", "");
        return str;
    }
}
