package com.android.vf;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.db.ItemDao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LoveActivity extends Activity implements View.OnClickListener {
    //确定按钮
    private Button sureBt;
    //标题和链接输入框
    private EditText titleText, urlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);

        //初始化组件
        initView();
    }

    private void initView() {
        sureBt = (Button) findViewById(R.id.bt_sure);
        titleText = (EditText) findViewById(R.id.love_title);
        urlText = (EditText) findViewById(R.id.love_url);

        sureBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_sure:
                //获取网页
                catchURL();
                break;
        }
    }

    private String image = "";

    private void catchURL() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //获取网页
                    Document doc = Jsoup.connect("http://www.qiushibaike.com/8hr/page/1/").get();
//                    Log.d("一、內容", doc.toString());
                    //src属性
                    Elements element = doc.select("[src]");
                    //获取网页的第一张图片
                    image = element.get(0).attr("abs:src") + element.get(0).attr("width") + element.get(0).attr("height");
//                    for (Element src : element) {
//                        //img标签
//                        if (src.tagName().equals("img")) {
//                            String st = src.attr("abs:src") + src.attr("width") + src.attr("height");
////                            Log.d("图片地址：", "" + st);
//                        }
//                        else
//                            Log.d("src:",""+  src.tagName()+src.attr("abs:src"));
//                    }
                    Log.d("完成网页抓取", "");
                    //添加数据库
                    doDB();

                } catch (IOException e) {
                    Toast.makeText(LoveActivity.this, "链接有问题", Toast.LENGTH_SHORT).show();
                    Log.d("链接错误", "错误");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //添加数据库
    private void doDB() {
        String title = titleText.getText().toString();
        String url = urlText.getText().toString();
        ItemDao itemDao = new ItemDao(LoveActivity.this);

        if (itemDao.insertDate(image, title, url)) {
            Log.d("item添加成功", "item添加成功");
//            Toast.makeText(LoveActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
            itemDao.isDataExist();
            finish();
        } else {
            Log.d("item添加失败", "item添加失败");
//            Toast.makeText(LoveActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
        }

    }
}
