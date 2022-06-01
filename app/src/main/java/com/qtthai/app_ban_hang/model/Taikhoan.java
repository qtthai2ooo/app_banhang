package com.qtthai.app_ban_hang.model;

public class Taikhoan {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getDienthoai() {
        return dienthoai;
    }

    public void setDienthoai(String dienthoai) {
        this.dienthoai = dienthoai;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String ten;
    private String diachi;

    public Taikhoan(int id, String ten, String diachi, String dienthoai, String photo, String email) {
        this.id = id;
        this.ten = ten;
        this.diachi = diachi;
        this.dienthoai = dienthoai;
        this.photo = photo;
        this.email = email;
    }

    private String dienthoai;
    private String photo;
    private String email;

}
