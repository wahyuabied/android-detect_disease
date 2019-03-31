package com.mrabid.detectdiseases.UI.Penyakit;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrabid.detectdiseases.Model.Hama;
import com.mrabid.detectdiseases.R;
import com.mrabid.detectdiseases.Retrofit.Services;
import com.squareup.picasso.Picasso;

public class DetailPenyakitActivity extends AppCompatActivity {

    TextView tJudul,tSolusi,tGejala,tDeskripsi;
    Toolbar toolbar;
    ImageView back,ivGambar;
    Hama hama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penyakit);

        toolbar = findViewById(R.id.toolbar);
        back = toolbar.findViewById(R.id.img_back);
        ivGambar = findViewById(R.id.img_detailPenyakitActivity);
        tJudul = toolbar.findViewById(R.id.tv_judul_detailPenyakit);
        tSolusi = findViewById(R.id.tv_solusi_detailPenyakitActivity);
        tGejala = findViewById(R.id.tv_gejala_detailPenyakitActivity);
        tDeskripsi = findViewById(R.id.tv_deskripsi_detailPenyakitActivity);

        hama = (Hama) getIntent().getSerializableExtra("penyakit");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tSolusi.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            tDeskripsi.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            tGejala.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        setData(hama);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void setData(Hama x){
        String[] solution ;
        tJudul.setText(x.getName());
        Picasso.with(DetailPenyakitActivity.this).load(Services.gambar+x.getGambar()).into(ivGambar);
        tDeskripsi.setText(x.getDeskripsi());
        tGejala.setText(x.getGejala());
        solution = x.getSolusi().split("\\.");
        for (int i=0;i<solution.length;i++){
            tSolusi.append("- "+solution[i]+"\n");
        }

    }
}
