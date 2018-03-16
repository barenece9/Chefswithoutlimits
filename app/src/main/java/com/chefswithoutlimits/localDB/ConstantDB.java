package com.chefswithoutlimits.localDB;

/**
 * Created by apps2 on 7/12/2017.
 */
public class ConstantDB {

    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "chefs";

    // table name
    public static final String TABLE_ORDER = "chefs_order";


    // contact Table Columns names
    public static final String TABLE_ID = "table_id";
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_KITCHEN_ID = "order_kitchen_id";
    public static final String ORDER_MENU_ID = "order_menu_id";
    public static final String ORDER_MENU_OPTION_ID = "order_menu_option_id";
    public static final String ORDER_MENU_QUANTITY = "order_nenu_quantity";


    /* params.put("kitchen_id",kitchen_id);
                params.put("menu_id",menu_id);
                params.put("menu_item",menu_item);
                params.put("quantity","1");
                params.put("format","json");*/




}
