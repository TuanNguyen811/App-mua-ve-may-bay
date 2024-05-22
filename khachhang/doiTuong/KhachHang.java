package com.example.khachhang.doiTuong;

import java.io.Serializable;

public class KhachHang implements Serializable {
    private int id;
    private String hoVaTen;
    private String gmail;
    private String matKhau;
    private String diaChi;
    private int soDienThoai;
    private int cCCD;

    public KhachHang(int id, String hoVaTen, String gmail, String matKhau, String diaChi, int soDienThoai, int cCCD) {
        this.id = id;
        this.hoVaTen = hoVaTen;
        this.gmail = gmail;
        this.matKhau = matKhau;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.cCCD = cCCD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(int soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getcCCD() {
        return cCCD;
    }

    public void setcCCD(int cCCD) {
        this.cCCD = cCCD;
    }
}
