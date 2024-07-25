package com.example.mobile1uts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class adapterList extends RecyclerView.Adapter<adapterList.ViewHolder> {
    private List<Data_Array> dataArray;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Data_Array data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public adapterList(List<Data_Array> dataArray) {
        this.dataArray = dataArray;
    }

    @NonNull
    @Override
    public adapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data,null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterList.ViewHolder holder, int position) {
        Data_Array data = dataArray.get(position);
        holder.kode.setText(data.getKode());
        holder.nama.setText(data.getNama());
        holder.kondisi.setText(data.getKondisi());
        Glide.with(holder.evident.getContext()).load(data.getImageUrl()).into(holder.evident);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArray.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView kode;
        TextView nama;
        TextView kondisi;
        ImageView evident;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kode = itemView.findViewById(R.id.kode);
            nama = itemView.findViewById(R.id.nama);
            kondisi = itemView.findViewById(R.id.kondisi);
            evident = itemView.findViewById(R.id.evident);
        }
    }
}
