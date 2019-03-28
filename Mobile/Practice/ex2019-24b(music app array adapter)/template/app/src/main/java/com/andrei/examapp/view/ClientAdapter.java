package com.andrei.examapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andrei.examapp.R;
import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.MyCallback;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.GenericClientHolder> {
    private List<GenericEntity> entities = new ArrayList<>();
    private MyCallback listener;

    @NonNull
    @Override
    public GenericClientHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.enitity_item_client, viewGroup, false);
        return new GenericClientHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericClientHolder holder, int i) {
        GenericEntity entity = entities.get(i);
        holder.tvTitle.setText(entity.getTitle());
        holder.tvAlbum.setText(entity.getAlbum());
        holder.tvGenre.setText(entity.getGenre());
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setEntities(List<GenericEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    public void setEventListener(MyCallback listener){
        this.listener = listener;
    }

    class GenericClientHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvAlbum;
        private TextView tvGenre;

        public GenericClientHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAlbum = itemView.findViewById(R.id.tv_album);
            tvGenre = itemView.findViewById(R.id.tv_genre);

            itemView.setOnClickListener(v->{
                listener.onCall(entities.get(getAdapterPosition()));
            });
        }
    }
}

