package com.mc2022.template;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment {

    private RecyclerView newsListRecyclerView;
    private RecyclerView.Adapter newsListItemAdapter;

    public static List<ListItem> listItems = new ArrayList<ListItem>();;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        newsListRecyclerView = view.findViewById(R.id.NewsListRecyclerView);
        newsListRecyclerView.setHasFixedSize(true);
        newsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        for(int i=0; i<20; i++){
//            listItems.add(new ListItem(i, "Dummy Text"));
//        }

        newsListItemAdapter = new NewsListItemAdapter(listItems, getContext());
        newsListRecyclerView.setAdapter(newsListItemAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.app");
        getActivity().registerReceiver(new MyBroadcastReceiver(), intentFilter);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String title = extras.getString("title");
            String body = extras.getString("body");
            String image = extras.getString("image");

            listItems.add(new ListItem(listItems.size()+1, new NewsInfo(title, body, image)));
            newsListItemAdapter = new NewsListItemAdapter(listItems, getContext());
            newsListRecyclerView.setAdapter(newsListItemAdapter);
        }
    }
}