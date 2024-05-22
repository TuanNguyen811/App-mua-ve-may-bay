package com.example.khachhang.doiTuong;

public class KhachHangPhu {
    private String ten;
    private int sdt;

    public KhachHangPhu(String ten, int sdt) {
        this.ten = ten;
        this.sdt = sdt;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getSdt() {
        return sdt;
    }

    public void setSdt(int sdt) {
        this.sdt = sdt;
    }
}
