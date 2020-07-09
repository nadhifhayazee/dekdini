package com.nadhifhayazee.dekdini.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nadhifhayazee.dekdini.R;
import com.nadhifhayazee.dekdini.model.Gejala;

import java.util.ArrayList;
import java.util.List;

public class DeteksiAdapter extends RecyclerView.Adapter<DeteksiAdapter.ViewHolder> {

    public List<Gejala> listGejala;

    public DeteksiAdapter(List<Gejala> listGejala) {
        this.listGejala = listGejala;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_gejala_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(holder, listGejala.get(position));
    }

    @Override
    public int getItemCount() {
        return listGejala.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView gejala;
        Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gejala = itemView.findViewById(R.id.gejala);
            spinner = itemView.findViewById(R.id.spinner);
        }

        public void bind(ViewHolder holder, final Gejala gejala) {
            holder.gejala.setText(gejala.nama);
            final ArrayList<String> options = new ArrayList<>();
            options.add("Tidak");
            options.add("Tidak Tahu");
            options.add("Sedikit Yakin");
            options.add("Cukup Yakin");
            options.add("Yakin");
            options.add("Sangat Yakin");

            ArrayAdapter adapter = new ArrayAdapter(itemView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, options);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (options.get(position)){

                        case "Tidak Tahu":
                            gejala.cfhe = gejala.bobot * 0.2;
                            break;
                         case "Sedikit Yakin":
                            gejala.cfhe = gejala.bobot * 0.4;
                            break;
                        case "Cukup Yakin":
                            gejala.cfhe = gejala.bobot * 0.6;
                            break;
                        case "Yakin":
                            gejala.cfhe = gejala.bobot * 0.8;
                            break;
                        case "Sangat Yakin":
                            gejala.cfhe = gejala.bobot * 1;
                            break;
                        default:
                            gejala.cfhe = gejala.bobot * 0;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }
}
