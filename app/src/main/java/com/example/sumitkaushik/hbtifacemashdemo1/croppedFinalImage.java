package com.example.sumitkaushik.hbtifacemashdemo1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class croppedFinalImage extends Activity {
ImageView iv;
Bitmap cropped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropped_final_image2);
        Button button1=(Button)findViewById(R.id.bEdit);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        Button button2=(Button)findViewById(R.id.bUpload);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(croppedFinalImage.this);
                builder.setMessage("You won't be able change or upload any image afterward for a session.Are you sure? ")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                try{
                                    MainActivity mainActivity=new MainActivity();

                                mainActivity.myUploadImage(cropped);
                                finish();
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(getBaseContext(),"myUploadImage exception",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                            finish();
                    }
                });
                builder.show();


            }
        });

      iv= (ImageView) findViewById(R.id.finalCropped);
        /*Intent intent=getIntent();
        byte[] bytes = intent.getByteArrayExtra("cropped");
         cropped = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        iv.setImageBitmap(cropped);*/
        try{
            InputStream inputStream=openFileInput("CroppedImage.txt");
            if(inputStream!=null){
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inJustDecodeBounds=true;
               cropped= BitmapFactory.decodeStream(inputStream);
                iv.setImageBitmap(cropped);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
