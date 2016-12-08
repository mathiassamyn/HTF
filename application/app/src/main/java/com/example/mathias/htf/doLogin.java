package com.example.mathias.htf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
 * Created by joeyd on 8-12-2016.
 */

public class doLogin extends AsyncTask<ArrayList<String>,String,String> {
    public Context thisContext;
    public Activity thisActivity;
    doLogin(Context context, Activity activity){
        thisActivity = activity;
        this.thisContext = context;
    }
    String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
    @Override
    protected void onPreExecute(){

    }
    protected void onPostExecute(String r) {
        Object json = null;
        Object re;
        String message = r;
        String result;
        try {
            json = new JSONTokener(r).nextValue();
            if(json instanceof JSONObject){
                JSONObject jsonobj = (JSONObject)json;
                result = jsonobj.getString("token");
                Log.e("JSON","OBJECT");
                message = "Succesfull logged in";
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(thisContext);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("key",result);
                editor.commit();
                Intent intent = new Intent(thisContext,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                thisContext.startActivity(intent);
            }else if(json instanceof JSONArray){
                Log.e("JSON","Array");

            }else{
                Log.e("JSON","something else");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(thisContext,message.toString(),Toast.LENGTH_SHORT).show();
    }
    @Override
    protected String doInBackground(ArrayList<String>... params) {
        Log.e("Starting","starting doRegister");
//        String urlString = params[0]; // url to call
        String result = null;
        ArrayList<String> param = params[0];
//        ArrayList<String> result = new ArrayList<String>();
        InputStream in;
//        InputStream in = null;
        try{
//            URL url = new URL(urlString);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            in = new BufferedInputStream(urlConnection.getInputStream());
//            Log.e("Logging",in.toString());
            URL url = new URL("https://htf-mars.azurewebsites.net/api/researcher/startmission");
            HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            httpURLConnection.connect();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",param.get(0));
            jsonObject.put("password",param.get(1));

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();
            int status = httpURLConnection.getResponseCode();
            if(status >= 400){
                in = httpURLConnection.getErrorStream();

            }else{
                in = new BufferedInputStream(httpURLConnection.getInputStream());
            }

            result = convertStreamToString(in);
            Log.e("Log token",result);
//            httpURLConnection.disconnect();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }

        return result;
    }
}
