package com.example.khachhang.MucActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Trace;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.R;
import com.example.khachhang.dialog.DialogDoiMatKhau;
import com.example.khachhang.dialog.DialogSuaThongTinCaNhan;
import com.example.khachhang.doiTuong.KhachHang;
import com.example.khachhang.doiTuong.VeMayBayDaBan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Activity_TrangCaNhan extends AppCompatActivity {
    TextView txt_ten,txt_sdt,txt_gmail,txt_diaChi,txt_Cccd;
    ConstraintLayout bt_trangCaNhan2;
    TextView bt_trangCaNhan1,bt_caiDat,bt_suaThongTin,bt_doiMatKhau,bt_xemLichSuMua,bt_catDat;
    TextView bt_dangXuat,bt_chuyenTaiKhoan,bt_xoaTaiKhoan;
    //String urlKhachHang="http://192.168.231.154/CSDLMayBay/khachhang.php";

    KhachHang khachHang;
    RequestQueue requestQueue;
    String gmailNguoiDung;
    String matKhauNguoiDung;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences dataUrl;
    SharedPreferences.Editor editorDataUrl;
    String urlVeMayBay;
    String urlDiaChi;
    String urlMayBay;
    String urlKhachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_ca_nhan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bt_tab);
        bottomNavigationView.getMenu().findItem(R.id.menu_taikhoan).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menu_timve){
                    startActivity(new Intent(Activity_TrangCaNhan.this, MainActivity.class));
                }else if(menuItem.getItemId()==R.id.menu_khamPha){
                    startActivity(new Intent(Activity_TrangCaNhan.this, Activity_KhamPha.class));
                }else if(menuItem.getItemId()==R.id.menu_quanly){
                    startActivity(new Intent(Activity_TrangCaNhan.this,Activity_VeMayBayDaMua.class));
                } else if (menuItem.getItemId()==R.id.menu_taikhoan) {

                }
                return true;
            }
        });
        khoiTaoBanDau();
        layThongTinNguoiDung(urlKhachHang,"timkhachhang",gmailNguoiDung,matKhauNguoiDung);
        bt_dangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Activity_TrangCaNhan.this);
                builder2.setTitle("Thông báo!");
                builder2.setMessage("Bạn có muốn đăng xuất không?");
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Activity_TrangCaNhan.this, "Đang đăng xuất...", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("dangNhap",false);
                        editor.putString("taiKhoan","");
                        editor.putString("matKhau","");
                        editor.commit();
                        startActivity(new Intent(Activity_TrangCaNhan.this,MainActivity.class));
                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder2.show();
            }
        });
        bt_chuyenTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Activity_TrangCaNhan.this);
                builder2.setTitle("Thông báo!");
                builder2.setMessage("Bạn có muốn đăng xuất không?");
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Activity_TrangCaNhan.this, "Đang đăng xuất...", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("dangNhap",false);
                        editor.putString("taiKhoan","");
                        editor.putString("matKhau","");
                        editor.commit();
                        startActivity(new Intent(Activity_TrangCaNhan.this,MainActivity.class));
                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder2.show();
            }
        });
        bt_xemLichSuMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_TrangCaNhan.this,Activity_VeMayBayDaMua.class));
            }
        });
        bt_trangCaNhan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSuaThongTinCaNhan dialogSuaThongTinCaNhan=new DialogSuaThongTinCaNhan(Activity_TrangCaNhan.this,khachHang);
                dialogSuaThongTinCaNhan.show();
            }
        });
        bt_trangCaNhan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSuaThongTinCaNhan dialogSuaThongTinCaNhan=new DialogSuaThongTinCaNhan(Activity_TrangCaNhan.this,khachHang);
                dialogSuaThongTinCaNhan.show();
            }
        });
        bt_suaThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSuaThongTinCaNhan dialogSuaThongTinCaNhan=new DialogSuaThongTinCaNhan(Activity_TrangCaNhan.this,khachHang);
                dialogSuaThongTinCaNhan.show();
            }
        });
        bt_doiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDoiMatKhau dialogDoiMatKhau=new DialogDoiMatKhau(Activity_TrangCaNhan.this,khachHang);
                dialogDoiMatKhau.show();
            }
        });
        bt_catDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDoiID();
            }
        });
        bt_xoaTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_TrangCaNhan.this);
                builder.setTitle("Thông báo quan trọng!");
                builder.setMessage("Bạn có muốn xóa vĩnh viễn tài khoản không?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Activity_TrangCaNhan.this, "Đang xóa dữ liệu...", Toast.LENGTH_SHORT).show();
                        xoaTaiKhoan(khachHang);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
    private  void xoaTaiKhoan(KhachHang khachHang){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlKhachHang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("1")){
                            editor.putBoolean("dangNhap",false);
                            editor.putString("taiKhoan","");
                            editor.putString("matKhau","");
                            editor.commit();
                            startActivity(new Intent(Activity_TrangCaNhan.this,MainActivity.class));
                        }else if (s.equals("0")) {
                            Toast.makeText(Activity_TrangCaNhan.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Activity_TrangCaNhan.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Activity_TrangCaNhan.this, "Mất kết nối!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parmas=new HashMap<>();
                // gửi dữ liệu di
                String id=String.valueOf(khachHang.getId());
                parmas.put("ME","delete");
                parmas.put("id",id.trim());
                parmas.put("hoVaTen","".trim());
                parmas.put("gmail","".trim());
                parmas.put("matKhau","".trim());
                parmas.put("diaChi","".trim());
                parmas.put("soDienThoai","".trim());
                parmas.put("cCCD","".trim());
                return parmas;
            }
        };
        requestQueue.add(stringRequest);// tao request
    }

    private void dialogDoiID(){
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_caidat);
        // Thiết lập các thuộc tính cho cửa sổ của dialog
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.TOP; // Hiển thị ở phía dưới màn hình
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView txt_id=(TextView) dialog.findViewById(R.id.txt_dialogcatdat_iphientai);
        EditText edt_id=(EditText) dialog.findViewById(R.id.edt_dialogcatdat_ipmoi);
        Button bt_capNhat=(Button) dialog.findViewById(R.id.bt_dialogcatdat_capnhat);
        String id=dataUrl.getString("ip","");
        txt_id.setText(id);
        edt_id.setText(id);
        bt_capNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idnew =edt_id.getText().toString();
                editorDataUrl.putString("ip",idnew);
                editorDataUrl.commit();
                Toast.makeText(Activity_TrangCaNhan.this, "Cập nhật thành công!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Activity_TrangCaNhan.this, MainActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void layThongTinNguoiDung(String url,String me,String gmail,String matKhau){
        String urlKhachHangDK=url+"?me="+me+"&gmail="+gmail+"&matKhau="+matKhau;
        //url+="?me="+me+"&gmail="+gmail+"&matKhau="+matKhau;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, urlKhachHangDK, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            JSONObject object=jsonArray.getJSONObject(0);
                            khachHang=new KhachHang(object.getInt("Id"),object.getString("HoVaTen"),object.getString("Gmail"),object.getString("MatKhau"),object.getString("DiaChi"),object.getInt("SoDienThoai"),object.getInt("CCCD"));
                            txt_ten.setText(khachHang.getHoVaTen());
                            txt_sdt.setText(0+""+String.valueOf(khachHang.getSoDienThoai()));
                            txt_gmail.setText("Gmail: "+khachHang.getGmail());
                            txt_diaChi.setText("Địa chỉ: "+khachHang.getDiaChi());
                            txt_Cccd.setText("CCCD: "+ String.valueOf(khachHang.getcCCD()));
                        } catch (JSONException e) {
                            Toast.makeText(Activity_TrangCaNhan.this, "Không có thông tin", Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
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
                                        startActivity(new Intent(Activity_TrangCaNhan.this,Activity_TrangCaNhan.class));
                                    }}).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    private void anhXa(){
        bt_trangCaNhan1=(TextView)findViewById(R.id.bt_trangcanhan_trangcanhan1);
        bt_trangCaNhan2=(ConstraintLayout) findViewById(R.id.bt_trangcanhan_trangcanha2);
        bt_caiDat=(TextView)findViewById(R.id.bt_trangcanhan_caidat);
        bt_suaThongTin=(TextView)findViewById(R.id.bt_trangcanhan_suathongtincanha);
        bt_doiMatKhau=(TextView)findViewById(R.id.bt_trangcanhan_doimatkhau);
        bt_xemLichSuMua=(TextView)findViewById(R.id.bt_trangcanhan_xemlichsumau);//
        bt_chuyenTaiKhoan=(TextView)findViewById(R.id.bt_trangcanhan_doitaikhoan);//
        bt_xoaTaiKhoan=(TextView)findViewById(R.id.bt_trangcanhan_xoataikhoan);
        bt_dangXuat=(TextView) findViewById(R.id.bt_trangcanhan_dangxuat);//
        bt_catDat=(TextView)findViewById(R.id.bt_trangcanhan_caidat);

        txt_ten=(TextView)findViewById(R.id.txt_trangcanhan_ten);
        txt_sdt=(TextView)findViewById(R.id.txt_trangcanhan_sdt);
        txt_gmail=(TextView)findViewById(R.id.txt_trangcanhan_gmail);
        txt_diaChi=(TextView)findViewById(R.id.txt_trangcanhan_diaChi);
        txt_Cccd=(TextView)findViewById(R.id.txt_trangcanhan_cccd);

    }
    private void khoiTaoBanDau(){
        anhXa();
        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        editorDataUrl=dataUrl.edit();
        urlDiaChi=dataUrl.getString("urlDiaChi"," ");
        urlVeMayBay=dataUrl.getString("urlVeMayBay"," ");
        urlMayBay=dataUrl.getString("urlMayBay"," ");
        urlKhachHang=dataUrl.getString("urlKhachHang"," ");

        requestQueue=Volley.newRequestQueue(this);
        sharedPreferences=getSharedPreferences("dataKhachHang",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gmailNguoiDung=sharedPreferences.getString("taiKhoan","");
        matKhauNguoiDung=sharedPreferences.getString("matKhau","");
    }
}