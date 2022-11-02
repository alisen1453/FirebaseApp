package com.example.firebase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.firebase.databinding.ActivityAddcommentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class addcomment extends AppCompatActivity {
private ActivityAddcommentBinding binding;
//ActivityResultLauncher<Intent> activityResultLauncher;
public ActivityResultLauncher<String> permissonLauncer;
private FirebaseFirestore firestore;
private FirebaseAuth auth;
private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddcommentBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

binding.imagedata.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ActivityResultLauncher<String> permissonLaunce=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                binding.imagedata.setImageURI(result);
            }
        });

    }
});

    }





public void addComment(View view) {
    String comment = binding.addcomment.getText().toString().trim();
    String email = user.getEmail();
    HashMap<String, Object> data = new HashMap<>();
    data.put("yorum", comment);
    data.put("mail", email);
    data.put("date", FieldValue.serverTimestamp());

    firestore.collection("Post").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Toast.makeText(addcomment.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(addcomment.this, mainpage.class);
            startActivity(intent);
            finish();

        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(addcomment.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }
    });


}

}