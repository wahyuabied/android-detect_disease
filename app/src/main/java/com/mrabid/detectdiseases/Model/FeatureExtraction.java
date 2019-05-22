package com.mrabid.detectdiseases.Model;

import java.io.Serializable;

public class FeatureExtraction implements Serializable {
    private int id;
    private String mean;
    private String median;
    private String standart_deviasi;
    private String saved_path;
    private String penyakit;
    private String fase;

    public FeatureExtraction(){
        super();
    }

    public FeatureExtraction(FeatureExtraction featureExtraction){
        this.mean = featureExtraction.getMean();
        this.median = featureExtraction.getMedian();
        this.standart_deviasi = featureExtraction.getStandart_deviasi();
        this.saved_path = featureExtraction.getSaved_path();
        this.penyakit = featureExtraction.getPenyakit();
        this.fase = featureExtraction.getFase();
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getMedian() {
        return median;
    }

    public void setMedian(String median) {
        this.median = median;
    }

    public String getStandart_deviasi() {
        return standart_deviasi;
    }

    public void setStandart_deviasi(String standart_deviasi) {
        this.standart_deviasi = standart_deviasi;
    }

    public String getSaved_path() {
        return saved_path;
    }

    public void setSaved_path(String saved_path) {
        this.saved_path = saved_path;
    }

    public String getPenyakit() {
        return penyakit;
    }

    public void setPenyakit(String penyakit) {
        this.penyakit = penyakit;
    }
}
