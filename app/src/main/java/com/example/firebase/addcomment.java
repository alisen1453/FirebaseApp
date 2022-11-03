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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class addcomment extends AppCompatActivity {
    Uri imagedata;
    private ActivityAddcommentBinding binding;
    public ActivityResultLauncher<Intent> activityResultLauncher;
    public ActivityResultLauncher<String> permissonLauncer;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddcommentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getimagedata();


    }


    public void addComment(View view) {
        if (imagedata != null) {
            UUID uuid = UUID.randomUUID();
            String imagename = "Images/" + uuid + ".jpg";
            storageReference.child(imagename).putFile(imagedata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReferenc = firebaseStorage.getReference(imagename);
                    newReferenc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downurl = uri.toString();
                            String comment = binding.addcomment.getText().toString().trim();
                            String email = user.getEmail();
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("yorum", comment);
                            data.put("mail", email);
                            data.put("url", downurl);
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
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addcomment.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            /*
             */

        }
    }

    public void imageonclick(View view) {
        if (ContextCompat.checkSelfPermission(addcomment.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(addcomment.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "İzin Gerekli", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //İzin alındı
                        permissonLauncer.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }


                }).show();
            } else {
                //İzin alındı
                permissonLauncer.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            }

        } else {
            Intent intentgalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentgalery);

        }
    }

    //onCreate yazılmaz Program Çöker
    //public ActivityResultLauncher<Intent> activityResultLauncher;Olması Gerekir
    //public ActivityResultLauncher<String> permissonLauncer; Olması Gerekir İzin Almak İçin READ_EXTERNAL_STORAGE
    public void getimagedata() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intentResult = result.getData();
                    imagedata = intentResult.getData();
                    binding.imagedata.setImageURI(imagedata);
                }

            }
        });
        permissonLauncer = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    Intent intentroGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentroGallery);

                }


            }
        });


    }


}