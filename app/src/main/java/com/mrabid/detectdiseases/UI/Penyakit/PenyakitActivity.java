package com.mrabid.detectdiseases.UI.Penyakit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mrabid.detectdiseases.Model.Hama;
import com.mrabid.detectdiseases.R;
import com.mrabid.detectdiseases.Retrofit.CallbackRetrofit;
import com.mrabid.detectdiseases.Retrofit.Services;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenyakitActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    ImageView imageView;
    RecyclerView recyclerView;
    PenyakitAdapter mAdapter;
    ArrayList<Hama> penyakit = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyakit);

        toolbar = findViewById(R.id.toolbar);
        imageView = toolbar.findViewById(R.id.img_back);
        recyclerView = findViewById(R.id.rv_penyakitActivity);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loding..");
        progressDialog.setCancelable(false);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData(new CallbackRetrofit() {
            @Override
            public void onSuccess(Boolean result) {
                if (result){
                    progressDialog.dismiss();
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PenyakitActivity.this);
                    mAdapter = new PenyakitAdapter(penyakit,PenyakitActivity.this);
                    ((LinearLayoutManager)mLayoutManager).setStackFromEnd(true);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    public void getData(final CallbackRetrofit callbackRetrofit){
        progressDialog.show();
        Services.buildPicture().getPenyakit().enqueue(new Callback<ArrayList<Hama>>() {
            @Override
            public void onResponse(Call<ArrayList<Hama>> call, Response<ArrayList<Hama>> response) {
                penyakit = response.body();
                callbackRetrofit.onSuccess(true);
            }

            @Override
            public void onFailure(Call<ArrayList<Hama>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Response",t.toString());
                Toast.makeText(PenyakitActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                callbackRetrofit.onSuccess(false);
            }
        });
    }
}
