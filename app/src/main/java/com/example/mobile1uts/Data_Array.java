package com.example.mobile1uts;

public class Data_Array {

    private String id;
    private String kode;
    private String nama;
    private String kondisi;
    private String imageUrl;

    public Data_Array(String kode, String nama, String kondisi, String imageUrl){
        this.kode = kode;
        this.nama = nama;
        this.kondisi = kondisi;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
