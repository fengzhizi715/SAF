package com.test.saf.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.test.saf.R;
import com.test.saf.app.BaseActivity;
import com.zzhoujay.richtext.RichText;

import cn.salesuite.saf.inject.annotation.InjectExtra;
import cn.salesuite.saf.inject.annotation.InjectView;
import cn.salesuite.saf.utils.Preconditions;

/**
 * Created by Tony Shen on 2016/11/22.
 */

public class AnnotationActivity extends BaseActivity {

    @InjectView
    TextView text;

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
            RichText.fromMarkdown(content).into(text);
        }
    }

    private String getUsage(String annotationName) {

        String result = null;
        if ("@Async".equals(annotationName)) {
            result = "    @Async\n" +
                    "    private void useAsync() {\n" +
                    "        Log.e(TAG, \" thread=\" + Thread.currentThread().getId());\n" +
                    "        Log.e(TAG, \"ui thread=\" + Looper.getMainLooper().getThread().getId" +
                    "());\n" +
                    "    }";
        } else if ("@Cacheable".equals(annotationName)) {
            result = "   @Cacheable(key = \"user\")\n" +
                    "    private User initData() {\n" +
                    "       User user = new User();\n" +
                    "       user.userName = \"tony\";\n" +
                    "       user.password = \"123456\";\n" +
                    "       return user;\n" +
                    "    }";
        }

        return result;
    }
}
