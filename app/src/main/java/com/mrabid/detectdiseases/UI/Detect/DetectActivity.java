package com.mrabid.detectdiseases.UI.Detect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.mrabid.detectdiseases.Fragment.DetailFragment;
import com.mrabid.detectdiseases.Fragment.SolutionFragment;
import com.mrabid.detectdiseases.Helper.PreProcessing;
import com.mrabid.detectdiseases.Helper.SharedPref;
import com.mrabid.detectdiseases.Model.FeatureExtraction;
import com.mrabid.detectdiseases.Model.Graphic;
import com.mrabid.detectdiseases.R;
import com.mrabid.detectdiseases.Retrofit.CallbackRetrofit;
import com.mrabid.detectdiseases.Retrofit.Services;
import com.mrabid.detectdiseases.UI.GraphActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetectActivity extends AppCompatActivity {

    ImageView imageView,back;
    GraphView graphView;
    TextView kondisi,stadium,penjelasan,saran,sebab;
    Double meanData,medianData,stDeviasiData;
    Bitmap resizedBitmap,bitmap;
    Button btn, showGraph;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    FeatureExtraction data;
    SharedPref sharedPrefeLocal;
    SharedPreferences sharedPref;
    CardView changeImage;
    SharedPreferences.Editor prefs;
    Uri uri;
    Graphic grafikPenyakit;
    ViewPager viewPager;
    ArrayList<ArrayList<String>> solution;
    TabLayout tabLayout;
    DetectAdapter adapter;

    DetailFragment detailFragment = new DetailFragment();

    @SuppressLint({"WrongConstant", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        sharedPref = this.getSharedPreferences("data",MODE_NO_LOCALIZED_COLLATORS);
        prefs = sharedPref.edit();
        sharedPrefeLocal = new SharedPref(DetectActivity.this);


        progressBar = findViewById(R.id.progressBar_detectActivity);
        viewPager = findViewById(R.id.vp_detectActivity);
        tabLayout = findViewById(R.id.tbl_detectActivity);
        back = findViewById(R.id.img_back_toolbar_detect_activity);
        changeImage = findViewById(R.id.cv_photo_change_image_detectActivity);
        imageView = findViewById(R.id.img_detect_activity);
        linearLayout = findViewById(R.id.lnr_detail);
        btn = findViewById(R.id.btn_process_detect_activity);
        kondisi = findViewById(R.id.tv_kondisi_detect_activity);
        stadium = findViewById(R.id.tv_stadium_detect_activity);
        penjelasan = findViewById(R.id.tv_penjelasan_detect_activity);
        graphView = findViewById(R.id.graph_detect_activity);
        saran = findViewById(R.id.tv_saran_detect_activity);
        sebab = findViewById(R.id.tv_sebab_detect_activity);
        showGraph = findViewById(R.id.btn_show_graph_detect_activity);
        //set Adapter
        adapter = new DetectAdapter(getSupportFragmentManager(), DetectActivity.this);

        bitmap = (Bitmap)getIntent().getParcelableExtra("image");
            if(bitmap==null){
                uri = (Uri)getIntent().getParcelableExtra("uri");
                try {
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                }catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        resizedBitmap = bitmap.createScaledBitmap(bitmap,256,256,true);
        imageView.setImageBitmap(resizedBitmap);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (ActivityCompat.checkSelfPermission(DetectActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetectActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            return;
        }

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DetectActivity.this);
                final EditText edittext = new EditText(DetectActivity.this);
                progressBar.setVisibility(View.VISIBLE);
                alert.setTitle("Berapa Umur Tanaman ini ?(bulan)");
                alert.setView(edittext);
                alert.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //remove all content adapter
                        if (ActivityCompat.checkSelfPermission(DetectActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(DetectActivity.this, "Maaf kami perlu akses untuk menyimpan file", Toast.LENGTH_SHORT).show();
                        }
                        Editable YouEditTextValue = edittext.getText();
                        File file =  PreProcessing.bitmapToFile(resizedBitmap);
                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

                        Services.buildPicture().getFeatureExtraction(body).enqueue(new Callback<FeatureExtraction>() {
                            @Override
                            public void onResponse(Call<FeatureExtraction> call, Response<FeatureExtraction> response) {
                                data = new FeatureExtraction(response.body());
                                meanData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getMean())));
                                medianData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getMedian())));
                                stDeviasiData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getStandart_deviasi())));
                                showGraph.setVisibility(View.VISIBLE);

                                //send umur daun dan data with shared pref
                                prefs.putString("umur",edittext.getText()+"");
                                prefs.putString("daun","Kentang");
                                prefs.putString("featureExtraction",new Gson().toJson(data));
                                prefs.apply();

//                                sendSolution(data);
                                PreProcessing.saveArrayList("solusi",solution,DetectActivity.this);

                                viewPager.setAdapter(adapter);
                                tabLayout.setupWithViewPager(viewPager);

                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onFailure(Call<FeatureExtraction> call, Throwable t) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.e("Response",t.toString());

                            }
                        });


                    }
                });
                alert.setNegativeButton("Tidak Jadi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                alert.show();
            }
        });

        showGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGraph(new CallbackRetrofit() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if(result){
                            Intent i = new Intent(DetectActivity.this,GraphActivity.class);
                            i.putExtra("graph",grafikPenyakit);
                            i.putExtra("dataMean",meanData+"");
                            i.putExtra("dataStDeviasi",stDeviasiData+"");
                            startActivity(i);
                        }
                    }
                });
            }
        });

        adapter.addFragment(new DetailFragment(), "Detail");
        adapter.addFragment(new SolutionFragment(), "Solution");

    }
    public void getGraph(final CallbackRetrofit callback){
        Services.buildPicture().getGraph().enqueue(new Callback<Graphic>() {
            @Override
            public void onResponse(Call<Graphic> call, Response<Graphic> response) {
                grafikPenyakit = new Graphic(response.body());
                callback.onSuccess(true);
            }
            @Override
            public void onFailure(Call<Graphic> call, Throwable t) {
                Log.e("Response",t.toString());
                callback.onSuccess(false);
            }
        });
    }
    public double ChangeComma(String x){
        return Double.parseDouble(new DecimalFormat("##.###").format(Double.parseDouble(x)));
    }

    public void changeImage() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetectActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try {
            Bitmap selectedImage = null;
            //0(kamera) dan 1(galeri) untuk Kentang || 2(kamera) dan 3(galeri) untuk tomat
            if (requestCode == 0) {
                selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
            } else {
                Uri url = imageReturnedIntent.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(url);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                }catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
            resizedBitmap = selectedImage.createScaledBitmap(selectedImage,256,256,true);
            imageView.setImageBitmap(resizedBitmap);
        } catch (Exception e) {
            Log.e("MainActivity", "Null selected");
        }
    }

}
