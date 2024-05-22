package com.example.khachhang.MucActivity;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.khachhang.R;
import com.example.khachhang.adapter.AdapterVeMayBay;
import com.example.khachhang.adapter.DiaChiAdapter;
import com.example.khachhang.dialog.DialogDangNhap;
import com.example.khachhang.dialog.DialogXemDanhSachDiaChi;
import com.example.khachhang.dialog.DialogVeMayBay;
import com.example.khachhang.doiTuong.DiaChi;
import com.example.khachhang.doiTuong.VeMayBay;
import com.example.khachhang.my_interfave.IClickItem;
import com.example.khachhang.my_interfave.IClickItemVeMayBay;
import com.example.khachhang.my_interfave.ITFDiaChi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //LayOut 1
    ImageView im_Anhdaidien;
    TextView txt_xinChao;
    ConstraintLayout layout1;
    Button bttxt_tinhDi, bttxt_tinhDen, bttxt_NgayDi, bttxt_NgayVe;
    ImageButton im_doiTinh;
    Switch swt_khuHoi;
    ImageView im_anhLich;
    TextView txt_ngayveThoiGian;
    Button bt_timKiem;

    //DANH SÁCH ĐỊA CHỈ
    RecyclerView rcv_diaChi;
    DiaChiAdapter diaChiAdapter;
    List<DiaChi>danhSachDiaChi;
    SearchView searchView_main_diaChi;

    RequestQueue requestQueue;
    DialogDangNhap dialogDangNhap;
    //luu thong tin khach hang
    private SharedPreferences dataKhachHang,dataUrl;
    private  SharedPreferences.Editor editorDataKhachHang;
    private SharedPreferences.Editor editorDataUrl;
    String gmailKhachHang,tenKhachHang;
    Boolean daDangNhap;
    String urlVeMayBay,urlDiaChi,urlMayBay,urlKhachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bt_tab);
        bottomNavigationView.getMenu().findItem(R.id.menu_timve).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menu_timve){

                }else if(menuItem.getItemId()==R.id.menu_khamPha){
                    if(daDangNhap==false){
                        dialogDangNhap.show();
                    }else{
                        startActivity(new Intent(MainActivity.this, Activity_KhamPha.class));

                    }
                }else if(menuItem.getItemId()==R.id.menu_quanly){
                    if(daDangNhap==false){
                        dialogDangNhap.show();
                    }else{
                        startActivity(new Intent(MainActivity.this, Activity_VeMayBayDaMua.class));
                    }
                } else if (menuItem.getItemId()==R.id.menu_taikhoan) {
                    if(daDangNhap==false){
                        dialogDangNhap.show();
                    }else{
                        startActivity(new Intent(MainActivity.this,Activity_TrangCaNhan.class));
                    }
                }
                return true;
            }
        });
        khoiTaoBanDau();

        im_Anhdaidien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Activity_TrangCaNhan.class));
            }
        });
        // tạo danh sách dia chi
        bttxt_tinhDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogThongTinTinh("di");
            }
        });
        bttxt_tinhDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogThongTinTinh("den");
            }
        });
        // phim chuc nang
        swt_khuHoi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    bttxt_NgayVe.setVisibility(View.VISIBLE);
                    txt_ngayveThoiGian.setVisibility(View.VISIBLE);
                    im_anhLich.setVisibility(View.VISIBLE);
                } else if (isChecked==false) {
                    txt_ngayveThoiGian.setVisibility(View.GONE);
                    bttxt_NgayVe.setVisibility(View.GONE);
                    im_anhLich.setVisibility(View.GONE);
                }
            }
        });
        im_doiTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tinhDen2=bttxt_tinhDen.getText().toString();
                String tinhDi2=bttxt_tinhDi.getText().toString();
                bttxt_tinhDen.setText(tinhDi2);
                bttxt_tinhDi.setText(tinhDen2);
            }
        });
        bttxt_NgayDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int y=calendar.get(Calendar.YEAR);
                int m=calendar.get(Calendar.MONTH);
                int d=calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat spd=new SimpleDateFormat("dd/MM/yyyy");
                        calendar.set(year,month,dayOfMonth);
                        String StrngayDi=spd.format(calendar.getTime()).toString();
                        bttxt_NgayDi.setText(StrngayDi);
                    }
                },y,m,d);
                // Giới hạn ngày tối thiểu là ngày hiện tại
                // datePickerDialog.getDatePicker().setMinDate(thòi gian cần giới hạn);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        bttxt_NgayVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StrngayDi=bttxt_NgayDi.getText().toString();
                Calendar ngayDi=Calendar.getInstance();
                Calendar ngayVe=Calendar.getInstance();
                SimpleDateFormat spd=new SimpleDateFormat("dd/MM/yyyy");
                Date date;
                //chuyên stirng sang ngày đi
                {
                    try {
                        //chuyên string thành date
                        date = spd.parse(StrngayDi);
                        ngayDi=Calendar.getInstance();
                        ngayDi.setTime(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                int y=ngayDi.get(Calendar.YEAR);
                int m=ngayDi.get(Calendar.MONTH);
                int d=ngayDi.get(Calendar.DATE);
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ngayVe.set(year,month,dayOfMonth);
                        String strNgayVe=spd.format(ngayVe.getTime());
                        bttxt_NgayVe.setText(strNgayVe);
                    }
                },y,m,d);
                datePickerDialog.getDatePicker().setMinDate(ngayDi.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        bt_timKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timKiemTinhDi = bttxt_tinhDi.getText().toString();
                String timKiemTinhDen = bttxt_tinhDen.getText().toString();
                String strtimKiemThoiGianDi1 = bttxt_NgayDi.getText().toString();
                // gui du lieu di
                Intent intent1=new Intent(MainActivity.this,Activity_TimChuyenBay.class);
                intent1.putExtra("tinhDi",timKiemTinhDi);
                intent1.putExtra("tinhDen",timKiemTinhDen);
                intent1.putExtra("thoiGian",strtimKiemThoiGianDi1);
                startActivity(intent1);
            }
        });
    }
    private void dialogThongTinTinh(String methol){
        //Lay thong tin dia chi tu server
        layDuLieuDiaChi(urlDiaChi);
        //tạo dialong
        DialogXemDanhSachDiaChi dialog2=new DialogXemDanhSachDiaChi(this, new ITFDiaChi() {
            @Override
            public void layThongTinDiaChi(DiaChi diaChi) {
            }
        });
        rcv_diaChi=(RecyclerView)dialog2.findViewById(R.id.rcv_dialog_main_diachi);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rcv_diaChi.setLayoutManager(linearLayoutManager);
        // bắt sự kiện ở interface
        diaChiAdapter=new DiaChiAdapter(danhSachDiaChi, new IClickItem() {
            @Override
            public void layDuLieuDiaChi(DiaChi diaChi) {
                if(methol.equals("den")){
                    bttxt_tinhDen.setText(diaChi.getTenDiaChi());
                    dialog2.dismiss();
                }else if(methol.equals("di")){
                    bttxt_tinhDi.setText(diaChi.getTenDiaChi());
                    dialog2.dismiss();
                }
            }
        });
        // tạo dòng kẻ
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcv_diaChi.addItemDecoration(itemDecoration);
        rcv_diaChi.setAdapter(diaChiAdapter);
        //tìm kiếm
        searchView_main_diaChi=(SearchView)dialog2.findViewById(R.id.searchview_main_timDiaChi);
        searchView_main_diaChi.setIconified(false);
        searchView_main_diaChi.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                diaChiAdapter.getFilter().filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (diaChiAdapter != null) {
                    diaChiAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        Button bt_huy=(Button)dialog2.findViewById(R.id.bt_main_dialog_huy);
        bt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }
    public void layDuLieuDiaChi(String url){
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        if(jsonArray.length()==0){
                            Toast.makeText(MainActivity.this, "Không tìm thấy!", Toast.LENGTH_SHORT).show();
                        }
                        danhSachDiaChi.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            try {
                                JSONObject object=jsonArray.getJSONObject(i);
                                danhSachDiaChi.add(new DiaChi(object.getInt("Id"),object.getString("TenDiaDiem"),object.getString("MaDiaDiem"),object.getString("TenSanBay")));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        diaChiAdapter.notifyDataSetChanged();

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
                                    }}).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
    private void anhXaThongTin(){
        //layout 1
        im_Anhdaidien=(ImageView)findViewById(R.id.im_main_anhdaidien);
        txt_xinChao=(TextView)findViewById(R.id.txt_mainchinh_loichao);
        layout1=(ConstraintLayout)findViewById(R.id.constraintLayout_main_timkiem);
        bttxt_tinhDi=(Button)findViewById(R.id.btTxt_main_tinhDi);
        bttxt_tinhDen=(Button)findViewById(R.id.btTxt_main_tinhden);
        bttxt_NgayDi=(Button)findViewById(R.id.btTxt_main_ngaydi);
        bttxt_NgayVe=(Button)findViewById(R.id.btTxt_main_ngayve);
        swt_khuHoi=(Switch)findViewById(R.id.swt_main_khuhoi);
        im_anhLich=(ImageView)findViewById(R.id.imngayvemain);
        bt_timKiem=(Button)findViewById(R.id.bt_main_timKiem);
        im_doiTinh=(ImageButton)findViewById(R.id.btIm_main_doidiadiem);
        txt_ngayveThoiGian=(TextView)findViewById(R.id.txt_main_ngayveThoigian);
    }
    private void khoiTaoBanDau(){
        anhXaThongTin();
        requestQueue=Volley.newRequestQueue(this);
        //tải thông tin url đồng bộ
        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        editorDataUrl=dataUrl.edit();

        String id=dataUrl.getString("ip","");
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
        //
        //lấy thong tin hoa hang
        dialogDangNhap=new DialogDangNhap(this);
        dataKhachHang=getSharedPreferences("dataKhachHang",MODE_PRIVATE);
        daDangNhap=dataKhachHang.getBoolean("dangNhap",false);
        if(daDangNhap==false){
            dialogDangNhap.show();
        }else{
            gmailKhachHang=dataKhachHang.getString("taiKhoan","");
            tenKhachHang=dataKhachHang.getString("tenKhachHang","");
            txt_xinChao.setText(""+tenKhachHang);
        }
        editorDataKhachHang=dataKhachHang.edit();
        /*
        String urlVeMayBay;
        String urlDiaChi;
        String urlMayBay;
        String urlKhachHang;
        private SharedPreferences dataUrl;
        private SharedPreferences.Editor editorDataUrl;

        dataUrl=getSharedPreferences("dataUrl",MODE_PRIVATE);
        editorDataUrl=dataUrl.edit();
        urlDiaChi=dataUrl.getString("urlDiaChi"," ");
        urlVeMayBay=dataUrl.getString("urlVeMayBay"," ");
        urlMayBay=dataUrl.getString("urlMayBay"," ");
        urlKhachHang=dataUrl.getString("urlKhachHang"," ");
         */

        bttxt_NgayVe.setVisibility(View.GONE);
        im_anhLich.setVisibility(View.GONE);
        txt_ngayveThoiGian.setVisibility(View.GONE);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat spb=new SimpleDateFormat("dd/MM/yyyy");
        String strNgayHienTai=spb.format(calendar.getTime());
        bttxt_NgayDi.setText(strNgayHienTai);
        bttxt_NgayVe.setText(strNgayHienTai);
        danhSachDiaChi=new ArrayList<>();
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
                Toast.makeText(MainActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        dialog.show();
    }
}