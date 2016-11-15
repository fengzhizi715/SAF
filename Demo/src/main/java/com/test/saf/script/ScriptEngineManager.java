package com.test.saf.script;

import android.content.Context;

/**
 * Created by Tony Shen on 16/7/13.
 */
public class ScriptEngineManager {

    private static ScriptEngine engine;

    public ScriptEngine getEngineByName(String name, Context context) {

        if (engine == null) {
            engine = new ScriptEngine(context);
        }
        return engine;
    }
}
