package com.example.appmaybay1.MucActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmaybay1.R;
import com.example.appmaybay1.adapter.AdapterDiaChi;
import com.example.appmaybay1.doiTuong.DiaChi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_diaChi extends AppCompatActivity {
    ArrayList<DiaChi> danhSachDiaChi;
    AdapterDiaChi adapterDiaChi;
    ListView lv_diaChi;

    SharedPreferences dataUrl;
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang,urlVeMayBayDaBan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dia_chi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //tabtar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bt_tab);
        bottomNavigationView.getMenu().findItem(R.id.menu_diachi).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menu_trangchu){
                    startActivity(new Intent(Activity_diaChi.this,MainActivity_trangchu.class));
                } else if (menuItem.getItemId()==R.id.menu_diachi) {
                    //startActivity(new Intent(MainActivity_trangchu.this,Activity_diaChi.class));
                }else if(menuItem.getItemId()==R.id.menu_maybay){
                    startActivity(new Intent(Activity_diaChi.this,Activity_mayBay.class));
                }else if(menuItem.getItemId()==R.id.menu_vemaybay){
                    startActivity(new Intent(Activity_diaChi.this,Activity_VeDaBan.class));

                }
                return true;
            }
        });
        KhoiTao();
        taoListView();
        layDuLieuDiaChi(urlDiaChi);
        //tao su kien giu de xoa
        lv_diaChi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogThongTinDiaChi(danhSachDiaChi.get(position),"delete");
                return false;
            }
        });
    }
   private void taiDuLieuDiaChi(String url, DiaChi diaChi,String ME){
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String s) {
               layDuLieuDiaChi(urlDiaChi);
               if(s.equals("ThanhCong")){
                   Toast.makeText(Activity_diaChi.this, ME+" Thành công!", Toast.LENGTH_SHORT).show();
               }
               else if (s.equals("ThatBai")) {
                   Toast.makeText(Activity_diaChi.this, ME+" Thất bại!", Toast.LENGTH_SHORT).show();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError volleyError) {
               Toast.makeText(Activity_diaChi.this, "Lỗi kết nối!??", Toast.LENGTH_SHORT).show();
           }
       }){
           @Nullable
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String>parmas=new HashMap<>();
               String id=String.valueOf(diaChi.getId());
               String ten=diaChi.getTenDiaChi();
               String ma=diaChi.getMaDiaChi();
               String tenSanBay=diaChi.getTenSanBay();
               parmas.put("ME",ME.toString().trim());
               parmas.put("id",id.toString().trim());
               parmas.put("tenDiaDiem",diaChi.getTenDiaChi().toString().trim());
               parmas.put("maDiaDiem",diaChi.getMaDiaChi().toString().trim());
               parmas.put("tenSanBay",diaChi.getTenSanBay().toString().trim());
               return parmas;
           }
       };
       requestQueue.add(stringRequest);
   }
    public void layDuLieuDiaChi(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
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
                        adapterDiaChi.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Activity_diaChi.this, "Loi ket noi", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
    private void taoListView(){
        danhSachDiaChi=new ArrayList<>();
        lv_diaChi=(ListView) findViewById(R.id.lv_diachi);
        adapterDiaChi=new AdapterDiaChi(Activity_diaChi.this,R.layout.dong_diachi,danhSachDiaChi );
        lv_diaChi.setAdapter(adapterDiaChi);
    }
    public void dialogThongTinDiaChi(DiaChi diaChi,String ME){
        //khởi tạo dialong èn
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_suadiachi);
        // doi tieu de
        // bước ánh xa
        EditText edt_tenTinh=(EditText) dialog.findViewById(R.id.edt_dialog_tentinh);
        EditText edt_tenMayBay=(EditText) dialog.findViewById(R.id.edt_dialog_tenSanBay);
        EditText edt_maTinh=(EditText) dialog.findViewById(R.id.edt_dialog_matinh);
        TextView txt_id=(TextView) dialog.findViewById(R.id.txt_dialog_id);
        Button bt_xacNhan=(Button) dialog.findViewById(R.id.bt_dialogdiadiem_xacnhan);
        Button bt_huy=(Button) dialog.findViewById(R.id.bt_dialogdiadiem_huy);
        TextView txt_tieuDe=(TextView) dialog.findViewById(R.id.txt_TieuDe);
        if(ME.equals("insert")){
            bt_xacNhan.setText("Thêm");
            txt_tieuDe.setText("THÊM THÔNG TIN");
        }else if(ME.equals("update")){
            bt_xacNhan.setText("Cập Nhât");
            txt_tieuDe.setText("CẬP NHẬT THÔNG TIN");
        }
        else if(ME.equals("delete")){
            bt_xacNhan.setText("Xóa");
            txt_tieuDe.setText("XÓA THÔNG TIN");
        }
        //chuyển dữ liệu hiện tại vào dialog
        edt_tenTinh.setText(diaChi.getTenDiaChi());
        edt_tenMayBay.setText(diaChi.getTenSanBay());
        edt_maTinh.setText(diaChi.getMaDiaChi());
        txt_id.setText("ID:"+diaChi.getId());
        //bắt sự kiện
        bt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tạo dữ liệu mới
                String idString = txt_id.getText().toString();
                if (!idString.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idString);
                        diaChi.setId(id);
                    } catch (NumberFormatException e) {
                        // Xử lý ngoại lệ NumberFormatException
                    }
                }
                diaChi.setTenDiaChi(edt_tenTinh.getText().toString());
                diaChi.setMaDiaChi(edt_maTinh.getText().toString());
                diaChi.setTenSanBay(edt_tenMayBay.getText().toString());
                taiDuLieuDiaChi(urlDiaChi,diaChi,ME);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void KhoiTao(){
        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        urlDiaChi=dataUrl.getString("urlDiaChi","");
        urlVeMayBay=dataUrl.getString("urlVeMayBay","");
        urlMayBay=dataUrl.getString("urlMayBay","");
        urlKhachHang=dataUrl.getString("urlKhachHang","");
        urlVeMayBayDaBan=dataUrl.getString("urlVeMayBayDaBan","");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate((R.menu.menu_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_themthongtin){
            DiaChi diaChi=new DiaChi(0,"","","");
            dialogThongTinDiaChi(diaChi,"insert");
        }
        return super.onOptionsItemSelected(item);
    }
}