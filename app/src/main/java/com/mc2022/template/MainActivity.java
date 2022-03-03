package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //private Button serviceButton;
    private FragmentContainerView fragmentContainerView;

    private String filePrefix = "news_";
    private int count =0;
    private String extension = ".json";
    private List<NewsInfo> news = new ArrayList<NewsInfo>();
    private String filename;

    private TextView textView;
    private LinearLayout listItemBox;
    private RecyclerView recyclerView;
    private NewsListItemAdapter adapter;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(!isMyServiceRunning(DownloadService.class)) {
            if (networkInfo != null && networkInfo.isConnected()) {

                startService(new Intent(this, DownloadService.class));
//                serviceButton.setText(R.string.StopServiceButton);
//                serviceButton.setBackgroundColor(Color.RED);
            }
        }

        updateNewsList();

//        serviceButton = findViewById(R.id.serviceStartStopButton);
//        if(isMyServiceRunning(DownloadService.class)){
//            serviceButton.setText(R.string.StopServiceButton);
//        }else{
//            serviceButton.setText(R.string.StartServiceButton);
//        }
        //serviceButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNewsList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateNewsList();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateNewsList();
    }


    @Override
    public void onClick(View view) {

//        if(view==serviceButton){
//            if(serviceButton.getText().toString().equals(getString(R.string.StartServiceButton))) {
//
//                String stringUrl = "https://petwear.in/mc2022/news/news_0.json";
//                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
//                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//                if(!isMyServiceRunning(DownloadService.class)) {
//                    if (networkInfo != null && networkInfo.isConnected()) {
//
//                        startService(new Intent(this, DownloadService.class));
//                        serviceButton.setText(R.string.StopServiceButton);
//                        serviceButton.setBackgroundColor(Color.RED);
//                    }
//                }
//            }
//            else if(serviceButton.getText().toString().equals(getString(R.string.StopServiceButton))) {
//                if(isMyServiceRunning(DownloadService.class)) {
//                    stopService(new Intent(this, DownloadService.class));
//                    serviceButton.setText(R.string.StartServiceButton);
//                    serviceButton.setBackgroundColor(Color.parseColor("#00AA00"));
//                }
//
//            }
//        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void updateNewsList() {

        NewsInfo item = null;
        do {
            filename = filePrefix+String.valueOf(count)+extension;
            item = parseJSONObject(accessFile(filename));
            news.add(item);
            if(item!=null)
                NewsListFragment.listItems.add(new ListItem(NewsListFragment.listItems.size()+1, item));
            count++;
            System.out.println("File Found");
        }while(item != null);

        System.out.println("File Not Found");


    }

    private String accessFile(String filename) {

        String contents = "";
        FileInputStream fis = null;
        try {
            System.out.println("File is "+filename);
            fis = this.openFileInput(filename);
            System.out.println("Context path = "+this.getFilesDir());
        } catch (FileNotFoundException e) {
            return null;
        }catch (Exception e) {
            return null;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuffer stringBuffer = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = "";
            while ((line = reader.readLine() )!= null) {
                stringBuffer.append(line+'\n');
            }
        } catch (IOException e) {
            System.out.println("Error occurred when opening raw file for reading.");
            Log.i("IOException Occurred", "Error occurred when opening raw file for reading.");
            return null;
        } catch (Exception e) {
            return null;
        }
        finally {
            contents = stringBuffer.toString();
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Line got = "+contents);
        return contents;

    }


    private NewsInfo parseJSONObject(String accessFile) {
        if (accessFile != null) {
            try {
                //JSONObject jsonObj = new JSONObject(accessFile);
                JSONObject jObject = new JSONObject(accessFile);
                return new NewsInfo(jObject.getString("title"), jObject.getString("body"), jObject.getString("image-url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("\n\nfetched = \n\n"+news+"\n\n\n");
        } else {
            Log.e("JSON Parser : ", "Couldn't get json from server.");
            return null;
        }
        return null;

    }



}