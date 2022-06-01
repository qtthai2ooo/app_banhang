package com.qtthai.app_ban_hang.model;

public class Donhang {

    private String tentaikhoan;
    private String diachitaikhoan;

    public String getTentaikhoan() {
        return tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        this.tentaikhoan = tentaikhoan;
    }

    public String getDiachitaikhoan() {
        return diachitaikhoan;
    }

    public void setDiachitaikhoan(String diachitaikhoan) {
        this.diachitaikhoan = diachitaikhoan;
    }

    public String getDienthoaitaikhoan() {
        return dienthoaitaikhoan;
    }

    public void setDienthoaitaikhoan(String dienthoaitaikhoan) {
        this.dienthoaitaikhoan = dienthoaitaikhoan;
    }

    public String getPhototaikhoan() {
        return phototaikhoan;
    }

    public void setPhototaikhoan(String phototaikhoan) {
        this.phototaikhoan = phototaikhoan;
    }

    public String getEmailtaikhoan() {
        return emailtaikhoan;
    }

    public void setEmailtaikhoan(String emailtaikhoan) {
        this.emailtaikhoan = emailtaikhoan;
    }

    public int getIdtaikhoan() {
        return idtaikhoan;
    }

    public void setIdtaikhoan(int idtaikhoan) {
        this.idtaikhoan = idtaikhoan;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public String getHinhanhsanpham() {
        return hinhanhsanpham;
    }

    public void setHinhanhsanpham(String hinhanhsanpham) {
        this.hinhanhsanpham = hinhanhsanpham;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSoluongsanpham() {
        return soluongsanpham;
    }

    public void setSoluongsanpham(int soluongsanpham) {
        this.soluongsanpham = soluongsanpham;
    }

    public int getGiasanpham() {
        return giasanpham;
    }

    public void setGiasanpham(int giasanpham) {
        this.giasanpham = giasanpham;
    }

    private String dienthoaitaikhoan;
    private String phototaikhoan;
    private String emailtaikhoan;

    public Donhang(String tentaikhoan, String diachitaikhoan, String dienthoaitaikhoan, String phototaikhoan, String emailtaikhoan, int idtaikhoan, int id, String tensanpham, String hinhanhsanpham, String date, int soluongsanpham, int giasanpham) {
        this.tentaikhoan = tentaikhoan;
        this.diachitaikhoan = diachitaikhoan;
        this.dienthoaitaikhoan = dienthoaitaikhoan;
        this.phototaikhoan = phototaikhoan;
        this.emailtaikhoan = emailtaikhoan;
        this.idtaikhoan = idtaikhoan;
        Id = id;
        this.tensanpham = tensanpham;
        this.hinhanhsanpham = hinhanhsanpham;
        this.date = date;
        this.soluongsanpham = soluongsanpham;
        this.giasanpham = giasanpham;
    }

    private int idtaikhoan;

    public int Id;
    public String tensanpham;
    public String hinhanhsanpham;
    public String date;
    public int soluongsanpham;
    public int giasanpham;
}
