package com.example.mobile1uts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class new_add extends AppCompatActivity {
    String id="", skode, snama, skondisi, sevident;
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText kode, nama, kondisi;
    private ImageView evident;
    private Button BimageEvident, BSimpan;
    private Uri imageUri;
    private FirebaseFirestore dbNew;
    private FirebaseStorage storage;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add);

        dbNew = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        kode = findViewById(R.id.edKode);
        nama = findViewById(R.id.edNama);
        kondisi = findViewById(R.id.edKondisi);
        evident = findViewById(R.id.edEvident);
        BimageEvident = findViewById(R.id.imageEvident);
        BSimpan = findViewById(R.id.Simpan);

        progressDialog = new ProgressDialog(new_add.this);
        progressDialog.setTitle("Loading...");

        Intent updateOption = getIntent();
        if (updateOption != null){
            id = updateOption.getStringExtra("id");
            skode = updateOption.getStringExtra("kode");
            snama = updateOption.getStringExtra("nama");
            skondisi = updateOption.getStringExtra("kondisi");
            sevident = updateOption.getStringExtra("evident");

            kode.setText(skode);
            nama.setText(snama);
            kondisi.setText(skondisi);
            Glide.with(this).load(sevident).into(evident);
        }

        BimageEvident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        BSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newKode = kode.getText().toString().trim();
                String newNama = nama.getText().toString().trim();
                String newKondisi = kondisi.getText().toString().trim();

                if (newKode.isEmpty() || newNama.isEmpty() || newKondisi.isEmpty()){
                    Toast.makeText(new_add.this, "Kolom Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageUri != null){
                    uploadImageToStorage(newKode, newNama, newKondisi);
                } else {
                    saveData(newKode, newNama, newKondisi, sevident);
                }
            }
        });

    }

    private  void openFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            imageUri = data.getData();
            evident.setImageURI(imageUri);
        }
    }
    private void uploadImageToStorage(String newKode, String newNama, String newKondis){
        if(imageUri != null){
            StorageReference storageReference = storage.getReference().child("new_image/"
            + System.currentTimeMillis()+ ".jpg");
            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUri = uri.toString();
                    saveData(newKode, newNama, newKondis, imageUri);
                    })).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                    Toast.makeText(new_add.this, "Gagal Unggah Poto"+e.getMessage(),
                            Toast.LENGTH_SHORT).show();});
        }
    }

    private void saveData(String newKode, String newNama, String newKondis, String imageUrl){
        Map<String, Object> news = new HashMap<>();
        news.put("kode", newKode);
        news.put("nama", newNama);
        news.put("kondisi", newKondis);
        news.put("evident", imageUrl);

        if (id != null){
            dbNew.collection("data_crud").document(id).update(news)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(new_add.this, "Data Berhasil Diperbaharui"
                        ,Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(new_add.this, "Data Gagal Diperbaharui"+e.getMessage()
                        ,Toast.LENGTH_SHORT).show();
                        Log.w("data_crud", "Data Gagal Diperbaharui",e);
                    });
        } else {
            dbNew.collection("data_crud").add(news)
                    .addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        Toast.makeText(new_add.this, "Data Berhasil Ditambahkan",
                                Toast.LENGTH_SHORT).show();
                        kode.setText("");
                        nama.setText("");
                        kondisi.setText("");
                        evident.setImageResource(0);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(new_add.this, "Gagal Menambahkan Data"+e.getMessage()
                                , Toast.LENGTH_SHORT).show();
                        Log.w("new_add", "Gagal Menambahkan", e);
                    });
        }
    }
}