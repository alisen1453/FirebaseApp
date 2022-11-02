package com.example.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.databinding.RecycleRowBinding;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListView> {
    ArrayList<Post> postArrayList;
    public ListAdapter(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public ListAdapter.ListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecycleRowBinding recycleRowBinding=RecycleRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ListView(recycleRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListView holder, int position) {
        holder.recycleRowBinding.email.setText(postArrayList.get(position).email);
        holder.recycleRowBinding.comment.setText(postArrayList.get(position).comment);

    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ListView extends RecyclerView.ViewHolder {
        public RecycleRowBinding recycleRowBinding;

        public ListView(RecycleRowBinding recycleRowBinding) {
            super(recycleRowBinding.getRoot());
            this.recycleRowBinding = recycleRowBinding;
        }
    }
}
