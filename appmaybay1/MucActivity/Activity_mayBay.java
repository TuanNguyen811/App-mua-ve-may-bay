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
import com.example.appmaybay1.adapter.AdapterMayBay;
import com.example.appmaybay1.doiTuong.DiaChi;
import com.example.appmaybay1.doiTuong.MayBay;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_mayBay extends AppCompatActivity {
    ListView lv_maybay;
    ArrayList<MayBay> danhSachMayBay;
    AdapterMayBay adapterMayBay;

    SharedPreferences dataUrl;
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang,urlVeMayBayDaBan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_may_bay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bt_tab);
        bottomNavigationView.getMenu().findItem(R.id.menu_maybay).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menu_trangchu){
                    startActivity(new Intent(Activity_mayBay.this,MainActivity_trangchu.class));
                } else if (menuItem.getItemId()==R.id.menu_diachi) {
                    startActivity(new Intent(Activity_mayBay.this,Activity_diaChi.class));
                }else if(menuItem.getItemId()==R.id.menu_maybay){
                   // startActivity(new Intent(Activity_mayBay.this,Activity_mayBay.class));
                }else if(menuItem.getItemId()==R.id.menu_vemaybay){
                    startActivity(new Intent(Activity_mayBay.this,Activity_VeDaBan.class));

                }
                return true;
            }
        });

        khoiTao();
        taoListview();
        layDuLieuMayBay(urlMayBay);
        lv_maybay.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               MayBay mayBay=danhSachMayBay.get(position);
                dialogThongTinMayBay(mayBay,"delete");
                return false;
            }
        });
    }
    public void taiDuLieuMayBay(String url, MayBay mayBay,String ME){
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    layDuLieuMayBay(url);
                }
            }, new Response.ErrorListener() {
            @Override
                public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Activity_mayBay.this, "Loi ket noi", Toast.LENGTH_SHORT).show();
                }
            }
        ){
        @Nullable
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String>parmas=new HashMap<>();
            String id=String.valueOf(mayBay.getId());
            String maMayBay=mayBay.getMaMayBay();
            String tenMayBay=mayBay.getTenMayBay();
            String soGhe=String.valueOf(mayBay.getSoGhe());
            parmas.put("ME",ME.toString().trim());
            parmas.put("id",id.toString().trim());
            parmas.put("maMayBay",maMayBay.toString().trim());
            parmas.put("tenMayBay",tenMayBay.toString().trim());
            parmas.put("soGhe",soGhe.toString().trim());
            return parmas;
        }
    };
        requestQueue.add(stringRequest);
    }
    public void dialogThongTinMayBay(MayBay mayBay,String ME){
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thongtinmaybay);
       // Toast.makeText(this, ME+" "+mayBay.getId(), Toast.LENGTH_SHORT).show();
        TextView txt_id=(TextView) dialog.findViewById(R.id.txt_dialog_idmaybay);
        EditText edt_tenMayBay=(EditText) dialog.findViewById(R.id.edt_dialog_tenmaybay);
        EditText edt_soGhe=(EditText) dialog.findViewById(R.id.edt_dialog_soghe);
        EditText edt_maMayBay=(EditText) dialog.findViewById(R.id.edt_dialog_mamaybay);
        Button bt_xacNhan=(Button) dialog.findViewById(R.id.bt_dialogmaybay_xacNhan);
        Button bt_huy=(Button) dialog.findViewById(R.id.bt_dialogmaybay_huy);
        TextView txt_tieuDe=(TextView)dialog.findViewById(R.id.txt_TieuDeMayBay);
        //chuyển dữ liệu hiện tại vào dialog
        txt_id.setText("ID:" + mayBay.getId());
        edt_maMayBay.setText(mayBay.getMaMayBay().toString());
        edt_tenMayBay.setText(mayBay.getTenMayBay().toString());
        edt_soGhe.setText(""+mayBay.getSoGhe());

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

        bt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString=txt_id.getText().toString();
                if(!idString.isEmpty()){
                    try{
                        int id=Integer.parseInt(idString);
                        mayBay.setId(id);
                    }catch (NumberFormatException e){
                    }
                }
                //chuyen du lieu moi ve
                mayBay.setMaMayBay(edt_maMayBay.getText().toString());
                mayBay.setTenMayBay(edt_tenMayBay.getText().toString());
                mayBay.setSoGhe(Integer.parseInt(edt_soGhe.getText().toString()));
                // taỉ dữ liệu lên
                taiDuLieuMayBay(urlMayBay,mayBay,ME);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void layDuLieuMayBay(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null,
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
                        }adapterMayBay.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Activity_mayBay.this, "Loi ket noi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void taoListview(){
        danhSachMayBay=new ArrayList<>();
        lv_maybay=(ListView) findViewById(R.id.lv_maybay);
        adapterMayBay=new AdapterMayBay(this,R.layout.dong_maybay,danhSachMayBay);
        lv_maybay.setAdapter(adapterMayBay);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate((R.menu.menu_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_themthongtin) {
            MayBay mayBay=new MayBay(0,"","",0);
            dialogThongTinMayBay(mayBay,"insert");
        }
        return super.onOptionsItemSelected(item);
    }

    private void khoiTao(){
        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        urlDiaChi=dataUrl.getString("urlDiaChi","");
        urlVeMayBay=dataUrl.getString("urlVeMayBay","");
        urlMayBay=dataUrl.getString("urlMayBay","");
        urlKhachHang=dataUrl.getString("urlKhachHang","");
        urlVeMayBayDaBan=dataUrl.getString("urlVeMayBayDaBan","");
    }
}

