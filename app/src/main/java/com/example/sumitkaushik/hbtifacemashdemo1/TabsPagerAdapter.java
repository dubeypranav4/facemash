package com.example.sumitkaushik.hbtifacemashdemo1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.io.ByteArrayOutputStream;

/**
 * Created by Sumit Kaushik on 16-Mar-16.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {


Bitmap maleImage;
    Bitmap femaleImage;
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0: {
                Male fragment=new Male();
                Bundle args=new Bundle();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                maleImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                args.putByteArray("Male", imageBytes);
                fragment.setArguments(args);
                return fragment;

            }
            case 1: {
                Female fragment=new Female();
                Bundle args=new Bundle();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                femaleImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                args.putByteArray("Female",imageBytes);
                fragment.setArguments(args);
                return fragment;
            }

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }


    public void init(Bitmap bitmap, Bitmap bitmap1) {
        maleImage=bitmap;
        femaleImage=bitmap1;
    }
}
