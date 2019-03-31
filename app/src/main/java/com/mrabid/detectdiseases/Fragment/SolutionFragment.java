package com.mrabid.detectdiseases.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mrabid.detectdiseases.Helper.InsertData;
import com.mrabid.detectdiseases.Model.FeatureExtraction;
import com.mrabid.detectdiseases.R;

import java.util.ArrayList;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

public class SolutionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_solution, container, false);
    }
    TextView textView;
    RecyclerView alamiRecycler,kimiaRecycler;

    FeatureExtraction penyakit;
    SharedPreferences sharedPref;
    FragmentAdapter fAdapterAlami,fAdapterKimia;
    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getActivity().getSharedPreferences("data",MODE_NO_LOCALIZED_COLLATORS);
        textView = getActivity().findViewById(R.id.tv_penyakit);
        alamiRecycler = getActivity().findViewById(R.id.rv_alami_fragmentSolution);
        kimiaRecycler = getActivity().findViewById(R.id.rv_kimia_fragmentSolution);
        penyakit = new Gson().fromJson(sharedPref.getString("featureExtraction",""),FeatureExtraction.class);

        if(penyakit.getPenyakit().equalsIgnoreCase("late")){
            textView.setText("Solusi Late Blight");
            Toast.makeText(getActivity(), InsertData.lateSolution().get(0).get(0)+"", Toast.LENGTH_SHORT).show();
            setSolusi(InsertData.lateSolution());
        }else if(penyakit.getPenyakit().equalsIgnoreCase("sehat")){
            textView.setText("Tidak ada Solusi");
            Toast.makeText(getActivity(), InsertData.sehatSolution().get(0).get(0)+"", Toast.LENGTH_SHORT).show();
            setSolusi(InsertData.sehatSolution());
        }else{
            textView.setText("Solusi Early Blight");
            Toast.makeText(getActivity(), InsertData.earlySolution().get(0).get(0)+"", Toast.LENGTH_SHORT).show();
            setSolusi(InsertData.earlySolution());
        }
    }

    public void setSolusi(ArrayList<ArrayList<String>> solusi){
        fAdapterAlami = new FragmentAdapter(solusi.get(0),getActivity());
        fAdapterKimia = new FragmentAdapter(solusi.get(1),getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager)mLayoutManager).setStackFromEnd(true);
        ((LinearLayoutManager)mLayoutManager2).setStackFromEnd(true);
        alamiRecycler.setLayoutManager(mLayoutManager);
        alamiRecycler.setAdapter(fAdapterAlami);
        kimiaRecycler.setLayoutManager(mLayoutManager2);
        kimiaRecycler.setAdapter(fAdapterKimia);
    }
}
