package com.chefswithoutlimits.util;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by db on 12/6/2017.
 */

public class CommonMethodes {

    public static String getStringImage(Bitmap bmp){

        //float maxHeight = 816.0f;
        //float maxWidth = 612.0f;

        float maxHeight = 1024.0f;
        float maxWidth = 1024.0f;

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
}
