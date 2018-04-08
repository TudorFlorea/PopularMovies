package com.example.tudor.popularmovies.utils;

/**
 * Created by tudor on 08.04.2018.
 */

public class ArrayUtils {

    private static String strSeparator = "__,__";

    public static String arrayToString(String[] array) {
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    public static String[] stringToArray(String string) {
        String[] arr = string.split(strSeparator);
        return arr;
    }

}
