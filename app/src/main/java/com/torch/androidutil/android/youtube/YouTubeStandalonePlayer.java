package com.torch.androidutil.android.youtube;

import android.content.Context;
import android.content.Intent;

public class YouTubeStandalonePlayer {

    public static Intent createVideoIntent(Context context, String videoId) {
        Intent intent = new Intent(context, YouTubeStandalonePlayerActivity.class);
        intent.putExtra("video_id", videoId);
        return intent;
    }
}
