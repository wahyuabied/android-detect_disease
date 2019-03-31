package com.mrabid.detectdiseases;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.mrabid.detectdiseases.Helper.PreProcessing;
import com.mrabid.detectdiseases.Model.FeatureExtraction;
import com.mrabid.detectdiseases.Model.Graphic;
import com.mrabid.detectdiseases.Model.Kentang;
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

public class DetectTomatoActivity extends AppCompatActivity  {

    ImageView imageView,back;
    GraphView graphView;
    String suhu;
    Toolbar toolbar;
    TextView kondisi,stadium,penjelasan,saran,sebab,mean,median,stDeviasi;
    Double meanData,medianData,stDeviasiData;
    Bitmap resizedBitmap,bitmap;
    Button btn, showGraph;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    FeatureExtraction data;
    Uri uri;
    ArrayList<Kentang> jenis = new ArrayList<>();
    Graphic grafikPenyakit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_tomato);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Kami sedang berproses...");
        toolbar = findViewById(R.id.toolbar);
        back = toolbar.findViewById(R.id.img_back_toolbar_detect_activity);
        mean = findViewById(R.id.tv_mean_detect_activity);
        median = findViewById(R.id.tv_median_detect_activity);
        stDeviasi = findViewById(R.id.tv_standartDeviasi_detect_activity);
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

        bitmap = (Bitmap)getIntent().getParcelableExtra("image");
        if(bitmap==null){
            uri = (Uri)getIntent().getParcelableExtra("uri");
            try {
                InputStream imageStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        suhu = getIntent().getStringExtra("suhu");
        resizedBitmap = bitmap.createScaledBitmap(bitmap,256,256,true);
        imageView.setImageBitmap(resizedBitmap);
        setData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (ActivityCompat.checkSelfPermission(DetectTomatoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetectTomatoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            return;
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DetectTomatoActivity.this);
                final EditText edittext = new EditText(DetectTomatoActivity.this);
                alert.setTitle("Berapa Umur Tanaman ini ?(bulan)");
                alert.setView(edittext);
                alert.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        progressDialog.show();
                        if (ActivityCompat.checkSelfPermission(DetectTomatoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(DetectTomatoActivity.this, "Maaf kami perlu akses untuk menyimpan file", Toast.LENGTH_SHORT).show();
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
                                mean.setText(meanData+"");
                                median.setText(medianData+"");
                                stDeviasi.setText(stDeviasiData+"");
//                                showGraph.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<FeatureExtraction> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("Response",t.toString());
                            }
                        });

                    }
                });

                alert.setNegativeButton("Tidak Jadi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });

        showGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                getGraph(new CallbackRetrofit() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if(result){
                            DataPoint[] early = new DataPoint[grafikPenyakit.getEarly_x().size()];
                            ArrayList<ArrayList<String>> early_sort = sorted(grafikPenyakit.getEarly_x(),grafikPenyakit.getEarly_y());
                            for(int i=0;i<grafikPenyakit.getEarly_x().size();i++){
                                early[i] = new DataPoint(Double.parseDouble(early_sort.get(0).get(i)),Double.parseDouble(early_sort.get(1).get(i)));
                            }
                            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(early);
                            graphView.addSeries(series);
                            series.setCustomShape(new PointsGraphSeries.CustomShape() {
                                @Override
                                public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                                    paint.setStrokeWidth(5);
                                    canvas.drawCircle(x-10,y-10,10,paint);
                                }
                            });
                            series.setColor(Color.RED);

                            DataPoint[] late = new DataPoint[grafikPenyakit.getLate_x().size()];
                            ArrayList<ArrayList<String>> late_sort = sorted(grafikPenyakit.getLate_x(),grafikPenyakit.getLate_y());
                            for(int i=0;i<grafikPenyakit.getLate_x().size();i++){
                                late[i] = new DataPoint(Double.parseDouble(late_sort.get(0).get(i)),Double.parseDouble(late_sort.get(1).get(i)));
                            }
                            PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(late);
                            graphView.addSeries(series2);
                            series2.setCustomShape(new PointsGraphSeries.CustomShape() {
                                @Override
                                public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                                    paint.setStrokeWidth(5);
                                    canvas.drawCircle(x-10,y-10,10,paint);
                                }
                            });
                            series2.setColor(Color.BLUE);

                            DataPoint[] sehat = new DataPoint[grafikPenyakit.getSehat_x().size()];
                            ArrayList<ArrayList<String>> sehat_sort = sorted(grafikPenyakit.getSehat_x(),grafikPenyakit.getSehat_y());
                            for(int i=0;i<grafikPenyakit.getSehat_x().size();i++){
                                sehat[i] = new DataPoint(Double.parseDouble(sehat_sort.get(0).get(i)),Double.parseDouble(sehat_sort.get(1).get(i)));
                            }
                            PointsGraphSeries<DataPoint> series3 = new PointsGraphSeries<DataPoint>(sehat);
                            graphView.addSeries(series3);
                            series3.setCustomShape(new PointsGraphSeries.CustomShape() {
                                @Override
                                public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                                    paint.setStrokeWidth(5);
                                    canvas.drawCircle(x-10,y-10,10,paint);
                                }
                            });
                            series3.setColor(Color.GREEN);

                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[]{
                                    new DataPoint(stDeviasiData,meanData),
                            });
                            graphView.addSeries(series4);
                            series4.setShape(PointsGraphSeries.Shape.RECTANGLE);
                            series4.setColor(Color.YELLOW);


                            graphView.getViewport().setXAxisBoundsManual(true);
                            graphView.getViewport().setMaxX(70.0);
                            graphView.getViewport().setMinX(45.0);
                            graphView.getViewport().setYAxisBoundsManual(true);
                            graphView.getViewport().setMaxY(100.0);
                            graphView.getViewport().setMinY(60.0);

                            progressDialog.dismiss();
                            graphView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        graphView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetectTomatoActivity.this,GraphActivity.class);
                i.putExtra("graph",grafikPenyakit);
                i.putExtra("dataMean",meanData+"");
                i.putExtra("dataStDeviasi",stDeviasiData+"");
                startActivity(i);
            }
        });
    }

    public void setData(){
        jenis.add(new Kentang("Late Blight","Awal","Daun kentang terkena penyakit late blight dalam stadium awal, karena teridentifikasi bentuk penyakit melingkar pada lukanya, namun luka hanya pada sediki bagian daun","Pangkas daun yang sakit\nKeringkan tanah\nLakukan Aplikasi fungisida(mankozeb,propineb)15kaliperiode",
                "Pythopthora infestan"));
        jenis.add(new Kentang("Late Blight","Menengah","Daun kentang terkena penyakit late blight dalam stadium menengah, karena teridentifikasi bentuk penyakit melingkar pada lukanya, namun luka sudah menyebar hampir keseluruh daun","Lakukan pemanenan jika memungkinkan\nKeringkan tanah\nLakukan Aplikasi fungisida(mankozeb,propineb)15kaliperiode",
                "Pythopthora infestan"));
        jenis.add(new Kentang("Early Blight","Awal","Daun kentang terkena penyakit late blight dalam stadium menengah, karena teridentifikasi bentuk penyakit melingkar pada lukanya, namun luka sudah menyebar hampir keseluruh daun","Lakukan pemanenan jika memungkinkan\nKeringkan tanah\nLakukan Aplikasi fungisida(mankozeb,propineb)15kaliperiode",
                "Pythopthora infestan"));
        jenis.add(new Kentang("Early Blight","Menengah","Daun kentang terkena penyakit late blight dalam stadium menengah, karena teridentifikasi bentuk penyakit melingkar pada lukanya, namun luka sudah menyebar hampir keseluruh daun","Lakukan pemanenan jika memungkinkan\nKeringkan tanah\nLakukan Aplikasi fungisida(mankozeb,propineb)15kaliperiode",
                "Pythopthora infestan"));
        jenis.add(new Kentang("Sehat","-","-","-",
                "-"));
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

    public ArrayList<ArrayList<String>> sorted(ArrayList<String> x , ArrayList<String> y){
        double temp,temp2;
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        for(int i = 0;i<x.size();i++){
            for(int j=i+1;j<x.size();j++){
                if(Double.parseDouble(x.get(i))>Double.parseDouble(x.get(j))){
                    temp = Double.parseDouble(x.get(i));
                    x.set(i,x.get(j));
                    x.set(j,temp+"");
                    temp2 = Double.parseDouble(y.get(i));
                    y.set(i,y.get(j));
                    y.set(j,temp2+"");
                }
            }
        }
        data.add(x);data.add(y);
        return data;
    }

    public double ChangeComma(String x){
        return Double.parseDouble(new DecimalFormat("##.###").format(Double.parseDouble(x)));
    }

}
