package com.example.appmaybay1.MucActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.example.appmaybay1.R;
import com.example.appmaybay1.doiTuong.DiaChi;
import com.example.appmaybay1.doiTuong.MayBay;
import com.example.appmaybay1.doiTuong.VeMayBay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Activity_NhapVeMayBay extends AppCompatActivity {

    Button btxt_thoiGianDen,btxt_thoiGianDi,btxt_ngayLapLai;
    TextView txt_tieuDe;
    EditText edt_maChuyenBay,edt_soGhe,edt_giaVe;
    EditText edt_noiDungThem;
    Button bt_xacNhan,bt_huy;
    VeMayBay veMayBayNhan;
    //sử dụng cho địa chỉ
    Spinner Sedt_tinhDen,Sedt_tinhDi,Sedt_mayBay;
    ArrayList<DiaChi> danhSachDiaChi;
    ArrayList<MayBay> danhSachMayBay;
    String tenDiaChiDi,tenDiaChiDen,tenMayBay;
    String[] danhSachTenDiaChi,danhSachTenMayBay;
    int[] danhSachChoNgoi;

    ArrayAdapter<String> adapterDiaChi;
    ArrayAdapter<String>adapterMayBay;


    SharedPreferences dataUrl;
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang,urlVeMayBayDaBan;

    String ME2;
    Calendar thoiGianDi=Calendar.getInstance();
    Calendar thoiGianDen=Calendar.getInstance();
    Calendar thoiGianLapLaii=Calendar.getInstance();
    int soNgayLapLai=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nhap_ve_may_bay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide();
        anhXa();
        ME2="oke";
        //đọc dưx olieu
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            veMayBayNhan=(VeMayBay) bundle.getSerializable("dataThemVeMayBay");
            String ME=bundle.getString("dataME");
            if(ME!=null) {
                if (ME.equals("insert")) {
                    ME2=ME;
                    Toast.makeText(this, ME, Toast.LENGTH_SHORT).show();
                    txt_tieuDe.setText("Thêm chuyến bay");
                    bt_xacNhan.setText("THÊM");
                } else if (ME.equals("update")) {
                    ME2=ME;
                    Toast.makeText(this, ME, Toast.LENGTH_SHORT).show();
                    txt_tieuDe.setText("Sửa chuyến bay");
                    bt_xacNhan.setText("Sửa");
                } else if (ME.equals("delete")) {
                    ME2=ME;
                    Toast.makeText(this, ME, Toast.LENGTH_SHORT).show();
                    txt_tieuDe.setText("Xóa chuyến bay:"+veMayBayNhan.getId());
                    bt_xacNhan.setText("Xóa");
                }

                thoiGianDen=veMayBayNhan.getThoiGianDen();
                thoiGianDi=veMayBayNhan.getThoiGianDi();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String StringThoiGianDi=sdf.format(thoiGianDi.getTime());
                String StringThoiGianDen=sdf.format(thoiGianDen.getTime());
                btxt_thoiGianDi.setText(StringThoiGianDi);
                btxt_thoiGianDen.setText(StringThoiGianDen);

                edt_maChuyenBay.setText(veMayBayNhan.getMaChuyenBay());
                edt_giaVe.setText(String.valueOf(veMayBayNhan.getGiaVe()));
                if(!veMayBayNhan.getThongTinThem().toString().isEmpty()){
                    edt_noiDungThem.setText(veMayBayNhan.getThongTinThem());
                }
            }
        }
       // lấy dữ liệu danh sachs
        //SPINNER
        layDuLieuDiaChi(urlDiaChi);
        layDuLieuMayBay(urlMayBay);
        //THỜI GIAN

        btxt_thoiGianDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y=thoiGianDi.get(Calendar.YEAR);
                int m=thoiGianDi.get(Calendar.MONTH);
                int d=thoiGianDi.get(Calendar.DATE);
                DatePickerDialog datePickerDialog= new DatePickerDialog(Activity_NhapVeMayBay.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,final int year,final int month,final int dayOfMonth) {
                        thoiGianDi.set(year,month,dayOfMonth);
                        TimePickerDialog timePickerDialog =new TimePickerDialog(Activity_NhapVeMayBay.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                thoiGianDi.set(year,month,dayOfMonth,hourOfDay,minute);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                btxt_thoiGianDi.setText(simpleDateFormat.format(thoiGianDi.getTime()));
                            }
                        },0,0,true);
                        timePickerDialog.show();
                    }
                },y,m,d);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        btxt_ngayLapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y=thoiGianDi.get(Calendar.YEAR);
                int m=thoiGianDi.get(Calendar.MONTH);
                int d=thoiGianDi.get(Calendar.DATE);
                DatePickerDialog datePickerDialog=new DatePickerDialog(Activity_NhapVeMayBay.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        thoiGianLapLaii.set(year, month, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        btxt_ngayLapLai.setText(simpleDateFormat.format(thoiGianLapLaii.getTime()));
                        // Chuyển thời gian thành LocalDate
                        LocalDate localThoiGianDi = LocalDate.of(thoiGianDi.get(Calendar.YEAR), thoiGianDi.get(Calendar.MONTH) + 1, thoiGianDi.get(Calendar.DATE));
                        LocalDate localThoiGianLapLaii = LocalDate.of(year, month + 1, dayOfMonth);
                        // Tính khoảng cách giữa hai ngày
                        long soNgayLapLaiL = ChronoUnit.DAYS.between(localThoiGianDi, localThoiGianLapLaii);
                        // Hiển thị số ngày lặp lại
                        soNgayLapLai = (int) soNgayLapLaiL;
                        Toast.makeText(Activity_NhapVeMayBay.this, "Số ngày lặp lại: " + soNgayLapLai, Toast.LENGTH_SHORT).show();
                    }
                },y,m,d);
                datePickerDialog.getDatePicker().setMinDate(thoiGianDi.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        btxt_thoiGianDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y=thoiGianDen.get(Calendar.YEAR);
                int m=thoiGianDen.get(Calendar.MONTH);
                int d=thoiGianDen.get(Calendar.DATE);
                int h=thoiGianDen.get(Calendar.HOUR_OF_DAY);
                int mm=thoiGianDen.get(Calendar.MINUTE);
                DatePickerDialog datePickerDialog= new DatePickerDialog(Activity_NhapVeMayBay.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year,final int month,final int dayOfMonth) {
                        thoiGianDen.set(year,month,dayOfMonth);
                        TimePickerDialog timePickerDialog =new TimePickerDialog(Activity_NhapVeMayBay.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                thoiGianDen.set(year,month,dayOfMonth,hourOfDay,minute);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                btxt_thoiGianDen.setText(simpleDateFormat.format(thoiGianDen.getTime()));
                            }
                        },h,mm,true);
                        timePickerDialog.show();
                    }
                },y,m,d);
                datePickerDialog.getDatePicker().setMinDate(thoiGianDi.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        bt_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=0;
                i=veMayBayNhan.getId();
                ArrayList<VeMayBay> danhSachVeGuiDi=new ArrayList<>();
                    if(soNgayLapLai>0){
                        thoiGianDi.add(Calendar.DATE,-1);
                        thoiGianDen.add(Calendar.DATE,-1);
                        for(int j=1;j<=soNgayLapLai;j++){
                            thoiGianDi.add(Calendar.DATE,1);
                            thoiGianDen.add(Calendar.DATE,1);
                            VeMayBay veMayBay=new VeMayBay(i,edt_maChuyenBay.getText().toString()+String.valueOf(j)+"",tenMayBay,
                                    tenDiaChiDi,tenDiaChiDen, thoiGianDi,
                                    thoiGianDen, Integer.parseInt(edt_soGhe.getText().toString()),
                                    Integer.parseInt(edt_giaVe.getText().toString()), edt_noiDungThem.getText().toString());
                            taiDuLieuVeMayBay(urlVeMayBay,veMayBay,ME2);
                            startActivity(new Intent(Activity_NhapVeMayBay.this,MainActivity_trangchu.class));
                        }
                    }else{
                        VeMayBay veMayBay=new VeMayBay(i,edt_maChuyenBay.getText().toString(),tenMayBay,
                                tenDiaChiDi,tenDiaChiDen, thoiGianDi,
                                thoiGianDen, Integer.parseInt(edt_soGhe.getText().toString()),
                                Integer.parseInt(edt_giaVe.getText().toString()), edt_noiDungThem.getText().toString());
                        taiDuLieuVeMayBay(urlVeMayBay,veMayBay,ME2);
                        startActivity(new Intent(Activity_NhapVeMayBay.this,MainActivity_trangchu.class));
                    }
                }
        });

    }

    public void layDuLieuDiaChi(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        danhSachDiaChi=new ArrayList<>();
                        danhSachTenDiaChi=new String[jsonArray.length()];
                        for (int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject object=jsonArray.getJSONObject(i);
                                danhSachDiaChi.add(new DiaChi(object.getInt("Id"),object.getString("TenDiaDiem"),object.getString("MaDiaDiem"),object.getString("TenSanBay")));
                                DiaChi diaChi;
                                diaChi=danhSachDiaChi.get(i);
                                danhSachTenDiaChi[i]=diaChi.getTenDiaChi();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //sét adapter
                        adapterDiaChi = new ArrayAdapter<>(Activity_NhapVeMayBay.this, android.R.layout.simple_spinner_item, danhSachTenDiaChi);
                        adapterDiaChi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        Sedt_tinhDi.setAdapter(adapterDiaChi);
                        Sedt_tinhDen.setAdapter(adapterDiaChi);

                        Intent intent=getIntent();
                        Bundle bundle=intent.getExtras();
                        String vitriDi="";
                        String vitriDen="";
                        if(bundle!=null){
                            VeMayBay veMayBay=(VeMayBay) bundle.getSerializable("dataThemVeMayBay");
                            vitriDi=veMayBay.getTenTinhDi().toString();
                            vitriDen=veMayBay.getTenTinhDen().toString();
                        }
                        int pst_TinhDi=adapterDiaChi.getPosition(vitriDi);
                        int pst_TinhDen=adapterDiaChi.getPosition(vitriDen);
                        if(pst_TinhDen!=-1&&pst_TinhDi!=-1){
                            Toast.makeText(Activity_NhapVeMayBay.this, "OKE", Toast.LENGTH_SHORT).show();
                            Sedt_tinhDen.setSelection(pst_TinhDen);
                            Sedt_tinhDi.setSelection(pst_TinhDi);
                        }
                        Sedt_tinhDi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                tenDiaChiDi=danhSachTenDiaChi[position];
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(Activity_NhapVeMayBay.this, "Chọn địa chỉ", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Sedt_tinhDen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                tenDiaChiDen = danhSachTenDiaChi[position];
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(Activity_NhapVeMayBay.this, "Chọn địa chỉ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Activity_NhapVeMayBay.this, "Loi ket noi", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public void taiDuLieuVeMayBay(String url, VeMayBay veMayBay, String Me){
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("ThanhCong")){
                        } else if (s.equals("ThatBai")) {
                        }else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parmas=new HashMap<>();
                //Lấy dữ liệu ra
                String id=String.valueOf(veMayBay.getId());
                String maChuyenBay=veMayBay.getMaChuyenBay();
                String maMayBay=veMayBay.getMaMayBay();
                String tenTinhDi=veMayBay.getTenTinhDi();
                String tenTinhDen=veMayBay.getTenTinhDen();
                //Thòi gian

                Calendar CLDthoiGianDi=Calendar.getInstance();
                Calendar CLDthoiGianDen=Calendar.getInstance();
                CLDthoiGianDi=veMayBay.getThoiGianDi();
                CLDthoiGianDen=veMayBay.getThoiGianDen();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String thoiGianDi=sdf.format(CLDthoiGianDi.getTime());
                String thoiGianDen=sdf.format(CLDthoiGianDen.getTime());

                String soGhe=String.valueOf(veMayBay.getSoGhe());
                String giaVe=String.valueOf(veMayBay.getGiaVe());
                String thongTinThem=veMayBay.getThongTinThem();
                //gửi dữ liệu đi dưới dạng <KEY><VALUE>
                parmas.put("ME",Me.toString().trim());
                parmas.put("id",id.toString().trim());
                parmas.put("maChuyenBay",maChuyenBay.toString().trim());
                parmas.put("maMayBay",maMayBay.toString().trim());
                parmas.put("tenTinhDi",tenTinhDi.toString().trim());
                parmas.put("tenTinhDen",tenTinhDen.toString().trim());
                parmas.put("thoiGianDi",thoiGianDi.toString().trim());
                parmas.put("thoiGianDen",thoiGianDen.toString().trim());
                parmas.put("soGhe",soGhe.toString().trim());
                parmas.put("giaVe",giaVe.toString().trim());
                parmas.put("thongTinThem",thongTinThem.toString().trim());
                return parmas;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void layDuLieuMayBay(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        danhSachMayBay=new ArrayList<>();
                        danhSachTenMayBay=new String[jsonArray.length()];
                        danhSachChoNgoi=new int[jsonArray.length()];
                        for (int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject object=jsonArray.getJSONObject(i);
                                danhSachMayBay.add(new MayBay(object.getInt("Id"),object.getString("MaMayBay"),object.getString("TenMayBay"),object.getInt("SoGhe")));
                                MayBay mayBay=danhSachMayBay.get(i);
                                danhSachTenMayBay[i]=mayBay.getTenMayBay();
                                danhSachChoNgoi[i]=mayBay.getSoGhe();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        adapterMayBay = new ArrayAdapter<>(Activity_NhapVeMayBay.this, android.R.layout.simple_spinner_item, danhSachTenMayBay);
                        adapterMayBay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        Sedt_mayBay.setAdapter(adapterMayBay);

                        Intent intent=getIntent();
                        Bundle bundle=intent.getExtras();
                        String mayBAYYY="";
                        if(bundle!=null){
                            VeMayBay veMayBay=(VeMayBay) bundle.getSerializable("dataThemVeMayBay");
                            mayBAYYY=veMayBay.getMaMayBay().toString();
                        }
                        int pst_mayBayy=adapterMayBay.getPosition(mayBAYYY);
                        if(pst_mayBayy!=-1){
                            Sedt_mayBay.setSelection(pst_mayBayy);
                        }
                        Sedt_mayBay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                tenMayBay=danhSachTenMayBay[position];
                                String GheNgoi=danhSachChoNgoi[position]+"";
                                edt_soGhe.setText(GheNgoi);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(Activity_NhapVeMayBay.this, "Chọn máy bay", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Activity_NhapVeMayBay.this, "Loi ket noi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void anhXa() {
        btxt_ngayLapLai=(Button)findViewById(R.id.bt_nhapmaybay_laplaingay);
        btxt_thoiGianDen=(Button)findViewById(R.id.btedt_nhapmaybay_thoigianden);
        btxt_thoiGianDi=(Button)findViewById(R.id.btedt_nhapmaybay_thoigiandi);
        txt_tieuDe=(TextView)findViewById(R.id.txt_nhapmaybay_tieude);
        Sedt_tinhDen=(Spinner) findViewById(R.id.Sedt_nhapmaybay_tinhDen);
        Sedt_tinhDi=(Spinner) findViewById(R.id.Sedt_nhapmaybay_tinhDi);
        Sedt_mayBay=(Spinner) findViewById(R.id.Sedt_nhapmaybay_mayBay);
        edt_maChuyenBay=(EditText)findViewById(R.id.edt_nhapmaybay_machuyenbayyy);
        edt_soGhe=(EditText)findViewById(R.id.edt_nhapmaybay_soghee);
        edt_giaVe=(EditText)findViewById(R.id.edt_nhapmaybay_giave);
        bt_xacNhan=(Button) findViewById(R.id.bt_nhapmaybay_xacnhan);
        edt_noiDungThem=(EditText)findViewById(R.id.edt_nhapmaybay_noidungthem);



        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        urlDiaChi=dataUrl.getString("urlDiaChi","");
        urlVeMayBay=dataUrl.getString("urlVeMayBay","");
        urlMayBay=dataUrl.getString("urlMayBay","");
        urlKhachHang=dataUrl.getString("urlKhachHang","");
        urlVeMayBayDaBan=dataUrl.getString("urlVeMayBayDaBan","");
    }
}