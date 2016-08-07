package com.example.sumitkaushik.hbtifacemashdemo1;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Players extends ListActivity {

    String[] data;
    EditText search;
    SimpleAdapter simpleAdapter;
String result;
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<HashMap<String, String>> feedList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players);
        search = (EditText) findViewById(R.id.etSearch);
        Button button=(Button)findViewById(R.id.list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = getData();
                if (!result.equals("")) {
                    String[] r1 = result.split("<br>");
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
        });
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

    private String getData() {
        String data = null;
        class RetrieveData extends AsyncTask<String, String, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Players.this, "Connecting servers...", "Please wait...", true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                String url = "http://192.168.43.89/phpmyadmin/players.php";
                try {
                    URL u = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String response = "";
                    String temp = "";
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
            }
        }
        RetrieveData retrieveData = new RetrieveData();
        try {
            data = retrieveData.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return data;
    }
}
