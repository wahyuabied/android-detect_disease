package com.mrabid.detectdiseases.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mr Abid on 9/12/2018.
 */

public class Services {
    private static Retrofit.Builder builder = new Retrofit.Builder();
    public static String gambar = "http://157.230.252.251:5000";

    public static RequestApi buildServiceClient() {
        return builder.baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RequestApi.class);
    }

    public static RequestApi buildPicture() {
        return builder.baseUrl("http://157.230.252.251:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RequestApi.class);
    }
}
