package com.mc2022.template;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class DownloadService extends Service {

    private static final String DEBUG_TAG = "Service Download Section";
    private Context context = this;
    private MediaPlayer player;
    private String filepath = "https://petwear.in/mc2022/news/news_";
    private static int count =0;
    private String extension = ".json";
    private static List<NewsInfo> news = new ArrayList<NewsInfo>();
    private String filename;
    private MyBroadcastReceiver broadcastReceiver;
    Thread serThread;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        player = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        player.setLooping(true);
        player.start();

        serThread = new Thread(new Runnable() {
            @Override
            public void run() {

                //broadcast receiver code
                broadcastReceiver = new MyBroadcastReceiver();
                registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
                registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));


                String url = filepath + String.valueOf(count) + extension;
                filename = "news_"+count+extension;
                while ( URLUtil.isValidUrl(url)) {

                    Log.i("Download Service ", "Service is running "+count);
                    try {
                        //Toast.makeText(context, "Download Service starting", Toast.LENGTH_SHORT).show();

                        if(!fileExist(filename)) {
                            new DownloadWebpageTask().execute(url);
                            Thread.sleep(2000);
                        }
                        parseJSONObject(accessFile(filename));
                        displayOnFragment();
                        count++;
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    url = filepath + String.valueOf(count) + extension;
                    filename = "news_"+count+extension;
                }
            }
        });
        serThread.start();

        /*new Thread(new Runnable() {
            @Override
            public void run() {

                //broadcast receiver code
                broadcastReceiver = new MyBroadcastReceiver();
                registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
                registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
                registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_OKAY));
                registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));


                String url = filepath + String.valueOf(count) + extension;
                filename = "news_"+count+extension;
                while ( URLUtil.isValidUrl(url)) {

                    Log.i("Download Service ", "Service is running "+count);
                    try {
                        //Toast.makeText(context, "Download Service starting", Toast.LENGTH_SHORT).show();

                        if(!fileExist(filename)) {
                            new DownloadWebpageTask().execute(url);
                            Thread.sleep(2000);
                        }
                        parseJSONObject(accessFile(filename));
                        displayOnFragment();
                        count++;
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    url = filepath + String.valueOf(count) + extension;
                    filename = "news_"+count+extension;
                }
            }
        }).start();*/
        return super.onStartCommand(intent, flags, startId);
        //return START_STICKY;
    }

    private void displayOnFragment() {

        Intent intent = new Intent();
        intent.putExtra("title", news.get(count).getTitle());
        intent.putExtra("body", news.get(count).getBody());
        intent.putExtra("image", news.get(count).getImage_url());
        intent.setAction("com.my.app");
        sendBroadcast(intent);
    }



    @Override
    public void onDestroy() {
        player.stop();
        /*try {
            serThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        unregisterReceiver(broadcastReceiver);
        Toast.makeText(this, "Download Service Destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }



    private class DownloadWebpageTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {


                System.out.println("URL = "+urls[0]);
                String fileText =  downloadUrl(urls[0]);
                saveFileInInternalStorage(fileText);
                return fileText;
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }


        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, "The file has been downloaded", Toast.LENGTH_LONG).show();
        }
    }

    private void saveFileInInternalStorage(String fileText) {

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileText.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    @SuppressLint("LongLogTag")
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer stringBuilder = new StringBuffer();
            String line = "";
            while ((line =reader.readLine()) != null) {
                stringBuilder.append(line+'\n');
            }
            String fileText = stringBuilder.toString();
            System.out.println("The new line read = "+fileText);
            return fileText;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    private String accessFile(String filename) {

        String contents = "";
        FileInputStream fis = null;
        try {
            System.out.println("File is "+filename);
            fis = context.openFileInput(filename);
            System.out.println("Context path = "+context.getFilesDir());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        } finally {
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


    private void parseJSONObject(String accessFile) {
        if (accessFile != null) {
            try {
                //JSONObject jsonObj = new JSONObject(accessFile);
                JSONObject jObject = new JSONObject(accessFile);
                news.add(new NewsInfo(jObject.getString("title"), jObject.getString("body"), jObject.getString("image-url")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("\n\nfetched = \n\n"+news+"\n\n\n");


        } else {
            Log.e("JSON Parser : ", "Couldn't get json from server.");
        }


    }

    public boolean fileExist(String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.

            //System.out.println("++++++++++++++++++++++++++Broadcast received!++++++++++++++++++++++++++++");
            if(Intent.ACTION_BATTERY_LOW.equals(intent.getAction())){
                //System.out.println("++++++++++++++++++++++++++LOW BATTERY!++++++++++++++++++++++++++++");
                Toast.makeText(context, "Battery Low!", Toast.LENGTH_SHORT).show();
                //Intent serIntent = new Intent(context, DownloadService.class);
                stopSelf();

            }
            if(Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){
                Toast.makeText(context, "Power Connected!", Toast.LENGTH_SHORT).show();
                //Intent serIntent = new Intent(context, DownloadService.class);
                stopSelf();
            }
            //throw new UnsupportedOperationException("Not yet implemented");
        }
    }


}