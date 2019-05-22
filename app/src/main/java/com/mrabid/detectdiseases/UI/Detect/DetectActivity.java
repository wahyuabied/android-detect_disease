package com.mrabid.detectdiseases.UI.Detect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetectActivity extends AppCompatActivity {

    ImageView imageView,back;
    TextView judul;
    Double meanData,medianData,stDeviasiData;
    Bitmap resizedBitmap,bitmap;
    Button btn, showGraph;
    ProgressBar progressBar;
    FeatureExtraction data;
    SharedPref sharedPrefeLocal;
    SharedPreferences sharedPref;
    CardView changeImage;
    SharedPreferences.Editor prefs;
    Uri uri;
    Graphic grafikPenyakit;
    ViewPager viewPager;
    TabLayout tabLayout;
    CheckBox cbAutoLevel;
    DetectAdapter adapter;
    RequestBody autoLevelRB;
    MultipartBody.Part body;
    AlertDialog.Builder alert,alertPlant ;
    EditText edittext ;
    String autoLevel = "no";

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
        cbAutoLevel = findViewById(R.id.cb_detectActivity_autoLevel);
        tabLayout = findViewById(R.id.tbl_detectActivity);
        back = findViewById(R.id.img_back_toolbar_detect_activity);
        changeImage = findViewById(R.id.cv_photo_change_image_detectActivity);
        imageView = findViewById(R.id.img_detect_activity);
        btn = findViewById(R.id.btn_process_detect_activity);
        showGraph = findViewById(R.id.btn_show_graph_detect_activity);
        judul = findViewById(R.id.tv_judul_detectActivity);
        //set Adapter
        adapter = new DetectAdapter(getSupportFragmentManager(), DetectActivity.this);

        judul.setText("Deteksi Penyakit "+getIntent().getStringExtra("tanaman"));

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

        cbAutoLevel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if(sharedPref.getString("information","yes").equalsIgnoreCase("yes")){
                        autoLevelDialog();
                        autoLevel = "yes";
                    }
                }else
                    autoLevel="no";
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert = new AlertDialog.Builder(DetectActivity.this);
                alertPlant = new AlertDialog.Builder(DetectActivity.this);
                alertPlant.setCancelable(false);
                alert.setCancelable(false);
                edittext = new EditText(DetectActivity.this);
                progressBar.setVisibility(View.VISIBLE);
                int color[] = PreProcessing.isPlant(resizedBitmap);
                if(color[1]<color[0] || color[1]<color[2]){
                    alertPlant.setTitle("Daun tidak terdeteksi apakah ingin tetap mengirim gambar ?");
                    alertPlant.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkPlant();
                        }
                    });
                    alertPlant.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    alertPlant.show();
                }else {
                    checkPlant();
                }
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
                            i.putExtra("penyakit",data.getPenyakit());
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

    public void sendImage(final String tanaman){
        if(tanaman.equalsIgnoreCase("Kentang")){
            autoLevelRB = RequestBody.create(MediaType.parse("text/plain"), autoLevel);
            Services.buildPicture().getFeatureExtraction(body,autoLevelRB).enqueue(new Callback<FeatureExtraction>() {
                @Override
                public void onResponse(Call<FeatureExtraction> call, Response<FeatureExtraction> response) {
                    data = new FeatureExtraction(response.body());
                    meanData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getMean())));
                    medianData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getMedian())));
                    stDeviasiData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getStandart_deviasi())));
                    showGraph.setVisibility(View.VISIBLE);

                    //send umur daun dan data with shared pref
                    prefs.putString("daun",tanaman);
                    prefs.putString("featureExtraction",new Gson().toJson(data));
                    prefs.apply();
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
        }else{
            Services.buildPicture().getFeatureExtractionTomat(body).enqueue(new Callback<FeatureExtraction>() {
                @Override
                public void onResponse(Call<FeatureExtraction> call, Response<FeatureExtraction> response) {
                    data = new FeatureExtraction(response.body());
                    meanData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getMean())));
                    medianData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getMedian())));
                    stDeviasiData = ChangeComma(new DecimalFormat("##.##").format(Double.parseDouble(response.body().getStandart_deviasi())));
                    showGraph.setVisibility(View.VISIBLE);

                    //send umur daun dan data with shared pref
                    prefs.putString("daun",tanaman);
                    prefs.putString("featureExtraction",new Gson().toJson(data));
                    prefs.apply();
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
    }

    public void checkPlant(){
        if (ActivityCompat.checkSelfPermission(DetectActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(DetectActivity.this, "Maaf kami perlu akses untuk menyimpan file", Toast.LENGTH_SHORT).show();
        }
        File file =  PreProcessing.bitmapToFile(resizedBitmap);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        sendImage(getIntent().getStringExtra("tanaman"));
    }

    public void autoLevelDialog(){
        final Dialog dialog = new Dialog(DetectActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_autolevel);

        ImageView close = dialog.findViewById(R.id.iv_close_dialogautoLevel);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView content = dialog.findViewById(R.id.tv_autoLevel_dialogautoLevel);
        content.setText("Aplikasi akan menyesuaikan kecerahan foto untuk proses (recommend kamera HP)");
        CheckBox cbIngatkan = dialog.findViewById(R.id.cb_autoLevel_jangan);
        cbIngatkan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    prefs.putString("information","no");
                    prefs.apply();
                }else{
                    prefs.putString("information","yes");
                    prefs.apply();
                }
            }
        });

        dialog.show();

    }


}
