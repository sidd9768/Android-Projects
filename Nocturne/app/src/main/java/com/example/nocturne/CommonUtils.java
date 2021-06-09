package com.example.nocturne;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import static android.graphics.BitmapFactory.decodeFile;
import static android.graphics.BitmapFactory.decodeStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonUtils {

    public static String getPath(Context context, Uri uri){
        String path = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index;
        if (cursor != null){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        }
        return path;
    }

    public static File createTempFile(File file){
        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/com/example.nocturne");
        if(!directory.exists() || !directory.isDirectory()){
            directory.mkdirs();
        }
        if(file == null){
            file = new File(directory, "orig.jpg");
        }
        return file;
    }

    public static Bitmap resizePhoto(File imageFile, Context context, Uri uri, ImageView imageView){
        BitmapFactory.Options newOptions = new BitmapFactory.Options();
        try {
            decodeStream(context.getContentResolver().openInputStream(uri), null, newOptions);
            int photoHeight = newOptions.outHeight;
            int photoWidth = newOptions.outWidth;

            newOptions.inSampleSize = Math.min(photoWidth / imageView.getWidth(), photoHeight / imageView.getHeight());
            return compressPhoto(imageFile, decodeStream(context.getContentResolver().openInputStream(uri), null, newOptions));
        }catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
        }
    }

    public static Bitmap resizePhoto(File imageFile, String path, ImageView imageView){
        BitmapFactory.Options options = new BitmapFactory.Options();
        decodeFile(path, options);
        int photoHeight = options.outHeight;
        int photoWidth = options.outWidth;
        options.inSampleSize = Math.min(photoWidth/imageView.getWidth(), photoHeight/imageView.getHeight());
        return compressPhoto(imageFile, decodeFile(path, options));

    }

    private static Bitmap compressPhoto(File photoFile, Bitmap bitmap) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

