package com.torch.androidutil.android;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;

import java.io.InputStream;

public class BitmapUtils {

    public static Bitmap decodeSampledBitmapFromFile(String path, int targetW, int targetH) {
        return decodeSampledBitmapFromFile(path, targetW, targetH, true);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int targetW,
                                                     int targetH,
                                                     boolean sampleAsCloseToTargetSize) {

//        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);

        int inSampleSize;

        if (sampleAsCloseToTargetSize)
            inSampleSize = calculateExactInSampleSize(bmOptions, targetW, targetH);
        else inSampleSize = calculateInSampleSize(bmOptions, targetW, targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = inSampleSize;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap decodeSampledBitmapFromFileDescriptor(ParcelFileDescriptor descriptor,
                                                               int targetW,
                                                               int targetH) {
        return decodeSampledBitmapFromFileDescriptor(descriptor, targetW, targetH, true);
    }

    public static Bitmap decodeSampledBitmapFromFileDescriptor(ParcelFileDescriptor descriptor,
                                                               int targetW,
                                                               int targetH,
                                                               boolean sampleAsCloseToTargetSize) {

//        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor(), null, bmOptions);

        int inSampleSize;

        if (sampleAsCloseToTargetSize)
            inSampleSize = calculateExactInSampleSize(bmOptions, targetW, targetH);
        else inSampleSize = calculateInSampleSize(bmOptions, targetW, targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = inSampleSize;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor());
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream stream,
                                                       int reqWidth,
                                                       int reqHeight,
                                                       Bitmap.Config config) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        if (config != null) {
            options.inPreferredConfig = config;
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(stream, null, options);
    }

    public static int calculateExactInSampleSize(BitmapFactory.Options options,
                                                 int reqWidth,
                                                 int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            inSampleSize = Math.min(height / reqHeight, width / reqWidth);
        }

        return inSampleSize;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
