package com.sumit.kaushik.HBTUFacemash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Cropper extends Activity {
    CropImageView cropImageView;
    Button cropIt;
    public Bitmap croppedImage;
    Bitmap bitmap,bp;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);

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


        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
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
                croppedImage.compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
                FileOutputStream fileOutputStream=null;
                try{
                    fileOutputStream=openFileOutput("CroppedImage.txt",Context.MODE_PRIVATE);
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    fileOutputStream.close();
                   // Toast.makeText(getBaseContext(),"File written again",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace(
                    );
                }
                    Intent intent=new Intent("android.intent.action.CROPPEDFINALIMAGE");
                startActivity(intent);
                finish();
            }
        });


    }

    }



