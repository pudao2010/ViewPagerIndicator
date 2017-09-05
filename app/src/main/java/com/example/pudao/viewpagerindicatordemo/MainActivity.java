package com.example.pudao.viewpagerindicatordemo;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pudao.viewpagerindicatordemo.adapter.CommonAdapter;
import com.example.pudao.viewpagerindicatordemo.ui.CommonFragment;
import com.example.pudao.viewpagerindicatordemo.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private CommonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        String[] titles = {"标题1", "标题2", "标题3", "标题4", "标题5", "标题6", "标题7", "标题8", "标题9"};
        mIndicator.setTabItemTitles(Arrays.asList(titles));
        List<Fragment> fragments = new ArrayList<>();
        for (String title : titles) {
            fragments.add(CommonFragment.newInstance(title));
        }
        mAdapter = new CommonAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);

        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initView() {
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }
}
