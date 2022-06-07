package com.torch.androidutil.android;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by zimong on 10/23/2017.
 */

public class FileFinder {

    public static final int PK_NULL = -4;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef(value = {KEY_FILE_NAME, KEY_FILE_MIME_TYPE, KEY_FILE_PK, KEY_FILE_FOLDER})
    public @interface FileProperty {
    }

    /**
     * root path of directory where this app saves the file.
     * <p>
     * NOTE:- must be retrieve by {@link #getAppSpecificPath(Context)}
     */
    private static String APP_DIRECTORY_PATH = "";  // do not make this public.

    public static final String KEY_FILE_NAME = "file_name";
    public static final String KEY_FILE_MIME_TYPE = "file_mime_type";
    public static final String KEY_FILE_PK = "file_pk";
    public static final String KEY_FILE_FOLDER = "file_folder";

    /**
     * properties of file which we need to find.
     */
    private HashMap<String, Object> targetFileProperties;

    /**
     * encoded file name with file pk or any other key.
     */
    private static String mFullFilename;
    private final Context context;

    private boolean isPrepared;

    public static FileFinder from(Context context) {
        return new FileFinder(context);
    }

    public FileFinder(Context context) {
        this.context = context;
    }

    public FileFinder set(@FileProperty String propertyName, Object o) {
        if (targetFileProperties == null) {
            targetFileProperties = new HashMap<>();
        }
        targetFileProperties.put(propertyName, o);
        return this;
    }

    public boolean equals(FileFinder obj) {
        return this.targetFileProperties != null &&
                obj.targetFileProperties != null &&
                this.targetFileProperties.equals(obj.targetFileProperties);
    }

    /**
     * Finds the file with specified file name, file pk, mime type and destination folder.
     *
     * @deprecated use {@link #find()} from now on.
     * <p>
     * Note:- this method should be used only to support older implementation of FileFinder.
     */
    @Deprecated
    public static boolean findFile(Context context, @NonNull String fileName, @NonNull String mimeType, long filePk, @Nullable String folder) {
        return FileFinder.from(context)
                .set(KEY_FILE_NAME, fileName)
                .set(KEY_FILE_MIME_TYPE, mimeType)
                .set(KEY_FILE_PK, filePk)
                .set(KEY_FILE_FOLDER, folder).find();
    }

    private void buildFullFileName() {
        Long pk = getFilePropertyOfType(KEY_FILE_PK, Long.class);
        String fileName = getFilePropertyOfType(KEY_FILE_NAME, String.class);
        String mimType = getFilePropertyOfType(KEY_FILE_MIME_TYPE, String.class);

        String pkPart = (pk != null && pk.compareTo((long) PK_NULL) == 0) ? "" : ("_" + pk);

        if (!TextUtils.isEmpty(fileName)) {
            mFullFilename = FileUtils.appendingBeforeExtension(fileName, pkPart, mimType);
        }
    }

    private void checkPrecondition() {
        String fileName = getFilePropertyOfType(KEY_FILE_NAME, String.class);
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("file name cannot be empty or null");
        }
    }

    private <T> T getFilePropertyOfType(@FileProperty String prop, Class<T> tClass) {
        if (targetFileProperties.containsKey(prop)) {
            return tClass.cast(targetFileProperties.get(prop));
        }
        return null;
    }

    public void prepare() {
        checkPrecondition();
        buildFullFileName();
        isPrepared = true;
    }

    public boolean find() {
        if (!isPrepared)
            prepare();

        Uri file = getFile();
        try (ParcelFileDescriptor fileDescriptor = ContentUriUtils.openFileOrThrow(context, file)) {
            return fileDescriptor.getFileDescriptor().valid();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Uri getFile() {
        if (!isPrepared)
            prepare();

        Objects.requireNonNull(mFullFilename);

        // we get the file by Media Store Api starting from Android 10
        // other we get from external storage directory
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            fileUri = MediaStoreUtils.getUriForFile(context, mFullFilename);
        } else {
            String fileFolder = getFilePropertyOfType(KEY_FILE_FOLDER, String.class);
            String appSpecificPath = getAppSpecificPath(context);
            String fullFilePath = FileUtils.appendPathSegmentsToPath(appSpecificPath, fileFolder, mFullFilename);
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS +
                    File.separator + fullFilePath);
            fileUri = ContentUriUtils.getUriForFile(context, file);
        }
        return fileUri;
    }

    public static String getAppSpecificPath(Context context) {
        if (APP_DIRECTORY_PATH.isEmpty()) {
            String schoolId = "school_id";
            APP_DIRECTORY_PATH = "ssms_" + schoolId;
        }
        return APP_DIRECTORY_PATH;
    }
}
