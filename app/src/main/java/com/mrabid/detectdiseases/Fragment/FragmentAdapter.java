package com.mrabid.detectdiseases.Fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrabid.detectdiseases.R;

import java.util.ArrayList;

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.MyViewHolder> {
    private ArrayList<String> list = new ArrayList<>();
    private Context context;


    public FragmentAdapter(ArrayList<String> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public FragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_solution,null));
    }

    @Override
    public void onBindViewHolder(final FragmentAdapter.MyViewHolder holder, int position) {
        final String p = list.get(position);

        holder.textView.setText(p+"");
    }

    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(View inflate) {
            super(inflate);
            textView = inflate.findViewById(R.id.tv_itemSolution);
        }
    }
}
