package com.example.pudao.viewpagerindicatordemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pudao.viewpagerindicatordemo.R;

/**
 * Created by pucheng on 2017/9/5.
 */

public class CommonFragment extends Fragment {

    private TextView tvContent;
    private String title;

    public static Fragment newInstance(String title) {
        CommonFragment fragment = new CommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View ret = inflater.inflate(R.layout.fragment_common, container, false);
        tvContent = (TextView) ret.findViewById(R.id.tv_content);
        title = getArguments().getString("title");
        tvContent.setText(title);
        return ret;
    }

    public String getTitle(){
        return title;
    }

}
