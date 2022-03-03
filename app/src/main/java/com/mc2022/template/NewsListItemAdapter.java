package com.mc2022.template;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsListItemAdapter extends RecyclerView.Adapter<NewsListItemAdapter.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;

    public NewsListItemAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);

    }

    public void onBindViewHolder(@NonNull NewsListItemAdapter.ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.newsNumber.setText(String.valueOf(position+1));
        holder.newsTitle.setText(listItem.getNews().getTitle());

        String image=listItems.get(position).getNews().getImage_url();
        String title=listItems.get(position).getNews().getTitle();
        String body=listItems.get(position).getNews().getBody();
        int number = listItems.get(position).getNewsNumber();



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity act = (AppCompatActivity) view.getContext();

                //Fragment newsFragment = new NewsFragment();
                Fragment newsFragment = new NewsFragment().newInstance(title, body, image);
                act.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,newsFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView newsNumber;
        public TextView newsTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsNumber = itemView.findViewById(R.id.news_number);
            newsTitle = itemView.findViewById(R.id.news_title);
        }
    }
}
