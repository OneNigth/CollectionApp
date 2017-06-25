package com.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.vf.MainActivity;
import com.android.vf.MyApplication;
import com.android.vf.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftMenuFragment extends Fragment implements View.OnClickListener {

    View view;
    public Context context;
    //按钮
    private LinearLayout menuFirst, menuSceond, menuThird, menuFourth;

    public LeftMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_left_menu, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }
    //初始化
    private void init() {
        menuFirst = (LinearLayout) view.findViewById(R.id.left_menu_first);
        menuSceond = (LinearLayout) view.findViewById(R.id.left_menu_sceond);
        menuThird = (LinearLayout) view.findViewById(R.id.left_menu_third);
        menuFourth = (LinearLayout) view.findViewById(R.id.left_menu_fourth);

        context = MyApplication.getContext();
        //监听
        menuFirst.setOnClickListener(this);
        menuSceond.setOnClickListener(this);
        menuThird.setOnClickListener(this);
        menuFourth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String st = "";
        switch (id) {
            case R.id.left_menu_first:
                st = "第一页";
                break;
            case R.id.left_menu_sceond:
                st = "第二页";
                break;
            case R.id.left_menu_third:
                st = "第三页";
                break;
            case R.id.left_menu_fourth:
                st = "第四页";
                break;
        }
        makeToast(st);
    }

    private void makeToast(String st) {
        Toast.makeText(context,st,Toast.LENGTH_SHORT).show();
    }
}
