package com.mrabid.detectdiseases.UI.Pestisida;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrabid.detectdiseases.Model.Pestisida;
import com.mrabid.detectdiseases.R;
import com.mrabid.detectdiseases.Retrofit.Services;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PestisidaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Pestisida> listPestisida;
    Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public PestisidaAdapter(Context context, ArrayList<Pestisida> listPestisida) {
        this.context = context;
        this.listPestisida = listPestisida;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_ITEM: {
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_penyakit, null);
                return new ViewHolderOne(itemView);
            }
            case VIEW_PROG: {
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_pestisida, null);
                return new ViewHolderTwo(itemView);
            }
        }
        return null;
    }

    public int getItemCount() {
        return listPestisida.size();
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView judul, deskripsi;
        ImageView imageView;
        CardView cardView;

        public ViewHolderOne(View inflate) {
            super(inflate);
            cardView = inflate.findViewById(R.id.cv_itemPenyakit);
            judul = inflate.findViewById(R.id.tv_judul_itemPenyakit);
            deskripsi = inflate.findViewById(R.id.tv_deskripsi_item_penyakit);
            imageView = inflate.findViewById(R.id.img_itemPenyakit);
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {

        TextView judul, deskripsi;
        ImageView imageView;
        CardView cardView;

        public ViewHolderTwo(View inflate) {
            super(inflate);
            cardView = inflate.findViewById(R.id.cv_itemPenyakit);
            judul = inflate.findViewById(R.id.tv_judul_itemPestisida);
            deskripsi = inflate.findViewById(R.id.tv_deskripsi_itemPestisida);
            imageView = inflate.findViewById(R.id.img_itemPestisida);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position%2 != 0 ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PestisidaAdapter.ViewHolderOne) {
            final Pestisida p = listPestisida.get(position);

            ((ViewHolderOne)holder).judul.setText(p.getNama()+"");
            Picasso.with(context).load(Services.gambar+p.getGambar()).into(((ViewHolderOne)holder).imageView);
            if(p.getDeskripsi().length()>80)
                ((ViewHolderOne)holder).deskripsi.setText(p.getDeskripsi().substring(0,80)+"....");
            else
                ((ViewHolderOne)holder).deskripsi.setText(p.getDeskripsi());

            ((ViewHolderOne)holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,PestisidaActivity.class);
                    i.putExtra("judul",p.getNama());
                    i.putExtra("url",p.getUrl());
                    i.putExtra("tokopedia_search",p.getTokopedia_search());
                    context.startActivity(i);
                }
            });
        }else{
            final Pestisida p = listPestisida.get(position);

            ((ViewHolderTwo)holder).judul.setText(p.getNama()+"");
            Picasso.with(context).load(Services.gambar+p.getGambar()).into(((ViewHolderTwo)holder).imageView);
            if(p.getDeskripsi().length()>80)
                ((ViewHolderTwo)holder).deskripsi.setText(p.getDeskripsi().substring(0,80)+"....");
            else
                ((ViewHolderTwo)holder).deskripsi.setText(p.getDeskripsi());

            ((ViewHolderTwo)holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,PestisidaActivity.class);
                    i.putExtra("url",p.getUrl());
                    i.putExtra("judul",p.getNama());
                    i.putExtra("tokopedia_search",p.getTokopedia_search());
                    context.startActivity(i);
                }
            });
        }
    }
}