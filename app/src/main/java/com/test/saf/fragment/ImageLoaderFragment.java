package com.test.saf.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.injectview.annotations.OnClick;
import com.safframework.log.L;
import com.safframework.saf.async.RxAsyncTask;
import com.safframework.saf.recyclerview.OnItemClickListener;
import com.safframework.saf.rest.RestClient;
import com.safframework.saf.rest.RestUtil;
import com.safframework.saf.rest.UrlBuilder;
import com.safframwork.tony.common.utils.Preconditions;
import com.test.saf.R;
import com.test.saf.activity.ImageDetailActivity;
import com.test.saf.adapter.DividerGridItemDecoration;
import com.test.saf.adapter.ImageLoaderAdapter;
import com.test.saf.app.BaseFragment;
import com.test.saf.config.Config;
import com.test.saf.domain.MMPicsResponse;
import com.test.saf.ui.GridRecyclerView;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Tony Shen on 2016/11/22.
 */

public class ImageLoaderFragment extends BaseFragment {

    @InjectView(R.id.recyclerview)
    GridRecyclerView recyclerview;

    @InjectView(R.id.menu_labels_right)
    FloatingActionMenu faMenu;

    MMPicsResponse respnose;

    int page = 1;

    ProgressDialog progDailog;

    ImageLoaderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_imageloader, container, false);
        Injector.injectInto(this, v);

        initViews();
        initData();

        return v;
    }

    private void initViews() {

        progDailog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);

        recyclerview.addOnItemTouchListener(new OnItemClickListener(recyclerview){

            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                if (respnose != null && Preconditions.isNotBlank(respnose.tngou)) {
                    MMPicsResponse.Pic pic =respnose.tngou.get(position);
                    if (pic!=null && Preconditions.isNotBlank(pic.img)) {
                        Intent intent = new Intent(mContext, ImageDetailActivity.class);
                        intent.putExtra("image", pic.img);
                        startActivity(intent);
                        mContext.overridePendingTransition(0, 0);
                    }
                }
            }

            @Override
            public void onLongPress(RecyclerView.ViewHolder holder, int position) {

            }
        });
    }

    @OnClick(id={R.id.fab1})
    void clickFab1() {
        page++;

        // 封装url
        UrlBuilder urlBuilder = new UrlBuilder("http://www.tngou.net/tnfs/api/list");
        urlBuilder.parameter("page", page);
        String url = urlBuilder.buildUrl();

        newGetPicTask(url);

        if (faMenu.isOpened()) {
            faMenu.close(true);
        }
    }

    @OnClick(id={R.id.fab2})
    void clickFab2() {
        if (page<=1) {
            toast("前一批无美女，老板要么换下一批？");

            if (faMenu.isOpened()) {
                faMenu.close(true);
            }

            return;
        }

        page--;

        // 封装url
        UrlBuilder urlBuilder = new UrlBuilder("http://www.tngou.net/tnfs/api/list");
        urlBuilder.parameter("page", page);
        String url = urlBuilder.buildUrl();

        newGetPicTask(url);

        if (faMenu.isOpened()) {
            faMenu.close(true);
        }
    }

    private void initData() {

        if (mCache.getObject(Config.FIRST_PICS)!=null) {
            GridLayoutManager manager = new GridLayoutManager(mContext,2);
            manager.setRecycleChildrenOnDetach(true);

            adapter = new ImageLoaderAdapter(mContext, (List<MMPicsResponse.Pic>) mCache.getObject(Config.FIRST_PICS));
            recyclerview.setLayoutManager(manager);
            recyclerview.setAdapter(adapter);
            recyclerview.addItemDecoration(new DividerGridItemDecoration(mContext));
            recyclerview.setRecycledViewPool(myPool);
        }

        String url = "http://www.tngou.net/tnfs/api/news";
        newGetPicTask(url);
    }

    private void newGetPicTask(final String url) {

        if (progDailog != null && !progDailog.isShowing()) {
            progDailog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
            progDailog.setCancelable(false);
        }

        new GetPicTask(url,progDailog)
                .retry(3) // 如果请求有失败，会重试三次
                .success(new RxAsyncTask.SuccessHandler<String>() {
                    @Override
                    public void onSuccess(String content) {
                        try {
                            respnose = RestUtil.parseAs(MMPicsResponse.class, content);
                            if (respnose != null && Preconditions.isNotBlank(respnose.tngou)) {
                                GridLayoutManager manager = new GridLayoutManager(mContext,2);
                                manager.setRecycleChildrenOnDetach(true);

                                adapter = new ImageLoaderAdapter(mContext, respnose.tngou);
                                recyclerview.setLayoutManager(manager);
                                recyclerview.setAdapter(adapter);
                                recyclerview.addItemDecoration(new DividerGridItemDecoration(mContext));
                                recyclerview.setRecycledViewPool(myPool);

                                if ("http://www.tngou.net/tnfs/api/news".equals(url)) {
                                    mCache.put(Config.FIRST_PICS,(Serializable) respnose.tngou);
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
        }).start();
    }

    /**
     * 获取美女图片的task
     */
    class GetPicTask extends RxAsyncTask<String> {

        private String apiUrl;

        public GetPicTask(String url,Dialog dialog) {
            super(dialog);
            this.apiUrl = url;
        }

        @Override
        public String onExecute() {
            return RestClient.get(apiUrl).body();
        }
    }

    static RecyclerView.RecycledViewPool myPool = new RecyclerView.RecycledViewPool();
    static{
        myPool.setMaxRecycledViews(0, 10);
    }
}