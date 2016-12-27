/**
 * 
 */
package cn.salesuite.saf.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Tony Shen
 *
 * 
 */
public class BasePrefs {
	
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    protected BasePrefs(Context context, String prefsName) {
        prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return prefs.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return prefs.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public Object getObject(String key) {
		try {
			String stringBase64 = prefs.getString(key, "");
			if (TextUtils.isEmpty(stringBase64))
				return null;
			
			byte[] base64Bytes = Base64.decodeBase64(stringBase64.getBytes());
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    public void putBoolean(String key, boolean v) {
        ensureEditorAvailability();
        editor.putBoolean(key, v);
        save();
    }

    public void putFloat(String key, float v) {
        ensureEditorAvailability();
        editor.putFloat(key, v);
        save();
    }

    public void putInt(String key, int v) {
        ensureEditorAvailability();
        editor.putInt(key, v);
        save();
    }

    public void putLong(String key, long v) {
        ensureEditorAvailability();
        editor.putLong(key, v);
        save();
    }

    public void putString(String key, String v) {
        ensureEditorAvailability();
        editor.putString(key, v);
        save();
    }

    public void putObject(String key, Object obj) {
        ensureEditorAvailability();
        try {  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            ObjectOutputStream oos = new ObjectOutputStream(baos);  
            oos.writeObject(obj);  
  
            String stringBase64 = new String(Base64.encodeBase64(baos.toByteArray()));  
            editor.putString(key, stringBase64);
            save();
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }

    public void save() {
        if (editor != null) {
            editor.apply();
        }
    }

    private void ensureEditorAvailability() {
        if (editor == null) {
            editor = prefs.edit();
        }
    }
    
    public void remove(String key) {
    	ensureEditorAvailability();
    	editor.remove(key);
    	save();
    }
    
    public void clear() {
    	ensureEditorAvailability();
    	editor.clear();
    	save();
    }
}
