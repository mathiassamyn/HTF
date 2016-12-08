package com.example.mathias.htf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.data;

public class TakePic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);



        Button takePic = (Button) findViewById(R.id.takePic);
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    Uri photoURI;


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                Log.e("in try", "in try");
                photoFile = createImageFile();
                Log.e("na createimagefile", "ernaaaaaaaaaaaaaaaaaaaaaaaa");
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
                Log.e("exception: ", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.e("voorextra", "extraxxxxxxxxxxxxxxxxxxxxxxxxx");
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.mathias.htf",
                        photoFile);
                Log.e("photoURI0    " , photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        Log.e("voor storagedir", "in createimagefile");
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.e("achter storagedir", "in try");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.e("in onactivityresult", "ressssult");
        ImageView imageView = (ImageView)findViewById(R.id.result);
        imageView.setImageURI(photoURI);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("encodedImage     ",encodedImage );

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);

        ArrayList<String> dataList = new ArrayList<String>();
        dataList.add("mooie dag om te progge");
        dataList.add(encodedImage);
        dataList.add(String.valueOf(20160206) + "-" + String.valueOf(232525));

        uploadPic uploadClass = new uploadPic(this, dataList);
        Log.e("hhhj         ", "xxxxxxxxxxxxxxxxxxxxxxxx");
        uploadClass.execute(dataList);
    }


}
