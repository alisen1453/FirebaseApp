package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.firebase.databinding.ActivityMainpageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class mainpage extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private ActivityMainpageBinding binding;
    ArrayList<Post> postArrayList;
    ListAdapter listAdapter;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainpageBinding.inflate(getLayoutInflater());
            View view=binding.getRoot();
        setContentView(view);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        postArrayList=new ArrayList<>();
        binding.recycleview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter=new ListAdapter(postArrayList);
        binding.recycleview.setAdapter(listAdapter);
            datalist();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(mainpage.this);
        menuInflater.inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.addcomment){
            Intent commentintetn=new Intent(mainpage.this,addcomment.class);
            startActivity(commentintetn);
        }if(item.getItemId()==R.id.singoutt){
           auth.signOut();
            Intent outintent=new Intent(mainpage.this,MainActivity.class);
            startActivity(outintent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public  void datalist(){
            firestore.collection("Post").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Toast.makeText(mainpage.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }else if(value!=null){
                        for(DocumentSnapshot snapshot:value.getDocuments() ){
                            Map<String,Object> data=snapshot.getData();
                            String email=(String) data.get("mail");
                            String comment=(String) data.get("yorum");
                            Post post=new Post(email,comment);
                            postArrayList.add(post);
                        }
                        listAdapter.notifyDataSetChanged();
                    }

                }
            });

    }
}