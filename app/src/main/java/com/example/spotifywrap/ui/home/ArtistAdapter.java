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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private ArrayList<Artist> artistList;
    private Context context;

    public ArtistAdapter(Context context, ArrayList<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_top_artists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artist artist = artistList.get(position);

        if (artist != null) {
            holder.artistName.setText(artist.getName());
            Picasso.get().load(artist.getImageUrl()).into(holder.artistImage);
        } else {
            Toast.makeText(context, "Artist is null", Toast.LENGTH_SHORT).show();
            holder.artistName.setText("Default Artist Name");
            holder.artistImage.setImageResource(R.drawable.ic_dashboard_black_24dp); // Set a default image
        }
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView artistImage;
        TextView artistName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artistImage);
            artistName = itemView.findViewById(R.id.artistName);
        }
    }
}
