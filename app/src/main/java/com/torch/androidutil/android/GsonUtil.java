package com.torch.androidutil.android;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GsonUtil {
    public static Map<String, String> toMap(String jsonString) {
        try {
            JsonObject jsonObject = convert(jsonString, JsonObject.class);
            return jsonObject.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().getAsString()));
        } catch (Exception ignored) {
        }
        return new HashMap<>();
    }
    public static <T> T convert(String v, Class<T> aClass) {
        Gson gson = new Gson();
        return gson.fromJson(v, aClass);
    }
}
