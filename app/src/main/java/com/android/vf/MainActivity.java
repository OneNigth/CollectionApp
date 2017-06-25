package com.android.vf;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Fragment.FirstFragment;
import com.android.Fragment.LeftMenuFragment;
import com.android.Fragment.SceondFragment;
import com.android.Fragment.ThirdFragment;
import com.android.adapter.MFragmentPagerAdapter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * 主界面显示三个选项
 *
 * @author weizhi
 * @version 1.0
 */
public class MainActivity extends FragmentActivity {

    //第一页
    private TextView pictureTextView;
    //第二页
    private TextView movieTextView;
    //第三页
    private TextView musicTextView;
    //侧滑栏
    private TextView btMenu;

    //下栏的按钮图片
    private ImageView btAdd;
    //实现Tab滑动效果
    public static ViewPager mViewPager;

    //动画图片
    private ImageView cursor;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;

    //动画图片宽度
    private int bmpW;

    //当前页卡编号
    private int currIndex = 0;

    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;

    //管理Fragment
    private FragmentManager fragmentManager;

    //上下文
    public Context context;

    //侧滑栏
    private SlidingMenu menu;

    public static final String TAG = "MainActivity";
    //监听标签
    public static final int MODLE_LISTEN_TAB = 0;
    //监听按钮
    public static final int MODLE_LISTEN_BUTTON = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = this;

        //初始化TextView
        InitTextView();

        //初始化ImageView
        InitImageView();

        //初始化侧滑栏
        InitSlidMenu();

        //初始化Fragment
        InitFragment();

        //初始化ViewPager
        InitViewPager();

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onResume();
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {

        //第三页tab
        pictureTextView = (TextView) findViewById(R.id.picture_text);
        //第二页Tab
        movieTextView = (TextView) findViewById(R.id.movie_text);
        //第三页Tab
        musicTextView = (TextView) findViewById(R.id.music_text);
        //侧滑栏
        btMenu = (TextView) findViewById(R.id.slid_menu_bt);

        //中间的按钮图片
        btAdd = (ImageView) findViewById(R.id.add_bt);

        //添加点击事件
        pictureTextView.setOnClickListener(new MyOnClickListener(0, MODLE_LISTEN_TAB));
        movieTextView.setOnClickListener(new MyOnClickListener(1, MODLE_LISTEN_TAB));
        musicTextView.setOnClickListener(new MyOnClickListener(2, MODLE_LISTEN_TAB));

        btMenu.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.vPager);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, fragmentArrayList));

        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(2);

        //设置默认打开第一页
        mViewPager.setCurrentItem(0);

        //将顶部文字恢复默认值
        resetTextViewTextColor();
        pictureTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));

        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        btAdd.setOnClickListener(new MyOnClickListener(0, MODLE_LISTEN_BUTTON));
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 获取分辨率宽度
        int screenW = dm.widthPixels;

        bmpW = (screenW / 3);

        //设置动画图片宽度
        setBmpW(cursor, bmpW);
        offset = 0;

        //动画图片偏移量赋值
        position_one = (int) (screenW / 3.0);
        position_two = position_one * 2;

    }

    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment() {
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(new FirstFragment());
        fragmentArrayList.add(new SceondFragment());
        fragmentArrayList.add(new ThirdFragment());

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启事务
        // 替换事务
        transaction.replace(R.id.slide_main_left1, new LeftMenuFragment());
        transaction.commit();// 开启事务
    }

    private void InitSlidMenu() {
        menu = new SlidingMenu(this); // 创建侧滑菜单
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 设置触摸屏幕的模式
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset); // 设置滑动菜单视图的宽度
        menu.setFadeDegree(0.2f); // 设置渐入渐出效果的值
        // 把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 为侧滑菜单设置布局
        menu.setMenu(R.layout.slide_main_left);
        // menu.setMenu(new SlideLeft(menu.getContext()));
    }

    /**
     * 头标点击监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        //监听的分类，0为标签，1为按钮
        private int LISTEN_MODLE = 0;
        public MyOnClickListener(){

        }
        //标签监听
        public MyOnClickListener(int i, int LISTEN_MODLE) {
            index = i;
            this.LISTEN_MODLE = LISTEN_MODLE;
        }

        @Override
        public void onClick(View v) {
            if (LISTEN_MODLE == 0)
                mViewPager.setCurrentItem(index);
            //不同页按钮跳转不同activity
            if (LISTEN_MODLE == 1) {
                int page = mViewPager.getCurrentItem();
                switch (page) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, LoveActivity.class);
                        MainActivity.this.startActivity(intent);
//                        Toast.makeText(MainActivity.this,"page:"+page,Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
//                        Toast.makeText(MainActivity.this,"page:"+page,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
//                        Toast.makeText(MainActivity.this,"page:"+page,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            //侧滑按钮
            if (v.getId() == R.id.slid_menu_bt) {
                menu.showMenu();
            }
        }
    }

    /**
     * 页卡切换监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {

                //当前为页卡1
                case 0:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        resetTextViewTextColor();
                        pictureTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                    } else if (currIndex == 2) {//从页卡1跳转转到页卡3
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        resetTextViewTextColor();
                        pictureTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                    }
                    break;

                //当前为页卡2
                case 1:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        resetTextViewTextColor();
                        movieTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                    } else if (currIndex == 2) { //从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        resetTextViewTextColor();
                        movieTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                    }
                    break;

                //当前为页卡3
                case 2:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        resetTextViewTextColor();
                        musicTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                    } else if (currIndex == 1) {//从页卡1跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        resetTextViewTextColor();
                        musicTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
                    }
                    break;
            }
            currIndex = position;

            animation.setFillAfter(true);// true:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    ;

    /**
     * 设置动画图片宽度
     *
     * @param mWidth
     */
    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }

    /**
     * 将顶部文字恢复默认值
     */
    private void resetTextViewTextColor() {

        pictureTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        movieTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color));
        musicTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color));
    }
}
