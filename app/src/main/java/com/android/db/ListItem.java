package com.android.db;

/**
 * Created by Jne
 * Date: 2015/1/6.
 */
public class ListItem {
    public int Id;//id
    public String Image;//图片
    public String Url;//内容
    public String Title;//标题

    public ListItem(){

    }
     ListItem(int Id, String Image, String Url , String Title) {
        this.Id = Id;
        this.Image = Image;
        this.Url = Url;
         this.Title = Title;
    }
}
