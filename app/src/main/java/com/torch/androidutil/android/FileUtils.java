package com.torch.androidutil.android;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public final class FileUtils {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static Uri getUriFromType(String type) {
        if (type.equals(Environment.DIRECTORY_DOWNLOADS)) {
            return MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        } else if (type.equals(Environment.DIRECTORY_PICTURES)) {
            return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (type.equals(Environment.DIRECTORY_MUSIC) ||
                type.equals(Environment.DIRECTORY_RINGTONES)) {
            return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else if (type.equals(Environment.DIRECTORY_MOVIES)) {
            return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            return MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        }
    }

    private static final int DEFAULT_BUFFER_SIZE = 2104;

    public static long copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return count;
    }

    private static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static String appendingBeforeExtension(String fileName,
                                                  String appending,
                                                  String mimeType) {

        fileName = correctExtensionFromMimeType(fileName, mimeType);

        StringBuilder fileNameBuilder = new StringBuilder(fileName);
        int dotIndex = fileNameBuilder.lastIndexOf(".");
        if (dotIndex > -1) {
            fileNameBuilder.insert(dotIndex, appending);
        } else {
            fileNameBuilder.insert(fileName.length(), appending);
        }

        return fileNameBuilder.toString();
    }

    private static String correctExtensionFromMimeType(String fileName, String mimeType) {
        boolean isValidExt = hasValidExtension(fileName);

        if (!isValidExt) {
            String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            if (Objects.nonNull(ext)) {
                fileName += "." + ext;
            }
        }
        return fileName;
    }

    public static boolean hasValidExtension(String fileName) {
        String fileExt = extractExtension(fileName);
        return validateExtension(fileExt);
    }

    public static boolean validateExtension(String extension) {
        if (Objects.isNull(extension)) return false;
        return MimeTypeMap.getSingleton().hasExtension(extension);
    }

    public static String extractExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        return filename.substring(dotIndex + 1);
    }

    public static String appendPathSegmentsToPath(String path, String... segments) {
        StringBuilder stringBuilder = new StringBuilder(path);
        for (String seg : segments) {
            appendPathSegment(stringBuilder, seg);
        }
        return stringBuilder.toString();
    }

    public static void appendPathSegment(StringBuilder path, String segment) {
        if (segment != null && !TextUtils.isEmpty(segment.trim())) {
            path.append(File.separator).append(segment.trim());
        }
    }

    public static String appendPathSegment(String path, String segment) {
        if (segment != null && !TextUtils.isEmpty(segment.trim())) {
            path += File.separator + segment.trim();
        }
        return path;
    }

    public static @Nullable
    Uri createEmptyFileInDirOrThow(Context context,
                                   String dirType,
                                   String fullFileName,
                                   String relativePath) throws IOException {
        Uri newFileUri;
        String dirPath = appendPathSegment(dirType, relativePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String ext = extractExtension(fullFileName);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
            newFileUri = MediaStoreUtils.insertFileInUri(context,
                                                         getUriFromType(dirType),
                                                         fullFileName,
                                                         mimeType,
                                                         dirPath);
        } else {
            File file =
                    new File(Environment.getExternalStoragePublicDirectory(dirPath), fullFileName);
            File parentDir = file.getParentFile();
            boolean created = false;
            if (parentDir != null) {
                created = parentDir.exists();
                if (!created) {
                    created = parentDir.mkdirs();
                }
            }
            if (created) {
                created = file.createNewFile();
            }
            if (!created) return null;
            newFileUri = ContentUriUtils.getUriForFile(context, file);
        }
        return newFileUri;
    }

    public static @Nullable
    Uri createEmptyFileInDir(Context context,
                             String dirType,
                             String fullFileName,
                             String relativePath) {
        Uri newFileUri = null;
        try {
            newFileUri = createEmptyFileInDirOrThow(context, dirType, fullFileName, relativePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFileUri;
    }

    public static @Nullable
    File createTempFile(Context context, String name, String extension) {
        if (extension == null) throw new IllegalArgumentException("file extension cannot be Null.");
        File image = null;
        try {
            image = File.createTempFile(
                    name,  /* prefix */
                    "." + extension,         /* suffix */
                    context.getCacheDir()      /* directory */
                                       );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static boolean deleteFileInDir(Context context,
                                          String dirType,
                                          String fileName,
                                          String relativePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return MediaStoreUtils.deleteFileInUri(context, fileName, getUriFromType(dirType)) > 0;
        } else {
            String relativeFilePath = FileUtils.appendPathSegmentsToPath(dirType, relativePath);
            return new File(Environment.getExternalStoragePublicDirectory(relativeFilePath),
                            fileName).delete();
        }
    }

    @NonNull
    public static String getMimeType(File file) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
        String mimeType = null;
        if (!extension.isEmpty()) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType == null ? "application/octet-stream" : mimeType;
    }
}
