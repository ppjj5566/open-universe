package com.example.john.openmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.openmap.UIconnector.tools;
import com.example.john.openmap.helper.SessionManager;
import com.example.john.openmap.map_activity.MainActivity;
import com.example.john.openmap.map_activity.log_out;
import com.example.john.openmap.provider_shower.providers;


/**
 * Created by ppjj5 on 2018-01-22.
 *
 */

public class tab_layout extends AppCompatActivity{
    SessionManager session;
    ViewPager viewPager;
    Pager pagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new Pager(getSupportFragmentManager(),tab_layout.this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    private void logoutUser(){
        Intent intent = new Intent(tab_layout.this, log_out.class);
        startActivity(intent);
        finish();
    }



    public class Pager extends FragmentPagerAdapter{

        String names[] = {"universe","own info","info"};
        Fragment[] fragments = new Fragment[names.length];
        Context context;

        Pager(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        View getTabView(int position) {
            View tab = LayoutInflater.from(tab_layout.this).inflate(R.layout.tab_names, null);
            TextView tv = tab.findViewById(R.id.tabs_name);
            tv.setText(names[position]);
            return tab;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return names[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MainActivity(context);
                case 1:
                    return new providers(context);
                case 2:
                    return new tools(context);
            }
            return null;
        }


        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container,position);
            fragments[position] = createdFragment;
            return createdFragment;
        }
    }

}
