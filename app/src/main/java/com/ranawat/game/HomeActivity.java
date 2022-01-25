package com.ranawat.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ranawat.game.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase databaseReference;

    KProgressHUD kProgressHUD;

    long coin=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        kProgressHUD=KProgressHUD.create(this);
        kProgressHUD.setDimAmount(0.5f);
        kProgressHUD.show();

        firebaseAuth=FirebaseAuth.getInstance();

        databaseReference= FirebaseDatabase.getInstance();

        databaseReference.getReference().child("Profile")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        binding.lBtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coin=coin-5;
                binding.total.setText(coin+" ");
            }
        });

        binding.gBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coin=coin+10;
                binding.total.setText(coin+" ");
            }
        });
    }
}