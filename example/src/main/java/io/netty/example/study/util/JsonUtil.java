package io.netty.example.study.util;

import com.google.gson.Gson;

public final class JsonUtil {

    private static final Gson GSON = new Gson();

    private JsonUtil() {
        //no instance
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return GSON.fromJson(jsonStr, clazz);
    }

    public static <T> T fromJson(byte[] jsonBytes, Class<T> clazz) {
        return GSON.fromJson(new String(jsonBytes), clazz);
    }


    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static byte[] toJsonBytes(Object object) {
        return GSON.toJson(object).getBytes();
    }

}
