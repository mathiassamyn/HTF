package com.example.mathias.htf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class showImages extends AppCompatActivity {
    ListView list;
    String[] web = {
            "Google Plus",
            "Twitter",
            "Windows",
            "Bing",
            "Itunes",
            "Wordpress",
            "Drupal"
    } ;
    Integer[] imageId = {
            R.drawable.hacktthefuture,
            R.drawable.hacktthefuture,
            R.drawable.hacktthefuture,
            R.drawable.hacktthefuture,
            R.drawable.hacktthefuture,
            R.drawable.hacktthefuture,
            R.drawable.hacktthefuture

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        CustomList adapter = new
                CustomList(showImages.this, web, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(showImages.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });

        Button goToIntent = (Button) findViewById(R.id.gotopicintent);
        goToIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showImages.this, TakePic.class);
                startActivity(intent);
            }
        });

        Log.e("Sharedpref",sharedPref.getString("key","nokey"));
        getImages();
    }
    public void getImages(){
        getImagesUser getimage = new getImagesUser(getApplicationContext(),this);
        getimage.execute("");
    }
}
