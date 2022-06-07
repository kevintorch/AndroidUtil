package com.torch.androidutil;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.RawRes;

public class Sounds {

    public static void play(Context context, @RawRes int resId) {
        MediaPlayer mp = MediaPlayer.create(context, resId);
        mp.setOnCompletionListener(MediaPlayer::release);
        mp.start();
    }
}
