package com.boardinglabs.mireta.standalone.modul.old.dialogactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.util.DateHelper;

import java.io.File;
import java.io.IOException;

import rx.functions.Action1;

/**
 * Created by Dhimas on 1/1/18.
 */

public class DialogAttachPicture extends Activity {

    private static final int REQUEST_PICK_IMAGE = 11;
    private static final int REQUEST_IMAGE_CAPTURE = 12;
    public static final String RESULT_LOAD_IMAGE = "pictPath";
    public static final int DIALOG_ATTACH_PICTURE = 256;

    private ImageView mBtnClose;
    private LinearLayout mCooiceOpenGalery;
    private LinearLayout mCooiceOpenCamera;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_attach_picture);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        initComponent();
        initAction();

    }

    private void initComponent() {
        mBtnClose = (ImageView) findViewById(R.id.dlgAttchPicture_btnClose);
        mCooiceOpenCamera = (LinearLayout) findViewById(R.id.dlgAttchPicture_chooseCamera);
        mCooiceOpenGalery = (LinearLayout) findViewById(R.id.dlgAttchPicture_chooseGalery);
    }

    private void initAction() {
        RxView.clicks(mBtnClose).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(mCooiceOpenCamera).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                takePhoto();
            }
        });
        RxView.clicks(mCooiceOpenGalery).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                browseFile();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to access your device", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
            }

            if (photoFile != null) {
                Uri outputFileUri = Uri.fromFile(photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    public void browseFile() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_PICK_IMAGE);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "aku_" + DateHelper.getUnixTime();
        File storageDir = createDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = "file:" + image.getAbsolutePath();
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createDirectory() {
        File storeDir = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera");
        if (!storeDir.exists()) {
            storeDir.mkdir();
        }

        return storeDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE && null != data) {
                String picturePath = "";
                String[] fileName = data.getDataString().split("://", 2);

                if (!fileName[0].equals("file")) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                    }

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                } else {
                    picturePath = fileName[1];
                }


                //            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                //            Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_LOAD_IMAGE, picturePath);

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_LOAD_IMAGE, currentPhotoPath);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }

    }
}
