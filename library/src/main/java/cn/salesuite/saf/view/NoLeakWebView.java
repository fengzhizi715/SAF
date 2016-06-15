package cn.salesuite.saf.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

/**
 * Created by Tony Shen on 16/6/15.
 */
public class NoLeakWebView extends WebView {

    private static final String TAG = "NoLeakWebView";

    public class NoLeakWebViewClient extends WebViewClient {

        protected WeakReference mActivity;

        protected NoLeakWebViewClient(Activity activity) {
            mActivity = new WeakReference(activity);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {

            if (!NoLeakWebView.this.shouldJumpToBrowser(str)) {
                return false;
            }
            Activity activity = (Activity) mActivity.get();
            if (activity == null) {
                return false;
            }

            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } catch (Throwable e) {
                Log.e(TAG, "fail to go to url in NoLeakWebViewClient.shouldOverrideUrlLoading", e);
            }
            return true;
        }
    }

    public NoLeakWebView(Context context) {
        super(context.getApplicationContext());
        init(context);
    }

    public NoLeakWebView(Context context, AttributeSet attributeSet) {
        super(context.getApplicationContext(), attributeSet);
        init(context);
    }

    public NoLeakWebView(Context context, AttributeSet attributeSet, int i) {
        super(context.getApplicationContext(), attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        setWebViewClient(new NoLeakWebViewClient((Activity)context));
        getSettings().setSavePassword(false);
    }

    protected boolean shouldJumpToBrowser(String str) {

        return false;
    }

    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        }
    }
}
