package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebase.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        if(firebaseUser!=null){
            Intent intent = new Intent(MainActivity.this, mainpage.class);
            startActivity(intent);
            finish();
        }


    }

    public void singIn (View view) {
        String email = binding.email.getText().toString().trim();
        String pass = binding.pass.getText().toString().trim();
        if (email.equals("") && pass.equals("")) {
            Toast.makeText(MainActivity.this, "Değer Boş Girilemez", Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(MainActivity.this, mainpage.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    public void singUp (View view) {
        String email = binding.email.getText().toString().trim();
        String pass = binding.pass.getText().toString().trim();
        if (email.equals("") && pass.equals("")) {
            Toast.makeText(MainActivity.this, "Değer Boş Girilemez", Toast.LENGTH_LONG).show();
        } else {
            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(MainActivity.this, mainpage.class);
                    Toast.makeText(MainActivity.this, "Kayıt Başarılı", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}