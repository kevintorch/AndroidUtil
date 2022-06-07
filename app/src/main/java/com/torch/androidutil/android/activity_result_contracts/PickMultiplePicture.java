package com.torch.androidutil.android.activity_result_contracts;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


public class PickMultiplePicture extends ActivityResultContract<Void, List<Uri>> {
    private final boolean pickMultiple;

    public PickMultiplePicture(boolean pickMultiple) {
        this.pickMultiple = pickMultiple;
    }

    public PickMultiplePicture() {
        this(true);
    }

    @TargetApi(19)
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        return new Intent(Intent.ACTION_PICK)
                .setType(MediaStore.Images.Media.CONTENT_TYPE)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, pickMultiple);
    }

    @Override
    public List<Uri> parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode != Activity.RESULT_OK || intent == null) return Collections.emptyList();
        return getClipDataUris(intent);
    }

    @NonNull
    public static List<Uri> getClipDataUris(@NonNull Intent intent) {
        // Use a LinkedHashSet to maintain any ordering that may be
        // present in the ClipData
        LinkedHashSet<Uri> resultSet = new LinkedHashSet<>();
        if (intent.getData() != null) {
            resultSet.add(intent.getData());
        }
        ClipData clipData = intent.getClipData();
        if (clipData == null && resultSet.isEmpty()) {
            return Collections.emptyList();
        } else if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                Uri uri = clipData.getItemAt(i).getUri();
                if (uri != null) {
                    resultSet.add(uri);
                }
            }
        }
        return new ArrayList<>(resultSet);
    }
}
