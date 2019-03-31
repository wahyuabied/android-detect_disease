package com.mrabid.detectdiseases.UI.Pestisida;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mrabid.detectdiseases.Model.Pestisida;
import com.mrabid.detectdiseases.R;
import com.mrabid.detectdiseases.Retrofit.CallbackRetrofit;
import com.mrabid.detectdiseases.Retrofit.Services;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPestisidaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back;
    android.support.v7.widget.Toolbar toolbar;
    ProgressBar progressBar;
    ArrayList<Pestisida> listPestisida= new ArrayList<>();
    PestisidaAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pestisida);

        toolbar = findViewById(R.id.toolbar);
        back = toolbar.findViewById(R.id.img_back);
        recyclerView = findViewById(R.id.rv_listPestisidaActivity);
        progressBar = findViewById(R.id.progress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData(new CallbackRetrofit() {
            @Override
            public void onSuccess(Boolean result) {
                    if (result){
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListPestisidaActivity.this);
                        mAdapter = new PestisidaAdapter(ListPestisidaActivity.this,listPestisida);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(mAdapter);
                    }
            }
        });

    }

    public void getData(final CallbackRetrofit callbackRetrofit){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Services.buildPicture().getPestisida().enqueue(new Callback<ArrayList<Pestisida>>() {
            @Override
            public void onResponse(Call<ArrayList<Pestisida>> call, Response<ArrayList<Pestisida>> response) {
                listPestisida = response.body();
                callbackRetrofit.onSuccess(true);
            }

            @Override
            public void onFailure(Call<ArrayList<Pestisida>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(ListPestisidaActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                callbackRetrofit.onSuccess(false);
            }
        });
    }

}
