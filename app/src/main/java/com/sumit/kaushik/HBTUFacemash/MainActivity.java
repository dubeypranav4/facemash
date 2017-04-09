package com.sumit.kaushik.HBTUFacemash;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SELECT_FILE = 1;
    public static String srno;
    public static String name;
    public static String gender;
    public String status;
    TextView t1,t2,t3;
    ImageView img;
    public Bitmap bmp;
    private int PICK_IMAGE_REQUEST = 1;
    private int cameraData = 0;
    public static Bundle b;
    String image_url;
    private AdView adView;
    String path;
    String password;
    String result;
    NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this);
    SwipeRefreshLayout swipeRefreshLayout;
    TextView t4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"Swipe right for more options.",Toast.LENGTH_LONG).show();
        adView=(AdView)findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        t1 = (TextView) findViewById(R.id.main_name);
        t2=(TextView)findViewById(R.id.year);
        t3=(TextView)findViewById(R.id.branch);
        img = (ImageView) findViewById(R.id.imageView2);
        t4=(TextView)findViewById(R.id.Score);
     //   img.setImageResource(R.drawable.malen);
        InputStream is = getResources().openRawResource(R.drawable.malen);
        bmp = BitmapFactory.decodeStream(is);

        b = getIntent().getExtras();
        name = b.getString("name");
        name = name.toUpperCase();
        String ar[] = name.split(",");
        name = ar[0];
        gender=ar[1];
        srno = ar[2];
        String score=ar[6];
        String year=ar[3];
        String branch=ar[4];
        password=ar[5];
        t1.setText("Hello,Welcome " + name);
        t2.setText("Year "+year);
        t3.setText("Branch " + branch);
        if(score!=null){
            t4.setText("Score :"+score);
        }
        else
        {
            t4.setText("Score : 0");
        }
        //Toast.makeText(getBaseContext(),gender,Toast.LENGTH_SHORT).show();


     //getImage();
        new GetImage().execute(srno,gender);
    /*  if(gender.equals("MALE")) {
            image_url = "http://103.235.104.80/~hbtilibrary/facemash/maleUploads/" + srno.replace("/", "") + ".jpg";
        }else
        {
            image_url="http://103.235.104.80/~hbtilibrary/facemash/femaleUploads/" + srno.replace("/", "") + ".jpg";
        }

        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(MainActivity.this).build();
        ImageLoader.getInstance().init(configuration);
         imageLoader=ImageLoader.getInstance();
        //imageLoader.displayImage(image_url,img);

        imageLoader.displayImage(image_url, img,options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
           imageLoader.displayImage(image_url,img);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener(){
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });*/

      /*/////  try {
            path=new GetPath().execute(srno,password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ImageLoader imageLoader=AppController.getInstance().getImageLoader();
        if(gender.equals("MALE")) {
            image_url = "http://www.hbtifacemash.com/facemash/" + path;
        }else
        {
            image_url="http://www.hbtifacemash.com/facemash/" +path;
        }
        if(gender.equals("MALE"))
        imageLoader.get(image_url,ImageLoader.getImageListener(img,R.drawable.spinner,R.drawable.malen));
        else
            imageLoader.get(image_url,ImageLoader.getImageListener(img,R.drawable.spinner,R.drawable.femalen));*/
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
                //String s = getStatus(srno);
                boolean b=isConnectedToServer();
                if(b) {
                    new GetStatus().execute(srno);
                }
                else
                {
                        Toast.makeText(MainActivity.this,"Interent Interruption...",Toast.LENGTH_SHORT).show();
                }
             /*   String temp[] = result.split(",");
                //gender = temp[1];
                status = temp[0];
                if (status.equals("1")) {
                    Intent i = new Intent(MainActivity.this, Gender.class);
                    i.putExtra("info", name + "," + srno);
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Your account hasn't been verified yet.\n Help us to verify you by uploading RC/ID").setPositiveButton("Upload RC/Id", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 5);


                        }
                    }).setNegativeButton("Yes,I have.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this,"Please give us some more time...",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.show();

                }
                */
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Bitmap icon=BitmapFactory.decodeResource(MainActivity.this.getResources(),R.drawable.logo);
       // mBuilder.setLargeIcon(icon);
        //mBuilder.setSmallIcon(R.drawable.logo);
        //mBuilder.setContentTitle("HbtuFacemash");
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new GetScore().execute(srno,gender);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
    }

    private boolean isConnectedToServer() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adView!=null){
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adView!=null){
            adView.destroy();
        }
    }


        class GetStatus extends AsyncTask<String, String, String> {
    ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=ProgressDialog.show(MainActivity.this,"Please Wait..","",true,true);
            }

            @Override
            protected String doInBackground(String... params) {
                String srno = params[0];
                String sr = srno.replace("/", "");
                String response = null;
          //      String u="http://shareyourbook.netau.net/test/OgetStatus.php";
                try {
                    URL url = new URL("http://www.hbtifacemash.com/facemash/getStatus.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                  response=bufferedReader.readLine();

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                new GetNotification().execute();
                result=s;
                String temp[] = result.split(",");
                //gender = temp[1];
                status = temp[0];
                if (status.equals("1")) {
                    Intent i = new Intent(MainActivity.this, Gender.class);
                    i.putExtra("info", name + "," + srno);
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Your account hasn't been verified yet.\n Help us to verify you by uploading RC/ID").setPositiveButton("Upload RC/Id", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 5);


                        }
                    }).setNegativeButton("Yes,I have.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this,"Please give us some more time...",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.show();

                }

            }
        }



      private   class GetImage extends AsyncTask<String, String, Bitmap> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=ProgressDialog.show(MainActivity.this,"Downloading Image...","Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                progressDialog.dismiss();
                if(bitmap!=null)
                img.setImageBitmap(bitmap);
                        else {
                    if(gender.equals("MALE")){
                        img.setImageResource(R.drawable.malen);
                    }
                    else
                        img.setImageResource(R.drawable.femalen);
                }
            }



            @Override
            protected Bitmap doInBackground(String... strings) {
                String sr=strings[0].replace("/","");
                String gender=strings[1];
                Bitmap b=null;
                try{
                    URL url=new URL("http://www.hbtifacemash.com/facemash/downloadFromServer.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8")+"&"+
                            URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    b=BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                    httpURLConnection.disconnect();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return b;
                           }



        }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            boolean b=isConnectedToServer();
            if(b)
            new GetImageStatus().execute(srno);
            else
            Toast.makeText(MainActivity.this,"Intenet interruption",Toast.LENGTH_LONG).show();
          /*    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);*/

        } else if (id == R.id.nav_edit_profile) {
            boolean b =isConnectedToServer();
            if (b) {
                Intent intent = new Intent(MainActivity.this, EditPofile.class);
                intent.putExtra("srno", srno);
                startActivity(intent);
            } else
                Toast.makeText(MainActivity.this,"Intenet interruption",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {
            boolean b=isConnectedToServer();
            if(b) {
                String lnk = "https://play.google.com/store/apps/details?id=com.sumit.kaushik.HBTIFacemash";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT,"Hey Check this new app..."+lnk);
                try {
                    startActivity(Intent.createChooser(share, "Send link..."));

                    Toast.makeText(MainActivity.this, "Forwarding Link...", Toast.LENGTH_SHORT).show();
                } catch (ActivityNotFoundException e) {
                    //Toast.makeText(MainActivity.this, "THERE IS NO EMAIL CLIENT INSTALLED", Toast.LENGTH_SHORT).show();
                    Snackbar.make(null, "THERE IS NO EMAIL CLIENT INSTALLED", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            else
                Toast.makeText(MainActivity.this,"Intenet interruption",Toast.LENGTH_LONG).show();
        }  else if (id == R.id.nav_take_pic) {
            new GetImageStatus1().execute(srno);
        } else if (id == R.id.nav_mail) {

            Intent r = new Intent("android.intent.action.ContactUs");
            String info = srno + "," + name;
            r.putExtra("info", info);
            startActivity(r);
        } else if (id == R.id.nav_players) {
            boolean b=isConnectedToServer();
            Intent r = new Intent(MainActivity.this, Players.class);
            if(b)
            startActivity(r);
            else
                Toast.makeText(MainActivity.this,"Internet Interruption...",Toast.LENGTH_SHORT).show();

        }
            else if(id==R.id.aboutus){
            Intent r=new Intent(MainActivity.this,aboutUs.class);
            startActivity(r);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this, Cropper.class);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    FileOutputStream fileOutputStream=null;
                    try{
                        fileOutputStream=openFileOutput("Image.txt",Context.MODE_PRIVATE);
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        fileOutputStream.close();
                        Toast.makeText(getBaseContext(),"image written",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getBaseContext(),"error",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    //   img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            } else if (requestCode == cameraData) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = openFileOutput("Image.txt", Context.MODE_PRIVATE);
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        fileOutputStream.close();
                        Toast.makeText(getBaseContext(), "image written", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    startActivity(intent);
                 //   intent.putExtra("Bitmap", bitmap);
                   // startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


       /* public class MyRequestHandler extends AsyncTask<String, String, String> {
                ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=ProgressDialog.show(MainActivity.this,"Uploading...","Please Wait...",true,true);
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
                String url = "http://103.235.104.80/~hbtilibrary/facemash/uploadToServer2.php";
                try {
                    URL add = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) add.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("uploadImage", "UTF-8") + "=" + URLEncoder.encode(UploadImage, "UTF-8")
                            + "&" + URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(srno, "UTF-8")
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
                progressDialog.dismiss();
            }
        }*/
       // MyRequestHandler myRequestHandler = new MyRequestHandler();

     //  Bitmap resize = resizeBitmap(bitmap);

        //String StringBmp = getStringImage(bitmap);
       // String sr = srno.replace("/", "");
       // myRequestHandler.execute(StringBmp, sr, gender, name);






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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
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
    private class GetPath extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            String srno = params[0];
            String sr = srno.replace("/", "");
            String password=params[1];
            String response = null;
            try {
                URL url = new URL("http://www.hbtifacemash.com/facemash/getPath.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8")
                        + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                response=bufferedReader.readLine();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;

        }
    }

    private class GetNotification extends AsyncTask<String,String,String > {
        @Override
        protected String doInBackground(String... params) {
            String response=null;
            try {
                URL url = new URL("http://www.hbtifacemash.com/facemash/getNotification.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                response=bufferedReader.readLine();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          /*  Intent result=new Intent(MainActivity.this,LoginActivity.class);
            TaskStackBuilder stackBuilder=TaskStackBuilder.create(MainActivity.this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(result);
            PendingIntent resultPendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setContentText(s);
            NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,mBuilder.build());*/
            String temp[]=s.split(",");
            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            Bitmap icon=BitmapFactory.decodeResource(MainActivity.this.getResources(),R.drawable.logo);
            Notification notification=mBuilder.setSmallIcon(R.drawable.logo).setLargeIcon(icon).setAutoCancel(true).setStyle(new NotificationCompat.BigTextStyle().bigText(temp[1])).setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(temp[1]).setContentTitle("HbtuFacemash").build();
            NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                if(temp[0].equals("1"))
            notificationManager.notify(0,notification);


        }
    }

    private class GetImageStatus extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(MainActivity.this,"Please Wait...","",true,true);
        }

        @Override
        protected String doInBackground(String... params) {
            String srno = params[0];
            String sr = srno.replace("/", "");
            String response = null;
            //      String u="http://shareyourbook.netau.net/test/OgetStatus.php";
            try {
                URL url = new URL("http://www.hbtifacemash.com/facemash/getImageStatus.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                response=bufferedReader.readLine();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s!=null){
                if(s.equals("0"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                }
                else{
    Toast.makeText(MainActivity.this,"Image can't be updated until the session ends. ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetImageStatus1 extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(MainActivity.this,"Please Wait...","",true,true);
        }

        @Override
        protected String doInBackground(String... params) {
            String srno = params[0];
            String sr = srno.replace("/", "");
            String response = null;
            //      String u="http://shareyourbook.netau.net/test/OgetStatus.php";
            try {
                URL url = new URL("http://www.hbtifacemash.com/facemash/getImageStatus.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                response=bufferedReader.readLine();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s!=null){
                if(s.equals("0"))
                {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT,true);
                    startActivityForResult(i, cameraData);

                }
                else{
                    Toast.makeText(MainActivity.this,"Image can't be uploaded until the session ends. ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private class GetScore extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(MainActivity.this,"Updating Score...","Please Wait...",true,true);
        }

        @Override
        protected String doInBackground(String... params) {
            String srno = params[0];
            String sr = srno.replace("/", "");
            String gender=params[1];
            String response = null;
            //      String u="http://shareyourbook.netau.net/test/OgetStatus.php";
            try {
                URL url = new URL("http://www.hbtifacemash.com/facemash/getScore.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8")+ "&" +
                        URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                response=bufferedReader.readLine();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            new GetImage().execute(srno,gender);
            t4.setText(s);

        }
    }
}
