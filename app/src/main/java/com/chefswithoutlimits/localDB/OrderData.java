package com.chefswithoutlimits.localDB;

/**
 * Created by db on 10/16/2017.
 */
public class OrderData {

    public  String table_id="";
    public  String order_id = "";
    public  String order_kitchen_id = "";
    public  String order_menu_id = "";
    public  String order_menu_option_id = "";
    public  String order_nenu_quantity = "";

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_kitchen_id() {
        return order_kitchen_id;
    }

    public void setOrder_kitchen_id(String order_kitchen_id) {
        this.order_kitchen_id = order_kitchen_id;
    }

    public String getOrder_menu_id() {
        return order_menu_id;
    }

    public void setOrder_menu_id(String order_menu_id) {
        this.order_menu_id = order_menu_id;
    }

    public String getOrder_menu_option_id() {
        return order_menu_option_id;
    }

    public void setOrder_menu_option_id(String order_menu_option_id) {
        this.order_menu_option_id = order_menu_option_id;
    }

    public String getOrder_nenu_quantity() {
        return order_nenu_quantity;
    }

    public void setOrder_nenu_quantity(String order_nenu_quantity) {
        this.order_nenu_quantity = order_nenu_quantity;
    }


}
