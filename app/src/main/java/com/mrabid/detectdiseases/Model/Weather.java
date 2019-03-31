package com.mrabid.detectdiseases.Model;

import com.mrabid.detectdiseases.Model.sub_weather.Coordinate;
import com.mrabid.detectdiseases.Model.sub_weather.Main;
import com.mrabid.detectdiseases.Model.sub_weather.SubWeather;
import com.mrabid.detectdiseases.Model.sub_weather.Sys;
import com.mrabid.detectdiseases.Model.sub_weather.Wind;

import java.io.Serializable;
import java.util.ArrayList;

public class Weather implements Serializable {
    private Coordinate coord;
    private ArrayList<SubWeather> weather;
    private String base;
    private Main main;
    private Wind wind;
    private Sys sys;
    private int id;
    private String name;
    private int cod;


    public Weather() {
    }

    public Weather(Coordinate coord, ArrayList<SubWeather> weather, String base, Main main, Wind wind, Sys sys, int id, String name, int cod) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.wind = wind;
        this.sys = sys;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public ArrayList<SubWeather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<SubWeather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
