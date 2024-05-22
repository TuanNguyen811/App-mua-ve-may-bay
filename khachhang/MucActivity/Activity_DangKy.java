package com.example.khachhang.MucActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.R;
import com.example.khachhang.dialog.DialogDangNhap;
import com.example.khachhang.doiTuong.KhachHang;
import com.example.khachhang.my_interfave.InterfaceKhachHang;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class Activity_DangKy extends AppCompatActivity {
    Boolean daDangNhap;
    //Khai báo phần layout
    ConstraintLayout layout1,layout2,layout3,layout4;
    EditText edt_ten,edt_cccd,edt_gmail,edt_sdt,edt_taiKhoan,edt_matKhau,edt_diaChi;
    TextView txt_hienAn, txt_6kyTu,txt_chuVaSo;
    boolean hienMatKhau=false;
    Button bt_nhap, bt_dangNhap;
    ImageView bt_sua;
    //khai báo phần mạng
    RequestQueue requestQueue;
   // String urlKhachHang="http:/192.168.231.154/CSDLMayBay/khachhang.php";
    //luwu nguoi dfung
    SharedPreferences dataKhachHang;
    SharedPreferences.Editor editorDataKhachHang;
    String urlVeMayBay;
    String urlDiaChi;
    String urlMayBay;
    String urlKhachHang;
    SharedPreferences dataUrl;
    SharedPreferences.Editor editorDataUrl;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InterfaceKhachHang interfaceKhachHang=new InterfaceKhachHang() {
            @Override
            public void taiDuLieuKhachHang(String url, KhachHang khachHang, String Me) {
            }
            @Override
            public void layDuLieuKhachHang(String url) {
            }
            @Override
            public void timDuLieuKhachHang(String url, int id) {
            }
        };
        thietLapBanDau();
        txt_chuVaSo.setVisibility(View.GONE);
        txt_6kyTu.setVisibility(View.GONE);
        bt_dangNhap.setEnabled(false);
        // khoi tao ban dau
        bt_nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_ten.getText().toString().isEmpty()||edt_gmail.getText().toString().isEmpty()||edt_sdt.getText().toString().isEmpty()){
                    Toast.makeText(Activity_DangKy.this, "Vui lòng điền đầy đủ nội dung", Toast.LENGTH_SHORT).show();
                }else{
                    layout1.setVisibility(View.GONE);
                    layout4.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    layout3.setVisibility(View.VISIBLE);
                    edt_taiKhoan.setText(edt_gmail.getText().toString()+"");
                }
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_gmail.setText(edt_taiKhoan.getText().toString()+"");
                layout1.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.GONE);
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_ten.getText().toString().isEmpty()||edt_gmail.getText().toString().isEmpty()||edt_sdt.getText().toString().isEmpty()||edt_diaChi.getText().toString().isEmpty()){
                    Toast.makeText(Activity_DangKy.this, "Vui lòng điền đầy đủ nội dung", Toast.LENGTH_SHORT).show();
                }else{
                    edt_taiKhoan.setText(edt_gmail.getText().toString()+"");
                    layout1.setVisibility(View.GONE);
                    layout4.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    layout3.setVisibility(View.VISIBLE);
                }
            }
        });
        bt_dangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_taiKhoan.getText().toString().isEmpty()||edt_matKhau.getText().toString().isEmpty()){
                    Toast.makeText(Activity_DangKy.this, "Vui lòng điền đầy đủ nội dung", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Activity_DangKy.this, "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show();
                    int id=0;
                    String ten=edt_ten.getText().toString();
                    String gmail=edt_taiKhoan.getText().toString();
                    String matKhai=edt_matKhau.getText().toString();
                    String diaChi=edt_diaChi.getText().toString();
                    int sDT=Integer.parseInt(edt_sdt.getText().toString());
                    int cccd= 0;
                    KhachHang khachHang=new KhachHang(id,ten,gmail,matKhai,diaChi,sDT,cccd);
                    taiDuLieuKhachHang(urlKhachHang,khachHang,"insert");
                }
            }
        });
        txt_hienAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hienMatKhau){
                    edt_matKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hienMatKhau=false;
                    txt_hienAn.setText("Hiện");
                }else{
                    edt_matKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hienMatKhau=true;
                    txt_hienAn.setText("Ẩn");
                }
            }
        });
        edt_matKhau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_dangNhap.setEnabled(kiemTraDieuKien(s.toString()));//hien thi nut xac nhan
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private boolean kiemTraDieuKien(String password){
        if (password.length() < 6 || password.length() > 32) {
            txt_chuVaSo.setVisibility(View.GONE);
            txt_6kyTu.setVisibility(View.VISIBLE);
            return false;
        }else{
            txt_6kyTu.setVisibility(View.GONE);
        }
        boolean dieuKien1=false;
        boolean dieuKien2=false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {//la chu
                dieuKien1 = true;
            }else if (Character.isDigit(c)) {//la so
                dieuKien2 = true;
            }
        }
        if(!dieuKien1||!dieuKien2){
            txt_chuVaSo.setVisibility(View.VISIBLE);
        }else{
            txt_chuVaSo.setVisibility(View.GONE);
        }
        return dieuKien1 && dieuKien2;
    }
    private void taiDuLieuKhachHang(String url,KhachHang khachHang,String ME){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("1")){
                            Toast.makeText(Activity_DangKy.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            editorDataKhachHang.putString("taiKhoan",edt_taiKhoan.getText().toString());
                            editorDataKhachHang.putString("matKhau",edt_matKhau.getText().toString());
                            editorDataKhachHang.putBoolean("dangNhap",true);
                            editorDataKhachHang.putString("tenKhachHang",edt_ten.getText().toString());
                            editorDataKhachHang.commit();
                            intent=new Intent(Activity_DangKy.this,MainActivity.class);
                            startActivity(intent);
                        }else if (s.equals("0")) {
                            Toast.makeText(Activity_DangKy.this, "Đăng ký thất bại, thử lại sau!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Activity_DangKy.this, "Gmai đã có trên hệ thống!", Toast.LENGTH_SHORT).show();
                        }
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
                                }}).show();                }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parmas=new HashMap<>();
                //lay du lieu ra
                String id=String.valueOf(khachHang.getId());
                String ten=khachHang.getHoVaTen();
                String gmail=khachHang.getGmail();
                String matKhau=khachHang.getMatKhau();
                String diaChi=khachHang.getDiaChi();
                String soDienThoai=String.valueOf(khachHang.getSoDienThoai());
                String cCCD=String.valueOf(khachHang.getcCCD());
                // gửi dữ liệu di
                parmas.put("ME",ME.trim());
                parmas.put("id",id.trim());
                parmas.put("hoVaTen",ten.trim());
                parmas.put("gmail",gmail.trim());
                parmas.put("matKhau",matKhau.trim());
                parmas.put("diaChi",diaChi.trim());
                parmas.put("soDienThoai",("0"+soDienThoai).trim());
                parmas.put("cCCD",cCCD.trim());
                return parmas;
            }
        };
        requestQueue.add(stringRequest);// tao request
    }
    private void anhXa(){
        layout1=(ConstraintLayout)findViewById(R.id.layout_dangky_layout1);
        layout2=(ConstraintLayout)findViewById(R.id.layout_dangky_layout2);
        layout3=(ConstraintLayout)findViewById(R.id.layout_dangky_layout3);
        layout4=(ConstraintLayout)findViewById(R.id.layout_dangky_layout4);
        edt_ten=(EditText)findViewById(R.id.edt_dangky_ten);
        edt_cccd=(EditText)findViewById(R.id.edt_dangky_cccd);
        edt_gmail=(EditText)findViewById(R.id.edt_dangky_gmail);
        edt_sdt=(EditText)findViewById(R.id.edt_dangky_sdt);
        edt_taiKhoan=(EditText)findViewById(R.id.edt_dangky_gmaidangnhap);
        edt_matKhau=(EditText)findViewById(R.id.edt_dangky_matkhau);
        edt_diaChi=(EditText)findViewById(R.id.edt_dangky_diaChi);
        bt_nhap=(Button)findViewById(R.id.bt_dangky_nhap);
        bt_dangNhap=(Button)findViewById(R.id.bt_dangky_dangnhap);
        bt_sua=(ImageView)findViewById(R.id.btim_dangky_suathongtin);

        txt_hienAn=(TextView)findViewById(R.id.txt_dangky_hienAn);
        txt_6kyTu=(TextView)findViewById(R.id.txtTB_dangky_6kytu);
        txt_chuVaSo=(TextView) findViewById(R.id.txtTB_dangky_chuvaso);
    }
    private void thietLapBanDau(){
        anhXa();
        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        editorDataUrl=dataUrl.edit();
        urlDiaChi=dataUrl.getString("urlDiaChi"," ");
        urlVeMayBay=dataUrl.getString("urlVeMayBay"," ");
        urlMayBay=dataUrl.getString("urlMayBay"," ");
        urlKhachHang=dataUrl.getString("urlKhachHang"," ");

        dataKhachHang=getSharedPreferences("dataKhachHang",MODE_PRIVATE);
        editorDataKhachHang = dataKhachHang.edit();
        daDangNhap=dataKhachHang.getBoolean("dangNhap",false);

       /* editorDataKhachHang.putString("taiKhoan","");
        editorDataKhachHang.putString("matKhau","");
        editorDataKhachHang.putBoolean("dangNhap",false);
        editorDataKhachHang.commit();
        */

        requestQueue= Volley.newRequestQueue(this);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);

    }
}