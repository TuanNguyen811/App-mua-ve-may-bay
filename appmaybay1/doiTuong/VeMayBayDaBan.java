package com.example.appmaybay1.doiTuong;

import java.util.Calendar;

public class VeMayBayDaBan {
    int id;
    String maChuyenBay;
    int soGhe;
    String gmailKhachHang;
    String tenKhachHang;
    int sdtKhachHang;
    String tinhDi;
    String tinhDen;
    Calendar ngayDi;
    int giaVe;
    String mayBay;

    public VeMayBayDaBan(int id, String maChuyenBay, int soGhe, String gmailKhachHang, String tenKhachHang, int sdtKhachHang, String tinhDi, String tinhDen, Calendar ngayDi, int giaVe, String mayBay) {
        this.id = id;
        this.maChuyenBay = maChuyenBay;
        this.soGhe = soGhe;
        this.gmailKhachHang = gmailKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.sdtKhachHang = sdtKhachHang;
        this.tinhDi = tinhDi;
        this.tinhDen = tinhDen;
        this.ngayDi = ngayDi;
        this.giaVe = giaVe;
        this.mayBay = mayBay;
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

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public String getGmailKhachHang() {
        return gmailKhachHang;
    }

    public void setGmailKhachHang(String gmailKhachHang) {
        this.gmailKhachHang = gmailKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public int getSdtKhachHang() {
        return sdtKhachHang;
    }

    public void setSdtKhachHang(int sdtKhachHang) {
        this.sdtKhachHang = sdtKhachHang;
    }

    public String getTinhDi() {
        return tinhDi;
    }

    public void setTinhDi(String tinhDi) {
        this.tinhDi = tinhDi;
    }

    public String getTinhDen() {
        return tinhDen;
    }

    public void setTinhDen(String tinhDen) {
        this.tinhDen = tinhDen;
    }

    public Calendar getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(Calendar ngayDi) {
        this.ngayDi = ngayDi;
    }

    public int getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(int giaVe) {
        this.giaVe = giaVe;
    }

    public String getMayBay() {
        return mayBay;
    }

    public void setMayBay(String mayBay) {
        this.mayBay = mayBay;
    }
}
