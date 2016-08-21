package com.example.ousmane.movies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ousmane on 8/17/16.
 */
public class HttpManager {
    private static final String LOG_TAG = HttpManager.class.getSimpleName();

    public String getData(String baseUrl) {
        String data = "";
        HttpURLConnection httpURLConnection = null;

        try {
            Uri buildUri = Uri.parse(baseUrl).buildUpon()
                    .build();

            URL url = new URL(buildUri.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            int status = httpURLConnection.getResponseCode();
            
            InputStream in = httpURLConnection.getInputStream();

            data = readStream(in);

        }catch (MalformedURLException e) {

        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return data;
    }

    private String readStream(InputStream in) {

        BufferedReader reader = null;
        StringBuffer data  = new StringBuffer();

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null) {
                data.append(line + "\n");
            }

        }catch(IOException e) {
            Log.v(LOG_TAG, "IOException");
        }finally {
            if(reader != null) {
                try {
                    reader.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

}
