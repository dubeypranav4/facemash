package com.example.sumitkaushik.hbtifacemashdemo1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Sumit Kaushik on 16-Mar-16.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {



    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
              return new Male();

            case 1:
                return new Female();


        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}
