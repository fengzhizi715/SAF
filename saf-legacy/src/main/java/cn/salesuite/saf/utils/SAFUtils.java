package cn.salesuite.saf.utils;

import android.os.Environment;
import android.os.StatFs;

/**
 * Created by Tony Shen on 2017/2/16.
 */

public class SAFUtils {

    /**
     * 判断是否存在sd卡
     * @return
     */
    public static boolean hasSdcard() {

        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    /**
     * 获取SD 卡内存
     * @return
     */
    public static long getAvailableSD() {
        if (!hasSdcard())
            return 0;

        String storageDirectory = Environment.getExternalStorageDirectory().toString();

        try {
            StatFs stat = new StatFs(storageDirectory);
            long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat
                    .getBlockSize());
            return avaliableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }
}
