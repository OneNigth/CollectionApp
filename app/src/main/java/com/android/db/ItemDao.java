package com.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jne
 * Date: 2015/1/6.
 */
public class ItemDao {
    private static final String TAG = "OrdersDao";

    // 列定义
    private final String[] ORDER_COLUMNS = new String[]{"Id", "Image", "Title","Url"};

    private Context context;
    private ItemDBHelper ordersDBHelper;

    public ItemDao(Context context) {
        this.context = context;
        ordersDBHelper = new ItemDBHelper(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist() {
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(ItemDBHelper.TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    /**
     * 初始化数据
     */
    public void initTable() {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + ItemDBHelper.TABLE_NAME + " (Id, CustomName, OrderPrice, Country) values (1, 'http://mmbiz.qpic    .cn/mmbiz_jpg/Fic8lEicGJjYlFw1kDJq8vRZQbLmBxbnmjZHjRFISGKsAfgrKjCuOADbqsNbdRGiclvoUUbJkSg7keEia7yJTk1LibQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1', 'http://mp.weixin.qq.com/s/EiiocUyN3Zaqv7H4uYG3gw')");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 执行自定义SQL语句
     */
//    public void execSQL(String sql) {
//        SQLiteDatabase db = null;
//
//        try {
//            if (sql.contains("select")) {
//                Toast.makeText(context, R.string.strUnableSql, Toast.LENGTH_SHORT).show();
//            } else if (sql.contains("insert") || sql.contains("update") || sql.contains("delete")) {
//                db = ordersDBHelper.getWritableDatabase();
//                db.beginTransaction();
//                db.execSQL(sql);
//                db.setTransactionSuccessful();
//                Toast.makeText(context, R.string.strSuccessSql, Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(context, R.string.strErrorSql, Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "", e);
//        } finally {
//            if (db != null) {
//                db.endTransaction();
//                db.close();
//            }
//        }
//    }

    /**
     * 查询数据库中所有数据
     */
    public List<ListItem> getAllDate() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(ItemDBHelper.TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<ListItem> itemList = new ArrayList<ListItem>(cursor.getCount());
                while (cursor.moveToNext()) {
                    itemList.add(parseListItem(cursor));
                }
                return itemList;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 新增一条数据
     */
    public boolean insertDate(String Image, String Title , String Url) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // insert into Orders(Id, CustomName, OrderPrice, Country) values (7, "Jne", 700, "China");
            ContentValues contentValues = new ContentValues();
            contentValues.put("Image", Image);
            contentValues.put("Title", Title);
            contentValues.put("Url", Url);
            db.insertOrThrow(ItemDBHelper.TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 删除一条数据  此处删除Id为?的数据
     */
    public boolean deleteOrder(int Id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // delete from Orders where Id = 7
            db.delete(ItemDBHelper.TABLE_NAME, "Id = ?", new String[]{String.valueOf(Id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

//    /**
//     * 修改一条数据  此处将Id为6的数据的OrderPrice修改了800
//     */
//    public boolean updateOrder() {
//        SQLiteDatabase db = null;
//        try {
//            db = ordersDBHelper.getWritableDatabase();
//            db.beginTransaction();
//
//            // update Orders set OrderPrice = 800 where Id = 6
//            ContentValues cv = new ContentValues();
//            cv.put("OrderPrice", 800);
//            db.update(OrderDBHelper.TABLE_NAME,
//                    cv,
//                    "Id = ?",
//                    new String[]{String.valueOf(6)});
//            db.setTransactionSuccessful();
//            return true;
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (db != null) {
//                db.endTransaction();
//                db.close();
//            }
//        }
//
//        return false;
//    }

    /**
     * 数据查询  此处将用户名为???的信息提取出来
     */
    public List<ListItem> getBorOrder() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();

            // select * from Orders where CustomName = 'Bor'
            cursor = db.query(ItemDBHelper.TABLE_NAME,
                    ORDER_COLUMNS,
                    "CustomName = ?",
                    new String[]{"Bor"},
                    null, null, null);

            if (cursor.getCount() > 0) {
                List<ListItem> listItems = new ArrayList<ListItem>(cursor.getCount());
                while (cursor.moveToNext()) {
                    ListItem item = parseListItem(cursor);
                    listItems.add(item);
                }
                return listItems;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 统计查询  此处查询Country为China的用户总数
     */
//    public int getChinaCount() {
//        int count = 0;
//
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//
//        try {
//            db = ordersDBHelper.getReadableDatabase();
//            // select count(Id) from Orders where Country = 'China'
//            cursor = db.query(ItemDBHelper.TABLE_NAME,
//                    new String[]{"COUNT(Id)"},
//                    "Country = ?",
//                    new String[]{"China"},
//                    null, null, null);
//
//            if (cursor.moveToFirst()) {
//                count = cursor.getInt(0);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return count;
//    }

    /**
     * 比较查询  此处查询单笔数据中OrderPrice最高的
     */
//    public ListItem getMaxOrderPrice() {
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//
//        try {
//            db = ordersDBHelper.getReadableDatabase();
//            // select Id, CustomName, Max(OrderPrice) as OrderPrice, Country from Orders
//            cursor = db.query(OrderDBHelper.TABLE_NAME, new String[]{"Id", "CustomName", "Max(OrderPrice) as OrderPrice", "Country"}, null, null, null, null, null);
//
//            if (cursor.getCount() > 0) {
//                if (cursor.moveToFirst()) {
//                    return parseListItem(cursor);
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return null;
//    }

    /**
     * 将查找到的数据转换成ListItem类
     */
    private ListItem parseListItem(Cursor cursor) {
        ListItem listItem = new ListItem();
        listItem.Id = (cursor.getInt(cursor.getColumnIndex("Id")));
        listItem.Image = (cursor.getString(cursor.getColumnIndex("Image")));
        listItem.Title = (cursor.getString(cursor.getColumnIndex("Title")));
        listItem.Url = (cursor.getString(cursor.getColumnIndex("Url")));
        return listItem;
    }
}
