package com.sumit.kaushik.HBTUFacemash;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Players extends ListActivity {

    String[] data;
    EditText search;
    SimpleAdapter simpleAdapter;
String result;
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<HashMap<String, String>> feedList = new ArrayList<HashMap<String, String>>();
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players);
        Toast.makeText(Players.this,"Swipe down to refresh this page..",Toast.LENGTH_LONG).show();
        search = (EditText) findViewById(R.id.etSearch);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refreshPlayer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                feedList.clear();

                boolean b=isConnectedToServer();
                if(b)
                new RetrieveData().execute();
                else
                Toast.makeText(Players.this,"Internet Interruption...",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        boolean b=isConnectedToServer();
        if(b) {
            new RetrieveData().execute();
        }
        else
        {
            Toast.makeText(Players.this,"Internet Interruption...",Toast.LENGTH_SHORT).show();
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Players.this.simpleAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Players.this.simpleAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Players.this.simpleAdapter.getFilter().filter(s);
            }
        });

    }

    private boolean isConnectedToServer() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private   class RetrieveData extends AsyncTask<String, String, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Players.this, "Downloading list...", "Please wait...", true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                String url = "http://www.hbtifacemash.com/facemash/players.php";

                try {
                    URL u = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String temp = "";
                    String response="";
                    while ((temp = bufferedReader.readLine()) != null) {
                        response += temp;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                if (!s.equals("")) {
                    String[] r1 = s.split("<br>");
                    for (int i = 0; i < r1.length; i++) {
                        String[] r2 = r1[i].split(" ", 3);

                        map = new HashMap<String, String>();
                        map.put("SrNo", r2[0]);

                        map.put("Name", r2[2]);
                        map.put("Score", r2[1]);

                        feedList.add(map);
                    }
                }
                simpleAdapter = new SimpleAdapter(Players.this, feedList, R.layout.view_item, new String[]{"SrNo", "Name", "Score"}, new int[]{R.id.textSrno, R.id.textName, R.id.textScore});
                setListAdapter(simpleAdapter);

            }
        }

}
