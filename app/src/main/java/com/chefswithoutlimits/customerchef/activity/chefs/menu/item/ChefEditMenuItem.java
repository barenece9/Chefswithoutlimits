package com.chefswithoutlimits.customerchef.activity.chefs.menu.item;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.WebServiceURL;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChefEditMenuItem extends Activity {


    ImageView imageView2;
    TextView headerTxt;
    Button btn_add,btn_brouse,btn_close;
    String menu_name="",menu_desc="",menu_regular_price="",menu_sales_price="",menu_max_quantity_available="";
    String multiple_option="Y",base_price_status="Y",priced_stat="PR";
    UserInformation userInfo;
    ImageView btnBack;
    ImageView img_take;
    EditText etn_menu_name,etn_menu_desc,etn_menu_regular_price,etn_menu_sales_price,etn_menu_max_quantity_available;
    Spinner spinner_category,spinner_available;

    CheckBox checkbox_multiple_option,checkbox_base_price;
    RadioButton radio_Priced,radio_non_price;
    RadioGroup pricing_option;

    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayList<String> categoryId = new ArrayList<String>();

    ArrayList<String> availablity=new ArrayList<>();

    ArrayAdapter<String> adaptercategory;
    ArrayAdapter<String> adapteravailablity;

    String selectcategoryList="",selectavailablity="";

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    String item_name="",item_id="";
    private static final int
            REQUEST_READ_STORAGE = 115;
    Uri compressUri = null;
    private static final int ImageSelect=132;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_edit_menu_item);
        Bundle bundle = getIntent().getExtras();
        item_id = bundle.getString("item_id");
        item_name = bundle.getString("item_name");
        setwidget();
        getMenuCategoryItem();
    }
    public  void setwidget(){

        userInfo = Sharepreferences.getSharePreferance(this);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Edit Menu Item");

        etn_menu_name=(EditText)findViewById(R.id.etn_menu_name);
        etn_menu_name.setText(item_name);
        etn_menu_desc=(EditText)findViewById(R.id.etn_menu_desc);
        etn_menu_regular_price=(EditText)findViewById(R.id.etn_menu_regular_price);
        etn_menu_sales_price=(EditText)findViewById(R.id.etn_menu_sales_price);
        etn_menu_max_quantity_available=(EditText)findViewById(R.id.etn_menu_max_quantity_available);

        checkbox_multiple_option=(CheckBox)findViewById(R.id.checkbox_multiple_option);
        checkbox_base_price=(CheckBox)findViewById(R.id.checkbox_base_price);
        pricing_option=(RadioGroup)findViewById(R.id.pricing_option);
        radio_Priced=(RadioButton)findViewById(R.id.radio_Priced);
        radio_non_price=(RadioButton)findViewById(R.id.radio_non_price);

        pricing_option.setVisibility(View.GONE);
        radio_Priced.setVisibility(View.GONE);
        radio_non_price.setVisibility(View.GONE);
        checkbox_base_price.setVisibility(View.GONE);
        checkbox_multiple_option.setVisibility(View.GONE);



        spinner_category=(Spinner)findViewById(R.id.spinner_category);
        spinner_available=(Spinner)findViewById(R.id.spinner_available);

        adaptercategory = new ArrayAdapter<String>(ChefEditMenuItem.this,R.layout.spinner_rows, categoryList);
        adaptercategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adaptercategory);

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               // selectcategoryList=categoryList.get(position);
                selectcategoryList=categoryId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        availablity.add("Select availability");
        availablity.add("Yes");
        availablity.add("No");

        adapteravailablity = new ArrayAdapter<String>(ChefEditMenuItem.this,R.layout.spinner_rows, availablity);
        adapteravailablity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_available.setAdapter(adapteravailablity);

        spinner_available.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               String checkselectavailablity=availablity.get(position);
                if(checkselectavailablity.equalsIgnoreCase("Yes")){
                    selectavailablity="1";
                }else if(checkselectavailablity.equalsIgnoreCase("No")){
                    selectavailablity="0";
                }else {
                    selectavailablity="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_take=(ImageView)findViewById(R.id.img_take);
        btn_brouse=(Button)findViewById(R.id.btn_brouse);
        btn_brouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasPermissionRead = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if(hasPermissionRead) {
                    takeimage();
                }else {
                    ActivityCompat.requestPermissions(ChefEditMenuItem.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_READ_STORAGE);
                }
            }
        });
        btn_close=(Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefEditMenuItem.this)){
                    startActivity(new Intent(ChefEditMenuItem.this,ChefMenuItemActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });


        btn_add=(Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_name=etn_menu_name.getText().toString();
                menu_desc=etn_menu_desc.getText().toString();
                //menu_regular_price=String.valueOf(round(Double.valueOf(etn_menu_regular_price.getText().toString()), 2));
               // menu_sales_price=String.valueOf(round(Double.valueOf(etn_menu_sales_price.getText().toString()), 2));
                menu_regular_price=etn_menu_regular_price.getText().toString();
                menu_sales_price=etn_menu_sales_price.getText().toString();
                //menu_max_quantity_available=etn_menu_max_quantity_available.getText().toString();


                if(menu_name.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ChefEditMenuItem.this, "Enter Menu name");
                }else if(selectcategoryList.equalsIgnoreCase("")||selectcategoryList.equalsIgnoreCase("-1")){
                    CreateDialog.showDialog(ChefEditMenuItem.this, "Select Menu Category");
                }else if(selectavailablity.equalsIgnoreCase("")||selectavailablity.equalsIgnoreCase("Select availability")){
                    CreateDialog.showDialog(ChefEditMenuItem.this, "Select availability");
                }
                else {

                    if(InternetConnectivity.isConnectedFast(ChefEditMenuItem.this)){
                        editmenuitem();
                    }else {
                        Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefEditMenuItem.this)){
                    startActivity(new Intent(ChefEditMenuItem.this,ChefMenuItemActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public  void editmenuitem(){
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditMenuItem.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.EDIT_MENU_ITEM;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseData(response);
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(ChefEditMenuItem.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefEditMenuItem.this);
                Map<String, String>  params = new HashMap<String, String>();
                String image="";
                if(bitmap!=null) {
                    image=BitMapToString(bitmap);
                   // image = getStringImage2(bitmap);
                }
                params.put("userid",userInfo.getUserId());
                params.put("menu_id",item_id);
                params.put("menu_name",menu_name);

                params.put("menu_category",selectcategoryList);
                params.put("menu_description",menu_desc);
                params.put("menu_image",image);

                params.put("regular_price",menu_regular_price);
                params.put("sale_price",menu_sales_price);
                params.put("available_for_delivery",selectavailablity);

                params.put("max_quantity_available",menu_max_quantity_available);
                params.put("active","1");
                params.put("multiple_option",multiple_option);
                params.put("priced_stat",priced_stat);
                params.put("base_price_status",base_price_status);

                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseData(String response)
    {
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                Toast.makeText(ChefEditMenuItem.this, "Menu Item edited successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,ChefMenuItemActivity.class));
                finish();
            }else {
                Toast.makeText(ChefEditMenuItem.this, "Failed to edit menu Item", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    void getMenuCategoryItem()
    {
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditMenuItem.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.MENU_CATEGORY_LISTING;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseData1(response);
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(ChefEditMenuItem.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefEditMenuItem.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("user_id",userInfo.getUserId());
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseData1(String response)
    {

        try {
            categoryList.clear();
            categoryId.clear();

            categoryList.add("Select Category");
            categoryId.add("-1");

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                JSONArray parentArr = parentJSON.optJSONArray("Records");
                for (int i = 0; i < parentArr.length(); i++) {
                    //  ChefMenuItem object = new ChefMenuItem();
                    JSONObject dataObj = parentArr.optJSONObject(i);
                    //JSONObject dataObj = childObj.optJSONObject("records");
                    categoryId.add(dataObj.optString("menu_catid"));
                    //object.setMenuImage(dataObj.optString("item_image"));
                    categoryList.add(dataObj.optString("cat_name"));
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        adaptercategory.notifyDataSetChanged();
        getMenuItemdataforedit();

    }

    public void takeimage(){
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        // pickIntent.setType("image/* video/*");
        pickIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickIntent, ImageSelect);
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
    }

    // Method which will process the captured image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //verify if the image was gotten successfully
        if (requestCode == ImageSelect && resultCode == Activity.RESULT_OK) {

            try {
                Uri selectedMediaUri = data.getData();

                if ((null == data) || (data.getData() == null)){
                    new ImageCompressionAsyncTask(this).execute(selectedMediaUri.toString(),
                            Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getPackageName()+"/media/images");
                }
                else {
                    new ImageCompressionAsyncTask(this).execute(selectedMediaUri.toString(),
                            Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getPackageName()+"/media/images");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void filesize(Uri filePath){
        File file = new File(filePath.toString());
        long length = file.length();
        length = length/1024;
        System.out.println("File Path : " + file.getPath() + ", File size : " + length +" KB");
    }


    public static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    public String getStringImage(Bitmap bmp){
        if(byteSizeOf(bmp)<=1000000){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            long lengthbmp = imageBytes.length;
            System.out.println(" File size 1 : " + lengthbmp +" KB");

            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }else if(byteSizeOf(bmp)<=9000000){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            byte[] imageBytes = baos.toByteArray();

            long lengthbmp = imageBytes.length;
            System.out.println(" File size 2 : " + lengthbmp +" KB");

            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] imageBytes = baos.toByteArray();

            long lengthbmp = imageBytes.length;
            System.out.println(" File size 3 : " + lengthbmp +" KB");

            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
    }


    public String getStringImage2(Bitmap bmp){

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;

        int actualHeight = bmp.getHeight();
        int actualWidth = bmp.getWidth();

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        int inSampleSize = calculateInSampleSize(bmp, actualWidth, actualHeight);

        actualHeight=actualHeight/inSampleSize;
        actualWidth=actualWidth/inSampleSize;
        bmp=Bitmap.createScaledBitmap(bmp, actualWidth,actualHeight , true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public static int calculateInSampleSize(Bitmap bmp, int reqWidth, int reqHeight) {
        final int height = bmp.getHeight();
        final int width = bmp.getWidth();
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public void imagesize(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;
        System.out.println(" File size : " + lengthbmp +" KB");
    }

    /*get old menu item data...*/
    void getMenuItemdataforedit()
    {
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditMenuItem.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.MENU_ITEM_DATA_FOR_EDIT;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseeditmenuitemdata(response);
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(ChefEditMenuItem.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefEditMenuItem.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("userid",userInfo.getUserId());
                params.put("menu_id",item_id);
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseeditmenuitemdata(String response){

        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                JSONArray parentArr = parentJSON.optJSONArray("Records");
                if(parentArr.length()>0){
                    JSONObject dataObj = parentArr.getJSONObject(0);
                    //JSONObject dataObj = childObj.optJSONObject("records");
                    etn_menu_name.setText(dataObj.optString("menu_name"));
                    etn_menu_desc.setText(dataObj.optString("menu_description"));
                    etn_menu_regular_price.setText(dataObj.optString("regular_price"));
                    etn_menu_sales_price.setText(dataObj.optString("sale_price"));
                    etn_menu_max_quantity_available.setText(dataObj.optString("max_quantity_available"));

                    String available_for_delivery=dataObj.optString("available_for_delivery");
                    if(available_for_delivery.equalsIgnoreCase("1")){
                        spinner_available.setSelection(1);
                    }else if(available_for_delivery.equalsIgnoreCase("0")){
                        spinner_available.setSelection(2);
                    }
                    String cat_name=dataObj.optString("cat_name");
                    System.out.println("$$$$$$$$$$$$ "+cat_name);

                    for(int i=0;i<categoryList.size();i++){
                       // boolean isStringExists = categoryList.contains(cat_name);
                        if(categoryList.get(i).equalsIgnoreCase(cat_name)){
                            spinner_category.setSelection(i);
                        }
                    }

                    multiple_option=dataObj.optString("multiple_option");
                    if(multiple_option.equalsIgnoreCase("Y")){
                        //checkbox_multiple_option.setChecked(true);
                    }else if(multiple_option.equalsIgnoreCase("N")){
                        //checkbox_multiple_option.setChecked(false);
                    }
                    priced_stat=dataObj.optString("priced_stat");
                    if(priced_stat.equalsIgnoreCase("PR")){
                       // radio_Priced.setChecked(true);
                    }else if(priced_stat.equalsIgnoreCase("NP")){
                        //radio_non_price.setChecked(true);
                    }
                    base_price_status=dataObj.optString("base_price_status");
                    if(base_price_status.equalsIgnoreCase("Y")){
                        //checkbox_base_price.setChecked(true);
                    }else if(base_price_status.equalsIgnoreCase("N")){
                        //checkbox_base_price.setChecked(false);
                        //etn_menu_sales_price.setText("");
                        //etn_menu_regular_price.setText("");
                       // etn_menu_regular_price.setVisibility(View.GONE);
                       // etn_menu_sales_price.setVisibility(View.GONE);
                    }

                    String menuimg=dataObj.optString("menu_image");
                    if(!menuimg.equalsIgnoreCase("")){
                        Picasso.with(this)
                                .load("https://chefswithoutlimits.com/"+menuimg)
                                .into(img_take);
                    }

                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

        Context mContext;
        ProgressDialog pd;

        public ImageCompressionAsyncTask(Context context){
            mContext = context;
            pd = new ProgressDialog(ChefEditMenuItem.this);
            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String filePath = SiliCompressor.with(mContext).compress(params[0], new File(params[1]));
            return filePath;


            /*
            Bitmap compressBitMap = null;
            try {
                compressBitMap = SiliCompressor.with(mContext).getCompressBitmap(params[0], true);
                return compressBitMap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return compressBitMap;

            */
        }

        @Override
        protected void onPostExecute(String s) {
            /*
            if (null != s){
                imageView.setImageBitmap(s);
                int compressHieght = s.getHeight();
                int compressWidth = s.getWidth();
                float length = s.getByteCount() / 1024f; // Size in KB;

                String text = String.format("Name: %s\nSize: %fKB\nWidth: %d\nHeight: %d", "ff", length, compressWidth, compressHieght);
                picDescription.setVisibility(View.VISIBLE);
                picDescription.setText(text);
            }
            */

            File imageFile = new File(s);
            compressUri = Uri.fromFile(imageFile);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), compressUri);

                //Getting the Bitmap from Gallery
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                img_take.setImageBitmap(bitmap);

                //imageView.setImageBitmap(bitmap);

                String name = imageFile.getName();
                float length = imageFile.length() / 1024f; // Size in KB
                int compressWidth = bitmap.getWidth();
                int compressHieght = bitmap.getHeight();

                String text = String.format(Locale.US, "Name: %s\nSize: %fKB\nWidth: %d\nHeight: %d", name, length, compressWidth, compressHieght);
                System.out.println("Image Des : "+text);
                //picDescription.setVisibility(View.VISIBLE);
                //picDescription.setText(text);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if (pd != null)
            {
                pd.dismiss();
            }

        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(ChefEditMenuItem.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ChefEditMenuItem.this, "The app was not allowed to read to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefEditMenuItem.this)){
            startActivity(new Intent(ChefEditMenuItem.this,ChefMenuItemActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }
}
