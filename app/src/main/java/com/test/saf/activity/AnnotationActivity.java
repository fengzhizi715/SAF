package com.test.saf.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.test.saf.R;
import com.test.saf.app.BaseActivity;

import cn.salesuite.router.annotations.RouterRule;
import cn.salesuite.saf.inject.annotation.InjectExtra;
import cn.salesuite.saf.inject.annotation.InjectView;
import cn.salesuite.saf.utils.Preconditions;
import us.feras.mdv.MarkdownView;

/**
 * Created by Tony Shen on 2016/11/22.
 */
@RouterRule(url={"annotationName/:anno_name"})
public class AnnotationActivity extends BaseActivity {

    @InjectView
    MarkdownView text;

    @InjectView
    TextView title;

    @InjectExtra(key = ANNO_NAME)
    String annotationName;

    public final static String ANNO_NAME = "anno_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);

        initViews();
    }

    private void initViews() {

        if (Preconditions.isNotBlank(annotationName)) {
            title.setText(annotationName+"使用方法");
        }

        String content = getUsage(annotationName);

        if (Preconditions.isNotBlank(content)) {
            text.loadMarkdown(content);
        }
    }

    private String getUsage(String annotationName) {

        String result = null;
        if ("@Async".equals(annotationName)) {
            result = "<pre><code>\n" +
                    "\t@Async\n" +
                    "\tprivate void useAsync() {\n" +
                    "\t\tLog.e(TAG, \" thread=\" + Thread.currentThread().getId());\n" +
                    "\t\tLog.e(TAG, \"ui thread=\" + Looper.getMainLooper().getThread().getId());" +
                    "\n" +
                    "\t}\n" +
                    "</pre></code>";
        } else if ("@Cacheable".equals(annotationName)) {
            result = "<pre><code>\n" +
                    "\t@Cacheable(key = \"user\")\n" +
                    "\tprivate User initData() {\n" +
                    "\t\tUser user = new User();\n" +
                    "\t\tuser.userName = \"tony\";\n" +
                    "\t\tuser.password = \"123456\";\n" +
                    "\t\treturn user;\n" +
                    "\t}\n" +
                    "</pre></code>";
        }

        return result;
    }
}
