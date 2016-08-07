package com.example.sumitkaushik.hbtifacemashdemo1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sumitkaushik.hbtifacemashdemo1.CropImageView;
import com.example.sumitkaushik.hbtifacemashdemo1.MainActivity;
import com.example.sumitkaushik.hbtifacemashdemo1.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.opengles.GL10;

public class Cropper extends Activity {
    CropImageView cropImageView;
    Button cropIt;
    public Bitmap croppedImage;
    Bitmap bitmap,bp;
    ImageButton bLeft,bRight;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        bLeft=(ImageButton)findViewById(R.id.bLeft);
        bRight=(ImageButton)findViewById(R.id.bRight);
      //  Bundle b = getIntent().getExtras();
        try {
            InputStream inputStream=openFileInput("Image.txt");
            if(inputStream!=null){
                BitmapFactory.Options options =new BitmapFactory.Options();
                options.inJustDecodeBounds=true;
                  bp=BitmapFactory.decodeStream(inputStream);
                bitmap=ScalingUtility.createScaledBitmap(bp,1200,720);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        cropImageView = (CropImageView) findViewById(R.id.cropImage);
        cropIt = (Button) findViewById(R.id.cropIt);
        cropImageView.setImageBitmap(bitmap);
        if (Build.VERSION.SDK_INT >= 19) {
            cropImageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            cropImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        cropIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                croppedImage = cropImageView.getCroppedImage();
                ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
                croppedImage.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                FileOutputStream fileOutputStream=null;
                try{
                    fileOutputStream=openFileOutput("CroppedImage.txt",Context.MODE_PRIVATE);
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    fileOutputStream.close();
                    Toast.makeText(getBaseContext(),"File written again",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace(
                    );
                }
                    Intent intent=new Intent("android.intent.action.CROPPEDFINALIMAGE");
                startActivity(intent);
                finish();
            }
        });
        bLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix mat=new Matrix();
                mat.setRotate(-90);
                Bitmap rotatedBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),mat,true);
                cropImageView.setImageBitmap(rotatedBitmap);
            }
        });
        bRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix mat=new Matrix();
                mat.setRotate(90);
                Bitmap rotatedBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),mat,true);
                cropImageView.setImageBitmap(rotatedBitmap);

            }
        });

    }

    }



