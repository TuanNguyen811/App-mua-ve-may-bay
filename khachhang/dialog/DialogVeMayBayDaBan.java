package com.example.khachhang.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.MucActivity.Activity_ThanhToan;
import com.example.khachhang.MucActivity.Activity_TrangCaNhan;
import com.example.khachhang.MucActivity.MainActivity;
import com.example.khachhang.R;
import com.example.khachhang.adapter.AdapterKhachHangPhu;
import com.example.khachhang.doiTuong.DiaChi;
import com.example.khachhang.doiTuong.KhachHang;
import com.example.khachhang.doiTuong.KhachHangPhu;
import com.example.khachhang.doiTuong.MayBay;
import com.example.khachhang.doiTuong.VeMayBay;
import com.example.khachhang.doiTuong.VeMayBayDaBan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DialogVeMayBayDaBan extends Dialog {
    Context context;
    TextView txt_giaVe;
    TextView txt_gioDi,txt_ngayDi,txt_tongThoiGian,txt_gioDen,txt_ngayDen;
    TextView txt_tinhDi,txt_sanBayDi,txt_maMayBay,txt_mayBay,txt_soGhe,txt_tinhDen,txt_sanBayDen,txt_thongTinVe;
    //thong tinlien he
    TextView txt_tenKhachHang,txt_gmailKhachHang,txt_diaChiKhachHang,txt_sdtKhachHang;
    //Phan ve may bay
    ImageView bt_back1,bt_back2;
    // phan thong tin gui den
    SharedPreferences dataUrl,dataKhachHang;
    SharedPreferences.Editor editorDataUrl;
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang,urlVeMayBayDaBan;
    String gmailNguoiDung,matKhauNguoiDung;
    RequestQueue requestQueue;
    public DialogVeMayBayDaBan(@NonNull Context context, VeMayBayDaBan veMayBayDaBan) {
        super(context);
        this.context=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_vemaybaydaban);
        Window window =getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // Cho phép dialog hiển thị ra toàn bộ màn hình
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            params.gravity = Gravity.TOP; // Hiển thị ở phía dưới màn hình
            params.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều rộng là match parent
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
        anhxa();
        khoiTaoBanDau();
        txt_tenKhachHang.setText(veMayBayDaBan.getTenKhachHang());
        txt_sdtKhachHang.setText("SDT: 0"+String.valueOf(veMayBayDaBan.getSdtKhachHang()));
        ganThongTinChuyenBat(veMayBayDaBan);

        bt_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        bt_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
        });
    }
    private void ganThongTinChuyenBat(VeMayBayDaBan veMayBayDaBan){
        //lay thong tin di tim kiem
        String maMayBay=veMayBayDaBan.getMayBay();
        String tenTinhDi=veMayBayDaBan.getTinhDi();
        String tenTinhDen=veMayBayDaBan.getTinhDen();
        String maChuyenBay=veMayBayDaBan.getMaChuyenBay();
        //gui du lieu di
        String urlMayBayDk=urlMayBay+"?me="+"timmaybay"+"&tenMayBay="+maMayBay;
        String urldiaChiDk=urlDiaChi+"?me="+"timdiachi"+"&tenTinhDi="+tenTinhDi+"&tenTinhDen="+tenTinhDen;
        String urlVeMayBayDk=urlVeMayBay+"?me="+"timvemaybaytheoma"+"&tinhDi="+maChuyenBay+"&tinhDen="+""+"&ngayDi="+"";
        String urlKhachHangDK=urlKhachHang+"?me="+"timkhachhang"+"&gmail="+gmailNguoiDung+"&matKhau="+matKhauNguoiDung;

        // tao danh sach de luu thong tinve
        ArrayList<DiaChi> danhSachDiaChi=new ArrayList<>();
        ArrayList<MayBay> danhSachMayBay=new ArrayList<>();
        ArrayList<VeMayBay> danhSachVeMayBay=new ArrayList<>();
        ArrayList<KhachHang>danhSachKhachHang=new ArrayList<>();
        //lay DỮ LIỆU ĐỊA CHỈ
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
                        txt_tinhDi.setText(veMayBayDaBan.getTinhDi()+" - "+ maTinhDi);
                        txt_sanBayDi.setText(tenSanBayDi);
                        txt_tinhDen.setText(veMayBayDaBan.getTinhDen()+" - "+maTinhDen);
                        txt_sanBayDen.setText(tenSanBayDen);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "Lỗi Hệ Thống!", Toast.LENGTH_SHORT).show();
                    }
                });
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
                        txt_mayBay.setText(veMayBayDaBan.getMayBay());
                        txt_soGhe.setText("Ghế: "+veMayBayDaBan.getSoGhe());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "Lỗi Mạng", Toast.LENGTH_SHORT).show();
                    }
                });
        JsonArrayRequest jsonArrayRequestVeMayBay=new JsonArrayRequest(Request.Method.GET, urlVeMayBayDk, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        danhSachVeMayBay.clear();
                        Toast.makeText(context, jsonArray.length()+"", Toast.LENGTH_SHORT).show();
                        for(int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject object=jsonArray.getJSONObject(i);
                                int id=object.getInt("Id");
                                String maChuyenBay = object.getString("MaChuyenBay");
                                String maMayBay = object.getString("MaMayBay");
                                String tenTinhDi = object.getString("TenTinhDi");
                                String tenTinhDen = object.getString("TenTinhDen");
                                //lấy thời gian với kiểu String
                                String strThoiGianDi = object.getString("ThoiGianDi");
                                String strThoiGianDen = object.getString("ThoiGianDen");
                                //chuyen string sang calender
                                SimpleDateFormat sfd =new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
                                danhSachVeMayBay.add(new VeMayBay(id,maChuyenBay,maMayBay,tenTinhDi,tenTinhDen,thoiGianDi,thoiGianDen,soGhe,giaVe,thongTinChiTiet));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        VeMayBay veMayBay=danhSachVeMayBay.get(0);
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        //LAY DƯ LIỆU NGƯỜI DÙNG
        JsonArrayRequest jsonArrayRequestNguoiDung=new JsonArrayRequest(Request.Method.GET, urlKhachHangDK, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        danhSachKhachHang.clear();
                        try {
                            JSONObject object=jsonArray.getJSONObject(0);
                            KhachHang khachHang=new KhachHang(object.getInt("Id"),object.getString("HoVaTen"),object.getString("Gmail"),object.getString("MatKhau"),object.getString("DiaChi"),object.getInt("SoDienThoai"),object.getInt("CCCD"));
                            //txt_tenKhachHang.setText(khachHang.getHoVaTen());
                           // txt_sdtKhachHang.setText("SDT: 0"+String.valueOf(khachHang.getSoDienThoai()));
                            txt_gmailKhachHang.setText("Gmail: "+khachHang.getGmail());
                            txt_diaChiKhachHang.setText("Địa chỉ: "+khachHang.getDiaChi());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });

        requestQueue.add(jsonArrayRequestMayBay);
        requestQueue.add(jsonArrayRequest);
        requestQueue.add(jsonArrayRequestNguoiDung);
        requestQueue.add(jsonArrayRequestVeMayBay);
        float giave=(float) veMayBayDaBan.getGiaVe();
        DecimalFormat dfgiatien=new DecimalFormat("#,###đ");
        String giaVe=dfgiatien.format(giave);
        txt_giaVe.setText(maChuyenBay);
    }
    private void khoiTaoBanDau(){
        requestQueue= Volley.newRequestQueue(context);
        //lay thong tin url
        dataUrl=context.getSharedPreferences("dataUrl",Context.MODE_PRIVATE);
        urlDiaChi=dataUrl.getString("urlDiaChi","");
        urlVeMayBay=dataUrl.getString("urlVeMayBay","");
        urlMayBay=dataUrl.getString("urlMayBay","");
        urlKhachHang=dataUrl.getString("urlKhachHang","");
        urlVeMayBayDaBan=dataUrl.getString("urlVeMayBayDaBan","");
        //lay thong tin khach hang
        dataKhachHang=context.getSharedPreferences("dataKhachHang",Context.MODE_PRIVATE);
        gmailNguoiDung=dataKhachHang.getString("taiKhoan","");
        matKhauNguoiDung=dataKhachHang.getString("matKhau","");
    }
    private void anhxa(){
        txt_giaVe=(TextView)findViewById(R.id.txt_DAthanhtoan_vemaybay_giave);
        txt_gioDi=(TextView)findViewById(R.id.txt_DAthanhtoan_giodi);
        txt_ngayDi=(TextView)findViewById(R.id.txt_DAthanhtoan_ngaydi);
        txt_tongThoiGian=(TextView)findViewById(R.id.txt_DAthanhtoan_tongthoigian);
        txt_gioDen=(TextView)findViewById(R.id.txt_DAthanhtoan_giodien);
        txt_ngayDen=(TextView)findViewById(R.id.txt_DAthanhtoan_ngayden);
        //PHAN 2
        txt_tinhDi=(TextView)findViewById(R.id.txt_DAthanhtoan_tinhdi);
        txt_sanBayDi=(TextView)findViewById(R.id.txt_DAthanhtoan_sanbaydi);
        txt_maMayBay=(TextView)findViewById(R.id.txt_DAthanhtoan_mamybay);
        txt_mayBay=(TextView)findViewById(R.id.txt_DAthanhtoan_maybay);
        txt_soGhe=(TextView)findViewById(R.id.txt_DAthanhtoan_soghe);
        txt_tinhDen=(TextView)findViewById(R.id.txt_DAthanhtoan_tinhden);
        txt_sanBayDen=(TextView)findViewById(R.id.txt_DAthanhtoan_sanbayden);
        txt_thongTinVe=(TextView)findViewById(R.id.txt_DAthanhtoan_thongtinve);
        //thong tinlien he
        txt_tenKhachHang=(TextView)findViewById(R.id.txt_DAthanhtoan_khachhang_ten);
        txt_gmailKhachHang=(TextView)findViewById(R.id.txt_DAthanhtoan_khachhang_gmail);
        txt_diaChiKhachHang=(TextView)findViewById(R.id.txt_DAthanhtoan_khachhang_diachi);
        txt_sdtKhachHang=(TextView)findViewById(R.id.txt_DAthanhtoan_khachhang_sdt);
        //Phan ve may bay
        bt_back1=(ImageView) findViewById(R.id.bt_vedaban_back);
        bt_back2=(ImageView) findViewById(R.id.bt_vedaban_back2);
    }
}
