package com.test.saf.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.permission.RxPermissions;
import com.test.saf.R;
import com.test.saf.app.BaseFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Tony Shen on 2017/6/23.
 */

public class PermissionFragment extends BaseFragment {

    @InjectView(R.id.text1)
    TextView text1;

    @InjectView(R.id.text2)
    TextView text2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_permission, container, false);
        Injector.injectInto(this, v);

        initViews();

        return v;
    }

    private void initViews() {

        RxView.clicks(text1).throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(new RxPermissions(mContext).ensure(Manifest.permission.CAMERA))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                        if (aBoolean) {
                            Toast.makeText(mContext,"获取摄像头权限成功",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext,"获取摄像头权限失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        RxView.clicks(text2).throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(new RxPermissions(mContext).ensure(Manifest.permission.CALL_PHONE))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {

                        if (aBoolean) {
                            Toast.makeText(mContext,"获取打电话权限成功",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext,"获取打电话权限失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
