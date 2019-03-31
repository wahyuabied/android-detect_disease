package com.mrabid.detectdiseases.Model;

import java.io.Serializable;

public class Hama implements Serializable {
    private String name;
    private String deskripsi;
    private String gejala;
    private String solusi;
    private String gambar;

    public Hama(String name, String deskripsi, String gejala, String solusi, String gambar) {
        this.name = name;
        this.deskripsi = deskripsi;
        this.gejala = gejala;
        this.solusi = solusi;
        this.gambar = gambar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGejala() {
        return gejala;
    }

    public void setGejala(String gejala) {
        this.gejala = gejala;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
