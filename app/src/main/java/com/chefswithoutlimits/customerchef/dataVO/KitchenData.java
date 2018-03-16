package com.chefswithoutlimits.customerchef.dataVO;

/**
 * Created by db on 4/11/2017.
 */
public class KitchenData {
    public static String getMin_order() {
        return min_order;
    }

    public static void setMin_order(String min_order) {
        KitchenData.min_order = min_order;
    }

    public static String kichen_Logo="";
    public static String kichen_rating="";
    public static String kichen_name="";

    public static String getKitchen_id() {
        return kitchen_id;
    }

    public static void setKitchen_id(String kitchen_id) {
        KitchenData.kitchen_id = kitchen_id;
    }

    public static String insurance_file="";
    public static String distance="";
    public static String kitchen_id="";

    public static String min_order="";

    public static String getDistance() {
        return distance;
    }

    public static void setDistance(String distance) {
        KitchenData.distance = distance;
    }

    public static String getDistance_cover_by_vehicle() {
        return distance_cover_by_vehicle;
    }

    public static void setDistance_cover_by_vehicle(String distance_cover_by_vehicle) {
        KitchenData.distance_cover_by_vehicle = distance_cover_by_vehicle;
    }

    public static String distance_cover_by_vehicle="";

    public static String getInsurance_file() {
        return insurance_file;
    }

    public static void setInsurance_file(String insurance_file) {
        KitchenData.insurance_file = insurance_file;
    }

    public String getKichen_Logo() {
        return kichen_Logo;
    }

    public void setKichen_Logo(String kichen_Logo) {
        this.kichen_Logo = kichen_Logo;
    }

    public String getKichen_rating() {
        return kichen_rating;
    }

    public void setKichen_rating(String kichen_rating) {
        this.kichen_rating = kichen_rating;
    }

    public String getKichen_name() {
        return kichen_name;
    }

    public void setKichen_name(String kichen_name) {
        this.kichen_name = kichen_name;
    }
}
