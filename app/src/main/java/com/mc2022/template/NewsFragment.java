package com.mc2022.template;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    public TextView fragmentTitle;
    public ImageView fragmentImage;
    public TextView fragmentBody;
    public EditText editComment;
    public TextView showComment;
    public EditText editRating;
    public TextView showRating;
    public Button commentButton;
    public Button ratingButton;
    private int position;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private static String mParam1;
    private static String mParam2;
    private static String image_url;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2, String image) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();

        mParam1 = param1;
        mParam2 = param2;
        image_url = image;

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            image_url = getArguments().getString(ARG_PARAM3);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        editComment = v.findViewById(R.id.editTextComment);
        editRating = v.findViewById(R.id.editTextRating);
        showComment = v.findViewById(R.id.textViewComment);
        showRating = v.findViewById(R.id.textViewRating);
        commentButton = v.findViewById(R.id.buttonComment);
        ratingButton = v.findViewById(R.id.buttonRating);

        boolean commentFlag = false;
        boolean ratingFlag = false;
        String comment = "";
        int rating = 0;
        for(int i=0; i<NewsListFragment.listItems.size(); i++){
            NewsInfo news = NewsListFragment.listItems.get(i).getNews();

            if(news.getTitle().equals(mParam1)){

                System.out.println("From On Create");
                System.out.println("News Title from Field :- "+news.getTitle());
                System.out.println("News Title from fragment :- "+mParam1);
                System.out.println("Comment = "+ news.getComment());
                System.out.println("Rating = "+ news.getRating());

                if(news.getComment().equals("")) {
                    commentFlag = true;
                }
                else{
                    comment = news.getComment();
                }
                if(news.getRating() == 0){
                    ratingFlag = true;
                }
                else{
                    rating = news.getRating();
                }
                position = i;
            }
        }

        if(commentFlag){
            editComment.setVisibility(View.VISIBLE);
            showComment.setVisibility(View.GONE);
            commentButton.setText("SUBMIT COMMENT");
        }
        else{
            showComment.setText(comment);
            //NewsListFragment.listItems.get(position).getNews().setComment(String.valueOf(comment));
            editComment.setVisibility(View.GONE);
            showComment.setVisibility(View.VISIBLE);
            showComment.setText(NewsListFragment.listItems.get(position).getNews().getComment());
            commentButton.setText("EDIT COMMENT");

        }
        if(ratingFlag){
            editRating.setVisibility(View.VISIBLE);
            showRating.setVisibility(View.GONE);
            ratingButton.setText("SUBMIT RATING");
        }
        else{
            showRating.setText(String.valueOf(rating));
            //NewsListFragment.listItems.get(position).getNews().setRating(Integer.valueOf(rating));
            editRating.setVisibility(View.GONE);
            showRating.setVisibility(View.VISIBLE);
            showRating.setText(String.valueOf(NewsListFragment.listItems.get(position).getNews().getRating()));
            ratingButton.setText("EDIT RATING");
        }


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentButton.getText().equals("SUBMIT COMMENT")){
                    NewsListFragment.listItems.get(position).getNews().setComment(String.valueOf(editComment.getText()));
                    showComment.setText(editComment.getText());
                    editComment.setText("");
                    editComment.setVisibility(View.GONE);
                    showComment.setVisibility(View.VISIBLE);
                    commentButton.setText("EDIT COMMENT");
                }
                else if(commentButton.getText().equals("EDIT COMMENT")){

                    editComment.setText(showComment.getText());
                    showComment.setText("");
                    showComment.setVisibility(View.GONE);
                    editComment.setVisibility(View.VISIBLE);
                    commentButton.setText("SUBMIT COMMENT");
                }
            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingButton.getText().equals("SUBMIT RATING")){
                    NewsListFragment.listItems.get(position).getNews().setRating(Integer.parseInt(editRating.getText().toString()));
                    showRating.setText(editRating.getText());
                    editRating.setText("");
                    editRating.setVisibility(View.GONE);
                    showRating.setVisibility(View.VISIBLE);
                    ratingButton.setText("EDIT RATING");
                }
                else if(ratingButton.getText().equals("EDIT RATING")){
                    editRating.setText(showRating.getText());
                    showRating.setText("");
                    showRating.setVisibility(View.GONE);
                    editRating.setVisibility(View.VISIBLE);
                    ratingButton.setText("SUBMIT RATING");
                }
            }
        });
        // Inflate the layout for this fragment

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.app");

        View v = getView();

        boolean commentFlag = false;
        boolean ratingFlag = false;
        String comment = "";
        int rating = 0;
        for(int i=0; i<NewsListFragment.listItems.size(); i++){
            NewsInfo news = NewsListFragment.listItems.get(i).getNews();

            if(news.getTitle().equals(mParam1)){

                System.out.println("From On Resume");
                System.out.println("News Title from Field :- "+news.getTitle());
                System.out.println("News Title from fragment :- "+mParam1);
                System.out.println("Comment = "+ news.getComment());
                System.out.println("Rating = "+ news.getRating());

                if(news.getComment().equals("")) {
                    commentFlag = true;
                }
                else{
                    comment = news.getComment();
                }
                if(news.getRating() == 0){
                    ratingFlag = true;
                }
                else{
                    rating = news.getRating();
                }
                position = i;
            }
        }

        if(commentFlag){
            editComment.setVisibility(View.VISIBLE);
            showComment.setVisibility(View.GONE);
            commentButton.setText("SUBMIT COMMENT");
        }
        else{
            showComment.setText(comment);
            //NewsListFragment.listItems.get(position).getNews().setComment(comment);
            editComment.setVisibility(View.GONE);
            showComment.setVisibility(View.VISIBLE);
            showComment.setText(NewsListFragment.listItems.get(position).getNews().getComment());
            commentButton.setText("EDIT COMMENT");

        }
        if(ratingFlag){
            editRating.setVisibility(View.VISIBLE);
            showRating.setVisibility(View.GONE);
            ratingButton.setText("SUBMIT RATING");
        }
        else{
            showRating.setText(String.valueOf(rating));
            //NewsListFragment.listItems.get(position).getNews().setRating(rating);
            editRating.setVisibility(View.GONE);
            showRating.setVisibility(View.VISIBLE);
            showRating.setText(String.valueOf(NewsListFragment.listItems.get(position).getNews().getRating()));
            ratingButton.setText("EDIT RATING");
        }











        fragmentImage = v.findViewById(R.id.imageView);
        new ImageLoadTask(image_url, fragmentImage).execute();

        System.out.println("IMAGE URI = "+image_url);
        System.out.println("Title = "+mParam1);
        System.out.println("Body = "+mParam2);

        fragmentTitle = v.findViewById(R.id.textViewTitle);// update your textView in the main layout
        fragmentTitle.setText(mParam1);
        fragmentBody = v.findViewById(R.id.textViewBody);
        fragmentBody.setText(mParam2);

        getActivity().registerReceiver(new MyBroadcastReceiver(), intentFilter);
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String title = extras.getString("title");
            String body = extras.getString("body");
            String image = extras.getString("image");

            //System.out.println("IMAGE URI = "+image);
            fragmentImage = getView().findViewById(R.id.imageView);
            new ImageLoadTask(image, fragmentImage).execute();

            fragmentTitle = getView().findViewById(R.id.textViewTitle);// update your textView in the main layout
            fragmentTitle.setText(title);
            fragmentBody = getView().findViewById(R.id.textViewBody);
            fragmentBody.setText(body);

        }
    }


    public static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            //System.out.println("Got URL : "+url);
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

}