package com.mrabid.detectdiseases.Model;

import java.io.Serializable;

public class Penyakit implements Serializable {
    private String gambar;
    private String judul;
    private String deskripsi;

    public Penyakit(String gambar, String judul, String deskripsi) {
        this.gambar = gambar;
        this.judul = judul;
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
