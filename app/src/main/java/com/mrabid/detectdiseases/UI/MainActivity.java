package com.mrabid.detectdiseases.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrabid.detectdiseases.Helper.PreProcessing;
import com.mrabid.detectdiseases.Helper.SharedPref;
import com.mrabid.detectdiseases.Model.Weather;
import com.mrabid.detectdiseases.R;
import com.mrabid.detectdiseases.Retrofit.CallbackRetrofit;
import com.mrabid.detectdiseases.Retrofit.RequestApi;
import com.mrabid.detectdiseases.Retrofit.Services;
import com.mrabid.detectdiseases.UI.Detect.DetectActivity;
import com.mrabid.detectdiseases.UI.Penyakit.PenyakitActivity;
import com.mrabid.detectdiseases.UI.Pestisida.ListPestisidaActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private boolean doubleBackToExit = false;
    CardView kentang,tomato,penyakit,pestisida;
    SharedPref sharedPref;
    TextView date, temperature, country, city, humidity, windSpeed,main;
    ImageView imageView,info;
    String latitude = "", longitude = "";
    LocationManager locationManager;
    Location tempLocation;
    Weather cuaca = new Weather();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = new SharedPref(this);

        main = findViewById(R.id.tv_main_main_activity);
        humidity = findViewById(R.id.tv_humidity_main_activity);
        imageView = findViewById(R.id.iv_icon_main_activity);
        windSpeed = findViewById(R.id.tv_wind_speed_main_activity);
        date = findViewById(R.id.tv_date_main_activity);
        temperature = findViewById(R.id.tv_temperature_main_activity);
        country = findViewById(R.id.tv_country_main_activity);
        city = findViewById(R.id.tv_city_main_activity);
        kentang = findViewById(R.id.cv_detect_pict_main_activity);
        tomato = findViewById(R.id.cv_detect_tomato_main_activity);
        penyakit = findViewById(R.id.cv_penyakit_main_activity);
        pestisida = findViewById(R.id.cv_pestisida_main_activity);
        info = findViewById(R.id.iv_info_mainActivity);
        //Body Code
        kentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picButtonKentang();
            }
        });
        penyakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PenyakitActivity.class));
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,InformActivity.class));
            }
        });

        tomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picButtonTomat();
            }
        });
        pestisida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(MainActivity.this,ListPestisidaActivity.class)); }
        });


        date.setText(getCurrentDate());
        body();
    }

    public void body(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    body();
                }
            },2000);
        }else{
            getLocation();
            getData(new CallbackRetrofit() {
                @Override
                public void onSuccess(Boolean result) {
                    //if there is internet conn
                    if(result==true){
                        Toast.makeText(MainActivity.this, latitude+" "+longitude, Toast.LENGTH_SHORT).show();
                        Resources res = getResources();
                        String mDrawableName = "a"+cuaca.getWeather().get(0).getIcon();
                        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
                        Drawable drawable = res.getDrawable(resID );
                        imageView.setImageDrawable(drawable);
                        try {
                            main.setText(cuaca.getWeather().get(0).getMain()+"");
                            temperature.setText(PreProcessing.kelvinCelcius(cuaca.getMain().getTemp()+"")+" "+(char)0x00B0+"C");
                            humidity.setText(cuaca.getMain().getHumidity()+"");
                            windSpeed.setText(cuaca.getWind().getSpeed()+"");
                            sharedPref.saveData("suhu",cuaca.getMain().getTemp()+"");
                            // below is for location
                            Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                            List<Address> addressList = gcd.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                            if (addressList.size() > 0) {
                                country.setText(addressList.get(0).getAdminArea()+"");
                                city.setText(addressList.get(0).getSubLocality()+"");
                            } else {
                                country.setText("Undifined");
                                city.setText("Undifined");
                            }
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Something error with location", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //if there is not internet conn
                        imageView.setImageResource(R.drawable.undifined);
                        country.setText("Need Connection");
                        city.setText("Need Connection");
                        main.setText("?");
                        sharedPref.saveData("suhu","0");
                        temperature.setText("? K");
                        humidity.setText("?");
                        windSpeed.setText("?");
                        Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd - MMMM - yyyy ");
        String date = mdformat.format(calendar.getTime()) + "";
        return date;
    }

    public void picButtonKentang() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose one of both")
                .setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        } else {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickPhoto.setType("image/*");
                            startActivityForResult(pickPhoto, 1);
                        }
                    }
                });
        builder.show();
    }

    public void picButtonTomat(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose one of both")
                .setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 2);
                        } else {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickPhoto.setType("image/*");
                            startActivityForResult(pickPhoto, 3);
                        }
                    }
                });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Intent i = new Intent(MainActivity.this, DetectActivity.class);
        try {
            //0(kamera) dan 1(galeri) untuk Kentang || 2(kamera) dan 3(galeri) untuk tomat
            if (requestCode == 0) {
                Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                i.putExtra("image", selectedImage);
                i.putExtra("tanaman","Kentang");
                try{
                    if(selectedImage!=null){ startActivity(i); }
                }catch (Exception e){
                    Log.e("MainActivity", "Null selected");
                }
            } else if(requestCode == 1) {
                Uri uri = imageReturnedIntent.getData();
                i.putExtra("uri", uri);
                i.putExtra("tanaman","Kentang");
                startActivity(i);
            }else if(requestCode == 2) {
                i = new Intent(MainActivity.this, DetectActivity.class);
                Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                i.putExtra("image", selectedImage);
                i.putExtra("tanaman","Tomat");
                try{
                    if(selectedImage!=null){ startActivity(i); }
                }catch (Exception e){
                    Log.e("MainActivity", "Null selected");
                }
            }else{
                i = new Intent(MainActivity.this, DetectActivity.class);
                Uri uri = imageReturnedIntent.getData();
                i.putExtra("uri", uri);
                i.putExtra("tanaman","Tomat");
                startActivity(i);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Null selected");
        }

    }

    public void getData(final CallbackRetrofit callback) {
       Log.e("Response",latitude+" dan "+longitude);
            if(latitude.equalsIgnoreCase("")){
                try{
                    latitude = sharedPref.loadData("latitude");
                    longitude = sharedPref.loadData("longitude");
                }catch (Exception e){
                    latitude = "0";
                    longitude = "0";
                }
            }
          Services.buildServiceClient().getWeather(latitude,longitude,RequestApi.appid).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                cuaca = response.body();
                callback.onSuccess(true);
            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                callback.onSuccess(false);
                Log.e("Response", t.toString());
            }
        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
            if(longitude.equalsIgnoreCase("")){
                tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                latitude = tempLocation.getLatitude()+"";
                longitude = tempLocation.getLongitude()+"";
                sharedPref.saveData("longitude",longitude);
                sharedPref.saveData("latitude",latitude);
            }
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude()+"";
        latitude = location.getLatitude()+"";
        sharedPref.saveData("longitude",longitude);
        sharedPref.saveData("latitude",latitude);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExit){
            super.onBackPressed();
            return;
        }
        this.doubleBackToExit = true;
        Toast.makeText(this, "Please click BACK again to Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, 2000);
    }
}
