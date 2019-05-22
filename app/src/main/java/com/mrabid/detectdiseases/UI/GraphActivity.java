package com.mrabid.detectdiseases.UI;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.mrabid.detectdiseases.Model.Graphic;
import com.mrabid.detectdiseases.R;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    Graphic graphicData ;
    double meanData,stDeviasiData;
    GraphView graphView;
    String penyakit,kelompok;
    ImageView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graphView = findViewById(R.id.graph_graph_activity);
        note = findViewById(R.id.iv_note_graphActivity);

        try{
            graphicData =(Graphic) getIntent().getSerializableExtra("graph");
            meanData = Double.parseDouble(getIntent().getStringExtra("dataMean"));
            stDeviasiData = Double.parseDouble(getIntent().getStringExtra("dataStDeviasi"));
            penyakit = getIntent().getStringExtra("penyakit");
            if(penyakit.equalsIgnoreCase("early")){
                penyakit = "Early Blight";
                kelompok = "Merah";
            }else if(penyakit.equalsIgnoreCase("sehat")){
                penyakit = "Sehat";
                kelompok = "Hijau";
            }else{
                penyakit = "Late Blight";
                kelompok = "Biru";
            }

        }catch (Exception e){
            Log.e("ErrorGraphActivity",e.toString());
        }

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNote(meanData+"",stDeviasiData+"",penyakit,kelompok);
            }
        });


        DataPoint[] early = new DataPoint[graphicData.getEarly_x().size()];
        ArrayList<ArrayList<String>> early_sort = sorted(graphicData.getEarly_x(),graphicData.getEarly_y());
        for(int i=0;i<graphicData.getEarly_x().size();i++){
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

        DataPoint[] late = new DataPoint[graphicData.getLate_x().size()];
        ArrayList<ArrayList<String>> late_sort = sorted(graphicData.getLate_x(),graphicData.getLate_y());
        for(int i=0;i<graphicData.getLate_x().size();i++){
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

        DataPoint[] sehat = new DataPoint[graphicData.getSehat_x().size()];
        ArrayList<ArrayList<String>> sehat_sort = sorted(graphicData.getSehat_x(),graphicData.getSehat_y());
        for(int i=0;i<graphicData.getSehat_x().size();i++){
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

    public void showNote(String mean,String sDeviasi,String penyakit,String kelompok){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_note);
        ImageView close = dialog.findViewById(R.id.iv_close_dialogNote);
        TextView content = dialog.findViewById(R.id.tv_note_dialogNote);

        content.setText("Pada grafik terdapat 3 warna titik yang berbeda,setiap titik adalah gambar yang telah dikelompokkan sesuai jenis penyakit. Gambar anda yang berwarna kuning cenderung lebih mengelompok ke kelompok "+kelompok+ " " +
                "yang artinya gambar anda sejenis dan berpenyakit "+penyakit +". Hal ini dikarenakan gambar daun memiliki ciri mean = "+mean+"(mewakili Y) dan standart deviasi = "+sDeviasi+"(mewakili X) setelah diproses oleh aplikasi");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            content.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
