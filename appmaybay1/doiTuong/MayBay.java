package com.example.appmaybay1.doiTuong;

public class MayBay {
    private int id;
    private String maMayBay;
    private String tenMayBay;
    private int soGhe;

    public MayBay(int id, String maMayBay, String tenMayBay, int soGhe) {
        this.id = id;
        this.maMayBay = maMayBay;
        this.tenMayBay = tenMayBay;
        this.soGhe = soGhe;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaMayBay() {
        return maMayBay;
    }

    public void setMaMayBay(String maMayBay) {
        this.maMayBay = maMayBay;
    }

    public String getTenMayBay() {
        return tenMayBay;
    }

    public void setTenMayBay(String tenMayBay) {
        this.tenMayBay = tenMayBay;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }
}
