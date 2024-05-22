package com.example.khachhang.doiTuong;

import java.io.Serializable;

public class DiaChi implements Serializable {
    private int id;
    private String tenDiaChi;
    private String maDiaChi;
    private String tenSanBay;

    public DiaChi(int id, String tenDiaChi, String maDiaChi, String tenSanBay) {
        this.id = id;
        this.tenDiaChi = tenDiaChi;
        this.maDiaChi = maDiaChi;
        this.tenSanBay = tenSanBay;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDiaChi() {
        return tenDiaChi;
    }

    public void setTenDiaChi(String tenDiaChi) {
        this.tenDiaChi = tenDiaChi;
    }

    public String getMaDiaChi() {
        return maDiaChi;
    }

    public void setMaDiaChi(String maDiaChi) {
        this.maDiaChi = maDiaChi;
    }

    public String getTenSanBay() {
        return tenSanBay;
    }

    public void setTenSanBay(String tenSanBay) {
        this.tenSanBay = tenSanBay;
    }
}
