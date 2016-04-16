package com.example.sumitkaushik.hbtifacemashdemo1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String srno;
    public String name;
    public String gender;
    TextView t1;
    ImageView img;
    Bitmap bmp;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private int cameraData = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = (TextView) findViewById(R.id.main_name);
        img = (ImageView) findViewById(R.id.imageView2);
        //img.setImageResource(R.drawable.malen);
        InputStream is = getResources().openRawResource(R.drawable.malen);
        bmp = BitmapFactory.decodeStream(is);
        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        name = name.toUpperCase();
        String ar[] = name.split(",");
        name = ar[0];
        gender = ar[1];
        srno = ar[2];
        t1.setText("Hello,Welcome " + name);
           // Toast.makeText(getBaseContext(),gender,Toast.LENGTH_SHORT).show();

        getImage();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            /*    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
             /*   Intent i = new Intent("android.intent.action.Game");
                i.putExtra("srno",srno+","+name);
                startActivity(i);*/
                String result=getStatus(srno);
                if(result.equals("1")){
                Intent i = new Intent(MainActivity.this, Gender.class);
                i.putExtra("info", name + "," + srno);
                startActivity(i);}
                else
                {
                    Toast.makeText(getBaseContext(),"Your account hasn't been varified yet.Please try again after sometime..",Toast.LENGTH_LONG).show();
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private String getStatus(String srno) {

        class GetStatus extends AsyncTask<String,String,String>{

            @Override
            protected String doInBackground(String... params) {
                String srno=params[0];
                String sr=srno.replace("/","");
                String res="";
                try{
                    URL url=new URL("http://192.168.43.89/phpmyadmin/getStatus.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                            OutputStream outputStream=httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    res=bufferedReader.readLine();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                }catch (Exception e){
                    e.printStackTrace();
                }
                return res;
            }
        }
        GetStatus getStatus=new GetStatus();
        String re=null;
        try {
            re= getStatus.execute(srno).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  re;
    }

    public void getImage() {
        class GetImage extends AsyncTask<String, String, Bitmap> {
            ProgressDialog loading;

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                loading.dismiss();
                if(bitmap!=null) {
                    img.setImageBitmap(bitmap);
                }
                else
                {
                    if(gender.equals("M")){
                        img.setImageResource(R.drawable.malen);
                    }
                    else
                    {
                        img.setImageResource(R.drawable.femalen);
                    }
                }

            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                String srno=strings[0];
                String sr=srno.replace("/","");
                String gender = strings[1];
                // String url="http://192.168.43.89/phpmyadmin/getImage.php?srno="+srno;
                ///String url="http://192.168.43.89/phpmyadmin/DemoGetImage.php";
                String url = "http://192.168.43.89/phpmyadmin/downloadFromServer.php";
                Bitmap image = null;
                try {
                    // URL add=new URL(url);
                    // image=BitmapFactory.decodeStream(add.openConnection().getInputStream());
                    URL add = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) add.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8")
                            + "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                  /*  BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inJustDecodeBounds=true;
                    Rect r=new Rect(-1,-1,-1,-1);
                    //image = BitmapFactory.decodeStream(httpURLConnection.getInputStream(),r,options);
                    long totalImagePixels=options.outHeight*options.outWidth;
                    long totalScreenPixels=300*400;
                    if(totalImagePixels>totalScreenPixels){
                        double factor=(float)totalImagePixels/(float)(totalScreenPixels);
                        int sampleSize=(int)Math.pow(2,Math.floor(Math.sqrt(factor)));
                        options.inJustDecodeBounds=false;
                        options.inSampleSize=sampleSize;
                        image= BitmapFactory.decodeStream(httpURLConnection.getInputStream(),r,options);
                    }
                    else {
                        options.inJustDecodeBounds=false;
                        image = BitmapFactory.decodeStream(httpURLConnection.getInputStream(), r, options);
                    }*/
                    Bitmap tempImage=BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                    image=resizeBitmap(tempImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return image;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Uploading...",null, true, true);

            }
        }
        GetImage gi = new GetImage();
        gi.execute(srno, gender);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(MainActivity.this, EditPofile.class);
            intent.putExtra("srno", srno);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            String lnk = "www.google.com";
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");

            share.putExtra(android.content.Intent.EXTRA_TEXT, lnk);
            try {
                startActivity(Intent.createChooser(share, "Send link..."));

                Toast.makeText(MainActivity.this, "Forwarding Link...", Toast.LENGTH_SHORT).show();
            } catch (android.content.ActivityNotFoundException e) {
                //Toast.makeText(MainActivity.this, "THERE IS NO EMAIL CLIENT INSTALLED", Toast.LENGTH_SHORT).show();
                Snackbar.make(null, "THERE IS NO EMAIL CLIENT INSTALLED", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_take_pic) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, cameraData);

        } else if (id == R.id.nav_mail) {
            Intent r = new Intent("android.intent.action.ContactUs");
            String info = srno + "," + name;
            r.putExtra("info", info);
            startActivity(r);
        } else if (id == R.id.nav_dp_celebrity) {
            Intent r = new Intent("android.intent.action.DPCELEBRITY");
            startActivity(r);
        }else if(id==R.id.nav_players){
            Intent r=new Intent(MainActivity.this,Players.class);
            startActivity(r);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("WANT TO SAVE IMAGE?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            try {

                                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                img.setImageBitmap(bmp);

                                myUploadImage();
                            } catch (Exception e) {
                                Toast.makeText(getBaseContext(), "Image Size is too Large", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });
            builder.show();


        }
        if (requestCode == cameraData && resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            bmp = (Bitmap) b.get("data");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("WANT TO SAVE IMAGE?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            try {
                                img.setImageBitmap(bmp);
                                myUploadImage();
                            } catch (Exception e) {
                                Toast.makeText(getBaseContext(), "Image Size is too Large", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });
            builder.show();

        }
    }

    private void myUploadImage() {
        class MyRequestHandler extends AsyncTask<String, String, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Uploading Image...", "Please Wait...", true, true);
            }

            @Override
            protected String doInBackground(String... strings) {
                String response = "";
                String UploadImage = strings[0];
                String sno = strings[1];
                String gender = strings[2];
                String name = strings[3];
                //String url="http://192.168.43.89/phpmyadmin/upload2.php";
                //String url="http://192.168.43.89/phpmyadmin/DemoImageUpload.php";
                String url = "http://192.168.43.89/phpmyadmin/uploadToServer2.php";
                try {
                    URL add = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) add.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("uploadImage", "UTF-8") + "=" + URLEncoder.encode(UploadImage, "UTF-8")
                            + "&" + URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sno, "UTF-8")
                            + "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8")
                            + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
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
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            }
        }
        MyRequestHandler myRequestHandler = new MyRequestHandler();
       Bitmap resize=resizeBitmap(bmp);
        String StringBmp = getStringImage(resize);
        String sr=srno.replace("/","");
        myRequestHandler.execute(StringBmp, sr, gender, name);

    }

    private Bitmap resizeBitmap(Bitmap image) {

        int width=image.getWidth();
        int height=image.getHeight();
        int newWidth=0;
        int newHeight=0;
        if (height >= width) {
            newWidth=600;
            newHeight=800;
        }
        if(width>height){
            newWidth=800;
            newHeight=600;
        }
        float scaleWidth=((float)newWidth)/width;
        float scaleHeigth=((float)newHeight)/height;
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidth,scaleHeigth);
        Bitmap resized=Bitmap.createBitmap(image,0,0,width,height,matrix,false);
        return resized;
    }


    public String getStringImage(Bitmap bitmap) {
     /*  int width= bitmap.getWidth();
        int height=bitmap.getHeight();
        int newWidth=200;
        int newHeight=200;
        float scaleWidth=((float)newWidth)/width;
        float scaleHeight=((float)newHeight)/height;
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("WANT TO EXIT?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            finish();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });
            builder.show();
        }

    }

}
