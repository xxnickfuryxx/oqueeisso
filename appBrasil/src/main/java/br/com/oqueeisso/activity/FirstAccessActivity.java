package br.com.oqueeisso.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.oqueeisso.R;
import br.com.oqueeisso.dialog.WaitDialog;
import br.com.oqueeisso.fragment.FirstFragment;
import br.com.oqueeisso.fragment.SecondFragment;
import br.com.oqueeisso.fragment.ThirdFragment;
import br.com.oqueeisso.indicator.CirclePageIndicator;

public class FirstAccessActivity extends FragmentActivity{

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_access);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        CirclePageIndicator circlePageIndicator = (CirclePageIndicator)findViewById(R.id.ind_circles);
        circlePageIndicator.setViewPager(mPager);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                default:
                    return new FirstFragment();
            }
        }

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
