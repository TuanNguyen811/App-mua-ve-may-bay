package com.example.khachhang.doiTuong;

import java.io.Serializable;
import java.util.Calendar;

public class VeMayBay implements Serializable {
    private int id;
    private String maChuyenBay;
    private String maMayBay;
    private String tenTinhDi;
    private String tenTinhDen;
    private Calendar thoiGianDi;
    private Calendar thoiGianDen;
    private int soGhe;
    private int giaVe;
    private String thongTinThem;
    public VeMayBay(int id, String maChuyenBay, String maMayBay, String tenTinhDi, String tenTinhDen, Calendar thoiGianDi, Calendar thoiGianDen, int soGhe, int giaVe,String thongTinThem) {
        this.id = id;
        this.maChuyenBay = maChuyenBay;
        this.maMayBay = maMayBay;
        this.tenTinhDi = tenTinhDi;
        this.tenTinhDen = tenTinhDen;
        // Đặt thời gian cho thoiGianDi
        this.thoiGianDi = Calendar.getInstance();
        this.thoiGianDi.setTimeInMillis(thoiGianDi.getTimeInMillis());

        // Đặt thời gian cho thoiGianDen
        this.thoiGianDen = Calendar.getInstance();
        this.thoiGianDen.setTimeInMillis(thoiGianDen.getTimeInMillis());
        this.soGhe = soGhe;
        this.giaVe = giaVe;
        this.thongTinThem=thongTinThem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaChuyenBay() {
        return maChuyenBay;
    }

    public void setMaChuyenBay(String maChuyenBay) {
        this.maChuyenBay = maChuyenBay;
    }

    public String getMaMayBay() {
        return maMayBay;
    }

    public void setMaMayBay(String maMayBay) {
        this.maMayBay = maMayBay;
    }

    public String getTenTinhDi() {
        return tenTinhDi;
    }

    public void setTenTinhDi(String tenTinhDi) {
        this.tenTinhDi = tenTinhDi;
    }

    public String getTenTinhDen() {
        return tenTinhDen;
    }

    public void setTenTinhDen(String tenTinhDen) {
        this.tenTinhDen = tenTinhDen;
    }

    public Calendar getThoiGianDi() {
        return thoiGianDi;
    }

    public void setThoiGianDi(Calendar thoiGianDi) {
        this.thoiGianDi = thoiGianDi;
    }

    public Calendar getThoiGianDen() {
        return thoiGianDen;
    }

    public void setThoiGianDen(Calendar thoiGianDen) {
        this.thoiGianDen = thoiGianDen;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public int getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(int giaVe) {
        this.giaVe = giaVe;
    }

    public String getThongTinThem() {
        return thongTinThem;
    }

    public void setThongTinThem(String thongTinThem) {
        this.thongTinThem = thongTinThem;
    }
}

