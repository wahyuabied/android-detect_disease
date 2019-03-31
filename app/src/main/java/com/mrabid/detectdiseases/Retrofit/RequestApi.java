package com.mrabid.detectdiseases.Retrofit;

import com.mrabid.detectdiseases.Model.FeatureExtraction;
import com.mrabid.detectdiseases.Model.Graphic;
import com.mrabid.detectdiseases.Model.Hama;
import com.mrabid.detectdiseases.Model.Pestisida;
import com.mrabid.detectdiseases.Model.Weather;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Mr Abid on 9/12/2018.
 */


public interface RequestApi {
    public static String appid="3f0c1424f67bea25a2f2f82763a74a13";

    @GET("http://api.openweathermap.org/data/2.5/weather")
    Call<Weather> getWeather(@Query("lat")String lat,@Query("lon") String lon,@Query("appid") String appid);

    @Multipart
    @POST("api/feature-extraction-kentang")
    Call<FeatureExtraction> getFeatureExtraction(@Part MultipartBody.Part image);

    @GET("api/Graph-Disease")
    Call<Graphic> getGraph();

    @GET("api/get-penyakit")
    Call<ArrayList<Hama>> getPenyakit();

    @GET("api/get-pestisida")
    Call<ArrayList<Pestisida>> getPestisida();
}
