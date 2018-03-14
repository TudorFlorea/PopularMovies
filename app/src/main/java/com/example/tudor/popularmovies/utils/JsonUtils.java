package com.example.tudor.popularmovies.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tudor on 04.03.2018.
 */

public class JsonUtils {

    /**
     * function that returns a json object from a json formatted string or null.
     * @param rawJson
     * @return JSONObject or null
     */

    public static JSONObject jsonObjectFromString(String rawJson) {
        try {
            return new JSONObject(rawJson);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

}
