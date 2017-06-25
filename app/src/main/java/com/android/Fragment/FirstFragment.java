package com.android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.adapter.ListAdt;
import com.android.db.ItemDao;
import com.android.db.ListItem;
import com.android.vf.MyApplication;
import com.android.vf.R;
import com.android.vf.WebActivity;
import com.android.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {
    //通过该view获取组件
    private View view;
    //列表View
    private XListView xListView;
    //列表适配器
    private ListAdt listAdt;
    //列表内容
    private List<ListItem> mList;
    //数据库操作
    private ItemDao itemDao;
    //上下文
    private Context context;
    //上下文菜单选项
    private static final int COPY_ID = ContextMenu.FIRST + 1;
    private static final int DELETE_ID = ContextMenu.FIRST + 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, COPY_ID, 0, "复制").setActionView(v);
        menu.add(0, DELETE_ID, 1, "删除").setActionView(v);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id = item.getItemId();
        int position = menuInfo.position-1;
        switch (id) {
            case COPY_ID:
                copyData(position);
//                Log.d("当前是", "" + menuInfo.position);
                break;
            case DELETE_ID:
                //数据库id需+1
                if(itemDao.deleteOrder(position+1)){
                    makeToast("删除成功");
                    listAdt.notifyDataSetChanged();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 吐司提示
     * @param st 提示内容
     */
    private void makeToast(String st){
        Toast.makeText(context,st,Toast.LENGTH_SHORT).show();
    }

    //复制到剪贴板
    private void copyData(int position) {
        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(mList.get(position).Url);
        makeToast("已复制到剪贴板");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        xListView = (XListView) view.findViewById(R.id.first_list);
        mList = new ArrayList<ListItem>();
        //获取当前上下文
        context = MyApplication.getContext();
        //获取数据库
        itemDao = new ItemDao(context);
        //适配器
        listAdt = new ListAdt(context, xListView);

        xListView.setAdapter(listAdt);

        //添加上下文菜单
        xListView.setOnCreateContextMenuListener(this);

        xListView.setOnItemClickListener(this);//点击事件监听
        xListView.setXListViewListener(this);// 加载和刷新
        onLoadMore();
        xListView.setPullLoadEnable(true);// 可刷新
        xListView.setPullRefreshEnable(true);// 可加载


    }

    //handler
    private static final int REFRESH_LIST = 0x10001;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                handleOtherMessage(msg.what);
            }
        }

        ;
    };

    private void handleOtherMessage(int flag) {

        switch (flag) {
            case REFRESH_LIST:
                listAdt.notifyDataSetChanged();
            default:
                break;
        }
    }

    //点击
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(context, "当前item的url是:" + mList.get(position - 1).Url, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", mList.get(position - 1).Url.toString());
        startActivity(intent);
    }

    //刷新
    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listAdt.clean();
                xListView.setAdapter(listAdt);
                loadList();
                onLoad();
            }
        }, 2000);
    }


    //加载
    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                listAdt.clean();
                loadList();
                listAdt.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }


    //数据库加载处理
    private void loadList() {
        mList = itemDao.getAllDate();
        if (mList != null) {
            int j = mList.size();
            if (j != 0)
                for (int i = 0; i < j; i++) {
                    listAdt.addBook(mList.get(i).Title, mList.get(i).Image);
                }
        }

        listAdt.notifyDataSetChanged();
    }
    // 加载完成后

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
    }


}
