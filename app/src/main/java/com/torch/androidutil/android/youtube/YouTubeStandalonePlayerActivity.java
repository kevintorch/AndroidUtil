package com.torch.androidutil.android.youtube;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;
import com.zimong.ssms.R;
import com.zimong.ssms.base.BaseActivity;
import com.zimong.ssms.image.ImageUtils;


public class YouTubeStandalonePlayerActivity extends BaseActivity {

    private final float seekSecond = 10f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_standalone_player);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        String currentVideoId = getIntent().getStringExtra("video_id");
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                addFastForwardActions(initializedYouTubePlayer, youTubePlayerView);
                initializedYouTubePlayer.loadVideo(currentVideoId, 0);
            }
        });
        youTubePlayerView.exitFullScreen();
        View decorView = getWindow().getDecorView();
        hideSystemUi(decorView);
    }

    private void enableLiveUiIfNeeded(YouTubePlayerView youTubePlayerView) {
//        youTubePlayerView.getPlayerUiController().enableLiveVideoUi()
    }

    private void addFastForwardActions(YouTubePlayer player, YouTubePlayerView youTubePlayerView) {
        PlayerUiController uiController = youTubePlayerView.getPlayerUiController();
        Drawable fastForward = ImageUtils.getVectorDrawable(this, R.drawable.ic_fast_forward);
        Drawable rewind = ImageUtils.getVectorDrawable(this, R.drawable.ic_fast_rewind);
        YouTubePlayerTracker tracker = new YouTubePlayerTracker();
        player.addListener(tracker);
        uiController.setCustomAction1(rewind, v -> {
            float current = tracker.getCurrentSecond();
            float seek = seekSecond;
            if (seek > current) {
                seek = current;
            }
            player.seekTo(current - seek);
        });
        uiController.setCustomAction2(fastForward, v -> {
            float current = tracker.getCurrentSecond();
            float seek = seekSecond;
            float secondsLeft = tracker.getVideoDuration() - current;
            if (seek > secondsLeft) {
                seek = secondsLeft;
            }
            player.seekTo(current + seek);
        });
        uiController.showCustomAction1(true);
        uiController.showCustomAction2(true);
        uiController.showYouTubeButton(false);
    }

    private void hideSystemUi(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void showSystemUi(View mDecorView) {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

}

