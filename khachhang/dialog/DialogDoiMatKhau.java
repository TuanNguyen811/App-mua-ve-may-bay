package com.example.khachhang.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.example.khachhang.R;
import com.example.khachhang.doiTuong.KhachHang;

import java.util.HashMap;
import java.util.Map;

public class DialogDoiMatKhau extends Dialog {
    Context mcontext;
    KhachHang khachHang;
    ImageView bt_back;
    Button bt_xacNhan;
    EditText edt_matKhauHienTai, edt_matKhauMoi1, edt_matKhauMoi2;
    TextView txtTB_saiMatKhau,txtTB_6KyTu,txtTB_soVaChu,txtTB_khongKhop,txt_hienAn;
    String urlKhachHang;
    SharedPreferences dataUrl, dataKhachHang;
    SharedPreferences.Editor editorDataKhachHang;
    RequestQueue requestQueue;
    boolean hienMatKhau=false;
    public DialogDoiMatKhau(@NonNull Context context, KhachHang khachHang) {
        super(context);
        this.mcontext=context;
        this.khachHang=khachHang;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_doimatkhau);
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
            params.dimAmount = 0.0f;  // Không làm mờ nền
            window.setAttributes(params);
        }
        khoiTao();
        bt_xacNhan.setEnabled(false);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        bt_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_matKhauHienTai.getText().toString().isEmpty()){
                    Toast.makeText(mcontext, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    doiMatKhau(urlKhachHang,"doiMatKhau");
                }
            }
        });
        txt_hienAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hienMatKhau){
                    edt_matKhauMoi1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edt_matKhauMoi2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edt_matKhauHienTai.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hienMatKhau=false;
                    txt_hienAn.setText("Hiện");
                }else{
                    edt_matKhauMoi1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edt_matKhauMoi2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edt_matKhauHienTai.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hienMatKhau=true;
                    txt_hienAn.setText("Ẩn");
                }
            }
        });
        edt_matKhauMoi2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(edt_matKhauMoi1.getText().toString())){
                    bt_xacNhan.setEnabled(false);
                    txtTB_khongKhop.setVisibility(View.VISIBLE);
                }else{
                    bt_xacNhan.setEnabled(kiemTraDieuKien(s.toString()));
                    txtTB_khongKhop.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edt_matKhauMoi1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_xacNhan.setEnabled(kiemTraDieuKien(s.toString()));//hien thi nut xac nhan
                if(kiemTraDieuKien(s.toString())){
                    if(!s.toString().equals(edt_matKhauMoi2.getText().toString())){
                        bt_xacNhan.setEnabled(false);
                        txtTB_khongKhop.setVisibility(View.VISIBLE);
                    }else{
                        txtTB_khongKhop.setVisibility(View.GONE);
                    }
                }else{
                    txtTB_khongKhop.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private boolean kiemTraDieuKien(String password){
        if (password.length() < 6 || password.length() > 32) {
            txtTB_soVaChu.setVisibility(View.GONE);
            txtTB_6KyTu.setVisibility(View.VISIBLE);
            return false;
        }else{
            txtTB_6KyTu.setVisibility(View.GONE);
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
            txtTB_soVaChu.setVisibility(View.VISIBLE);
        }else{
            txtTB_soVaChu.setVisibility(View.GONE);
        }
        return dieuKien1 && dieuKien2;
    }
    private void doiMatKhau(String url, String IPA){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("1")){
                            editorDataKhachHang.putString("matKhau",edt_matKhauMoi2.getText().toString());
                            editorDataKhachHang.commit();
                            Toast.makeText(mcontext, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else if(s.equals("2")){
                            txtTB_saiMatKhau.setVisibility(View.VISIBLE);
                        }else if(s.equals("0")){
                            Toast.makeText(mcontext, "Thất bại, xin thử lại sau!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mcontext, "Thất bại, xin thử lại sau!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(mcontext, "Mất kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
            }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>parmas=new HashMap<>();
                String matKhau=edt_matKhauHienTai.getText().toString();
                String matKhauMoi=edt_matKhauMoi1.getText().toString();
                String id=String.valueOf(khachHang.getId());
                parmas.put("ME",IPA.trim());
                parmas.put("id",id.trim());
                parmas.put("matKhau",matKhau.trim());
                parmas.put("matKhauMoi",matKhauMoi.trim());

                parmas.put("hoVaTen"," ");
                parmas.put("gmail"," ");
                parmas.put("diaChi"," ");
                parmas.put("soDienThoai"," ");
                parmas.put("cCCD","0".trim());

                return parmas;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void khoiTao(){
        dataUrl=mcontext.getSharedPreferences("dataUrl",Context.MODE_PRIVATE);
        dataKhachHang=mcontext.getSharedPreferences("dataKhachHang",Context.MODE_PRIVATE);
        urlKhachHang=dataUrl.getString("urlKhachHang","");
        editorDataKhachHang=dataKhachHang.edit();

        requestQueue= Volley.newRequestQueue(mcontext);
        bt_back=(ImageView) findViewById(R.id.bt_dialogdoimatkhau_back);
        bt_xacNhan=(Button) findViewById(R.id.bt_dialogdoimatkhau_xacnhan);
        edt_matKhauHienTai=(EditText)findViewById(R.id.edt_dialogdoimatkhau_matkhauhientai);
        edt_matKhauMoi2=(EditText) findViewById(R.id.edt_dialogdoimatkhau_matkhaumoi2);
        edt_matKhauMoi1=(EditText) findViewById(R.id.edt_dialogdoimatkhau_matkhaumoi1);

        txt_hienAn=(TextView)findViewById(R.id.txt_dialogdoimatkhau_hienan);
        txtTB_saiMatKhau=(TextView) findViewById(R.id.txtTB_dialogdoimatkhau_mkkhongchinhxac);
        txtTB_6KyTu=(TextView) findViewById(R.id.txtTB_dialogdoimatkhau_mk6kytu);
        txtTB_soVaChu=(TextView) findViewById(R.id.txtTB_dialogdoimatkhau_mkkytudacbiet);
        txtTB_khongKhop=(TextView) findViewById(R.id.txtTB_dialogdoimatkhau_mkkokhop);
    }
}
