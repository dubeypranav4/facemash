package com.sumit.kaushik.HBTUFacemash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FullImage extends Activity {

    ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        i=(ImageView) findViewById(R.id.fullView);
        i.setClickable(true);
        i.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        int check=getIntent().getIntExtra("male", 1);
        if(check==1){
            i.setImageDrawable(getResources().getDrawable(R.drawable.malen));
        }
        else if(check==0){
            i.setImageDrawable(getResources().getDrawable(R.drawable.femalen));
        }


    }

}
