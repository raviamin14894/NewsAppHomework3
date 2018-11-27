package com.npktech.ravi.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;
    List<NewsItem> newsItems;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setNewsItems(List<NewsItem> newsItems) {
        this.newsItems = newsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_news_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) {
        final NewsItem newsItem = newsItems.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "" + newsItem.getWebUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

        holder.title.setText("Title : " + newsItem.getTitle());
        holder.description.setText("Description : " + newsItem.getDescription());
        holder.publishedat.setText("Date  : " + newsItem.getPublishedAt());


        Picasso.with(context)
                .load(newsItem.getImageUrL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .resize(350, 220)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (newsItems != null) return newsItems.size();
        else return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView title, description, publishedat;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            publishedat = itemView.findViewById(R.id.publishedat);

        }
    }
}
