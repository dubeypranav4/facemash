package com.sumit.kaushik.HBTUFacemash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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
                                    //MainActivity mainActivity=new MainActivity();

                               // mainActivity.myUploadImage(cropped);
                                    String StringBmp=getStringBitmap(cropped);
                                    MyRequestHandler myRequestHandler=new MyRequestHandler();
                                    myRequestHandler.execute(StringBmp);

                              //  finish();
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

            private String getStringBitmap(Bitmap cropped) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                cropped.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                return encodedImage;

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
    public class MyRequestHandler extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(croppedFinalImage.this,"Uploading...","Please Wait...",true,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            String UploadImage = strings[0];
            // String sno = strings[1];
            //String gender = strings[2];
            //String name = strings[3];
            //String url="http://192.168.43.89/phpmyadmin/upload2.php";
            //String url="http://192.168.43.89/phpmyadmin/DemoImageUpload.php";
            String url = "http://www.hbtifacemash.com/facemash/uploadToServer2.php";
            try {
                URL add = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) add.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("uploadImage", "UTF-8") + "=" + URLEncoder.encode(UploadImage, "UTF-8")
                        + "&" + URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(MainActivity.srno.replace("/",""), "UTF-8")
                        + "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(MainActivity.gender, "UTF-8")
                        + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(MainActivity.name, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (java.lang.OutOfMemoryError outOfMemoryError) {
                Toast.makeText(getBaseContext(), "Please try againg after some time", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(croppedFinalImage.this, MainActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Please try againg after some time", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(croppedFinalImage.this,"Swipe down to refresh this page...",Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
