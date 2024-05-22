package com.example.khachhang.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.khachhang.R;
import com.example.khachhang.doiTuong.DiaChi;
import com.example.khachhang.my_interfave.ITFDiaChi;

public class DialogXemDanhSachDiaChi extends Dialog {
    @SuppressLint("MissingInflatedId")
    public DialogXemDanhSachDiaChi(@NonNull Context context, ITFDiaChi itfDiaChi) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_main_diachi);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // thiet lap thuoc tinh de hien thi dialog
        Window window =getWindow();

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // Cho phép dialog hiển thị ra toàn bộ màn hình
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.TOP; // Hiển thị ở phía dưới màn hình
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều rộng là match parent
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        /*
        WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);


            Window window = getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // Cho phép dialog hiển thị ra toàn bộ màn hình
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM; // Hiển thị ở phía dưới màn hình
            params.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều rộng là match parent
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);

         */

    }
}
