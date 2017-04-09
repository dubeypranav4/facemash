package com.sumit.kaushik.HBTUFacemash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Gender extends Activity {

    Button bMale,bFemale;
    String male,female;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        Bundle bundle=getIntent().getExtras();
        final String info=bundle.getString("info");
        bMale=(Button)findViewById(R.id.bMale);
        bFemale=(Button)findViewById(R.id.bFemale);
        male=bMale.getText().toString();
        female=bFemale.getText().toString();

        bMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Gender.this, Game.class);
                i.putExtra("info", male+","+info);

                startActivity(i);
            }
        });
        bFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Gender.this, Game.class);
                i.putExtra("info", female+","+info);
                startActivity(i);

            }
        });
    }
}
