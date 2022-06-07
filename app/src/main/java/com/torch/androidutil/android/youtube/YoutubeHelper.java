package com.torch.androidutil.android.youtube;

import android.util.Log;

import androidx.annotation.NonNull;

import com.torch.androidutil.android.GsonUtil;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

public class YoutubeHelper {

    public static String extractVideoIdFromYoutubeUrl(String url) {
        String regex =
                "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)" +
                        "\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        String regex1 =
                "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:vi|e(?:mbed)?)" +
                        "\\/|\\S*?[?&]vi=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        String videoId = getVideoIdFromYoutubeUrl(regex, url);
        if (videoId == null) {
            videoId = getVideoIdFromYoutubeUrl(regex1, url);
        }
        return videoId;
    }

    private static String getVideoIdFromYoutubeUrl(String regex, String url) {
        String videoId = null;
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }

    public static String getYoutubeVideoThumbnailUrl(String videoId) {
        return String.format("https://img.youtube.com/vi/%s/mqdefault.jpg", videoId);
    }

    public static String videoTitle(String videoId) {
        Map<String, String> videoMetadata = videoMetadata(videoId);
        if (videoMetadata != null) {
            return videoMetadata.get("title");
        }
        return null;
    }

    public static Map<String, String> videoMetadata(String videoId) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = String.format(
                    "https://www.youtube.com/oembed?url=https://youtu.be/%s&format=json",
                    videoId);
            String response = get(url, client);
            return GsonUtil.toMap(response);
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            Log.e("YoutubeHelper", message == null ? "Error getting metadata" : message);
            return null;
        }
    }

    public static String makeYouTubeUrl(@NonNull String videoId) {
        return String.format("https://www.youtube.com/watch?v=%s", videoId);
    }

    private static String get(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
