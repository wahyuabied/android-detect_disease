package com.mrabid.detectdiseases.UI.Penyakit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mrabid.detectdiseases.Model.Hama;
import com.mrabid.detectdiseases.R;
import com.mrabid.detectdiseases.Retrofit.Services;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PenyakitAdapter extends RecyclerView.Adapter<PenyakitAdapter.MyViewHolder> {
    private ArrayList<Hama> list = new ArrayList<>();
    private Context context;


    public PenyakitAdapter(ArrayList<Hama> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public PenyakitAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_penyakit,null));
    }

    @Override
    public void onBindViewHolder(final PenyakitAdapter.MyViewHolder holder, int position) {
        final Hama p = list.get(position);

        holder.judul.setText(p.getName()+"");
        Picasso.with(context).load(Services.gambar+p.getGambar()).into(holder.imageView);
        if(p.getDeskripsi().length()>80)
            holder.deskripsi.setText(p.getDeskripsi().substring(0,80)+"....");
        else
            holder.deskripsi.setText(p.getDeskripsi());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,DetailPenyakitActivity.class);
                i.putExtra("penyakit",p);
                context.startActivity(i);
            }
        });
    }

    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView judul,deskripsi;
        ImageView imageView;
        CardView cardView;
        Toolbar toolbar;


        public MyViewHolder(View inflate) {
            super(inflate);
            cardView = inflate.findViewById(R.id.cv_itemPenyakit);
            judul = inflate.findViewById(R.id.tv_judul_itemPenyakit);
            deskripsi = inflate.findViewById(R.id.tv_deskripsi_item_penyakit);
            imageView = inflate.findViewById(R.id.img_itemPenyakit);
        }
    }
}
