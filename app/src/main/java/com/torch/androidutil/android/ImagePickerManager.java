package com.torch.androidutil.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.torch.androidutil.CollectionsUtil;
import com.torch.androidutil.android.activity_result_contracts.PickMultiplePicture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class ImagePickerManager extends Observable {

    private static final String[] CAMERA_OPTIONS = {"Take Photo", "Choose Photo"};

    public static class OnImagesPickedObserver implements Observer {
        private final ImagePickerListener listener;

        public OnImagesPickedObserver(ImagePickerListener listener) {
            this.listener = listener;
        }

        @Override
        public void update(Observable o, Object arg) {
            if (o instanceof ImagePickerManager) {
                listener.update(((ImagePickerManager) o).pictures);
            }
        }
    }

    public interface ImagePickerListener {
        void update(List<Uri> images);
    }

    private final AppCompatActivity appCompatActivity;

    private final ActivityResultLauncher<Void> pickMultiplePicker;
    private final ActivityResultLauncher<Uri> takePicture;
    private final ActivityResultLauncher<String> permisionLauncher;
    private List<Uri> pictures = new ArrayList<>();
    private Uri cameraPhotoUri;

    public ImagePickerManager(AppCompatActivity appCompatActivity) {
        this(appCompatActivity, false);
    }

    public ImagePickerManager(AppCompatActivity appCompatActivity, boolean pickMultiple) {
        this.appCompatActivity = appCompatActivity;

        pickMultiplePicker = appCompatActivity.registerForActivityResult(
                new PickMultiplePicture(pickMultiple),
                this::onReceivePictures);

        permisionLauncher = appCompatActivity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                permissionGranted -> {
                });

        takePicture = appCompatActivity.registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                pictureTaken -> {
                    if (pictureTaken && cameraPhotoUri != null) {
                        onReceivePictures(Collections.singletonList(cameraPhotoUri));
                    }
                });
    }

    private void onReceivePictures(List<Uri> pictures) {
        if (CollectionsUtil.isEmpty(pictures)) return;
        this.pictures = pictures;
        setChanged();
        notifyObservers(pictures);
    }

    public void start() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(appCompatActivity);
        builder.setTitle("Change photo");
        builder.setItems(CAMERA_OPTIONS, (dialogInterface, index) -> {
            dialogInterface.dismiss();
            if (index == 0) {
                pickFromCamera();
            } else if (index == 1) {
                pickFromGallery();
            }
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }

    public void pickFromGallery() {
        pickMultiplePicker.launch(null);
    }

    public void pickFromCamera() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            permisionLauncher.launch(Manifest.permission.CAMERA);
            return;
        }
        Uri pictureUri = getPictureUri();
        if (pictureUri != null)
            takePicture.launch(pictureUri, ActivityOptionsCompat.makeBasic());
    }

    private boolean isPermissionGranted(String permission) {
        int result = ContextCompat.checkSelfPermission(appCompatActivity, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = appCompatActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private Uri getPictureUri() {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ignore) {
        }
        if (photoFile != null) {
            Uri photoUri = ContentUriUtils.getUriForFile(appCompatActivity, photoFile);
            this.cameraPhotoUri = photoUri;
            return photoUri;
        }
        return null;
    }
}
