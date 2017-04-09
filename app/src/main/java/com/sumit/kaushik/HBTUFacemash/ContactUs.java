package com.sumit.kaushik.HBTUFacemash;

/**
 * Created by Pranav Dubey on 09-Mar-16.
 */


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactUs extends Activity implements View.OnClickListener {
    EditText  subject, content;
    Button send;
    String  sub, cont, info;
    String name, srno;
    String[] email = {"hbtifacemash@gmail.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        Bundle b = getIntent().getExtras();
        String info = b.getString("info");
        String strings[] = info.split(",");
        name = strings[1];
        srno = strings[0];
        init();
        send.setOnClickListener(this);

    }

    private void init() {


        subject = (EditText) findViewById(R.id.et2);
        content = (EditText) findViewById(R.id.et3);
        //  link=(EditText) findViewById(R.id.link);
        send = (Button) findViewById(R.id.b1);
        //   share=(Button)findViewById(R.id.b2);

    }

    public void onClick(View v) {

        convert();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        String msg = cont + "\n " + info + "\n senders information:" + "\n name:" + name + "\n SR NO." + srno;
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
        emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email..."));
            finish();
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(ContactUs.this, "THERE IS NO EMAIL CLIENT INSTALLED", Toast.LENGTH_SHORT).show();
        }
    }

    private void convert() {
        sub = subject.getText().toString();
        cont = content.getText().toString();


    }

}


