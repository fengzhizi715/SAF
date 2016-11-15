package com.test.saf.script;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.test.saf.domain.HotPatchModel;

import java.io.IOException;

import cn.salesuite.saf.http.rest.RestUtil;
import cn.salesuite.saf.reflect.Reflect;

/**
 * Created by Tony Shen on 16/7/13.
 */
public class ScriptEngine extends WebChromeClient implements Invocable {

    private static Context context;
    private static Handler handler;

//    public static void init(Context context) {
//        ScriptEngine.context = context;
//        handler = new Handler();
//    }

    private WebView webView;
    private boolean isFinish;
    private Object result;

    public ScriptEngine(final Context context) {
        ScriptEngine.context = context;
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                webView = new WebView(context);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(ScriptEngine.this);
                webView.addJavascriptInterface(ScriptEngine.this, "app");
            }
        });
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
            Log.e("ScriptEngine", consoleMessage.message());
            isFinish = true;
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @JavascriptInterface
    public void finish(String result) {
        isFinish = true;
        this.result = result;
    }

    @JavascriptInterface
    public Object execute(String json) {
        isFinish = true;
        try {
            HotPatchModel model = RestUtil.parseAs(HotPatchModel.class,json);
            Class<?> clazz = Class.forName(model.className);
            Object obj = clazz.newInstance();
            result = Reflect.on(obj).set(model.field,model.fieldValue).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Object invokeFunction(String name, Object... args) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:app.finish(");
        stringBuilder.append(String.format("%s('%s'));", name, getParamString(args)));

        isFinish = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(stringBuilder.toString());
            }
        });
//        while (!isFinish) { }

        return result;
    }

    public Object eval(String paramString) throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<script type='text/javascript'>");
        stringBuilder.append("app.finish(eval('");
        stringBuilder.append(convertForEval(paramString));
        stringBuilder.append("'));");
        stringBuilder.append("</script>");

//        Log.i("ScriptEngine", paramString);
//        Log.i("ScriptEngine", stringBuilder.toString());

        isFinish = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                webView.loadData(stringBuilder.toString(), "text/html", "UTF-8");
            }
        });
//        while (!isFinish) { }

        return result;
    }

    public Object executeJava(String js) throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<script type='text/javascript'>");
        stringBuilder.append("app.execute(JSON.stringify(");
        stringBuilder.append(convertForEval(js));
        stringBuilder.append("));");
        stringBuilder.append("</script>");

        Log.i("ScriptEngine", stringBuilder.toString());

        isFinish = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                webView.loadData(stringBuilder.toString(), "text/html", "UTF-8");
            }
        });
//        while (!isFinish) { }

        return result;
    }

    private static String getParamString(Object... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(args[i]);
            if (i < args.length - 1)
                stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    private static String convertForEval(String script) {
//        script = script.replace("\r", "\\r");
//        script = script.replace("</", "<\\/");
//        script = script.replace("\n", "\\n");
        script = script.replace("'", "\"");
//        script = script.replace("\\", "\\\\");
        script = script.replace("\n", "  ");
        script = script.replace("\r", "  ");
        return script;
    }
}
