package com.akash.applications.socgen.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by abdullahghani on 31/07/17.
 */

public class ImageEncoder {
    public static String encode(String str)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(str);
        if(bitmap == null)
            return "null";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
