package com.ranawat.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ranawat.game.databinding.ActivityResigsterBinding;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityResigsterBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityResigsterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        //setup progress dailog
        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);





        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        //handel click , begin register

        binding.regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }

        });

    }

    private  String name= "", email="", password="";
    private void validateData() {

        /* Before Creating account , lets do some data vaildation */


        //get data
        name =binding.nameEt.getText().toString().trim();
        email =binding.emailEt.getText().toString().trim();
        password =binding.passwordEt.getText().toString().trim();
        String cPassword = binding.cfpasswordEt.getText().toString().trim();



        //vaildate data
        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this, "Enter you name.....", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(RegisterActivity.this, "Invalid email Pattern.....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "Enter Password....!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cPassword)){
            Toast.makeText(RegisterActivity.this, "Confirm Password....!", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(cPassword)){
            Toast.makeText(RegisterActivity.this, "Password doesn't match.... ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            createuserAccount();
        }

    }

    private void createuserAccount() {
        progressDialog.setMessage("Creating Account....");
        progressDialog.dismiss();

        //creatr user in firebase auth

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //account creation is successful now send the data in firebase
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        //acount creating failed
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void updateUserInfo() {

        progressDialog.setMessage("Saving Information....");

        //TimeStamp
        long timestamp=System.currentTimeMillis();

        //Creating Uid
        String uid= firebaseAuth.getUid();

        //setup data to add in db

        HashMap<String, Object> hashMap= new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("name",name);
        hashMap.put("email", email);
        hashMap.put("Password" , password);
        hashMap.put("timestamp", timestamp);

        //set data to db

        DatabaseReference referenc= FirebaseDatabase.getInstance().getReference("Users");
        referenc.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        //data added to db
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Account Created...", Toast.LENGTH_SHORT).show();
                        //
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class ));
                        finish();



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

    }


}
