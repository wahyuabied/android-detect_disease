package com.mrabid.detectdiseases.Model;

import java.io.Serializable;

public class Kentang implements Serializable {
    private String kondisi;
    private String stadium;
    private String penjelasan;
    private String saran;
    private String penyebab;

    public Kentang(String kondisi, String stadium, String penjelasan, String saran, String penyebab) {
        this.kondisi = kondisi;
        this.stadium = stadium;
        this.penjelasan = penjelasan;
        this.saran = saran;
        this.penyebab = penyebab;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getPenjelasan() {
        return penjelasan;
    }

    public void setPenjelasan(String penjelasan) {
        this.penjelasan = penjelasan;
    }

    public String getSaran() {
        return saran;
    }

    public void setSaran(String saran) {
        this.saran = saran;
    }

    public String getPenyebab() {
        return penyebab;
    }

    public void setPenyebab(String penyebab) {
        this.penyebab = penyebab;
    }
}
