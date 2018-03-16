package com.chefswithoutlimits.localDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, ConstantDB.DATABASE_NAME, null, ConstantDB.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PROFILE_STATUS_TABLE = "CREATE TABLE " + ConstantDB.TABLE_ORDER + "("
                + ConstantDB.TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ConstantDB.ORDER_ID + " VARCHAR,"
                + ConstantDB.ORDER_KITCHEN_ID + " VARCHAR,"
                + ConstantDB.ORDER_MENU_ID + " VARCHAR,"
                + ConstantDB.ORDER_MENU_OPTION_ID + " VARCHAR,"
                + ConstantDB.ORDER_MENU_QUANTITY + " VARCHAR" + ")";
        db.execSQL(CREATE_PROFILE_STATUS_TABLE);



    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ConstantDB.TABLE_CONTACT_PHONE);
        // Create tables again
        onCreate(db);*/

    }
}
