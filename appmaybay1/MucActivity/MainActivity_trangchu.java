package com.example.appmaybay1.MucActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmaybay1.R;
import com.example.appmaybay1.adapter.AdapterVeMayBay;
import com.example.appmaybay1.doiTuong.MayBay;
import com.example.appmaybay1.doiTuong.VeMayBay;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity_trangchu extends AppCompatActivity {
    ListView lv_veMaybay;
    ArrayList<VeMayBay> danhSachVeMayBay;
    AdapterVeMayBay adapterVeMayBay;
    String id;
    SharedPreferences dataUrl;
    SharedPreferences.Editor editorDataUrl;

    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang,urlVeMayBayDaBan;
    TextView txt_catDatIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_trangchu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bt_tab);
        bottomNavigationView.getMenu().findItem(R.id.menu_trangchu).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menu_trangchu){
                    //startActivity(new Intent(MainActivity_trangchu.this,MainActivity_trangchu.class));
                } else if (menuItem.getItemId()==R.id.menu_diachi) {
                    startActivity(new Intent(MainActivity_trangchu.this,Activity_diaChi.class));
                }else if(menuItem.getItemId()==R.id.menu_maybay){
                    startActivity(new Intent(MainActivity_trangchu.this,Activity_mayBay.class));
                }else if(menuItem.getItemId()==R.id.menu_vemaybay){
                    startActivity(new Intent(MainActivity_trangchu.this,Activity_VeDaBan.class));

                }
                return true;
            }
        });
        //bắt đầu
        taoListview();
        layDuLieuVeMayBay(urlVeMayBay);
        khoiTao();
        txt_catDatIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDoiID();
            }
        });
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        if(bundle!=null){
            Toast.makeText(this, "TrangChu", Toast.LENGTH_SHORT).show();
            VeMayBay veMayBay= (VeMayBay) bundle.getSerializable("dataThemVeMayBay");
            String ME=bundle.getString("dataME");
           // Toast.makeText(this, ME, Toast.LENGTH_SHORT).show();
        //  Toast.makeText(this, ME+veMayBay.getMaChuyenBay()+veMayBay.getTenTinhDi()+veMayBay.getTenTinhDen(), Toast.LENGTH_SHORT).show();
            taiDuLieuVeMayBay(urlVeMayBay,veMayBay,ME);
        }
        layDuLieuVeMayBay(urlVeMayBay);

        lv_veMaybay.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                VeMayBay veMayBay=danhSachVeMayBay.get(position);
                thongTinVeMayBay(veMayBay,"delete");
                Toast.makeText(MainActivity_trangchu.this, veMayBay.getId()+"Xoa", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        txt_catDatIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDoiID();
            }
        });

    }
    public void thongTinVeMayBay(VeMayBay veMayBay,String ME){
        Intent intent=new Intent(MainActivity_trangchu.this,Activity_NhapVeMayBay.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("dataThemVeMayBay",veMayBay);
        bundle.putString("dataME",ME);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void taiDuLieuVeMayBay(String url, VeMayBay veMayBay, String Me){
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("ThanhCong")){
                            Toast.makeText(MainActivity_trangchu.this, "Thành Công", Toast.LENGTH_SHORT).show();
                        } else if (s.equals("ThatBai")) {
                            Toast.makeText(MainActivity_trangchu.this, "That Bai", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity_trangchu.this, "Thua", Toast.LENGTH_SHORT).show();
                        }
                        layDuLieuVeMayBay(url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity_trangchu.this, "Loi ket noi", Toast.LENGTH_SHORT).show();
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
    public void layDuLieuVeMayBay(String url){
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null,
                new Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        danhSachVeMayBay.clear();
                        for (int i=0;i<jsonArray.length();i++){
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
                                //giờ thì chuyển string sang calende
                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Calendar thoiGianDi=Calendar.getInstance();
                                Calendar thoiGianDen=Calendar.getInstance();
                                try {
                                    Date date=sdf.parse(strThoiGianDen);
                                    thoiGianDen.setTime(date);
                                    Date date2=sdf.parse(strThoiGianDi);
                                    thoiGianDi.setTime(date2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //tiếp theo
                                int soGhe=object.getInt("SoGhe");
                                int giaVe=object.getInt("GiaVe");
                                String thongTinThem=object.getString("ThongTinThem");
                                danhSachVeMayBay.add(new VeMayBay(id,maChuyenBay,maMayBay,tenTinhDi,tenTinhDen,thoiGianDi,thoiGianDen,soGhe,giaVe,thongTinThem));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        adapterVeMayBay.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity_trangchu.this, "Loi ket noi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
    public void taoListview(){
        danhSachVeMayBay=new ArrayList<>();
        lv_veMaybay=(ListView) findViewById(R.id.lv_trangchu_vemaybay);
        adapterVeMayBay= new AdapterVeMayBay(this,R.layout.dong_vemaybay,danhSachVeMayBay);
        lv_veMaybay.setAdapter(adapterVeMayBay);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate((R.menu.menu_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_themthongtin){
            Calendar calendar1=Calendar.getInstance();
            Calendar calendar2=Calendar.getInstance();
            VeMayBay veMayBay=new VeMayBay(1,"","","","",calendar1,calendar2,1,1,"");
            thongTinVeMayBay(veMayBay,"insert");
        }
        return super.onOptionsItemSelected(item);
    }

    private void khoiTao(){
        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        editorDataUrl=dataUrl.edit();
        id=dataUrl.getString("ip","");
        if(id.isEmpty()){
            //ip du phong
            dialogDoiID();
            id=dataUrl.getString("ip","");
        }
        editorDataUrl.putString("urlDiaChi","http://"+id+"/CSDLMayBay/diachi.php");
        editorDataUrl.putString("urlMayBay","http://"+id+"/CSDLMayBay/maybay.php");
        editorDataUrl.putString("urlVeMayBay","http://"+id+"/CSDLMayBay/vemaybay.php");
        editorDataUrl.putString("urlKhachHang","http://"+id+"//CSDLMayBay/khachhang.php");
        editorDataUrl.putString("urlVeMayBayDaBan","http://"+id+"/CSDLMayBay/vemaybaydaban.php");
        editorDataUrl.commit();

        urlDiaChi=dataUrl.getString("urlDiaChi","");
        urlVeMayBay=dataUrl.getString("urlVeMayBay","");
        urlMayBay=dataUrl.getString("urlMayBay","");
        urlKhachHang=dataUrl.getString("urlKhachHang","");
        urlVeMayBayDaBan=dataUrl.getString("urlVeMayBayDaBan","");


        txt_catDatIP=(TextView) findViewById(R.id.txt_main_cadiachiip);
    }
    private void dialogDoiID(){
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_caidiachiip);
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
                Toast.makeText(MainActivity_trangchu.this, "Cập nhật thành công!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity_trangchu.this, MainActivity_trangchu.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}