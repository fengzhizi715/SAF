package com.safframework.saf.imagecache;

/**
 * Created by Tony Shen on 2016/11/15.
 */

public class DataUtils {

    public static boolean isAvailable(Data data) {

        return data==null?false:data.isAvailable();
    }
}
