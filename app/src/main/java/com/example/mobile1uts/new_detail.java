package com.example.mobile1uts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class new_detail extends AppCompatActivity {

    private TextView newkode, newnama,newkondisi;
    private ImageView newevident;
    private Button edit, delet;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        newkode = findViewById(R.id.newKode);
        newnama = findViewById(R.id.newNama);
        newkondisi = findViewById(R.id.newKondisi);
        newevident = findViewById(R.id.newEvident);
        edit = findViewById(R.id.editButton);
        delet = findViewById(R.id.deletButton);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String kode = intent.getStringExtra("kode");
        String nama = intent.getStringExtra("nama");
        String kondisi = intent.getStringExtra("kondisi");
        String evident = intent.getStringExtra("evident");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new_detail.this, new_add.class);
                intent.putExtra("id", id);
                    intent.putExtra("kode", kode);
                intent.putExtra("nama", nama);
                intent.putExtra("kondisi", kondisi);
                intent.putExtra("evident", evident);
                startActivity(intent);
            }
        });

        delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("data_crud").document(id).delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(new_detail.this, "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(new_detail.this, listScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(new_detail.this, "Gagal Hapus"+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Log.w("new_detail", "Gagal Hapus",e);
                        });
            }
        });

        newkode.setText(kode);
        newnama.setText(nama);
        newkondisi.setText(kondisi);
        Glide.with(this).load(evident).into(newevident);
    }
}