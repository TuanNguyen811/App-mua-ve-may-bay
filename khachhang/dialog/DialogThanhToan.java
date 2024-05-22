package com.example.khachhang.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.khachhang.MucActivity.Activity_VeMayBayDaMua;
import com.example.khachhang.R;
import com.example.khachhang.doiTuong.VeMayBayDaBan;

import java.text.DecimalFormat;

public class DialogThanhToan extends Dialog {
    private Context mcontext;
    public DialogThanhToan(@NonNull Context context, int giave,String ten,int sdt, String maChuyenBay,int soLuong) {
        super(context);
        this.mcontext=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_thanhtoan_camon);
        setCancelable(false);
        Window window =getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // Cho phép dialog hiển thị ra toàn bộ màn hình
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            params.gravity = Gravity.TOP; // Hiển thị ở phía dưới màn hình
            params.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều rộng là match parent
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
        ImageButton im_veHome=(ImageButton) findViewById(R.id.dialog_camon_vehome);
        TextView txt_giatien=(TextView) findViewById(R.id.dialog_camon_giaten);
        TextView txt_ten=(TextView) findViewById(R.id.dialog_camon_hoten);
        TextView txt_sdt=(TextView) findViewById(R.id.dialog_camon_sdt);
        TextView txt_giatien2=(TextView) findViewById(R.id.dialog_camon_sotien);
        TextView txt_noidung=(TextView) findViewById(R.id.dialog_camon_noidung);
        TextView txt_ma=(TextView) findViewById(R.id.dialog_camon_ma);
        im_veHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,Activity_VeMayBayDaMua.class);
                mcontext.startActivity(intent);
            }
        });
        giave=giave*soLuong;
        float giaved=(float) giave;
        DecimalFormat dfgiaTien=new DecimalFormat("#,###đ");
        String giave1=dfgiaTien.format(giaved);


        txt_giatien.setText(giave1);
        txt_ten.setText(ten);
        txt_sdt.setText("0"+sdt);
        txt_giatien2.setText(giave1);
        txt_noidung.setText("Thanh toán vé máy bay\nMã: "+maChuyenBay+"\nSố Lượng: "+soLuong);
        txt_ma.setText(maChuyenBay);
    }
}
