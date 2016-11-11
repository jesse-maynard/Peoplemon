package com.example.jessemaynard.peoplemon.Components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by jessemaynard on 11/11/16.
 */

public class Utils {

    static String image = "";

    // Utility for resizing images.
    public static Bitmap resizeImage(Bitmap image) {
        image = Bitmap.createScaledBitmap(image, 100, 100, false);
        return image;
    }

    // Utility for decoding images.
    public static Bitmap decodeImage(String encodedString) {
        byte[] decodedString;
        Bitmap decodedByte;
        if (encodedString != null) {
            if (encodedString.contains(",")) {
                String[] encImage = encodedString.split(",");
                decodedString = Base64.decode(encImage[1], Base64.DEFAULT);
            } else {
                if (encodedString.length() > 200) {
                    decodedString = Base64.decode(encodedString, Base64.DEFAULT);
                } else {
                    decodedString = null;
                }
            }
            if (decodedString != null) {
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                decodedByte = resizeImage(decodedByte);
                return decodedByte;
            }
        }
        return null;
    }

    // Utility for encoding images into base64.
    public static String encodeImage(Bitmap bitmap) {
        bitmap = resizeImage(bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
