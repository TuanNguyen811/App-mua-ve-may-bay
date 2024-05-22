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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.MucActivity.Activity_ThanhToan;
import com.example.khachhang.MucActivity.MainActivity;
import com.example.khachhang.R;
import com.example.khachhang.doiTuong.DiaChi;
import com.example.khachhang.doiTuong.MayBay;
import com.example.khachhang.doiTuong.VeMayBay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DialogVeMayBay extends Dialog {
    TextView txt_gioDi,txt_ngayDi,txt_tongThoiGian,txt_gioDen,txt_ngayDen;
    TextView txt_tinhDi,txt_sanBayDi,txt_maMayBay,txt_mayBay, txt_tinhDen,txt_sanBayDen;
    TextView txt_tieuDe,txt_thongTin,txt_giaVe,txt_soGhe;
    Button bt_tiepTuc;
    RequestQueue requestQueue;
    // ljai bap
    String maTinhDi="",tenSanBayDi="",maTinhDen="",tenSanBayDen="";
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang;
    SharedPreferences dataUrl;
    SharedPreferences.Editor editorDataUrl;
    public DialogVeMayBay(@NonNull Context context, VeMayBay veMayBay) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_main_xemthongtinvemaybay);
        // Thiết lập thuộc tính của cửa sổ để dialog hiển thị ở phần dưới của màn hình
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // Cho phép dialog hiển thị ra toàn bộ màn hình
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM; // Hiển thị ở phía dưới màn hình
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều rộng là match parent
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        // khoiTao ban dau
        requestQueue = Volley.newRequestQueue(context);
        anhXa();
        layURL(context);
        layThongTin(context,veMayBay);
        //layThongVeTinMayBay(veMayBay);
        ganThongTin(veMayBay);
        //
        bt_tiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyenThongTin(context,veMayBay);
            }
        });
    }
    private void chuyenThongTin(Context context,VeMayBay veMayBay){
        Intent intent=new Intent(context, Activity_ThanhToan.class);
        intent.putExtra("thongTinVeMayBay",veMayBay);
        context.startActivity(intent);
    }
    private void ganThongTin(VeMayBay veMayBay){
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
        //gan thong tin
        txt_gioDi.setText(gioDi.toString());
        txt_ngayDi.setText(ngayDi.toString());
        txt_tongThoiGian.setText(formatNuber);
        txt_gioDen.setText(gioDen.toString());
        txt_ngayDen.setText(ngayDen.toString());
        txt_thongTin.setText(veMayBay.getThongTinThem().toString());
        float giaved=(float) veMayBay.getGiaVe();
        DecimalFormat dfgiaTien=new DecimalFormat("#,###đ");
        String giave=dfgiaTien.format(giaved);
        txt_giaVe.setText(giave);
        txt_soGhe.setText("Số ghế: "+veMayBay.getSoGhe());
    }
    private void layThongTin(Context context,VeMayBay veMayBay){
        String maMayBay = veMayBay.getMaMayBay().toString();
        String tenTinhDi = veMayBay.getTenTinhDi().toString();
        String tenTinhDen = veMayBay.getTenTinhDen().toString();
        //sau url de gui them thong tin
        String urlMayBayDk=urlMayBay+"?me="+"timmaybay"+"&tenMayBay="+maMayBay;
        String urldiaChiDk=urlDiaChi+"?me="+"timdiachi"+"&tenTinhDi="+tenTinhDi+"&tenTinhDen="+tenTinhDen;
        // tao danh sach de luu thong tin
        ArrayList<DiaChi> danhSachDiaChi=new ArrayList<>();
        ArrayList<MayBay> danhSachMayBay=new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequestDiaChi=new JsonArrayRequest(Request.Method.GET, urldiaChiDk, null,
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
                        txt_tinhDi.setText((veMayBay.getTenTinhDi().toString())+" - "+ maTinhDi);
                        txt_sanBayDi.setText(tenSanBayDi);// chua co
                        txt_tinhDen.setText((veMayBay.getTenTinhDen().toString())+" - "+ maTinhDen);
                        txt_sanBayDen.setText(tenSanBayDen);
                        txt_tieuDe.setText("Hành Trình Chuyến Bay: "+veMayBay.getMaChuyenBay());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        requestQueue.add(jsonArrayRequestDiaChi);
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
                        txt_mayBay.setText(veMayBay.getMaMayBay());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        requestQueue.add(jsonArrayRequestMayBay);
    }
    private void layURL(Context context){
        dataUrl= context.getSharedPreferences("dataUrl",Context.MODE_PRIVATE);
        editorDataUrl=dataUrl.edit();
        urlDiaChi=dataUrl.getString("urlDiaChi"," ");
        urlVeMayBay=dataUrl.getString("urlVeMayBay"," ");
        urlMayBay=dataUrl.getString("urlMayBay"," ");
        urlKhachHang=dataUrl.getString("urlKhachHang"," ");
    }
    private void anhXa(){
        txt_gioDi=(TextView)findViewById(R.id.txt_dialogVeMB_main_giodi);
        txt_ngayDi =(TextView)findViewById(R.id.txt_dialogVeMB_main_ngaydi);
        txt_tongThoiGian=(TextView)findViewById(R.id.txt_dialogVeMB_main_tongthoigian);
        txt_gioDen=(TextView)findViewById(R.id.txt_dialogVeMB_main_gioden);
        txt_ngayDen=(TextView)findViewById(R.id.txt_dialogVeMB_main_ngayden);

        txt_tinhDi=(TextView)findViewById(R.id.txt_dialogVeMB_main_tinhdi);
        txt_sanBayDi=(TextView)findViewById(R.id.txt_dialogVeMB_main_sanbaydi);

        txt_maMayBay=(TextView)findViewById(R.id.txt_dialogVeMB_main_mamaybay);
        txt_mayBay=(TextView)findViewById(R.id.txt_dialogVeMB_main_maybayy);

        txt_tinhDen=(TextView)findViewById(R.id.txt_dialogVeMB_main_tinhden);
        txt_sanBayDen=(TextView)findViewById(R.id.txt_dialogVeMB_main_sanbayden);

        txt_soGhe=(TextView) findViewById(R.id.txt_dialogVeMB_main_soGhe);
        txt_tieuDe=(TextView)findViewById(R.id.txt_dialogVeMB_main_tieude);
        txt_thongTin=(TextView)findViewById(R.id.txt_dialogVeMB_main_thongtinchitiet);
        txt_giaVe=(TextView)findViewById(R.id.txt_dialogVeMB_main_giave) ;
        bt_tiepTuc=(Button) findViewById(R.id.bt_dialogVeMB_main_tieptuc);
    }
}
