package com.example.spotifywrap.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<Song> songs;
    private Context context;

    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_recent_songs, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        if (song != null) {
            holder.textViewSongName.setText(song.getName());
            holder.textViewArtistName.setText(getArtistsAsString(song.getArtists()));
            Picasso.get().load(song.getImageUrl()).into(holder.imageView);
        } else {
            Toast.makeText(context, "Song is null", Toast.LENGTH_SHORT).show();
            holder.textViewSongName.setText("Default Song Name");
            holder.textViewArtistName.setText("Default Artist Name");
        }
    }

    private String getArtistsAsString(List<Artist> artists) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < artists.size(); i++) {
            builder.append(artists.get(i).getName());
            if (i < artists.size() - 1) {
                builder.append(", "); // Add comma and space if it's not the last artist
            }
        }
        return builder.toString();
    }
    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewSongName;
        public TextView textViewArtistName;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewSongName = itemView.findViewById(R.id.textViewSongName);
            textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
        }
    }
}