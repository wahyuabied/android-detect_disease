package com.mrabid.detectdiseases.Model;

public class Pestisida {
    private int id;
    private String nama;
    private String deskripsi;
    private String gambar;
    private String url;
    private String tokopedia_search;


    public Pestisida(Pestisida pestisida) {
        this.id = pestisida.getId();
        this.nama = pestisida.getNama();
        this.deskripsi = pestisida.getDeskripsi();
        this.gambar = pestisida.getGambar();
        this.url = pestisida.getUrl();
        this.tokopedia_search = pestisida.getTokopedia_search();
    }

    public String getTokopedia_search() {
        return tokopedia_search;
    }

    public void setTokopedia_search(String tokopedia_search) {
        this.tokopedia_search = tokopedia_search;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
