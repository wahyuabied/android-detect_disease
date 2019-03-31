package com.mrabid.detectdiseases.UI.Pestisida;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mrabid.detectdiseases.R;

import static android.view.View.GONE;

public class PestisidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pestisida);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView judul = toolbar.findViewById(R.id.tv_judul_pestisidaActivity);
        final ProgressBar progressBar = findViewById(R.id.pb_pestisidaActivity);
        ImageView back = toolbar.findViewById(R.id.img_back);
        final WebView myWebViewDisply = (WebView) findViewById(R.id.wv_pestisidaActivity);

        judul.setText(getIntent().getStringExtra("judul"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        progressBar.setVisibility(GONE);
        myWebViewDisply.setVisibility(View.VISIBLE);
        myWebViewDisply.getSettings().setJavaScriptEnabled(true);
        myWebViewDisply.loadUrl("javascript:<script>(window.onload = function() { " +
                "(elem1 = document.getElementById('/header/')); elem.parentNode.removeChild(elem1); " +
                "(elem2 = document.getElementById('/footer/')); elem2.parentNode.removeChild(elem2); " +
                "(elem3 = document.getElementByClassName('field field--name-dynamic-block-fieldnode-related-products field--type-ds field--label-above')); elem2.parentNode.removeChild(elem3); " +
                "})()</script>"+getIntent().getStringExtra("url"));

    }
}
