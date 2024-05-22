package com.example.khachhang.MucActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.R;
import com.example.khachhang.adapter.AdapterVeMayBay;
import com.example.khachhang.dialog.DialogVeMayBay;
import com.example.khachhang.doiTuong.VeMayBay;
import com.example.khachhang.my_interfave.IClickItemVeMayBay;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_TimChuyenBay extends AppCompatActivity {
    //khoi bao phan giao dien
    ImageView bt_timKiem;
    ImageButton bt_thoat;
    ConstraintLayout layout_timKiem;
    TextView txt_tinhDi, txt_tinhDen,txt_thoiGian,txt_thongBao;
    RecyclerView rsc_chuyenBayPhuHop;
    RecyclerView rsc_chuyenBayGoiY;
    // khoi bao phan URL
    private SharedPreferences dataUrl;
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang;
    // khoi bao phan adapter danh Sach chuen bay
    AdapterVeMayBay adapterVeMayBayPhuHop;
    AdapterVeMayBay adapterVeMayBayGoiY;
    ArrayList <VeMayBay> danhSachVeMayBayPhuHop;
    ArrayList <VeMayBay> danhSachVeMayBayGoiY;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tim_chuyen_bay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide();//an thanh toolbar
        khoiTaoBanDau();
        Toast.makeText(this, "Đang tìm chuyến bay...", Toast.LENGTH_SHORT).show();
        layout_timKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bt_thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bt_timKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Nhap du lieu tu thanh tim kiem main chinh
        Intent intent=getIntent();
        if(intent!=null){
            String tinhDi=intent.getStringExtra("tinhDi");
            String tinhDen=intent.getStringExtra("tinhDen");
            String thoiGian=intent.getStringExtra("thoiGian");
            txt_tinhDi.setText(tinhDi);
            txt_tinhDen.setText(tinhDen);
            txt_thoiGian.setText(thoiGian);

            //định dạng lại thời gian
            SimpleDateFormat spdIn=new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat spdOU=new SimpleDateFormat("yyyy-MM-dd");
            String strtimKiemThoiGianDi2="";
            try {
                Date date=spdIn.parse(thoiGian.toString());
                strtimKiemThoiGianDi2=spdOU.format(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(this, strtimKiemThoiGianDi2, Toast.LENGTH_SHORT).show();
            layDuLieuVeMayBayCoDieuKien2(tinhDi,tinhDen,strtimKiemThoiGianDi2);
            //layDuLieuVeMayBay();
        }
    }

    private void khoiTaoBanDau(){
        anhXa();
        requestQueue= Volley.newRequestQueue(this);
        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        urlDiaChi=dataUrl.getString("urlDiaChi"," ");
        urlVeMayBay=dataUrl.getString("urlVeMayBay"," ");
        urlMayBay=dataUrl.getString("urlMayBay"," ");
        urlKhachHang=dataUrl.getString("urlKhachHang"," ");
        danhSachVeMayBayPhuHop=new ArrayList<>();
        danhSachVeMayBayGoiY=new ArrayList<>();
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rsc_chuyenBayPhuHop.addItemDecoration(itemDecoration);
        rsc_chuyenBayGoiY.addItemDecoration(itemDecoration);
        // Tạo LinearLayoutManager cho RecyclerView rsc_chuyenBayPhuHop
        LinearLayoutManager linearLayoutManagerPhuHop = new LinearLayoutManager(this);
        rsc_chuyenBayPhuHop.setLayoutManager(linearLayoutManagerPhuHop);
        // Tạo LinearLayoutManager cho RecyclerView rsc_chuyenBayGoiY
        LinearLayoutManager linearLayoutManagerGoiY = new LinearLayoutManager(this);
        rsc_chuyenBayGoiY.setLayoutManager(linearLayoutManagerGoiY);

        adapterVeMayBayPhuHop=new AdapterVeMayBay(this, danhSachVeMayBayPhuHop, new IClickItemVeMayBay() {
            @Override
            public void traDuLieuVeMayBay(VeMayBay veMayBay) {
                DialogVeMayBay dialogVeMayBay=new DialogVeMayBay(Activity_TimChuyenBay.this,veMayBay);
                dialogVeMayBay.show();
            }
        });
        adapterVeMayBayGoiY=new AdapterVeMayBay(this, danhSachVeMayBayGoiY, new IClickItemVeMayBay() {
            @Override
            public void traDuLieuVeMayBay(VeMayBay veMayBay) {
                DialogVeMayBay dialogVeMayBay=new DialogVeMayBay(Activity_TimChuyenBay.this,veMayBay);
                dialogVeMayBay.show();
            }
        });

        rsc_chuyenBayPhuHop.setAdapter(adapterVeMayBayPhuHop);
        rsc_chuyenBayGoiY.setAdapter(adapterVeMayBayGoiY);
    }
    private void layDuLieuVeMayBayCoDieuKien2(String tinhDi, String tinhDen,String ngayDi){
        String urlVeMayBayDk=urlVeMayBay+"?tinhDi="+tinhDi+"&tinhDen="+tinhDen+"&ngayDi="+ngayDi+"&me="+"timvemaybaygoiy";
        Calendar thoiGianCanTim=Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf1.parse(ngayDi);
            thoiGianCanTim.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        thoiGianCanTim.set(Calendar.HOUR_OF_DAY, 0);
        thoiGianCanTim.set(Calendar.MINUTE, 0);
        thoiGianCanTim.set(Calendar.SECOND, 0);
        thoiGianCanTim.set(Calendar.MILLISECOND, 0);
        String strThoiGianCanTim=sdf1.format(thoiGianCanTim.getTime());

        Toast.makeText(this, strThoiGianCanTim, Toast.LENGTH_SHORT).show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlVeMayBayDk, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){
                            Toast.makeText(Activity_TimChuyenBay.this, "Không có chuyến bay", Toast.LENGTH_SHORT).show();
                            txt_thongBao.setVisibility(View.VISIBLE);
                        }
                        danhSachVeMayBayPhuHop.clear();
                        danhSachVeMayBayGoiY.clear();
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject object=response.getJSONObject(i);
                                int id=object.getInt("Id");
                                String maChuyenBay = object.getString("MaChuyenBay");
                                String maMayBay = object.getString("MaMayBay");
                                String tenTinhDi = object.getString("TenTinhDi");
                                String tenTinhDen = object.getString("TenTinhDen");
                                //lấy thời gian với kiểu String
                                String strThoiGianDi = object.getString("ThoiGianDi");
                                String strThoiGianDen = object.getString("ThoiGianDen");
                                //chuyen string sang calender
                                SimpleDateFormat sfd =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Calendar thoiGianDi=Calendar.getInstance();
                                Calendar thoiGianDen=Calendar.getInstance();
                                try{
                                    Date date=sfd.parse(strThoiGianDi);
                                    thoiGianDi.setTime(date);
                                    Date date1=sfd.parse(strThoiGianDen);
                                    thoiGianDen.setTime(date1);
                                }catch (ParseException e){
                                    e.printStackTrace();
                                }
                                //thong so tiep theo
                                int soGhe=object.getInt("SoGhe");
                                int giaVe=object.getInt("GiaVe");
                                String thongTinChiTiet=object.getString("ThongTinThem");
                                //So sanh voi thoi gian hien tsai
                                Calendar thoiGianHienTai=Calendar.getInstance();
                                int result = thoiGianDi.compareTo(thoiGianHienTai);
                                if (result < 0) {

                                }else{
                                    thoiGianDi.set(Calendar.HOUR_OF_DAY, 0);
                                    thoiGianDi.set(Calendar.MINUTE, 0);
                                    thoiGianDi.set(Calendar.SECOND, 0);
                                    thoiGianDi.set(Calendar.MILLISECOND, 0);
                                    //xet laij truong hop
                                    int result2 = thoiGianDi.compareTo(thoiGianCanTim);
                                    if (result2 != 0&&soGhe>=1) {
                                        try{
                                            Date date=sfd.parse(strThoiGianDi);
                                            thoiGianDi.setTime(date);
                                        }catch (ParseException e){
                                            e.printStackTrace();
                                        }
                                        danhSachVeMayBayGoiY.add(new VeMayBay(id,maChuyenBay,maMayBay,tenTinhDi,tenTinhDen,thoiGianDi,thoiGianDen,soGhe,giaVe,thongTinChiTiet));
                                    }else if(result2 == 0&&soGhe>=1){
                                        try{
                                            Date date=sfd.parse(strThoiGianDi);
                                            thoiGianDi.setTime(date);
                                        }catch (ParseException e){
                                            e.printStackTrace();
                                        }
                                        danhSachVeMayBayPhuHop.add(new VeMayBay(id,maChuyenBay,maMayBay,tenTinhDi,tenTinhDen,thoiGianDi,thoiGianDen,soGhe,giaVe,thongTinChiTiet));
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        adapterVeMayBayGoiY.notifyDataSetChanged();
                        adapterVeMayBayPhuHop.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_TimChuyenBay.this, "Loi ket noi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    private void layDuLieuVeMayBayCoDieuKien(String tinhDi, String tinhDen,String ngayDiCanTim){
        String urlVeMayBayDk=urlVeMayBay+"?tinhDi="+tinhDi+"&tinhDen="+tinhDen+"&ngayDi="+ngayDiCanTim+"&me="+"timvemaybaygoiy";

        SimpleDateFormat sfd =new SimpleDateFormat("yyyy-MM-dd");
        Calendar ngayCanTim=Calendar.getInstance();
        try{
            Date date=sfd.parse(ngayDiCanTim);
            ngayCanTim.setTime(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        String thoiGianCanTimmmm = sfd.format(ngayCanTim.getTime());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlVeMayBayDk, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){
                            Toast.makeText(Activity_TimChuyenBay.this, "Không có chuyến bay", Toast.LENGTH_SHORT).show();
                            txt_thongBao.setVisibility(View.VISIBLE);
                        }
                        danhSachVeMayBayPhuHop.clear();
                        danhSachVeMayBayGoiY.clear();
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject object=response.getJSONObject(i);
                                int id=object.getInt("Id");
                                String maChuyenBay = object.getString("MaChuyenBay");
                                String maMayBay = object.getString("MaMayBay");
                                String tenTinhDi = object.getString("TenTinhDi");
                                String tenTinhDen = object.getString("TenTinhDen");
                                //lấy thời gian với kiểu String
                                String strThoiGianDi = object.getString("ThoiGianDi");
                                String strThoiGianDen = object.getString("ThoiGianDen");
                                //chuyen string sang calender
                                Calendar thoiGianDi=Calendar.getInstance();
                                Calendar thoiGianDen=Calendar.getInstance();
                                try{
                                    Date date=sfd.parse(strThoiGianDi);
                                    thoiGianDi.setTime(date);
                                    Date date1=sfd.parse(strThoiGianDen);
                                    thoiGianDen.setTime(date1);
                                }catch (ParseException e){
                                    e.printStackTrace();
                                }
                                //thong so tiep theo
                                int soGhe=object.getInt("SoGhe");
                                int giaVe=object.getInt("GiaVe");
                                String thongTinChiTiet=object.getString("ThongTinThem");
                                //So sanh voi thoi gian hien tsai
                                Calendar thoiGianHienTai=Calendar.getInstance();
                                int result = thoiGianDi.compareTo(thoiGianHienTai);
                                if (result < 0) {
                                    //truong hop ve het han
                                }else{
                                    //truong hop ve con han
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String thoiGian= sfd.format(thoiGianDi.getTime());

                                    if (thoiGian!=thoiGianCanTimmmm&&soGhe>=1) {
                                        try{
                                            Date date=sdf2.parse(strThoiGianDi);
                                            thoiGianDi.setTime(date);
                                        }catch (ParseException e){
                                            e.printStackTrace();
                                        }
                                        danhSachVeMayBayGoiY.add(new VeMayBay(id,maChuyenBay,maMayBay,tenTinhDi,tenTinhDen,thoiGianDi,thoiGianDen,soGhe,giaVe,thongTinChiTiet));
                                    }else if(thoiGian==thoiGianCanTimmmm&&soGhe>=1){
                                        try{
                                            Date date=sdf2.parse(strThoiGianDi);
                                            thoiGianDi.setTime(date);
                                        }catch (ParseException e){
                                            e.printStackTrace();
                                        }
                                        danhSachVeMayBayPhuHop.add(new VeMayBay(id,maChuyenBay,maMayBay,tenTinhDi,tenTinhDen,thoiGianDi,thoiGianDen,soGhe,giaVe,thongTinChiTiet));
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        adapterVeMayBayGoiY.notifyDataSetChanged();
                        adapterVeMayBayPhuHop.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(findViewById(android.R.id.content), "Lỗi mạng. Vui lòng thử lại sau.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Thử lại", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Retry logic here
                                    }}).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    private void anhXa(){
        bt_timKiem=(ImageView) findViewById(R.id.btIm_timchuyenbay_timKiem);
        bt_thoat=(ImageButton)findViewById(R.id.btIm_timchuyenbay_thoat);
        layout_timKiem=(ConstraintLayout) findViewById(R.id.constraintLayout_timchuyenbay_timkiem);
        txt_tinhDi=(TextView) findViewById(R.id.txt_timchuyenbay_tinhdi);
        txt_tinhDen=(TextView) findViewById(R.id.txt_timchuyenbay_tinhden);
        txt_thoiGian=(TextView) findViewById(R.id.txt_timchuyenbay_thoigian);
        txt_thongBao=(TextView)findViewById(R.id.txt_timchuyenbay_thongbaokocochuyenbay);
        rsc_chuyenBayPhuHop=(RecyclerView) findViewById(R.id.recyclerView_timchuyenbay_phuhop);
        rsc_chuyenBayGoiY=(RecyclerView) findViewById(R.id.recyclerView_timchuyenbay_goiy);
    }
}