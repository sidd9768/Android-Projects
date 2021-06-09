package com.example.nocturne;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Bitmap myBitmap;
    private ImageView myImageView;
    private TextView myTextView;
    public static final int WRITE_STORAGE = 100;
    public static final int SELECT_PHOTO = 102;
    public File photo;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = findViewById(R.id.textView);
        myImageView = findViewById(R.id.imageView);
        findViewById(R.id.checkText).setOnClickListener(this);
        findViewById(R.id.select_image).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.checkText:
                if(myBitmap != null){
                    runTextRecognition();
                }
                break;
            case R.id.select_image:
                checkPermission(WRITE_STORAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case WRITE_STORAGE:
                    checkPermission(requestCode);
                    break;
                case SELECT_PHOTO:
                    Uri dataUri = data.getData();
                    String path = CommonUtils.getPath(this, dataUri);
                    if(path == null){
                        myBitmap = CommonUtils.resizePhoto(photo, this, dataUri, myImageView);
                    }else{
                        myBitmap = CommonUtils.resizePhoto(photo, path, myImageView);
                    }
                    if (myBitmap != null){
                        myTextView.setText(null);
                        myImageView.setImageBitmap(myBitmap);
                    }
                    break;
            }
        }
    }


    public void checkPermission(int requestCode) {
        switch (requestCode){
            case WRITE_STORAGE:
                int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED){
                    selectPicture();
                }else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                }
                break;
        }
    }

    public void runTextRecognition() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(myBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processExtractedTexts(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void processExtractedTexts(FirebaseVisionText firebaseVisionText) {
        myTextView.setText(null);
        if(firebaseVisionText.getTextBlocks().size() == 0){
            myTextView.setText("No text");
            return;
        }
        for (FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()){
            myTextView.append(block.getText());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case WRITE_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    selectPicture();
                }else{
                    requestPermissions(this, requestCode, 10);
                }
                break;
        }
    }

    public static void requestPermissions(final Activity activity, final int requestCode, int msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent permissionIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                permissionIntent.setData(Uri.parse("package:"+activity.getPackageName()));
                activity.startActivityForResult(permissionIntent, requestCode);
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    private void selectPicture() {
        photo = CommonUtils.createTempFile(photo);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, SELECT_PHOTO);
    }
}
