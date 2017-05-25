package com.safframework.saf.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tony Shen on 2017/5/25.
 */

public class BindingFragment extends Fragment {

    private final LifecyclePublisher lifecyclePublisher = new LifecyclePublisher();

    public BindingFragment() {
    }

    public LifecyclePublisher getLifecyclePublisher() {
        return lifecyclePublisher;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecyclePublisher.onAttach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lifecyclePublisher.onAttach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecyclePublisher.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lifecyclePublisher.onCreateView();
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecyclePublisher.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecyclePublisher.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecyclePublisher.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecyclePublisher.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecyclePublisher.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecyclePublisher.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecyclePublisher.onDetach();
    }
}
