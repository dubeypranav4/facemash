package com.example.sumitkaushik.hbtifacemashdemo1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Female extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_female, container, false);
        Bundle args = getArguments();
        byte[] stream = args.getByteArray("Female");
        final Bitmap bitmap = BitmapFactory.decodeByteArray(stream, 0, stream.length);

        // get the button view
        CircularImageView m = ((CircularImageView) rootView.findViewById(R.id.female));
        m.setImageBitmap(bitmap);

        // get the button view
        //    m = (CircularImageView) getView().findViewById(R.id.female);
        //  m.setImageBitmap(BitmapFactory.decodeByteArray(stream,0,stream.length));
        // set a onclick listener for when the button gets clicked
        m.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent("android.intent.action.FULLIMAGEFEMALE");
                i.putExtra("image", bitmap);

                startActivity(i);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);


    }


}
