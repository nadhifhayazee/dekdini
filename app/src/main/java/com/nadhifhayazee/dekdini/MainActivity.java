package com.nadhifhayazee.dekdini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.nadhifhayazee.dekdini.adapter.DeteksiAdapter;
import com.nadhifhayazee.dekdini.model.Gejala;
import com.nadhifhayazee.dekdini.model.Penyakit;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    RecyclerView recyclerView;

    List<Gejala> gejalas;
    List<Penyakit> penyakits;

    Button btnDiagnosa;
    TextView tvHasil;

    DeteksiAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDiagnosa = findViewById(R.id.btnDiagnosa);
        recyclerView = findViewById(R.id.recyclerView);
        tvHasil = findViewById(R.id.hasil);
        btnDiagnosa.setOnClickListener(this);

        getPenyakit();
        getGejala();


    }

    private void getPenyakit() {
        AndroidNetworking.get("http://nadhif.it.student.pens.ac.id/penyakit.json")
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Penyakit.class, new ParsedRequestListener<List<Penyakit>>() {
                    @Override
                    public void onResponse(List<Penyakit> response) {
                        penyakits = response;
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void getGejala() {
        AndroidNetworking.get("http://nadhif.it.student.pens.ac.id/gejala.json")
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Gejala.class, new ParsedRequestListener<List<Gejala>>() {
                    @Override
                    public void onResponse(List<Gejala> response) {
                        gejalas = response;
                        adapter = new DeteksiAdapter(gejalas);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error",anError.getMessage());
                    }
                });
    }



    public Gejala getTiapGejala(String kode){
        for (Gejala item : adapter.listGejala){
            if (kode.equals(item.kode)){
                return item;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        double total = 0;
        String hasil = "";
        for (Penyakit penyakit : penyakits){
            Gejala gejala = getTiapGejala(penyakit.gejala.get(0));
            penyakit.presentaseKeyakinan = hitungDiagnosa(gejala.cfhe, 1, penyakit.gejala);
            hasil += penyakit.nama + " memiliki tingkat keyakinan " + penyakit.presentaseKeyakinan +" %\n";
        }
        tvHasil.setVisibility(View.VISIBLE);
        tvHasil.setText(hasil);
    }

    private void showHasilAkhir() {

    }

    private double hitungDiagnosa(double cfhe, int i, ArrayList<String> gejala) {
        if (i < gejala.size()){
            Gejala g = getTiapGejala(gejala.get(i));
            double cfcombine = cfhe + g.cfhe * (1 - cfhe);
            i++;
            return hitungDiagnosa(cfcombine,i,gejala);
        }

        return cfhe * 100;
    }
}
