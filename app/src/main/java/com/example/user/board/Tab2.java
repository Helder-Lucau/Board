package com.example.user.board;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Tab2 extends AppCompatActivity {

    private TabLayout tablayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        tablayout = (TabLayout) findViewById(R.id.tablayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarid);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());

        //Adding fragments
        adapter.AddFragment(new GeneralFragment(),"Top Stories");
        adapter.AddFragment(new SpecificFragment(),"Faculty News");
        adapter.AddFragment(new PostFragment(),"My News");

        //adapter setup
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

    }
}