package com.sumit.kaushik.HBTUFacemash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sumit Kaushik on 18-Mar-16.
 */
public class    EditPofile extends Activity {
    Button b;
    EditText e1,e2,e3;
    String srno;
    boolean cancel=true;
    View focusView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        Bundle bundle=getIntent().getExtras();
        srno=bundle.getString("srno");
        b=(Button)findViewById(R.id.button);



        e1=(EditText)findViewById(R.id.editText4);
        e2=(EditText)findViewById(R.id.editText5);
        e3=(EditText)findViewById(R.id.editText6);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldPass=e1.getText().toString();
                String newPass=e2.getText().toString();
                String conPass=e3.getText().toString();
                if(TextUtils.isEmpty(oldPass)){
                    e1.setError("This Field is Required");
                    cancel =false;
                    focusView=e1;
                }
                if(TextUtils.isEmpty(newPass)){
                    e2.setError("This Field is Required");
                    cancel =false;
                    focusView=e2;
                }  if(TextUtils.isEmpty(conPass)){
                    e3.setError("This Field is Required");
                    cancel =false;
                    focusView=e3;
                }
                if(!newPass.equals(conPass)){
                    Toast.makeText(getBaseContext(),"Password don't match!!",Toast.LENGTH_SHORT).show();
                    cancel=false;
                }
                if(newPass.length()<6){
                    Toast.makeText(getBaseContext(),"Password must be more than 6 characters",Toast.LENGTH_SHORT).show();
                    cancel=false;
                    focusView=e2;
                }
                new CheckPass().execute(oldPass,srno);

                if(!cancel){
                    focusView.requestFocus();
                }
                else
                {
                  new ChangePassword().execute(newPass,srno);
                }
            }
        });
    }

            public  class ChangePassword extends AsyncTask<String, String, String>{
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=ProgressDialog.show(EditPofile.this,null,"Please Wait...",true,true);
            }

            @Override
            protected String doInBackground(String... strings) {
                String password = strings[0];
                String SrNo = strings[1];
                String url = "http://www.hbtifacemash.com/facemash/changePass.php";
                String response = null;
                try {
                    URL add = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) add.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                            URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(SrNo, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
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
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                }
        }

        class CheckPass extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=ProgressDialog.show(EditPofile.this,null,"Please Wait...",true,true);
            }

            @Override
            protected String doInBackground(String... strings) {
                String oldPassword = strings[0];
                String SrNo = strings[1];
                String url = "http://www.hbtifacemash.com/facemash/checkOldPass.php";
                String response = null;
                try {
                    URL add = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) add.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("oldPassword", "UTF-8") + "=" + URLEncoder.encode(oldPassword, "UTF-8") + "&" +
                            URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(SrNo, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
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
                if(s!=null) {
                    if (s.equals("Invalid")) {
                        Toast.makeText(getBaseContext(), "Password is Invalid!", Toast.LENGTH_SHORT).show();
                        cancel = false;
                        focusView = e1;
                        return;

                    }
                    }

            }
        }

}
