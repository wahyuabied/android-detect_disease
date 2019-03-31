package com.mrabid.detectdiseases.Helper;

import com.mrabid.detectdiseases.Model.Penyakit;

import java.util.ArrayList;

public class InsertData {

    public static ArrayList<ArrayList<String>> earlySolution(){
        ArrayList<ArrayList<String>> solutionE = new ArrayList<>();
        ArrayList<String> alami = new ArrayList<>();
        alami.add("Memberikan kompos, rabuk atau biosolid untuk menurunkan kadar nitrogen pada tanah");
        alami.add("Menurunkan kadar fosfor tanah(mengurangi pelapukan batuan disekitar tanah)");
        alami.add("Menjaga kekeringan daun karena jika lembab Jamur Alternaria berkerja sangat produktif");
        alami.add("Membersihkan bekas-bekas tanaman yang terinfeksi");
        alami.add("Siram tanaman hanya pada pagi hari untuk tanaman basah sesingkat mungkin");
        alami.add("Menggunakan sistem irigasi tetes");
        alami.add("Menyilangkan tanaman dengan tanaman liar");
        alami.add("Menggunakan mulsa (Material penutup tanaman)");
        alami.add("Meningkatkan sirkulasi udara (apabila tanaman berada di ruang tertutup)");
        alami.add("Menjaga kadar kalium tetap stabil");

        ArrayList<String> kimia = new ArrayList<>();
        kimia.add("Pemberian Azoxystrobin");
        kimia.add("Pemberian Pyraclostrobin");
        kimia.add("Pemberian Bacilus Subtilis");
        kimia.add("Pemberian Chlorothalonil");
        kimia.add("Pemberian Hidrogen Dioksida");
        kimia.add("Pemberian Mancozeb");
        kimia.add("Pemberian Pottasium Bicarbonate");
        kimia.add("Pemberian Ziram");

        solutionE.add(alami);
        solutionE.add(kimia);

        return solutionE;
    }
    public static ArrayList<ArrayList<String>> sehatSolution(){
        ArrayList<ArrayList<String>> solutionS = new ArrayList<>();

        ArrayList<String> alami = new ArrayList<>();
        alami.add("-");
        ArrayList<String> kimia = new ArrayList<>();
        kimia.add("-");

        solutionS.add(alami);
        solutionS.add(kimia);

        return solutionS;
    }
    public static ArrayList<ArrayList<String>> lateSolution(){
        ArrayList<ArrayList<String>> solutionL = new ArrayList<>();
        ArrayList<String> alami = new ArrayList<>();
        alami.add("Menggunakan mulsa (Material penutup tanaman)");
        alami.add("Menyilangkan tanaman dengan tanaman liar");
        alami.add("Menghancurkan kanopi sekitar lima minggu sebelum panen");
        alami.add("Menghindari inokulum (pemindahan mikroorganisme untuk kepentingan pertanian)");
        alami.add("Menghindari kelebapan yang ekstrem");
        alami.add("Menjaga suhu lebih dari 10"+(char)0x00B0+" Celcius");

        ArrayList<String> kimia = new ArrayList<>();
        kimia.add("Pemberian mis. Paraquat melalui penggunaan srayer ransel");
        kimia.add("Mencampurkan mis. Phostrol dengan air kemudian ");
        kimia.add("Pemberian capuran Bordeaux");
        kimia.add("Membakar dedaunan yang terinfeksi dengan herbisida konta atau asam sulfat");
        kimia.add("(Pencegahan) Menyemprotkan Metalaxyl yang dicampurkan karbamat atau (Mandi Propamid, Fluazinam, Triphenyltin, Mancozeb) setiap minggu");

        solutionL.add(alami);
        solutionL.add(kimia);
        return solutionL;
    }

    public static ArrayList<Penyakit> penyakit(){
        ArrayList<Penyakit> penyakit = new ArrayList<>();
        penyakit.add(new Penyakit("Early","Early Blight","Alternaria solani is a fungal pathogen, that produces a disease in tomato and potato plants called early blight. The pathogen produces distinctive (bullseye) patterned leaf spots and can also cause stem lesions and fruit rot on tomato and tuber blight on potato. Despite the name (early) foliar symptoms usually occur on older leaves. If uncontrolled, early blight can cause significant yield reductions. Primary methods of controlling this disease include preventing long periods of wetness on leaf surfaces and applying fungicides. Geographically, A. solani is problematic in tomato production areas east of the Rocky Mountains and is generally not an issue in the less humid Pacific or inter-mountain regions. A. solani is also present in most potato production regions every year but has a significant effect on yield only when frequent wetting of foliage favors symptom development"));
        penyakit.add(new Penyakit("Late","Late Blight","Phytophthora infestans is an oomycete or water mold, a microorganism that causes the serious potato and tomato disease known as late blight or potato blight. (Early blight, caused by Alternaria solani, is also often called (potato blight).) Late blight was a major culprit in the 1840s European, the 1845 Irish, and the 1846 Highland potato famines. The organism can also infect some other members of the Solanaceae. The pathogen is favored by moist, cool environments: sporulation is optimal at 12–18 C in water-saturated or nearly saturated environments, and zoospore production is favored at temperatures below 15 C. Lesion growth rates are typically optimal at a slightly warmer temperature range of 20 to 24 C. "));
        return penyakit;
    }

}
