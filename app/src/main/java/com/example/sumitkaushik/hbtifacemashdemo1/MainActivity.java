package com.example.sumitkaushik.hbtifacemashdemo1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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

import javax.microedition.khronos.opengles.GL10;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = (TextView) findViewById(R.id.main_name);
        t2=(TextView)findViewById(R.id.year);
        t3=(TextView)findViewById(R.id.branch);
        img = (ImageView) findViewById(R.id.imageView2);
        img.setImageResource(R.drawable.malen);
        InputStream is = getResources().openRawResource(R.drawable.malen);
        bmp = BitmapFactory.decodeStream(is);

        b = getIntent().getExtras();
        name = b.getString("name");
        name = name.toUpperCase();
        String ar[] = name.split(",");
        name = ar[0];
        gender=ar[1];
        srno = ar[2];
        String year=ar[3];
        String branch=ar[4];
        t1.setText("Hello,Welcome " + name);
        t2.setText("Year "+year);
        t3.setText("Branch " + branch);
        //Toast.makeText(getBaseContext(),gender,Toast.LENGTH_SHORT).show();


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
                String s = getStatus(srno);
                String temp[] = s.split(",");
                //gender = temp[1];
                status = temp[0];
                if (status.equals("1")) {
                    Intent i = new Intent(MainActivity.this, Gender.class);
                    i.putExtra("info", name + "," + srno);
                    startActivity(i);
                } else {
                    Toast.makeText(getBaseContext(), "Your account hasn't been varified yet.Please try again after sometime..", Toast.LENGTH_LONG).show();
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

        class GetStatus extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {
                String srno = params[0];
                String sr = srno.replace("/", "");
                String response = null;
          //      String u="http://shareyourbook.netau.net/test/OgetStatus.php";
                try {
                    URL url = new URL("http://192.168.43.89/phpmyadmin/getStatus.php");
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
        }
        GetStatus getStatus = new GetStatus();
        String re = null;
        try {
            re = getStatus.execute(srno).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return re;
    }

    public void getImage() {
        class GetImage extends AsyncTask<String, String, Bitmap> {
            ProgressDialog loading;

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                loading.dismiss();
               // Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
               if (bitmap != null) {

                   img.setImageBitmap(ScalingUtility.createScaledBitmap(bitmap,300,250));
                  /* if(bitmap.getHeight()>GL10.GL_MAX_TEXTURE_SIZE){
                       float aspect_ratio = ((float)bitmap.getHeight())/((float)bitmap.getWidth());
                       Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                               (int) ((GL10.GL_MAX_TEXTURE_SIZE*0.9)*aspect_ratio),
                               (int) (GL10.GL_MAX_TEXTURE_SIZE*0.9));
                       img.setImageBitmap(scaledBitmap);
                   }else{
                       img.setImageBitmap(bitmap);
                   }*/
                } else {
                    if (gender.equals("MALE")) {
                        img.setImageResource(R.drawable.malen);
                    } else {
                        img.setImageResource(R.drawable.femalen);
                    }
                }

            }



            @Override
            protected Bitmap doInBackground(String... strings) {
                String srno = strings[0];
                String gender = strings[1];
                // String url="http://192.168.43.89/phpmyadmin/getImage.php?srno="+srno;
                ///String url="http://192.168.43.89/phpmyadmin/DemoGetImage.php";
                String url = "http://192.168.43.89/phpmyadmin/downloadFromServer.php";
               // String u="http://shareyourbook.netau.net/test/OdownloadFromServer.php";
                Bitmap image = null;
                String s=null;
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
                    String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(srno, "UTF-8")
                            + "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                        image = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                   // image = resizeBitmap(tempImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return image;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Uploading...", null, true, true);

            }
        }
        GetImage gi = new GetImage();
        String sr=srno.replace("/","");
        gi.execute(sr, gender);
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
        /*    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);*/
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
            i.putExtra(MediaStore.EXTRA_OUTPUT,true);
            startActivityForResult(i, cameraData);


        } else if (id == R.id.nav_mail) {

            Intent r = new Intent("android.intent.action.ContactUs");
            String info = srno + "," + name;
            r.putExtra("info", info);
            startActivity(r);
        } else if (id == R.id.nav_dp_celebrity) {
            Intent r = new Intent("android.intent.action.DPCELEBRITY");
            startActivity(r);
        } else if (id == R.id.nav_players) {
            Intent r = new Intent(MainActivity.this, Players.class);
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
                    intent.putExtra("Bitmap", bitmap);
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void myUploadImage(Bitmap bitmap) {
        class MyRequestHandler extends AsyncTask<String, String, String> {


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
            }
        }
        MyRequestHandler myRequestHandler = new MyRequestHandler();

     //  Bitmap resize = resizeBitmap(bitmap);

        String StringBmp = getStringImage(bitmap);
        String sr = srno.replace("/", "");
        myRequestHandler.execute(StringBmp, sr, gender, name);

    }

    private Bitmap resizeBitmap(Bitmap image) {
        Bitmap scaledBitmap=Bitmap.createBitmap(300,250,Config.ARGB_8888);
        float scaleX=300/(float) image.getWidth();
        float scaleY=250/(float)image.getHeight();
        float pivotX=0;
        float pivotY=0;
        Matrix scaleMatrix=new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);
        Canvas canvas=new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(image,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
         getImage();
    }

}
