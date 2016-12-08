package com.example.mathias.htf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mathias on 8/12/2016.
 */

public class uploadPic extends AsyncTask<ArrayList<String>,String,String> {
    public Context thisContext;
    SharedPreferences sharedPref;
    uploadPic(Context context) {
        this.thisContext = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

    protected void onPostExecute(String r) {
        Object json = null;
        Object re;
        String result;
        /*try {
            json = new JSONTokener(r).nextValue();
            if (json instanceof JSONObject) {
                JSONObject jsonobj = (JSONObject) json;
                result = jsonobj.getString("token");
                Log.e("JSON", "OBJECT");
            } else if (json instanceof JSONArray) {
                Log.e("JSON", "Array");

            } else {
                Log.e("JSON", "something else");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        Toast.makeText(thisContext, r.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(ArrayList<String>... params) {
        Log.e("Starting", "starting uploadPic");
//        String urlString = params[0]; // url to call
        String result = null;
        ArrayList<String> param = params[0];
//        ArrayList<String> result = new ArrayList<String>();
        InputStream in;
//        InputStream in = null;
        try {
//            URL url = new URL(urlString);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            in = new BufferedInputStream(urlConnection.getInputStream());
//            Log.e("Logging",in.toString());
            URL url = new URL("https://htf-mars.azurewebsites.net/api/photo");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty ("Authorization", sharedPref.getString("key","nokey"));
            httpURLConnection.connect();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("description", param.get(0));
            jsonObject.put("base64imagedata", param.get(1));
            jsonObject.put("datetime", param.get(2));
            Log.e("JSON", jsonObject.toString());
            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();
            int status = httpURLConnection.getResponseCode();
            if (status >= 400) {
                in = httpURLConnection.getErrorStream();

            } else {
                in = new BufferedInputStream(httpURLConnection.getInputStream());
            }

            result = convertStreamToString(in);
            Log.e("Log token", result);
            Log.e("status", String.valueOf(status));
//            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
