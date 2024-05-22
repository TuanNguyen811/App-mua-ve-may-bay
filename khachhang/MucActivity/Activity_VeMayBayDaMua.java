package com.example.khachhang.MucActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.khachhang.adapter.AdapterVeDaBan;
import com.example.khachhang.dialog.DialogVeMayBayDaBan;
import com.example.khachhang.doiTuong.DiaChi;
import com.example.khachhang.doiTuong.VeMayBayDaBan;
import com.example.khachhang.my_interfave.Interface_vemaybaydaban;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_VeMayBayDaMua extends AppCompatActivity {
    RecyclerView rsc_danhSach,rsc_danhSachHetHan;
    ArrayList<VeMayBayDaBan>danhSachVeMayBayDaMua,danhSachVeMayBayDaMuaHetHan;
    AdapterVeDaBan adapterVeDaBan,adapterVeDaBanHetHan;

    SharedPreferences dataKhachHang,dataUrl;
    SharedPreferences.Editor editorDataKhachHang,editorDataUrl;
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang,urlVeMayBayDaBan;
    String gmailKhachHang,matKhauKhachHang;
    Boolean daDangNhap;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ve_may_bay_da_mua);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.bt_tab);
        bottomNavigationView.getMenu().findItem(R.id.menu_quanly).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menu_timve){
                    startActivity(new Intent(Activity_VeMayBayDaMua.this, MainActivity.class));
                }else if(menuItem.getItemId()==R.id.menu_khamPha){
                    startActivity(new Intent(Activity_VeMayBayDaMua.this, Activity_KhamPha.class));
                }else if(menuItem.getItemId()==R.id.menu_quanly){

                } else if (menuItem.getItemId()==R.id.menu_taikhoan) {
                    startActivity(new Intent(Activity_VeMayBayDaMua.this,Activity_TrangCaNhan.class));
                }
                return true;
            }
        });
        khoiTao();
        layDuLieuBanDau();
        layVeMayBayDaBan();
    }

    private void layVeMayBayDaBan(){
        String urlVeMayBayDK=urlVeMayBayDaBan+"?me="+"timvemaybaydabantheoma"+"&gmail="+gmailKhachHang;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, urlVeMayBayDK, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        danhSachVeMayBayDaMua.clear();
                        danhSachVeMayBayDaMuaHetHan.clear();
                        if(jsonArray.length()==0){
                            Toast.makeText(Activity_VeMayBayDaMua.this, "Danh sách rỗng", Toast.LENGTH_SHORT).show();
                        }
                        for (int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject object=jsonArray.getJSONObject(i);
                                int id=object.getInt("Id");
                                String maChuyenBay=object.getString("MaChuyenBay");
                                int soGhe=object.getInt("SoGhe");
                                String gmailKhachHang=object.getString("GmailKhachHang");
                                String tenKhachHang=object.getString("TenKhachHang");
                                int sdtKhachHang=object.getInt("SdtKhachHang");
                                String tinhDi=object.getString("TinhDi");
                                String tinhDen=object.getString("TinhDen");
                                String strNgay = object.getString("NgayDi");
                                SimpleDateFormat sfd =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Calendar thoiGianDi=Calendar.getInstance();
                                try{
                                    Date date=sfd.parse(strNgay);
                                    thoiGianDi.setTime(date);
                                }catch (ParseException e){
                                    e.printStackTrace();
                                }
                                int giaVe=object.getInt("GiaVe");
                                String mayBay=object.getString("MaMayBay");
                                VeMayBayDaBan veMayBayDaBan=new VeMayBayDaBan(id,maChuyenBay,soGhe,gmailKhachHang,tenKhachHang,sdtKhachHang,tinhDi,tinhDen,thoiGianDi,giaVe,mayBay);
                                //so sanh voi thoi gian hien tai

                                Calendar thoiGianHienTai=Calendar.getInstance();
                                int result = thoiGianDi.compareTo(thoiGianHienTai);
                                if (result < 0) {
                                    danhSachVeMayBayDaMuaHetHan.add(veMayBayDaBan);
                                } else if (result > 0) {
                                    danhSachVeMayBayDaMua.add(veMayBayDaBan);
                                } else {
                                    danhSachVeMayBayDaMua.add(veMayBayDaBan);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        adapterVeDaBanHetHan.notifyDataSetChanged();
                        adapterVeDaBan.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(findViewById(android.R.id.content), "Lỗi mạng. Vui lòng thử lại sau.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Thử lại", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Retry logic here
                                        startActivity(new Intent(Activity_VeMayBayDaMua.this,Activity_VeMayBayDaMua.class));
                                    }}).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    void layDuLieuBanDau(){
        requestQueue= Volley.newRequestQueue(this);
        //lay thong tin url
        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        urlDiaChi=dataUrl.getString("urlDiaChi","");
        urlVeMayBay=dataUrl.getString("urlVeMayBay","");
        urlMayBay=dataUrl.getString("urlMayBay","");
        urlKhachHang=dataUrl.getString("urlKhachHang","");
        urlVeMayBayDaBan=dataUrl.getString("urlVeMayBayDaBan","");
        //lay thong tin khach hang
        dataKhachHang=getSharedPreferences("dataKhachHang",MODE_PRIVATE);
        daDangNhap=dataKhachHang.getBoolean("dangNhap",false);
        gmailKhachHang=dataKhachHang.getString("taiKhoan","");
        matKhauKhachHang=dataKhachHang.getString("matKhau","");
    }
    void khoiTao(){
        rsc_danhSach=(RecyclerView) findViewById(R.id.rsc_vedanay_danhsach);
        rsc_danhSachHetHan=(RecyclerView)findViewById(R.id.rsc_vedanay_danhsachHetHan);
        danhSachVeMayBayDaMua=new ArrayList<>();
        danhSachVeMayBayDaMuaHetHan=new ArrayList<>();
        //danhSachVeMayBayDaMua.add(new VeMayBayDaBan(1,"",00,"","",00,"","", Calendar.getInstance(),00,""));
        adapterVeDaBan=new AdapterVeDaBan(danhSachVeMayBayDaMua, new Interface_vemaybaydaban() {
            @Override
            public void layDuLieu(VeMayBayDaBan veMayBayDaBan) {
                DialogVeMayBayDaBan dialogVeMayBayDaBan=new DialogVeMayBayDaBan(Activity_VeMayBayDaMua.this,veMayBayDaBan);
                dialogVeMayBayDaBan.show();
            }
        });
        adapterVeDaBanHetHan=new AdapterVeDaBan(danhSachVeMayBayDaMuaHetHan, new Interface_vemaybaydaban() {
            @Override
            public void layDuLieu(VeMayBayDaBan veMayBayDaBan) {
                DialogVeMayBayDaBan dialogVeMayBayDaBan=new DialogVeMayBayDaBan(Activity_VeMayBayDaMua.this,veMayBayDaBan);
                dialogVeMayBayDaBan.show();
            }
        });
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(this);
        rsc_danhSachHetHan.setLayoutManager(linearLayoutManager2);
        RecyclerView.ItemDecoration itemDecoration2=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rsc_danhSachHetHan.addItemDecoration(itemDecoration2);
        rsc_danhSachHetHan.setAdapter(adapterVeDaBanHetHan);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rsc_danhSach.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rsc_danhSach.addItemDecoration(itemDecoration);
        rsc_danhSach.setAdapter(adapterVeDaBan);

    }
}