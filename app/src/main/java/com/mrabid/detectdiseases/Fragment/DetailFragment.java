package com.mrabid.detectdiseases.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mrabid.detectdiseases.Helper.SharedPref;
import com.mrabid.detectdiseases.Model.FeatureExtraction;
import com.mrabid.detectdiseases.R;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

public class DetailFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    TextView t_hasil,t_penyebab, t_daun, t_umur, t_suhu, t_diagnosa ,t_fase;
    FeatureExtraction featureExtraction;
    ImageView imageView;
    SharedPreferences sharedPref;
    SharedPref shared;

    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        sharedPref = getActivity().getSharedPreferences("data",MODE_NO_LOCALIZED_COLLATORS);
        shared = new SharedPref(getActivity());

        imageView = getActivity().findViewById(R.id.iv_solutionFragment);
        t_suhu = getActivity().findViewById(R.id.tv_suhu_fragmentSolution);
        t_diagnosa = getActivity().findViewById(R.id.tv_diagnosa_fragmentSolution);
        t_fase = getActivity().findViewById(R.id.tv_fase_fragmentSolution);
        t_umur = getActivity().findViewById(R.id.tv_umur_fragmentSolution);
        t_daun = getActivity().findViewById(R.id.tv_daun_fragmentSolution);
        t_penyebab = getActivity().findViewById(R.id.tv_penyebab_fragmentSolution);
        t_hasil = getActivity().findViewById(R.id.tv_hasil_fragmentSolution);

        try{
            updateSolutionFragment(sharedPref.getString("daun","-"),shared.loadData("suhu"),sharedPref.getString("umur",""),new Gson().fromJson(sharedPref.getString("featureExtraction",""),FeatureExtraction.class));
        }catch (Exception e){

        }
    }

    public void updateSolutionFragment(String daun,String suhu, String umur,FeatureExtraction featureExtraction){
        if(featureExtraction.getPenyakit().equalsIgnoreCase("sehat")){
            t_hasil.setText("Sehat");
            t_diagnosa.setText("Sehat");
            t_penyebab.setText("-");
            t_fase.setText("-");
        }else{
            t_hasil.setText("Sakit");
            if(featureExtraction.getPenyakit().equalsIgnoreCase("late")){
                t_penyebab.setText("Mikroorganisme Pythopthora Infestan");
                t_diagnosa.setText("Late Blight");
                imageView.setImageResource(R.drawable.late1);
            }else{
                t_penyebab.setText("Jamur Alternaria Solani");
                t_diagnosa.setText("Early Blight");
            }
        }
        t_daun.setText(daun);
        t_suhu.setText(suhu+" "+(char)0x00B0+"K");
        t_umur.setText(umur+" Bulan");
    }
}
