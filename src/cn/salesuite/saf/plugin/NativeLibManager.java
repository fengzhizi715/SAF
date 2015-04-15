/**
 * 
 */
package cn.salesuite.saf.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import cn.salesuite.saf.utils.IOUtils;
import android.util.Log;

/**
 * @author Tony Shen
 *
 */
public class NativeLibManager {

    
    public static final String TAG = NativeLibManager.class.getSimpleName();
    public static final String ARCH_ARMEABI = "armeabi";
    public static final String ARCH_ARMEABI_V7A = "armeabi-v7a";
    public static String ARCH = System.getProperty("os.arch");
    
    public static void unPackSOFromApk(String apkPath,String toPath){
    	
        Log.i(TAG,"CPU is "+ ARCH);

        try {
            ZipFile apk = new ZipFile(new File(apkPath));
            boolean hasSoLib = extractSoLibFile(apk,new File(toPath));
            if(hasSoLib){
                Log.i(TAG,"The plugin is contains .so files.");
            }else{
                Log.i(TAG,"The plugin isn't contain any .so files.");
            }


        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }

    }
    
    private static boolean extractSoLibFile(ZipFile zip, File to)
            throws ZipException, IOException {

        Map<String,List<ZipEntry>> archLibEntries = new HashMap<String, List<ZipEntry>>();
        for (Enumeration<? extends ZipEntry> e = zip.entries(); e
                .hasMoreElements();) {
            ZipEntry entry = e.nextElement();
            String name = entry.getName();

            if (name.startsWith("/")) {
                name = name.substring(1);
            }
            if (name.startsWith("lib/")) {
                if(entry.isDirectory()){
                    continue;
                }
                int sp = name.indexOf('/', 4);
                String en2add;
                if (sp > 0) {
                    String osArch = name.substring(4, sp);
                    en2add=osArch.toLowerCase();
                } else {
                    en2add=ARCH_ARMEABI;
                }
                List<ZipEntry> zipEntries = archLibEntries.get(en2add);
                if (zipEntries == null) {
                    zipEntries = new LinkedList<ZipEntry>();
                    archLibEntries.put(en2add, zipEntries);
                }
                zipEntries.add(entry);
            }
        }

        List<ZipEntry> libEntries = archLibEntries.get(ARCH.toLowerCase());
        if (libEntries == null) {
            libEntries = archLibEntries.get(ARCH_ARMEABI);
            if(libEntries == null){
                libEntries = archLibEntries.get(ARCH_ARMEABI_V7A);
            }
        }
        boolean hasSoLib = false;//是否包含so
        if (libEntries != null) {
            hasSoLib = true;
            if (!to.exists()) {
                to.mkdirs();
            }
            for (ZipEntry libEntry : libEntries) {
                String name = libEntry.getName();
                String pureName = name.substring(name.lastIndexOf('/') + 1);
                File target = new File(to, pureName);
                IOUtils.writeToFile(zip.getInputStream(libEntry), target);
            }
        }

        return hasSoLib;
    }
}
