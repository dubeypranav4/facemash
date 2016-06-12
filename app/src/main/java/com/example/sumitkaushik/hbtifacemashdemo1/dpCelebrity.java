package com.example.sumitkaushik.hbtifacemashdemo1;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.os.Bundle;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class dpCelebrity extends FragmentActivity implements
        ActionBar.TabListener {



    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "MALE","FEMALE" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dp_celebrity);
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);

        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
     Bitmap mImage= getMImage();
        Bitmap gImage=getGImage();
             mAdapter.init(mImage, gImage);
        viewPager.setAdapter(mAdapter);
    //actionBar.setBackgroundDrawable(new ColorDrawable(R.color.colorPrimary));
    //actionBar.setHomeButtonEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
// Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));

        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private Bitmap getGImage() {
        class GetGImage extends  AsyncTask<String,String,Bitmap>{


            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap image=null;
                try {
                    URL url = new URL("http://192.168.43.89/phpmyadmin/getFImage.php");
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                    String data=URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode("FEMALE","UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    image=BitmapFactory.decodeStream( httpURLConnection.getInputStream());

                }catch (Exception e){
                    e.printStackTrace();
                }
                return image;
            }
        }
        GetGImage getGImage=new GetGImage();
        Bitmap bitmap=null;
        try {
            bitmap=getGImage.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  bitmap;

    }

    private Bitmap getMImage() {

        class GetMImage extends  AsyncTask<String,String,Bitmap>{


            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap image=null;
                try {
                    URL url = new URL("http://192.168.43.89/phpmyadmin/getMImage.php");
                  HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                    String data=URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode("MALE","UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                   image=BitmapFactory.decodeStream( httpURLConnection.getInputStream());

                }catch (Exception e){
                    e.printStackTrace();
                }
                return image;
            }
        }
        GetMImage getMImage=new GetMImage();
        Bitmap bitmap=null;
        try {
             bitmap=getMImage.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  bitmap;
    }


    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        viewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }
}