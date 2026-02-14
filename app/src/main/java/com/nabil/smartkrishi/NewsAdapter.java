package com.nabil.smartkrishi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private final ArrayList<NewsItem> newsList;

    public NewsAdapter(ArrayList<NewsItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem currentItem = newsList.get(position);

        holder.newsTitle.setText(currentItem.getTitle());
        holder.newsDescrip.setText(currentItem.getDescription());
        holder.newsDate.setText(currentItem.getDate());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // Renamed from myViewHolder to follow conventions
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImg;
        TextView newsTitle, newsDescrip, newsDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImg = itemView.findViewById(R.id.news_img);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescrip = itemView.findViewById(R.id.news_description);
            newsDate = itemView.findViewById(R.id.home_news_date);
        }

        // Helper method to keep onBindViewHolder clean
        public void bind(NewsItem item) {
            newsTitle.setText(item.getTitle());
            newsDescrip.setText(item.getDescription());
            newsDate.setText(item.getDate());
            // Set the image if available
            if (item.getImageUrl() != null) {
                 Picasso.get().load(item.getImageUrl()).into(newsImg);
            }


        }
    }
}
