package com.mrabid.detectdiseases.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mrabid.detectdiseases.Helper.InsertData;
import com.mrabid.detectdiseases.Helper.PreProcessing;
import com.mrabid.detectdiseases.Helper.SharedPref;
import com.mrabid.detectdiseases.Model.FeatureExtraction;
import com.mrabid.detectdiseases.R;

import java.util.ArrayList;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

public class DetailFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    TextView t_hasil,t_penyebab, t_daun, t_suhu, t_diagnosa ,t_fase,t_gejala;
    ArrayList<String> gejala = new ArrayList<>();
    SharedPreferences sharedPref;
    SharedPref shared;

    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        sharedPref = getActivity().getSharedPreferences("data",MODE_NO_LOCALIZED_COLLATORS);
        shared = new SharedPref(getActivity());

        t_suhu = getActivity().findViewById(R.id.tv_suhu_fragmentSolution);
        t_diagnosa = getActivity().findViewById(R.id.tv_diagnosa_fragmentSolution);
        t_fase = getActivity().findViewById(R.id.tv_fase_fragmentSolution);
        t_daun = getActivity().findViewById(R.id.tv_daun_fragmentSolution);
        t_penyebab = getActivity().findViewById(R.id.tv_penyebab_fragmentSolution);
        t_hasil = getActivity().findViewById(R.id.tv_hasil_fragmentSolution);
        t_gejala = getActivity().findViewById(R.id.tv_gejala_fragmentSolution);

        try{
            updateSolutionFragment(sharedPref.getString("daun","-"),shared.loadData("suhu"),new Gson().fromJson(sharedPref.getString("featureExtraction",""),FeatureExtraction.class));
        }catch (Exception e){

        }
    }

    public void updateSolutionFragment(String daun,String suhu,FeatureExtraction featureExtraction){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            t_gejala.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        if(featureExtraction.getPenyakit().equalsIgnoreCase("sehat")){
            t_hasil.setText("Sehat");
            t_diagnosa.setText("Sehat");
            t_penyebab.setText("-");
            t_fase.setText(featureExtraction.getFase());
            t_gejala.setText("-");
        }else{
            t_hasil.setText("Sakit");
            t_fase.setText("- "+featureExtraction.getFase());
            if(featureExtraction.getPenyakit().equalsIgnoreCase("late")){
                gejala = InsertData.gejalaLate();
                for(int i=0;i<gejala.size();i++){
                    t_gejala.append("- "+gejala.get(i)+"\n");
                }
                if(PreProcessing.fasePenyebaranLate(suhu)){
                    t_fase.append("\n- Fase Optimum Penyebaran");
                }
                t_penyebab.setText("Mikroorganisme Pythopthora Infestan");
                t_diagnosa.setText("Late Blight");

            }else{
                gejala = InsertData.gejalaEarly();
                for(int i=0;i<gejala.size();i++){
                    t_gejala.append("- "+gejala.get(i)+"\n");
                }
                if(PreProcessing.fasePenyebaranEarly(suhu)){
                    t_fase.append("\n- Fase Optimum Penyebaran");
                }
                t_penyebab.setText("Jamur Alternaria Solani");
                t_diagnosa.setText("Early Blight");
            }
        }
        t_suhu.setText(PreProcessing.kelvinCelcius(suhu) +" "+(char)0x00B0+"C");
        t_daun.setText(daun);
    }
}
