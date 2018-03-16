package com.chefswithoutlimits.localDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class OrderModel {

    public static void addMenu(DatabaseHandler DB,OrderData orderData) {
        SQLiteDatabase db = DB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConstantDB.ORDER_ID,orderData.getOrder_id());
        values.put(ConstantDB.ORDER_KITCHEN_ID, orderData.getOrder_kitchen_id());
        values.put(ConstantDB.ORDER_MENU_ID, orderData.getOrder_menu_id());
        values.put(ConstantDB.ORDER_MENU_OPTION_ID,orderData.getOrder_menu_option_id());
        values.put(ConstantDB.ORDER_MENU_QUANTITY, orderData.getOrder_nenu_quantity());
        db.insert(ConstantDB.TABLE_ORDER, null, values);
        db.close();
    }

    // Getting Order Details
    public static ArrayList<OrderData> getOrderDetails(DatabaseHandler DB) {
        ArrayList<OrderData> orderList=new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + ConstantDB.TABLE_ORDER;

        SQLiteDatabase db = DB.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderData orderData = new OrderData();
                orderData.setTable_id(cursor.getString(0));
                orderData.setOrder_id(cursor.getString(1));
                orderData.setOrder_kitchen_id(cursor.getString(2));
                orderData.setOrder_menu_id(cursor.getString(3));
                orderData.setOrder_menu_option_id(cursor.getString(4));
                orderData.setOrder_nenu_quantity(cursor.getString(5));

                // Adding contact to list
                orderList.add(orderData);
            } while (cursor.moveToNext());
        }
        return orderList;
    }



    public static void updateMenuQuantity(DatabaseHandler DB, String table_Id, String quantity) {
        SQLiteDatabase db = DB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConstantDB.ORDER_MENU_QUANTITY,quantity);

        db.update(ConstantDB.TABLE_ORDER,values,ConstantDB.TABLE_ID+ "=?", new String[]{table_Id});
        db.close(); // Closing database connection
    }

    public static void deleteOrder(DatabaseHandler DB){
        SQLiteDatabase db = DB.getWritableDatabase();
        db.execSQL("delete from "+ ConstantDB.TABLE_ORDER);
    }

    public static void deleteMenuFromOrder(DatabaseHandler DB){
        SQLiteDatabase db = DB.getWritableDatabase();
        db.execSQL("delete from "+ ConstantDB.TABLE_ORDER);
    }

    public static void clearOrderTable(DatabaseHandler DB){
        SQLiteDatabase db = DB.getWritableDatabase();
        db.execSQL("delete from "+ ConstantDB.TABLE_ORDER);
    }
}
