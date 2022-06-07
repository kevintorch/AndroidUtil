package com.torch.androidutil.android;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.torch.androidutil.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ContentUriUtils {

    public static String getPath(Context context, Uri uri) {
        return query(context, uri, MediaStore.MediaColumns.DATA);
    }

    public static String getMimeType(Context context, Uri uri) {
        return query(context, uri, MediaStore.MediaColumns.MIME_TYPE);
    }

    public static String getName(Context context, Uri uri) {
        return query(context, uri, MediaStore.MediaColumns.DISPLAY_NAME);
    }

    public static String getSize(Context context, Uri uri) {
        return query(context, uri, MediaStore.MediaColumns.SIZE);
    }

    public static ParcelFileDescriptor openFileOrThrow(Context context, Uri contentUri)
            throws NullPointerException, IOException {
        ParcelFileDescriptor descriptor = context.getApplicationContext()
                .getContentResolver()
                .openFileDescriptor(contentUri, "r");
        Objects.requireNonNull(descriptor);
        return descriptor;

    }

    public static ParcelFileDescriptor openFile(Context context, Uri contentUri) {
        try {
            return openFileOrThrow(context, contentUri);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String query(Context context, Uri contentUri, String column) {
        return query(context, contentUri, column, null);
    }

    public static String query(Context context, Uri contentUri, String column, String selection) {

        String[] projection = {column};

        try (Cursor returnCursor = context.getApplicationContext()
                .getContentResolver().query(contentUri, projection, selection, null, null)) {
            if (returnCursor != null) {
                int columnIndex = returnCursor.getColumnIndexOrThrow(column);
                returnCursor.moveToFirst();
                return returnCursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri getUriForFile(Context context, File file) {
        // TODO:- Change this tag according to your application.
        String fileProviderTag = ".fileProvider";
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + fileProviderTag, file);
    }
}
