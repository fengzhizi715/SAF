package com.test.saf.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.test.saf.R;
import com.test.saf.app.BaseActivity;
import com.zzhoujay.richtext.RichText;

import cn.salesuite.saf.inject.annotation.InjectView;

/**
 * Created by Tony Shen on 2016/11/22.
 */

public class AnnotationActivity extends BaseActivity {

    @InjectView
    TextView text;

    public final static String ANNO_NAME = "anno_name";

    private String annotationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);

        initViews();
    }

    private void initViews() {
        String content = "```java" +
                "\t@Async\n" +
                "\tprivate void useAsync() {\n" +
                "\t\tLog.e(TAG, \" thread=\" + Thread.currentThread().getId());\n" +
                "\t\tLog.e(TAG, \"ui thread=\" + Looper.getMainLooper().getThread().getId());\n" +
                "\t}\n" +
                "```";
        RichText.fromMarkdown(content).into(text);
    }
}
