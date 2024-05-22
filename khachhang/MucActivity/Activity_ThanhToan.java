package com.example.khachhang.MucActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.R;
import com.example.khachhang.adapter.AdapterKhachHangPhu;
import com.example.khachhang.dialog.DialogThanhToan;
import com.example.khachhang.doiTuong.DiaChi;
import com.example.khachhang.doiTuong.KhachHang;
import com.example.khachhang.doiTuong.KhachHangPhu;
import com.example.khachhang.doiTuong.MayBay;
import com.example.khachhang.doiTuong.VeMayBay;
import com.example.khachhang.doiTuong.VeMayBayDaBan;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Activity_ThanhToan extends AppCompatActivity {
    //Phan chuyen bay
    TextView txt_maChuyenBay,txt_gioDi,txt_ngayDi,txt_tongThoiGian,txt_gioDen,txt_ngayDen;
    TextView txt_tinhDi,txt_sanBayDi,txt_maMayBay,txt_mayBay,txt_soGhe,txt_tinhDen,txt_sanBayDen,txt_thongTinVe;
    //thong tinlien he
    TextView txt_tenKhachHang,txt_gmailKhachHang,txt_diaChiKhachHang,txt_cccdKhachHang,txt_sdtKhachHang;
    //Phan ve may bay
    TextView txt_VMBngayDi, txt_VMBtinhDi, txt_VMBtinhDen, txt_VMBsoLuongVe,txt_VMBgiaVe;
    Button bt_muaNgay;
    ImageView bt_congVe,bt_truVe;
    ListView lv_danhSachVe;
    ArrayList <KhachHangPhu> danhSachKhachHangPhu;
    AdapterKhachHangPhu adapterKhachHangPhu;
    // phan thong tin gui den
    VeMayBay veMayBay;
    KhachHang khachHang;
    SharedPreferences dataKhachHang,dataUrl;
    SharedPreferences.Editor editorDataKhachHang,editorDataUrl;
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang,urlVeMayBayDaBan;
    String gmailKhachHang,matKhauKhachHang;
    Boolean daDangNhap;
    RequestQueue requestQueue;

    VeMayBayDaBan veMayBayDaBan1111;
    int soLuongVe=1;
    int soLuongGheCon=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thanh_toan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhXa();
        khoiTaoBanDau();
        ganThongTinChuyenBat(veMayBay);
        ganThongTinNguoiDung("timkhachhang",gmailKhachHang,matKhauKhachHang);
        bt_congVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soLuongVe++;
                if(soLuongVe<=soLuongGheCon){
                    txt_VMBsoLuongVe.setText(String.valueOf(soLuongVe));
                    float giave=(float) veMayBay.getGiaVe();
                    giave*=soLuongVe;
                    DecimalFormat dfgiatien=new DecimalFormat("#,###đ");
                    String giaVe=dfgiatien.format(giave);
                    txt_VMBgiaVe.setText(giaVe);
                    danhSachKhachHangPhu.add(new KhachHangPhu("",0));
                    adapterKhachHangPhu.notifyDataSetChanged();
                    dieuChinhDoChoLisvew();
                }else{
                    Toast.makeText(Activity_ThanhToan.this, "Hệ thống hết vé!", Toast.LENGTH_SHORT).show();
                    soLuongVe--;
                }
            }
        });
        bt_truVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soLuongVe>1){
                    soLuongVe--;
                    txt_VMBsoLuongVe.setText(String.valueOf(soLuongVe));
                    float giave=(float) veMayBay.getGiaVe();
                    giave*=soLuongVe;
                    DecimalFormat dfgiatien=new DecimalFormat("#,###đ");
                    String giaVe=dfgiatien.format(giave);
                    txt_VMBgiaVe.setText(giaVe);
                    danhSachKhachHangPhu.remove(danhSachKhachHangPhu.size()-1);
                    adapterKhachHangPhu.notifyDataSetChanged();
                    dieuChinhDoChoLisvew();
                }else{
                    Toast.makeText(Activity_ThanhToan.this, "Số lượng tối thiểu 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lv_danhSachVe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogThemKhachHangPhu(position);
            }
        });
        bt_muaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean dieuKien=true;
                for(int i=0;i<soLuongVe;i++){
                    KhachHangPhu khachHangPhu2=danhSachKhachHangPhu.get(i);
                    if(khachHangPhu2.getTen().isEmpty()){
                        dieuKien=false;
                    }
                }
                if(dieuKien){
                    dialogXacNhanThanhToan();
                }else{
                    Toast.makeText(Activity_ThanhToan.this, "Vui lòng điển đầy đủ thông tin khach hàng!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void dialogXacNhanThanhToan(){
        AlertDialog.Builder diaLogXacNhan=new AlertDialog.Builder(this);
        diaLogXacNhan.setMessage("Xác nhận thanh toán\n"+"Đơn giá: "+txt_VMBgiaVe.getText());
        diaLogXacNhan.setPositiveButton("Thanh toán", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=1;i<=soLuongVe;i++){
                    int id=0;
                    String maChuyenBay=veMayBay.getMaChuyenBay();
                    int soGhe=veMayBay.getSoGhe();
                    String gmailKhachHang=khachHang.getGmail();
                    KhachHangPhu khachHangPhu1=danhSachKhachHangPhu.get(i-1);
                    String tenKhachHang=khachHangPhu1.getTen();
                    int sdtKhachHang=khachHangPhu1.getSdt();
                    String tinhDi=veMayBay.getTenTinhDi();
                    String tinhDen=veMayBay.getTenTinhDen();
                    Calendar ngayDi=Calendar.getInstance();
                    ngayDi.setTime(veMayBay.getThoiGianDen().getTime());
                    int giaVe=veMayBay.getGiaVe();
                    String mayBay=veMayBay.getMaMayBay();
                    VeMayBayDaBan veMayBayDaBan1=new VeMayBayDaBan(id,maChuyenBay,soGhe,gmailKhachHang,tenKhachHang,sdtKhachHang,tinhDi,tinhDen,ngayDi,giaVe,mayBay);
                    taiDuLieuVeMayBayDaBan(urlVeMayBayDaBan,veMayBayDaBan1);
                }
            }
        });
        diaLogXacNhan.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        diaLogXacNhan.show();
    }

    private void taiDuLieuVeMayBayDaBan(String url,VeMayBayDaBan veMayBayDaBan){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("ThanhCong")){
                            DialogThanhToan dialogThanhToan=new DialogThanhToan(Activity_ThanhToan.this,veMayBay.getGiaVe(),khachHang.getHoVaTen(),khachHang.getSoDienThoai(),veMayBay.getMaChuyenBay(),soLuongVe);
                            dialogThanhToan.show();
                        }else if (s.equals("ThatBai")) {
                            Toast.makeText(Activity_ThanhToan.this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Activity_ThanhToan.this, "Hệ thống đang bận, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(findViewById(android.R.id.content), "Lỗi mạng. Vui lòng thử lại sau.", Snackbar.LENGTH_SHORT)
                                .setAction("Thử lại", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Retry logic here
                                    }}).show();

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parmas=new HashMap<>();
                //lay du lieu ra
                String id=String.valueOf(veMayBayDaBan.getId());
                String maChuyenBay=veMayBayDaBan.getMaChuyenBay();
                String soGhe=String.valueOf(veMayBayDaBan.getSoGhe());
                String gmail=veMayBayDaBan.getGmailKhachHang();
                String ten=veMayBayDaBan.getTenKhachHang();
                String sdt=String.valueOf(veMayBayDaBan.getSdtKhachHang());
                String tinhDi=veMayBayDaBan.getTinhDi();
                String tinhDen=veMayBayDaBan.getTinhDen();

                Calendar cld_ngayDi=veMayBayDaBan.getNgayDi();
                SimpleDateFormat simpleDateForma=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String str_ngayDi=simpleDateForma.format(cld_ngayDi.getTime());

                String giave=String.valueOf(veMayBayDaBan.getGiaVe());
                String maybay=veMayBayDaBan.getMayBay();

                // gửi dữ liệu di
                parmas.put("id",id.trim());
                parmas.put("maChuyenBay",maChuyenBay.trim());
                parmas.put("soGhe",soGhe.trim());
                parmas.put("gmailKhachHang",gmail.trim());
                parmas.put("tenKhachHang",ten.trim());
                parmas.put("sdtKhachHang",sdt.trim());
                parmas.put("tinhDi",tinhDi.trim());
                parmas.put("tinhDen",tinhDen.trim());
                parmas.put("ngayDi",str_ngayDi.trim());
                parmas.put("giaVe",giave.trim());
                parmas.put("maMayBay",maybay.trim());
                return parmas;
            }
        };
        requestQueue.add(stringRequest);// tao request
    }

    private void dialogThemKhachHangPhu(int position){
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thanhtoan_themkhachhangphu);
        EditText edt_ten=(EditText) dialog.findViewById(R.id.edt_diglog_thanhtoan_themten);
        EditText edt_sdt=(EditText) dialog.findViewById(R.id.edt_diglog_thanhtoan_themsdt);
        Button bt_them=(Button) dialog.findViewById(R.id.bt_edt_diglog_thanhtoan_them);
        Button bt_xoa=(Button) dialog.findViewById(R.id.bt_diglog_thanhtoan_xoa);
        if(!danhSachKhachHangPhu.get(position).getTen().isEmpty()){
            edt_ten.setText(danhSachKhachHangPhu.get(position).getTen().toString());
            edt_sdt.setText("0"+danhSachKhachHangPhu.get(position).getSdt());
        }
        bt_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_ten.getText().toString().isEmpty()||edt_sdt.getText().toString().isEmpty()){
                    Toast.makeText(Activity_ThanhToan.this, "Nhập đủ nội dung", Toast.LENGTH_SHORT).show();
                }else{
                    String ten=edt_ten.getText().toString();
                    String sdt=edt_sdt.getText().toString();
                    danhSachKhachHangPhu.get(position).setSdt(Integer.parseInt(sdt));
                    danhSachKhachHangPhu.get(position).setTen(ten);
                    adapterKhachHangPhu.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        bt_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                danhSachKhachHangPhu.remove(position);
                soLuongVe--;
                txt_VMBsoLuongVe.setText(String.valueOf(soLuongVe));
                float giave=(float) veMayBay.getGiaVe();
                giave*=soLuongVe;
                DecimalFormat dfgiatien=new DecimalFormat("#,###đ");
                String giaVe=dfgiatien.format(giave);
                txt_VMBgiaVe.setText(giaVe);

                adapterKhachHangPhu.notifyDataSetChanged();
                dieuChinhDoChoLisvew();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void dieuChinhDoChoLisvew(){
        AdapterKhachHangPhu adapter = (AdapterKhachHangPhu) lv_danhSachVe.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, lv_danhSachVe);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = lv_danhSachVe.getLayoutParams();
        params.height = totalHeight + (lv_danhSachVe.getDividerHeight() * (adapter.getCount() - 1));
        lv_danhSachVe.setLayoutParams(params);
        lv_danhSachVe.requestLayout();
    }
    private void ganThongTinChuyenBat(VeMayBay veMayBay){
        //lay thong tin di tim kiem
        String maMayBay=veMayBay.getMaMayBay();
        String tenTinhDi=veMayBay.getTenTinhDi();
        String tenTinhDen=veMayBay.getTenTinhDen();
        //gui du lieu di
        String urlMayBayDk=urlMayBay+"?me="+"timmaybay"+"&tenMayBay="+maMayBay;
        String urldiaChiDk=urlDiaChi+"?me="+"timdiachi"+"&tenTinhDi="+tenTinhDi+"&tenTinhDen="+tenTinhDen;
        // tao danh sach de luu thong tinve
        ArrayList<DiaChi> danhSachDiaChi=new ArrayList<>();
        ArrayList<MayBay> danhSachMayBay=new ArrayList<>();
        //lay du lie
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, urldiaChiDk, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        danhSachDiaChi.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject object=jsonArray.getJSONObject(i);
                                danhSachDiaChi.add(new DiaChi(object.getInt("Id"),object.getString("TenDiaDiem"),object.getString("MaDiaDiem"),object.getString("TenSanBay")));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        DiaChi diaChi1=danhSachDiaChi.get(0);
                        DiaChi diaChi2=danhSachDiaChi.get(1);
                        String maTinhDi,tenSanBayDi,maTinhDen,tenSanBayDen;
                        // gan thong tin len giao dien
                        if((diaChi1.getTenDiaChi().toString()).equals(tenTinhDi.toString())){
                            maTinhDi=diaChi1.getMaDiaChi();
                            tenSanBayDi=diaChi1.getTenSanBay();
                            maTinhDen=diaChi2.getMaDiaChi();
                            tenSanBayDen=diaChi2.getTenSanBay();
                        }else{
                            maTinhDi=diaChi2.getMaDiaChi();
                            tenSanBayDi=diaChi2.getTenSanBay();
                            maTinhDen=diaChi1.getMaDiaChi();
                            tenSanBayDen=diaChi1.getTenSanBay();
                        }
                        txt_tinhDi.setText(veMayBay.getTenTinhDi()+" - "+ maTinhDi);
                        txt_sanBayDi.setText(tenSanBayDi);
                        txt_tinhDen.setText(veMayBay.getTenTinhDen()+" - "+maTinhDen);
                        txt_sanBayDen.setText(tenSanBayDen);
                        txt_VMBtinhDi.setText(maTinhDi);
                        txt_VMBtinhDen.setText(maTinhDen);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Activity_ThanhToan.this, "Lỗi Hệ Thống!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
       // gan cac thong so ve may bay
        JsonArrayRequest jsonArrayRequestMayBay=new JsonArrayRequest(Request.Method.GET, urlMayBayDk, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        danhSachMayBay.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject object=jsonArray.getJSONObject(i);
                                danhSachMayBay.add(new MayBay(object.getInt("Id"),object.getString("MaMayBay"),object.getString("TenMayBay"),object.getInt("SoGhe")));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        MayBay mayBay1=danhSachMayBay.get(0);
                        String maMB=mayBay1.getMaMayBay();
                        txt_maMayBay.setText(maMB);
                        txt_mayBay.setText(veMayBay.getMaMayBay());
                        txt_soGhe.setText("Ghế: "+veMayBay.getSoGhe());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Activity_ThanhToan.this, "Lỗi Mạng", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequestMayBay);
        // gan cac thong so thoi gian
        // giờ đã có danh sách
        SimpleDateFormat spdNgay=new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat spdGio=new SimpleDateFormat("HH:mm");
        String gioDi=spdGio.format(veMayBay.getThoiGianDi().getTime());
        String ngayDi=spdNgay.format(veMayBay.getThoiGianDen().getTime());
        String gioDen=spdGio.format(veMayBay.getThoiGianDen().getTime());
        String ngayDen=spdNgay.format(veMayBay.getThoiGianDen().getTime());
        float di=veMayBay.getThoiGianDi().getTimeInMillis();
        float den=veMayBay.getThoiGianDen().getTimeInMillis();
        float tongthoigianbay=(den-di)/(60*60*1000);
        //định dạng số
        DecimalFormat df=new DecimalFormat("#.#h");
        String formatNuber=df.format(tongthoigianbay);
        txt_gioDi.setText(gioDi);
        txt_ngayDi.setText(ngayDi);
        txt_gioDen.setText(gioDen);
        txt_ngayDen.setText(ngayDen);
        txt_tongThoiGian.setText(formatNuber);
        txt_thongTinVe.setText(veMayBay.getThongTinThem().toString());
        txt_VMBngayDi.setText(ngayDi);
        float giave=(float) veMayBay.getGiaVe();
        DecimalFormat dfgiatien=new DecimalFormat("#,###đ");
        String giaVe=dfgiatien.format(giave);
        txt_VMBgiaVe.setText(giaVe);
        txt_VMBsoLuongVe.setText("1");
    }
    private void ganThongTinNguoiDung(String me, String gmail,String matKhau){
        String urlKhachHangDK=urlKhachHang+"?me="+me+"&gmail="+gmail+"&matKhau="+matKhau;
        //url+="?me="+me+"&gmail="+gmail+"&matKhau="+matKhau;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, urlKhachHangDK, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            JSONObject object=jsonArray.getJSONObject(0);
                            khachHang=new KhachHang(object.getInt("Id"),object.getString("HoVaTen"),object.getString("Gmail"),object.getString("MatKhau"),object.getString("DiaChi"),object.getInt("SoDienThoai"),object.getInt("CCCD"));
                            txt_tenKhachHang.setText(khachHang.getHoVaTen());
                            txt_gmailKhachHang.setText("Gmail: "+khachHang.getGmail());
                            txt_diaChiKhachHang.setText("Địa Chỉ: "+khachHang.getDiaChi());
                            txt_cccdKhachHang.setText("CCCD: "+khachHang.getcCCD());
                            txt_sdtKhachHang.setText("SĐT: 0"+khachHang.getSoDienThoai());
                            danhSachKhachHangPhu.add(new KhachHangPhu(khachHang.getHoVaTen(),khachHang.getSoDienThoai()));
                            adapterKhachHangPhu.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(Activity_ThanhToan.this, "Không có thông tin", Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Activity_ThanhToan.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    private void khoiTaoBanDau(){
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
        Intent intent=getIntent();
        if(intent!=null){
            veMayBay=(VeMayBay) intent.getSerializableExtra("thongTinVeMayBay");
            Toast.makeText(this, veMayBay.getMaMayBay()+" ", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Lỗi Hệ Thống!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Activity_ThanhToan.this,MainActivity.class));
        }
        txt_maChuyenBay.setText(veMayBay.getMaChuyenBay().toString());
        soLuongGheCon=veMayBay.getSoGhe();
        danhSachKhachHangPhu=new ArrayList<>();
        adapterKhachHangPhu=new AdapterKhachHangPhu(this,R.layout.item_khachhangphu,danhSachKhachHangPhu);
        lv_danhSachVe.setAdapter(adapterKhachHangPhu);
    }
    private void anhXa(){
        txt_maChuyenBay=(TextView)findViewById(R.id.txt_thanhtoan_machuyenbay2);
        txt_gioDi=(TextView)findViewById(R.id.txt_thanhtoan_giodi);
        txt_ngayDi=(TextView)findViewById(R.id.txt_thanhtoan_ngaydi);
        txt_tongThoiGian=(TextView)findViewById(R.id.txt_thanhtoan_tongthoigian);
        txt_gioDen=(TextView)findViewById(R.id.txt_thanhtoan_giodien);
        txt_ngayDen=(TextView)findViewById(R.id.txt_thanhtoan_ngayden);
        txt_tinhDi=(TextView)findViewById(R.id.txt_thanhtoan_tinhdi);
        txt_sanBayDi=(TextView)findViewById(R.id.txt_thanhtoan_sanbaydi);
        txt_maMayBay=(TextView)findViewById(R.id.txt_thanhtoan_mamybay);
        txt_mayBay=(TextView)findViewById(R.id.txt_thanhtoan_maybay);
        txt_soGhe=(TextView)findViewById(R.id.txt_thanhtoan_soghe);
        txt_tinhDen=(TextView)findViewById(R.id.txt_thanhtoan_tinhden);
        txt_sanBayDen=(TextView)findViewById(R.id.txt_thanhtoan_sanbayden);
        txt_thongTinVe=(TextView)findViewById(R.id.txt_thanhtoan_thongtinve);
        //thong tinlien he
        txt_tenKhachHang=(TextView)findViewById(R.id.txt_thanhtoan_khachhang_ten);
        txt_gmailKhachHang=(TextView)findViewById(R.id.txt_thanhtoan_khachhang_gmail);
        txt_diaChiKhachHang=(TextView)findViewById(R.id.txt_thanhtoan_khachhang_diachi);
        txt_cccdKhachHang=(TextView)findViewById(R.id.txt_thanhtoan_khachhang_cccd);
        txt_sdtKhachHang=(TextView)findViewById(R.id.txt_thanhtoan_khachhang_sdt);
        //Phan ve may bay
        txt_VMBngayDi=(TextView)findViewById(R.id.txt_thanhtoan_vemaybay_ngaydi);
        txt_VMBtinhDi=(TextView)findViewById(R.id.txt_thanhtoan_vemaybay_tinhdi);
        txt_VMBtinhDen=(TextView)findViewById(R.id.txt_thanhtoan_vemaybay_tinhden);
        txt_VMBsoLuongVe=(TextView)findViewById(R.id.txt_thanhtoan_vemaybay_sove);
        txt_VMBgiaVe=(TextView)findViewById(R.id.txt_thanhtoan_vemaybay_giave);

        bt_congVe=(ImageView)findViewById(R.id.txt_thanhtoan_vemaybay_cong);
        bt_truVe=(ImageView)findViewById(R.id.bt_txt_thanhtoan_vemaybay_tru);
        bt_muaNgay=(Button) findViewById(R.id.bt_txt_thanhtoan_vemaybay_muangay);

        lv_danhSachVe=(ListView)findViewById(R.id.lv_thanhtoan_danhsachve);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}