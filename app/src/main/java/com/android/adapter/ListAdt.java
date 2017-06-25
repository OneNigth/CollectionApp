package com.android.adapter;

import java.util.Vector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.vf.R;


public class ListAdt extends BaseAdapter {


    public class BookModel {
        public String title;
        public String imageUrl;
    }

    private LayoutInflater mInflater;
    private Vector<BookModel> mModels = new Vector<BookModel>();
    private ListView mListView;
    SyncImageLoader syncImageLoader;
    private LinearLayout linearLayout;

    public ListAdt(Context context, ListView listView) {
        mInflater = LayoutInflater.from(context);
        syncImageLoader = new SyncImageLoader();
        mListView = listView;

		/*
         *
		 * 这一句话取消掉注释的话，那么能更加的节省资源，不过体验稍微有点，
		 * 你滑动的时候不会读取图片，当手放开后才开始度图片速度更快，你们可以试一试
		 */

//		mListView.setOnScrollListener(onScrollListener);
    }

    public void addBook(String title, String imageUrl) {
        BookModel model = new BookModel();
        model.title = title;
        model.imageUrl = imageUrl;
        mModels.add(model);
    }

    public int size() {
        return mModels.size();
    }

    public void clean() {
        mModels.clear();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mModels.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= getCount()) {
            return null;
        }
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_layout, null);
        }
        //获取item的linnerlayout添加上下文菜单
        linearLayout = (LinearLayout) convertView.findViewById(R.id.first_list_item);

        BookModel model = mModels.get(position);
        convertView.setTag(position);
        ImageView iv = (ImageView) convertView.findViewById(R.id.list_image);
        TextView sItemTitle = (TextView) convertView
                .findViewById(R.id.list_title);
        sItemTitle.setText(model.title);

        // 添加背景在滑动的时就会显示背景而不是其他的缓存的照片，用户体验更好
        iv.setBackgroundResource(R.drawable.souqiqulogo);
        syncImageLoader.loadImage(position, model.imageUrl,
                imageLoadListener, model.title);
        return convertView;
    }
    public LinearLayout getLinearLayout(){
        return linearLayout;
    }
    SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {

        @Override
        public void onImageLoad(Integer t, Drawable drawable) {
            // BookModel model = (BookModel) getItem(t);
            View view = mListView.findViewWithTag(t);
            if (view != null) {
                ImageView iv = (ImageView) view
                        .findViewById(R.id.list_image);
                iv.setBackgroundDrawable(drawable);
            }
        }

        @Override
        public void onError(Integer t) {
            BookModel model = (BookModel) getItem(t);
            View view = mListView.findViewWithTag(model);
            if (view != null) {
                ImageView iv = (ImageView) view
                        .findViewById(R.id.list_image);
                iv.setBackgroundResource(R.drawable.ic_launcher);
            }
        }

    };

    public void loadImage() {
        int start = mListView.getFirstVisiblePosition();
        int end = mListView.getLastVisiblePosition();
        if (end >= getCount()) {
            end = getCount() - 1;
        }
        syncImageLoader.setLoadLimit(start, end);
        syncImageLoader.unlock();
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                // 滚动状态
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    syncImageLoader.lock();
                    break;
                // 停止滚动
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    loadImage();
                    break;
                // 正在触摸状态的滚动
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    syncImageLoader.lock();
                    break;

                default:
                    break;
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }
    };
}

