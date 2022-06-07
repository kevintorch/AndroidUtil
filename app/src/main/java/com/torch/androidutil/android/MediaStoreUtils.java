package com.torch.androidutil.android;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public final class MediaStoreUtils {


    public static Uri getUriForFile(Context context, String fileName) {
        return queryFileNameInDownloads(context, fileName);
    }

    private static Uri queryFileNameInDownloads(Context context, String fileName) {
        return queryFileNameInUri(context, fileName, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
    }

    private static Uri queryFileNameInUri(Context context, String fileName, Uri queryUri) {
        Uri fileUri = null;

        String[] projection = new String[]{
                MediaStore.Downloads._ID,
                MediaStore.Downloads.DISPLAY_NAME
        };

        String selection = MediaStore.Downloads.DISPLAY_NAME + " = ?";

        String[] selectionArgs = new String[]{fileName};

        try (Cursor cursor = context.getContentResolver()
                .query(queryUri, projection, selection, selectionArgs, null)) {

            if (cursor == null) return null;

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);

                if (name.equals(fileName)) {
                    fileUri = ContentUris.withAppendedId(queryUri, id);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUri;
    }


    public static Uri insertFileInUri(Context context, Uri uri, String fileName, String mimeType, String relativePath) {
        ContentResolver resolver = context.getApplicationContext()
                .getContentResolver();

        ContentValues contentValues = new ContentValues();
        if (mimeType != null)
            contentValues.put(MediaStore.Downloads.MIME_TYPE,
                    mimeType);
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Downloads.RELATIVE_PATH, relativePath);
        return resolver.insert(uri, contentValues);
    }

    public static int deleteFileInUri(Context context, String fileName, Uri uri) {
        String selection = MediaStore.Downloads.DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[]{fileName};

        return context.getApplicationContext().getContentResolver()
                .delete(uri, selection, selectionArgs);
    }


}
