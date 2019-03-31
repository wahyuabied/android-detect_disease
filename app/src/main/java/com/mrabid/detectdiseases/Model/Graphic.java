package com.mrabid.detectdiseases.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Graphic implements Serializable {
    private ArrayList<String> early_x;
    private ArrayList<String> early_y;
    private ArrayList<String> late_x;
    private ArrayList<String> late_y;
    private ArrayList<String> sehat_x;
    private ArrayList<String> sehat_y;

    public Graphic(ArrayList<String> early_x, ArrayList<String> early_y, ArrayList<String> late_x, ArrayList<String> late_y, ArrayList<String> sehat_x, ArrayList<String> sehat_y) {
        this.early_x = early_x;
        this.early_y = early_y;
        this.late_x = late_x;
        this.late_y = late_y;
        this.sehat_x = sehat_x;
        this.sehat_y = sehat_y;
    }

    public Graphic(Graphic graphic) {
        this.early_x = graphic.early_x;
        this.early_y = graphic.early_y;
        this.late_x = graphic.late_x;
        this.late_y = graphic.late_y;
        this.sehat_x = graphic.sehat_x;
        this.sehat_y = graphic.sehat_y;
    }

    public ArrayList<String> getEarly_x() {
        return early_x;
    }

    public void setEarly_x(ArrayList<String> early_x) {
        this.early_x = early_x;
    }

    public ArrayList<String> getEarly_y() {
        return early_y;
    }

    public void setEarly_y(ArrayList<String> early_y) {
        this.early_y = early_y;
    }

    public ArrayList<String> getLate_x() {
        return late_x;
    }

    public void setLate_x(ArrayList<String> late_x) {
        this.late_x = late_x;
    }

    public ArrayList<String> getLate_y() {
        return late_y;
    }

    public void setLate_y(ArrayList<String> late_y) {
        this.late_y = late_y;
    }

    public ArrayList<String> getSehat_x() {
        return sehat_x;
    }

    public void setSehat_x(ArrayList<String> sehat_x) {
        this.sehat_x = sehat_x;
    }

    public ArrayList<String> getSehat_y() {
        return sehat_y;
    }

    public void setSehat_y(ArrayList<String> sehat_y) {
        this.sehat_y = sehat_y;
    }
}
