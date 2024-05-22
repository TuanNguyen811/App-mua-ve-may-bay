package com.example.khachhang.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.MucActivity.Activity_DangKy;
import com.example.khachhang.MucActivity.Activity_TrangCaNhan;
import com.example.khachhang.MucActivity.MainActivity;
import com.example.khachhang.R;
import com.example.khachhang.doiTuong.KhachHang;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringJoiner;

public class DialogDangNhap extends Dialog {
    private Context mcontext;
    String urlVeMayBay;
    String urlDiaChi;
    String urlMayBay;
    String urlKhachHang;
    SharedPreferences dataUrl,dataKhachHang;
    SharedPreferences.Editor editorDataKhachHang;
    SharedPreferences.Editor editorDataUrl;
    TextView txt_thongBao,txt_hienAn;
    RequestQueue requestQueue;
    boolean hienMatKhau=false;
    public DialogDangNhap(@NonNull Context context) {
        super(context);
        this.mcontext=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_dangnhap);
        setCancelable(false);
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
        khoiTaoBanDau();

        EditText edt_tenDangNhap=(EditText) findViewById(R.id.edt_dialog_dangnhap_tenTK);
        EditText edt_matKhau=(EditText)  findViewById(R.id.edt_dialog_dangnhap_matkhau);
        Button bt_dangNhap=(Button) findViewById(R.id.bt_dialog_dangnhap_dangnhap);
        TextView bt_dangKy=(TextView) findViewById(R.id.bt_dialog_dangnhap_dangky);
        txt_hienAn=(TextView)findViewById(R.id.txt_dialog_dangnhap_hienAn);

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

        txt_thongBao=(TextView)findViewById(R.id.txt_dialog_dangnhap_thongbaodangnhap);
        txt_thongBao.setVisibility(View.GONE);
        bt_dangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmail=edt_tenDangNhap.getText().toString();
                String matKhau=edt_matKhau.getText().toString();
                if(gmail.isEmpty()||matKhau.isEmpty()){
                    Toast.makeText(context, "Vui lòng nhập đầy đủ nội dung!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mcontext, "Đang tải...", Toast.LENGTH_SHORT).show();
                    kiemTraVaDangNhap(urlKhachHang,"kiemtravadangnhap",gmail,matKhau);
                }
            }
        });
        bt_dangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
    }
    private void kiemTraVaDangNhap(String url,String me,String gmail,String matKhau){
        url+="?me="+me+"&gmail="+gmail+"&matKhau="+matKhau;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        if(jsonArray.length()>0){
                            try {
                                JSONObject object=jsonArray.getJSONObject(0);
                                KhachHang khachHang=new KhachHang(object.getInt("Id"),object.getString("HoVaTen"),object.getString("Gmail"),object.getString("MatKhau"),object.getString("DiaChi"),object.getInt("SoDienThoai"),object.getInt("CCCD"));
                                editorDataKhachHang.putString("taiKhoan",khachHang.getGmail().toString());
                                editorDataKhachHang.putString("matKhau",khachHang.getMatKhau().toString());
                                editorDataKhachHang.putBoolean("dangNhap",true);
                                editorDataKhachHang.putString("tenKhachHang",khachHang.getHoVaTen().toString());
                                editorDataKhachHang.commit();
                                mcontext.startActivity(new Intent(mcontext,MainActivity.class));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            txt_thongBao.setVisibility(View.VISIBLE);
                            Toast.makeText(mcontext, "Thông tin tài khoản không chính xác", Toast.LENGTH_SHORT).show();
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
                                    }}).show();                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    private void khoiTaoBanDau(){
        requestQueue= Volley.newRequestQueue(mcontext);
        dataUrl=mcontext.getSharedPreferences("dataUrl", Context.MODE_PRIVATE);
        editorDataUrl=dataUrl.edit();
        urlDiaChi=dataUrl.getString("urlDiaChi"," ");
        urlVeMayBay=dataUrl.getString("urlVeMayBay"," ");
        urlMayBay=dataUrl.getString("urlMayBay"," ");
        urlKhachHang=dataUrl.getString("urlKhachHang"," ");

        dataKhachHang=mcontext.getSharedPreferences("dataKhachHang",Context.MODE_PRIVATE);
        editorDataKhachHang = dataKhachHang.edit();
    }

    private void dangKy(){
        Intent intent=new Intent(mcontext, Activity_DangKy.class);
        mcontext.startActivity(intent);
    }
}
