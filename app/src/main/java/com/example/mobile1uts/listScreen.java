package com.example.mobile1uts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class listScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatButton;
    private adapterList adapterList;
    private List<Data_Array> dataArray;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        floatButton = findViewById(R.id.floating);
        recyclerView = findViewById(R.id.ReView);

        progressDialog = new ProgressDialog(listScreen.this);
        progressDialog.setTitle("Loading...");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataArray = new ArrayList<>();
        adapterList = new adapterList(dataArray);
        recyclerView.setAdapter(adapterList);


        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddPage = new Intent(listScreen.this, new_add.class);
                startActivity(toAddPage);
            }
        });

        adapterList.setOnItemClickListener(new adapterList.OnItemClickListener() {
            @Override
            public void onItemClick(Data_Array data) {
                Intent intent = new Intent(listScreen.this, new_detail.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("kode", data.getKode());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("kondisi", data.getKondisi());
                intent.putExtra("evident", data.getImageUrl());
                startActivity(intent);
            }
        });


    }

    public void onItemClick(Data_Array data){
        Intent intent = new Intent();
    }

    protected void onStart(){
        super.onStart();
        getData();
    }

    private void getData(){
        progressDialog.show();
        db.collection("data_crud").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            dataArray.clear();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Data_Array data = new Data_Array(
                                        document.getString("kode"),
                                        document.getString("nama"),
                                        document.getString("kondisi"),
                                        document.getString("evident")
                                );
                                data.setId(document.getId());
                                dataArray.add(data);
                                Log.d("data", document.getId() +"->"+document.getData());
                            }
                            adapterList.notifyDataSetChanged();
                        }else {
                            Log.w("data", "Error Getting Document", task.getException());
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionItemSelection(@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_logout){
            mAuth.signOut();
            Toast.makeText(listScreen.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(listScreen.this, welcome_screen.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}