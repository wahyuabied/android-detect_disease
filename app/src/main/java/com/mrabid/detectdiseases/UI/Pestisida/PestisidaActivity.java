package com.mrabid.detectdiseases.UI.Pestisida;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrabid.detectdiseases.R;

public class PestisidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pestisida);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView judul = toolbar.findViewById(R.id.tv_judul_pestisidaActivity);
        Button beli = findViewById(R.id.btn_buy_pestisidaActivity);
        ImageView back = toolbar.findViewById(R.id.img_back);
        final WebView myWebViewDisply = (WebView) findViewById(R.id.wv_pestisidaActivity);

        judul.setText(getIntent().getStringExtra("judul"));

        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getIntent().getStringExtra("tokopedia_search")));
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        myWebViewDisply.getSettings().setJavaScriptEnabled(true);
        myWebViewDisply.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url)
            {
                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('header').remove();})()");
                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('footer').style.display='none';})()");
            }
        });

        myWebViewDisply.loadUrl(getIntent().getStringExtra("url"));





    }
}
