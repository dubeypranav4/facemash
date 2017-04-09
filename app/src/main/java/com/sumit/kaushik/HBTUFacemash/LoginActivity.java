package com.sumit.kaushik.HBTUFacemash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A login screen that offers login via srno/password.
 */
public class LoginActivity extends Activity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    int count = 0;
    String gender, srno, password;
    CheckBox chk;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        chk= (CheckBox) findViewById(R.id.showPassword);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
                else
                {
                    mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.srno);
        mPasswordView = (EditText) findViewById(R.id.password);
        autoFill();
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
    /*    mEmailSignInButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ((event.getAction())){
                    case MotionEvent.ACTION_DOWN:{
                        Button view=(Button)v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        attemptLogin();
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        Button view =(Button)v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }

                }
                return true;
            }
        });*/
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });
        // Button register = (Button) findViewById(R.id.register_button);
        TextView register = (TextView) findViewById(R.id.register_button);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent i = new Intent("android.intent.action.REGISTRATION");
                Intent i = new Intent(LoginActivity.this, Registration.class);
                startActivity(i);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        srno = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        /*if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }*/
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid srno address.
        if (TextUtils.isEmpty(srno)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (!isEmailValid(srno)) {
            mEmailView.setError(getString(R.string.error_invalid_srno));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask();
            mAuthTask.execute(srno, password);

        }
    }


    private boolean isEmailValid(String srno) {
        //TODO: Replace this with your own logic
        if (srno.indexOf("/") == -1) {
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if (password.length() < 6) {
            return false;
        }
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class UserLoginTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this, "Signing In..", "Please wait..", true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.
            String srno = params[0];
            String sr = srno.replace("/", "");
            String password = params[1];
          //  String url = "http://103.235.104.80/~hbtilibrary/facemash/hbtiFaceMashLogin.php";
            String url="http://www.hbtifacemash.com/facemash/hbtiFaceMashLogin.php";
          //  String url="http://shareyourbook.netau.net/test/OhbtiFaceMashLogin.php";
            boolean b = isConnectedToServer();
            if (b) {
                try {
                    URL u = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("srno", "UTF-8") + "=" + URLEncoder.encode(sr, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                   String response="";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    int i=httpURLConnection.getResponseCode();
                    httpURLConnection.disconnect();
                    return response;
                } catch (Exception e) {

                }
            }

            return null;
        }

        private boolean isConnectedToServer() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }


        public void onPostExecute(String success) {
            mAuthTask = null;
            progressDialog.dismiss();
            //  Toast.makeText(getBaseContext(),success,Toast.LENGTH_LONG).show();
            if (success != null) {
                String re[] = success.split(",");
                if (re[0].equals("Success")) {
                    String name = re[1];
                    String year=re[2];
                    String gender=re[3];
                   String branch=re[4];
                    String score=re[5];
                    autoSet();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("name", name + "," +gender+","+ srno+","+year+","+branch+","+password+","+score);
                    startActivity(intent);
                    finish();
                } else if (success.equals("LogIn Failed")) {

                    Toast.makeText(getBaseContext(), "LogIn Failed", Toast.LENGTH_SHORT).show();
                    mPasswordView.requestFocus();
                } else {
                    Toast.makeText(getBaseContext(), success, Toast.LENGTH_SHORT).show();
                    mEmailView.setText("");
                    mPasswordView.setText("");

                }
            } else {
                Toast.makeText(getBaseContext(), "Internet Interruption...", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            Toast.makeText(getBaseContext(), "LogIn Failed", Toast.LENGTH_SHORT).show();
            showProgress(false);
        }
    }

    private void autoSet() {
        String no=mEmailView.getText().toString();
        String password=mPasswordView.getText().toString();
        File f=new File("Info.txt");

        try {
            OutputStreamWriter out=new OutputStreamWriter(openFileOutput("Info.txt",MODE_PRIVATE));
out.write(no);
            out.write("\n");
            out.write(password);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  Toast.makeText(getBaseContext(),"autoset called",Toast.LENGTH_SHORT).show();


    }


    private void autoFill() {


        String Srno = "";
        String Pass = "";
        try {
            InputStream is = openFileInput("Info.txt");
            if (is != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String recieveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((recieveString = bufferedReader.readLine()) != null) {
                    if (count == 0) {
                        Srno = recieveString;
                        count++;
                    } else if (count == 1) {
                        Pass = recieveString;
                        count=0;
                    }

                }

            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        mEmailView.setText(Srno);
        mPasswordView.setText(Pass);
          //     Toast.makeText(getBaseContext(),"autofill called",Toast.LENGTH_SHORT).show();


    }


}

