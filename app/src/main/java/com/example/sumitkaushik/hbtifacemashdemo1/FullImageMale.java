package com.example.sumitkaushik.hbtifacemashdemo1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FullImageMale extends Activity {

    ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_male);
        i=(ImageView) findViewById(R.id.fullView);
        i.setClickable(true);
        i.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

            Bundle b=getIntent().getExtras();
            Bitmap bitmap= (Bitmap) b.get("image");
            i.setImageBitmap(bitmap);



    }

}
