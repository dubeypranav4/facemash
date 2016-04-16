package com.example.sumitkaushik.hbtifacemashdemo1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Help extends AppCompatActivity {

    Button button;
    EditText e1, e2, e3;
    String name, srno, mobileNo;
    String[] email = {"sumitkaushik4867@gmail.com", "dubeypranav4@gmail.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        button = (Button) findViewById(R.id.bSend);
        e1 = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);
        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                View focusView = null;
                boolean check = true;
                name = e1.getText().toString();
                srno = e2.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    e1.setError("This Field is required");
                    focusView = e1;
                    check = false;
                }
                if (TextUtils.isEmpty(srno)) {
                    e2.setError("This Field is required");
                    focusView = e2;
                    check = false;
                }

                if (srno.indexOf("/") == -1) {
                    Toast.makeText(getBaseContext(), "Invalid Sr. No.", Toast.LENGTH_SHORT).show();
                    focusView = e2;
                    check = false;
                }


                if (check) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
                    String msg = "\n Senders Information:" + "\n Name: " + name + "\n Sr. No. " + srno ;

                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Forget Password");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send Email..."));
                        finish();
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(Help.this, "THERE IS NO EMAIL CLIENT INSTALLED", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!check) {
                    focusView.requestFocus();
                }
            }
        });
    }
}
