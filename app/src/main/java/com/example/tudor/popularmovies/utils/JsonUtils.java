package com.example.tudor.popularmovies.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tudor on 04.03.2018.
 */

public class JsonUtils {

    public static String getStringFromJsonObject (JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

    public static int getIntFromJsonObject (JSONObject jsonObject, String key) {
        try {
            return jsonObject.getInt(key);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return 0;
        }
    }

    public static boolean getBooleanFromJsonObject (JSONObject jsonObject, String key) {
        try {
            return jsonObject.getBoolean(key);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return false;
        }
    }

    public static JSONObject jsonObjectFromString(String rawJson) {
        try {
            return new JSONObject(rawJson);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

}
