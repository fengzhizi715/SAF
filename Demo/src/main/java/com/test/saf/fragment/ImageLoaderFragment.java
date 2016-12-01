package com.test.saf.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.test.saf.R;
import com.test.saf.adapter.ImageLoaderAdapter;
import com.test.saf.app.BaseFragment;
import com.test.saf.domain.MMPicsResponse;

import java.io.IOException;

import cn.salesuite.saf.http.rest.RestClient;
import cn.salesuite.saf.http.rest.RestUtil;
import cn.salesuite.saf.inject.Injector;
import cn.salesuite.saf.inject.annotation.InjectView;
import cn.salesuite.saf.log.L;
import cn.salesuite.saf.rxjava.RxAsyncTask;
import cn.salesuite.saf.utils.Preconditions;

/**
 * Created by Tony Shen on 2016/11/22.
 */

public class ImageLoaderFragment extends BaseFragment {

    @InjectView
    RecyclerView recyclerview;

    @InjectView(id=R.id.menu_labels_right)
    FloatingActionMenu faMenu;

    @InjectView
    FloatingActionButton fab1;

    MMPicsResponse respnose;

    int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_imageloader, container, false);
        Injector.injectInto(this, v);

        initViews();
        initData();

        return v;
    }

    private void initViews() {
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                String url = "http://www.tngou.net/tnfs/api/list"+"?page="+page;
                new GetPicTask(url)
                        .success(new RxAsyncTask.SuccessHandler<String>() {
                            @Override
                            public void onSuccess(String content) {
                                try {
                                    respnose = RestUtil.parseAs(MMPicsResponse.class, content);
                                    if (respnose != null && Preconditions.isNotBlank(respnose.tngou)) {
                                        recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2));
                                        recyclerview.setAdapter(new ImageLoaderAdapter(mContext, respnose.tngou));
                                        if (faMenu.isOpened()) {
                                            faMenu.close(true);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).failed(new RxAsyncTask.FailedHandler() {
                    @Override
                    public void onFail(Throwable e) {
                        L.e(e.getMessage());
                    }
                });
            }
        });
    }

    private void initData() {

        new GetPicTask("http://www.tngou.net/tnfs/api/news")
                .success(new RxAsyncTask.SuccessHandler<String>() {
            @Override
            public void onSuccess(String content) {
                try {
                    respnose = RestUtil.parseAs(MMPicsResponse.class, content);
                    if (respnose != null && Preconditions.isNotBlank(respnose.tngou)) {
                        recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2));
                        recyclerview.setAdapter(new ImageLoaderAdapter(mContext, respnose.tngou));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).failed(new RxAsyncTask.FailedHandler() {
            @Override
            public void onFail(Throwable e) {
                L.e(e.getMessage());
            }
        });
    }

    class GetPicTask extends RxAsyncTask<String> {

        private String apiUrl;

        public GetPicTask(String url) {
            this.apiUrl = url;
        }

        @Override
        public String onExecute() {
            return RestClient.get(apiUrl).body();
        }
    }
}
