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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.MucActivity.Activity_DangKy;
import com.example.khachhang.MucActivity.Activity_TrangCaNhan;
import com.example.khachhang.MucActivity.MainActivity;
import com.example.khachhang.R;
import com.example.khachhang.doiTuong.KhachHang;

import java.util.HashMap;
import java.util.Map;

public class DialogSuaThongTinCaNhan extends Dialog {
    private Context mcontext;
    KhachHang khachHang;
    String urlKhachHang;
    SharedPreferences dataUrl,dataKhachHang;
    SharedPreferences.Editor editorDataKhachHang;
    RequestQueue requestQueue;
    ImageView bt_back;
    TextView txt_gmail;
    EditText edt_ten,edt_diaChi,edt_sdt;
    Button bt_capNhat;
    String gmail,ten,diaChi,sdt,matKhau,id;
    public DialogSuaThongTinCaNhan(@NonNull Context context, KhachHang khachHang) {
        super(context);
        this.mcontext=context;
        this.khachHang=khachHang;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_suathongtintaikhoan);
        //setCancelable(false);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // thiet lap thuoc tinh de hien thi dialog
        Window window =getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // Cho phép dialog hiển thị ra toàn bộ màn hình
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            params.gravity = Gravity.TOP; // Hiển thị ở phía dưới màn hình
            params.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều rộng là match parent
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.dimAmount = 0.0f;
            window.setAttributes(params);
        }
        khoiTao();
        id=String.valueOf(khachHang.getId());
        gmail=khachHang.getGmail();
        ten=khachHang.getHoVaTen();
        diaChi=khachHang.getDiaChi();
        sdt="0"+khachHang.getSoDienThoai();
        txt_gmail.setText(gmail.toString());
        edt_ten.setText(ten.toString());
        edt_diaChi.setText(diaChi.toString());
        edt_sdt.setText(sdt.toString());
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        bt_capNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_ten.toString().isEmpty()||edt_sdt.toString().isEmpty()||edt_diaChi.toString().isEmpty()){
                    Toast.makeText(mcontext, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mcontext, "Đang cập nhật", Toast.LENGTH_SHORT).show();
                    capNhatThongTinNguoiDung(urlKhachHang,"update");
                }
            }
        });
    };

    private void capNhatThongTinNguoiDung(String url, String ipa){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("1")){
                            Toast.makeText(mcontext, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            ten=edt_ten.getText().toString();
                            editorDataKhachHang.putString("tenKhachHang",ten);
                            editorDataKhachHang.commit();
                            mcontext.startActivity(new Intent(mcontext, Activity_TrangCaNhan.class));
                        }else if (s.equals("0")) {
                            Toast.makeText(mcontext, "Thất bại!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mcontext, "Lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(mcontext, "Mất kết nối!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parmas=new HashMap<>();
                ten=edt_ten.getText().toString();
                diaChi=edt_diaChi.getText().toString();
                sdt=edt_sdt.getText().toString();
                // gửi dữ liệu di
                parmas.put("ME",ipa.trim());
                parmas.put("id",id.trim());
                parmas.put("hoVaTen",ten.trim());
                parmas.put("gmail",gmail.trim());
                parmas.put("matKhau",matKhau.trim());
                parmas.put("diaChi",diaChi.trim());
                parmas.put("soDienThoai",("0"+sdt).trim());
                parmas.put("cCCD","0".trim());
                return parmas;
            }
        };
        requestQueue.add(stringRequest);// tao request
    }
    private void khoiTao(){
        dataUrl=mcontext.getSharedPreferences("dataUrl",Context.MODE_PRIVATE);
        urlKhachHang=dataUrl.getString("urlKhachHang","");
        dataKhachHang=mcontext.getSharedPreferences("dataKhachHang",Context.MODE_PRIVATE);
        matKhau=dataKhachHang.getString("matKhau","");
        editorDataKhachHang=dataKhachHang.edit();

        requestQueue= Volley.newRequestQueue(mcontext);
        bt_back=(ImageView)findViewById(R.id.bt_dialogsuathongtinkhachhang_back);
        txt_gmail=(TextView)findViewById(R.id.edt_dialogsuathongtinkhachhang_gmail);
        edt_ten=(EditText)findViewById(R.id.edt_dialogsuathongtinkhachhang_ten);
        edt_diaChi=(EditText)findViewById(R.id.edt_dialogsuathongtinkhachhang_diachi);
        edt_sdt=(EditText)findViewById(R.id.edt_dialogsuathongtinkhachhang_sodienthoai);
        bt_capNhat=(Button) findViewById(R.id.bt_dialogsuathongtinkhachhang_xacnhan);
    }
}
